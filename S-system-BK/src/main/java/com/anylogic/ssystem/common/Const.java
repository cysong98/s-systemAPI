/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.ssystem.common;


public class Const {

    public static final String IOT_MAKERS_MANAGER_SYS_DIV_CD = "PMG";
    public static final String PACKAGE = "com.anylogic";
    public static final String ROLE_ID_PREFIX = "ROLE_";
    public static final String V1 = "v1";

    public static final String RESPONSE_CODE_OK = "200";


    public static final String DEFAULT_CFG_FILE = "";
    public static final String DEFAULT_ATACH_FILE_PROPERTICE = "properties/file.properties";

    public static final String DEFAULT_ATACH_FILE_SYSTEM_ROOT = "file.upload.root";
    public static final String DEFAULT_ATACH_FILE_URL_ROOT = "file.upload.url";
    public static final String DOMAIN_IP_ADDRESS = "http://192.168.0.85:8580";

    private static final String FILE_TYPE_CD_000 = "000"; // 첨부 파일
    private static final String FILE_TYPE_ROOT_000 = "file.upload.000";  // system 경로

    private static final String FILE_TYPE_CD_001 = "001"; // thumb 이미지
    private static final String FILE_TYPE_ROOT_001 = "file.upload.001";  // system 경로

    private static final String FILE_TYPE_CD_002 = "002"; // 사업자 등록증
    private static final String FILE_TYPE_ROOT_002 = "file.upload.002";  // system 경로

    private static final String FILE_TYPE_CD_003 = "003"; // 법인 등기부 원본
    private static final String FILE_TYPE_ROOT_003 = "file.upload.003";  // system 경로

    private static final String FILE_TYPE_CD_004 = "004"; // 로고
    private static final String FILE_TYPE_ROOT_004 = "file.upload.004";  // system 경로

    private static final String FILE_TYPE_CD_005 = "005"; // 회사제품소개 첨부파일
    private static final String FILE_TYPE_ROOT_005 = "file.upload.005";  // system 경로

    private static final String FILE_TYPE_CD_006 = "006"; // 회사제품소개 첨부파일
    private static final String FILE_TYPE_ROOT_006 = "file.upload.006";  // system 경로

    private static final String FILE_TYPE_CD_007 = "007"; // 사업제안 동영상
    private static final String FILE_TYPE_ROOT_007 = "file.upload.007";  // system 경로

    private static final String FILE_TYPE_CD_008 = "008"; // 기타
    private static final String FILE_TYPE_ROOT_008 = "file.upload.008";  // system 경로

    private static final String FILE_TYPE_CD_009 = "009"; // 단말 이미지
    private static final String FILE_TYPE_ROOT_009 = "file.upload.009";  // system 경로

    private static final String FILE_TYPE_CD_010 = "010"; // 모뎀 이미지
    private static final String FILE_TYPE_ROOT_010 = "file.upload.010";  // system 경로

    private static final String FILE_TYPE_CD_011 = "011"; // 입고문서
    private static final String FILE_TYPE_ROOT_011 = "file.upload.011";  // system 경로

    private static final String FILE_TYPE_CD_012 = "012"; // 망 영향 성 검토 결과
    private static final String FILE_TYPE_ROOT_012 = "file.upload.012";  // system 경로

    private static final String FILE_TYPE_CD_013 = "013"; // KC 인증서
    private static final String FILE_TYPE_ROOT_013 = "file.upload.013";  // system 경로

    private static final String FILE_TYPE_CD_014 = "014"; // 위치정보사업자 등록 허가
    private static final String FILE_TYPE_ROOT_014 = "file.upload.014";  // system 경로

    private static final String FILE_TYPE_CD_015 = "015"; // 위치기반서비스 사업자 신고
    private static final String FILE_TYPE_ROOT_015 = "file.upload.015";  // system 경로

    private static final String FILE_TYPE_CD_016 = "016"; // 웨이버 문서
    private static final String FILE_TYPE_ROOT_016 = "file.upload.016";  // system 경로

    private static final String FILE_TYPE_CD_017 = "017"; // 기타 문서
    private static final String FILE_TYPE_ROOT_017 = "file.upload.017";  // system 경로

    private static final String FILE_TYPE_CD_018 = "018"; // 단말 펌웨어
    private static final String FILE_TYPE_ROOT_018 = "file.upload.018";  // system 경로

    private static final String FILE_TYPE_CD_019 = "019"; // 모뎀 펌웨어
    private static final String FILE_TYPE_ROOT_019 = "file.upload.019";  // system 경로

    private static final String FILE_TYPE_CD_020 = "020"; // 검증 시나리오
    private static final String FILE_TYPE_ROOT_020 = "file.upload.020";  // system 경로

    private static final String FILE_TYPE_CD_021 = "021"; // 검증 시나리오(FAIL)
    private static final String FILE_TYPE_ROOT_021 = "file.upload.021";  // system 경로

    private static final String FILE_TYPE_CD_022 = "022"; // 검증 시나리오(FAIL) 제조사 답변
    private static final String FILE_TYPE_ROOT_022 = "file.upload.022";  // system 경로

    private static final String FILE_TYPE_CD_023 = "023"; // 검증결과서
    private static final String FILE_TYPE_ROOT_023 = "file.upload.023";  // system 경로

    private static final String FILE_TYPE_CD_024 = "024"; // 검증결과 기타문서
    private static final String FILE_TYPE_ROOT_024 = "file.upload.024";  // system 경로

    private static final String FILE_TYPE_CD_025 = "025"; // 배포파일
    private static final String FILE_TYPE_ROOT_025 = "file.upload.025";  // system 경로

    private static final String FILE_TYPE_CD_026 = "026"; // 배포파일
    private static final String FILE_TYPE_ROOT_026 = "file.upload.026";  // system 경로

    private static final String FILE_TYPE_CD_DEF = "def"; // 기타
    private static final String FILE_TYPE_ROOT_DEF = "file.upload.def";  // system 경로
    
    
    
    public static final class SNS_TYPE{
        
    
    public static final String NAVER_SUMMARY = "NV";
    public static final String NAVER = "_naver";
    public static final String FACEBOOK_SUMMARY = "FB";
    public static final String FACEBOOK = "_faceBook";
    public static final String KAKAOTALK_SUMMARY ="KK";
    public static final String KAKAOTALK ="_kakao";
    public static final String GOOGLE_SUMMARY ="GG";
    public static final String GOOGLE ="_google";
    }
    
    
    public static final class EMAIL_CONFIRM{
        public static final String CONFIRM_TEMPLATE= "<html>\n" + 
                "\n" + 
                "<head>\n" + 
                "    <meta charset=\"UTF-8\">\n" + 
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" + 
                "    <meta name=\"viewport\" content=\"width=1100\">\n" + 
                "    <meta name=\"author\" content=\"\">\n" + 
                "    <meta name=\"keywords\" content=\"\">\n" + 
                "    <meta name=\"subject\" content=\"\">\n" + 
                "    <meta name=\"description\" content=\"\">\n" + 
                "    <meta name=\"copyright\" content=\"\">\n" + 
                "    <meta name=\"robots\" content=\"all\">\n" + 
                "\n" + 
                "    <!-- title -->\n" + 
                "    <!-- //title -->\n" + 
                "    <link href=\"https://fonts.googleapis.com/css?family=Noto+Sans+KR:100,300,400,500&amp;subset=korean\" rel=\"stylesheet\">\n" + 
                "\n" + 
                "</head>\n" + 
                "\n" + 
                "<body style=\" width:100%; background-color:#f1f1f1; letter-spacing: -0.8px; font-weight: 300;\">\n" + 
                "    <div style=\"width:100%; min-width:800px; background-color:#f1f1f1; font-weight: 300; letter-spacing: -2px;\">\n" + 
                "        <table style=\"margin-top: 30px; margin-left: auto; margin-bottom: 0; margin-right: auto; width:600px; border-collapse: collapse;\">\n" + 
                "            <caption style=\"display: none;\">인증번호 안내</caption>\n" + 
                "            <tr style=\"height:90px;\">\n" + 
                "                <td colspan=\"2\">\n" + 
                "                    <h1 style=\"line-height: 0;\">\n" + 
                "                    </h1>\n" + 
                "                </td>\n" + 
                "            </tr>\n" + 
                "        </table>\n" + 
                "    </div>\n" + 
                "</body>\n" + 
                "\n" + 
                "</html>";
        
        public static final String CONFIRM_ATTR = "CONFIRM_NUMBER";
        public static final String CONFIRM_TIMER_START_ATTR = "CONFIRM_TIMER_START";
        public static final long APPLYED_TIMER = 600000;//10분 (기존 3분은 적다는 이유로 취소)
        public static final String CONFIRM_SUCCESS_ATTR = "ONE_CONFIRM";
        
    }
    
    
    public static final class Location{
        
        public static final String SITE_URL = "127.0.0.1";
        public static final String SAVED_RECURUIT_PHOTO = "./src/main/webapp/webdata/_CJ/recuruitIMG";
        //public static final String SAVED_RECURUIT_PHOTO = "/webdata/_CJ/recuruitIMG";
        public static final String SAVED_RECURUIT_PHOTO_URL = "/webdata/_CJ/recuruitIMG/";
        
    }
    
    public static final class USERINFO{
        
        public static final String ADMIN_AUTHORITY = "ADMIN_AUTHORITY";
        public static final String ID_ATTR = "AdminUserCd";
        public static final String USER_ID_ATTR = "AdminUserId";
        
        public static final String NEED_LOGIN_CERT = "NEED_LOGIN_CERT";
        
        public static final String EMAIL_ATTR = "Email";
        public static final String WRITING_APPLICATION = "ApplicationCd";

        
        
    }
    
    
    
    

    public static enum FILE_TYPE_CD{
        ATTACH_COM(FILE_TYPE_CD_000){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_000;
            }
        },
        ATTACH_THUMB(FILE_TYPE_CD_001){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_001;
            }
        },
        ATTACH_BRC(FILE_TYPE_CD_002){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_002;
            }
        },
        ATTACH_CRS(FILE_TYPE_CD_003){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_003;
            }
        },
        ATTACH_LOGO(FILE_TYPE_CD_004){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_004;
            }
        },
        ATTACH_CPA(FILE_TYPE_CD_005){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_005;
            }
        },
        ATTACH_RP(FILE_TYPE_CD_006){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_006;
            }
        },
        ATTACH_AVI(FILE_TYPE_CD_007){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_007;
            }
        },
        ATTACH_ETC(FILE_TYPE_CD_008){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_008;
            }
        },
        ATTACH_MODEL(FILE_TYPE_CD_009){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_009;
            }
        },
        ATTACH_MODEM(FILE_TYPE_CD_010){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_010;
            }
        },
        ATTACH_WARES(FILE_TYPE_CD_011){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_011;
            }
        },
        ATTACH_NET(FILE_TYPE_CD_012){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_012;
            }
        },
        ATTACH_KC(FILE_TYPE_CD_013){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_013;
            }
        },
        ATTACH_BIZR(FILE_TYPE_CD_014){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_014;
            }
        },
        ATTACH_DCLA(FILE_TYPE_CD_015){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_015;
            }
        },
        ATTACH_WAIVER(FILE_TYPE_CD_016){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_016;
            }
        },
        ATTACH_ETCTXT(FILE_TYPE_CD_017){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_017;
            }
        },
        ATTACH_TFA(FILE_TYPE_CD_018){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_018;
            }
        },
        ATTACH_MFA(FILE_TYPE_CD_019){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_019;
            }
        },
        ATTACH_VERS(FILE_TYPE_CD_020){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_020;
            }
        },
        ATTACH_VERF(FILE_TYPE_CD_021){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_021;
            }
        },
        ATTACH_VERFA(FILE_TYPE_CD_022){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_022;
            }
        },
        ATTACH_VERSUL(FILE_TYPE_CD_023){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_023;
            }
        },
        ATTACH_VERETC(FILE_TYPE_CD_024){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_024;
            }
        },
        ATTACH_DIST(FILE_TYPE_CD_025){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_025;
            }
        },
        ATTACH_MODEMFW(FILE_TYPE_CD_026){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_026;
            }
        },
        ATTACH_DEF(FILE_TYPE_CD_DEF){
            public String getSystemPath() {
                return FILE_TYPE_ROOT_DEF;
            }
        };

        private String typeCd;
        private String systemPath;

        FILE_TYPE_CD(String typeCd){
            this.typeCd = typeCd;
        };

        public String getTypeCd() {
            return typeCd;
        }
        public String getSystemPath() {
            return systemPath;
        }

    }
}

