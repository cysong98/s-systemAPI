/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.ssystem.common.file.controller;

import com.anylogic.ssystem.common.file.model.FileVO;
import com.anylogic.ssystem.common.file.service.AttachFileSbmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.anylogic.ssystem.common.file.model.ImageVO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/fileUpload")
public class HwpFileController {

    private static final Logger logger = LoggerFactory.getLogger(AttachFileSbmsController.class);

    @Autowired
    private AttachFileSbmsService attachFileService;


 
    @GetMapping(value = "/{fileName}")
    public ResponseEntity getHwpFile(@PathVariable String fileName) {
        HttpHeaders responseHeaders = new HttpHeaders();
        FileVO fileVO = attachFileService.getHwpFile(fileName, "0");
        responseHeaders.set(HttpHeaders.CONTENT_TYPE, fileVO.getContentType());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(fileVO.getBytes());
    }

    @GetMapping(value = "/form/{fileName}")
    public ResponseEntity getHwpFormFile(@PathVariable String fileName) {
        HttpHeaders responseHeaders = new HttpHeaders();
        FileVO fileVO = attachFileService.getHwpFile(fileName, "1");
        responseHeaders.set(HttpHeaders.CONTENT_TYPE, fileVO.getContentType());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(fileVO.getBytes());
    }

}
