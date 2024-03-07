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
  <div>
    <BmsUseReqList
    >  </BmsUseReqList>
  </div>
</template>

<script setup>
import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import BmsUseReqList from "@/components/use/BmsUseReqList.vue";

const name = ref('BmsUsereqlistView')
const route = useRoute()
const router = useRouter()

onBeforeMount(() => {
})

onMounted(() => {
})

watchEffect(() => {
})


</script>

<style lang="scss"></style>
