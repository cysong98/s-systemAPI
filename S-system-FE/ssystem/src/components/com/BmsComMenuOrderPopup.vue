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
        <div class="row" style="display: block;">
          <div class="col">
            <v-data-table
              v-model:expanded="expanded"
              :headers="staticColumnsSelectList"
              :items="treeData"
              :item-value="selectMenuListIdentifier"
              items-per-page="1000"
              fixed-header
              show-expand
              hover
              height="500px"
              class="table-type-02"
            >
              <template v-slot:item.name="{ item }">
                <tr>
                  <td :class="{ 'text-left': item }" style="background-color: #FFFFFF00;">
                    {{ item.raw.name }}
                  </td>
                </tr>
              </template>
              <template v-slot:item.menuorder="{ item }">
                <tr>
                  <td style="background-color: #FFFFFF00; width: 200px;">
                    <v-text-field
                      @update:modelValue="(newVal) => addUpdateMenuOrderList(item, 'a', newVal)"
                      v-model="item.raw.menuorder"
                      maxlength="5"
                      style="width: 100%;"
                      hide-details="auto"
                    />
                  </td>
                </tr>
              </template>
              <template v-slot:expanded-row="{ item }">
                <tr v-for="node in item.raw.nodes">
                  <td>{{ node.menuid }}</td>
                  <td :class="{ 'text-left': item }">
                    <pre class="branch-down">          {{ node.name }}</pre>
                  </td>
                  <td>
                    <v-text-field
                      @update:modelValue="(newVal) => addUpdateMenuOrderList(node, 'b', newVal)"
                      v-model="node.menuorder"
                      maxlength="5"
                      style="width: 100%;"
                      hide-details="auto"
                    />
                  </td>
                  <td></td>
                </tr>
              </template>
              <template v-slot:item.data-table-expand="{ item }">
                <tr @click="(e) => e.stopPropagation()">
                  <td style="background-color: #FFFFFF00;">
                    <v-btn
                      v-if="item.raw.nodes.length !== 0"
                      @click="(e) => { clickExpandBtn(e, item) }"
                      :icon="expanded.includes(item.raw.identifier) === false ? 'mdi-chevron-down' : 'mdi-chevron-up'"
                      :ripple="false"
                      size="x-large"
                      color="#FFFFFF00"
                      border="0"
                      rounded
                    ></v-btn>
                  </td>
                </tr>
              </template>
              <template v-slot:bottom></template>
            </v-data-table>
          </div>
        </div>
      </div>
  
      <div class="buttons-bottom" style="padding-top: 0px;">
        <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
        <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="updateMenuOrder">확인</v-btn>
      </div>
    </div>
  </template>
      
  <script setup>
  import console from "console";
  
  import { ref, onMounted, computed, watch } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import _ from 'lodash';
  import { checkRV } from "@/utils/CheckRV.js";
  import { API } from '@/api'; 
  
  const name = ref('BmsComMenuSelect')
  const props = defineProps({
    args: Array,
    toggleFunc: Function
  })
  const treeData = props.args;
  
  const route = useRoute()
  const router = useRouter()
  const urlPaths =  ref('')

  const staticColumnsSelectList = [
    { title: "메뉴아이디", key: "menuid", width: "160px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
    { title: "메뉴명", key: "name", width: "240px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
    { title: "순서", key: "menuorder", width: "120px", sortable: true, filterable: true, align: "center", divider: true, class: "custom-class" },
    { title: "하위메뉴", key: "data-table-expand", width: "40px" },
  ];
    
/* ======================= multi selection ======================= */
const selectMenuListIdentifier = (item) => {
  return item.identifier;
};
/* =============================================================== */

/* ============================= Expandable Table ============================= */
const expanded = ref([]);
const clickExpandBtn = (e, item) => {
  e.stopPropagation();
  const index = expanded.value.findIndex((i) => i.id === item.raw.menuid);
  if (index !== -1) {
    expanded.value.splice(index, 1);
  } else {
    expanded.value.push(item.raw.identifier);
  }
};

/* =========================================================================== */


// Update Menuorder
const updateMenuOrderList = ref([]);
const updateMenuOrder = () => {
  updateMenuOrderList.value.map((item) => {
    if(checkRVMenuOrder(item.menuorder) == false)
      return;
  });

  API.comAPI
    .updateMenuOrder(updateMenuOrderList.value, urlPaths.value)
    .then(response => {
      if (response.status == 200) {
        alert("메뉴 순서가 변경되었습니다");
        props.toggleFunc();
      }
    })
    .catch(error => {
      console.log(error);
    });
};

const addUpdateMenuOrderList = (item, type, newVal) => {
  let index = -1;
  const updateMenuOrderData = ref({
    id: "",
    menuorder: newVal,
    type: "",
  });

  if(type === 'a')
    item = item.raw;

  updateMenuOrderData.value.id = item.menuid;
  updateMenuOrderData.value.type = type;
  index = updateMenuOrderList.value.findIndex((i) => i.id === item.menuid);
  
  if (index !== -1)
    updateMenuOrderList.value.splice(index, 1);
  updateMenuOrderList.value.push(updateMenuOrderData.value);
};


// Toggle Function
const closePopUp = () => {
  props.toggleFunc();
};
    

// Check Validation
const checkRVMenuOrder = (menuorder) => {
  if (
    !checkRV.checkRV(
      menuorder,
      false,
      "checkReal",
      "부서 순서는 숫자만 입력 가능합니다."
    )
  )
    return false;
  if(menuorder.length > 5) {
    alert("부서 순서의 최대길이는 5자입니다.");
    return false;
  }

  return true;
}
</script>