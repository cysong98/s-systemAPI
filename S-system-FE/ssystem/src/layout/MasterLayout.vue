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
  <component :is="layout || 'HomeLayout'">
    <slot />
  </component>
</template>

<script setup>
import { shallowRef, ref, watchEffect, markRaw } from 'vue'
import HomeLayout from "./sublayout/HomeLayout.vue"
import LoginLayout from "./sublayout/LoginLayout.vue"
import { useRoute } from 'vue-router'
const name = shallowRef('MasterLayout')
const layout = ref('')
const route = useRoute()
watchEffect(async () => {
  try {
    const component = await import(`./sublayout/${route.meta.layout}.vue`)
    layout.value = markRaw(component?.default || HomeLayout)
  } catch (e) {
    layout.value = LoginLayout
  }                  
})
</script>

<style lang="scss">

</style>


