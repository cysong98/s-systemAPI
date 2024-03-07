/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

import { computed, ref } from "vue";
import { API } from "/src/api";

const chiefid = ref("");
const findChiefId = async (id) => {
  try {
    const response = await API.dctAPI.selectOrganization({ orgid: id }, "");
    chiefid.value = response.data[0].chiefid;
  } catch (error) {
    alert("Server Error");
  }
  return chiefid.value;
};

const chiefInfo = ref({});
const findChiefInfo = async (id) => {
  try {
    const response = await API.dctAPI.selectOrganizationInfo({ findchiefinfo: "yes", userid: id }, "");
    chiefInfo.value = response.data[0];
  } catch (error) {
    alert("Server Error");
  }
  return chiefInfo.value;
};

/* 
  사용 요청서 경로 지정
  - 요청자/비밀담당자/비밀담당자의 부서장
*/
const isChiefMe = ref(false)

// 공통 경로 생성
const createPath = (apprreqseq, apprtemp, apprtype, apprgubun, apprstatus, item, rolePrefix, user) => {
  const approverId = rolePrefix === '비밀담당자' ? item.authorid : item.userid;
  const approverName = rolePrefix === '비밀담당자' ? item.authorname : item.username;
  const approverDutyname = rolePrefix === '비밀담당자' ? item.authordutyname : item.grade;
  const roleInfo = rolePrefix ? `(${rolePrefix}) ` : '';
  const approverInfo = `${approverDutyname}/${approverName}`;
  return {
    apprreqseq,
    apprtemp,
    apprtype,
    apprgubun,
    apprstatus,
    apprid: approverId,
    apprname: approverName,
    apprdeptid: item.deptid,
    apprdeptname: item.deptname,
    apprdutyname: approverDutyname,
    apprnametemp: `${roleInfo}${approverInfo}`,
    rgstid: user.userid,
    rgstname: user.username,
    identifier: `${item.deptid}-${approverId}`
  };
};

// 요청자 경로
const  createRequesterPath = (user) => {
  return createPath(1, "요청", "1", "1", "APP00", user, null, user);
};

// 기안 담당자 경로
const createManagerPath = (info, user) => {
  return createPath(2, "승인", "3", "2", "APP10", info, "비밀담당자", user);
};

// 부서장 경로
const createHeadPath = (info, user) => {
  return createPath(3, "승인", "3", "2", "APP10", info, "비밀담당자 부서장", user);
};

// 부서장 1인결재 경로
const createHeadMePath = (input, user) => {
  const existingHeadsIndex = input.draftmanagersheads.findIndex(manager => manager.apprid === user.userid);
  if (existingHeadsIndex !== -1) {
    input.draftmanagersheads.splice(existingHeadsIndex, 1);
  }
  isChiefMe.value = true;
  return createPath(1, "1인결재", "4", "1", "APP30", user, null, user);
};

// 비밀 담당자들 등록
const setDraftManagers = async(input, mgmtlist, user) => {
  input.draftmanagers = [];
  const addedAuthorIdsSet = new Set();
  mgmtlist.forEach(data => {
    if (!addedAuthorIdsSet.has(data.authorid)) {
      const manager = createManagerPath(data, user);
      input.draftmanagers.push(manager);
      addedAuthorIdsSet.add(data.authorid);
    }
  });
  return input;
};

// 비밀 담당자가 속한 부서의 부서장들 등록
const setDraftManagersDeptHeads = async(input, user) => {
  const addedHeadsDeptIdsSet = new Set();
  input.draftmanagers.forEach(data => {
    if (!addedHeadsDeptIdsSet.has(data.apprdeptid)) {
      addedHeadsDeptIdsSet.add(data.apprdeptid);
    }
  });
  const addedHeadsDeptIds = Array.from(addedHeadsDeptIdsSet);
  const chiefInfos = [];
  for (const apprdeptid of addedHeadsDeptIds) {
    const chiefId = await findChiefId(apprdeptid);
    if (chiefId) {
      const chiefInfo = await findChiefInfo(chiefId);
      chiefInfos.push(chiefInfo);
      // 부서장이 비밀담당자들 목록에 있다면 삭제
      const existingManagerIndex = input.draftmanagers.findIndex(manager => manager.apprid === chiefId);
      if (existingManagerIndex !== -1) {
        input.draftmanagers.splice(existingManagerIndex, 1);
      }
    }
  }
  if (chiefInfos.length > 0) {
    for (const chiefInfo of chiefInfos) {
      const head = createHeadPath(chiefInfo, user);
      input.draftmanagersheads.push(head);
    }
  }
  return input;
};

// 요청자
const setRequester = async(input, user) => {
  // 요청자가 비밀담당자들 목록에 있다면 삭제
  const existingManagerIndex = input.draftmanagers.findIndex(manager => manager.apprid === user.userid);
  if (existingManagerIndex !== -1) {
    input.draftmanagers.splice(existingManagerIndex, 1);
  }
  const isDraftManagerHead = input.draftmanagersheads.some(data => data.apprid === user.userid);
  const draftManagerCount = input.draftmanagers.length;
  const removeDraftManagerHead = () => {
    const indexToRemove = input.draftmanagersheads.findIndex(data => data.apprid === user.userid);
    if (indexToRemove !== -1) { 
      input.draftmanagersheads.splice(indexToRemove, 1);
    }
  };
  const requester = isDraftManagerHead
    ? (draftManagerCount >= 1
      ? (removeDraftManagerHead(), createRequesterPath(user)) // 비밀담당자 존재O, 부서장이 요청자일 경우 -> 부서장을 승인자에서 요청자로 전환
      : createHeadMePath(input, user)) // 비밀담당자 존재X, 부서장이 요청자일 경우 -> 부서장 1인결재
    : createRequesterPath(user);
  input.requester.push(requester);
  return input;
};

// pathList에 추가
const insertUsePathList = async(input, mgmtlist, user) => {
  await setDraftManagers(input, mgmtlist, user);
  await setDraftManagersDeptHeads(input, user);
  await setRequester(input, user);
  const requester = input.requester;
  const draftmanagers = input.draftmanagers;
  const draftmanagersheads = input.draftmanagersheads;
  input.pathList = [];
  input.pathList.push(...requester, ...draftmanagers, ...draftmanagersheads);
  return input;
};

// 결재선지정 팝업 내에서 초기선택될 부서 (pathList 데이터 중 마지막으로)
const reqContentDeptid = ref('');
const reqContentDeptname = ref('');
const getDeptInfo = async(input) => {
  input.pathList.forEach(path=>{
    reqContentDeptid.value = path.apprdeptid;
    reqContentDeptname.value = path.apprdeptname;
  })
};

// 디폴트 경로 유저들의 id 저장(요청자 제외)
const defaultPathIds = ref([]);
const getDefaultPathUsers = async (input) => {
  input.pathList.forEach((path, index) => {
    if (index !== 0) {
      defaultPathIds.value.push(path.apprid);
    }
  });
};

// 선택된 경로 데이터들 중 비밀담당자 또는 비밀담당자의 부서장이 존재하는지 체크
const checkPathList = (input) => {
  return input.pathList.slice(1).some((data) => defaultPathIds.value.includes(data.apprid));
};


// 공통 경로 생성 (존안/이관 용도)
const createPath2 = (apprreqseq, apprtemp, apprtype, apprgubun, apprstatus, item, user) => {
  const apprnametemp = apprtemp === "요청"
  ? `${item.grade}/${item.username}`
  : `(${item.vgroupname}) ${item.grade}/${item.username}`;
  return {
    apprreqseq,
    apprtemp,
    apprtype,
    apprgubun,
    apprstatus,
    apprid: item.userid,
    apprname: item.username,
    apprdeptid: item.deptid,
    apprdeptname: item.deptname,
    apprdutyname: item.grade,
    apprnametemp,
    rgstid: user.userid,
    rgstname: user.username,
    identifier: `${item.deptid}-${item.userid}`,
    vgroupid: item.vgroupid ? item.vgroupid : '',
    vgroupname: item.vgroupname ? item.vgroupname : ''
  }
};

// 요청자 경로 (존안/이관 용도)
const createRequesterPath2 = (user) => {
  return createPath2("1", "요청", "1", "1", "APP00", user, user);
};

// 존안/이관 담당자/부서장 경로
const createManagerPath2 = (info, user) => {
  return createPath2("2", "승인", "3", "2", "APP10", info, user);
};


// 존안/이관 부서장 1인결재 경로
const createHeadMePath2 = (input, user) => {
  const existingHeadsIndex = input.headers.findIndex(manager => manager.apprid === user.userid);
  if (existingHeadsIndex !== -1) {
    user.vgroupname = input.headers[existingHeadsIndex].vgroupname;
  }
  isChiefMe.value = true;
  input.managers = [];
  input.headers = [];
  return createPath2("1", "1인결재", "4", "1", "APP30", user, user);
};

// 존안/이관 담당자/부서장 등록
const setManagers2 = async(input, managerlist, user) => {
  input.managers = [];
  input.headers = [];
  managerlist.forEach(data => {
    if(['8010', '8050'].includes(data.vgroupid)){ // 8010: 존안관리담당자, 8050: 이관관리담당자
      const manager = createManagerPath2(data, user);
      input.managers.push(manager);
    }else if(['8015','8255'].includes(data.vgroupid)){ // 8015: 존안관리부서장, 8255: 이관관리부서장
      const manager = createManagerPath2(data, user);
      input.headers.push(manager);
    }
  });
  return input;
};

// 요청자 등록 (존안/이관 용도)
const setRequester2 = async(input, user) => {
  // 요청자가 담당자들 목록에 있다면 삭제
  const existingManagerIndex = input.managers.findIndex(manager => manager.apprid === user.userid);
  if (existingManagerIndex !== -1) {
    input.managers.splice(existingManagerIndex, 1);
  }
  // 요청자가 존안/이관 부서장일 경우
  let isHead = false;
  const vgroupidArr = user.vgroupid.split(',').map(value => value.trim());
  if (vgroupidArr.length > 0 && (vgroupidArr.includes('8015') || vgroupidArr.includes('8255'))) {
    isHead = true;
    const chief = createHeadMePath2(input, user);
    input.requester.push(chief);
  }else{
    const requester = createRequesterPath2(user);
    input.requester.push(requester);
  }
  return input;
};

// pathList에 추가 (존안/이관 용도)
const insertUsePathList2 = async(input, managerlist, user) => {
  await setManagers2(input, managerlist, user);
  await setRequester2(input, user);
  const requester = input.requester;
  const managers = input.managers;
  const heads = input.headers;
  input.pathList = [];
  input.pathList.push(...requester, ...managers, ...heads);
  return input;
};

// 디폴트 경로 유저들의 id 저장 (존안/이관 용도)
const defaultPathIds2 = ref([]);
const type = ref('');
const getDefaultPathUsers2 = async (input) => {
  defaultPathIds2.value = [];
  input.pathList.forEach(path => {
    let vgroupid = '';
    if (path.vgroupid.includes(',')) {
      const vgroupidArr = path.vgroupid.split(',').map((value) => value.trim());
      const matchingValue = vgroupidArr.find((value) => ['8010', '8015', '8050', '8255'].includes(value));
      if (matchingValue) {
        vgroupid = matchingValue;
      }
    }else{
      vgroupid = path.vgroupid;
    }
    type.value = ['8010','8015'].includes(vgroupid) ? '존안' : '이관';
    defaultPathIds2.value.push({userid: path.apprid,vgroupid: vgroupid});
  });
};

// 선택된 경로 데이터들 중 존안/이관 담당자1명 부서장1명씩 존재하는지 체크
const checkPathList2 = (input) => {
  const pathLists = input.pathList.map((data) => {
    const matchingData = defaultPathIds2.value.find((item) => item.userid === data.apprid);
    return matchingData ? matchingData.vgroupid : null;
  });
  const targetVgroupIds = type.value === '존안' ? ['8010', '8015'] : ['8050', '8255'];
  const hasAllTargets = targetVgroupIds.every((target) => pathLists.includes(target));
  return hasAllTargets;
};

export { 
  findChiefId, findChiefInfo, isChiefMe,
  createPath, createRequesterPath, createManagerPath, createHeadPath, createHeadMePath,
  setDraftManagers, setDraftManagersDeptHeads, setRequester,
  insertUsePathList,
  getDefaultPathUsers, defaultPathIds,
  getDeptInfo, reqContentDeptid, reqContentDeptname,
  checkPathList,
  insertUsePathList2, getDefaultPathUsers2, checkPathList2,
};
