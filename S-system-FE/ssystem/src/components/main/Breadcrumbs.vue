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
  <div class="bread-crumbs">
    <ul>
      <li v-for="(item, index) in breadcrumbs.activeLink" :key="index">
        <RouterLink to="" style="cursor: auto;">{{ item }}</RouterLink>
      </li>
    </ul>
  </div>
</template>

<script setup>
import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';

import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { topMenu, breadcrumbs } = storeToRefs(mainStore)

const name = ref('HomeTop')
const route = useRoute()
const router = useRouter()

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)
const manageflag = ref(false);
onBeforeMount(() => {
})

onMounted(() => {
  manageflag.value = checkManagerFlag();
  if (manageflag.value == false) {
    handleActiveLinkForNonManager(breadcrumbs, '공지사항');
    handleActiveLinkForNonManager(breadcrumbs, 'FAQ');
    handleActiveLinkForNonManager(breadcrumbs, 'Q&A');
  }else{
    let arr = [];
    if((typeof breadcrumbs.value.activeLink) == 'string') {
      arr = breadcrumbs.value.activeLink.split(',');
      breadcrumbs.value.activeLink = arr;
    }
  }
})

watch(()=>breadcrumbs.value.activeLink, () => {
  if (manageflag.value == false) {
    handleActiveLinkForNonManager(breadcrumbs, '공지사항');
    handleActiveLinkForNonManager(breadcrumbs, 'FAQ');
    handleActiveLinkForNonManager(breadcrumbs, 'Q&A');
  }else{
    let arr = [];
    if((typeof breadcrumbs.value.activeLink) == 'string') {
      arr = breadcrumbs.value.activeLink.split(',');
      breadcrumbs.value.activeLink = arr;
    }
  }
});

const checkManagerFlag = () => {
  return getUserLoginData.value.hasOwnProperty('vgroupid') && getUserLoginData.value.vgroupid.includes('4001');
};

const handleActiveLinkForNonManager = (breadcrumbs, type) => {
  if (breadcrumbs.value.activeLink.includes(` ${type} 관리`) || breadcrumbs.value.activeLink.includes(`${type} 관리`)) {
    breadcrumbs.value.activeLink = ['HOME', '게시판', type];
  }
};
</script>

<style lang="scss" scoped></style>
