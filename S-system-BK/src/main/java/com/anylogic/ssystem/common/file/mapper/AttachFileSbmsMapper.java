/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.common.file.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import com.anylogic.ssystem.common.file.model.AttachFileVO;
import com.anylogic.ssystem.common.file.model.CommFileVO;

@Mapper
public interface AttachFileSbmsMapper {

     String getFilePath(String fileName);
     String getDctFilePath(String fileName);
     String getFormFilePath(String fileName);
     AttachFileVO getAttachFileByFleid(AttachFileVO parameter);
     List<AttachFileVO> selectAttachFileByRecord(AttachFileVO parameter);
     AttachFileVO getAttachFile(AttachFileVO parameter);
     int deleteAttachFileByFleid(AttachFileVO parameter);
     int deleteAttachFile(AttachFileVO parameter);
     int deleteAttachFileDelYN(AttachFileVO parameter);
     int deleteAttachFileByRecord(AttachFileVO parameter);
     int insertAttachFile(AttachFileVO parameter);
     int updateAttachFile(AttachFileVO parameter);
     int updateAttachFilePath(AttachFileVO parameter);
     int insertCommFile(CommFileVO parameter);
}

