/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.mapper;

import com.anylogic.ssystem.bms.com.model.*;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;


@Mapper
public interface ComMenuMapper {

    /* 시스템관리 > 메뉴관리 > 메뉴 목록 조회 */
    List<SelectMenuListResVO> selectMenuList(SelectMenuListReqVO param) throws RuntimeException;

    /* LEFT MENU 조회 - 상위 메뉴 */
    List<SelectLeftMenuResVO> selectLeftMenuParent() throws RuntimeException;

    /* 시스템관리 > 메뉴관리 > 메뉴 삭제 */
    int deleteMenuBIA(DeleteMenuBIAReqVO param) throws RuntimeException;

    /* 시스템관리 > 메뉴관리 > 메뉴 상세 조회 */
    GetMenuDetailBIAResVO getMenuDetailBIA(GetMenuDetailBIAReqVO param) throws RuntimeException;

    /* 시스템관리 > 메뉴관리 > 메뉴 등록 */
    int insertMenuBIA(InsertMenuBIAReqVO param) throws RuntimeException;

    /* 시스템관리 > 메뉴관리 > 메뉴 수정 */
    int updateMenuBIA(UpdateMenuBIAReqVO param) throws RuntimeException;

}
