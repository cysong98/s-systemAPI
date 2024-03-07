/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

import axios from 'axios'
import { env } from "@/api/ApiEnv.js";
import { makeQueryString } from '@/utils/Query.js'
const API_BASE_URL = import.meta.env.VITE_APP_API_URL;
// const API_BASE_URL = import.meta.env.production.VITE_APP_API_URL;

const config = {
  baseUrl: API_BASE_URL + "/bms/dct/"
};

const dctAPIDFN = {
  dctAPI: (APIName, conditions, paths) => {
    return dctAPI[APIName](conditions, paths);
  }
};

const dctAPI = {
  selectRdocForKeywordSearch: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/selectRdocForKeywordSearch";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectSendWaitList: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/selectSendWaitList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  insertCardBIA: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/insertCardBIA";
    return axios.post(`${config.baseUrl}${uri}/`, conditions, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  insertOfflineRdoc: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/insertOfflineRdoc";
    return axios.post(`${config.baseUrl}${uri}/`, conditions, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  selectTempList: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/selectTempList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  deleteTempList: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/deleteTempList";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  deleteDraftCompleteList: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/deleteDraftCompleteList";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  rejectedDocumentToTempSave: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/rejectedDocumentToTempSave";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  insertTempList: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/insertTempList";
    return axios.post(`${config.baseUrl}${uri}/`, conditions, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  insertReuseDraft: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/insertReuseDraft";
    return axios.post(`${config.baseUrl}${uri}/`, conditions, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  selectTempCreateCardList: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/selectTempCreateCardList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getTempCreateCardList: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/getTempCreateCardList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getDctRdoc: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/getDctRdoc";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getDctAuthCard: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/getDctAuthCard";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectReferenceList: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/selectReferenceList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectReqCompleteList: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/selectReqCompleteList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectWaitList: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/selectWaitList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getReviewPrint: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/getReviewPrint";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  updateCardBIA1: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/updateCardBIA1";
    return axios.post(`${config.baseUrl}${uri}/`, conditions, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  updateCardBIA2: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/updateCardBIA2";
    return axios.post(`${config.baseUrl}${uri}/`, conditions, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  selectIngList: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/selectIngList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectApprovalCompleteList: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/selectApprovalCompleteList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  updateCardBIA: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/updateCardBIA";
    return axios.post(`${config.baseUrl}${uri}/`, conditions, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  selectSendCurrentList: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/selectSendCurrentList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectDistributeList: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/selectDistributeList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getReturnDetailCard: (conditions, paths) => {
    if (conditions.enfdocid == null || conditions.enfdocid == undefined || conditions.enfdocid == "") {
      const { enfdocid, ...newConditions } = conditions;
      conditions = newConditions;
    }
    const uri = paths || "bmsdctrdoc/getReturnDetailCard";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectdctRdoc: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/selectdctRdoc";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectDctPath: (conditions, paths) => {
    const uri = paths || "bmsdctpath/selectDctPath";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getDctRecv: (conditions, paths) => {
    const uri = paths || "bmsdctapprrecv/getDctRecv";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getDctOriRecv: (conditions, paths) => {
    const uri = paths || "bmsdctapprrecv/getDctOriRecv";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectDctReader: (conditions, paths) => {
    const uri = paths || "bmsdctrdocrcvs/selectDctReader";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  hasViewingPermission: (conditions, paths) => {
    const uri = paths || "bmsdctrdocrcvs/hasViewingPermission";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getDctAttr: (conditions, paths) => {
    const uri = paths || "bmsdctattr/getDctAttr";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getDctAttrAdd: (conditions, paths) => {
    const uri = paths || "bmsdctattr/getDctAttrAdd";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectDctFile: (conditions, paths) => {
    const uri = paths || "bmsdctfile/selectDctFile";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getHistCardInfo: (conditions, paths) => {
    const uri = paths || "bmsdcthistcard/getHistCardInfo";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectOrganization: (conditions, paths) => {
    const uri = paths || "comorganizationinfo/selectOrganization";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectOrganizationLDAP: (conditions, paths) => {
    const uri = paths || "comorganizationinfo/selectOrganizationLDAP";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectOrganizationInfo: (conditions, paths) => {
    const uri = paths || "comorganizationinfo/selectOrganizationInfo";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectManagerInfo: (conditions, paths) => {
    const uri = paths || "comorganizationinfo/selectManagerInfo";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectSymbol: (conditions, paths) => {
    const uri = paths || "comorganizationinfo/selectSymbol";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getDctSummary: (conditions, paths) => {
    const uri = paths || "bmsdctsummary/getDctSummary";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectTaskInfoTemp: (conditions, paths) => {
    const uri = paths || "bmstaskinfomationtemp/selectTaskInfoTemp";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectTaskInfo: (conditions, paths) => {
    const uri = paths || "bmstaskinfomationtemp/selectTaskInfo";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectMgmtRegiList: (conditions, paths) => {
    const uri = paths || "bmsdctmgmtregi/selectMgmtRegiList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectOtherDept: (conditions, paths) => {
    const uri = paths || "bmsdctmgmtregi/selectOtherDept";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getMgmtidByDocid: (conditions, paths) => {
    const uri = paths || "bmsdctmgmtregi/getMgmtidByDocid";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getMgmtRegi: (conditions, paths) => {
    const uri = paths || "bmsdctmgmtregi/getMgmtRegi";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectGawnin: (conditions, paths) => {
    const uri = paths || "comorgdetailinfo/selectGawnin";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectSign: (conditions, paths) => {
    const uri = paths || "comorgdetailinfo/selectSign";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectReturnList: (conditions, paths) => {
    const uri = paths || "bmsdctenfdoc/selectReturnList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectDctDistributeList: (conditions, paths) => {
    const uri = paths || "bmsdctenfdoc/selectDctDistributeList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectReceiptList: (conditions, paths) => {
    const uri = paths || "bmsdctenfdoc/selectReceiptList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  updateEnfDocRepath: (conditions, paths) => {
    const uri = paths || "bmsdctenfdoc/updateEnfDocRepath";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  selectReprocessList: (conditions, paths) => {
    const uri = paths || "bmsdctenfdoc/selectReprocessList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getReprocess: (conditions, paths) => {
    const uri = paths || "bmsdctenfdoc/getReprocess";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  documentProcBIA: (conditions, paths) => {
    const uri = paths || "bmsdctenfhistory/documentProcBIA";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  selectReceipt: (conditions, paths) => {
    const uri = paths || "bmsdctinvrcpt/selectReceipt";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectSendinfo: (conditions, paths) => {
    const uri = paths || "bmsdctdocsendinfo/selectSendinfo";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  updateCardHoldBIA: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/updateCardHoldBIA";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  selectReceiptCurrentList: (conditions, paths) => {
    const uri = paths || "bmsdctenfdoc/selectReceiptCurrentList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectMgmtRegiCnt: (conditions, paths) => {
    const uri = paths || "bmsdctmgmtregi/selectMgmtRegiCnt";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectMgmtRegiNonPageList: (conditions, paths) => {
    const uri = paths || "bmsdctmgmtregi/selectMgmtRegiNonPageList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  registerDocByFile: (ShieldViewerFormData) => {
    const uri = "shieldviewer/registerDocByFile";
    return axios.post(`${API_BASE_URL}/${uri}/`, ShieldViewerFormData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  checkDocConvert: (conditions) => {
    const uri = "shieldviewer/checkDocConvert";
    return axios.get(`${API_BASE_URL}/${uri}/${makeQueryString(conditions)}`);
  },
  viewDocument: (fid, saveAuthArray, printAuth) => {
    const uri = 'https://dms.s-system.kinac/dms/api/viewer?fid=' + fid + '&saveAuthArray=' + saveAuthArray + '&printAuth=' + printAuth;
    return uri;
  },
  selectRelayHis: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/selectRelayHis";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getTempSeq: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/getTempSeq";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getRecentMgmt: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/getRecentMgmt";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  resetTempSeq: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/resetTempSeq";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectSendFileList: (conditions, paths) => {
    const uri = paths || "bmsdctfile/selectSendFileList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  insertHistoryCard: (conditions, paths) => {
    const uri = paths || "bmsdctrdoc/insertHistoryCard";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  insertRecvInvrcpt: (conditions, paths) => {
    const uri = paths || "bmsdctinvrcpt/insertRecvInvrcpt";
    return axios.post(`${config.baseUrl}${uri}/`, conditions, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  selectCommonDctFile: (conditions, paths) => {
    const uri = paths || "bmsdctfile/selectCommonDctFile";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectDctEnfPath: (conditions, paths) => {
    const uri = paths || "bmsdctenfdoc/selectDctEnfPath";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },

};

export { dctAPIDFN, dctAPI };
