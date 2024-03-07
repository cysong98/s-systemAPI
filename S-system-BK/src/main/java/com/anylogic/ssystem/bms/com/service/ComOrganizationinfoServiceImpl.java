/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.service;


import com.anylogic.ssystem.bms.com.mapper.*;
import com.anylogic.ssystem.bms.com.model.*;
import com.anylogic.ssystem.bms.dct.model.ReqVO;
import com.anylogic.ssystem.bms.login.model.ResOauthDto;
import com.anylogic.ssystem.bms.login.service.ShieldgateReqIdgpService;
import com.anylogic.ssystem.common.file.model.AttachFileVO;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Slf4j
@RequiredArgsConstructor
@Service("ComOrganizationinfoService")
@Transactional
public class ComOrganizationinfoServiceImpl implements ComOrganizationinfoService {

    @Autowired
    private ComOrganizationinfoMapper comOrganizationinfoMapper;

    @Autowired
    private ComOrgDetailMapper comOrgDetailMapper;

    @Autowired
    private ComUserinfoMapper comUserinfoMapper;

    @Autowired
    private ComVgroupUserMapper comVgroupUserMapper;

    @Autowired
    private ShieldgateReqIdgpService shieldgateReqIdgpService;


    /* ======================================== 부서관리 ======================================== */

    /* 부서 정보 TREE 조회 */
    @Override
    public PageInfo<SelectOrganizationListResVO> selectOrganizationList(SelectOrganizationListReqVO param){
        PageInfo<SelectOrganizationListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(comOrganizationinfoMapper.selectOrganizationList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 조직관리 > 부서/사용자관리 > 부서 삭제 */
    @Override
    public List<Integer> deleteOrganizationBIA(List<DeleteOrganizationBIAReqVO> params){
        List<Integer> res = new ArrayList<Integer>();
        try {
            List<Integer> resDeleteOrganizationDetail = new ArrayList<Integer>();
            for(DeleteOrganizationBIAReqVO param : params) {
                /* 사용자 존재 여부 검사 */
                List<SelectUserListResVO> list = comUserinfoMapper.selectUserList(SelectUserListReqVO.builder()
                                                                                    .deptid(param.getOrgid())
                                                                                    .build());
                if(!list.isEmpty()) {
                    res.add(0, -1);
                    return res;
                }
            }

            for(DeleteOrganizationBIAReqVO param : params){
                /* 부서 상세 내용 삭제 */
                resDeleteOrganizationDetail.add(comOrgDetailMapper.deleteOrganizationDetail(DeleteOrganizationDetailReqVO.builder()
                                                                                                            .orgid(param.getOrgid())
                                                                                                            .build()));
                /* 부서 삭제 처리 */
                int ares = comOrganizationinfoMapper.deleteOrganizationBIA(param);

                /* IdGP deleteUserInfo */
                ResOauthDto resOauthDto = shieldgateReqIdgpService.getOauthTokenByClientCredential();
                shieldgateReqIdgpService.delGroupInfo(param, resOauthDto);
                res.add(ares);
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }


    /* 조직관리 > 부서/사용자관리 > 부서 상세 조회 */
    @Override
    public GetOrganizationDetailResVO getOrganizationDetail(GetOrganizationDetailReqVO param){
        GetOrganizationDetailResVO resGetOrganizationDetail;
        try {
            resGetOrganizationDetail = comOrganizationinfoMapper.getOrganizationDetail(param);

        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resGetOrganizationDetail == null){
            resGetOrganizationDetail = new GetOrganizationDetailResVO();
        }
        return resGetOrganizationDetail;
    }


    /* 조직관리 > 부서/사용자관리 > 부서 등록 */
    @Override
    public int insertOrganizationBIA(InsertOrganizationBIAReqVO param){
        log.debug("comOrganizationinfo insertOrganizationBIA" + param.toString());

        int resInsertOrganizationBIA = 0;
        try {
            /* orgdepth, toporgid 조회 */
            if(param.getParentorgid() != null && !param.getParentorgid().equals("")) {
                GetOrganizationDetailResVO parentOrg = comOrganizationinfoMapper.getOrganizationDetail(GetOrganizationDetailReqVO.builder()
                                                                                                            .orgid(param.getParentorgid())
                                                                                                            .build());
                param.setOrgdepth(parentOrg.getOrgdepth().add(BigDecimal.ONE));
                /* toporgid 칼럼 값 입력 */
                if(parentOrg.getOrgdepth().intValue() == 1)             // depth 최상위 index: 1
                    param.setToporgid(parentOrg.getOrgid());
                else
                    param.setToporgid(parentOrg.getToporgid());
            }
            else {
                param.setOrgdepth(BigDecimal.ONE);
            }

            /* IdGP addGroupInfo */
            ResOauthDto resOauthDto = shieldgateReqIdgpService.getOauthTokenByClientCredential();
            shieldgateReqIdgpService.addGroupInfo(param, resOauthDto);

            /* 부서 내용 등록 */
            resInsertOrganizationBIA = comOrganizationinfoMapper.insertOrganizationBIA(param);
            comOrgDetailMapper.insertOrganizationDetail(InsertOrganizationDetailReqVO.builder()
                                                                        .orgid(param.getOrgid())
                                                                        .fax(param.getFax())
                                                                        .telephone(param.getTelephone())
                                                                        .build());
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resInsertOrganizationBIA;
    }


    /* 조직관리 > 부서/사용자관리 > 부서 수정 */
    @Override
    public int updateOrganizationBIA(UpdateOrganizationBIAReqVO param){
        int resUpdateOrganizationBIA = 0;
        try {
            /* orgdepth, toporgid 조회 */
            if(param.getParentorgid() != null && !param.getParentorgid().equals("")) {
                GetOrganizationDetailResVO parentOrg = comOrganizationinfoMapper.getOrganizationDetail(GetOrganizationDetailReqVO.builder()
                                                                                                                .orgid(param.getParentorgid())
                                                                                                                .build());
                param.setOrgdepth(parentOrg.getOrgdepth().add(BigDecimal.ONE));
                /* depth 최상위 index: 1 */
                if(parentOrg.getOrgdepth().intValue() == 1)
                    param.setToporgid(parentOrg.getOrgid());
                else
                    param.setToporgid(parentOrg.getToporgid());
            }
            else {
                param.setOrgdepth(BigDecimal.ONE);
            }
            /* IdGP addGroupInfo */
            ResOauthDto resOauthDto = shieldgateReqIdgpService.getOauthTokenByClientCredential();
            shieldgateReqIdgpService.updateGroup(param, resOauthDto);

            /* 부서 내용 등록 */
            resUpdateOrganizationBIA = comOrganizationinfoMapper.updateOrganizationBIA(param);
            if(param.getFax() != null || param.getTelephone() != null)
                comOrgDetailMapper.updateOrganizationDetail(UpdateOrganizationDetailReqVO.builder()
                                                                    .orgid(param.getOrgid())
                                                                    .fax(param.getFax())
                                                                    .telephone(param.getTelephone())
                                                                    .build());

            /* 하위 사용자 부서 정보 변경 (부서명 변경 시) */
            if(!param.getOrgname().equals(param.getPreOrgname())) {
                List<SelectUserListResVO> list = comUserinfoMapper.selectUserList(SelectUserListReqVO.builder()
                                                                                        .deptid(param.getOrgid())
                                                                                        .build());
                for(SelectUserListResVO vo : list) {
                    comUserinfoMapper.updateUserBIA(UpdateUserBIAReqVO.builder()
                                                            .userid(vo.getUserid())
                                                            .deptname(param.getOrgname())
                                                            .build());
                }
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resUpdateOrganizationBIA;
    }


    /* 조직관리 > 부서/사용자관리 > 최상위부서 관리 팝업 > 부서 권한 수정 */
    @Override
    public int changeToporgVgroup(ChangeToporgVgroupReqVO param) {
        int res = 0;
        try {
            List<SelectUserListResVO> userList = comUserinfoMapper.selectUserList(SelectUserListReqVO.builder()
                                                                                    .deptid(param.getPredeptid())
                                                                                    .build());
            if(userList != null) {
                for (SelectUserListResVO user : userList) {
                    res += comVgroupUserMapper.deleteUserVgroup(DeleteUserVgroupReqVO.builder()
                                                                    .userid(user.getUserid())
                                                                    .delFlag(param.getVgroupid())
                                                                    .build());
                }
            }

            GetOrganizationDetailResVO vo = comOrganizationinfoMapper.getOrganizationDetail(GetOrganizationDetailReqVO.builder()
                                                                                                        .orgid(param.getDeptid())
                                                                                                        .build());
            if(!param.getVgroupid().equals("5210") && vo.getChiefid() != null) {
                /* 현재시간 출력 */
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String currentDateTime = LocalDateTime.now().format(formatter);
                res += comVgroupUserMapper.insertUserVgroup(InsertUserVgroupReqVO.builder()
                                                                    .userid(vo.getChiefid())
                                                                    .orgid(vo.getOrgid())
                                                                    .vgroupid(param.getVgroupid())
                                                                    .vgrouptype("a")
                                                                    .indt(currentDateTime)
                                                                    .build());
            }
        } catch(Exception e) {
            throw new AnyXException(e);
        }
        return res;
    }


    /* ======================================== 서명인관리 ======================================== */

    /* 조직관리 > 서명인관리 > 서명인 목록 조회 */
    @Override
    public PageInfo<SelectSignListResVO> selectSignList(SelectSignListReqVO param){
        PageInfo<SelectSignListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(comOrganizationinfoMapper.selectSignList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 조직관리 > 서명인관리 > 서명인 상세 조회 */
    @Override
    public GetSignDetailResVO getSignDetail(GetSignDetailReqVO param){
        GetSignDetailResVO resGetSignDetail;
        try {
            resGetSignDetail = comOrganizationinfoMapper.getSignDetail(param);
            if(resGetSignDetail != null && resGetSignDetail.getFileid() != null && !resGetSignDetail.getFileid().isEmpty()) {
                List<GetSignDetailSelectFileResVO> fileList = comOrganizationinfoMapper.getSignDetailSelectFile(GetSignDetailSelectFileReqVO.builder()
                                                                                                                .fileid(resGetSignDetail.getFileid())
                                                                                                                .build());
                resGetSignDetail.setFilelist(fileList);
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resGetSignDetail;
    }


    /* ======================================== 로고관리 ======================================== */

    /* 조직관리 > 심볼/로고관리 > 심볼/로고 목록 조회 */
    @Override
    public PageInfo<SelectLogoListResVO> selectLogoList(SelectLogoListReqVO param){
        PageInfo<SelectLogoListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(comOrganizationinfoMapper.selectLogoList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 조직관리 > 심볼/로고관리 > 심볼/로고 상세 조회 */
    @Override
    public GetLogoDetailResVO getLogoDetail(GetLogoDetailReqVO param){
        GetLogoDetailResVO resGetLogoDetail;
        try {
            resGetLogoDetail = comOrganizationinfoMapper.getLogoDetail(param);
            if(resGetLogoDetail != null && resGetLogoDetail.getFileid() != null && !resGetLogoDetail.getFileid().isEmpty()) {
                List<GetLogoDetailSelectFileResVO> fileList = comOrganizationinfoMapper.getLogoDetailSelectFile(GetLogoDetailSelectFileReqVO.builder()
                                                                                                                    .fileid(resGetLogoDetail.getFileid())
                                                                                                                    .build());
                resGetLogoDetail.setFilelist(fileList);
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resGetLogoDetail;
    }
}
