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
  <div :class="{'container':true, 'hidden': !topVisible}">
    <div v-if="isLoading" class="overlay">
      <div class="spinner"></div>
    </div>
    <HomeTop
      :topVisible="!topVisible"
      :returnFunc="loadingFunc"
    />
    <QuickMenu />
    <div class="top-toggle">
      <button class="button" @click="toggleTop">
        <img src="../../assets/images/arrow.png" :class="{ rotated: topRotated }">
      </button>
    </div>
    <div class="left-toggle">
      <button class="button" @click="toggleLeft">
        <img src="../../assets/images/arrow.png" :class="{ rotated: leftRotated }">
      </button>
    </div>
    <div class="content-wrap">
      <div id="content">
        <div class="layout-sub" :style="{height: contentHeight}">
          <Breadcrumbs/>
          <DetailLeft :leftVisible="leftVisible"/>
          <!-- <DetailContent /> -->
          <slot />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watchEffect } from 'vue'
import HomeTop from "@/components/main/HomeTop.vue";
import DetailLeft from "@/components/main/DetailLeft.vue";
import DetailContent from "@/components/main/DetailContent.vue";
import QuickMenu from "@/components/main/QuickMenu.vue";
import Breadcrumbs from "@/components/main/Breadcrumbs.vue";
import { storeToRefs } from 'pinia';

import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth, storeContentHeight } = storeToRefs(mainStore);

const name = ref('DetailLayout')

// const topMenu = ref({data: ""});
// const breadcrumbs = ref({activeLink: "", topMenu: ""});

const topVisible = ref(true);
const leftVisible = ref(true);
const topRotated = ref(false);
const leftRotated = ref(false);

// const contentWidth = ref('1525px');
const contentHeight = ref('calc(100vh - 95px)');

const toggleTop = () => {
  topVisible.value = !topVisible.value;
  topRotated.value = !topRotated.value;

  changeContent();
}

const toggleLeft = () => {
  leftVisible.value = !leftVisible.value;
  leftRotated.value = !leftRotated.value;

  changeContent();
}

const changeContent = () => {
  contentWidth.value = leftVisible.value?'1525px':'1825px';
  contentHeight.value = topVisible.value?'calc(100vh - 95px)':'calc(100vh - 25px)'
  storeContentHeight.value = contentHeight.value;
}

const preventScroll = () => {
  if (!topVisible.value) {
    document.body.classList.add('no-scroll');
  } else {
    document.body.classList.remove('no-scroll');
  }
}

// 로그아웃 시 스피너 위함
const isLoading = ref(false);
const loadingFunc = (retValue) => {
  isLoading.value = retValue;
}

watchEffect(() => {
  preventScroll();
});

</script>

<style lang="scss">
body.no-scroll {
  overflow-y: hidden;
}
</style>


