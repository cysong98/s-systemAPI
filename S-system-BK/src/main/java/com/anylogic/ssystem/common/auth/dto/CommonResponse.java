/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.ssystem.common.auth.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
@Builder //.build로 객체 생성하게 해줌
public class CommonResponse {
    @Builder.Default // Builder default 지정
    private String id = UUID.randomUUID().toString(); // uuid
    @Builder.Default
    private Date dateTime = new Date(); // date
    private Boolean success;
    private Object response;
    private Object error;
}

