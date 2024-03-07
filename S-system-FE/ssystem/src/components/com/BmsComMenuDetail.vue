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
          <h5 class="h5">메뉴 상세</h5>
          <div class="action-buttons">
            <v-btn outlined @click="moveToBmsCommenuupdate(getMenuDetailBIAData.id)">수정</v-btn>
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
              <th>메뉴아이디</th>
              <td>
                <v-text-field
                  :readonly="true"
                  v-model="getMenuDetailBIAData.id"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th>메뉴명</th>
              <td>
                <v-text-field
                  :readonly="true"
                  v-model="getMenuDetailBIAData.name"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th>메뉴URL</th>
              <td>
                <v-text-field
                  :readonly="true"
                  v-model="getMenuDetailBIAData.url"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th>메뉴설명</th>
              <td>
                <v-text-field
                  :readonly="true"
                  v-model="getMenuDetailBIAData.description"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr v-if="getMenuDetailBIAData.type === 'a'">
              <th>메뉴순서</th>
              <td>
                <v-text-field
                  :readonly="true"
                  v-model="getMenuDetailBIAData.menuorder"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr v-else>
              <th>하위메뉴순서</th>
              <td>
                <v-text-field
                  :readonly="true"
                  v-model="getMenuDetailBIAData.submenuorder"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th>상위메뉴아이디</th>
              <td>
                <v-text-field
                  :readonly="true"
                  v-model="parentMenuName"
                  variant="outlined"
                  hide-details="auto"
                ></v-text-field>
              </td>
            </tr>
          </tbody>
        </v-table>
      </section>
    </div>
  </div>
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)

import { ref, onBeforeMount, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

const name = ref('BmsComMenuDetail')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const query = ref({});
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
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


const parentMenuName = ref("");
const getMenuDetailBIA = async () => {
  try {
    getMenuDetailBIACondi.value.id = query.value.id;
    const response = await API.comAPI.getMenuDetailBIA(getMenuDetailBIACondi.value, urlPaths.value);
    getMenuDetailBIAData.value = response.data;

    if(response.data.type === "b") {
      getMenuDetailBIACondi.value.id = response.data.parentMenuid;
      let res = await API.comAPI.getMenuDetailBIA(getMenuDetailBIACondi.value, urlPaths.value);
      parentMenuName.value = res.data.id + "(" + res.data.name + ")" 

      res = await API.comAPI.selectSubMenu({submenuid: response.data.id, sortDirection: "ASC"}, urlPaths.value);
      getMenuDetailBIAData.value.submenuorder = res.data[0].submenuorder;
    }
  } catch (error) {
    console.log(error);
  }
}


// Move Function
const moveToBmsCommenuupdate = (item) => {
  router.push({
    name: "BmsCommenuupdate",
    query: {
      ...{id: item}
    },
  });
}

// Move Function
const moveToBmsCommenulist = () => {
  router.push({
    name: "BmsCommenulist"
  });
}

</script>