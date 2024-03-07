/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

import { defineStore } from 'pinia';
import { computed, ref } from 'vue'
import { API } from "/src/api"
import { useRoute, useRouter } from 'vue-router';
import { goLoginPage } from "@/components/login/ShieldgateLogin.js";

export const useLoginStore = defineStore('login', () => {
  const urlPaths = ref("");
  const getUserLoginData = ref({});
  const getUserLoginCondi = ref({
    userid: "",
    jwt: ""
  });

  const route = useRoute();
  const router = useRouter();
  
  const getUserLogin = async () => {
    /* 개발용 */
    if (getUserLoginCondi.value.userid !== null && getUserLoginCondi.value.userid !== "") {
      try {
        const response = await API.loginAPI.getUserLogin(getUserLoginCondi.value, urlPaths.value);
        getUserLoginData.value = response.data;
        if(getUserLoginData.value.userid !== null) {
          router.push({
            name: "Home",
          });
        }
        else
          alert("아이디를 확인해주세요.");
      } catch (error) {
        console.log(error);
      }
    } else {
      alert("아이디를 입력해주세요.");
    }

    /* IdGP Login */
    // try {
    //   const response = await API.loginAPI.getUserLogin(getUserLoginCondi.value, urlPaths.value);
    //   getUserLoginData.value = response.data;
    //   if(response.data.userid === undefined || response.data.userid === null || response.data.userid === '') {
    //     goLoginPage();
    //   }
    // } catch (error) {
    //   console.log(error);
    // }
  };

  const alertCnt = ref(1);

  return {
    getUserLoginData,
    getUserLoginCondi,
    getUserLogin,
    alertCnt,
  };
});
