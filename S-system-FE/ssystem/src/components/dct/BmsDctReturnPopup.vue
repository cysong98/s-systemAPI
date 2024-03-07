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
                <th>비밀ID</th>
                <td>{{ getReturnDetailCardData.docid }}</td>
                <th>담당자 이름</th>
                <td>{{ getReturnDetailCardData.authorname }}</td>
                <th>담당자직위직급</th>
                <td>{{ getReturnDetailCardData.authordutyname }}</td>
              </tr>
              <tr>
                <th>문서 제목(비밀명)</th>
                <td colspan="5">{{ getReturnDetailCardData.docttl }}</td>
              </tr>
            </tbody>
          </v-table>
        </div>
      </div>

      <div class="row">
        <div class="col">
          <v-table class="table-type-02 narrow">
            <thead>
              <tr>
                <th rowspan="2"><br></th>
                <th rowspan="2">구분</th>
                <th rowspan="2">키워드</th>
                <th rowspan="2">비밀일련번호</th>
                <th rowspan="2">기록물형태</th>
                <th colspan="3">등록</th>
                <th colspan="3">수정</th>
                <th rowspan="2">DRM구분</th>
              </tr>
              <tr>
                <th>등록일</th>
                <th>보고일자</th>
                <th>종료일</th>
                <th>수정일</th>
                <th>수정자 ID</th>
                <th>비밀출처</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td></td>
                <td>{{ transformRdocGubun(getReturnDetailCardData.gubun) }}</td>
                <td>{{ getReturnDetailCardData.keyword }}</td>
                <td>{{ getReturnDetailCardData.docnoseq }}</td>
                <td>{{ getReturnDetailCardData.paperrectypes }}</td>
                <td>{{ transformDate(getReturnDetailCardData.indt) }}</td>
                <td>{{ transformDate(getReturnDetailCardData.reportdt) }}</td>
                <td>{{ transformDate(getReturnDetailCardData.enddt) }}</td>
                <td>{{ transformDate(getReturnDetailCardData.updt) }}</td>
                <td>{{ getReturnDetailCardData.upid }}</td>
                <td>{{ getReturnDetailCardData.docfrom }}</td>
                <td>{{ getReturnDetailCardData.drmgubun === 'n' ? 'X': 'O' }}</td>
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
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()

import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { API } from '@/api'; 
import { popups, togglePopup, bringPopupToFront, getPopupSize, centerPopup } from "@/utils/Popup.js"; 
import { transformStatus, transformState, transformAsktype, transformDate, transformNotiprsrvterm, transformRdocGubun, transformSeclevel } from "@/utils/TransFormLabelDataUtil.js" //레이블링을 위한 임포트.for labeling..

const name = ref('BmsDctReturnPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function
})
const args = ref({})

const route = useRoute()
const router = useRouter()
// for popup
const childReturnValue = ref({})
const urlPaths =  ref('')
// for getReturnDetailCard
const getReturnDetailCardData = ref({})
const getReturnDetailCardDefCondi = {
   enfdocid: "",
   docid: ""
  }
const getReturnDetailCardCondi = ref({ ...getReturnDetailCardDefCondi })

onBeforeMount(() => {
})

onMounted(async () => {
  args.value = props.args;
  getReturnDetailCardCondi.value.docid = args.value.docid;
  await getReturnDetailCard(getReturnDetailCardCondi.value);
})

watchEffect(() => {
  args.value = props.args
})

// eslint-disable-next-line
const getReturnDetailCard = async () => {
  try {
    const response = await API.dctAPI.getReturnDetailCard(getReturnDetailCardCondi.value, urlPaths.value);
    getReturnDetailCardData.value = response.data;
    getReturnDetailCardData.value = response.data

  } catch (error) {
    console.log(error);
    alert("Server Error")
  }
}

const closePopUp = () => {
  getReturnDetailCardCondi.value = { ...getReturnDetailCardDefCondi };
  props.toggleFunc();
}

</script>

<style lang="scss">
  .tg  {border-collapse:collapse;border-spacing:0;}
.tg td{border-color:black;border-style:solid;border-width:1px;font-family:Arial, sans-serif;font-size:14px;
  overflow:hidden;padding:10px 5px;word-break:normal;}
.tg th{border-color:black;border-style:solid;border-width:1px;font-family:Arial, sans-serif;font-size:14px;
  font-weight:normal;overflow:hidden;padding:10px 5px;word-break:normal;}
.tg .tg-lboi{border-color:inherit;text-align:left;vertical-align:middle}
.tg .tg-9wq8{border-color:inherit;text-align:center;vertical-align:middle}
.tg .tg-m6kf{background-color:#DDEBF7;border-color:inherit;text-align:left;vertical-align:middle}

.tg  {border-collapse:collapse;border-spacing:0;}
.tg td{border-color:black;border-style:solid;border-width:1px;font-family:Arial, sans-serif;font-size:14px;
  overflow:hidden;padding:10px 5px;word-break:normal;}
.tg th{border-color:black;border-style:solid;border-width:1px;font-family:Arial, sans-serif;font-size:14px;
  font-weight:normal;overflow:hidden;padding:10px 5px;word-break:normal;}
.tg .tg-cly1{text-align:left;vertical-align:middle}
.tg .tg-m0vc{background-color:#DDEBF7;text-align:center;vertical-align:middle}
.tg .tg-ceoq{background-color:#DDEBF7;text-align:left;vertical-align:middle}
.tg .tg-idbl{background-color:#DDEBF7;border-color:inherit;text-align:center;vertical-align:middle}
</style>
