/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

import { createRouter, createWebHistory } from 'vue-router'
import AnyXViews from '../views/AnyXViews.vue'
import { LoginLayoutRoutes } from './LoginLayoutRouter.js';
import { HomeLayoutRoutes } from './HomeLayoutRouter.js';
import { DetailLayoutRoutes } from './DetailLayoutRouter.js';


// eslint-disable-next-line
const routes = [
              ...LoginLayoutRoutes
              , ...HomeLayoutRoutes
              , ...DetailLayoutRoutes
              , {
                path: "/anyxviews",
                name: "anyxviews",
                component: AnyXViews
              }
              // otherwise redirect to home
              , { path: '/:pathMatch(.*)*', redirect: '/login/Login' }
];

const isServer = typeof window === 'undefined';
const router = createRouter({
    history: isServer ? createMemoryHistory() : createWebHistory(), // uris without hashes #, see https://router.vuejs.org/guide/essentials/history-mode.html#html5-history-mode
    routes
});

router.beforeEach((to, from, next) => {
    next();
//  if (to.matched.some(record => record.meta.requiresAuth)) {
//    if (!store.getters.isLoggedIn) {
//      next({
//        name: 'Login'
//      })
//    } else {
//      next();
//    }
//  } else {
//    next();
//  }
});

export default router;
