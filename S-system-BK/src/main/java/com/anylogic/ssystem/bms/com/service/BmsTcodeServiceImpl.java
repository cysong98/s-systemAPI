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


import com.anylogic.ssystem.bms.com.mapper.BmsCodeMapper;
import com.anylogic.ssystem.bms.com.model.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.com.mapper.BmsTcodeMapper;

import java.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Slf4j
@RequiredArgsConstructor
@Service("BmsTcodeService")
@Transactional
public class BmsTcodeServiceImpl implements BmsTcodeService {

    @Autowired
    private BmsTcodeMapper bmsTcodeMapper;

    @Autowired
    private BmsCodeMapper bmsCodeMapper;


    /* 시스템관리 > 코드관리 > 코드분류 목록 조회 */
    @Override
    public PageInfo<SelectCodeCategoryListResVO> selectCodeCategoryList(SelectCodeCategoryListReqVO param){
        PageInfo<SelectCodeCategoryListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(bmsTcodeMapper.selectCodeCategoryList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 시스템관리 > 코드관리 > 코드분류 삭제 */
    @Override
    public List<Integer> deleteCodeCategory(List<DeleteCodeCategoryReqVO> params){
        List<Integer> res = new ArrayList<>();
        try {
            for (DeleteCodeCategoryReqVO param : params) {
                int ares = bmsTcodeMapper.deleteCodeCategory(param);
                List<SelectCodeListResVO> list = bmsCodeMapper.selectCodeList(SelectCodeListReqVO.builder()
                                                                                .tcodeid(param.getTcodeid())
                                                                                .build());
                for(SelectCodeListResVO vo : list) {
                    bmsCodeMapper.deleteCode(DeleteCodeReqVO.builder()
                                                .codeid(vo.getCodeid())
                                                .build());
                }
                res.add(ares);
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }


    /* 시스템관리 > 코드관리 > 코드분류 등록 */
    @Override
    public int insertCodeCategory(InsertCodeCategoryReqVO param){
        int resInsertCodeCategory = 0;
        try {
            resInsertCodeCategory = bmsTcodeMapper.insertCodeCategory(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resInsertCodeCategory;
    }


    /* 시스템관리 > 코드관리 > 코드분류 수정 팝업 > 상세조회 */
    @Override
    public GetCodeCategoryDetailResVO getCodeCategoryDetail(GetCodeCategoryDetailReqVO param){
        GetCodeCategoryDetailResVO resGetCodeCategoryDetail;
        try {
            resGetCodeCategoryDetail = bmsTcodeMapper.getCodeCategoryDetail(param);

        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resGetCodeCategoryDetail == null){
            resGetCodeCategoryDetail = new GetCodeCategoryDetailResVO();
        }
        return resGetCodeCategoryDetail;
    }


    /* 시스템관리 > 코드관리 > 코드분류 수정 팝업 > 수정 */
    @Override
    public int updateCodeCategory(UpdateCodeCategoryReqVO param){
        int resUpdateCodeCategory = 0;
        try {
            resUpdateCodeCategory = bmsTcodeMapper.updateCodeCategory(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resUpdateCodeCategory;
    }


}
