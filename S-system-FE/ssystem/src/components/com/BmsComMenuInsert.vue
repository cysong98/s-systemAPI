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
  <div class="page" :style="{width: contentWidth}">
    <div class="box-title">
      <h4 class="h4">시스템관리</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">메뉴 등록</h5>
          <div class="action-buttons">
            <v-btn outlined @click="insertMenuBIA()">저장</v-btn>
            <v-btn outlined @click="moveToBmsCommenulist()">목록</v-btn>
          </div>
        </div>
      </section>

      <section class="section">
        <v-table class="table-type-03">
          <colgroup>
            <col width="240">
            <col width="">
          </colgroup>
          <tbody>
            <tr>
              <th class="required">메뉴아이디</th>
              <td>
                <v-text-field
                  v-model="insertMenuBIAData.id"
                  maxlength="35"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th class="required">메뉴명</th>
              <td>
                <v-text-field
                  v-model="insertMenuBIAData.name"
                  maxlength="256"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th class="required">메뉴URL</th>
              <td>
                <v-text-field
                  v-model="insertMenuBIAData.url"
                  maxlength="256"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th>메뉴설명</th>
              <td>
                <v-text-field
                  v-model="insertMenuBIAData.description"
                  maxlength="512"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th>메뉴순서</th>
              <td>
                <v-text-field
                  :readonly="existParent"
                  v-model="insertMenuBIAData.menuorder"
                  maxlength="5"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th>상위메뉴아이디</th>
              <td>
                <div class="box-flex">
                  <v-text-field
                    :readonly="true"
                    v-model="parentMenuName"
                    variant="outlined"
                    hide-details="auto"
                    clearable
                  ></v-text-field>
                  <v-btn @click="toggleParentMenu()" class="magnify-solid" >
                    <v-icon></v-icon>검색
                  </v-btn>
                </div>
              </td>
            </tr>
          </tbody>
        </v-table>
      </section>
    </div>
  </div>
  
  <v-dialog v-model="visibleMenu" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>상위메뉴아이디 검색</v-toolbar-title>
        <v-btn class="close" icon @click="visibleMenu = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComMenuSelect
        v-if="visibleMenu"
        :toggleFunc="toggleParentMenu"
        :returnFunc="selectParentMenuList"
      ></BmsComMenuSelect>
    </v-card>
  </v-dialog>
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)

import dayjs from 'dayjs';
import { ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { checkRV } from "@/utils/CheckRV.js";
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import BmsComMenuSelect from '@/components/com/BmsComMenuSelect.vue';

const name = ref('BmsComMenuInsert')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for insertMenuBIA
const insertMenuBIAData = ref({
    id: "",
    name: "",
    type: "a",
    description: "",
    url: "",
    isuse: "y",
    inid: loginStore.getUserLoginData.userid,
    indt: dayjs(new Date()).format('YYYYMMDDHHmmss'),
    parentMenuid: "",
    menuorder: ""
});

const checkDupIdData = ref([]);
const checkDupId = async (condi) => {
  try {
    const response = await API.comAPI.getMenuDetailBIA(condi, urlPaths.value);
    checkDupIdData.value = response.data;
  } catch (error) {
    console.log(error);
  }
}


const insertMenuBIA = async () => {
  await checkDupId({id: insertMenuBIAData.value.id});
  if (checkRVComMenu() == false)
    return;

  API.comAPI
    .insertMenuBIA(insertMenuBIAData.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        // alert("정상 등록되었습니다");
        moveToBmsCommenulist();
      }
    })
    .catch(error => {
      console.log(error);
    });
}


/* ========================== submenu ========================== */

const visibleMenu = ref(false);
const toggleParentMenu = ref(() => {
  visibleMenu.value = !visibleMenu.value;
});

// return Function
const existParent = ref(false);
const parentMenuName = ref("");

const selectParentMenuList = ref((retValue) => {
  toggleParentMenu.value();
  existParent.value = true;
  insertMenuBIAData.value.menuorder = null;
  insertMenuBIAData.value.parentMenuid = retValue.id;
  parentMenuName.value = retValue.name + "(" + retValue.id + ")";
});

watch(parentMenuName, async (parentMenuName) => {
  if(parentMenuName === "" || parentMenuName === null) {
    existParent.value = false;
    insertMenuBIAData.value.parentMenuid = "";
  }
})
/* ============================================================= */


// Move Function
const moveToBmsCommenulist = () => {
  router.push({
    name: "BmsCommenulist"
  });
}

// Check Validation
const checkRVComMenu = () => {
  if(checkDupIdData.value.id !== undefined) {
    checkDupIdData.value = [];
    alert("이미 사용중인 메뉴아이디입니다.");
    return false;
  }
  if (insertMenuBIAData.value.id.length == 0) {
    alert("메뉴아이디를 입력해주세요.");
    return false;
  }
  if (insertMenuBIAData.value.id.length > 35) {
    alert("메뉴아이디의 최대 길이는 35자입니다.");
    return false;
  }
  if (insertMenuBIAData.value.name.length == 0) {
    alert("메뉴명을 입력해주세요.");
    return false;
  }
  if (insertMenuBIAData.value.name.length > 256) {
    alert("메뉴명의 최대 길이는 256자입니다.");
    return false;
  }
  if (insertMenuBIAData.value.description.length > 512) {
    alert("메뉴설명의 최대 길이는 512자입니다.");
    return false;
  }
  if (insertMenuBIAData.value.url.length == 0) {
    alert("메뉴URL을 입력해주세요..");
    return false;
  }
  if (insertMenuBIAData.value.url.length > 256) {
    alert("메뉴URL의 최대 길이는 256자입니다.");
    return false;
  }
  if (
    !checkRV.checkRV(
      insertMenuBIAData.value.menuorder,
      false,
      "checkReal",
      "메뉴순서는 숫자만 입력 가능합니다."
    )
  ) {
    insertMenuBIAData.value.menuorder = null;
    return false;
  }
  if(insertMenuBIAData.value.menuorder.length > 5) {
    alert("메뉴순서의 최대길이는 5자입니다.");
    return false;
  }

  return true;
}

</script>
