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
        <v-tabs align-tabs="end" class="tabs" style="margin-bottom: 0px">
          <v-tab :value="1" @click="props.returnFunc()">최상위부서 등록</v-tab>
        </v-tabs>
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
                      v-model="insertOrganizationBIAData.orgid"
                      maxlength="35"
                      variant="outlined"
                      hide-details="auto"
                    ></v-text-field>
                    <v-btn @click="checkOrgId()" class="magnify-solid">
                      중복확인
                    </v-btn>
                  </div>
                </td>
              </tr>
              <tr>
                <th class="required">부서명</th>
                <td>
                  <div class="box-flex">
                    <v-text-field
                      v-model="insertOrganizationBIAData.orgname"
                      maxlength="256"
                      variant="outlined"
                      hide-details="auto"
                    ></v-text-field>
                    <v-btn @click="checkOrgName()" class="magnify-solid">
                      중복확인
                    </v-btn>
                  </div>
                </td>
              </tr>
              <tr>
                <th>부서 순서</th>
                <td>
                  <v-text-field
                    v-model="insertOrganizationBIAData.orgorder"
                    :rules="[rules.checkNumber]"
                    maxlength="5"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>상위부서 코드</th>
                <td>
                  <div class="box-flex">
                    <v-text-field
                      :readonly="true"
                      v-model="insertOrganizationBIAData.parentorgid"
                      variant="outlined"
                      hide-details="auto"
                      clearable
                    ></v-text-field>
                    <v-btn @click="toggleParentOrg()" class="magnify-solid" >
                      <v-icon></v-icon>검색
                    </v-btn>
                  </div>
                </td>
              </tr>
              <tr>
                <th>부서장</th>
                <td>
                  <div class="box-flex">
                    <v-text-field
                      :readonly="true"
                      v-model="chiefname"
                      variant="outlined"
                      hide-details="auto"
                      clearable
                    ></v-text-field>
                    <v-btn @click="toggleChiefId()" class="magnify-solid" >
                      <v-icon></v-icon>검색
                    </v-btn>
                  </div>
                </td>
              </tr>
              <tr>
                <th>대표 전화번호</th>
                <td>
                  <v-text-field
                    v-model="insertOrganizationBIAData.telephone"
                    :rules="[rules.checkCallNumber]"
                    maxlength="20"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>대표 FAX</th>
                <td>
                  <v-text-field
                    v-model="insertOrganizationBIAData.fax"
                    :rules="[rules.checkCallNumber]"
                    maxlength="20"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
            </tbody>
          </v-table>
          <div class="buttons-bottom" style="padding-bottom: 0px">
            <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">취소</v-btn>
            <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="insertOrganizationBIA">저장</v-btn>
          </div>
        </div>
      </div>
    </div>
  </div>


  <v-dialog v-model="visibleParentOrg" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>부서 검색</v-toolbar-title>
        <v-btn class="close" icon @click="visibleParentOrg = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComOrgSelect
        v-if="visibleParentOrg"
        :args="orgArgs"
        :toggleFunc="toggleParentOrg"
        :returnFunc="selectParentOrgList"
      ></BmsComOrgSelect>
    </v-card>
  </v-dialog>

  <v-dialog v-model="visibleChiefId" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>부서장 검색</v-toolbar-title>
        <v-btn class="close" icon @click="visibleChiefId = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComUserSelect
        v-if="visibleChiefId"
        :toggleFunc="toggleChiefId"
        :returnFunc="selectChiefId"
      ></BmsComUserSelect>
    </v-card>
  </v-dialog>
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)

import dayjs from 'dayjs';
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { checkRV } from "@/utils/CheckRV.js";
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import BmsComOrgSelect from '@/components/com/BmsComOrgSelect.vue';
import BmsComUserSelect from '@/components/com/BmsComUserSelect.vue';

const name = ref('BmsComOrganizationInsert')
const props = defineProps({
  toggleFunc: Function,
  returnFunc: Function,
})
const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore
// for getOrganizationDetail
const getOrganizationDetailDefCondi = {
  orgid: ""
}
const getOrganizationDetailCondi = ref({ ...getOrganizationDetailDefCondi })
const getOrganizationDetailCondiOrgname = ref({orgname: ""});
// for insertOrganizationBIA
const insertOrganizationBIAData = ref({
    orgid: "",
    orgname: "",
    parentorgid: "",
    orgorder: "",
    toporgid: "",
    whencreated: dayjs(new Date()).format('YYYYMMDDHHmmss'),
    whendeleted: "99991231235959",
    chiefid: "",
    inid: getUserLoginData.value.userid,
    indt: dayjs(new Date()).format('YYYYMMDDHHmmss'),
    orgdepth: "",
    telephone: "",
    fax: ""
})

const rules = {
  checkNumber: value => /[0-9]/g.test(value) || '숫자만 입력하실 수 있습니다.',
  checkCallNumber: value => /^\d{2,3}-\d{3,4}-\d{4}$/.test(value) || '다음 형식을 지켜주세요. \"000-0000-0000\"',
}


const checkId = ref("");
const checkName = ref("");
const checkOrgId = async () => {
  if(insertOrganizationBIAData.value.orgid.length === 0) {
    alert("부서 코드를 입력해주세요.");
    return;
  }
  try {
    getOrganizationDetailCondi.value.orgid = insertOrganizationBIAData.value.orgid;
    const response = await API.comAPI.getOrganizationDetail(getOrganizationDetailCondi.value, urlPaths.value);
    if(response.data.orgid === undefined) {
      checkId.value = getOrganizationDetailCondi.value.orgid;
      alert("사용 가능한 부서 코드입니다.");
    }
    else
      alert("이미 사용 중인 부서 코드입니다.");
  } catch (error) {
    console.log(error);
  }
}
const checkOrgName = async () => {
  if(insertOrganizationBIAData.value.orgname.length === 0) {
    alert("부서명을 입력해주세요.");
    return;
  }
  try {
    getOrganizationDetailCondiOrgname.value.orgname = insertOrganizationBIAData.value.orgname;
    const response = await API.comAPI.getOrganizationDetail(getOrganizationDetailCondiOrgname.value, urlPaths.value);
    if(response.data.orgname === undefined) {
      checkName.value = getOrganizationDetailCondiOrgname.value.orgname;
      alert("사용 가능한 부서명입니다.");
    }
    else
      alert("이미 사용 중인 부서명입니다.");
  } catch (error) {
    console.log(error);
  }
}


const insertOrganizationBIA = async () => {
  if (checkRVComOrganizationinfo() == false) {
    return;
  }

  if(checkId.value !== insertOrganizationBIAData.value.orgid) {
    getOrganizationDetailCondi.value.orgid = insertOrganizationBIAData.value.orgid;
    const response = await API.comAPI.getOrganizationDetail(getOrganizationDetailCondi.value, urlPaths.value);
    if(response.data.orgid !== undefined) {
      checkId.value = getOrganizationDetailCondi.value.orgid;
      alert("이미 사용 중인 부서 코드입니다.");
      return;
    }
  }
  if(checkName.value !== insertOrganizationBIAData.value.orgname) {
    getOrganizationDetailCondiOrgname.value.orgname = insertOrganizationBIAData.value.orgname;
    const response = await API.comAPI.getOrganizationDetail(getOrganizationDetailCondiOrgname.value, urlPaths.value);
    if(response.data.orgname !== undefined) {
      alert("이미 사용 중인 부서명입니다.");
      return;
    }
  }

  if(chiefname.value === "" || chiefname.value === null)
    insertOrganizationBIAData.value.chiefid = null;
  else {
    const updateUserDeptCondi = {
      userid: insertOrganizationBIAData.value.chiefid,
      deptid: insertOrganizationBIAData.value.orgid,
      deptname: insertOrganizationBIAData.value.orgname
    }
    await API.comAPI.updateUserBIA(updateUserDeptCondi, urlPaths.value)
                    .catch(error => {
                      console.log(error);
                    });
  }

  API.comAPI
    .insertOrganizationBIA(insertOrganizationBIAData.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        // alert("정상 등록되었습니다");
        closePopUp();
      }
    })
    .catch(error => {
      console.log(error);
    });
}


/* ========================== subPopup ========================== */
const visibleParentOrg = ref(false);
const visibleChiefId = ref(false);
const chiefname = ref("");
const orgArgs = ref("");

const toggleParentOrg = ref(() => {
  visibleChiefId.value = false;
  orgArgs.value = "singular";
  visibleParentOrg.value = !visibleParentOrg.value;
});
const toggleChiefId = ref(() => {
  visibleParentOrg.value = false;
  visibleChiefId.value = !visibleChiefId.value;
});

// return Function
const selectParentOrgList = ref((retValue) => {
  toggleParentOrg.value();
  insertOrganizationBIAData.value.parentorgid = retValue.orgid;
});

const selectChiefId = ref((retValue) => {
  toggleChiefId.value();
  insertOrganizationBIAData.value.chiefid = retValue.userid;
  chiefname.value = retValue.username;
});
/* ============================================================= */


// Popup
const closePopUp = () => {
  props.toggleFunc();
}

// Check Validation
const checkRVComOrganizationinfo = () => {
  if (insertOrganizationBIAData.value.orgid.length == 0) {
    alert("부서 코드를 입력해주세요.");
    return false;
  }
  if (insertOrganizationBIAData.value.orgid.length > 35) {
    alert("부서 코드의 최대 길이는 35자입니다.");
    return false;
  }
  if (insertOrganizationBIAData.value.orgname.length == 0) {
    alert("부서명을 입력해주세요.");
    return false;
  }
  if (insertOrganizationBIAData.value.orgname.length > 256) {
    alert("부서명의 최대 길이는 256자입니다.");
    return false;
  }
  if (
    !checkRV.checkRV(
      insertOrganizationBIAData.value.orgorder,
      false,
      "checkReal",
      "부서 순서는 숫자만 입력 가능합니다."
    )
  )
    return false;
  if(insertOrganizationBIAData.value.orgorder.length > 5) {
    alert("부서 순서의 최대길이는 5자입니다.");
    return false;
  }
  if (insertOrganizationBIAData.value.fax.length > 20) {
    alert("대표 FAX의 최대 길이는 20자입니다.");
    return false;
  }
  if (insertOrganizationBIAData.value.telephone.length > 20) {
    alert("대표 전화번호의 최대 길이는 20자입니다.");
    return false;
  }

  return true;
}

</script>