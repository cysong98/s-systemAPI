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
          <h5 class="h5">메뉴 수정</h5>
          <div class="action-buttons">
            <v-btn outlined @click="updateMenuBIA()">저장</v-btn>
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
                  :readonly="true"
                  v-model="updateMenuBIAData.id"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th class="required">메뉴명</th>
              <td>
                <v-text-field
                  v-model="updateMenuBIAData.name"
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
                  v-model="updateMenuBIAData.url"
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
                  v-model="updateMenuBIAData.description"
                  maxlength="512"
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
import { ref, onBeforeMount, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import BmsComMenuSelect from '@/components/com/BmsComMenuSelect.vue';


const name = ref('BmsComMenuUpdate')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const query = ref({menuid: ""});
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for updateMenuBIA
const updateMenuBIAData = ref({})
// for getMenuDetailBIA
const getMenuDetailBIAData = ref({})
const getMenuDetailBIADefCondi = {
  id: ""
}
const getMenuDetailBIACondi = ref({ ...getMenuDetailBIADefCondi })

onBeforeMount(() => {
  query.value = route.query;
})

onMounted(async () => {
  await getMenuDetailBIA();
})


const getMenuDetailBIA = async () => {
  try {
    getMenuDetailBIACondi.value.id = query.value.id;
    const response = await API.comAPI.getMenuDetailBIA(getMenuDetailBIACondi.value, urlPaths.value);
    updateMenuBIAData.value = response.data;
    
    if(response.data.type === "b") {
      getMenuDetailBIACondi.value.id = response.data.parentMenuid;
      let res = await API.comAPI.getMenuDetailBIA(getMenuDetailBIACondi.value, urlPaths.value);
      parentMenuName.value = res.data.id + "(" + res.data.name + ")";
    }
  } catch (error) {
    console.log(error);
  }
}


const updateMenuBIA = async () => {
  if(checkRVComMenu() == false) {
    return;
  }
  
  if(updateMenuBIAData.value.parentMenuid === updateMenuBIAData.value.id) {
    alert("자기 자신을 상위메뉴로 선택할 수 없습니다.");
    updateMenuBIAData.value.parentMenuid = "";
    parentMenuName.value = "";
    return;
  }

  updateMenuBIAData.value.upid = loginStore.getUserLoginData.userid;
  updateMenuBIAData.value.updt = dayjs(new Date()).format('YYYYMMDDHHmmss');

  API.comAPI
    .updateMenuBIA(updateMenuBIAData.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        // alert("정상 갱신되었습니다");
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
const parentMenuName = ref("");
const selectParentMenuList = ref((retValue) => {
  toggleParentMenu.value();
  updateMenuBIAData.value.menuorder = null;
  updateMenuBIAData.value.parentMenuid = retValue.id;
  parentMenuName.value = retValue.name + "(" + retValue.id + ")";
});

watch(parentMenuName, async (parentMenuName) => {
  if(parentMenuName === "" || parentMenuName === null) {
    updateMenuBIAData.value.parentMenuid = "";
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
  if (updateMenuBIAData.value.id.length == 0) {
    alert("메뉴아이디를 입력해주세요.");
    return false;
  }
  if (updateMenuBIAData.value.id.length > 35) {
    alert("메뉴아이디의 최대 길이는 35자입니다.");
    return false;
  }
  if (updateMenuBIAData.value.name.length == 0) {
    alert("메뉴명을 입력해주세요.");
    return false;
  }
  if (updateMenuBIAData.value.name.length > 256) {
    alert("메뉴명의 최대 길이는 256자입니다.");
    return false;
  }
  if (updateMenuBIAData.value.description !== undefined && updateMenuBIAData.value.description.length > 512) {
    alert("메뉴설명의 최대 길이는 512자입니다.");
    return false;
  }
  if (updateMenuBIAData.value.url.length > 256) {
    alert("메뉴URL의 최대 길이는 256자입니다.");
    return false;
  }
  if (
    updateMenuBIAData.value.menuorder !== undefined && 
    !checkRV.checkRV(
      updateMenuBIAData.value.menuorder,
      false,
      "checkReal",
      "메뉴순서는 숫자만 입력 가능합니다."
    )
  ) {
    updateMenuBIAData.value.menuorder = null;
    return false;
  }
  if(updateMenuBIAData.value.menuorder !== undefined && updateMenuBIAData.value.menuorder.length > 5) {
    alert("메뉴순서의 최대길이는 5자입니다.");
    return false;
  }

  return true;
}

</script>