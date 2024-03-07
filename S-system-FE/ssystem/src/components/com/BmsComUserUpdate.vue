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
      <div v-if="isloading === true" class="overlay">
        <div class="spinner"></div>
      </div>
      <div class="row">
        <div class="col">
          <v-table class="table-type-03">
            <colgroup>
              <col width="240">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th :class="isMyPage !== 'true' ? 'required' : ''">아이디</th>
                <td>
                  <v-text-field :readonly="true" v-model="updateUserBIAData.userid" variant="outlined" hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th class="required">패스워드</th>
                <td>
                  <v-text-field
                    v-model="updateUserBIAData.password"
                    :append-icon="showPW ? 'mdi-eye' : 'mdi-eye-off'"
                    @click:append="showPW = !showPW"
                    :type="showPW ? 'text' : 'password'"
                    maxlength="2000"
                    variant="outlined"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th class="required">이름</th>
                <td>
                  <v-text-field v-model="updateUserBIAData.username" maxlength="100" variant="outlined" hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th :class="isMyPage !== 'true' ? 'required' : ''">부서</th>
                <td>
                  <div class="box-flex">
                    <v-text-field :readonly="true" v-model="updateUserBIAData.deptname" variant="outlined" hide-details="auto" :clearable="isMyPage !== 'true'"></v-text-field>
                    <v-btn v-if="isMyPage !== 'true'" @click="toggleOrg()" class="magnify-solid" >
                      <v-icon></v-icon>검색
                    </v-btn>
                  </div>
                </td>
              </tr>
              <tr>
                <th :class="isMyPage !== 'true' ? 'required' : ''">직위</th>
                <td>
                  <div class="box-flex">
                    <v-text-field
                      :readonly="true"
                      v-model="updateUserBIAData.position"
                      variant="outlined"
                      hide-details="auto"
                    ></v-text-field>
                    <v-btn v-if="isMyPage !== 'true'" @click="togglePosition()" class="magnify-solid" >
                      <v-icon></v-icon>검색
                    </v-btn>
                  </div>
                </td>
              </tr>
              <tr>
                <th :class="isMyPage !== 'true' ? 'required' : ''">직급</th>
                <td>
                  <div class="box-flex">
                    <v-text-field
                      :readonly="true"
                      v-model="updateUserBIAData.grade"
                      variant="outlined"
                      hide-details="auto"
                    ></v-text-field>
                    <v-btn v-if="isMyPage !== 'true'" @click="toggleGrade()" class="magnify-solid" >
                      <v-icon></v-icon>검색
                    </v-btn>
                  </div>
                </td>
              </tr>
              <tr>
                <th>권한</th>
                <td>
                  <div class="box-flex">
                    <v-text-field :readonly="true" v-model="vgrouplist" variant="outlined" hide-details="auto" :clearable="isMyPage !== 'true'"></v-text-field>
                    <v-btn v-if="isMyPage !== 'true'" @click="toggleVgroup()" class="magnify-solid" >
                      <v-icon></v-icon>검색
                    </v-btn>
                  </div>
                </td>
              </tr>
              <tr>
                <th>이메일</th>
                <td>
                  <v-text-field
                    v-model="updateUserBIAData.email"
                    :rules="[rules.checkEmail]"
                    maxlength="200"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>전화번호</th>
                <td>
                  <v-text-field v-model="updateUserBIAData.mobilephone" maxlength="50" variant="outlined" hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th :class="isMyPage !== 'true' ? 'required' : ''">비밀취급인가등급</th>
                <td>
                  <div class="box-flex">
                    <v-select
                      v-model="updateUserBIAData.secretgrade"
                      :readonly="isMyPage === 'true'"
                      :items="comboData"
                      item-title="view"
                      item-value="key"
                      variant="outlined"
                      hide-details="auto"
                    />
                  </div>
                </td>
              </tr>
            </tbody>
          </v-table>
          <div class="buttons-bottom" style="padding-bottom: 0px">
            <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">취소</v-btn>
            <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="updateUserBIA">저장</v-btn>
          </div>
        </div>
      </div>
    </div>
  </div>


  <v-dialog v-model="visibleOrg" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>부서 검색</v-toolbar-title>
        <v-btn class="close" icon @click="visibleOrg = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComOrgSelect
        v-if="visibleOrg"
        :args="userArgs"
        :toggleFunc="toggleOrg"
        :returnFunc="selectOrgList"
      ></BmsComOrgSelect>
    </v-card>
  </v-dialog>

  <v-dialog v-model="visiblePosition" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>직위 검색</v-toolbar-title>
        <v-btn class="close" icon @click="visiblePosition = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComGradePopup
        v-if="visiblePosition"
        :args="gubun"
        :toggleFunc="togglePosition"
        :returnFunc="function (retValue) {
          updateUserBIAData.position = retValue.codename;
          togglePosition();
        }"
      ></BmsComGradePopup>
    </v-card>
  </v-dialog>

  <v-dialog v-model="visibleGrade" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>직급 검색</v-toolbar-title>
        <v-btn class="close" icon @click="visibleGrade = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComGradePopup
        v-if="visibleGrade"
        :args="gubun"
        :toggleFunc="toggleGrade"
        :returnFunc="function (retValue) {
          updateUserBIAData.grade = retValue.gradename;
          toggleGrade();
        }"
      ></BmsComGradePopup>
    </v-card>
  </v-dialog>

  <v-dialog v-model="visibleVgroup" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>권한 검색</v-toolbar-title>
        <v-btn class="close" icon @click="visibleVgroup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <BmsComVgroupList
        v-if="visibleVgroup"
        :args="vgroupArgs"
        :toggleFunc="toggleVgroup"
        :returnFunc="selectVgroupList"
      ></BmsComVgroupList>
    </v-card>
  </v-dialog>

  <v-dialog v-model="visibleInsertDupAlert" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>알림</v-toolbar-title>
      </v-toolbar>
      <div class="modal-body">
        <div class="content">
          <div class="row">
            <div class="col" style="text-align: center;">
              <div style="font-size: 20px;">
                <br/><br/>
                <p style="font-weight: bold; font-size: 22px;">아래의 권한을 가진 사용자가 이미 존재합니다.</p><br/><br/>
                <div v-for="v in dupVgrouplist">
                  <p>{{ v.deptname }}({{ v.userid }}) : {{ v.vgroupname }}({{ v.vgroupid }})</p>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="buttons-bottom">
          <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="confirm()">확인</v-btn>
        </div>
      </div>
    </v-card>
  </v-dialog>

  <v-dialog v-model="visibleInsertLimitAlert" style="width: 900px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>알림</v-toolbar-title>
      </v-toolbar>
      <div class="modal-body">
        <div class="content">
          <div v-if="vgroupLoading === true" class="overlay">
            <div class="spinner"></div>
          </div>
          <div class="row">
            <div class="col" style="text-align: center;">
              <div style="font-size: 20px;">
                <br/><br/>
                <p style="font-weight: bold; font-size: 22px;">아래의 권한은 해당 부서의 사용자만 가질 수 있습니다.</p><br/><br/>
                <div v-for="v in limitVgrouplist">
                  <p>{{ v.vgroupname }}({{ v.vgroupid }}) : {{ v.deptname }}({{ v.deptid }})</p>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="buttons-bottom">
          <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="confirm()">확인</v-btn>
        </div>
      </div>
    </v-card>
  </v-dialog>
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth } = storeToRefs(mainStore)

import dayjs from 'dayjs';
import { ref, onBeforeMount, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from '@/api'; 
import { storeToRefs } from "pinia";
import { useLoginStore } from '@/store/Login.js';

import BmsComOrgSelect from '@/components/com/BmsComOrgSelect.vue';
import BmsComVgroupList from '@/components/com/BmsComUserVgroupList.vue';
import BmsComGradePopup from '@/components/com/BmsComGradePopup.vue';

const name = ref('BmsComUserUpdate')
const props = defineProps({
  args: Object,
  isMyPage: String,
  toggleFunc: Function,
  returnFunc: Function
})
const isMyPage = ref('');
const showPW = ref(false);

const route = useRoute()
const router = useRouter()
const urlPaths =  ref('')
const isloading = ref(false);
const vgroupLoading = ref(false);
// Pinia
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = loginStore

// for column combo
const comboData = [
  { key: null, view: "선택"},
  { key: "0", view: "비인가" },
  { key: "1", view: "1급"},
  { key: "2", view: "2급"},
  { key: "3", view: "3급"},
]
// for updateUserBIA
const updateUserBIAData = ref({})
// for getUserDetail
const getUserDetailDefCondi = { userid: "" }
const getUserDetailCondi = ref({ ...getUserDetailDefCondi })
const rules = {
  checkEmail: value => /^[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[@]{1}[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[.]{1}[A-Za-z]{2,5}$/.test(value) || '이메일 형식을 지켜주세요. \"user123@example.co.kr\"',
}

onMounted(async () => {
  await getUserDetail();
  if(props.isMyPage !== undefined && props.isMyPage !== null)
    isMyPage.value = props.isMyPage;
  await getToporg();
})

const dupVgrouplist = ref([]);
const limitVgrouplist = ref([]);
const confirm = () => {
  dupVgrouplist.value = [];
  limitVgrouplist.value = [];
  visibleInsertDupAlert.value = false;
  visibleInsertLimitAlert.value = false;
  isloading.value = false;
};

const topOrgData = ref({
  orgid: "",
  orgname: "",
  docdeptid: "",
  presedeptid: "",
  transdeptid: ""
})
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
    topOrgData.value = res.data;
  } catch (error) {
    console.log(error);
  }
}


let password = null;
const getUserDetail = async () => {
  try {
    getUserDetailCondi.value.userid = props.args.userid;
    const response = await API.comAPI.getUserDetail(getUserDetailCondi.value, urlPaths.value);
    updateUserBIAData.value = response.data[0];
    password = updateUserBIAData.value.password;
    updateUserBIAData.value.pwChanged = false;

    updateUserBIAData.value.vgrouplist = [];
    response.data.map((item) => {
      vgrouplist.value = vgrouplist.value + item.vgroupname + ", ";
      updateUserBIAData.value.vgrouplist.push({
        vgroupid: item.vgroupid,
        vgroupname: item.vgroupname
      });
      if(item.vgroupid !== undefined) {
        vgroupArgs.value.push({
          vgroupid: item.vgroupid,
          vgroupname: item.vgroupname
        });
      }
    });
    vgrouplist.value = vgrouplist.value.substring(0, vgrouplist.value.length-2);
    if(vgrouplist.value == "undefined")
      vgrouplist.value = "";
  } catch (error) {
    console.log(error);
  }
}

const updateUserBIA = async () => {
  if (checkRVComUserinfo() == false) {
    return;
  }

  isloading.value = true;
  updateUserBIAData.value.upid = getUserLoginData.value.userid;
  updateUserBIAData.value.updt = dayjs(new Date()).format('YYYYMMDDHHmmss');
  if(vgrouplist.value === "" || vgrouplist.value === null)
    updateUserBIAData.value.vgrouplist = [];
  if(updateUserBIAData.value.deptname === "" || updateUserBIAData.value.deptname === null)
    updateUserBIAData.value.deptid = "";
  if(password !== updateUserBIAData.value.password)
    updateUserBIAData.value.pwChanged = true;

  // 최상위부서 권한 체크
  let vgroupCheck = true;
  const selectedVgrouplist = ref([]);
  if(updateUserBIAData.value.vgrouplist !== undefined){
    updateUserBIAData.value.vgrouplist.map((el) => {
      if(el.vgroupid === '8255' || el.vgroupid === '8050') {
        if(updateUserBIAData.value.deptid !== topOrgData.value.transdeptid) {
          if(topOrgData.value.chiefid)
          selectedVgrouplist.value.push({
            vgroupOrgCondi: {orgid: topOrgData.value.transdeptid},
            vgrouplist: el
          });
          vgroupCheck = false;
        }
      }
      if(el.vgroupid === '8015' || el.vgroupid === '8010') {
        if(updateUserBIAData.value.deptid !== topOrgData.value.presedeptid) {
          selectedVgrouplist.value.push({
            vgroupOrgCondi: {orgid: topOrgData.value.presedeptid},
            vgrouplist: el
          });
          vgroupCheck = false;
        }
      }
      if(el.vgroupid === '5210') {
        if(updateUserBIAData.value.deptid !== topOrgData.value.docdeptid) {
          selectedVgrouplist.value.push({
            vgroupOrgCondi: {orgid: topOrgData.value.docdeptid},
            vgrouplist: el
          });
          vgroupCheck = false;
        }
      }
    })
  }

  if(vgroupCheck === true) {    // 중복 권한 체크
    API.comAPI
      .updateUserBIA(updateUserBIAData.value, urlPaths.value)
      .then(response => {
        if(response.data[0] !== undefined) {
          dupVgrouplist.value = [...response.data];
          visibleInsertDupAlert.value = true;
        }
        else {
          if (response.status == 200) {
            // alert("정상 갱신되었습니다");
            isloading.value = false;
            props.returnFunc();
          }
        }
      })
      .catch(error => {
        console.log(error);
      });
  }
  else {      // 부서별 가능 권한 체크
    let templist = [];
    let alertCheck = true;
    vgroupLoading.value = true;
    await Promise.all (selectedVgrouplist.value.map(async(item) => {
      const response = await API.comAPI.getOrganizationDetail(item.vgroupOrgCondi, urlPaths.value);
      if(alertCheck && (response.data.chiefid !== updateUserBIAData.value.userid)) {
        alertCheck = false;
        alert("존안관리부서장, 이관관리부서장 권한은 부서장만 부여할 수 있습니다.");
      }
      
      templist.push({
        deptid: response.data.orgid,
        deptname: response.data.orgname,
        vgroupname: item.vgrouplist.vgroupname,
        vgroupid: item.vgrouplist.vgroupid
      })
    }));

    visibleInsertLimitAlert.value = true;
    limitVgrouplist.value = templist;
    isloading.value = false;
    vgroupLoading.value = false;
  }
}


/* ====================== for submenu ====================== */

const visibleOrg = ref(false);
const visiblePosition = ref(false);
const visibleGrade = ref(false);
const visibleVgroup = ref(false);
const visibleInsertDupAlert = ref(false);
const visibleInsertLimitAlert = ref(false);

const vgrouplist = ref("");
const userArgs = ref("");
const gubun = ref("");

const toggleOrg = ref(() => {
  visibleVgroup.value = false;
  userArgs.value = "singular";
  visibleOrg.value = !visibleOrg.value;
});
const togglePosition = ref(() => {
  gubun.value = "position";
  visiblePosition.value = !visiblePosition.value;
});
const toggleGrade = ref(() => {
  gubun.value = "grade";
  visibleGrade.value = !visibleGrade.value;
});
const toggleVgroup = ref(() => {
  visibleOrg.value = false;
  visibleVgroup.value = !visibleVgroup.value;
});

// return function
const vgroupArgs = ref([]);
const selectOrgList = ref((retValue) => {
  toggleOrg.value();
  updateUserBIAData.value.deptid = retValue.orgid;
  updateUserBIAData.value.deptname = retValue.orgname;
});
const selectVgroupList = ref((retValue) => {
  toggleVgroup.value();
  vgrouplist.value = "";
  updateUserBIAData.value.vgrouplist = [];
  retValue.map((item) => {
    updateUserBIAData.value.vgrouplist.push(item);
    vgrouplist.value = vgrouplist.value + item.vgroupname + ", "
  });
  vgrouplist.value = vgrouplist.value.substring(0, vgrouplist.value.length-2);
  vgroupArgs.value = retValue;
});
/* ========================================================= */


//Popup
const closePopUp = () => {
  props.toggleFunc();
};


// Check Validation
const checkRVComUserinfo = () => {
  if (updateUserBIAData.value.userid == "") {
    alert("아이디를 입력해주세요.");
    return false;
  }
  if (updateUserBIAData.value.password.length === 0) {
    alert("패스워드를 입력해주세요.");
    return false;
  }
  if (updateUserBIAData.value.password !== undefined && updateUserBIAData.value.password.length > 2000) {
    alert("패스워드의 최대 길이는 2000자입니다.");
    return false;
  }
  if (updateUserBIAData.value.username == "") {
    alert("이름을 입력해주세요.");
    return false;
  }
  if (updateUserBIAData.value.username.length > 100) {
    alert("이름의 최대 길이는 100자입니다.");
    return false;
  }
  if (updateUserBIAData.value.deptname == "" || updateUserBIAData.value.deptname == undefined) {
    alert("부서를 선택해주세요.");
    return false;
  }
  if (updateUserBIAData.value.position == "") {
    alert("직위를 선택해주세요.");
    return false;
  }
  if (updateUserBIAData.value.grade == "") {
    alert("직급을 선택해주세요.");
    return false;
  }
  if (updateUserBIAData.value.secretgrade == null) {
    alert("비밀취급인가등급을 선택해주세요.");
    return false;
  }

  return true;
}

</script>