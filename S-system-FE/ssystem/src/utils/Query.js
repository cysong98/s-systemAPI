/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



const makeQueryString = jsonObject => {
  let queryString = "?"
  let qryStr = ""
  for (var key in jsonObject) {
    if (jsonObject[key] != undefined) {
      qryStr = jsonObject[key].toString().replace(/#/g, '%23')
      queryString += `${key}=${qryStr}&`
    }
  }
  queryString = queryString.slice(0, -1)
  return queryString
};

const setQueries = (route, operationCondi, defaultCondition) => {
  for (const key in defaultCondition)
    operationCondi[key] = route?.query[key] || defaultCondition[key];
};

const setCondiChanged = (operationCondiCheck, newVal, oldVal) => {
  for (const key in operationCondiCheck.flag){
    operationCondiCheck.flag[key] = newVal[key] !== operationCondiCheck.value[key];
    operationCondiCheck.value[key] = newVal[key];
  }
};

const setCondiClear = (operationCondiCheck, oldVal) => {
  for (const key in operationCondiCheck.value)
    operationCondiCheck.value[key] = oldVal[key]
  for (const key in operationCondiCheck.flag)
    operationCondiCheck.flag[key] = false
};

const changeCondi = (operationCondiCheck, operationCondi) => {
  for (const key in operationCondiCheck.value)
    operationCondi[key] = operationCondiCheck.value[key]
};

const checkCondiWithoutPageNum = (operationCondiCheck) => {
  let ret = false;
  for (const key in operationCondiCheck.flag) {
    if (key === "pageNum")
      continue;
    ret = ret || operationCondiCheck.flag[key]
  }
  return ret
};


export { makeQueryString, setQueries, setCondiChanged, setCondiClear, changeCondi, checkCondiWithoutPageNum }

