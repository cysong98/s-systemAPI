/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



import { ref } from 'vue';

const popups = ref([]);
let currentZIndex = 1000;

function togglePopup(component) {
  const index = popups.value.findIndex((popup) => popup.component === component);
  if (index !== -1) {
    popups.value.splice(index, 1);
  }else{
    const zIndex = (currentZIndex += 10);
    const popupInfo = { component, zIndex };
    popups.value.push(popupInfo);
  }
}

function bringPopupToFront(component) {
  const popupInfo = popups.value.find((popup) => popup.component === component);
  if (popupInfo) {
    const highestZIndex = popups.value.reduce((maxZIndex, popup) => {
      return Math.max(maxZIndex, popup.zIndex);
    }, 0);
    popupInfo.zIndex = highestZIndex + 10;
    return popupInfo.zIndex;
  }
  return null;
}

function getPopupSize(popupElement) {
  const width = popupElement.offsetWidth;
  const height = popupElement.offsetHeight;
  return { width, height };
}

function centerPopup(popupElement) {
  const { width, height } = getPopupSize(popupElement);

  const screenWidth = window.innerWidth;
  const screenHeight = window.innerHeight;

  const left = (screenWidth - width) / 2 + 'px';
  const top = (screenHeight - height) / 2 + 'px';

  popupElement.style.marginTop = '0';
  popupElement.style.marginLeft = '0';
  
  popupElement.style.top = top;
  popupElement.style.left = left;
  
}

export { popups, togglePopup, bringPopupToFront, getPopupSize, centerPopup };

