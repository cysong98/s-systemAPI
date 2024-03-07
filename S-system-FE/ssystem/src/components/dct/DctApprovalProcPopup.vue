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
  <div class="modal-body">
    <div class="content">
      <div class="row">
        <div class="col">
          <v-table class="table-type-03">
            <colgroup>
              <col width="120px">
              <col>
            </colgroup>
            <tbody>
              <tr>
                <th>문서 제목</th>
                <td>
                  <v-text-field readonly v-model="args.docttl" variant="solo" hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>처리 의견</th>
                <td>
                  <span class="item-textarea w100">
                    <textarea v-model="args.opinion"></textarea>
                  </span>
                </td>
              </tr>
            </tbody>
          </v-table>
          <div class="box-flex font-red" v-if="args.printflag == 'Y'">
            <v-checkbox v-model="chk" hide-details class="flex-0-1" style="margin-right: 10px;" density="comfort" />
            <span>검토용 인쇄 문서의 파쇄를 완료하였습니다.</span>
          </div>
        </div>
      </div>
    </div>

    <div class="buttons-bottom">
      <v-btn variant="flat" color="grey-lighten-3" rounded="xl" @click="closePopUp">닫기</v-btn>
      <v-btn variant="flat" color="indigo-darken-3" rounded="xl" @click="updateCardBIA1">승인</v-btn>
      <v-btn v-if="args.setBtnYn == 'Y'" variant="flat" color="indigo-darken-3" rounded="xl"
        @click="updateCardBIA2">반려</v-btn>
    </div>

    <div v-if="isloading" class="overlay">
      <div class="spinner"></div>
    </div>
  </div>
</template>

<script setup>
import console from "console";

import dayjs from 'dayjs';
import { ref, onBeforeMount, onBeforeUnmount, onMounted, onUnmounted, computed, watch, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { VDatePicker } from 'vuetify/labs/VDatePicker';
import { checkRV } from "@/utils/CheckRV.js";
import { API } from "@/api";

import { storeToRefs } from 'pinia';
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { topMenu, breadcrumbs } = storeToRefs(mainStore)

const name = ref('DctApprovalProcPopup')
const props = defineProps({
  args: Object,
  toggleFunc: Function,
  returnFunc: Function,
  saveFunc: Function,
  received: Boolean
})
const args = ref({})


const route = useRoute()
const router = useRouter()
// for popup
const childReturnValue = ref({})
const urlPaths = ref('')
// for updateCardBIA1
const updateCardBIA1Data = ref({})
// for updateCardBIA2
const updateCardBIA2Data = ref({})
const chk = ref(false);
const isloading = ref(false);

onBeforeMount(() => {
})

onMounted(() => {
  args.value = props.args
  setBtn();
})

watchEffect(() => {
  args.value = props.args
})

// 승인
const updateCardBIA1 = async () => {
  // if (!confirm("처리하시겠습니까?")) {
  //   return;
  // }
  if (args.value.printflag == 'N') {
    if (props.received != true && args.value.offlineflag != "Y") {
      if(args.value.resasktype != "DEA21" && args.value.resasktype != "DEA31") { // 협조, 검토 아닌 경우만 만들어줌
        await createNo();
      }
    }

    if(errFlag.value == "N") {
      // 스피너 true
      isloading.value = true;
      updateCardBIA1Data.value = args.value;
      updateCardBIA1Data.value.histgubun = "02"; // 결재
      updateCardBIA1Data.value.histstatus = "011"; // 결재

      var updateCardBIA1FormData = new FormData();
      args.value.files.forEach((file) => {
        updateCardBIA1FormData.append('draftAttach', file);
      });

      args.value.refList.forEach((ref) => {
        if (ref.file) {
          updateCardBIA1FormData.append('refAttach', ref.file);
        }
      });

      const { files, ...rest } = updateCardBIA1Data.value;
      const textData = Object.assign({}, rest);

      updateCardBIA1FormData.append(
        "reqUpdateCardBIA1",
        new Blob([JSON.stringify(textData)], {
          type: "application/json",
        })
      );

      await API.dctAPI
        .updateCardBIA1(updateCardBIA1FormData, urlPaths.value)
        .then(response => {
          if (response.status == 200) {
            // 스피너 false
            isloading.value = false;

            // alert("정상 등록되었습니다");

            if (updateCardBIA1Data.value.resasktype != "DEA41" && updateCardBIA1Data.value.resasktype != "DEA45" && updateCardBIA1Data.value.resasktype != "DEA51" && updateCardBIA1Data.value.resasktype != "DEA61" && updateCardBIA1Data.value.resasktype != "DEA71") {
              moveToBmsDctinglist();
            } else {
              moveToBmsDctapprovalcompletelist();
            }
            window.sessionStorage.removeItem('waitFlag');
          }
        })
        .catch(error => {
          // 스피너 false
          isloading.value = false;

          window.sessionStorage.removeItem('waitFlag');

          console.log(error);
          alert("Server Error");
        });
    }
  } else {
    if (chk.value == true) {
      if(props.received != true && args.value.offlineflag != "Y") {
        if (args.value.resasktype != "DEA21" && args.value.resasktype != "DEA31") { // 협조, 검토 아닌 경우만 만들어줌
          await createNo();
        }
      }

      if(errFlag.value == "N") {
        // 스피너 true
        isloading.value = true;

        updateCardBIA1Data.value = args.value;
        updateCardBIA1Data.value.histgubun = "02"; // 결재
        updateCardBIA1Data.value.histstatus = "011"; // 결재

        var updateCardBIA1FormData = new FormData();
        args.value.files.forEach((file) => {
          updateCardBIA1FormData.append('draftAttach', file);
        });

        args.value.refList.forEach((ref) => {
          if (ref.file) {
            updateCardBIA1FormData.append('refAttach', ref.file);
          }
        });

        const { files, ...rest } = updateCardBIA1Data.value;
        const textData = Object.assign({}, rest);

        updateCardBIA1FormData.append(
          "reqUpdateCardBIA1",
          new Blob([JSON.stringify(textData)], {
            type: "application/json",
          })
        );

        await API.dctAPI
          .updateCardBIA1(updateCardBIA1FormData, urlPaths.value)
          .then(response => {
            if (response.status == 200) {
              // 스피너 false
              isloading.value = false;

              // alert("정상 등록되었습니다");

              if (updateCardBIA1Data.value.resasktype != "DEA41" && updateCardBIA1Data.value.resasktype != "DEA45" && updateCardBIA1Data.value.resasktype != "DEA51") {
                moveToBmsDctinglist();
              } else {
                moveToBmsDctapprovalcompletelist();
              }
              window.sessionStorage.removeItem('waitFlag');
            }
          })
          .catch(error => {
            // 스피너 false
            isloading.value = false;

            window.sessionStorage.removeItem('waitFlag');

            console.log(error);
            alert("Server Error");
          });
      }
    } else {
      // 스피너 false
      isloading.value = false;

      alert("파쇄완료 여부를 체크해주세요.");
    }
  }


}

// 반려
const updateCardBIA2 = async () => {
  // 스피너 true
  isloading.value = true;

  // if (!confirm("처리하시겠습니까?")) {
  //   return;
  // }

  if (args.value.printflag == 'N') {
    updateCardBIA2Data.value = args.value;
    updateCardBIA2Data.value.histgubun = "02"; // 결재
    updateCardBIA2Data.value.histstatus = "012"; // 반려

    var updateCardBIA2FormData = new FormData();
    args.value.files.forEach((file) => {
      updateCardBIA2FormData.append('draftAttach', file);
    });

    args.value.refList.forEach((ref) => {
      if (ref.file) {
        updateCardBIA2FormData.append('refAttach', ref.file);
      }
    });

    const { files, ...rest } = updateCardBIA2Data.value;
    const textData = Object.assign({}, rest);

    updateCardBIA2FormData.append(
      "reqUpdateCardBIA2",
      new Blob([JSON.stringify(textData)], {
        type: "application/json",
      })
    );

    API.dctAPI
      .updateCardBIA2(updateCardBIA2FormData, urlPaths.value)
      .then(response => {
        if (response.status == 200) {
          // 스피너 false
          isloading.value = false;

          // alert("정상 등록되었습니다");
          moveToBmsDctapprovalcompletelist();
        }
      })
      .catch(error => {
        // 스피너 false
        isloading.value = false;

        console.log(error);
        alert("Server Error");
      });
  } else {
    if (chk.value == true) {
      updateCardBIA2Data.value = args.value;
      updateCardBIA2Data.value.histgubun = "02"; // 결재
      updateCardBIA2Data.value.histstatus = "012"; // 반려

      var updateCardBIA2FormData = new FormData();
      args.value.files.forEach((file) => {
        updateCardBIA2FormData.append('draftAttach', file);
      });

      args.value.refList.forEach((ref) => {
        if (ref.file) {
          updateCardBIA2FormData.append('refAttach', ref.file);
        }
      });

      const { files, ...rest } = updateCardBIA2Data.value;
      const textData = Object.assign({}, rest);

      updateCardBIA2FormData.append(
        "reqUpdateCardBIA2",
        new Blob([JSON.stringify(textData)], {
          type: "application/json",
        })
      );

      API.dctAPI
        .updateCardBIA2(updateCardBIA2FormData, urlPaths.value)
        .then(response => {
          if (response.status == 200) {
            // 스피너 false
            isloading.value = false;

            // alert("정상 등록되었습니다");
            moveToBmsDctapprovalcompletelist();
          }
        })
        .catch(error => {
          // 스피너 false
          isloading.value = false;

          console.log(error);
          alert("Server Error");
        });
    } else {
      // 스피너 false
      isloading.value = false;

      alert("파쇄완료 여부를 체크해주세요.");
    }
  }
}

// Popup
const closePopUp = () => {
  // to do
  props.toggleFunc();
}

const confirmPopUp = () => {
  alert("확인되었습니다.");
  props.returnFunc(childReturnValue.value);
}

const zIndex = ref(1000);
const handlePopupClick = (event) => {
  if (event.target.closest('.window-modal')) {
    zIndex.value = bringPopupToFront('DctApprovalProcPopup');
  }
}

// DEA41(결재), DEA51(전결) 인 경우에만 반려버튼 보이도록
const setBtn = () => {
  const resuserid = args.value.resuserid;
  for (let i = 0; i < args.value.pathList.length; i++) {
    if (resuserid == args.value.pathList[i].objid) {
      if (args.value.pathList[i].asktype == 'DEA41' || args.value.pathList[i].asktype == 'DEA51' || args.value.pathList[i].asktype == 'DEA61' || args.value.pathList[i].asktype == 'DEA71') {
        args.value.setBtnYn = 'Y';
      }
      args.value.resasktype = args.value.pathList[i].asktype;
    }
  }

}

// Move Function
// 진행비밀함
const moveToBmsDctinglist = () => {
  let arr = ['비밀문서', '결재', '진행비밀함'];

  breadcrumbs.value.activeLink = arr;

  router.push({
    name: "BmsDctinglist",
  });
};

// 결재완료함
const moveToBmsDctapprovalcompletelist = () => {
  let arr = ['비밀문서', '결재', '결재완료함'];

  breadcrumbs.value.activeLink = arr;

  router.push({
    name: "BmsDctapprovalcompletelist",
  });
};

// 관리번호, 사본번호 생성
const errFlag = ref("N");
const createNo = async () => {
  try {
    // API 호출
    let tempSeq = await API.dctAPI.getTempSeq({

    },
      urlPaths.value
    );

    let recentMgmt = await API.dctAPI.getRecentMgmt({

    },
      urlPaths.value
    );

    // 비밀등급 변환
    let seclevel = ""
    if (args.value.seclevel == "2") {
      seclevel = "II";
    }
    if (args.value.seclevel == "3") {
      seclevel = "III";
    }
    if (args.value.seclevel == "4") {
      seclevel = "대외비";
    }
    if (args.value.seclevel == "5") {
      seclevel = "일반";
    }

    // 년도
    let curDate = new Date();
    let curYear = curDate.getFullYear();

    // 가장 최근 비밀관리기록부 데이터의 등록일 년도보다 현재 년도가 크면 시퀀스 reset
    if (recentMgmt.data != null && recentMgmt.data != undefined && recentMgmt.data != "") {
      if (Number(curYear) > Number(recentMgmt.data.toString().substring(0, 4))) {
        let resetTempSeq = await API.dctAPI.resetTempSeq({

        },
          urlPaths.value
        );

        tempSeq = resetTempSeq;
      }
    }

    // 관리번호 생성
    let mgmtno = seclevel + "-" + curYear + "-" + tempSeq.data;
    args.value.mgmtno = mgmtno;

    // 사본번호 생성
    let copyno = "";
    if (args.value.recvList != null && args.value.recvList.length > 0) {
      if (args.value.recvList[0].receiverid != "") {
        copyno = "원본/" + args.value.recvList.length;
      } else {
        copyno = "원/본";
      }
    } else {
      copyno = "원/본";
    }
    args.value.copyno = copyno;

    // 웹한글기안기 저장
    isloading.value = true;
    await props.saveFunc('Y', 'Y');

    // 세션 스토리지 값이 Y로 변경될 때까지 대기
    let waitCnt = 0;
    while (window.sessionStorage.getItem('waitFlag') !== 'Y') {
      // 1초 대기 후 다시 확인
      await new Promise(resolve => setTimeout(resolve, 1000));

      if(window.sessionStorage.getItem('waitFlag') == null) {
        alert("웹한글기안기 오류");
        errFlag.value = "Y";
        break;
      }

      if(waitCnt > 60) {
        alert("Timeout Error");
        errFlag.value = "Y";
        break;
      }

      waitCnt++;
    }

    // await updateCardBIA1();

  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

</script>

<style lang="scss" scoped>
.opinion {
  height: 200px;
  border: 1px solid lightgray;
  border-radius: 5px;
  width: 500px;
  resize: none;
}
</style>
