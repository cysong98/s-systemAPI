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
  baseUrl: API_BASE_URL + "/bms/trn/"
};

const trnAPIDFN = {
  trnAPI: (APIName, conditions, paths) => {
    return trnAPI[APIName](conditions, paths);
  }
};

const trnAPI = {
  selectTransProcToDoList: (conditions, paths) => {
    if(conditions.appuserid == null || conditions.appuserid == undefined || conditions.appuserid == ""){
      const { appuserid, ...newConditions } =  conditions;
      conditions = newConditions;
    }
    const uri = paths || "bmstrnreport/selectTransProcToDoList"
    return axios.get(
      `${config.baseUrl}${uri}/${makeQueryString(conditions)}`
    );
  },
  updateTrnRepotBIA1: (conditions, paths) => {
    const uri = paths || "bmstrnreport/updateTrnRepotBIA1"
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  updateTrnRepotBIA2: (conditions, paths) => {
    const uri = paths || "bmstrnreport/updateTrnRepotBIA2"
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },

  selectTrnReqList: (conditions, paths) => {
    if(conditions.appuserid == null || conditions.appuserid == undefined || conditions.appuserid == ""){
      const { appuserid, ...newConditions } =  conditions;
      conditions = newConditions;
    }
    const uri = paths || "bmstrnreport/selectTrnReqList"
    return axios.get(
      `${config.baseUrl}${uri}/${makeQueryString(conditions)}`
    );
  },
  bmsTrnCompleteList: (conditions, paths) => {
    if(conditions.appuserid == null || conditions.appuserid == undefined || conditions.appuserid == ""){
      const { appuserid, ...newConditions } =  conditions;
      conditions = newConditions;
    }
    const uri = paths || "bmstrnreport/bmsTrnCompleteList"
    return axios.get(
      `${config.baseUrl}${uri}/${makeQueryString(conditions)}`
    );
  },
  bmsTrnCurrentList: (conditions, paths) => {
    if(conditions.appuserid == null || conditions.appuserid == undefined || conditions.appuserid == ""){
      const { appuserid, ...newConditions } =  conditions;
      conditions = newConditions;
    }
    const uri = paths || "bmstrnreport/bmsTrnCurrentList"
    return axios.get(
      `${config.baseUrl}${uri}/${makeQueryString(conditions)}`
    );
  },
  insertTrnStep: (conditions, paths) => {
    const uri = paths || "bmstrnstep/insertTrnStep"
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  insertApprreq: (conditions, paths) => {
    const uri = paths || "bmsdctapprreq/insertApprreq"
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  selectTrnOverList: (conditions, paths) => {
    if(conditions.appuserid == null || conditions.appuserid == undefined || conditions.appuserid == ""){
      const { appuserid, ...newConditions } =  conditions;
      conditions = newConditions;
    }
    const uri = paths || "bmstrnreport/selectTrnOverList"
    return axios.get(
      `${config.baseUrl}${uri}/${makeQueryString(conditions)}`
    );
  },
  selectTrnObjectList: (conditions, paths) => {
    if(conditions.appuserid == null || conditions.appuserid == undefined || conditions.appuserid == ""){
      const { appuserid, ...newConditions } =  conditions;
      conditions = newConditions;
    }
    const uri = paths || "bmstrnreport/selectTrnObjectList"
    return axios.get(
      `${config.baseUrl}${uri}/${makeQueryString(conditions)}`
    );
  },

};

export { trnAPIDFN, trnAPI };
