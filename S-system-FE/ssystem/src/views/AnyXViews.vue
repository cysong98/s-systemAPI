<!--
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
-->

<template>
    <div class="container">
        <h3>AnyX Views</h3>
        <div class="pub-status">
            <span class="pub">퍼블적용 완료</span>
            <span class="ing">퍼블적용 진행중</span>
            <span class="chk">컨텐츠 확인필요</span>
        </div>
        <div class="table">
            <table id="list">
            <thead>
                <tr>
                <th width ="12%">번호</th>
                <th width="15%">메뉴</th>
                <th width="25%">뷰이름</th>
                <th width ="40%">view component</th>
                <th>비고</th>
                </tr>
            </thead>
            <tbody id="contacts">
                <tr v-for="(view, index) in views" :key="index" :class="view.pub" >
                    <td>{{index + 1}}</td>
                    <td>{{view.menu}}</td>
                    <td>{{view.name}}</td>
                    <td><router-link :to="`${view.viewName}`" target="_blank">{{view.viewName}}</router-link></td>
                    <td>{{view.etc}}</td>
                </tr>
            </tbody>
            </table>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const views = ref(
  [
   {
       "menu": "login",
       "name": "로그인",
       "viewName":  "/login/Login" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "home",
       "name": "Home",
       "viewName":  "/home/Home" ,
       "etc": "",
       "pub": ""
   }, 
   {
       "menu": "dct",
       "name": "비밀문서 > 처리할일",
       "viewName":  "/dct/BmsTodolist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀관리 > 처리할일",
       "viewName":  "/dct/ManageTodolist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀문서 > 기안 > 비밀작성 (비밀관리카드)",
       "viewName":  "/dct/BmsDctcreatecard" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀문서 > 기안 > 임시저장함",
       "viewName":  "/dct/BmsDcttemplist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀관리카드",
       "viewName":  "/dct/BmsDcttempcard" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀문서 > 기안 > 기안완료함",
       "viewName":  "/dct/BmsDctreqcompletelist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀관리카드",
       "viewName":  "/dct/BmsDctreqcompletecard" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀문서 > 결재 > 결재대기함",
       "viewName":  "/dct/BmsDctwaitlist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀관리카드",
       "viewName":  "/dct/BmsDctwaitcard" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀문서 > 결재 > 진행비밀함",
       "viewName":  "/dct/BmsDctinglist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀관리카드",
       "viewName":  "/dct/BmsDctingcard" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀문서 > 결재 > 결재완료함",
       "viewName":  "/dct/BmsDctapprovalcompletelist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀관리카드",
       "viewName":  "/dct/BmsDctapprovalcompletecard" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀문서 > 발송 > 발송대기함",
       "viewName":  "/dct/BmsDctsendwaitlist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀관리카드",
       "viewName":  "/dct/BmsDctsendwaitcard" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀문서 > 발송 > 발송현황함",
       "viewName":  "/dct/BmsDctsendcurrentlist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "송증/접수증",
       "viewName":  "/dct/BmsDctreceipt" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀배포카드",
       "viewName":  "/dct/BmsDctdistributecard" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "오프라인발송 인쇄요청",
       "viewName":  "/dct/BmsDctoffsend" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "dct",
       "name": "오프라인발송 일괄인쇄요청",
       "viewName":  "/dct/BmsDctoffsendall" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "dct",
       "name": "비밀문서 > 발송 > 반송함",
       "viewName":  "/dct/BmsDctreturnlist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀문서 > 접수 > 배부함",
       "viewName":  "/dct/BmsDctdistributelist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀문서 > 접수 > 접수대기함",
       "viewName":  "/dct/BmsDctreceiptlist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀관리카드",
       "viewName":  "/dct/BmsReceiptdetailcard" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀문서 > 접수 > 과제미지정함",
       "viewName":  "/dct/BmsDctnotassigntasklist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀관리카드",
       "viewName":  "/dct/BmsNotassigntaskdetailcard" ,
       "etc": "",
       "pub": "chk"
   }, 
   {
       "menu": "dct",
       "name": "비밀문서 > 접수 > 결재대기함",
       "viewName":  "/dct/BmsTodolistapprready" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀관리카드",
       "viewName":  "/dct/BmsTodolistapprreadydetailcard" ,
       "etc": "",
       "pub": "chk"
   }, 
   {
       "menu": "dct",
       "name": "비밀문서 > 비밀열람함",
       "viewName":  "/dct/BmsDcttodolistread" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀관리카드",
       "viewName":  "/dct/BmsDctrdocdetailcard" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀문서 > 비밀관리기록부",
       "viewName":  "/dct/BmsDctmgmtregilist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀관리카드",
       "viewName":  "/dct/BmsDctmgmtregidetailcard" ,
       "etc": "",
       "pub": ""
   }, 
   {
       "menu": "dct",
       "name": "비밀문서 > 접수 > 재지정함",
       "viewName":  "/dct/BmsReprocesslist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀관리카드",
       "viewName":  "/dct/BmsReprocessdetailcard" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀문서 > 접수 > 반송대기함",
       "viewName":  "/dct/BmsDctsendbackreadylist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "dct",
       "name": "비밀관리카드",
       "viewName":  "/dct/BmsDctsendbackreadydetailcard" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "dct",
       "name": "비밀문서 > 비밀수발대장",
       "viewName":  "/dct/BmsDctsendrecvlist" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "trn",
       "name": "비밀관리 > 인계인수 > 개인소유문서현황",
       "viewName":  "/trn/BmsComprivateprossesionstatus" ,
       "etc": "",
       "pub": ""
   }, 
   {
       "menu": "trn",
       "name": "비밀관리 > 인계인수 >부서별 비밀보유현황",
       "viewName":  "/trn/BmsComstatdpt" ,
       "etc": "",
       "pub": ""
   }, 
   {
       "menu": "trn",
       "name": "비밀관리 > 인계인수 >인계인수서작성",
       "viewName":  "/trn/BmsComtransproclist" ,
       "etc": "",
       "pub": ""
   }, 
   {
       "menu": "trn",
       "name": "인계인수서작성",
       "viewName":  "/trn/BmsDctreqadd" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "trn",
       "name": "비밀관리 > 인계인수 > 처리할인계인수서(개인)",
       "viewName":  "/trn/BmsComtransproctodolist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "trn",
       "name": "인계인수서상세조회",
       "viewName":  "/trn/BmsTrndetailcard" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "trn",
       "name": "비밀관리 > 인계인수 > 처리할인계인수서(부서)",
       "viewName":  "/trn/BmsComtransprocdepttodolist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "trn",
       "name": "비밀관리 > 인계인수 > 요청한인계인수서",
       "viewName":  "/trn/BmsTrnreqlist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "trn",
       "name": "비밀관리 > 인계인수 > 처리한인계인수서",
       "viewName":  "/trn/BmsTrncompletelist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "trn",
       "name": "비밀관리 > 인계인수 > 임시저장함",
       "viewName":  "/trn/BmsTrntempsavelist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "trn",
       "name": "비밀관리 > 인계인수 > 인수현황함",
       "viewName":  "/trn/BmsTrncurrentlist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "trn",
       "name": "인계인수서상세조회",
       "viewName":  "/trn/BmsTrncurrentdetailcard" ,
       "etc": "",
       "pub": ""
   }, 
   {
       "menu": "com",
       "name": "코드분류관리",
       "viewName":  "/com/BmsDctcodecategorylist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "com",
       "name": "코드관리",
       "viewName":  "/com/BmsDctcodelist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "com",
       "name": "FAQ관리",
       "viewName":  "/com/BmsComfaqlist" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "com",
       "name": "FAQ상세",
       "viewName":  "/com/BmsComfaqdetail" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "com",
       "name": "FAQ등록",
       "viewName":  "/com/BmsComfaqinsert" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "com",
       "name": "FAQ수정",
       "viewName":  "/com/BmsComfaqupdate" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "com",
       "name": "FAQ공개대상조회",
       "viewName":  "/com/BmsComfaqorglist" ,
       "etc": ""
   },
   {
       "menu": "com",
       "name": "공지사항관리",
       "viewName":  "/com/BmsComnoticelist" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "com",
       "name": "공지사항상세",
       "viewName":  "/com/BmsComnoticedetail" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "com",
       "name": "공지사항등록",
       "viewName":  "/com/BmsComnoticeinsert" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "com",
       "name": "공지사항수정",
       "viewName":  "/com/BmsComnoticeupdate" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "com",
       "name": "Q&A관리",
       "viewName":  "/com/BmsComqnalist" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "com",
       "name": "Q&A상세",
       "viewName":  "/com/BmsComqnadetail" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "com",
       "name": "Q&A등록",
       "viewName":  "/com/BmsComqnainsert" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "com",
       "name": "Q&A수정",
       "viewName":  "/com/BmsComqnaupdate" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "com",
       "name": "사용자관리",
       "viewName":  "/com/BmsComuserlist" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "com",
       "name": "사용자상세",
       "viewName":  "/com/BmsComuserdetail" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "com",
       "name": "사용자등록",
       "viewName":  "/com/BmsComuserinsert" ,
       "etc": "",
       "pub": "fin"
   }, 
   {
       "menu": "com",
       "name": "사용자수정",
       "viewName":  "/com/BmsComuserupdate" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "com",
       "name": "사용자권한조회",
       "viewName":  "/com/BmsComuservgrouplist" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "부서관리",
       "viewName":  "/com/BmsComorganizationlist" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "com",
       "name": "부서상세",
       "viewName":  "/com/BmsComorganizationdetail" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "com",
       "name": "부서등록",
       "viewName":  "/com/BmsComorganizationinsert" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "com",
       "name": "부서수정",
       "viewName":  "/com/BmsComorganizationupdate" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "com",
       "name": "관인관리",
       "viewName":  "/com/BmsComgwanlist" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "관인상세",
       "viewName":  "/com/BmsComgwandetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "관인등록",
       "viewName":  "/com/BmsComgwaninsert" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "관인수정",
       "viewName":  "/com/BmsComgwanupdate" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "서명인관리",
       "viewName":  "/com/BmsComsignlist" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "서명인상세",
       "viewName":  "/com/BmsComsigndetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "서명인등록",
       "viewName":  "/com/BmsComsigninsert" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "서명인수정",
       "viewName":  "/com/BmsComsignupdate" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "메뉴관리",
       "viewName":  "/com/BmsCommenulist" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "메뉴상세",
       "viewName":  "/com/BmsCommenudetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "메뉴등록",
       "viewName":  "/com/BmsCommenuinsert" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "메뉴수정",
       "viewName":  "/com/BmsCommenuupdate" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "실시간접속현황",
       "viewName":  "/com/BmsComrealtimelog" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "시스템접속로그",
       "viewName":  "/com/BmsComsystemloginlog" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "서식관리",
       "viewName":  "/com/BmsDctformlist" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "서식상세",
       "viewName":  "/com/BmsDctformdetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "서식등록",
       "viewName":  "/com/BmsDctforminsert" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "com",
       "name": "서식수정",
       "viewName":  "/com/BmsDctformupdate" ,
       "etc": "",
       "pub": ""
   }, 
   {
       "menu": "use",
       "name": "처리할요청서",
       "viewName":  "/use/BmsUsereqlist" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "use",
       "name": "반출요청서 상세",
       "viewName":  "/use/BmsUsereqlenddetail" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "use",
       "name": "재사용요청서 상세",
       "viewName":  "/use/BmsUsereqrecycledetail" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "use",
       "name": "열람요청서 상세",
       "viewName":  "/use/BmsUsereqviewdetail" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "use",
       "name": "인쇄요청서 상세",
       "viewName":  "/use/BmsUsereqprintdetail" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "use",
       "name": "기안한요청서",
       "viewName":  "/use/BmsUsedraftlist" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "use",
       "name": "반출요청서 상세",
       "viewName":  "/use/BmsUsedraftlenddetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "재사용요청서 상세",
       "viewName":  "/use/BmsUsedraftrecycledetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "열람요청서 상세",
       "viewName":  "/use/BmsUsedraftviewdetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "인쇄요청서 상세",
       "viewName":  "/use/BmsUsedraftprintdetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "처리한요청서",
       "viewName":  "/use/BmsUseproclist" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "use",
       "name": "반출요청서 상세",
       "viewName":  "/use/BmsUseproclenddetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "재사용요청서 상세",
       "viewName":  "/use/BmsUseprocrecycledetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "열람요청서 상세",
       "viewName":  "/use/BmsUseprocviewdetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "인쇄요청서 상세",
       "viewName":  "/use/BmsUseprocprintdetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "임시저장함",
       "viewName":  "/use/BmsUsetemplist" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "use",
       "name": "반출요청서 상세",
       "viewName":  "/use/BmsUsetemplenddetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "재사용요청서 상세",
       "viewName":  "/use/BmsUsetemprecycledetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "열람요청서 상세",
       "viewName":  "/use/BmsUsetempviewdetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "인쇄요청서 상세",
       "viewName":  "/use/BmsUsetempprintdetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "요청서완료처리",
       "viewName":  "/use/BmsUsecompletelist" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "use",
       "name": "반출요청서 상세",
       "viewName":  "/use/BmsUsecompletelenddetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "재사용요청서 상세",
       "viewName":  "/use/BmsUsecompleterecycledetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "열람요청서 상세",
       "viewName":  "/use/BmsUsecompleteviewdetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "인쇄요청서 상세",
       "viewName":  "/use/BmsUsecompleteprintdetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "예고문도래목록",
       "viewName":  "/use/BmsCominglist" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "use",
       "name": "재분류예고문요청",
       "viewName":  "/use/BmsComingreclassdetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "존안요청",
       "viewName":  "/use/BmsComingcnsrvdetail" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "파기요청",
       "viewName":  "/use/BmsComingdestructdetail" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "use",
       "name": "이관요청",
       "viewName":  "/use/BmsComingtransdetail" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "use",
       "name": "비밀문서 사용요청 - 열람요청목록",
       "viewName":  "/use/BmsUseViewReqList" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "비밀문서 사용요청 - 재사용요청목록",
       "viewName":  "/use/BmsReUseReqList" ,
       "etc": "",
       "pub": "fin"
   },
   {
       "menu": "use",
       "name": "비밀문서 사용요청 - 인쇄요청목록",
       "viewName":  "/use/BmsUsePrintReqList" ,
       "etc": "",
       "pub": ""
   },
   {
       "menu": "use",
       "name": "비밀문서 사용요청 - 반출요청목록",
       "viewName":  "/use/BmsUseTakeOutReqList" ,
       "etc": "",
       "pub": ""
   },
    {
        "menu": "use",
        "name": "비밀관리 > 전자열람요청",
        "viewName":  "/use/BmsUsedigitalviewreq" ,
        "etc": "",
       "pub": ""
    },
    {
        "menu": "use",
        "name": "비밀관리 > 비전자열람요청",
        "viewName":  "/use/BmsUsenondigitalviewreq" ,
        "etc": "",
       "pub": "fin"
    },
    {
        "menu": "use",
        "name": "비밀관리 > 재사용요청",
        "viewName":  "/use/BmsReusereq" ,
        "etc": "",
       "pub": "fin"
    },
    {
        "menu": "use",
        "name": "비밀관리 > 전자인쇄요청",
        "viewName":  "/use/BmsUsedigitalprintreq" ,
        "etc": "",
       "pub": ""
    },
    {
        "menu": "use",
        "name": "비밀관리 > 비전자인쇄요청",
        "viewName":  "/use/BmsUsenondigitalprintreq" ,
        "etc": "",
       "pub": ""
    },
    {
        "menu": "use",
        "name": "비밀관리 > 반출요청",
        "viewName":  "/use/BmsUsetakeoutreq" ,
        "etc": "",
       "pub": ""
    },
  ]
)

onMounted(async ()=>{
  console.log(views.value)
})

</script>

<style lang="scss" scoped>h3 {
  text-align: center;
}
.table {
  text-align: center;
  position: relative;
}
table {
  width: 85%;
  border: 1px solid #444444;
  border-collapse: collapse;
  margin-left: auto;
  margin-right: auto;
}
th, td {
  border: 1px solid #444444;
  padding: 10px;
}
tbody td {
    background: #fff;
}
.fin {
    td {
        background: #ebf7ff;
    }
}
.ing {
    td {
        background: #f8ffeb;
    }
}
.chk {
    td {
        background: #ffebfc;
    }
}
.pub-status {
    width: 85%;
    margin: 0 auto;
    text-align: right;
    border: 1px solid #444;
    background: #fff;
    > span {
        display: block;
        line-height: 20px;
        padding: 10px;
        &:before {
            content: "";
            display: inline-block;
            width: 20px;
            height: 20px;
            margin-right: 10px;
            border: 1px solid #444;
            vertical-align: top;
        }
        &.pub:before {
            background: #ebf7ff;
        }
        &.ing:before {
            background: #f8ffeb;
        }
        &.chk:before {
            background: #ffebfc;
        }
    }
}
</style>
