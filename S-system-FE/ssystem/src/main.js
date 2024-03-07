/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



import { createApp } from "vue";
import axios from "axios";
import App from "./App.vue";
import router from "./router";
import { createPinia } from "pinia";
import vuetify from "./plugins/vuetify";
import { loadFonts } from "./plugins/webfontloader";

import vueFilePond from 'vue-filepond';
import FilePondPluginFileValidateType from 'filepond-plugin-file-validate-type/dist/filepond-plugin-file-validate-type.esm.js';
import 'filepond/dist/filepond.min.css';


// 2023-10-11 lbh, 퍼블리싱 scss import
import "@/assets/scss/common.scss";

const pinia = createPinia()
const FilePond = vueFilePond(FilePondPluginFileValidateType);
const app = createApp(App);


app.component('FilePond', FilePond);
app.use(pinia);
app.use(router);
app.use(vuetify);
app.mount('#app')

loadFonts();
app.config.globalProperties.$axios = axios;
app.config.globalProperties.$filters = {
  currencyUSD(value) {
    return '$' + value
  },
  currencyKOR(value) {
    return '￦' + value
  },
}

app.config.globalProperties.$Progress = () => {}

app.provide('filters', app.config.globalProperties.$filters)
app.provide('progress', app.config.globalProperties.$Progress)

