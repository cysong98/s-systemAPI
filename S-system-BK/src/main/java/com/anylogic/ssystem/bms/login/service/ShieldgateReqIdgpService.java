package com.anylogic.ssystem.bms.login.service;


import com.anylogic.ssystem.bms.com.model.*;
import com.anylogic.ssystem.bms.login.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ShieldgateReqIdgpService {

    private final WebClient poolWebClientForOauth;
    private final ObjectMapper objectMapper;

    @Value("${ssystem-redirect}")
    private String shieldGateServerApiRedirectUrl;

    @Value("${ssystem-client-key}")
    private String shieldGateServerApiClientCredentialClientId;

    @Value("${ssystem-secret-key}")
    private String shieldGateServerApiClientCredentialClientSecret;

    @Value("${company-id}")
    private String extra;

    @Value("${master-tenant}")
    private String shieldGateServerApiMasterExtra;

    private static final String URI_TOKEN_REQ = "/SCCloudOAuthService/common/oauth/token";
    private static final String URI_LOGOUT_REQ = "/SCCloudOAuthService/@extra@/logout";
    private static final String URI_KEY_REQ = "/SCCloudOAuthService/@extra@/company/getCompanyPublicKeyAllList";
    private static final String URI_RELAY_TOKEN_REQ = "/SCCloudOAuthService/@extra@/relayClientAccessToken";
    private static final String REPLACE_KEY_EXTRA = "@extra@";
    private static final String HEADER_KEY_AUTHORIZATION = "Authorization";
    private static final String HEADER_PREFIX_BASIC = "Basic ";
    private static final String HEADER_PREFIX_BEARER = "Bearer ";
    private static final String BODY_KEY_EXTRA = "extra";
    private static final String BODY_KEY_GRANT_TYPE = "grant_type";
    private static final String BODY_KEY_REDIRECT_URI = "redirect_uri";
    private static final String BODY_KEY_REFRESH_TOKEN = "refresh_token";


    /*
     * RSA 암호화 중 공개키를 얻는 과정에서 필요한 메소드
     * 모든 회사의 공개키 목록을 조회한다.
     * 모든 회사 : IdGP 콘솔에 등록되어 있는, 서비스를 사용중인 회사 목록
     */
    public List<ResOauthPublicKeyDto> getCompanyPublicKeyAllList(String token) {
        List<ResOauthPublicKeyDto> results = new ArrayList<>();

        try {
            String authorization = HEADER_PREFIX_BEARER + token;
            String reqUrl = URI_KEY_REQ.replace(REPLACE_KEY_EXTRA, extra);
            String apiResult = poolWebClientForOauth.get()
                    .uri(reqUrl)
                    .header(HEADER_KEY_AUTHORIZATION, authorization)
                    .retrieve()
                    .bodyToMono(String.class)
                    .retryWhen(Retry.backoff(2, Duration.ofSeconds(2)).jitter(0.75))
                    .onErrorResume(e -> {
                        log.error(e.getMessage());
                        return Mono.just(null);
                    })
                    .block();
            if (apiResult != null) {
                results = objectMapper.readValue(apiResult, new TypeReference<List<ResOauthPublicKeyDto>>() {});
            }

        } catch (Exception e) {
            log.error("getPublicKeys error : {}", e.getLocalizedMessage());
        }
        log.info("getPublicKeys size : {}", results.size());
        return results;
    }

    /* =================================== IdGP Login API =================================== */
    /* ====================================================================================== */
    /*
     * access_token을 취득하기 위한 API
     * Client Credentials 인증방식 : 앱 서비스의 client_key와 secret_key(application.yml에 저장)만으로 토큰을 취득함
     * access_token : IdGP API를 사용하기 위해 기본적으로 필요한 토큰
     */
    public ResOauthDto getOauthTokenByClientCredential() {
        ResOauthDto result;
        String authorization = shieldGateServerApiClientCredentialClientId + ":" + shieldGateServerApiClientCredentialClientSecret;
        authorization = HEADER_PREFIX_BASIC + Base64.getEncoder().encodeToString(authorization.getBytes());

        result = poolWebClientForOauth.post()
                .uri(URI_TOKEN_REQ)
                .header(HEADER_KEY_AUTHORIZATION, authorization)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(BODY_KEY_EXTRA, extra)
                        .with(BODY_KEY_GRANT_TYPE, "client_credentials"))
                .retrieve()
                .bodyToMono(ResOauthDto.class)
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(2)).jitter(0.75))
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(null);
                })
                .block();
        return result;
    }


    /*
     * jwt을 취득하기 위한 API
     * Authorization Code 인증방식 : 사용자의 로그인 정보를 필요로 하는 인증 방식
     *                              frontend에서 이미 사용자 정보를 검증하고 이 메소드가 호출될 때 그 인증 표시를 code라는 변수로 넘겨받음
     * jwt : 사용자 정보를 frontend로 넘겨주기전에 정상적인 호출인지 한번 더 검증하는데에 사용됨
     */
    public ResOauthDto getOauthTokenByCodeAndRedirectAndExtra(ReqOauthDto param) {
        ResOauthDto result = null;
        try {
            String grantType = "authorization_code";
            String authorization = shieldGateServerApiClientCredentialClientId + ":" + shieldGateServerApiClientCredentialClientSecret;
            authorization = HEADER_PREFIX_BASIC + Base64.getEncoder().encodeToString(authorization.getBytes());

            result = poolWebClientForOauth.post()
                    .uri(URI_TOKEN_REQ)
                    .header(HEADER_KEY_AUTHORIZATION, authorization)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("code", param.getCode())
                            .with(BODY_KEY_EXTRA, extra)
                            .with(BODY_KEY_GRANT_TYPE, grantType)
                            .with(BODY_KEY_REDIRECT_URI, shieldGateServerApiRedirectUrl))
                    .retrieve()
                    .bodyToMono(ResOauthDto.class)
                    .onErrorResume(e -> {
                        log.error(e.getMessage());
                        return Mono.just(null);
                    })
                    .block();
        } catch (Exception e) {
            log.error("getOauthTokenByCodeAndRedirectAndExtra error : {}", e.getLocalizedMessage());
        }
        return result;
    }


    /*
     * Authorization Code 인증 방식에서 얻은 refresh_token 값을 이용해서,
     * access_token이 만료되었을 시 토큰을 재발급한다.
     * refresh_token 마저 만료가 되었는 경우, 빈 값을 반환한다.
     */
    public ResOauthDto getOauthTokenByRefreshToken(ReqOauthDto param) {
        ResOauthDto result = null;
        try {
            String authorization = shieldGateServerApiClientCredentialClientId + ":" + shieldGateServerApiClientCredentialClientSecret;
            authorization = HEADER_PREFIX_BASIC + Base64.getEncoder().encodeToString(authorization.getBytes());

            result = poolWebClientForOauth.post()
                    .uri(URI_TOKEN_REQ)
                    .header(HEADER_KEY_AUTHORIZATION, authorization)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(BODY_KEY_EXTRA, extra)
                            .with(BODY_KEY_GRANT_TYPE, BODY_KEY_REFRESH_TOKEN)
                            .with(BODY_KEY_REFRESH_TOKEN, param.getRefreshToken()))
                    .retrieve()
                    .bodyToMono(ResOauthDto.class)
                    .onErrorResume(e -> {
                        log.error(e.getMessage());
                        return Mono.just(null);
                    })
                    .block();
        } catch (Exception e) {
            log.error("getOauthTokenByRefreshToken error : {}", e.getLocalizedMessage());
        }
        return result;
    }


    // 사용자 로그아웃
    public int logOut(TokenLogoutDto param) {

        String reqUrl = URI_LOGOUT_REQ.replace(REPLACE_KEY_EXTRA, extra);
        String authorization = HEADER_PREFIX_BEARER + param.getAccessToken();

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("client_id", shieldGateServerApiClientCredentialClientId);
        bodyMap.put("extra", extra);
        bodyMap.put("username", param.getUserAccount());

        try {
            poolWebClientForOauth.post()
                    .uri(reqUrl)
                    .header(HEADER_KEY_AUTHORIZATION, authorization)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(bodyMap)
                    .retrieve()
                    .bodyToMono(String.class)
                    .retryWhen(Retry.backoff(2, Duration.ofSeconds(2)).jitter(0.75))
                    .onErrorResume(e -> {
                        log.error(e.getMessage());
                        return Mono.just(null);
                    })
                    .block();
        } catch (Exception e) {
            log.error("oauth log out error : {}", e.getLocalizedMessage());
        }
        return 1;
    }


    /* ================================ 사용자 추가, 삭제, 수정 ================================ */
    /* ====================================================================================== */

    /*
     * 사용자 정보를 IdGP API 서버 상에도 추가한다.
     * 사용자명, 사용자 Email(=사용자 ID), 패스워드, 사용허가여부(true/false)
     */
    public void addUserInfo(InsertUserBIAReqVO param, ResOauthDto resOauthDto) {
        try {
            String authorization = resOauthDto.getAccess_token();
            authorization = "Bearer " + authorization;

            String customUserType = param.getUserid() + ":" + param.getPassword();      // param.getUserid() == IdGP.email
            List<ResOauthPublicKeyDto> publicKeyList = getCompanyPublicKeyAllList(resOauthDto.getAccess_token());
            for(ResOauthPublicKeyDto dto : publicKeyList)
                ShieldgateValidateService.setPublicKey(dto.getCompanyId(), dto.getCompanyRSAPubKey());

            byte[] publicKeyByteArray = ShieldgateValidateService.lookupVerificationKey(extra);
            X509EncodedKeySpec keySpecDec = null;
            KeyFactory kf = null;
            PublicKey publicKey =  null;

            kf = KeyFactory.getInstance("RSA");
            keySpecDec = new X509EncodedKeySpec(publicKeyByteArray);
            publicKey = kf.generatePublic(keySpecDec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] plainTextByte = cipher.doFinal(customUserType.getBytes());

            String finalUserTypeData = Base64.getEncoder().encodeToString(customUserType.getBytes());

            // for Request Body
            List<String> role = new ArrayList<>();
            role.add("USER");

            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("email", param.getUserid());
            bodyMap.put("companyid", extra);
            bodyMap.put("username", param.getUsername());
            bodyMap.put("role", role);
            bodyMap.put("use", true);
            bodyMap.put("customUserType", finalUserTypeData);

            poolWebClientForOauth.post()
                    .uri("/SCCloudOAuthService/" + extra + "/manage/addUserInfo")
                    .header("Authorization", authorization)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(bodyMap)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(e -> {
                        log.error(e.getMessage());
                        return Mono.just(null);
                    })
                    .block();
        } catch (Exception e) {
            log.error("addUserInfo error : {}", e.getLocalizedMessage());
        }
    }


    /*
     * IdGP 상 해당하는 사용자 정보를 삭제한다.
     */
    public void deleteUserInfo(List<DeleteUserBIAReqVO> params, ResOauthDto resOauthDto) {
        try {
            String authorization = resOauthDto.getAccess_token();
            authorization = "Bearer " + authorization;

            List<Map<String, Object>> bodyList = new ArrayList<>();
            for(DeleteUserBIAReqVO param : params) {
                Map<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("companyid", extra);
                bodyMap.put("userid", param.getUserid());
                bodyList.add(bodyMap);
            }

            poolWebClientForOauth.post()
                    .uri("/SCCloudOAuthService/" + extra + "/manage/deleteUserInfo")
                    .header("Authorization", authorization)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(bodyList)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(e -> {
                        log.error(e.getMessage());
                        return Mono.just(null);
                    })
                    .block();
        } catch (Exception e) {
            log.error("deleteUserInfo error : {}", e.getLocalizedMessage());
        }
    }


    /*
     * IdGP 상 해당하는 사용자 정보를 갱신한다. (패스워드 제외)
     */
    public void updateUserInfo(UpdateUserBIAReqVO param, ResOauthDto resOauthDto) {
        try {
            String authorization = resOauthDto.getAccess_token();
            authorization = "Bearer " + authorization;

            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("email", param.getUserid());
            bodyMap.put("userid", param.getUserid());
            bodyMap.put("companyid", extra);
            bodyMap.put("username", param.getUsername());

            List<Map<String, Object>> bodyList = new ArrayList<>();
            bodyList.add(bodyMap);
            poolWebClientForOauth.post()
                    .uri("/SCCloudOAuthService/" + extra + "/manage/updateUserInfo")
                    .header("Authorization", authorization)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(bodyList)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(e -> {
                        log.error(e.getMessage());
                        return Mono.just(null);
                    })
                    .block();
        } catch (Exception e) {
            log.error("updateUserInfo error : {}", e.getLocalizedMessage());
        }
    }

    /*
     * IdGP 상 해당하는 사용자 정보(패스워드)를 갱신한다.
     */
    public void updateUserPassword(GetUserDetailResVO currentUserPwVO, UpdateUserBIAReqVO param, ResOauthDto resOauthDto) {
        try {
            String authorization = resOauthDto.getAccess_token();
            authorization = "Bearer " + authorization;
            String customUserType = currentUserPwVO.getPassword() + ":" + param.getPassword();

            List<ResOauthPublicKeyDto> publicKeyList = getCompanyPublicKeyAllList(resOauthDto.getAccess_token());
            for(ResOauthPublicKeyDto dto : publicKeyList)
                ShieldgateValidateService.setPublicKey(dto.getCompanyId(), dto.getCompanyRSAPubKey());
            byte[] publicKeyByteArray = ShieldgateValidateService.lookupVerificationKey(extra);
            X509EncodedKeySpec keySpecDec = null;
            KeyFactory kf = null;
            PublicKey publicKey =  null;

            kf = KeyFactory.getInstance("RSA");
            keySpecDec = new X509EncodedKeySpec(publicKeyByteArray);
            publicKey = kf.generatePublic(keySpecDec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] plainTextByte = cipher.doFinal(customUserType.getBytes());

            String finalUserTypeData = Base64.getEncoder().encodeToString(customUserType.getBytes());

            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("email", param.getUserid());
            bodyMap.put("companyid", extra);
            bodyMap.put("customUserType", finalUserTypeData);

            poolWebClientForOauth.post()
                    .uri("/SCCloudOAuthService/" + extra + "/manage/v2/updateUserPassword")
                    .header("Authorization", authorization)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(bodyMap)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(e -> {
                        log.error(e.getMessage());
                        return Mono.just(null);
                    })
                    .block();
        } catch (Exception e) {
            log.error("updateUserInfo error : {}", e.getLocalizedMessage());
        }
    }


    /* ================================= 그룹 추가, 삭제, 수정 ================================= */
    /* ====================================================================================== */

    /*
     * 새로 추가되는 조직 정보를 IdGP 상의 그룹 정보에 추가한다.
     */
    public void addGroupInfo(InsertOrganizationBIAReqVO param, ResOauthDto resOauthDto) {
        try {
            String authorization = resOauthDto.getAccess_token();
            authorization = "Bearer " + authorization;

            Map<String, String> bodyMap = new HashMap<>();
            bodyMap.put("companyId", extra);
            bodyMap.put("groupName", param.getOrgname());

            poolWebClientForOauth.post()
                    .uri("/SCCloudOAuthService/" + extra + "/group/addGroupInfo")
                    .header("Authorization", authorization)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(bodyMap)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(e -> {
                        log.error(e.getMessage());
                        return Mono.just(null);
                    })
                    .block();
        } catch (Exception e) {
            log.error("addUserInfo error : {}", e.getLocalizedMessage());
        }
    }

    /*
     * IdGP 상 저장되어있는 그룹 정보 중 조건에 해당하는 것을 삭제한다.
     */
    public void delGroupInfo(DeleteOrganizationBIAReqVO param, ResOauthDto resOauthDto) {
        try {
            String authorization = resOauthDto.getAccess_token();
            authorization = "Bearer " + authorization;

            String groupId = "";
            GroupInfoDto[] list = getGroupByCompanyIdList(resOauthDto.getAccess_token());
            for(GroupInfoDto dto : list) {
                if(dto.getGroupName().equals(param.getOrgname())) {
                    groupId = dto.getGroupId();
                    break;
                }
            }

            String fullURI = "/SCCloudOAuthService/" + extra + "/group/delGroupInfo?companyId=" + extra + "&groupId=" + groupId;
            List<Map<String, Object>> bodyList = new ArrayList<>();
            poolWebClientForOauth.get()
                    .uri(fullURI)
                    .header("Authorization", authorization)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(e -> {
                        log.error(e.getMessage());
                        return Mono.just(null);
                    })
                    .block();
        } catch (Exception e) {
            log.error("deleteUserInfo error : {}", e.getLocalizedMessage());
        }
    }


    /*
     * 사용자가 속한 그룹 정보를 갱신한다.
     * 사용자의 소속 조직이 바뀌었을 시 그에 따라 사용자 정보의 그룹 정보를 갱신하는 것
     */
    public void updateGroupInfo(UpdateUserBIAReqVO param, ResOauthDto resOauthDto) {
        try {
            String authorization = resOauthDto.getAccess_token();
            authorization = "Bearer " + authorization;

            List<String> groupIdList = new ArrayList<>();
            GroupInfoDto[] list = getGroupByCompanyIdList(resOauthDto.getAccess_token());
            for(GroupInfoDto dto : list) {
                if(dto.getGroupName().equals(param.getDeptname())) {
                    groupIdList.add(dto.getGroupId());
                    break;
                }
            }

            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("email", param.getUserid());
            bodyMap.put("companyId", extra);
            bodyMap.put("groupIdList", groupIdList);
            poolWebClientForOauth.post()
                    .uri("/SCCloudOAuthService/" + extra + "/manage/v2/updateGroupInfo")
                    .header("Authorization", authorization)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(bodyMap)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(e -> {
                        log.error(e.getMessage());
                        return Mono.just(null);
                    })
                    .block();
        } catch (Exception e) {
            log.error("updateUserInfo error : {}", e.getLocalizedMessage());
        }
    }


    /*
     * IdGP 상 저장되어있는 그룹 정보 중 조건에 해당하는 것의 정보를 갱신한다.
     * 특정 그룹에 대한 자체 정보를 갱신하는 것
     */
    public void updateGroup(UpdateOrganizationBIAReqVO param, ResOauthDto resOauthDto) {
        try {
            String authorization = resOauthDto.getAccess_token();
            authorization = "Bearer " + authorization;

            String groupId = "";
            GroupInfoDto[] list = getGroupByCompanyIdList(resOauthDto.getAccess_token());
            for(GroupInfoDto dto : list) {
                if(dto.getGroupName().equals(param.getPreOrgname())) {
                    groupId = dto.getGroupId();
                    break;
                }
            }

            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("companyId", extra);
            bodyMap.put("groupId", groupId);
            bodyMap.put("groupName", param.getOrgname());
            poolWebClientForOauth.post()
                    .uri("/SCCloudOAuthService/" + extra + "/group/updateGroupInfo")
                    .header("Authorization", authorization)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(bodyMap)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(e -> {
                        log.error(e.getMessage());
                        return Mono.just(null);
                    })
                    .block();
        } catch (Exception e) {
            log.error("updateUserInfo error : {}", e.getLocalizedMessage());
        }
    }


    /*
     * 사용자가 속한 그룹 정보를 얻기 위해 해당 회사에 존재하는 모든 그룹 정보 목록을 조회합니다.
     */
    private GroupInfoDto[] getGroupByCompanyIdList(String token) {
        GroupInfoDto[] dto = null;
        try {
            String authorization = "Bearer " + token;
            String fullURI = "/SCCloudOAuthService/" + extra + "/group/getGroupByCompanyIdListResultGroupIdGroupName?companyId=" + extra;

            dto = poolWebClientForOauth.get()
                    .uri(fullURI)
                    .header("Authorization", authorization)
                    .retrieve()
                    .bodyToMono(GroupInfoDto[].class)
                    .onErrorResume(e -> {
                        log.error(e.getMessage());
                        return Mono.just(null);
                    })
                    .block();
        } catch (Exception e) {
            log.error("deleteUserInfo error : {}", e.getLocalizedMessage());
        }
        return dto;
    }
}
