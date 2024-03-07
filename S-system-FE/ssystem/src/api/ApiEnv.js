/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

function detectLocal() {
  var returnVal = false;
  var currentUrl = window.location.origin;

  if (currentUrl.includes("local") || currentUrl.includes("127.0")) {
    returnVal = true;
  } else {
    returnVal = false;
  }
  return returnVal;
}

// false =>  배포용
// true => 개발용
export const env =  {
  API_URL: detectLocal()
    ? "http://localhost:8580"
    : "http://165.243.102.104:8580",

  // 바코드 크롬 cors 해제 주소
  // chrome://flags/#block-insecure-private-network-requests
  // disabled로 설정
};

