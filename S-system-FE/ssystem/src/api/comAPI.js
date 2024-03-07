/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

import axios from "axios";
import { env } from "@/api/ApiEnv.js";
import { makeQueryString } from "@/utils/Query.js";
const API_BASE_URL = import.meta.env.VITE_APP_API_URL;
// const API_BASE_URL = import.meta.env.production.VITE_APP_API_URL;

const config = {
  baseUrl: API_BASE_URL + "/bms/com/",
};

const comAPIDFN = {
  comAPI: (APIName, conditions, paths) => {
    return comAPI[APIName](conditions, paths);
  },
};

const comAPI = {
  selectNoticeList: (conditions, paths) => {
    const uri = paths || "bmscomnotice/selectNoticeList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  deleteNoticeBIA: (conditions, paths) => {
    const uri = paths || "bmscomnotice/deleteNoticeBIA";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  getNoticeDetail: (conditions, paths) => {
    const uri = paths || "bmscomnotice/getNoticeDetail";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  insertNoticeBIA: (conditions, paths) => {
    const uri = paths || "bmscomnotice/insertNoticeBIA";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  updateNoticeBIA: (conditions, paths) => {
    const uri = paths || "bmscomnotice/updateNoticeBIA";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  selectFaqList: (conditions, paths) => {
    const uri = paths || "bmscomfaq/selectFaqList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  deleteFaqBIA: (conditions, paths) => {
    const uri = paths || "bmscomfaq/deleteFaqBIA";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  getFaqDetail: (conditions, paths) => {
    const uri = paths || "bmscomfaq/getFaqDetail";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  insertFaqBIA: (conditions, paths) => {
    const uri = paths || "bmscomfaq/insertFaqBIA";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  updateFaqBIA: (conditions, paths) => {
    const uri = paths || "bmscomfaq/updateFaqBIA";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  selectQnaList: (conditions, paths) => {
    const uri = paths || "bmscomqna/selectQnaList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  deleteQna: (conditions, paths) => {
    const uri = paths || "bmscomqna/deleteQna";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  getQnaDetail: (conditions, paths) => {
    const uri = paths || "bmscomqna/getQnaDetail";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  insertQna: (conditions, paths) => {
    const uri = paths || "bmscomqna/insertQna";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  updateQna: (conditions, paths) => {
    const uri = paths || "bmscomqna/updateQna";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  selectCodeCategoryList: (conditions, paths) => {
    const uri = paths || "bmstcode/selectCodeCategoryList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  deleteCodeCategory: (conditions, paths) => {
    const uri = paths || "bmstcode/deleteCodeCategory";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  insertCodeCategory: (conditions, paths) => {
    const uri = paths || "bmstcode/insertCodeCategory";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  getCodeCategoryDetail: (conditions, paths) => {
    const uri = paths || "bmstcode/getCodeCategoryDetail";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  updateCodeCategory: (conditions, paths) => {
    const uri = paths || "bmstcode/updateCodeCategory";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  selectReceiveMarkCodeList: (conditions, paths) => {
    const uri = paths || "bmscode/selectReceiveMarkCodeList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectOfflineCabinetInfo: (conditions, paths) => {
    const uri = paths || "bmscode/selectOfflineCabinetInfo";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectCodeList: (conditions, paths) => {
    const uri = paths || "bmscode/selectCodeList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  deleteCode: (conditions, paths) => {
    const uri = paths || "bmscode/deleteCode";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  insertCode: (conditions, paths) => {
    const uri = paths || "bmscode/insertCode";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  getCodeDetail: (conditions, paths) => {
    const uri = paths || "bmscode/getCodeDetail";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  updateCode: (conditions, paths) => {
    const uri = paths || "bmscode/updateCode";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  selectFindManager: (conditions, paths) => {
    const uri = paths || "comuserinfo/selectFindManager";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectUserList: (conditions, paths) => {
    const uri = paths || "comuserinfo/selectUserList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  deleteUserBIA: (conditions, paths) => {
    const uri = paths || "comuserinfo/deleteUserBIA";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  getUserDetail: (conditions, paths) => {
    const uri = paths || "comuserinfo/getUserDetail";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  checkUserId: (conditions, paths) => {
    const uri = paths || "comuserinfo/checkUserId";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  insertUserBIA: (conditions, paths) => {
    const uri = paths || "comuserinfo/insertUserBIA";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  updateUserBIA: (conditions, paths) => {
    const uri = paths || "comuserinfo/updateUserBIA";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  selectUserVgroup: (conditions, paths) => {
    const uri = paths || "comvgroup/selectUserVgroup";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectOrganizationList: (conditions, paths) => {
    const uri = paths || "comorganizationinfo/selectOrganizationList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  deleteOrganizationBIA: (conditions, paths) => {
    const uri = paths || "comorganizationinfo/deleteOrganizationBIA";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  getOrganizationDetail: (conditions, paths) => {
    const uri = paths || "comorganizationinfo/getOrganizationDetail";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  insertOrganizationBIA: (conditions, paths) => {
    const uri = paths || "comorganizationinfo/insertOrganizationBIA";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  updateOrganizationBIA: (conditions, paths) => {
    const uri = paths || "comorganizationinfo/updateOrganizationBIA";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  changeToporgVgroup: (conditions, paths) => {
    const uri = paths || "comorganizationinfo/changeToporgVgroup";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  selectSignList: (conditions, paths) => {
    const uri = paths || "comorganizationinfo/selectSignList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getSignDetail: (conditions, paths) => {
    if (conditions.seq == null || conditions.seq == undefined || conditions.seq == "") {
      const { seq, ...newConditions } = conditions;
      conditions = newConditions;
    }
    const uri = paths || "comorganizationinfo/getSignDetail";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectLogoList: (conditions, paths) => {
    const uri = paths || "comorganizationinfo/selectLogoList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  getLogoDetail: (conditions, paths) => {
    if (conditions.seq == null || conditions.seq == undefined || conditions.seq == "") {
      const { seq, ...newConditions } = conditions;
      conditions = newConditions;
    }
    const uri = paths || "comorganizationinfo/getLogoDetail";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectGwanList: (conditions, paths) => {
    const uri = paths || "comorggwaninfo/selectGwanList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  deleteGwanBIA: (conditions, paths) => {
    const uri = paths || "comorggwaninfo/deleteGwanBIA";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  getGwanDetail: (conditions, paths) => {
    if (conditions.seq == null || conditions.seq == undefined || conditions.seq == "") {
      const { seq, ...newConditions } = conditions;
      conditions = newConditions;
    }
    const uri = paths || "comorggwaninfo/getGwanDetail";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  insertGwanBIA: (comImageFormData, paths) => {
    const uri = paths || "comorggwaninfo/insertGwanBIA";
    return axios.post(`${config.baseUrl}${uri}/`, comImageFormData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  updateGwanBIA: (comImageFormData, paths) => {
    const uri = paths || "comorggwaninfo/updateGwanBIA";
    return axios.put(`${config.baseUrl}${uri}/`, comImageFormData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  deleteSignBIA: (conditions, paths) => {
    const uri = paths || "comorgdetailinfo/deleteSignBIA";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  insertSignBIA: (comImageFormData, paths) => {
    const uri = paths || "comorgdetailinfo/insertSignBIA";
    return axios.post(`${config.baseUrl}${uri}/`, comImageFormData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  updateSignBIA: (comImageFormData, paths) => {
    const uri = paths || "comorgdetailinfo/updateSignBIA";
    return axios.put(`${config.baseUrl}${uri}/`, comImageFormData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  deleteLogoBIA: (conditions, paths) => {
    const uri = paths || "comorgdetailinfo/deleteLogoBIA";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  insertLogoBIA: (comImageFormData, paths) => {
    const uri = paths || "comorgdetailinfo/insertLogoBIA";
    return axios.post(`${config.baseUrl}${uri}/`, comImageFormData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  updateLogoBIA: (comImageFormData, paths) => {
    const uri = paths || "comorgdetailinfo/updateLogoBIA";
    return axios.put(`${config.baseUrl}${uri}/`, comImageFormData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  selectGradeList: (conditions, paths) => {
    const uri = paths || "comgrade/selectGradeList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  deleteGrade: (conditions, paths) => {
    const uri = paths || "comgrade/deleteGrade";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  getGradeDetail: (conditions, paths) => {
    if (conditions.seq == null || conditions.seq == undefined || conditions.seq == "") {
      const { seq, ...newConditions } = conditions;
      conditions = newConditions;
    }
    const uri = paths || "comgrade/getGradeDetail";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  insertGrade: (conditions, paths) => {
    const uri = paths || "comgrade/insertGrade";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  updateGrade: (conditions, paths) => {
    const uri = paths || "comgrade/updateGrade";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  selectJobgubunList: (conditions, paths) => {
    const uri = paths || "comjobgubun/selectJobgubunList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  deleteJobgubun: (conditions, paths) => {
    const uri = paths || "comjobgubun/deleteJobgubun";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  getJobgubunDetail: (conditions, paths) => {
    if (conditions.seq == null || conditions.seq == undefined || conditions.seq == "") {
      const { seq, ...newConditions } = conditions;
      conditions = newConditions;
    }
    const uri = paths || "comjobgubun/getJobgubunDetail";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  insertJobgubun: (conditions, paths) => {
    const uri = paths || "comjobgubun/insertJobgubun";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  updateJobgubun: (conditions, paths) => {
    const uri = paths || "comjobgubun/updateJobgubun";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  selectLeftMenu: (conditions, paths) => {
    const uri = paths || "commenu/selectLeftMenu";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectMenuList: (conditions, paths) => {
    const uri = paths || "commenu/selectMenuList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  selectSubMenu: (conditions, paths) => {
    const uri = paths || "commenu/selectSubMenu";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  deleteMenuBIA: (conditions, paths) => {
    const uri = paths || "commenu/deleteMenuBIA";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  getMenuDetailBIA: (conditions, paths) => {
    const uri = paths || "commenu/getMenuDetailBIA";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  insertMenuBIA: (conditions, paths) => {
    const uri = paths || "commenu/insertMenuBIA";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  updateMenuBIA: (conditions, paths) => {
    const uri = paths || "commenu/updateMenuBIA";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  updateMenuOrder: (conditions, paths) => {
    const uri = paths || "commenu/updateMenuOrder";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  selectRealtimeLog: (conditions, paths) => {
    const uri = paths || "comrealtimelog/selectRealtimeLog";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  insertRealtimeLog: (conditions, paths) => {
    const uri = paths || "comrealtimelog/insertRealtimeLog";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  updateRealtimeLog: (conditions, paths) => {
    const uri = paths || "comrealtimelog/updateRealtimeLog";
    return axios.put(`${config.baseUrl}${uri}/`, conditions);
  },
  selectSystemLoginLog: (conditions, paths) => {
    const uri = paths || "comloginlog/selectSystemLoginLog";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  insertSystemLoginLog: (conditions, paths) => {
    const uri = paths || "comloginlog/insertSystemLoginLog";
    return axios.post(`${config.baseUrl}${uri}/`, conditions);
  },
  deleteSystemLoginLog: (conditions, paths) => {
    const uri = paths || "comloginlog/deleteSystemLoginLog";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  selectFormList: (conditions, paths) => {
    const uri = paths || "bmsdctform/selectFormList";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  deleteForm: (conditions, paths) => {
    const uri = paths || "bmsdctform/deleteForm";
    return axios.delete(`${config.baseUrl}${uri}/`, { data: conditions });
  },
  getFormDetail: (conditions, paths) => {
    const uri = paths || "bmsdctform/getFormDetail";
    return axios.get(`${config.baseUrl}${uri}/${makeQueryString(conditions)}`);
  },
  insertFormBIA: (bmsDctFormFormData, paths) => {
    const uri = paths || "bmsdctform/insertFormBIA";
    return axios.post(`${config.baseUrl}${uri}/`, bmsDctFormFormData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  updateFormBIA: (bmsDctFormFormData, paths) => {
    const uri = paths || "bmsdctform/updateFormBIA";
    return axios.put(`${config.baseUrl}${uri}/`, bmsDctFormFormData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
};

export { comAPIDFN, comAPI };
