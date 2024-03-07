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
  <v-overlay
    class="alert-overlay"
    activator="parent"
    location-strategy="connected"
    scroll-strategy="close"
    @click:outside="closeAlert"
  >
    <v-card>
      <v-card-text class="overlay-card">
        <v-list :items="notifications" item-props lines="three">
          <template v-slot:title="{ title }">
            <div v-html="title"  class="font-weight-bold"></div>
          </template>
          <template v-slot:subtitle="{ subtitle }">
            <div v-html="subtitle"></div>
          </template>
        </v-list>
      </v-card-text>
    </v-card>

  </v-overlay>
</template>
    
<script setup>
import console from "console";
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { API } from "@/api";
import _ from 'lodash';
import { storeToRefs } from 'pinia';
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { topMenu, breadcrumbs } = storeToRefs(mainStore)
import { useLoginStore } from '/src/store/Login';
const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)
const name = ref('AlertList')
const props = defineProps({
  toggleFunc: Function
})  
const route = useRoute()
const router = useRouter()
const urlPaths =  ref("");

const selectAlertCondi = ref({
  loginuserid: "",
})
onMounted(async() => {
  await selectAlert();
})

watchEffect(() => {
})

const closeAlert = () => {
  props.toggleFunc();
};

const overlay = ref(false);
const notifications = ref([]);

const selectAlert = async () => {
  selectAlertCondi.value.loginuserid = getUserLoginData.value.userid;
  try {
    const response = await API.useAPI.selectAlert({...selectAlertCondi.value}, urlPaths.value);
    response.data = [
      { type: 'subheader', title: '비밀문서 검토 안내' },
      ...response.data.flatMap(u => {
        const row = [];
        if (u && u.alertcontent) {
          if (u.alertcontent.includes("재분류를 검토하여주십시오")) {
            row.push({
              title: '재분류',
              subtitle: u.alertcontent,
            });
          } else if (u.alertcontent.includes("파기를 검토하여주십시오")) {
            row.push({
              title: '파기',
              subtitle: u.alertcontent,
            });
          } else if (u.alertcontent.includes("이관을 검토하여주십시오")) {
            row.push({
              title: '이관',
              subtitle: u.alertcontent,
            });
          }
          row.push({ type: 'divider', inset: false });
        }
        return row;
      }),
    ];
    notifications.value = response.data;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

</script>
