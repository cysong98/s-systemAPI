/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

import BmsTodolistView from "@/views/dct/BmsTodolistView.vue";
import ManageTodolistView from "@/views/dct/ManageTodolistView.vue";
import BmsDctcreatecardView from "@/views/dct/BmsDctcreatecardView.vue";
import BmsDctReceivedOfflinecreatecardView from "@/views/dct/BmsDctReceivedOfflinecreatecardView.vue";
import BmsDctProductOfflinecreatecardView from "@/views/dct/BmsDctProductOfflinecreatecardView.vue";
import BmsDcttemplistView from "@/views/dct/BmsDcttemplistView.vue";
import BmsDcttempcardView from "@/views/dct/BmsDcttempcardView.vue";
import BmsDctreqcompletelistView from "@/views/dct/BmsDctreqcompletelistView.vue";
import BmsDctreqcompletecardView from "@/views/dct/BmsDctreqcompletecardView.vue";
import BmsDctReceivedOfflinecompletecardView from "@/views/dct/BmsDctReceivedOfflinecompletecardView.vue";
import BmsDctReceivedOfflinesharecardView from "@/views/dct/BmsDctReceivedOfflinesharecardView.vue";
import BmsDctProductOfflinecompletecardView from "@/views/dct/BmsDctProductOfflinecompletecardView.vue";
import BmsDctReceivedOfflineupdatecardView from "@/views/dct/BmsDctReceivedOfflineupdatecardView.vue";
import BmsDctwaitlistView from "@/views/dct/BmsDctwaitlistView.vue";
import BmsDctwaitcardView from "@/views/dct/BmsDctwaitcardView.vue";
import BmsDctinglistView from "@/views/dct/BmsDctinglistView.vue";
import BmsDctingcardView from "@/views/dct/BmsDctingcardView.vue";
import BmsDctapprovalcompletelistView from "@/views/dct/BmsDctapprovalcompletelistView.vue";
import BmsDctapprovalcompletecardView from "@/views/dct/BmsDctapprovalcompletecardView.vue";
import BmsDctsendwaitlistView from "@/views/dct/BmsDctsendwaitlistView.vue";
import BmsDctsendwaitcardView from "@/views/dct/BmsDctsendwaitcardView.vue";
import BmsDctsendcurrentlistView from "@/views/dct/BmsDctsendcurrentlistView.vue";
import BmsDctreceiptView from "@/views/dct/BmsDctreceiptView.vue";
import BmsReceiptdetailcardView from "@/views/dct/BmsReceiptdetailcardView.vue";
import BmsDctreturnlistView from "@/views/dct/BmsDctreturnlistView.vue";
import BmsDctdistributelistView from "@/views/dct/BmsDctdistributelistView.vue";
import BmsDctreceiptlistView from "@/views/dct/BmsDctreceiptlistView.vue";
import BmsDcttodolistreadView from "@/views/dct/BmsDcttodolistreadView.vue";
import BmsDctRdocDetailCardView from "@/views/dct/BmsDctrdocdetailcardView.vue";
import BmsDctmgmtregilistView from "@/views/dct/BmsDctmgmtregilistView.vue";
import BmsDctmgmtregidetailcardView from "@/views/dct/BmsDctmgmtregidetailcardView.vue";
import BmsReprocesslistView from "@/views/dct/BmsReprocesslistView.vue";
import BmsReprocessCardView from "@/views/dct/BmsReprocessCardView.vue";
import BmsDctCardView from "@/views/dct/BmsDctCardView.vue";
import BmsDctreqaddView from "@/views/trn/BmsDctreqaddView.vue";
import BmsComtransproctodolistView from "@/views/trn/BmsComtransproctodolistView.vue";
import BmsTrndetailcardView from "@/views/trn/BmsTrndetailcardView.vue";
import BmsTrnreqlistView from "@/views/trn/BmsTrnreqlistView.vue";
import BmsTrncompletelistView from "@/views/trn/BmsTrncompletelistView.vue";
import BmsTrncurrentlistView from "@/views/trn/BmsTrncurrentlistView.vue";
import BmsDctcodecategorylistView from "@/views/com/BmsDctcodecategorylistView.vue";
import BmsDctcodelistView from "@/views/com/BmsDctcodelistView.vue";
import BmsComfaqlistView from "@/views/com/BmsComfaqlistView.vue";
import BmsComfaqdetailView from "@/views/com/BmsComfaqdetailView.vue";
import BmsComfaqinsertView from "@/views/com/BmsComfaqinsertView.vue";
import BmsComfaqupdateView from "@/views/com/BmsComfaqupdateView.vue";
import BmsComnoticelistView from "@/views/com/BmsComnoticelistView.vue";
import BmsComnoticedetailView from "@/views/com/BmsComnoticedetailView.vue";
import BmsComnoticeinsertView from "@/views/com/BmsComnoticeinsertView.vue";
import BmsComnoticeupdateView from "@/views/com/BmsComnoticeupdateView.vue";
import BmsComqnalistView from "@/views/com/BmsComqnalistView.vue";
import BmsComqnadetailView from "@/views/com/BmsComqnadetailView.vue";
import BmsComqnainsertView from "@/views/com/BmsComqnainsertView.vue";
import BmsComqnaupdateView from "@/views/com/BmsComqnaupdateView.vue";
import BmsComuserdetailView from "@/views/com/BmsComuserdetailView.vue";
import BmsComuserinsertView from "@/views/com/BmsComuserinsertView.vue";
import BmsComuserupdateView from "@/views/com/BmsComuserupdateView.vue";
import BmsComuservgrouplistView from "@/views/com/BmsComuservgrouplistView.vue";
import BmsComorganizationlistView from "@/views/com/BmsComorganizationlistView.vue";
import BmsComorganizationdetailView from "@/views/com/BmsComorganizationdetailView.vue";
import BmsComorganizationinsertView from "@/views/com/BmsComorganizationinsertView.vue";
import BmsComorganizationupdateView from "@/views/com/BmsComorganizationupdateView.vue";
import BmsComgwanlistView from "@/views/com/BmsComgwanlistView.vue";
import BmsComgwandetailView from "@/views/com/BmsComgwandetailView.vue";
import BmsComgwaninsertView from "@/views/com/BmsComgwaninsertView.vue";
import BmsComgwanupdateView from "@/views/com/BmsComgwanupdateView.vue";
import BmsComsignlistView from "@/views/com/BmsComsignlistView.vue";
import BmsComsigndetailView from "@/views/com/BmsComsigndetailView.vue";
import BmsComsigninsertView from "@/views/com/BmsComsigninsertView.vue";
import BmsComsignupdateView from "@/views/com/BmsComsignupdateView.vue";
import BmsComlogolistView from "@/views/com/BmsComlogolistView.vue";
import BmsComlogodetailView from "@/views/com/BmsComlogodetailView.vue";
import BmsComlogoinsertView from "@/views/com/BmsComlogoinsertView.vue";
import BmsComlogoupdateView from "@/views/com/BmsComlogoupdateView.vue";
import BmsComgradelistView from "@/views/com/BmsComgradelistView.vue";
import BmsComgradedetailView from "@/views/com/BmsComgradedetailView.vue";
import BmsComgradeinsertView from "@/views/com/BmsComgradeinsertView.vue";
import BmsComgradeupdateView from "@/views/com/BmsComgradeupdateView.vue";
import BmsComjobgubunlistView from "@/views/com/BmsComjobgubunlistView.vue";
import BmsComjobgubundetailView from "@/views/com/BmsComjobgubundetailView.vue";
import BmsComjobgubuninsertView from "@/views/com/BmsComjobgubuninsertView.vue";
import BmsComjobgubunupdateView from "@/views/com/BmsComjobgubunupdateView.vue";
import BmsCommenulistView from "@/views/com/BmsCommenulistView.vue";
import BmsCommenudetailView from "@/views/com/BmsCommenudetailView.vue";
import BmsCommenuinsertView from "@/views/com/BmsCommenuinsertView.vue";
import BmsCommenuupdateView from "@/views/com/BmsCommenuupdateView.vue";
import BmsComrealtimelogView from "@/views/com/BmsComrealtimelogView.vue";
import BmsComsystemloginlogView from "@/views/com/BmsComsystemloginlogView.vue";
import BmsDctformlistView from "@/views/com/BmsDctformlistView.vue";
import BmsDctformdetailView from "@/views/com/BmsDctformdetailView.vue";
import BmsDctforminsertView from "@/views/com/BmsDctforminsertView.vue";
import BmsDctformupdateView from "@/views/com/BmsDctformupdateView.vue";
import BmsUsetempcardView from "@/views/use/BmsUsetempcardView.vue";
import BmsUsereqlistView from "@/views/use/BmsUsereqlistView.vue";
import BmsUsereqlenddetailView from "@/views/use/BmsUsereqlenddetailView.vue";
import BmsUsereqlendbackdetailView from "@/views/use/BmsUsereqlendbackdetailView.vue";
import BmsUsereqrecycledetailView from "@/views/use/BmsUsereqrecycledetailView.vue";
import BmsUsereqviewdetailView from "@/views/use/BmsUsereqviewdetailView.vue";
import BmsUsereqprintdetailView from "@/views/use/BmsUsereqprintdetailView.vue";
import BmsUsedraftlistView from "@/views/use/BmsUsedraftlistView.vue";
import BmsUseproclistView from "@/views/use/BmsUseproclistView.vue";
import BmsUsecompletelistView from "@/views/use/BmsUsecompletelistView.vue";
import BmsCominglistView from "@/views/use/BmsCominglistView.vue";
import BmsComingreclassdetailView from "@/views/use/BmsComingreclassdetailView.vue";
import BmsComingcnsrvdetailView from "@/views/use/BmsComingcnsrvdetailView.vue";
import BmsComingdestructdetailView from "@/views/use/BmsComingdestructdetailView.vue";
import BmsComingtransdetailView from "@/views/use/BmsComingtransdetailView.vue";
import BmsComingtransdelayView from "@/views/use/BmsComingtransdelayView.vue";
import BmsComingTransWaitReqView from "@/views/use/BmsComingTransWaitReqView.vue";
import BmsUseViewReqListView from "@/views/use/BmsUseViewReqListView.vue";
import BmsReUseReqListView from "@/views/use/BmsReUseReqListView.vue";
import BmsUsePrintReqListView from "@/views/use/BmsUsePrintReqListView.vue";
import BmsUseTakeOutReqListView from "@/views/use/BmsUseTakeOutReqListView.vue";
import BmsUsedigitalviewreqView from "@/views/use/BmsUsedigitalviewreqView.vue";
import BmsUsenondigitalviewreqView from "@/views/use/BmsUsenondigitalviewreqView.vue";
import BmsReusereqView from "@/views/use/BmsReusereqView.vue";
import BmsUsedigitalprintreqView from "@/views/use/BmsUsedigitalprintreqView.vue";
import BmsUsenondigitalprintreqView from "@/views/use/BmsUsenondigitalprintreqView.vue";
import BmsUsetakeoutreqView from "@/views/use/BmsUsetakeoutreqView.vue";
import BmsUseNondigitaltakeoutreqView from "@/views/use/BmsUseNondigitaltakeoutreqView.vue";
import BmsDctreqaddronlyView from "@/views/trn/BmsDctreqaddronlyView.vue";
import BmsUsereqreclassdetailView from "@/views/use/BmsUsereqreclassdetailView.vue";
import BmsUsereqdestructdetailView from "@/views/use/BmsUsereqdestructdetailView.vue";
import BmsUsereqcnsrvdetailView from "@/views/use/BmsUsereqcnsrvdetailView.vue";
import BmsUsereqtranswaitdetailView from "@/views/use/BmsUsereqtranswaitdetailView.vue";
import BmsUsereqtransdetailView from "@/views/use/BmsUsereqtransdetailView.vue";
import BmsUseConserveMgmtRegiListView from "@/views/use/BmsUseConserveMgmtRegiListView.vue";
import BmsUseTransMgmtRegiListView from "@/views/use/BmsUseTransMgmtRegiListView.vue";
import BmsDctreceiptcurrentlistView from "@/views/dct/BmsDctreceiptcurrentlistView.vue";
import BmsTrncntlistView from "@/views/trn/BmsTrncntlistView.vue";

const DetailLayoutRoutes = [
  {
    path: "/dct/BmsTodolist",
    name: "BmsTodolist",
    component: BmsTodolistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/ManageTodolist",
    name: "ManageTodolist",
    component: ManageTodolistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctcreatecard",
    name: "BmsDctcreatecard",
    component: BmsDctcreatecardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctReceivedOfflinecreatecard",
    name: "BmsDctReceivedOfflinecreatecard",
    component: BmsDctReceivedOfflinecreatecardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctProductOfflinecreatecard",
    name: "BmsDctProductOfflinecreatecard",
    component: BmsDctProductOfflinecreatecardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDcttemplist",
    name: "BmsDcttemplist",
    component: BmsDcttemplistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDcttempcard",
    name: "BmsDcttempcard",
    component: BmsDcttempcardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctreqcompletelist",
    name: "BmsDctreqcompletelist",
    component: BmsDctreqcompletelistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctreqcompletecard",
    name: "BmsDctreqcompletecard",
    component: BmsDctreqcompletecardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctReceivedOfflinecompletecard",
    name: "BmsDctReceivedOfflinecompletecard",
    component: BmsDctReceivedOfflinecompletecardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctReceivedOfflinesharecard",
    name: "BmsDctReceivedOfflinesharecard",
    component: BmsDctReceivedOfflinesharecardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctProductOfflinecompletecard",
    name: "BmsDctProductOfflinecompletecard",
    component: BmsDctProductOfflinecompletecardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctReceivedOfflineupdatecard",
    name: "BmsDctReceivedOfflineupdatecard",
    component: BmsDctReceivedOfflineupdatecardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctwaitlist",
    name: "BmsDctwaitlist",
    component: BmsDctwaitlistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctwaitcard",
    name: "BmsDctwaitcard",
    component: BmsDctwaitcardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctinglist",
    name: "BmsDctinglist",
    component: BmsDctinglistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctingcard",
    name: "BmsDctingcard",
    component: BmsDctingcardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctapprovalcompletelist",
    name: "BmsDctapprovalcompletelist",
    component: BmsDctapprovalcompletelistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctapprovalcompletecard",
    name: "BmsDctapprovalcompletecard",
    component: BmsDctapprovalcompletecardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctsendwaitlist",
    name: "BmsDctsendwaitlist",
    component: BmsDctsendwaitlistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctsendwaitcard",
    name: "BmsDctsendwaitcard",
    component: BmsDctsendwaitcardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctsendcurrentlist",
    name: "BmsDctsendcurrentlist",
    component: BmsDctsendcurrentlistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctreceipt",
    name: "BmsDctreceipt",
    component: BmsDctreceiptView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctreturnlist",
    name: "BmsDctreturnlist",
    component: BmsDctreturnlistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsReceiptdetailcard",
    name: "BmsReceiptdetailcard",
    component: BmsReceiptdetailcardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctdistributelist",
    name: "BmsDctdistributelist",
    component: BmsDctdistributelistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctreceiptlist",
    name: "BmsDctreceiptlist",
    component: BmsDctreceiptlistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDcttodolistread",
    name: "BmsDcttodolistread",
    component: BmsDcttodolistreadView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctRdocDetailCard",
    name: "BmsDctRdocDetailCard",
    component: BmsDctRdocDetailCardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctmgmtregilist",
    name: "BmsDctmgmtregilist",
    component: BmsDctmgmtregilistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctmgmtregidetailcard",
    name: "BmsDctmgmtregidetailcard",
    component: BmsDctmgmtregidetailcardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsReprocesslist",
    name: "BmsReprocesslist",
    component: BmsReprocesslistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsReprocessCard",
    name: "BmsReprocessCard",
    component: BmsReprocessCardView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/trn/BmsDctreqadd",
    name: "BmsDctreqadd",
    component: BmsDctreqaddView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/trn/BmsComtransproctodolist",
    name: "BmsComtransproctodolist",
    component: BmsComtransproctodolistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/trn/BmsTrndetailcard",
    name: "BmsTrndetailcard",
    component: BmsTrndetailcardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/trn/BmsTrnreqlist",
    name: "BmsTrnreqlist",
    component: BmsTrnreqlistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/trn/BmsTrncompletelist",
    name: "BmsTrncompletelist",
    component: BmsTrncompletelistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/trn/BmsTrncurrentlist",
    name: "BmsTrncurrentlist",
    component: BmsTrncurrentlistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsDctcodecategorylist",
    name: "BmsDctcodecategorylist",
    component: BmsDctcodecategorylistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsDctcodelist",
    name: "BmsDctcodelist",
    component: BmsDctcodelistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctCard",
    name: "BmsDctCard",
    component: BmsDctCardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComfaqlist",
    name: "BmsComfaqlist",
    component: BmsComfaqlistView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComfaqdetail",
    name: "BmsComfaqdetail",
    component: BmsComfaqdetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComfaqinsert",
    name: "BmsComfaqinsert",
    component: BmsComfaqinsertView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComfaqupdate",
    name: "BmsComfaqupdate",
    component: BmsComfaqupdateView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComnoticelist",
    name: "BmsComnoticelist",
    component: BmsComnoticelistView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComnoticedetail",
    name: "BmsComnoticedetail",
    component: BmsComnoticedetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComnoticeinsert",
    name: "BmsComnoticeinsert",
    component: BmsComnoticeinsertView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComnoticeupdate",
    name: "BmsComnoticeupdate",
    component: BmsComnoticeupdateView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComqnalist",
    name: "BmsComqnalist",
    component: BmsComqnalistView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComqnadetail",
    name: "BmsComqnadetail",
    component: BmsComqnadetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComqnainsert",
    name: "BmsComqnainsert",
    component: BmsComqnainsertView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComqnaupdate",
    name: "BmsComqnaupdate",
    component: BmsComqnaupdateView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComuserdetail",
    name: "BmsComuserdetail",
    component: BmsComuserdetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComuserinsert",
    name: "BmsComuserinsert",
    component: BmsComuserinsertView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComuserupdate",
    name: "BmsComuserupdate",
    component: BmsComuserupdateView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComuservgrouplist",
    name: "BmsComuservgrouplist",
    component: BmsComuservgrouplistView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComorganizationlist",
    name: "BmsComorganizationlist",
    component: BmsComorganizationlistView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComorganizationdetail",
    name: "BmsComorganizationdetail",
    component: BmsComorganizationdetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComorganizationinsert",
    name: "BmsComorganizationinsert",
    component: BmsComorganizationinsertView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComorganizationupdate",
    name: "BmsComorganizationupdate",
    component: BmsComorganizationupdateView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComgwanlist",
    name: "BmsComgwanlist",
    component: BmsComgwanlistView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComgwandetail",
    name: "BmsComgwandetail",
    component: BmsComgwandetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComgwaninsert",
    name: "BmsComgwaninsert",
    component: BmsComgwaninsertView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComgwanupdate",
    name: "BmsComgwanupdate",
    component: BmsComgwanupdateView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComsignlist",
    name: "BmsComsignlist",
    component: BmsComsignlistView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComsigndetail",
    name: "BmsComsigndetail",
    component: BmsComsigndetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComsigninsert",
    name: "BmsComsigninsert",
    component: BmsComsigninsertView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComsignupdate",
    name: "BmsComsignupdate",
    component: BmsComsignupdateView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComlogolist",
    name: "BmsComlogolist",
    component: BmsComlogolistView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComlogodetail",
    name: "BmsComlogodetail",
    component: BmsComlogodetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComlogoinsert",
    name: "BmsComlogoinsert",
    component: BmsComlogoinsertView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComlogoupdate",
    name: "BmsComlogoupdate",
    component: BmsComlogoupdateView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComgradelist",
    name: "BmsComgradelist",
    component: BmsComgradelistView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComgradedetail",
    name: "BmsComgradedetail",
    component: BmsComgradedetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComgradeinsert",
    name: "BmsComgradeinsert",
    component: BmsComgradeinsertView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComgradeupdate",
    name: "BmsComgradeupdate",
    component: BmsComgradeupdateView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComjobgubunlist",
    name: "BmsComjobgubunlist",
    component: BmsComjobgubunlistView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComjobgubundetail",
    name: "BmsComjobgubundetail",
    component: BmsComjobgubundetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComjobgubuninsert",
    name: "BmsComjobgubuninsert",
    component: BmsComjobgubuninsertView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComjobgubunupdate",
    name: "BmsComjobgubunupdate",
    component: BmsComjobgubunupdateView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsCommenulist",
    name: "BmsCommenulist",
    component: BmsCommenulistView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsCommenudetail",
    name: "BmsCommenudetail",
    component: BmsCommenudetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsCommenuinsert",
    name: "BmsCommenuinsert",
    component: BmsCommenuinsertView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsCommenuupdate",
    name: "BmsCommenuupdate",
    component: BmsCommenuupdateView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComrealtimelog",
    name: "BmsComrealtimelog",
    component: BmsComrealtimelogView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsComsystemloginlog",
    name: "BmsComsystemloginlog",
    component: BmsComsystemloginlogView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsDctformlist",
    name: "BmsDctformlist",
    component: BmsDctformlistView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsDctformdetail",
    name: "BmsDctformdetail",
    component: BmsDctformdetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsDctforminsert",
    name: "BmsDctforminsert",
    component: BmsDctforminsertView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/com/BmsDctformupdate",
    name: "BmsDctformupdate",
    component: BmsDctformupdateView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsetempcard",
    name: "BmsUsetempcard",
    component: BmsUsetempcardView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsereqlist",
    name: "BmsUsereqlist",
    component: BmsUsereqlistView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsereqlenddetail",
    name: "BmsUsereqlenddetail",
    component: BmsUsereqlenddetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsereqlendbackdetail",
    name: "BmsUsereqlendbackdetail",
    component: BmsUsereqlendbackdetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsereqrecycledetail",
    name: "BmsUsereqrecycledetail",
    component: BmsUsereqrecycledetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsereqviewdetail",
    name: "BmsUsereqviewdetail",
    component: BmsUsereqviewdetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsereqprintdetail",
    name: "BmsUsereqprintdetail",
    component: BmsUsereqprintdetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsedraftlist",
    name: "BmsUsedraftlist",
    component: BmsUsedraftlistView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUseproclist",
    name: "BmsUseproclist",
    component: BmsUseproclistView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsecompletelist",
    name: "BmsUsecompletelist",
    component: BmsUsecompletelistView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsCominglist",
    name: "BmsCominglist",
    component: BmsCominglistView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsComingreclassdetail",
    name: "BmsComingreclassdetail",
    component: BmsComingreclassdetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsComingcnsrvdetail",
    name: "BmsComingcnsrvdetail",
    component: BmsComingcnsrvdetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsComingdestructdetail",
    name: "BmsComingdestructdetail",
    component: BmsComingdestructdetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsComingtransdetail",
    name: "BmsComingtransdetail",
    component: BmsComingtransdetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUseViewReqList",
    name: "BmsUseViewReqList",
    component: BmsUseViewReqListView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsReUseReqList",
    name: "BmsReUseReqList",
    component: BmsReUseReqListView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsePrintReqList",
    name: "BmsUsePrintReqList",
    component: BmsUsePrintReqListView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUseTakeOutReqList",
    name: "BmsUseTakeOutReqList",
    component: BmsUseTakeOutReqListView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsedigitalviewreq",
    name: "BmsUsedigitalviewreq",
    component: BmsUsedigitalviewreqView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsenondigitalviewreq",
    name: "BmsUsenondigitalviewreq",
    component: BmsUsenondigitalviewreqView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsReusereq",
    name: "BmsReusereq",
    component: BmsReusereqView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsedigitalprintreq",
    name: "BmsUsedigitalprintreq",
    component: BmsUsedigitalprintreqView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsenondigitalprintreq",
    name: "BmsUsenondigitalprintreq",
    component: BmsUsenondigitalprintreqView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsetakeoutreq",
    name: "BmsUsetakeoutreq",
    component: BmsUsetakeoutreqView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUseNondigitaltakeoutreq",
    name: "BmsUsenondigitaltakeoutreq",
    component: BmsUseNondigitaltakeoutreqView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/trn/BmsDctreqaddronly",
    name: "BmsDctreqaddronly",
    component: BmsDctreqaddronlyView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsComingtransdelay",
    name: "BmsComingtransdelay",
    component: BmsComingtransdelayView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsComingtranswait",
    name: "BmsComingtranswait",
    component: BmsComingTransWaitReqView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsereqreclassdetail",
    name: "BmsUsereqreclassdetail",
    component: BmsUsereqreclassdetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsereqdestructdetail",
    name: "BmsUsereqdestructdetail",
    component: BmsUsereqdestructdetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsereqcnsrvdetail",
    name: "BmsUsereqcnsrvdetail",
    component: BmsUsereqcnsrvdetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsereqtranswaitdetail",
    name: "BmsUsereqtranswaitdetail",
    component: BmsUsereqtranswaitdetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUsereqtransdetail",
    name: "BmsUsereqtransdetail",
    component: BmsUsereqtransdetailView,
    props: true,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUseConserveMgmtRegiList",
    name: "BmsUseConserveMgmtRegiList",
    component: BmsUseConserveMgmtRegiListView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/use/BmsUseTransMgmtRegiList",
    name: "BmsUseTransMgmtRegiList",
    component: BmsUseTransMgmtRegiListView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/dct/BmsDctreceiptcurrentlist",
    name: "BmsDctreceiptcurrentlist",
    component: BmsDctreceiptcurrentlistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },
  {
    path: "/trn/Bmstrncntlist",
    name: "Bmstrncntlist",
    component: BmsTrncntlistView,
    meta: {
      layout: "DetailLayout",
      requiresAuth: true,
    },
  },

];
export { DetailLayoutRoutes };
