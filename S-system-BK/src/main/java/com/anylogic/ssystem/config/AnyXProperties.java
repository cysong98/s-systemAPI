/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.ssystem.config;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;


@Getter
@Setter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("anyx")
public final class AnyXProperties {
    private final ServerPath serverPath;

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class ServerPath {
        private Integer servertype = null;
        private String devPath = "";
        private String deployPath = "";
        private String anyXFilePat = "";
        private String editorFilePat = "";
        private String tempExcelPat = "";
    }
}
