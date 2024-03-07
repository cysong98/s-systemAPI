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
public interface ComMenuGroupMapper {

    /* LEFT MENU 조회 - 하위 메뉴 */
    List<SelectLeftMenuSubResVO> selectLeftMenuSub() throws RuntimeException;

    /* 시스템관리 > 메뉴관리 > 하위메뉴 목록 조회 */
    List<SelectSubMenuResVO> selectSubmenu(SelectSubMenuReqVO param) throws RuntimeException;

    /* 시스템관리 > 메뉴관리 > 하위메뉴 삭제 */
    int deleteSubMenu(DeleteSubMenuReqVO param) throws RuntimeException;

    /* 시스템관리 > 메뉴관리 > 메뉴 상세 조회 - 부모메뉴 */
    GetParentMenuResVO getParentMenu(GetParentMenuReqVO param) throws RuntimeException;

    /* 시스템관리 > 메뉴관리 > 하위메뉴 등록 */
    int insertSubMenu(InsertSubMenuReqVO param) throws RuntimeException;

    /* 시스템관리 > 메뉴관리 > 하위메뉴 수정 */
    int updateSubMenu(UpdateSubMenuReqVO param) throws RuntimeException;

}
