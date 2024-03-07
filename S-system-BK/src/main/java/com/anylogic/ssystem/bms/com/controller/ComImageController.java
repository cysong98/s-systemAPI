/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.ssystem.bms.com.controller;

import com.anylogic.ssystem.bms.com.service.ComOrgGwaninfoServiceImpl;
import com.anylogic.ssystem.common.file.model.ImageVO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/fileUpload/com")
public class ComImageController {

    @Autowired
    private ComOrgGwaninfoServiceImpl comOrgGwaninfoService;


    /* 관인/서명인 이미지 조회 */
    @GetMapping(value = "/{fileName}")
    public ResponseEntity<?> getImage(@PathVariable String fileName) {
        HttpHeaders responseHeaders = new HttpHeaders();
        ImageVO imageVO = comOrgGwaninfoService.getImage(fileName);
        responseHeaders.set(HttpHeaders.CONTENT_TYPE, imageVO.getContentType());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(imageVO.getBytes());
    }

}
