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
      <div class="content"  >
        <div class="row">
          <div class="col">
            <v-table class="table-type-03">
                <colgroup>
                    <col width="240">
                    <col width="">
                </colgroup>
                <tbody>
                    <tr>
                        <th class="required">부서 코드</th>
                        <td>
                        <div class="box-flex">
                            <v-text-field
                                :readonly="true"
                                v-model="updateToporgData.orgid"
                                maxlength="35"
                                variant="outlined"
                                hide-details="auto"
                            ></v-text-field>
                            <v-btn @click="toggleToporg()" class="magnify-solid" >
                                <v-icon></v-icon>검색
                            </v-btn>
                        </div>
                        </td>
                    </tr>
                    <tr>
                        <th class="required">부서명</th>
                        <td>
                        <v-text-field
                            :readonly="true"
                            v-model="updateToporgData.orgname"
                            maxlength="256"
                            variant="outlined"
                            hide-details="auto"
                        ></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <th>부서 순서</th>
                        <td>
                        <v-text-field
                            v-model="updateToporgData.orgorder"
                            :rules="[rules.checkNumber]"
                            maxlength="5"
                            variant="outlined"
                            hide-details="auto"
                        ></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <th>문서과 지정</th>
                        <td>
                        <div class="box-flex">
                            <v-text-field
                                :readonly="true"
                                v-model="displayDocDept"
                                variant="outlined"
                                hide-details="auto"
                                clearable
                            ></v-text-field>
                            <v-btn @click="toggleDocDept()" class="magnify-solid" >
                                <v-icon></v-icon>검색
                            </v-btn>
                        </div>
                        </td>
                    </tr>
                    <tr>
                        <th>존안지원부서 지정</th>
                        <td>
                        <div class="box-flex">
                            <v-text-field
                                :readonly="true"
                                v-model="displayPreseDept"
                                variant="outlined"
                                hide-details="auto"
                                clearable
                            ></v-text-field>
                            <v-btn @click="togglePreseDept()" class="magnify-solid" >
                                <v-icon></v-icon>검색
                            </v-btn>
                        </div>
                        </td>
                    </tr>
                    <tr>
                        <th>이관관리부서 지정</th>
                        <td>
                        <div class="box-flex">
                            <v-text-field
                                :readonly="true"
                                v-model="displayTransDept"
                                variant="outlined"
                                hide-details="auto"
                                clearable
                            ></v-text-field>
                            <v-btn @click="toggleTransDept()" class="magnify-solid" >
                                <v-icon></v-icon>검색
                            </v-btn>
                        </div>
                        </td>
                    </tr>
                </tbody>
            </v-table>
            <div class="buttons-bottom" style="padding-bottom: 0px">
                <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">취소</v-btn>
                <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="updateOrganizationBIA">저장</v-btn>
            </div>
          </div>
        </div>
      </div>
    </div>

    <v-dialog v-model="visibleOrgPopup" style="width: 900px;">
        <v-card>
            <v-toolbar>
                <v-toolbar-title>부서 검색</v-toolbar-title>
                <v-btn class="close" icon @click="visibleOrgPopup = false">
                    <v-icon></v-icon>
                </v-btn>
            </v-toolbar>
            <BmsComOrgSelect
                v-if="visibleOrgPopup"
                :args="'singular'"
                :toggleFunc="toggleToporg"
                :returnFunc="selectToporgList"
            ></BmsComOrgSelect>
        </v-card>
    </v-dialog>
    <v-dialog v-model="visibleDocDeptPopup" style="width: 900px;">
        <v-card>
            <v-toolbar>
                <v-toolbar-title>부서 검색</v-toolbar-title>
                <v-btn class="close" icon @click="visibleDocDeptPopup = false">
                    <v-icon></v-icon>
                </v-btn>
            </v-toolbar>
            <BmsComOrgSelect
                v-if="visibleDocDeptPopup"
                :args="'singular'"
                :toggleFunc="toggleDocDept"
                :returnFunc="selectDocDept"
            ></BmsComOrgSelect>
        </v-card>
    </v-dialog>
    <v-dialog v-model="visiblePreseDeptPopup" style="width: 900px;">
        <v-card>
            <v-toolbar>
                <v-toolbar-title>부서 검색</v-toolbar-title>
                <v-btn class="close" icon @click="visiblePreseDeptPopup = false">
                    <v-icon></v-icon>
                </v-btn>
            </v-toolbar>
            <BmsComOrgSelect
                v-if="visiblePreseDeptPopup"
                :args="'singular'"
                :toggleFunc="togglePreseDept"
                :returnFunc="selectPreseDept"
            ></BmsComOrgSelect>
        </v-card>
    </v-dialog>
    <v-dialog v-model="visibleTransDeptPopup" style="width: 900px;">
        <v-card>
            <v-toolbar>
                <v-toolbar-title>부서 검색</v-toolbar-title>
                <v-btn class="close" icon @click="visibleTransDeptPopup = false">
                    <v-icon></v-icon>
                </v-btn>
            </v-toolbar>
            <BmsComOrgSelect
                v-if="visibleTransDeptPopup"
                :args="'singular'"
                :toggleFunc="toggleTransDept"
                :returnFunc="selectTransDept"
            ></BmsComOrgSelect>
        </v-card>
    </v-dialog>
</template>
    
<script setup>
import console from "console";

import dayjs from 'dayjs';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import _ from 'lodash';
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import BmsComOrgSelect from '@/components/com/BmsComOrgSelect.vue';

const name = ref('BmsComOrgSelect')
const props = defineProps({
    toggleFunc: Function,
})
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')

const preDocDeptid = ref("");
const prePreseDeptid = ref("");
const preTransDeptid = ref("");
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for updateToporgData
const updateToporgData = ref({
    orgid: "",
    orgname: "",
    orgorder: "",
    docdeptid: "",
    presedeptid: "",
    transdeptid: ""
})

onMounted(async () => {
  await getToporg();
  preDocDeptid.value = updateToporgData.value.docdeptid;
  prePreseDeptid.value = updateToporgData.value.presedeptid;
  preTransDeptid.value = updateToporgData.value.transdeptid;
})

const rules = {
  checkNumber: value => /[0-9]/g.test(value) || '숫자만 입력하실 수 있습니다.',
}


const getToporg = async () => {
  const toporgCondi = {
    orgid: "",
    pageSize: 1000,
    pageNum: 1,
  }
  try {
    const response = await API.comAPI.selectOrganizationList(toporgCondi, urlPaths.value);
    let toporgid = "";
    for(let i=0; i<response.data.total; i++) {
        if(response.data.list[i].toporgid !== undefined && response.data.list[i].toporgid !== null && response.data.list[i].toporgid !== "") {
            toporgid = response.data.list[i].toporgid;
        }
    }

    let res = await API.comAPI.getOrganizationDetail({orgid: toporgid, orgname: ""}, urlPaths.value);
    updateToporgData.value = res.data;

    for(let i=0; i<response.data.total; i++) {
        if(updateToporgData.value.docdeptid === response.data.list[i].orgid)
            displayDocDept.value = response.data.list[i].orgname;
        if(updateToporgData.value.presedeptid === response.data.list[i].orgid)
            displayPreseDept.value = response.data.list[i].orgname;
        if(updateToporgData.value.transdeptid === response.data.list[i].orgid)
            displayTransDept.value = response.data.list[i].orgname;
    }
  } catch (error) {
    console.log(error);
  }
}

const updateOrganizationBIA = async () => {
  updateToporgData.value.upid = getUserLoginData.value.userid;
  updateToporgData.value.updt = dayjs(new Date()).format('YYYYMMDDHHmmss');
  API.comAPI
    .updateOrganizationBIA(updateToporgData.value, urlPaths.value)
    .then(async(response) => {
      if (response.status == 200) {
        // alert("정상 갱신되었습니다");

        const changeToporgVgroupData = ref({
            predeptid: "",
            deptid: "",
            vgroupid: ""
        });
        if(preDocDeptid.value !== updateToporgData.value.docdeptid) {
            changeToporgVgroupData.value.predeptid = preDocDeptid.value;
            changeToporgVgroupData.value.deptid = updateToporgData.value.docdeptid;
            changeToporgVgroupData.value.vgroupid = "5210";
            await API.comAPI.changeToporgVgroup(changeToporgVgroupData.value, urlPaths.value)
        }
        if(prePreseDeptid.value !== updateToporgData.value.presedeptid) {
            changeToporgVgroupData.value.predeptid = prePreseDeptid.value;
            changeToporgVgroupData.value.deptid = updateToporgData.value.presedeptid;
            changeToporgVgroupData.value.vgroupid = "8015";
            await API.comAPI.changeToporgVgroup(changeToporgVgroupData.value, urlPaths.value)
        }
        if(preTransDeptid.value !== updateToporgData.value.transdeptid) {
            changeToporgVgroupData.value.predeptid = preTransDeptid.value;
            changeToporgVgroupData.value.deptid = updateToporgData.value.transdeptid;
            changeToporgVgroupData.value.vgroupid = "8255";
            await API.comAPI.changeToporgVgroup(changeToporgVgroupData.value, urlPaths.value)
        }

        closePopUp();
      }
    })
    .catch(error => {
      console.log(error);
    });
}


/* ========================== submenu ========================== */
const visibleOrgPopup = ref(false);
const visibleDocDeptPopup = ref(false);
const visiblePreseDeptPopup = ref(false);
const visibleTransDeptPopup = ref(false);
const displayDocDept = ref("");
const displayPreseDept = ref("");
const displayTransDept = ref("");

const toggleToporg = ref(() => {
  visibleOrgPopup.value = !visibleOrgPopup.value;
});
const toggleDocDept = ref(() => {
  visibleDocDeptPopup.value = !visibleDocDeptPopup.value;
});
const togglePreseDept = ref(() => {
  visiblePreseDeptPopup.value = !visiblePreseDeptPopup.value;
});
const toggleTransDept = ref(() => {
  visibleTransDeptPopup.value = !visibleTransDeptPopup.value;
});

// return Function
const selectToporgList = ref((retValue) => {
  toggleToporg.value();
  updateToporgData.value.orgid = retValue.orgid;
  updateToporgData.value.orgname = retValue.orgname;
});
const selectDocDept = ref((retValue) => {
  toggleDocDept.value();
  updateToporgData.value.docdeptid = retValue.orgid;
  displayDocDept.value = retValue.orgname;
});
const selectPreseDept = ref((retValue) => {
  togglePreseDept.value();
  updateToporgData.value.presedeptid = retValue.orgid;
  displayPreseDept.value = retValue.orgname;
});
const selectTransDept = ref((retValue) => {
  toggleTransDept.value();
  updateToporgData.value.transdeptid = retValue.orgid;
  displayTransDept.value = retValue.orgname;
});
/* ============================================================= */

// Popup
const closePopUp = () => {
  props.toggleFunc();
};

</script>