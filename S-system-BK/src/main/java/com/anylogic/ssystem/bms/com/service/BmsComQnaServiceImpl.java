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
import com.anylogic.ssystem.bms.dct.model.SelectWaitListReqVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.com.mapper.BmsComQnaMapper;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctRdocMapper;

import java.math.BigDecimal;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service("BmsComQnaService")
@Transactional
public class BmsComQnaServiceImpl implements BmsComQnaService {

    @Autowired
    private BmsComQnaMapper bmsComQnaMapper;

    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;


    /* 게시판관리 > Q&A 관리 > Q&A 목록 조회 */
    @Override
    public PageInfo<SelectQnaListResVO> selectQnaList(SelectQnaListReqVO param){
        final int PAGESIZE = 10;
        PageInfo<SelectQnaListResVO> resultListVO = null;
        try {
            /* 검색 조건이 존재하는 경우 리스트 정렬을 진행하지 않는다. */
            if(!param.getInname().equals("") || !param.getTitle().equals("") || !param.getStartDt().equals("")
                    || !param.getEndDt().equals("") || !param.getRefid().equals("")) {
                param.setPageSize(10);
                PageHelper.startPage(param);
                resultListVO = PageInfo.of(bmsComQnaMapper.selectQnaList(param));
                return  resultListVO;
            }

            List<SelectQnaListResVO> sortListVO = bmsComQnaMapper.selectQnaList(param);

            /* list sort */
            sortListVO.sort(new Comparator<SelectQnaListResVO>() {
                @Override
                public int compare(SelectQnaListResVO vo1, SelectQnaListResVO vo2) {
                    if (vo1.getThread().compareTo(vo2.getThread()) > 0)
                        return -1;
                    else if(vo1.getThread().equals(vo2.getThread()))
                        if(vo1.getPos().compareTo(vo2.getPos()) < 0) return -1;
                        else if(vo1.getPos().equals(vo2.getPos()))
                            return vo1.getDepth().intValue() - vo2.getDepth().intValue();
                        else return 1;
                    else
                        return 1;
                }
            });
            resultListVO = PageInfo.of(sortListVO);


            /* ============================ Pagination ============================ */
            // 10번째 row의 refid 검색
            // if(refid == null) => row : 질문 => pageSize -= 1;
            // else => row : 답변 => for문(10+1번째 검색, 10+2번째 검색 ... )
            //                   => 10 + n번째 row : 질문 => pageSize = 10+n-1;
            int endRow = 0, startRow = 0;
            int pageNum = param.getPageNum();
            List<SelectQnaListResVO> pageListVO = resultListVO.getList();
            for(int i=0; i<pageNum; i++) {
                startRow = endRow + 1;
                endRow += PAGESIZE;
                if(endRow >= sortListVO.size()) {
                    endRow = sortListVO.size();
                    break;
                }

                if(pageListVO.get(endRow-1).getRefid() == null)
                    endRow -= 1;
                else {
                    for(int j=1; j<pageListVO.size(); j++) {
                        if(endRow + j >= pageListVO.size()) {
                            endRow = sortListVO.size();
                            break;
                        }
                        if(pageListVO.get(endRow + j-1).getRefid() == null) {
                            endRow += (j - 1);
                            break;
                        }
                    }
                }
            }

            // PageInfo settings
            resultListVO.setPageSize(endRow - startRow + 1);
            resultListVO.setStartRow(startRow);
            resultListVO.setEndRow(endRow);
            if(resultListVO.getTotal()%PAGESIZE == 0)
                resultListVO.setPages((int)resultListVO.getTotal()/PAGESIZE);
            else
                resultListVO.setPages((int)resultListVO.getTotal()/PAGESIZE + 1);
            /* ==================================================================== */
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 게시판관리 > Q&A 관리 > Q&A 삭제 */
    @Override
    public List<Integer> deleteQna(List<DeleteQnaReqVO> params){
        List<Integer> res = new ArrayList<Integer>();
        try {
            for(DeleteQnaReqVO param : params) {
                int ares = 0;
                if(param.getDepth() == null) {
                    res.add(bmsComQnaMapper.deleteQna(param));
                    return res;
                }

                if(param.getDepth().equals(BigDecimal.ZERO)) {
                    List<SelectQnaListResVO> list = bmsComQnaMapper.selectQnaList(SelectQnaListReqVO.builder()
                                                                                        .thread(param.getThread())
                                                                                        .sortDirection("ASC")
                                                                                        .build());
                    for(int i=0; i<list.size(); i++)
                        ares = bmsComQnaMapper.deleteQna(DeleteQnaReqVO.builder().qnaid(list.get(i)
                                                                                        .getQnaid())
                                                                                        .build());
                }
                else {
                    List<SelectQnaListResVO> list = bmsComQnaMapper.selectQnaList(SelectQnaListReqVO.builder()
                                                                                        .thread(param.getThread())
                                                                                        .pos(param.getPos())
                                                                                        .sortDirection("DESC")
                                                                                        .build());
                    int pivot = param.getDepth().intValue();
                    for(int i=0; i<list.size(); i++) {
                        if(list.get(i).getDepth().intValue() >= pivot)
                            ares = bmsComQnaMapper.deleteQna(DeleteQnaReqVO.builder().qnaid(list.get(i)
                                                                                .getQnaid())
                                                                                .build());
                        else break;
                    }
                }
                res.add(ares);
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }


    /* 게시판관리 > Q&A 관리 > Q&A 상세 조회 */
    @Override
    public GetQnaDetailResVO getQnaDetail(GetQnaDetailReqVO param){
        GetQnaDetailResVO resGetQnaDetail;
        try {
            resGetQnaDetail = bmsComQnaMapper.getQnaDetail(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resGetQnaDetail == null){
            resGetQnaDetail = new GetQnaDetailResVO();
        }
        return resGetQnaDetail;
    }


    /* 게시판관리 > Q&A 관리 > Q&A 등록 */
    @Override
    public int insertQna(InsertQnaReqVO param){
        log.debug("bmsComQna insertQna" + param.toString());

        int resInsertQna = 0;
        try {
            /* qnaId 생성 */
            param.setQnaid("QNA" + bmsDctRdocMapper.getSysGuid());

            /* 답변인 경우 */
            if(param.getDepth() != null) {
                // refid(상위아이디) 참조
                GetQnaDetailResVO getQnaDetailResVO = bmsComQnaMapper.getQnaDetail(GetQnaDetailReqVO.builder()
                                                                                    .qnaid(param.getRefid())
                                                                                    .build());
                // repcnt(답변수) + 1
                bmsComQnaMapper.updateQna(UpdateQnaReqVO.builder()
                                            .qnaid(param.getRefid())
                                            .repcnt(getQnaDetailResVO.getRepcnt().add(BigDecimal.ONE))
                                            .build());
            }
            /* 질문인 경우 */
            else {
                GetSeqResVO vo = bmsComQnaMapper.getSeq();
                BigDecimal thread = null;
                if(vo == null)
                    thread = BigDecimal.ZERO;
                else
                    thread = vo.getThread().add(BigDecimal.ONE);

                param.setThread(thread);
                param.setPos(BigDecimal.ZERO);
                param.setDepth(BigDecimal.ZERO);
            }

            resInsertQna = bmsComQnaMapper.insertQna(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resInsertQna;
    }


    /* 게시판관리 > Q&A 관리 > Q&A 수정 */
    @Override
    public int updateQna(UpdateQnaReqVO param){
        int resUpdateQna = 0;
        try {
            resUpdateQna = bmsComQnaMapper.updateQna(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resUpdateQna;
    }
}
