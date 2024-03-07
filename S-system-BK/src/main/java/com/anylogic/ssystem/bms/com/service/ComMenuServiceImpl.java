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
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.com.mapper.ComMenuMapper;

import java.math.BigDecimal;
import java.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.anylogic.ssystem.bms.com.mapper.ComMenuGroupMapper;


@Slf4j
@RequiredArgsConstructor
@Service("ComMenuService")
@Transactional
public class ComMenuServiceImpl implements ComMenuService {


    @Autowired
    private ComMenuMapper comMenuMapper;

    @Autowired
    private ComMenuGroupMapper comMenuGroupMapper;

    /* LEFT MENU 조회 */
    @Override
    public Map<String, Object> selectLeftMenu(){
        Map<String, Object> resultListVO = new HashMap<>();
        try {
            PageHelper.clearPage();
            List<SelectLeftMenuResVO> selectLeftMenuResVOS = comMenuMapper.selectLeftMenuParent();
            List<SelectLeftMenuSubResVO> selectLeftMenuSubResVOS = comMenuGroupMapper.selectLeftMenuSub();
            resultListVO.put("parent",selectLeftMenuResVOS);
            resultListVO.put("sub",selectLeftMenuSubResVOS);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 시스템관리 > 메뉴관리 > 메뉴 목록 조회 */
    @Override
    public PageInfo<SelectMenuListResVO> selectMenuList(SelectMenuListReqVO param){
        PageInfo<SelectMenuListResVO> resultVOList = null;
        try {
            if((param.getId() != null && !param.getId().equals("")) || (param.getName() != null && !param.getName().equals(("")))) {
                List<SelectMenuListResVO> list = comMenuMapper.selectMenuList(param);
                List<SelectSubMenuResVO> sublist = comMenuGroupMapper.selectSubmenu(SelectSubMenuReqVO.builder().sortDirection("ASC").build());
                List<SelectMenuListResVO> addParentList = new ArrayList<>(), resultList = new ArrayList<>();
                for(SelectMenuListResVO vo : list) {
                    if(vo.getType().equals("a"))
                        addParentList.add(vo);
                    else {
                        for(SelectSubMenuResVO sublistVO : sublist) {
                            if(sublistVO.getSubmenuid().equals(vo.getId())) {
                                resultList.add(vo);

                                boolean execute = true;
                                for(SelectMenuListResVO parentVO : addParentList) {
                                    if(parentVO.getId().equals(sublistVO.getMenuid())) {
                                        execute = false;
                                        break;
                                    }
                                }
                                if(execute) {
                                    GetMenuDetailBIAResVO detailBIAResVO = comMenuMapper.getMenuDetailBIA(GetMenuDetailBIAReqVO.builder()
                                                                                                                .id(sublistVO.getMenuid())
                                                                                                                .build());
                                    addParentList.add(SelectMenuListResVO.builder()
                                                            .id(detailBIAResVO.getId())
                                                            .name(detailBIAResVO.getName())
                                                            .url(detailBIAResVO.getUrl())
                                                            .description(detailBIAResVO.getDescription())
                                                            .type(detailBIAResVO.getType())
                                                            .build());
                                }
                            }
                        }
                    }
                }

                resultList.addAll(addParentList);
                PageHelper.startPage(param);
                resultVOList = PageInfo.of(resultList);
            }
            else {
                PageHelper.startPage(param);
                resultVOList = PageInfo.of(comMenuMapper.selectMenuList(param));
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultVOList;
    }


    /* 시스템관리 > 메뉴관리 > 하위메뉴 목록 조회 */
    public List<SelectSubMenuResVO> selectSubMenu(SelectSubMenuReqVO param) {
        List<SelectSubMenuResVO> resultVOList = new ArrayList<>();
        try {
            resultVOList = comMenuGroupMapper.selectSubmenu(SelectSubMenuReqVO.builder()
                                                                .menuid(param.getMenuid())
                                                                .submenuid(param.getSubmenuid())
                                                                .sortDirection("ASC")
                                                                .build());
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultVOList;
    }


    /* 시스템관리 > 메뉴관리 > 메뉴 삭제 */
    @Override
    public List<Integer> deleteMenuBIA(List<DeleteMenuBIAReqVO> params){
        List<Integer> res = new ArrayList<Integer>();
        try {
            /* 하위 메뉴 삭제 */
            List<Integer> resDeleteSubMenu = new ArrayList<Integer>();
            List<SelectSubMenuResVO> delList = new ArrayList<>() ;
            for(DeleteMenuBIAReqVO param : params){
                delList.add(SelectSubMenuResVO.builder().submenuid(param.getId()).build());
                List<SelectSubMenuResVO> list = comMenuGroupMapper.selectSubmenu(SelectSubMenuReqVO.builder()
                                                                                        .sortDirection("DESC")
                                                                                        .menuid(param.getId())
                                                                                        .build());
                if(list == null || list.isEmpty())
                    continue;
                delList.addAll(list);

                for(int i=0; i<delList.size(); i++) {
                    List<SelectSubMenuResVO> volist = comMenuGroupMapper.selectSubmenu(SelectSubMenuReqVO.builder()
                                                                                    .sortDirection("DESC")
                                                                                    .menuid(delList.get(i).getSubmenuid())
                                                                                    .build());
                    if(volist != null && !volist.isEmpty())
                        delList.addAll(volist);
                }
            }

            for(int i = delList.size()-1; i>=0; i--) {
                SelectSubMenuResVO vo = delList.get(i);
                resDeleteSubMenu.add(comMenuGroupMapper.deleteSubMenu(DeleteSubMenuReqVO.builder()
                                                                                    .menuid(vo.getMenuid())
                                                                                    .submenuid(vo.getSubmenuid())
                                                                                    .build()));
                int ares = comMenuMapper.deleteMenuBIA(DeleteMenuBIAReqVO.builder().id(vo.getSubmenuid()).build());
                res.add(ares);
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return res;
    }


    /* 시스템관리 > 메뉴관리 > 메뉴 상세 조회 */
    @Override
    public GetMenuDetailBIAResVO getMenuDetailBIA(GetMenuDetailBIAReqVO param){
        GetMenuDetailBIAResVO resGetMenuDetailBIA;
        try {
            resGetMenuDetailBIA = comMenuMapper.getMenuDetailBIA(param);

            /* 부모 메뉴 정보 조회*/
            GetParentMenuResVO resGetParentMenu = comMenuGroupMapper.getParentMenu(GetParentMenuReqVO.builder()
                                                                                        .submenuid(param.getId())
                                                                                        .build());
            if(resGetParentMenu != null)
                resGetMenuDetailBIA.setParentMenuid(resGetParentMenu.getMenuid());
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resGetMenuDetailBIA == null){
            resGetMenuDetailBIA = new GetMenuDetailBIAResVO();
        }
        return resGetMenuDetailBIA;
    }

    /* 시스템관리 > 메뉴관리 > 메뉴 등록 */
    @Override
    public int insertMenuBIA(InsertMenuBIAReqVO param){
        int resInsertMenuBIA = 0;
        try {
            if(param.getParentMenuid() != null && !param.getParentMenuid().equals(""))
                param.setType("b");
            resInsertMenuBIA = comMenuMapper.insertMenuBIA(param);

            /* 하위 메뉴 추가 */
            if(!param.getParentMenuid().isEmpty()) {
                List<SelectSubMenuResVO> list = comMenuGroupMapper.selectSubmenu(SelectSubMenuReqVO.builder()
                                                                                    .sortDirection("DESC")
                                                                                    .menuid(param.getParentMenuid())
                                                                                    .build());
                BigDecimal submenuorder = BigDecimal.ONE;
                if(!list.isEmpty())
                    submenuorder = new BigDecimal(list.get(0).getSubmenuorder()).add(BigDecimal.ONE);

                int resInsertSubMenu = comMenuGroupMapper.insertSubMenu(InsertSubMenuReqVO.builder()
                                                                                    .menuid(param.getParentMenuid())
                                                                                    .submenuid(param.getId())
                                                                                    .isdefault("y")
                                                                                    .submenuorder(submenuorder)
                                                                                    .build());
            }

        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resInsertMenuBIA;
    }


    /* 시스템관리 > 메뉴관리 > 메뉴 수정 */
    @Override
    public int updateMenuBIA(UpdateMenuBIAReqVO param){
        int resUpdateMenuBIA = 0;
        try {
            // menu 정보 update
            BigDecimal submenuorder = BigDecimal.ONE;
            if(param.getParentMenuid() != null && !param.getParentMenuid().equals("")) {
                param.setType("b");
                // submenuorder 구하기
                List<SelectSubMenuResVO> list = comMenuGroupMapper.selectSubmenu(SelectSubMenuReqVO.builder()
                                                                                        .sortDirection("DESC")
                                                                                        .menuid(param.getParentMenuid())
                                                                                        .build());
                if(!list.isEmpty())
                    submenuorder = new BigDecimal(list.get(0).getSubmenuorder()).add(BigDecimal.ONE);
            }
            else
                param.setType("a");
            resUpdateMenuBIA = comMenuMapper.updateMenuBIA(param);

            // submenuid로 상위 메뉴 검사
            GetParentMenuResVO vo = comMenuGroupMapper.getParentMenu(GetParentMenuReqVO.builder()
                                                                            .submenuid(param.getId())
                                                                            .build());
            // 기존, 현재 상위 메뉴에 따른 분기
            InsertSubMenuReqVO insertSubMenuResVO = InsertSubMenuReqVO.builder()
                                                            .menuid(param.getParentMenuid())
                                                            .submenuid(param.getId())
                                                            .submenuorder(submenuorder)
                                                            .isdefault("y")
                                                            .build();
            UpdateSubMenuReqVO updateSubMenuReqVO = UpdateSubMenuReqVO.builder()
                                                            .menuid(param.getParentMenuid())
                                                            .isdefault("y")
                                                            .submenuid(param.getId())
                                                            .submenuorder(submenuorder)
                                                            .build();
            DeleteSubMenuReqVO deleteSubMenuReqVO = DeleteSubMenuReqVO.builder()
                                                            .submenuid(param.getId())
                                                            .build();

            if(vo == null) {    // 기존 상위 메뉴 : 존재하지 않음
                if(param.getParentMenuid() != null && !param.getParentMenuid().equals(""))   // update할 상위 메뉴 : 존재함
                    comMenuGroupMapper.insertSubMenu(insertSubMenuResVO);
                else                                 // update할 상위 메뉴 : 존재하지 않음
                    comMenuGroupMapper.updateSubMenu(updateSubMenuReqVO);
            }
            else {              // 기존 상위 메뉴 : 존재함
                if(param.getParentMenuid() != null && !param.getParentMenuid().equals("")) {   // update할 상위 메뉴 : 존재함
                    if(vo.getMenuid().equals(param.getParentMenuid()))   // 기존, 현재 상위 메뉴가 같음
                        comMenuGroupMapper.updateSubMenu(updateSubMenuReqVO);
                    else {                                                  // 기존, 현재 상위 메뉴가 다름
                        comMenuGroupMapper.deleteSubMenu(deleteSubMenuReqVO);
                        comMenuGroupMapper.insertSubMenu(insertSubMenuResVO);
                    }
                }
                else {                                  // update할 상위 메뉴 : 존재하지 않음
                    comMenuGroupMapper.deleteSubMenu(deleteSubMenuReqVO);
                }
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resUpdateMenuBIA;
    }


    /* 시스템관리 > 메뉴관리 > 메뉴 순서변경 */
    @Override
    public List<Integer> updateMenuOrder(List<UpdateMenuBIAReqVO> params) {
        List<Integer> res = new ArrayList<Integer>();
        try {
            for(UpdateMenuBIAReqVO param : params) {
                if(param.getType().equals("a"))
                    comMenuMapper.updateMenuBIA(param);
                else
                    comMenuGroupMapper.updateSubMenu(UpdateSubMenuReqVO.builder()
                                                        .submenuid(param.getId())
                                                        .submenuorder(param.getMenuorder())
                                                        .build());
            }
        } catch(Exception e) {
            throw new AnyXException(e);
        }
        return res;
    }

}
