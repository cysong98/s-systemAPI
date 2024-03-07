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
  baseUrl: API_BASE_URL + "/bms/login/"
};

const loginAPIDFN = {
  loginAPI: (APIName, conditions, paths) => {
    return loginAPI[APIName](conditions, paths);
  }
};

const loginAPI = {
  getUserLogin: (conditions, paths) => {
    const uri = paths || "comuserinfo/getUserLogin"
    return axios.get(
      `${config.baseUrl}${uri}/${makeQueryString(conditions)}`
    );
  },
  // getPublicKeys: (conditions, paths) => {
  //   const uri = paths || "comuserinfo/getPublicKeys";
  //   return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  // },
  clientCredential: (paths) => {
    const uri = paths || "comuserinfo/clientCredential";
    return axios.post(`${config.baseUrl}${uri}/`);
  },
  authorizationCode: (conditions, paths) => {
    const uri = paths || "comuserinfo/authorizationCode";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  refreshToken: (conditions, paths) => {
    const uri = paths || "comuserinfo/refreshToken";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  logout: (conditions, paths) => {
    const uri = paths || "comuserinfo/logout";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  updateAbsence: (conditions, paths) => {
    const uri = paths || "comuserinfo/updateAbsence";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
};

export { loginAPIDFN, loginAPI };
