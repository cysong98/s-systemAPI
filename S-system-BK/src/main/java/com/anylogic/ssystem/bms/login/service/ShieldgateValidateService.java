package com.anylogic.ssystem.bms.login.service;


import com.anylogic.ssystem.bms.login.model.ResOauthDto;
import com.anylogic.ssystem.bms.login.model.ResOauthPublicKeyDto;
import com.anylogic.ssystem.bms.login.model.TokenExpDto;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ShieldgateValidateService {

    private ShieldgateReqIdgpService shieldgateReqIdgpService;
    private final ObjectMapper objectMapper;

    // 공개키 목록
    private static HashMap<String, byte[]> rsaPublicKeys = new HashMap<>();

    // client_credentials 토큰 목록
    private HashMap<String, String> clientCredentialJwtTokens = new HashMap<>();
    private HashMap<String, String> clientCredentialAccessTokens = new HashMap<>();

    @Value("${ssystem-client-key}")
    private String shieldGateServerApiClientCredentialClientId;

    @Value("${ssystem-secret-key}")
    private String shieldGateServerApiClientCredentialClientSecret;

    @Value("${master-tenant}")
    private String shieldGateServerApiMasterExtra;


    // 미사용
    public String getClientCredentialAccessToken() {
        refreshClientCredential();      // 토큰 최신화
        return clientCredentialAccessTokens.get(shieldGateServerApiClientCredentialClientId);   // accessToken 리턴
    }

    // 미사용
    public String getClientCredentialJwtToken() {
        refreshClientCredential();      // 토큰 최신화
        return clientCredentialJwtTokens.get(shieldGateServerApiClientCredentialClientId);  // jwtToken 리턴
    }

    // parseJWTByOauthToken를 호출하여 토큰을 반환하면 true, null이면 false 반환
    public boolean validatedToken(String token) {
        boolean result = false;
        Claims claims = parseJWTByOauthToken(token);

        if (claims != null)
            result = true;
        return result;
    }


    /*
     * 올바른 토큰인지 jwt를 검증합니다.
     * 모든 회사의 companyid와 jwt 헤더의 keyid(=companyid)를 비교하여 일치하는 것이 있는 경우 해당 토큰을 반환합니다.
     * null 반환 = 검증 실패
     */
    public Claims parseJWTByOauthToken(String jwt) {

        Claims claims = null;
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                        @Override
                        public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
                            byte[] publicKeyByteArray = null;

                            //authKeyList  안에 jwsHeader.getKeyId() 라는 companyid 의 공개키가 있으면 가져온다.
                            publicKeyByteArray = lookupVerificationKey(jwsHeader.getKeyId());
                            X509EncodedKeySpec keySpecDec = null;
                            KeyFactory kf = null;
                            PublicKey pubKey =  null;
                            try {
                                kf = KeyFactory.getInstance("RSA");
                                keySpecDec = new X509EncodedKeySpec(publicKeyByteArray);
                                pubKey = kf.generatePublic(keySpecDec);
                            } catch (NoSuchAlgorithmException e) {
                                log.info("jwsHeader NoSuchAlgorithmException e : " + e);
                            } catch (InvalidKeySpecException e) {
                                log.info("jwsHeader InvalidKeySpecException e : " + e);
                            }

                            return pubKey;
                        }
                    })
                    .build()
                    .parseClaimsJws(jwt);
            claims = jws.getBody();

        } catch (Exception e) {
            throw new AnyXException("JWT parsing error");
        }
        return claims;
    }

    /*
     * 모든 회사의 companyid와 주어진 keyId 파라미터를 비교하여 일치하는 것이 있다면 반환한다.
     * 모든 회사 : IdGP 콘솔에 등록되어 있는, 서비스를 사용중인 회사 목록
     */
    public static byte[] lookupVerificationKey(String keyId) {
        try {
            for (String extra : rsaPublicKeys.keySet()) {
                if (extra.equals(keyId)) {
                    return rsaPublicKeys.get(extra);
                }
            }
        } catch (Exception e) {
            log.info("JwtService.lookupVerificationKey lookupVerificationKey error :" +  e.getMessage());
        }
        log.info("lookupVerificationKey error : key not found");
        return null;
    }


    public static void setPublicKey(String extra, byte[] publicKey) {
        rsaPublicKeys.put(extra, publicKey);
    }
    public static byte[] getPublicKey(String extra) {
        return rsaPublicKeys.get(extra);
    }

    // 미사용
    private void setClientCredentialTokenForMemory(String clientId, String accessToken, String jwtToken) {
        clientCredentialJwtTokens.put(clientId, jwtToken);
        clientCredentialAccessTokens.put(clientId, accessToken);
    }


    // client_credential check
    // 미사용
    private synchronized void refreshClientCredential() {
        // 토큰 유효성 체크를 위해 토큰 취득
        String accessToken = clientCredentialAccessTokens.get(shieldGateServerApiClientCredentialClientId);
        String jwtToken = clientCredentialJwtTokens.get(shieldGateServerApiClientCredentialClientId);

        // 메모리에 토큰 정보 존재하지 않을 경우 인증 / 인가에 발급 요청
        if (accessToken == null || jwtToken == null) {
            ResOauthDto resOauthDto = shieldgateReqIdgpService.getOauthTokenByClientCredential();

            if (resOauthDto != null) {
                accessToken = resOauthDto.getAccess_token();
                jwtToken = resOauthDto.getJwt();

                // 메모리에 저장
                setClientCredentialTokenForMemory(shieldGateServerApiClientCredentialClientId, accessToken, jwtToken);
            }
        }
    }
}
