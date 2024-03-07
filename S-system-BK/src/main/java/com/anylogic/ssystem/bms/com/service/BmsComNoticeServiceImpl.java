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


import com.anylogic.ssystem.bms.com.mapper.ComOrganizationinfoMapper;
import com.anylogic.ssystem.bms.com.mapper.ComUserinfoMapper;
import com.anylogic.ssystem.bms.com.model.*;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctRdocMapper;
import com.anylogic.ssystem.bms.dct.mapper.ComOrgDetailInfoMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.com.mapper.BmsComNoticeMapper;

import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import java.util.Map;

import com.anylogic.ssystem.bms.com.mapper.BmsComNoticeAuthorMapper;


@Slf4j
@RequiredArgsConstructor
@Service("BmsComNoticeService")
@Transactional
public class BmsComNoticeServiceImpl implements BmsComNoticeService {

    @Autowired
    private BmsComNoticeMapper bmsComNoticeMapper;

    @Autowired
    private BmsComNoticeAuthorMapper bmsComNoticeAuthorMapper;

    @Autowired
    private ComOrganizationinfoMapper comOrganizationinfoMapper;

    @Autowired
    private ComUserinfoMapper comUserinfoMapper;

    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;


    /* 게시판관리 > 공지사항 관리 > 공지사항 목록 조회 */
    @Override
    public PageInfo<SelectNoticeListResVO> selectNoticeList(SelectNoticeListReqVO param){
        PageInfo<SelectNoticeListResVO> resultListVO = null;
        try {
            String deptid = null;
            if(param.getUserid() != null && !param.getUserid().equals("")) {
                List<GetUserDetailResVO> list = comUserinfoMapper.getUserDetail(GetUserDetailReqVO.builder().userid(param.getUserid()).build());
                deptid = list.get(0).getDeptid();
            }
            List<SelectNoticeAuthorResVO> list = bmsComNoticeAuthorMapper.selectNoticeAuthorByDeptid(SelectNoticeAuthorReqVO.builder().deptid(deptid).build());
            List<SelectNoticeListResVO> noticeList = bmsComNoticeMapper.selectNoticeList(param);
            List<SelectNoticeListResVO> selectNoticeListResVOS = new ArrayList<SelectNoticeListResVO>();
            for(int i=0; i<noticeList.size(); i++) {
                for(SelectNoticeAuthorResVO vo : list) {
                    if(vo.getNtcid().equals(noticeList.get(i).getNtcid())) {
                        SelectNoticeListResVO insertVO = noticeList.get(i);
                        selectNoticeListResVOS.add(SelectNoticeListResVO.builder()
                                                            .ntcid(insertVO.getNtcid())
                                                            .title(insertVO.getTitle())
                                                            .selcnt(insertVO.getSelcnt())
                                                            .inname(insertVO.getInname())
                                                            .indt(insertVO.getIndt())
                                                            .build());
                    }
                }
            }
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(selectNoticeListResVOS);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 게시판관리 > 공지사항 관리 > 공지사항 삭제 */
    @Override
    public List<Integer> deleteNoticeBIA(List<DeleteNoticeBIAReqVO> params){
        List<Integer> res = new ArrayList<Integer>();
        try {
            for(DeleteNoticeBIAReqVO param : params) {
                DeleteNoticeAuthorReqVO deleteFaqAuthorReqVO = DeleteNoticeAuthorReqVO.builder()
                                                                    .ntcid(param.getNtcid())
                                                                    .build();
                bmsComNoticeAuthorMapper.deleteNoticeAuthor(deleteFaqAuthorReqVO);
            }

            for(DeleteNoticeBIAReqVO param : params) {
                res.add(bmsComNoticeMapper.deleteNoticeBIA(param));
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }


    /* 게시판관리 > 공지사항 관리 > 공지사항 상세 조회 */
    @Override
    public GetNoticeDetailResVO getNoticeDetail(GetNoticeDetailReqVO param){
        GetNoticeDetailResVO resGetNoticeDetail;
        try {
            List<SelectOrganizationListResVO> selectOrganizationList = comOrganizationinfoMapper.selectOrganizationList(SelectOrganizationListReqVO.builder().pageSize(10000).build());
            List<SelectNoticeAuthorResVO> selectNoticeAuthorlist = bmsComNoticeAuthorMapper.selectNoticeAuthor(SelectNoticeAuthorReqVO.builder()
                                                                                                                    .ntcid(param.getNtcid())
                                                                                                                    .build());
            resGetNoticeDetail = bmsComNoticeMapper.getNoticeDetail(param);
            /* notice_author 테이블의 로우가 organizationinfo 테이블의 전체 개수와 같을 때 */
            if(selectOrganizationList.size() == selectNoticeAuthorlist.size()) {
                List<SelectNoticeAuthorResVO> list = new ArrayList<>();
                list.add(SelectNoticeAuthorResVO.builder()
                                    .ntcid(param.getNtcid())
                                    .deptid("전체")
                                    .deptname("전체")
                                    .build());
                resGetNoticeDetail.setDeptlist(list);
            }
            else
                resGetNoticeDetail.setDeptlist(selectNoticeAuthorlist);
            /* 공지사항 조회수 관련 */
            if(param.getRole().equals("user")) {
                UpdateNoticeBIAReqVO updateNoticeBIAReqVO = UpdateNoticeBIAReqVO.builder()
                                                                .ntcid(param.getNtcid())
                                                                .build();
                updateNoticeBIAReqVO.setSelcnt(resGetNoticeDetail.getSelcnt() + 1);
                bmsComNoticeMapper.addSelCnt(updateNoticeBIAReqVO);
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resGetNoticeDetail;
    }


    /* 게시판관리 > 공지사항 관리 > 공지사항 등록 */
    @Override
    public int insertNoticeBIA(InsertNoticeBIAReqVO param){
        int resInsertNoticeBIA = 0;
        param.setSdate(param.getSdate().replaceAll("-", ""));
        if(param.getEdate() != null && !param.getEdate().equals(""))
            param.setEdate(param.getEdate().replaceAll("-", ""));

        try {
            param.setNtcid("NTC" + bmsDctRdocMapper.getSysGuid());
            resInsertNoticeBIA = bmsComNoticeMapper.insertNoticeBIA(param);

            if(param.getDeptlist().get(0).getDeptid().equals("전체")) {
                List<SelectOrganizationListResVO> list = comOrganizationinfoMapper.selectOrganizationList(SelectOrganizationListReqVO.builder().build());
                for(SelectOrganizationListResVO vo : list) {
                    bmsComNoticeAuthorMapper.insertNoticeAuthor(InsertNoticeAuthorReqVO.builder()
                                                                        .ntcid(param.getNtcid())
                                                                        .deptid(vo.getOrgid())
                                                                        .deptname(vo.getOrgname())
                                                                        .build());
                }
            }
            else {
                for(InsertNoticeAuthorReqVO vo : param.getDeptlist()) {
                    bmsComNoticeAuthorMapper.insertNoticeAuthor(InsertNoticeAuthorReqVO.builder()
                                                                        .ntcid(param.getNtcid())
                                                                        .deptid(vo.getDeptid())
                                                                        .deptname(vo.getDeptname())
                                                                        .build());
                }
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resInsertNoticeBIA;
    }


    /* 게시판관리 > 공지사항 관리 > 공지사항 수정 */
    @Override
    public int updateNoticeBIA(UpdateNoticeBIAReqVO param){
        int resUpdateNoticeBIA = 0;
        param.setSdate(param.getSdate().replaceAll("-", ""));
        if(param.getEdate() != null && !param.getEdate().equals(""))
            param.setEdate(param.getEdate().replaceAll("-", ""));

        try {
            resUpdateNoticeBIA = bmsComNoticeMapper.updateNoticeBIA(param);

            DeleteNoticeAuthorReqVO deleteNoticeAuthorReqVO = DeleteNoticeAuthorReqVO.builder()
                                                                        .ntcid(param.getNtcid())
                                                                        .build();
            bmsComNoticeAuthorMapper.deleteNoticeAuthor(deleteNoticeAuthorReqVO);

            if(param.getDeptlist().get(0).getDeptid().equals("전체")) {
                List<SelectOrganizationListResVO> list = comOrganizationinfoMapper.selectOrganizationList(SelectOrganizationListReqVO.builder().build());
                for(SelectOrganizationListResVO vo : list) {
                    bmsComNoticeAuthorMapper.insertNoticeAuthor(InsertNoticeAuthorReqVO.builder()
                                                                        .ntcid(param.getNtcid())
                                                                        .deptid(vo.getOrgid())
                                                                        .deptname(vo.getOrgname())
                                                                        .build());
                }
            }
            else {
                for(UpdateNoticeAuthorReqVO vo : param.getDeptlist()) {
                    bmsComNoticeAuthorMapper.insertNoticeAuthor(InsertNoticeAuthorReqVO.builder()
                                                                        .ntcid(param.getNtcid())
                                                                        .deptid(vo.getDeptid())
                                                                        .deptname(vo.getDeptname())
                                                                        .build());
                }
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resUpdateNoticeBIA;
    }

}
