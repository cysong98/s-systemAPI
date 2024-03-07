/*
  SHIELDGate Login Api FE
  /ssystem/src/components/login/ShieldgateLogin.js
  import { checkLoginSession, tokenExpiredCheck } from "@/components/login/ShieldgateLogin.js";
*/

import console from "console";
import dayjs from "dayjs";

import { ref } from 'vue';
import { API } from '@/api';

// Login url settings
const BASE_URL = "https://login.s-system.kinac";
const LOGIN_PATH = "/SCCloudOAuthService/common/oauth2/v2.0/authorize";
const APP_SERVICE_NAME = "s-system";
const urlPaths =  ref('');

/* 로그인 세션 체크 */
const checkLoginSession = async(code) => {
  // 현재 로컬 스토리지에 저장된 토큰이 있으면 로그인 처리한다.
  const jToken = sessionStorage.getItem('Authorization');
  const rToken = sessionStorage.getItem('Refresh-Authorization');
  const aToken = sessionStorage.getItem('Access-Token');

  if (jToken !== null && rToken !== null && aToken !== null) {
    await tokenExpiredCheck(rToken);
  }
  else {
    if (code === undefined || code === "") {
      console.log("code error");
      goLoginPage();
    }
    const tokenCondi = {
      code: code,
    }

    const InsertSystemLoginLogData = ref({
      opid: "",
      userid: "",
      logtime: dayjs(new Date()).format('YYYYMMDDHHmmss'),
      gubun: "로그인",
      ip: "",
      kdnid: ""
    });

    await API.loginAPI.authorizationCode(tokenCondi, urlPaths.value)
    .then(async (res) => {
      if (
        res.data.access_token &&
        res.data.refresh_token &&
        res.data.jwt
      ) {
        sessionStorage.setItem("Authorization", res.data.jwt);
        sessionStorage.setItem("Access-Token", res.data.access_token);
        sessionStorage.setItem("Refresh-Authorization", res.data.refresh_token);

        const decodeJwt = parseJwt(res.data.jwt);
        InsertSystemLoginLogData.value.userid = decodeJwt.user_email;
        InsertSystemLoginLogData.value.ip = decodeJwt.ipAddress;
        await API.comAPI.insertSystemLoginLog(InsertSystemLoginLogData.value, urlPaths.value);
      }
    })
    .catch((err) => {
      // 토큰 갱신이 실패한 경우 인증 실패로 처리한다.
      goLoginPage();
    });
  }
}


/* 토큰 만료일 확인 */
const tokenExpiredCheck = async(rToken) => {
  const jwt = sessionStorage.getItem("Authorization");
  let exp = -1, decodeObj = "";
  if(jwt !== null) {
    decodeObj = parseJwt(jwt);
    exp = decodeObj.exp * 1000;
  }
  if (exp < Date.now()) {
    // jwt의 만료일이 경과되었을 경우 refresh토큰 갱신을 시도한다.
    const tokenCondi = {
      refreshToken: rToken,
    }
    await API.loginAPI.refreshToken(tokenCondi, urlPaths.value)
    .then(async (res) => {
      if(res.data === "" || res.data === undefined) {   // refresh token이 만료된 경우
        const InsertSystemLoginLogData = ref({
          opid: "",
          userid: decodeObj.user_email,
          logtime: dayjs(new Date()).format('YYYYMMDDHHmmss'),
          gubun: "세션만료",
          ip: decodeObj.ipAddress,
          kdnid: ""
        });
        const updateRealtimeLogData = ref({
          userid: decodeObj.user_email,
          token: tokenCondi.refreshToken,
          flag: "N"
        });

        await API.comAPI.insertSystemLoginLog(InsertSystemLoginLogData.value, urlPaths.value);
        await API.comAPI.updateRealtimeLog(updateRealtimeLogData.value, urlPaths.value);
        alert("세션이 만료되었습니다.");
        goLoginPage();
      }

      if (
        res.data.access_token &&
        res.data.refresh_token &&
        res.data.jwt
      ) {
        sessionStorage.setItem("Authorization", res.data.jwt);
        sessionStorage.setItem("Access-Token", res.data.access_token);
        sessionStorage.setItem("Refresh-Authorization", res.data.refresh_token);
      }
    })
    .catch((err) => {
      // 토큰 갱신이 실패한 경우 인증 실패로 처리한다.
      goLoginPage();
    });
  }
}

const checkAndInsertRealtimeLog = async(userid, rToken) => {
  const InsertRealtimeLogData = ref({
    rtid: "RT" + dayjs(new Date()).format('YYYYMMDDHHmmSSS'),
    rtseq: "",
    indt: dayjs(new Date()).format('YYYYMMDDHHmmss'),
    userid: userid,
    username: "",
    deptid: "",
    deptname: "",
    flag: "",
    token: rToken,
  });
  const response = await API.comAPI.insertRealtimeLog(InsertRealtimeLogData.value, urlPaths.value);

  if(response.data.flag === "N") {
    const InsertSystemLoginLogData = ref({
      opid: "",
      userid: userid,
      logtime: dayjs(new Date()).format('YYYYMMDDHHmmss'),
      gubun: "세션만료",
      ip: "",
      kdnid: ""
    });
    const decodeJwt = parseJwt(sessionStorage.getItem('Authorization'));
    InsertSystemLoginLogData.value.ip = decodeJwt.ipAddress;
    await API.comAPI.insertSystemLoginLog(InsertSystemLoginLogData.value, urlPaths.value);

    alert("세션이 만료되어 로그인 페이지로 이동합니다.");
    goLoginPage();
  }
};

/* jwt decoding */
const parseJwt = (token) => {
  let base64Url = token.split('.')[1];
  let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  let jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
  }).join(''));

  return JSON.parse(jsonPayload);
};


/* 로그인 페이지로 Redirect */
const goLoginPage = () => {
  clearStorage();
  const url = BASE_URL + LOGIN_PATH + "?client_name=" + APP_SERVICE_NAME + "&response_type=code";
  window.location.replace(url);
}

const clearStorage = () => {
  sessionStorage.removeItem("Authorization");
  sessionStorage.removeItem("Access-Token");
  sessionStorage.removeItem("Refresh-Authorization");
  sessionStorage.removeItem("topmenu");
  sessionStorage.removeItem("breadcrumbs.topMenu");
  sessionStorage.removeItem("breadcrumbs.activeLink");
};

export {
  checkLoginSession,
  tokenExpiredCheck,
  parseJwt,
  goLoginPage,
  clearStorage,
  checkAndInsertRealtimeLog
};