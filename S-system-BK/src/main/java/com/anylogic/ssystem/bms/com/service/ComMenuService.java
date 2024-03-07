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

import java.util.List;
import com.github.pagehelper.PageInfo;

import java.util.*;


public interface ComMenuService {

    /* LEFT MENU 조회 */
    public Map<String, Object> selectLeftMenu();

    /* 시스템관리 > 메뉴관리 > 메뉴 목록 조회 */
    public PageInfo<SelectMenuListResVO> selectMenuList(SelectMenuListReqVO param);

    /* 시스템관리 > 메뉴관리 > 하위메뉴 목록 조회 */
    public List<SelectSubMenuResVO> selectSubMenu(SelectSubMenuReqVO param);

    /* 시스템관리 > 메뉴관리 > 메뉴 삭제 */
    public List<Integer> deleteMenuBIA(List<DeleteMenuBIAReqVO> param);

    /* 시스템관리 > 메뉴관리 > 메뉴 상세 조회 */
    public GetMenuDetailBIAResVO getMenuDetailBIA(GetMenuDetailBIAReqVO param);

    /* 시스템관리 > 메뉴관리 > 메뉴 등록 */
    public int insertMenuBIA(InsertMenuBIAReqVO param);

    /* 시스템관리 > 메뉴관리 > 메뉴 수정 */
    public int updateMenuBIA(UpdateMenuBIAReqVO param);

    /* 시스템관리 > 메뉴관리 > 메뉴 순서변경 */
    public List<Integer> updateMenuOrder(List<UpdateMenuBIAReqVO> params);

}
