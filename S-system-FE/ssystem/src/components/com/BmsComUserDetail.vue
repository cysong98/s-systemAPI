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
    <div class="content" style="padding-top: 10px; padding-bottom: 10px;">
      <div class="row">
        <div class="col">
          <v-table class="table-type-03">
            <colgroup>
              <col width="240">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th>아이디</th>
                <td>
                  <v-text-field :readonly="true" v-model="getUserDetailData.userid" variant="outlined" hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>패스워드</th>
                <td>
                  <v-text-field
                    :readonly="true"
                    v-model="getUserDetailData.password"
                    :append-icon="showPW ? 'mdi-eye' : 'mdi-eye-off'"
                    @click:append="showPW = !showPW"
                    :type="showPW ? 'text' : 'password'"
                    variant="outlined"
                    hide-details="auto">
                  </v-text-field>
                </td>
              </tr>
              <tr>
                <th>이름</th>
                <td>
                  <v-text-field :readonly="true" v-model="getUserDetailData.username" variant="outlined" hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>부서</th>
                <td>
                  <v-text-field :readonly="true" v-model="getUserDetailData.deptname" variant="outlined" hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>직위</th>
                <td>
                  <v-text-field :readonly="true" v-model="getUserDetailData.position" variant="outlined" hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>직급</th>
                <td>
                  <v-text-field :readonly="true" v-model="getUserDetailData.grade" variant="outlined" hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>권한</th>
                <td>
                  <v-text-field :readonly="true" v-model="vgrouplist" variant="outlined" hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>이메일</th>
                <td>
                  <v-text-field :readonly="true" v-model="getUserDetailData.email" maxlength="200" variant="outlined" hide-details="auto" ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>전화번호</th>
                <td>
                  <v-text-field :readonly="true" v-model="getUserDetailData.mobilephone" maxlength="50" variant="outlined" hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>비밀취급인가등급</th>
                <td>
                  <v-select
                    :readonly="true"
                    v-model="secretgrade"
                    variant="outlined"
                    hide-details="auto"
                  />
                </td>
              </tr>
            </tbody>
          </v-table>

          <div class="buttons-bottom" style="padding-bottom: 0px">
            <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import console from "console";

import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from '@/api'; 

const name = ref('BmsComUserDetail')
const props = defineProps({
  args: Object,
  toggleFunc: Function
})
const showPW = ref(false);

const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// for getUserDetail
const getUserDetailData = ref({})
const getUserDetailDefCondi = {
   userid: ""
}
const getUserDetailCondi = ref({ ...getUserDetailDefCondi })


onMounted(async () => {
  await getUserDetail();
})


const vgrouplist = ref("");
const secretgrade = ref("");

const getUserDetail = async () => {

  try {
    getUserDetailCondi.value.userid = props.args.userid;
    const response = await API.comAPI.getUserDetail(getUserDetailCondi.value, urlPaths.value);
    getUserDetailData.value = response.data[0];

    response.data.map((item) => {
      vgrouplist.value = vgrouplist.value + item.vgroupname + ", "
    });
    vgrouplist.value = vgrouplist.value.substring(0, vgrouplist.value.length-2);

    if(vgrouplist.value == "undefined")
      vgrouplist.value = "";

    var sg = getUserDetailData.value.secretgrade;
    if(sg == "0")
      secretgrade.value = "비인가";
    else if(sg == "1")
      secretgrade.value = "1급";
    else if(sg == "2")
      secretgrade.value = "2급";
    else if(sg == "3")
      secretgrade.value = "3급";
    else
      secretgrade.value = "";

  } catch (error) {
    console.log(error);
  }
}


// Popup
const closePopUp = () => {
  props.toggleFunc();
};
</script>
