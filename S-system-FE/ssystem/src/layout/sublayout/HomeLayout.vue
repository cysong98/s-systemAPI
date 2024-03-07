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
  <!-- To do -->
  <div class="container main">
    <div v-if="isLoading" class="overlay">
      <div class="spinner"></div>
    </div>
    <HomeTop :returnFunc="loadingFunc" />
    <!-- <HomeContent /> -->
    <slot />
  </div>
</template>

<script setup>
import { ref, watchEffect } from 'vue'
import HomeTop from "@/components/main/HomeTop.vue";
import HomeContent from "@/components/main/HomeContent.vue";
const name = ref('HomeLayout')

// 로그아웃 시 스피너 위함
const isLoading = ref(false);
const loadingFunc = (retValue) => {
  isLoading.value = retValue;
}
</script>

<style lang="scss">

</style>


