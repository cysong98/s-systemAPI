/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.ssystem.common.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


public class DateUtil {
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static int getHour( ) {
        Calendar cal = Calendar.getInstance(Locale.getDefault()) ;
        return cal.get(Calendar.HOUR_OF_DAY) ;
    }
    public static int getMinute( ) {
        Calendar cal = Calendar.getInstance(Locale.getDefault()) ;
        return cal.get(Calendar.MINUTE) ;
    }
    public static int getYear( ) {
        Calendar cal = Calendar.getInstance(Locale.getDefault()) ;
        return cal.get(Calendar.YEAR) ;
    }
    public static int getMonth( ) {
        Calendar cal = Calendar.getInstance(Locale.getDefault()) ;
        return cal.get(Calendar.MONTH) ;
    }
    public static int getDate( ) {
        Calendar cal = Calendar.getInstance(Locale.getDefault()) ;
        return cal.get(Calendar.DATE) ;
    }
    /**
    * 오늘 일자를 지정된 Format의 날짜 표현형식으로 돌려준다. <BR><BR>
    *
    * 사용예) getToday("yyyy/MM/dd hh:mm a")<BR>
    * 결 과 ) 2001/12/07 10:10 오후<BR><BR>
    *
    * Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
    *
    * @return java.lang.String
    * @param pOutformat String
    */
    public static String getToday( String pOutformat) {
        String rDateString;

        try
        {
            rDateString = getDateFormat( pOutformat, new Date());

        } catch( Exception e ) {
            return null;
        }

        return rDateString;
    }
    
    /**
    * 전달받은 날짜(Date)를 지정된 Format의 날짜 표현형식으로 돌려준다. <BR><BR>
    *
    * 사용예) getToday("yyyy-MM-dd hh:mm")<BR>
    * 결 과 ) 2001-12-07 10:10:10<BR><BR>
    *
    * Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
    *
    * @return String inUnix
    */
    public static String unixToStr(String inUnix)
    {
        long unix_seconds = Long.parseLong(inUnix);
        Date date = new Date(unix_seconds * 1000L);
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return jdf.format(date);
    }

    /**
    * 전달받은 날짜(Date)를 지정된 Format의 날짜 표현형식으로 돌려준다. <BR><BR>
    *
    * 사용예) getToday("yyyy/MM/dd hh:mm a")<BR>
    * 결 과 ) 2001/12/07 10:10 오후<BR><BR>
    *
    * Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
    *
    * @return java.lang.String
    * @param pOutformat String
    */
    public static String getDateFormat( String pOutformat, Date vDate) {

        SimpleDateFormat pOutformatter =  new SimpleDateFormat (pOutformat, Locale.KOREA);

        String rDateString;

        try
        {
            rDateString = pOutformatter.format(vDate);

        } catch( Exception e ) {
            return null;
        }

        return rDateString;
    }
    /**
    * 전달받은 날짜(Date)를 지정된 Format의 날짜 표현형식으로 돌려준다. <BR><BR>
    *
    * 사용예) getToday("yyyy/MM/dd hh:mm a")<BR>
    * 결 과 ) 2001/12/07 10:10 오후<BR><BR>
    *
    * Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
    *
    * @return java.lang.String
    * @param pOutformat String
    */
    public static String getDateFormat(String pIndate, String pInformat, String pOutformat ) {

        SimpleDateFormat pInformatter =  new SimpleDateFormat (pInformat, Locale.KOREA);

        String rDateString;

        try {
            Date vIndate = pInformatter.parse(pIndate);
            rDateString = getDateFormat(pOutformat,vIndate ) ;

        } catch( Exception e ) {
            rDateString = pIndate;
        }

        return rDateString;
    }
    /**
    * 전달받은 날짜(Date)를 지정된 Format의 날짜 표현형식으로 돌려준다. <BR><BR>
    *
    * 사용예) getDateFormat("20101121","-")<BR>
    * 결 과 ) 2010-11-21<BR><BR>
    *
    *
    * @return java.lang.String
    * @param type String
    */
    public static String getDateFormat( String date, String type) {
        return date.substring(0,4) + type + date.substring(4,6)+ type + date.substring(6);
    }
    /**
    * 입력받은 날짜에 일/시/분 단위의 값을 더하여 출력Format에 따라 값을 넘겨준다. <BR><BR>
    * Parameter는 입력일, 입력일 Format, 출력일 Format, 일단위 더하기, 시단위 더하기,
    * 분단위 더하기이다.
     * 간단한 사용예는 다음과 같다.
    * 사용예) LLog.debug.println( getFormattedDateAdd("200201010605","yyyyMMddhhmm","yyyy/MM/dd HH:mm",-100,10,-11) );
    * 결과) 2001/09/23 15:54
    * Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
    *
    * @return java.lang.String
    * @param pIndate String
    * @param pInformat String
    * @param pOutformat String
    * @param pDay int
    * @param pHour int
    * @param pMin int
    */
    public static String getFormattedDateAdd(String pIndate, String pInformat, String pOutformat, int pDay, int pHour, int pMin ) {

        SimpleDateFormat pInformatter =  new SimpleDateFormat (pInformat, Locale.KOREA);
        SimpleDateFormat pOutformatter =  new SimpleDateFormat (pOutformat, Locale.KOREA);

        String rDateString;
        Date vIndate;
        long vAddon = ( pDay * 24L*60L*60L*1000L ) + ( pHour * 60L*60L*1000L ) + ( pMin * 60L*1000L );
        try
        {
            vIndate = pInformatter.parse(pIndate);
            Date vAddday = new Date( vIndate.getTime() + vAddon );
            rDateString = pOutformatter.format(vAddday);

        } catch( Exception e ) {
            rDateString = pIndate;
        }
        return rDateString;
    }

    /**
    * 입력받은 날짜에 월 단위의 값을 더하여 출력Format에 따라 값을 넘겨준다. <BR><BR>
    * Parameter는 입력일, 입력일 Format, 출력일 Format, 일단위 더하기, 시단위 더하기,
    * 분단위 더하기이다.
    * 간단한 사용예는 다음과 같다.
    * 사용예) LLog.debug.println( getFormattedDateAdd("200201010605","yyyyMMddhhmm","yyyy/MM/dd HH:mm",-6) );
    * 결과) 2001/09/23 15:54
    * Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
    *
    * @return java.lang.String
    * @param pIndate String
    * @param pInformat String
    * @param pOutformat String
    * @param pYear int
    */
    public static String getFormattedDateYearAdd(String pIndate, String pInformat, String pOutformat, int pYear) {

        SimpleDateFormat pInformatter =  new SimpleDateFormat (pInformat, Locale.KOREA);
        SimpleDateFormat pOutformatter =  new SimpleDateFormat (pOutformat, Locale.KOREA);
        Calendar cal = Calendar.getInstance(Locale.getDefault()) ;
        String rDateString;
        Date vIndate;

        try {
            vIndate = pInformatter.parse(pIndate);
            cal.setTime(vIndate) ;
            cal.add(Calendar.YEAR, pYear) ;
            rDateString = pOutformatter.format(cal.getTime());

        } catch( Exception e ) {
            rDateString = pIndate;
        }

        return rDateString;
    }

    /**
    * 입력받은 날짜에 월 단위의 값을 더하여 출력Format에 따라 값을 넘겨준다. <BR><BR>
    * Parameter는 입력일, 입력일 Format, 출력일 Format, 일단위 더하기, 시단위 더하기,
    * 분단위 더하기이다.
    * 간단한 사용예는 다음과 같다.
    * 사용예) LLog.debug.println( getFormattedDateAdd("200201010605","yyyyMMddhhmm","yyyy/MM/dd HH:mm",-6) );
    * 결과) 2001/09/23 15:54
    * Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
    *
    * @return java.lang.String
    * @param pIndate String
    * @param pInformat String
    * @param pOutformat String
    * @param pMonth int
    */
    public static String getFormattedDateMonthAdd(String pIndate, String pInformat, String pOutformat, int pMonth) {

        SimpleDateFormat pInformatter =  new SimpleDateFormat (pInformat, Locale.KOREA);
        SimpleDateFormat pOutformatter =  new SimpleDateFormat (pOutformat, Locale.KOREA);
        Calendar cal = Calendar.getInstance(Locale.getDefault()) ;
        String rDateString;
        Date vIndate;

        try {
            vIndate = pInformatter.parse(pIndate);
            cal.setTime(vIndate) ;
            cal.add(Calendar.MONTH, pMonth) ;
            rDateString = pOutformatter.format(cal.getTime());

        } catch( Exception e ) {
            rDateString = pIndate;
        }

        return rDateString;
    }

    /**
    * 입력받은 날짜에 월 단위의 값을 더하여 출력 Format에 따라 값을 넘겨준다. <BR><BR>
    * Parameter는 입력일, 입력일 Format, 출력일 Format, 일단위 더하기, 시단위 더하기,
    * 분단위 더하기이다.
    * 간단한 사용예는 다음과 같다.
    * 사용예) LLog.debug.println( getFormattedDateAdd("200201010605","yyyyMMddhhmm","yyyy/MM/dd HH:mm",-6) );
    * 결과) 2001/09/23 15:54
    * Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
    *
    * @return java.lang.String
    * @param pIndate String
    * @param pInformat String
    * @param pOutformat String
    * @param pHour int
    */
    public static String getFormattedDateHourAdd(String pIndate, String pInformat, String pOutformat, int pHour) {

        SimpleDateFormat pInformatter =  new SimpleDateFormat (pInformat, Locale.KOREA);
        SimpleDateFormat pOutformatter =  new SimpleDateFormat (pOutformat, Locale.KOREA);
        Calendar cal = Calendar.getInstance(Locale.getDefault()) ;
        String rDateString;
        Date vIndate;

        try {
            vIndate = pInformatter.parse(pIndate);
            cal.setTime(vIndate) ;
            cal.add(Calendar.HOUR, pHour) ;
            rDateString = pOutformatter.format(cal.getTime());

        } catch( Exception e ) {
            rDateString = pIndate;
        }

        return rDateString;
    }

    public static String getFormattedDateMonthAdd(Date date,String pOutformat, int pMonth) {
        SimpleDateFormat pOutformatter =  new SimpleDateFormat (pOutformat, Locale.KOREA);
        Calendar cal = Calendar.getInstance(Locale.getDefault()) ;
        String rDateString;
        try {
            cal.setTime(date) ;
            cal.add(Calendar.MONTH, pMonth) ;
            rDateString = pOutformatter.format(cal.getTime());

        } catch( Exception e ) {
            rDateString = date.toString();
        }
        return rDateString;
    }

    public static boolean isDate( String pOutformat, String vDate) {
        SimpleDateFormat pOutformatter =  new SimpleDateFormat (pOutformat, Locale.KOREA);
        try {
            pOutformatter.parse(vDate);
            return true ;
        } catch( Exception e ) {
            return false ;
        }
    }

    /**
    * 일자들의 계산을 수행한다..
    * 제일 마지막의 Parameter pType에 따라서 Return값이 다르다.
    * 둘째 Parameter는 첫째 Parameter의 입력 형식을 지정하고 넷째 Parameter는
    * 셋째 Parameter의 입력형식을 지정한다.
    * Return값의 단위를 정해주는 pType에는 4가지가 올 수 있는데
    * ECOMJDateU.DAY, ECOMJDateU.HOUR, ECOMJDateU.MIN, ECOMJDateU.SEC 이다.
    * 각각의 단위는 일단위, 시단위, 분단위 이다.
    * 첫째 Parameter로 입력받은 일자에서 셋째 Parameter로 입력받은 일자를 빼서
    * 나온 결과를 돌려준다.
    * Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
    * 간단한 사용예는 다음과 같다.
    * LLog.debug.println(getComputedDate("2002/01/04 00:01","yyyy/MM/dd hh:mm","2002/01/02 23:59","yyyy/MM/dd hh:mm",ECOMJDateU.DAY));
    * 작업 결과로 '1'이 표시된다.
    *
    * @return long
    * @param pIndate1 java.lang.String
    * @param pInformat1 java.lang.String
    * @param pIndate2 java.lang.String
    * @param pInformat2 java.lang.String
    */

    public static long getComputedDate( String pIndate1, String pInformat1, String pIndate2, String pInformat2) {

        SimpleDateFormat pInformatter1 =  new SimpleDateFormat (pInformat1);
        SimpleDateFormat pInformatter2 =  new SimpleDateFormat (pInformat2);
        long vDategap;
        try {
            Date vIndate1 = pInformatter1.parse(pIndate1);
            Date vIndate2 = pInformatter2.parse(pIndate2);
            vDategap = vIndate1.getTime() - vIndate2.getTime();
        } catch ( Exception e ) {
            logger.error(e.getMessage());
            return 0;
        }

        return vDategap;
    }

    public static long getComputedDate( String pIndate1, String pInformat1, String pIndate2, String pInformat2, String pType) {

        SimpleDateFormat pInformatter1 =  new SimpleDateFormat (pInformat1);
        SimpleDateFormat pInformatter2 =  new SimpleDateFormat (pInformat2);
        long vDategap;
        try {
            Date vIndate1 = pInformatter1.parse(pIndate1);
            Date vIndate2 = pInformatter2.parse(pIndate2);

            vDategap = convertDate(pType, vIndate1.getTime() - vIndate2.getTime());

        } catch ( Exception e ) {
            logger.error(e.getMessage());
            return 0;
        }

        return vDategap;
    }

    public static long getComputedDate( String pIndate, String pInformat , String pType) {

        SimpleDateFormat pInformatter1 =  new SimpleDateFormat (pInformat);
        Date curDate;
        Calendar calendar = Calendar.getInstance() ;
        curDate = calendar.getTime() ;
        long vDategap;
        try {
            Date vIndate1 = pInformatter1.parse(pIndate);

            vDategap = convertDate (pType, vIndate1.getTime() - curDate.getTime());

        } catch ( Exception e ) {
            logger.error(e.getMessage());
            return 0;
        }

        return vDategap;
    }

    private static long convertDate(String pType, long dateValue) {

        long vDategap = 0;
        switch (pType) {
            case "S":
                vDategap = dateValue / (1000);              //초
                break;
            case "M":
                vDategap = dateValue / (60 * 1000);          //분
                break;
            case "H":
                vDategap = dateValue / (60 * 60 * 1000);      //시간
                break;
            case "D":
                vDategap = dateValue / (24 * 60 * 60 * 1000);    //일
                break;
            default:
                break;
        }
        return vDategap;
    }

    /**
     * amount 차이만큼 날짜를 반환한다.
     * @param amount 날짜 증가 량
     * @return 현재일자로 부터 증가된 날짜
     */
    public static String getAddDate( int amount ) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
                 cal.add(Calendar.DATE, amount);
        DecimalFormat df = new DecimalFormat("00");

        return cal.get(Calendar.YEAR) + df.format(cal.get(Calendar.MONTH) + 1) + df.format(cal.get(Calendar.DATE));
    }

    /**
     * amount 차이만큼 월수를 증가하여 날짜를 반환한다.
     * @param basicDate 기준일자 (YYYYMMDD)
     * @param amount 증가량
     * @return 기준이자로 부터 증가된 날짜
     */
    public static String getAddMonth( String basicDate , int amount ) {

        int basicYear = Integer.parseInt(basicDate.substring(0,4));
        int basicMonth= Integer.parseInt(basicDate.substring(4,6))-1;
        int basicDay  = Integer.parseInt(basicDate.substring(6,8));
        Calendar cal = new Calendar.Builder()
                        .setDate(basicYear, basicMonth, basicDay)
                                .build();
        DecimalFormat df = new DecimalFormat("00");

        cal.add(Calendar.MONTH, amount);

        return cal.get(Calendar.YEAR) + df.format(cal.get(Calendar.MONTH) + 1) + df.format(cal.get(Calendar.DATE));
    }

    /**
     * 현재 시간에서 amount 차이만큼 시간을 증가하여 반환한다.
     * @param amount 증가량
     * @return 증가된 시간
     */
    public static String getAddHour(int amount) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.add(Calendar.HOUR, amount);

        SimpleDateFormat dateprint = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return dateprint.format(cal.getTime());
    }

    /**
     * 현재 시간에서 amount 차이만큼 날짜를 반환한다.
     * @param amount 증가량
     * @return 증가된 개월 수
     */
    public static String getAddMonth( int amount ) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
                 cal.add(Calendar.MONTH, amount);

        DecimalFormat df = new DecimalFormat("00");

        return cal.get(Calendar.YEAR)
                + df.format(cal.get(Calendar.MONTH) + 1)
                + df.format(cal.get(Calendar.DATE));
    }

    /**
     * 입력된 날짜가 유효한지 확인
     * @param date 날자(YYYYMMDD)
     * @return 정상일자면 true 아니면 false
     */
    public static boolean isDate(String date) {
        if (!StringUtils.hasLength(date)) {
            return false;
        }
        if (date.length() != 8) {
            return false;
        }
        try {
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(4, 6));
            int day = Integer.parseInt(date.substring(6));
            return isDate(year, month, day);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 입력된 날짜가 유효한지 확인
     * @param year int 년도
     * @param month int 월
     * @param day int 날짜
     * @return 정상일자면 true 아니면 false
     */
    public static boolean isDate(int year, int month, int day) {
        if( year < 0 || month < 0 || day < 0 ) return false ;
        if ( month > 12 || day > 31 ) return false ;

        DecimalFormat df = new DecimalFormat("00");

        String yearMonth = year + df.format(month);
        int lastDay = getLastDayOfMonth(yearMonth);
        return day <= lastDay ;
    }

    /**
     * 해당월에 마지막 일자 구하기
     * yyyymm
     * @param yearMonth String 년월 (YYYYMM)
     * @return 마직막일자
     */
    public static int getLastDayOfMonth( String yearMonth)  {
        int year = Integer.parseInt( yearMonth.substring(0,4) ) ;
        int month = Integer.parseInt( yearMonth.substring(4,6) ) ;
        Calendar cal = new Calendar.Builder()
                .setDate(year, month-1, 1)
                .build();
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH) ;
    }

    /**
     * <pre>
     * 입력받은 날짜 시간 yyyy-MM-dd HH:mm:ss 형식으로 반한
     * date는 format과 날짜형식이 같아야 한다.
     * </pre>
     * @param date EX)2011-11-11 11:11:11
     * @param format EX) "yyyy-MM-dd HH:mm:ss"
     * @return "yyyy-MM-dd HH:mm:ss"
     */
    public static String getReqDate(String date, String format) {
        return getCurrentDate(date, format, "yyyy-MM-dd HH:mm:ss");

    }

    /**
     * "yyyy-MM-dd HH:mm:SS"형의 날짜를 반환한다.
    @return yyyy-MM-dd HH:mm:SS 형의 날짜
    */
    public static String getCurrentDate() {
        return getCurrentDate("yyyy-MM-dd HH:mm:ss");
    }

    public static String getCurrentDate(String s) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(s, Locale.KOREA);
        return simpledateformat.format(new Date());
    }

    public static String getCurrentDate(String date, String format, String s) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(s, Locale.KOREA);
        SimpleDateFormat simpledateformat2 = new SimpleDateFormat(format, Locale.KOREA);

        try{
            return simpledateformat.format(simpledateformat2.parse(date));
        }catch (ParseException e) {
            return "";
        }
    }

    public static String getCurrentDate(Date date, String s) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(s, Locale.KOREA);
        return simpledateformat.format(date);
    }

    public static int dayOfWeek(String date) {

        int year = Integer.parseInt(date.substring(0,4) ) ;
        int month = Integer.parseInt(date.substring(4,6) ) ;
        int day = Integer.parseInt(date.substring(6) ) ;

        Calendar cal = new Calendar.Builder()
                .setDate(year, month-1, day)
                .build();

        return cal.get( Calendar.DAY_OF_WEEK );
    }


    public static long getDateDiff(String dataPart, String pIndate1) {

        String pInformat1 = "yyyy-MM-dd HH:mm:ss";
        String pInformat2 = "yyyy-MM-dd HH:mm:ss";

        return getDateDiff(dataPart, pIndate1, pInformat1, getCurrentDate(), pInformat2);
    }


    public static long getDateDiff(String dataPart, String pIndate1, String pIndate2) {

        String pInformat1 = "yyyy-MM-dd HH:mm:ss";
        String pInformat2 = "yyyy-MM-dd HH:mm:ss";

        return getDateDiff(dataPart, pIndate1, pInformat1, pIndate2, pInformat2);
    }

    /**<pre>     *
     * getDataDiff : 년,월,일,시,분,초 로 지난 시간을 (long)로 리턴
     * dataPart(y : 년, m : 월, d : 일, h : 시, n : 분, s : 초)
     * pIndate1 EX)2011-01-01 01:01:01
     * pInformat1 EX)yyyyMMdd hh:mm:ss
     * pIndate2 EX)2011-12-31 01:01:01
     * pInformat2 EX)yyyyMMdd hh:mm:ss
     * pIndate1, pIndate2 값이 "now" 경우 현재 날짜가 들어간다.
     * </pre>
     * @param dataPart String (y : 년, m : 월, d : 일, h : 시, n : 분, s : 초)
     * @param pIndate1 String EX)2011-01-01 01:01:01
     * @param pInformat1 String EX)yyyyMMdd hh:mm:ss
     * @param pIndate2 String EX)2011-12-31 01:01:01
     * @param pInformat2 String EX)yyyyMMdd hh:mm:ss
     * @return 날짜간 차이
     */
    public static long getDateDiff(String dataPart, String pIndate1, String pInformat1, String pIndate2, String pInformat2) {

        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat nowFormatter =  new SimpleDateFormat ("yyyyMMdd HH:mm:ss", Locale.KOREA);

        pInformat1 = pIndate1.equals("now") ? "yyyyMMdd HH:mm:ss" : pInformat1;
        pInformat2 = pIndate2.equals("now") ? "yyyyMMdd HH:mm:ss" : pInformat2;

        SimpleDateFormat pInformatter1 =  new SimpleDateFormat (pInformat1, Locale.KOREA);
        SimpleDateFormat pInformatter2 =  new SimpleDateFormat (pInformat2, Locale.KOREA);

        pIndate1 = pIndate1.equals("now") ? nowFormatter.format(now) : pIndate1;
        pIndate2 = pIndate2.equals("now") ? nowFormatter.format(now) : pIndate2;

        long vDategap = 0;

        try {
            Date vIndate1 = pInformatter1.parse(pIndate1);
            Date vIndate2 = pInformatter2.parse(pIndate2);

            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();

            calendar1.setTime(vIndate1);
            calendar2.setTime(vIndate2);

            long vDateGapYear     = calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR);
            long vDateGapMonth     = calendar2.get(Calendar.MONTH) - calendar1.get(Calendar.MONTH);

            switch (dataPart) {
                case "y":
                    vDategap = vDateGapYear;
                    break;
                case "m":
                    vDategap = (vDateGapYear * 12) + vDateGapMonth;
                    break;
                case "d":
                    vDategap = ((calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / (1000 * 3600 * 24));
                    break;
                case "h":
                    vDategap = ((calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / (1000 * 3600));
                    break;
                case "n":
                    vDategap = ((calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / (1000 * 60));
                    break;
                case "s":
                    vDategap = ((calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / 1000);
                    break;
                default:
                    break;
            }
        } catch ( Exception e ) {
            logger.error(e.getMessage());
            return 0;
        }

        return vDategap;
    }

    /**<pre>     *
     * getDataDiff : 년,월,일,시,분,초 로 지난 시간을 (long)로 리턴
     * dataPart(y : 년, m : 월, d : 일, h : 시, n : 분, s : 초)
     * </pre>
     * @param dataPart String (y : 년, m : 월, d : 일, h : 시, n : 분, s : 초)
     * @param pIndate1 String EX)2011-01-01 01:01:01
     * @param pIndate2 String EX)2011-01-01 01:01:01
     * @return 구분별 차이
     */
    public static long getDateDiff(String dataPart, Date pIndate1, Date pIndate2) {

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        long vDategap = 0;

        try {
            calendar1.setTime(pIndate1);
            calendar2.setTime(pIndate2);

            long vDateGapYear = calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR);
            long vDateGapMonth = calendar2.get(Calendar.MONTH) - calendar1.get(Calendar.MONTH);

            switch (dataPart) {
                case "y":
                    vDategap = vDateGapYear;
                    break;
                case "m":
                    vDategap = (vDateGapYear * 12) + vDateGapMonth;
                    break;
                case "d":
                    vDategap = (calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / (1000 * 3600 * 24);
                    break;
                case "h":
                    vDategap = (calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / (1000 * 3600);
                    break;
                case "n":
                    vDategap = (calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / (1000 * 60);
                    break;
                case "s":
                    vDategap = (calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / 1000;
                    break;
                default:
                    break;
            }
        } catch ( Exception e ) {
            logger.error(e.getMessage());
            return 0;
        }

        return vDategap;
    }

    // 해당 년의 몇번째 주인지
     public static int getYearWeek(String thisday) {

      int year = Integer.parseInt(getDateFormat(thisday, "yyyyMMdd", "yyyy" ));
      int month = Integer.parseInt(getDateFormat(thisday, "yyyyMMdd", "MM" ));
      int day = Integer.parseInt(getDateFormat(thisday, "yyyyMMdd", "dd" ));

      Calendar cDate = new Calendar.Builder()
              .setDate(year, month, day)
              .build();

      return cDate.get(Calendar.WEEK_OF_YEAR);
     }


      public static long diffOfDate(String begin, String end) throws Exception
      {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        Date beginDate = formatter.parse(begin);
        Date endDate = formatter.parse(end);

        long diff = endDate.getTime() - beginDate.getTime();

          return diff / (24 * 60 * 60 * 1000);
      }


      public static long diffOfHour(String begin, String end) throws Exception
      {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        Date beginDate = formatter.parse(begin);
        Date endDate = formatter.parse(end);

        long diff = endDate.getTime() - beginDate.getTime();

          return diff / (60 * 60 * 1000);
      }



     /**
     * <PRE>
     * 1. MethodName    :    firstDateOfWeek
     * 2. Comment        :    해당일의 첫번째 날 리턴
     * 3. 작성자            :    retriver
     * 4. 작성일            :    2012. 4. 20.    오후 5:33:31
     * </PRE>
     *
     *    @param dtPrevDate String 'YYYYMMDD' 
     *    @return String 'YYYYMMDD'
     */
    public static String firstDateOfWeek(String  dtPrevDate)
    {
        int yr = Integer.parseInt(dtPrevDate.substring(0,4));
        int mh = Integer.parseInt(dtPrevDate.substring(5,7));
        int dy = Integer.parseInt(dtPrevDate.substring(8,10));
        Calendar cal = new Calendar.Builder()
                .setDate(yr, mh, dy)
                .setWeekDefinition(Calendar.SUNDAY, Calendar.DAY_OF_WEEK)
                .build();

        int today = cal.get( Calendar.DAY_OF_WEEK);
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DAY_OF_MONTH, firstDay - today);

        String month = Integer.toString(cal.get(Calendar.MONTH ) + 1);
        if(month.length() == 1)
            month = "0" + month;
        String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH ));
        if(day.length() == 1)
            day = "0" + day;

        return cal.get(Calendar.YEAR) +  month +  day;
    }

    public static String getTransDate(){

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String strDate = sdfDate.format(System.currentTimeMillis());

        String year = strDate.substring(0,4);
        String month = strDate.substring(5,7);
        String day = strDate.substring(8,10);

        return year + "년 " + month + "월 " + day + "일";
    }

}

