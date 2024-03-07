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


import com.anylogic.ssystem.bms.com.model.*;
import com.anylogic.ssystem.bms.com.mapper.ComUserinfoMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.com.mapper.BmsComFaqMapper;
import com.anylogic.ssystem.bms.com.mapper.BmsComFaqAuthorMapper;
import com.anylogic.ssystem.bms.com.mapper.ComOrganizationinfoMapper;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctRdocMapper;

import java.util.*;
import javax.annotation.PostConstruct;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Service("BmsComFaqService")
@Transactional
public class BmsComFaqServiceImpl implements BmsComFaqService {

    @Autowired
    private BmsComFaqMapper bmsComFaqMapper;

    @Autowired
    private BmsComFaqAuthorMapper bmsComFaqAuthorMapper;

    @Autowired
    private ComOrganizationinfoMapper comOrganizationinfoMapper;

    @Autowired
    private ComUserinfoMapper comUserinfoMapper;

    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;


    /* 게시판관리 > FAQ 관리 > FAQ 목록 조회 */
    @Override
    public PageInfo<SelectFaqListResVO> selectFaqList(SelectFaqListReqVO param){
        PageInfo<SelectFaqListResVO> resultListVO = null;
        try {
            String deptid = null;
            if(param.getUserid() != null && !param.getUserid().equals("")) {
                List<GetUserDetailResVO> list = comUserinfoMapper.getUserDetail(GetUserDetailReqVO.builder().userid(param.getUserid()).build());
                deptid = list.get(0).getDeptid();
            }
            List<SelectFaqAuthorResVO> list = bmsComFaqAuthorMapper.selectFaqAuthorByDeptid(SelectFaqAuthorReqVO.builder().deptid(deptid).build());
            List<SelectFaqListResVO> faqList = bmsComFaqMapper.selectFaqList(param);
            List<SelectFaqListResVO> selectFaqListResVOS = new ArrayList<SelectFaqListResVO>();
            for(int i=0; i<faqList.size(); i++) {
                for(SelectFaqAuthorResVO vo : list) {
                    if(vo.getFaqid().equals(faqList.get(i).getFaqid())) {
                        SelectFaqListResVO insertVO = faqList.get(i);
                        selectFaqListResVOS.add(SelectFaqListResVO.builder()
                                                        .faqid(insertVO.getFaqid())
                                                        .title(insertVO.getTitle())
                                                        .selcnt(insertVO.getSelcnt())
                                                        .inname(insertVO.getInname())
                                                        .indt(insertVO.getIndt())
                                                        .build());
                    }
                }
            }
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(selectFaqListResVOS);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 게시판관리 > FAQ 관리 > FAQ 삭제 */
    @Override
    public List<Integer> deleteFaqBIA(List<DeleteFaqBIAReqVO> params){
        List<Integer> res = new ArrayList<Integer>();
        try {
            for(DeleteFaqBIAReqVO param : params) {
                DeleteFaqAuthorReqVO deleteFaqAuthorReqVO = DeleteFaqAuthorReqVO.builder()
                                                                .faqid(param.getFaqid())
                                                                .build();
                bmsComFaqAuthorMapper.deleteFaqAuthor(deleteFaqAuthorReqVO);
            }

            for(DeleteFaqBIAReqVO param : params) {
                res.add(bmsComFaqMapper.deleteFaqBIA(param));
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }


    /* 게시판관리 > FAQ 관리 > FAQ 상세 조회 */
    @Override
    public GetFaqDetailResVO getFaqDetail(GetFaqDetailReqVO param){
        GetFaqDetailResVO resGetFaqDetail;
        try {
            List<SelectOrganizationListResVO> selectOrganizationList = comOrganizationinfoMapper.selectOrganizationList(SelectOrganizationListReqVO.builder().build());
            List<SelectFaqAuthorResVO> selectFaqAuthorList = bmsComFaqAuthorMapper.selectFaqAuthor(SelectFaqAuthorReqVO.builder()
                                                                                                    .faqid(param.getFaqid())
                                                                                                    .build());
            resGetFaqDetail = bmsComFaqMapper.getFaqDetail(param);
            /* faq_author 테이블의 로우가 organizationinfo 테이블의 전체 개수와 같을 때 */
            if(selectOrganizationList.size() == selectFaqAuthorList.size()) {
                List<SelectFaqAuthorResVO> list = new ArrayList<>();
                list.add(SelectFaqAuthorResVO.builder()
                                .faqid(param.getFaqid())
                                .deptid("전체")
                                .deptname("전체")
                                .build());
                resGetFaqDetail.setDeptlist(list);
            }
            else
                resGetFaqDetail.setDeptlist(selectFaqAuthorList);
            /* FAQ 조회수 관련 */
            if(param.getRole().equals("user")) {
                UpdateFaqBIAReqVO updateFaqBIAReqVO = UpdateFaqBIAReqVO.builder()
                                                            .faqid(param.getFaqid())
                                                            .build();
                updateFaqBIAReqVO.setSelcnt(resGetFaqDetail.getSelcnt() + 1);
                bmsComFaqMapper.addSelCnt(updateFaqBIAReqVO);
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resGetFaqDetail;
    }

    /* 게시판관리 > FAQ 관리 > FAQ 등록 */
    @Override
    public int insertFaqBIA(InsertFaqBIAReqVO param){
        int resInsertFaqBIA = 0;
        try {
            String faqid = "FAQ" + bmsDctRdocMapper.getSysGuid();
            param.setFaqid(faqid);
            resInsertFaqBIA = bmsComFaqMapper.insertFaqBIA(param);

            if(param.getDeptlist().get(0).getDeptid().equals("전체")) {
                List<SelectOrganizationListResVO> list = comOrganizationinfoMapper.selectOrganizationList(SelectOrganizationListReqVO.builder().build());
                for(SelectOrganizationListResVO vo : list) {
                    bmsComFaqAuthorMapper.insertFaqAuthor(InsertFaqAuthorReqVO.builder()
                                                            .faqid(param.getFaqid())
                                                            .deptid(vo.getOrgid())
                                                            .deptname(vo.getOrgname())
                                                            .build());
                }
            }
            else {
                for(InsertFaqAuthorReqVO vo : param.getDeptlist()) {
                    bmsComFaqAuthorMapper.insertFaqAuthor(InsertFaqAuthorReqVO.builder()
                                                            .faqid(param.getFaqid())
                                                            .deptid(vo.getDeptid())
                                                            .deptname(vo.getDeptname())
                                                            .build());
                }
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resInsertFaqBIA;
    }

    /* 게시판관리 > FAQ 관리 > FAQ 수정 */
    @Override
    public int updateFaqBIA(UpdateFaqBIAReqVO param){
        int resUpdateFaqBIA = 0;
        try {

            resUpdateFaqBIA = bmsComFaqMapper.updateFaqBIA(param);

            DeleteFaqAuthorReqVO deleteFaqAuthorReqVO = DeleteFaqAuthorReqVO.builder()
                                                            .faqid(param.getFaqid())
                                                            .build();
            bmsComFaqAuthorMapper.deleteFaqAuthor(deleteFaqAuthorReqVO);

            if(param.getDeptlist().get(0).getDeptid().equals("전체")) {
                List<SelectOrganizationListResVO> list = comOrganizationinfoMapper.selectOrganizationList(SelectOrganizationListReqVO.builder().build());
                for(SelectOrganizationListResVO vo : list) {
                    bmsComFaqAuthorMapper.insertFaqAuthor(InsertFaqAuthorReqVO.builder()
                                                            .faqid(param.getFaqid())
                                                            .deptid(vo.getOrgid())
                                                            .deptname(vo.getOrgname())
                                                            .build());
                }
            }
            else {
                for(UpdateFaqAuthorReqVO vo : param.getDeptlist()) {
                    bmsComFaqAuthorMapper.insertFaqAuthor(InsertFaqAuthorReqVO.builder()
                                                            .faqid(param.getFaqid())
                                                            .deptid(vo.getDeptid())
                                                            .deptname(vo.getDeptname())
                                                            .build());
                }
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resUpdateFaqBIA;
    }

}
