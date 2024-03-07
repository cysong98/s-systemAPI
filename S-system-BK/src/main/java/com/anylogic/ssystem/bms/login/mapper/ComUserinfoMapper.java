/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.login.mapper;

import com.anylogic.ssystem.bms.login.model.UpdateAbsenceReqVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import com.anylogic.ssystem.bms.login.model.GetUserLoginReqVO;
import com.anylogic.ssystem.bms.login.model.GetUserLoginResVO;


@Mapper
public interface ComUserinfoMapper {


    GetUserLoginResVO getUserLogin(GetUserLoginReqVO param) throws RuntimeException;
    int updateAbsence(UpdateAbsenceReqVO param) throws RuntimeException;


}
