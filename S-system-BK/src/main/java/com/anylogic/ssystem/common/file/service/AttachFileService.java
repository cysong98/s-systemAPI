/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.ssystem.common.file.service;

import com.anylogic.ssystem.common.file.model.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import com.anylogic.ssystem.common.file.model.ImageVO;
import com.anylogic.ssystem.common.file.model.AttachFileVO;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public interface AttachFileService {
     public ImageVO getImage(String fileName);
    public FileVO getHwpFile(String fileName, String flag);
     String getFilePath(String fileName);
    List<AttachFileVO> selectAttachFileByRecord(AttachFileVO parameter);
    void deleteAttachFileByRecordDelYN(AttachFileVO parameter);
    void deleteAttachFileByRecord(AttachFileVO parameter);
    void deleteAttachFile(AttachFileVO parameter);
     AttachFileVO getAttachFile(AttachFileVO parameter);
     default List<AttachFileVO> insertAttachFile(List<MultipartFile> mf, AttachFileVO data)
     {
         return insertAttachFile(mf, data, "");
     }

    List<AttachFileVO> insertAttachFile(List<MultipartFile> mf, AttachFileVO data, String bizpath);
    default List<AttachFileVO> updateAttachFile(List<MultipartFile> mf, AttachFileVO data)
    {
        return updateAttachFile(mf, data, "");
    }
    List<AttachFileVO> updateAttachFile(List<MultipartFile> mf, AttachFileVO sendinfo, String bizpath);
}

