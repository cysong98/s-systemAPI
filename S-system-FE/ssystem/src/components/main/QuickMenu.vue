<script setup>
import { useRoute, useRouter } from 'vue-router';

import { storeToRefs } from 'pinia';
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { topMenu, breadcrumbs } = storeToRefs(mainStore)

const router = useRouter()

const moveToFaqList = () => {
  router.push({
    name: "BmsComfaqlist"
  });

  topMenu.value.data = 'D';
  breadcrumbs.value.topMenu = '게시판관리';
  breadcrumbs.value.activeLink = ['관리자', '게시판관리', 'FAQ 관리'];
}

const moveToQnaList = () => {
  router.push({
    name: "BmsComqnalist"
  });

  topMenu.value.data = 'D';
  breadcrumbs.value.topMenu = '게시판관리';
  breadcrumbs.value.activeLink = ['관리자', '게시판관리', 'Q&A 관리'];
}

const moveToNoticeList = () => {
  router.push({
    name: "BmsComnoticelist"
  });

  topMenu.value.data = 'D';
  breadcrumbs.value.topMenu = '게시판관리';
  breadcrumbs.value.activeLink = ['관리자', '게시판관리', '공지사항 관리'];
}

const moveToCreateCard = () => {
  router.push({
    name: "BmsDctcreatecard"
  });

  topMenu.value.data = 'B';
  breadcrumbs.value.topMenu = '비밀문서';
  breadcrumbs.value.activeLink = ['비밀문서', '기안', '비밀작성'];
}

const toggleMenu = () => {
  let quickMenu = document.getElementById("quickMenu");
  quickMenu.classList.toggle("active")
}
</script>

<template>
  <div class="quick-menu" id="quickMenu">
    <button type="button" class="button faq" @click="moveToFaqList()">
      <strong class="title">FAQ</strong>
    </button>
    <button type="button" class="button qna" @click="moveToQnaList()">
      <strong class="title">QNA</strong>
    </button>
    <button type="button" class="button notice" @click="moveToNoticeList()">
      <strong class="title">공지사항</strong>
    </button>
    <button type="button" class="button write" @click="moveToCreateCard()">
      <strong class="title">비밀문서작성</strong>
    </button>
    <button type="button" class="button toggle" @click="toggleMenu()">
      <strong class="title">퀵메뉴</strong>
    </button>
  </div>
</template>