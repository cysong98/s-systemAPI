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
  baseUrl: API_BASE_URL + "/bms/use/"
};

const useAPIDFN = {
  useAPI: (APIName, conditions, paths) => {
    return useAPI[APIName](conditions, paths);
  }
};

const useAPI = {
  checkIsReusable: (conditions, paths) => {
    const uri = paths || "bmsdctapprreq/checkIsReusable";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectAlert: (conditions, paths) => {
    const uri = paths || "bmsdctmgmtregi/selectAlert";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectUseDigitalView: (conditions, paths) => {
    const uri = paths || "bmsdctmgmtregi/selectUseDigitalView";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectReUse: (conditions, paths) => {
    const uri = paths || "bmsdctmgmtregi/selectReUse";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectUsePrint: (conditions, paths) => {
    const uri = paths || "bmsdctmgmtregi/selectUsePrint";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectUseTakeOut: (conditions, paths) => {
    const uri = paths || "bmsdctmgmtregi/selectUseTakeOut";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectReclassPathInfo: (conditions, paths) => {
    const uri = paths || "bmsdctapprreq/selectReclassPathInfo";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectDestructPathInfo: (conditions, paths) => {
    const uri = paths || "bmsdctapprreq/selectDestructPathInfo";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectTransPathInfo: (conditions, paths) => {
    const uri = paths || "bmsdctapprreq/selectTransPathInfo";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectCnsrvPathInfo: (conditions, paths) => {
    const uri = paths || "bmsdctapprreq/selectCnsrvPathInfo";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectUseReqList: (conditions, paths) => {
    const uri = paths || "bmsdctapprreq/selectUseReqList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  deleteUseReqList: (conditions, paths) => {
    const uri = paths || "bmsdctapprreq/deleteUseReqList";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  selectUseReqViewDetail: (conditions, paths) => {
    const uri = paths || "bmsdctapprreq/selectUseReqViewDetail";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  insertUseTakeOut: (conditions, paths) => {
    const uri = paths || "bmsdctapprreqlend/insertUseTakeOut";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  insertUseNondigitalTakeOut: (conditions, paths) => {
    const uri = paths || "bmsdctapprreqlend/insertUseNondigitalTakeOut";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  checkPrtCnt: (conditions, paths) => {
    const uri = paths || "bmsdctapprreq/checkPrtCnt";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  updatePrtCnt: (conditions, paths) => {
    const uri = paths || "bmsdctapprreq/updatePrtCnt";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  updateUseReqViewAppStatusBIA: (conditions, paths) => {
    const uri = paths || "bmsdctapprreq/updateUseReqViewAppStatusBIA";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  updateUseReqViewAppPathApproveBIA: (conditions, paths) => {
    const uri = paths || "bmsdctapprreq/updateUseReqViewAppPathApproveBIA";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  insertReUseReq: (conditions, paths) => {
    const uri = paths || "bmsdctapprreqreuse/insertReUseReq";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  insertUseDigitalViewReq: (conditions, paths) => {
    const uri = paths || "bmsdctapprreqread/insertUseDigitalViewReq";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  insertUseNonDigitalViewReq: (conditions, paths) => {
    const uri = paths || "bmsdctapprreqread/insertUseNonDigitalViewReq";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  insertUseDigitalPrint: (conditions, paths) => {
    const uri = paths || "bmsdctapprreqprt/insertUseDigitalPrint";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  insertUseNonDigitalPrint: (conditions, paths) => {
    const uri = paths || "bmsdctapprreqprt/insertUseNonDigitalPrint";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  selectComingList: (conditions, paths) => {
    const uri = paths || "bmsdctmgmtregi/selectComingList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getDctMgmtRegi: (conditions, paths) => {
    const uri = paths || "bmsdctmgmtregi/getDctMgmtRegi";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getDctMgmtRegiAttr: (conditions, paths) => {
    const uri = paths || "bmsdctmgmtregi/getDctMgmtRegiAttr";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getDctMgmtRegiPrint: (conditions, paths) => {
    const uri = paths || "bmsdctmgmtregi/getDctMgmtRegiPrint";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  insertComingReclassApproveBIA: (conditions, paths) => {
    const uri = paths || "bmsdctapprreqreclass/insertComingReclassApproveBIA";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  insertComingCnsrvApproveBIA: (conditions, paths) => {
    const uri = paths || "bmsdctapprreqcnsrv/insertComingCnsrvApproveBIA";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  insertComingDestructApproveBIA: (conditions, paths) => {
    const uri = paths || "bmsdctapprreqdestruct/insertComingDestructApproveBIA";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  insertComingTransApproveBIA: (conditions, paths) => {
    const uri = paths || "bmsdctapprreqtrans/insertComingTransApproveBIA";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  insertComingTransDelayBIA: (conditions, paths) => {
    const uri = paths || "bmsdctapprreqtrans/insertComingTransDelayBIA";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  insertTransWait: (conditions, paths) => {
    const uri = paths || "bmsdctapprreqtrans/insertTransWait";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  selectUseConserveMgmtList: (conditions, paths) => {
    const uri = paths || "bmsdctconserve/selectUseConserveMgmtList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectUseTransMgmtList: (conditions, paths) => {
    const uri = paths || "bmsdctconserve/selectUseTransMgmtList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
};

export { useAPIDFN, useAPI };
