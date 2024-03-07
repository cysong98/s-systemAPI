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
  <div class="modal-body">
    <div class="content">
      <div class="row">
        <div class="col">
          <v-table class="table-type-03">
            <colgroup>
              <col width="120">
              <col width="">
              <col width="120">
              <col width="">
              <col width="120">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th>비밀등급</th>
                <td>{{ transformSeclevel(docData.seclevel) }}</td>
                <th>관리번호</th>
                <td>{{ docData.mgmtno }}</td>
                <th>사본번호</th>
                <td>{{ docData.copyno }}</td>
              </tr>
              <tr>
                <th>문서 제목</th>
                <td colspan="5">{{ docData.docttl }}</td>
              </tr>
            </tbody>
          </v-table>
        </div>
      </div>

      <div class="row">
        <div class="col">
          <v-table class="table-type-04" height="380" fixed-header>
            <thead>
              <tr>
                <th rowspan="2">구분</th>
                <th rowspan="2">목적</th>
                <th rowspan="2">인가여부</th>
                <th rowspan="2">요청일시</th>
                <th rowspan="2">부여일시</th>
                <th rowspan="2">만료일시</th>
                <th rowspan="2">파쇄일시<br>(반입)</th>
                <th rowspan="2">회수일시</th>
                <th colspan="3">요청자</th>
                <th colspan="3">결재자</th>
                <th colspan="3">비밀담당자</th>
              </tr>
              <tr>
                <th>소속</th>
                <th>성명</th>
                <th>서명</th>
                <th>소속</th>
                <th>성명</th>
                <th>서명</th>
                <th>소속</th>
                <th>성명</th>
                <th>서명</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(auth, idx) in getAuthCardInfoList" :key="idx">
                <td>{{ auth.authgubun }}</td>
                <td>{{ auth.authgoal }}</td>
                <td>{{ auth.authgrantflag }}</td>
                <td>{{ auth.authreqdt }}</td>
                <td>{{ auth.authgrantdt }}</td>
                <td>{{ auth.authexpiredt }}</td>
                <td>{{ auth.prtdestructdt }}</td>
                <td>{{ auth.revokedt }}</td>
                <td class="dept">{{ auth.reqdeptname }}</td>
                <td style="width: 70px;">{{ auth.reqname }}</td>
                <td class="signature" style="width: 100px;">{{ auth.reqname }}</td>
                <td class="dept">{{ auth.apprdeptname }}</td>
                <td style="width: 70px;">{{ auth.apprname }}</td>
                <td class="signature" style="width: 100px;">{{ auth.apprname }}</td>
                <td class="dept">{{ auth.authordeptname }}</td>
                <td style="width: 70px;">{{ auth.authorname }}</td>
                <td class="signature" style="width: 100px;">{{ auth.authorname }}</td>
              </tr>
            </tbody>
          </v-table>

        </div>
      </div>
    </div>

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
    </div>
  </div>
</template>

<script setup>
import console from "console";
import dayjs from 'dayjs';
import { nextTick, ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from "@/api";
import _ from 'lodash';
import { transformSeclevel, transAuthGubun } from "@/utils/TransFormLabelDataUtil.js"

const name = ref('DctAuthManageCardPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function
})
const urlPaths =  ref('')
const args = ref({})
const docid = ref({docid:""})
const docData = ref({})
const getAuthCardInfoList = ref([])

onBeforeMount(() => {
  args.value = props.args
  docid.value.docid = args.value.docid;
})

onMounted(async () => {
  await getDctRdoc();
  await getDctAttr();
  await getDctAuthCard();
})

const getDctRdoc = async () => {
  try {
    const response = await API.dctAPI.getDctRdoc({...docid.value }, urlPaths.value);
    const data = response.data;
    docData.value.docttl = data.docttl;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const getDctAttr = async () => {
  try {
    const response = await API.dctAPI.getDctAttr({...docid.value }, urlPaths.value);
    const data = response.data;
    docData.value.seclevel = data.seclevel;
    docData.value.mgmtno = data.mgmtno;
    docData.value.copyno = data.copyno;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const getDctAuthCard = async () => {
  try {
    const response = await API.dctAPI.getDctAuthCard({...docid.value }, urlPaths.value);
    response.data = response.data.map((u, idx) => {
      u = { ...u, authgubun: transAuthGubun(u.authgubun) };
      return { ...u};
    });
    getAuthCardInfoList.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const closePopUp = () => {
  props.toggleFunc();
};

</script>

<style lang="scss" scoped>

.dept {
  max-width: 80px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.dept:hover {
  overflow: visible;
  white-space: normal;
}

.dept:hover::before {
  content: attr(title);
  position: absolute;
  padding: 5px;
  z-index: 1;
  white-space: normal;
}
</style>