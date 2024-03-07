/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.ssystem.common.auth.exception;

import com.anylogic.ssystem.common.auth.dto.CommonResponse;
import com.anylogic.ssystem.common.auth.dto.ErrorResponse;
import com.anylogic.ssystem.common.auth.exception.error.InvalidRefreshTokenException;
import com.anylogic.ssystem.common.auth.exception.error.InvalidTokenException;
import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Bean Validation에 실패했을 때, 에러메시지를 내보내기 위한 Exception Handler
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<CommonResponse> handleParamViolationException(BindException ex) {
        // 파라미터 validation에 걸렸을 경우
        ErrorCode errorCode = ErrorCode.REQUEST_PARAMETER_BIND_FAILED;
        return new ResponseEntity<>(getResponse(errorCode), errorCode.getStatus());
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<CommonResponse> handleDuplicateMemberException(DuplicateMemberException ex) {
        ErrorCode errorCode = ErrorCode.DUPLICATE_MEMBER_EXCEPTION;
        return new ResponseEntity<>(getResponse(errorCode), errorCode.getStatus());
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<CommonResponse> handleInvalidRefreshTokenException(InvalidRefreshTokenException ex) {
        ErrorCode errorCode = ErrorCode.INVALID_REFRESH_TOKEN;
        return new ResponseEntity<>(getResponse(errorCode), errorCode.getStatus());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<CommonResponse> handleInvalidTokenException(InvalidTokenException ex) {
        ErrorCode errorCode = ErrorCode.INVALID_TOKEN;
        return new ResponseEntity<>(getResponse(errorCode), errorCode.getStatus());
    }

    private CommonResponse getResponse (ErrorCode errorCode) {
        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();
        return CommonResponse.builder()
                .success(false)
                .error(error)
                .build();
    }
}


