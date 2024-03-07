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
      <h4 class="h4">조직관리</h4>
    </div>

    <div class="box-content">
      <section class="section sticky">
        <div class="box-title">
          <h5 class="h5">부서 상세</h5>
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
              <th>부서 코드</th>
              <td>
                <v-text-field :readonly="true" v-model="getOrganizationDetailData.orgid" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>부서명</th>
              <td>
                <v-text-field :readonly="true" v-model="getOrganizationDetailData.orgname" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>부서 순서</th>
              <td>
                <v-text-field :readonly="true" v-model="getOrganizationDetailData.orgorder" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>상위부서 코드</th>
              <td>
                <v-text-field :readonly="true" v-model="getOrganizationDetailData.parentorgid" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>부서장</th>
              <td>
                <v-text-field :readonly="true" v-model="getOrganizationDetailData.username" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>대표 전화번호</th>
              <td>
                <v-text-field :readonly="true" v-model="getOrganizationDetailData.telephone" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
            <tr>
              <th>대표 FAX</th>
              <td>
                <v-text-field :readonly="true" v-model="getOrganizationDetailData.fax" variant="outlined" hide-details="auto"></v-text-field>
              </td>
            </tr>
          </tbody>
        </v-table>

        <div class="box-sort">
          <div class="action-buttons">
            <v-btn outlined @click="moveToBmsComorganizationupdate(getOrganizationDetailData.orgid)">수정</v-btn>
            <v-btn outlined @click="moveToBmsComorganizationlist()">목록</v-btn>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)

import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { API } from '@/api'; 

const name = ref('BmsComOrganizationDetail')
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const query = ref();
// for getOrganizationDetail
const getOrganizationDetailData = ref({})
const getOrganizationDetailDefCondi = {
  orgid: ""
}
const getOrganizationDetailCondi = ref({ ...getOrganizationDetailDefCondi })

onBeforeMount(() => {
  query.value = route.query; // to do    // query
})

onMounted(async () => {
  await getOrganizationDetail();
})

watchEffect(() => {
})

// eslint-disable-next-line
const getOrganizationDetail = async () => {

  try {
    getOrganizationDetailCondi.value.orgid = query.value.orgid;
    const response = await API.comAPI.getOrganizationDetail(getOrganizationDetailCondi.value, urlPaths.value);
    getOrganizationDetailData.value = response.data;

  } catch (error) {
    console.log(error);
  }
}


// Move Function
const moveToBmsComorganizationupdate = (orgid) => {
  router.push({
    name: "BmsComorganizationupdate",
    query: {
      ...{orgid: orgid}
    }
  });
}

// Move Function
const moveToBmsComorganizationlist = () => {
  router.push({
    name: "BmsComorganizationlist"
  });
}

</script>