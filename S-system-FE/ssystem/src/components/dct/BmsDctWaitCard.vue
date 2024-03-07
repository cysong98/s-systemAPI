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
  <div class="page" :style="{width: contentWidth}">
    <div class="box-title">
      <h4 class="h4">결재</h4>
    </div>

    <div class="box-content">
      <!-- 비밀작성 -->
      <section class="section sticky">
        <div class="box-title">
          <h5 v-if="draftToggle.data == 'N'" class="h5">비밀조회</h5>
          <h5 v-else class="h5">기안문조회</h5>
          <div class="action-buttons">
            <v-btn class="button" @click="dctReviewPrintPopupToggleFunc()" :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true">검토용인쇄</v-btn>
            <v-btn v-if="draftToggle.data == 'N' && rewriteFlag.data == 'N'" class="button" @click="writeDraftDoc()">기안문조회</v-btn>
            <v-btn v-if="draftToggle.data == 'N' && rewriteFlag.data == 'Y'" class="button" @click="rewriteDraftDoc()">기안문작성</v-btn>
            <v-btn v-if="draftToggle.data == 'Y' && (me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true)" class="button" @click="writeDraftDoc()">비밀카드</v-btn>
            <v-btn class="button" @click="dctProcPopupToggleFunc()">문서처리</v-btn>
            <v-btn class="button" @click="dctPathDirectPopupToggleFunc()" :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true || me.asktype == 'DEA41' || me.asktype == 'DEA51' || me.asktype == 'DEA61' || me.asktype == 'DEA71'">결재선지정</v-btn>
            <v-btn class="button" @click="dctRecvPopupToggleFunc()" :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true || chkbox == true">수신지정</v-btn>
            <v-btn class="button" @click="dctReadDirectPopupToggleFunc()" :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true">열람지정</v-btn>
            <v-btn class="button" @click="dctLogoSymbolPopupToggleFunc()" :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true">로고/심볼</v-btn>
            <v-btn class="button" @click="dctTempPopupToggleFunc()" :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true">임시저장</v-btn>
            <v-btn class="button" @click="moveToBmsDctwaitlist()">목록</v-btn>
          </div>
        </div>
      </section>

      <div v-show="draftToggle.data == 'N'">
      <!-- 비밀속성 -->
        <section class="section">
          <div class="box-title">
            <h6 class="h6">비밀속성</h6>
          </div>
          <v-table :class="{ 'table-type-03-copydoc': isReceivedDoc, 'table-type-03': !isReceivedDoc }">
            <colgroup>
              <col width="120">
              <col width="">
              <col width="120">
              <col width="">
              <col width="120">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th><span class="required">비밀등급</span></th>
                <td>
                  <v-select
                    v-model="updateCardData.seclevel"
                    :items="secLevelData"
                    item-title="secLevelDataKey"
                    item-value="secLevelDataValue"
                    variant="outlined"
                    hide-details="auto"
                    :readonly="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true"></v-select>
                </td>
                <th>관리번호</th>
                <td>
                  <v-text-field
                    v-model="updateCardData.mgmtno"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>사본번호</th>
                <td>
                  <v-text-field
                    v-model="updateCardData.copyno"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
                <th>내부생성<br>(사본없음)</th>
                <td><v-checkbox class="v-checkbox-square-btn" v-model="chkbox" hide-details="auto" @change="changeChkbox()" :readonly="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true"></v-checkbox></td>
              </tr>
              <tr>
                <th  rowspan="3"><span class="required">예고문</span></th>
                <td colspan="7" v-if="!isReceivedDoc">
                  <!-- 예고문 - 보존기간 -->
                  <div class="box-flex">
                    <v-label>보존기간:</v-label>
                    <v-select
                      class="custom-v-select"
                      v-model="updateCardData.prsrvterm"
                      @update:modelValue="updatePrsrvterm"
                      :items="prsRvTermData"
                      item-title="prsRvTermDataKey"
                      item-value="prsRvTermDataValue"
                      required
                      variant="outlined"
                      hide-details="auto"
                      :readonly="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true"></v-select>
                  </div>
                </td>
              </tr>
              <tr v-if="!isReceivedDoc">
                <td colspan="7">
                  <!-- 예고문 - 원본보호기간 -->
                  <div class="box-flex">
                    <v-label>원본보호기간:</v-label>
                    <v-select
                      v-model="orgnProtDtSelected"
                      @update:modelValue="updateOrgnDt"
                      :items="orgnProtDtData"
                      variant="outlined"
                      hide-details="auto"
                      :readonly="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true"></v-select>
                    <v-text-field
                      v-if="orgnProtDtSelected == '직접입력'"
                      v-model="orgnUserDate"
                      type="date"
                      @update:modelValue="getOrgnUserDate"
                      variant="outlined"
                      hide-details="auto"
                      :readonly="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true">
                    </v-text-field>

                    <template v-else>
                      <v-select
                        v-model="orgnProtDtPeriodSelected"
                        :items="orgnProtDtPeriodData"
                        @update:modelValue="getOrgnDate"
                        item-title="key"
                        item-value="value"
                        variant="outlined"
                        hide-details="auto"
                        :readonly="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true">
                      </v-select>
                      <v-text-field
                        v-model="orgnDate.year"
                        :readonly="true"
                        variant="outlined"
                        hide-details="auto"></v-text-field>
                      <v-label>년</v-label>
                      <v-text-field
                        v-model="orgnDate.month"
                        :readonly="true"
                        variant="outlined"
                        hide-details="auto"></v-text-field>
                      <v-label>월</v-label>
                      <v-text-field
                        v-model="orgnDate.day"
                        :readonly="true"
                        variant="outlined"
                        hide-details="auto"></v-text-field>
                      <v-label>일</v-label>
                    </template>
                      <v-select
                        v-model="orgnReclassSelected"
                        :items="orgnReclass"
                        variant="outlined"
                        hide-details="auto"
                        :readonly="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true">
                      </v-select>
                      <v-label>(으)로</v-label>
                      <v-select
                        v-model="updateCardData.orgnreclass"
                        :items="orgnReclassData"
                        @update:modelValue="selectedOrgnReClass"
                        item-title="key"
                        item-value="value"
                        variant="outlined"
                        hide-details="auto"
                        :readonly="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true">
                      </v-select>
                  </div>
                </td>
              </tr>
              <tr v-if="chkbox == false || isReceivedDoc == true">
                <td colspan="7">
                  <!-- 예고문 - 사본보호기간 -->
                  <div class="box-flex">
                    <v-label>사본보호기간:</v-label>
                    <v-select
                      v-model="copyProtDtSelected"
                      @update:modelValue="updateCopyDt"
                      :items="copyProtDtData"
                      variant="outlined"
                      hide-details="auto"
                      :readonly="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true"></v-select>
                    <v-text-field
                      v-if="copyProtDtSelected == '직접입력'"
                      v-model="copyUserDate"
                      type="date"
                      @update:modelValue="getCopyUserDate"
                      variant="outlined"
                      hide-details="auto"
                      :readonly="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true">
                    </v-text-field>

                    <template v-else>
                      <v-select
                        v-model="copyProtDtPeriodSelected"
                        @update:modelValue="getCopyDate"
                        :items="copyProtDtPeriodData"
                        item-title="key"
                        item-value="value"
                        variant="outlined"
                        hide-details="auto"
                        :readonly="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true">
                      </v-select>
                      <v-text-field
                        v-model="copyDate.year"
                        :readonly="true"
                        variant="outlined"
                        hide-details="auto"></v-text-field>
                      <v-label>년</v-label>
                      <v-text-field
                        v-model="copyDate.month"
                        :readonly="true"
                        variant="outlined"
                        hide-details="auto"></v-text-field>
                      <v-label>월</v-label>
                      <v-text-field
                        v-model="copyDate.day"
                        :readonly="true"
                        variant="outlined"
                        hide-details="auto"></v-text-field>
                        <v-label>일</v-label>
                    </template>
                      <v-select
                        v-model="copyReclassSelected"
                        :items="copyReclass"
                        variant="outlined"
                        hide-details="auto"
                        :readonly="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true">
                      </v-select>
                      <v-label>(으)로</v-label>
                      <v-select
                        v-model="updateCardData.copyreclass"
                        :items="copyReclassData"
                        @update:modelValue="selectedCopyReClass"
                        item-title="key"
                        item-value="value"
                        variant="outlined"
                        hide-details="auto"
                        :readonly="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true">
                      </v-select>
                  </div>
                </td>
              </tr>
            </tbody>
          </v-table>
        </section>

        <!-- 표제부 -->
        <section class="section">
          <div class="box-title">
            <h6 class="h6">표제부</h6>
          </div>
          <v-table :class="{ 'table-type-03-copydoc': isReceivedDoc, 'table-type-03': !isReceivedDoc }">
            <colgroup>
              <col width="120">
              <col width="">
              <col width="120">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th><span class="required">제목</span></th>
                <td>
                  <v-text-field
                    v-if="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true"
                    v-model="updateCardData.docttl"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"
                  ></v-text-field>
                  <v-text-field v-else
                    v-model="updateCardData.docttl"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
                <th>긴급도</th>
                <td>
                  <v-select
                    v-model="updateCardData.emergency"
                    :items="emergencyData"
                    item-title="emergencyDataKey"
                    item-value="emergencyDataValue"
                    variant="outlined"
                    hide-details="auto"
                    :readonly="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true"></v-select>
                </td>
              </tr>
              <tr>
                <th>
                  관련근거
                  <v-btn size="xsmall" class="magnify" @click.stop="dctReferencePopupToggleFunc" :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != ''" v-if="isReceivedDoc == false">
                    <v-icon></v-icon>
                  </v-btn>
                </th>
                <td>
                  <div v-for="refData, idx in updateCardData.refList" :key="idx">
                    <div class="box-flex">
                      <span>{{ refData.gubunTemp }}</span>
                      <v-text-field
                        v-model="refData.infotitle"
                        :readonly="true"
                        variant="outlined"
                        hide-details="auto">
                      </v-text-field>
                      <v-btn @click="deleteRef(idx)" class="del-btn" :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != ''" v-if="isReceivedDoc == false"><v-icon>mdi-close</v-icon></v-btn>
                    </div>
                    <div v-if="refData.sfilename != undefined && refData.sfilename != ''">
                      <ul class="attach-list ml-16">
                        <li>
                          <span class="branch-down"></span>
                          <v-icon :color="getFileIcon(getExtension(refData.sfilename)).color">{{ getFileIcon(getExtension(refData.sfilename)).icon }}</v-icon>
                          <span @click="toggleViewerPopup(refData, getUserLoginData.userid)" style="font-weight: 600; cursor: pointer;">{{ refData.sfilename }}</span>
                          <v-btn :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != ''" v-if="isReceivedDoc == false" @click="downloadFile(refData)" class="del-btn"><v-icon color="teal-lighten-1">mdi-download</v-icon></v-btn>
                        </li>
                      </ul>
                    </div>
                    <div v-else-if="refData.file != undefined && refData.file != ''">
                      <ul class="attach-list ml-16">
                        <li>
                          <span class="branch-down"></span>
                          <v-icon :color="getFileIcon(getExtension(refData.file.name)).color">{{ getFileIcon(getExtension(refData.file.name)).icon }}</v-icon>
                          <span @click="toggleViewerPopup(refData, getUserLoginData.userid)" style="font-weight: 600; cursor: pointer;">{{ refData.file.name }}</span>
                        </li>
                      </ul>
                    </div>
                  </div>
                </td>
                <th>검색어</th>
                <td>
                  <v-text-field
                    v-if="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true"
                    v-model="updateCardData.keyword"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"
                  ></v-text-field>
                  <v-text-field v-else
                    v-model="updateCardData.keyword"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr v-if="!isReceivedDoc">
                <th>문서취지</th>
                <td colspan="3">
                  <span v-if="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true">{{ updateCardData.summary }}</span>
                  <v-text-field v-else
                    v-model="updateCardData.summary"
                    variant="outlined"
                    hide-details="auto"
                  ></v-text-field>
                </td>
              </tr>
              <tr>
                <th>기안문</th>
                <td colspan="3">
                  <div class="box-flex" v-if="updateCardData.draftdocttl">
                    <v-icon :color="getFileIcon(getExtension(updateCardData.draftdocttl)).color">{{ getFileIcon(getExtension(updateCardData.draftdocttl)).icon }}</v-icon>
                    <span @click="writeDraftDoc()" :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true" style="font-weight: 600; cursor: pointer;">{{ updateCardData.draftdocttl }}</span>
                      <v-btn @click="deleteDraftDoc()" :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != ''" v-if="isReceivedDoc == false" class="del-btn"><v-icon>mdi-close</v-icon></v-btn>
                  </div>
                </td>
              </tr>
              <tr>
                <th>
                  <!-- <div class="box-flex"> -->
                    붙임
                    <!-- <v-btn size="xsmall" class="plus" @click="addAttach()" :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != ''">
                      <v-icon></v-icon>
                    </v-btn> -->
                  <!-- </div> -->
                  <!-- <v-btn min-width="100px" v-if="updateCardData.fileList.length >0" @click="downloadFileAll(updateCardData.fileList)">일괄다운</v-btn> -->
                </th>
                <td colspan="3">
                  <div>
                  <FilePond
                    v-if="!(me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true)"
                    ref="pondRef"
                    label-idle="Drop files here or Browse"
                    allow-multiple="true"
                    v-on:addfile="handleFileAdd"
                    v-on:removefile="handleFileRemove"
                    v-on:activatefile="clickFile"
                  />
                </div>
                  <div v-if="updateCardData.fileList != []">
                    <div class="box-flex" v-for="(fileData, idx) in updateCardData.fileList" :key="idx">
                      <v-icon :color="getFileIcon(getExtension(fileData.sfilename)).color">{{ getFileIcon(getExtension(fileData.sfilename)).icon }}</v-icon>
                      <span @click="toggleViewerPopup(fileData, getUserLoginData.userid)" style="font-weight: 600; cursor: pointer;">{{ fileData.sfilename }}</span>
                      <v-btn @click="delExistingAttach(idx)" class="del-btn" :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != ''" v-if="isReceivedDoc == false"><v-icon>mdi-close</v-icon></v-btn>
                      <v-btn :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != ''" v-if="isReceivedDoc == false" @click="downloadFile(fileData)" class="del-btn"><v-icon color="teal-lighten-1">mdi-download</v-icon></v-btn>
                      <!-- <v-text-field
                        v-model="fileData.sfilename"
                        :readonly="true"
                        variant="outlined"
                        hide-details="auto"
                        :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != ''">
                      </v-text-field> -->
                    </div>
                  </div>
                  <!-- <div class="box-flex" v-for="(attach, idx) in attachArr" :key="idx">
                    <v-file-input
                      :id="`fileUpload-${idx}`"
                      @change="(e) => addFiles(e, idx)"
                      variant="outlined"
                      hide-details="auto"
                      chips="true"
                      show-size
                      clear-icon="false"
                      :v-model="attachFileData[idx]"
                    ></v-file-input>
                    <v-btn @click="delAttach(attach.id)" class="del-btn" :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != ''"><v-icon>mdi-close</v-icon></v-btn>
                    <v-btn @click="downloadFileById(attach.id)" class="del-btn"><v-icon color="teal-lighten-1">mdi-download</v-icon></v-btn>
                  </div> -->
                </td>
              </tr>
            </tbody>
          </v-table>
        </section>

        <!-- 결재선정보 -->
        <section class="section">
          <div class="box-title">
            <h6 class="h6">결재선정보</h6>
            <div class="action-buttons">
              <v-btn class="button" @click="dctPathDirectPopupToggleFunc()" :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true || me.asktype == 'DEA41' || me.asktype == 'DEA51' || me.asktype == 'DEA61' || me.asktype == 'DEA71'">결재선지정</v-btn>
            </div>
          </div>
          <v-table :class="{ 'table-type-04-copydoc': isReceivedDoc, 'table-type-04': !isReceivedDoc }">
            <thead>
              <tr>
                <th>구분</th>
                <th>부서</th>
                <th>직위/성명</th>
                <th>의견/지시</th>
                <th>서명</th>
                <th>처리결과</th>
                <th>처리일시</th>
                <th>이력</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="pathData, idx in updateCardData.pathList" :key="idx">
                <td>
                  {{ pathData.askTemp }}
                </td>
                <td>
                  {{ pathData.deptname }}
                </td>
                <td>
                  {{ pathData.dutynamedesc }}
                </td>
                <td style="width: 800px;" class="text-left">
                  {{ pathData.opinion }}
                </td>
                <td class="signature">
                  {{ pathData.sign }}
                </td>
                <td>
                  {{ pathData.reportresult }}
                </td>
                <td>
                  {{ transformDate(pathData.reportdt) }}
                </td>
                <td>
                  <span v-if="pathData.reportdt" @click="hwpCtrlPopupToggleFunc(pathData)" style="cursor: pointer;">v{{ pathData.mainver }}.0</span>
                </td>
              </tr>
            </tbody>
          </v-table>
        </section>

        <!-- 시행정보 -->
        <div v-if="!isReceivedDoc">
          <section v-if="updateCardData.oridocid == ''" class="section">
            <div class="box-title">
              <h6 class="h6">시행정보</h6>
              <div class="action-buttons">
                <v-btn class="button" @click="dctRecvPopupToggleFunc()" :disabled="me.asktype == 'DEA31' || chkbox == true || isReceivedDoc == true">수신지정</v-btn>
              </div>
            </div>
            <v-table :class="{ 'table-type-03-copydoc': isReceivedDoc, 'table-type-03': !isReceivedDoc }">
              <colgroup>
                <col width="120">
                <col width="">
                <col width="120">
                <col width="">
              </colgroup>
              <tbody>
                <tr>
                  <th>발신기관명</th>
                  <td>
                    <v-select
                      class="w100"
                      v-model="updateCardData.sendOrgNameData"
                      :items="sendOrgNameData"
                      @update:modelValue="changeSendName"
                      item-title="key"
                      item-value="value"
                      variant="outlined"
                      hide-details="auto"
                      :readonly="me.asktype == 'DEA31' || isReceivedDoc == true"
                      ></v-select>
                  </td>
                  <th>발신명의</th>
                  <td>
                    <v-text-field
                      class="w100"
                      v-model="updateCardData.sendNameData"
                      variant="outlined"
                      maxlength="100"
                      hide-details="auto"
                      :readonly="me.asktype == 'DEA31' || isReceivedDoc == true"
                      ></v-text-field>
                  </td>
                </tr>
                <tr>
                  <th>수신자</th>
                  <td colspan="3">
                    <v-text-field
                      v-model="formatRecvList"
                      :readonly="true"
                      variant="solo"
                      hide-details="auto"
                    >
                    </v-text-field>
                  </td>
                </tr>
              </tbody>
            </v-table>
          </section>
        </div>
        <div v-else>
            <section class="section">
              <div class="box-title">
                <h6 class="h6">시행정보</h6>
              </div>
              <v-table :class="{ 'table-type-03-copydoc': isReceivedDoc, 'table-type-03': !isReceivedDoc }">
                <colgroup>
                  <col width="120">
                  <col width="">
                  <col width="120">
                  <col width="">
                </colgroup>
                <tbody>
                  <tr>
                    <th>발신기관명</th>
                    <td>
                      <v-text-field
                        v-model="oriRecvData.distdeptname"
                        :readonly="true"
                        variant="solo"
                        hide-details="auto"></v-text-field>
                    </td>
                    <th>발신명의</th>
                    <td>
                      <v-text-field
                        v-model="oriRecvData.distname"
                        :readonly="true"
                        variant="solo"
                        hide-details="auto"></v-text-field>
                    </td>
                  </tr>
                  <tr>
                    <th>생산문서번호</th>
                    <td>
                      <v-text-field
                        v-model="oriAttrData.docno"
                        :readonly="true"
                        variant="solo"
                        hide-details="auto"></v-text-field>
                    </td>
                    <th>접수번호</th>
                    <td>
                      <v-text-field
                        v-model="updateCardData.docno"
                        :readonly="true"
                        variant="solo"
                        hide-details="auto"></v-text-field>
                    </td>
                  </tr>
                  <tr>
                    <th>생산기관경로</th>
                    <td colspan="3">
                      <v-text-field
                        v-model="oriPathData"
                        :readonly="true"
                        variant="solo"
                        hide-details="auto"></v-text-field>
                    </td>
                  </tr>
                  <tr>
                    <th>수신자</th>
                    <td colspan="3">
                      <v-text-field
                        v-model="oriRecvData.recvdata"
                        :readonly="true"
                        variant="solo"
                        hide-details="auto"></v-text-field>
                    </td>
                  </tr>
                  <tr>
                    <th>배포처</th>
                    <td colspan="3">
                      <v-text-field
                        v-model="oriAttrData.docnosplit"
                        :readonly="true"
                        variant="solo"
                        hide-details="auto"></v-text-field>
                    </td>
                  </tr>
                </tbody>
              </v-table>
            </section>
          </div>
        

        <!-- 관리정보 -->
        <section class="section">
          <div class="box-title">
            <h6 class="h6">관리정보</h6>
            <div class="action-buttons">
              <v-btn class="button" @click="dctReadDirectPopupToggleFunc()" :disabled="me.asktype == 'DEA31' || updateCardData.oridocid != '' || isReceivedDoc == true">열람지정</v-btn>
            </div>
          </div>
          <v-table :class="{ 'table-type-03-copydoc': isReceivedDoc, 'table-type-03': !isReceivedDoc }">
            <colgroup>
              <col width="120">
              <col width="">
            </colgroup>
            <tbody>
              <tr>
                <th>문서번호</th>
                <td>
                  <v-text-field
                    v-model="updateCardData.docno"
                    :readonly="true"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
              <tr>
                <th>열람권자</th>
                <td>
                  <v-text-field 
                    :readonly="true"
                    v-model="formatRcvsList"
                    variant="solo"
                    hide-details="auto"></v-text-field>
                </td>
              </tr>
            </tbody>
          </v-table>
        </section>
      </div>

      <HwpCtrl v-if="draftToggle.data == 'Y'" :data="updateCardData" :toggle="draftToggle" :view="draftView" :received="isReceivedDoc" ref="childRef" :key="compKey"/>
    </div>
  </div>

  <!-- 문서 조회 팝업 -->
  <v-dialog v-model="visibleViewerPopup">
    <v-card>
      <div v-if="docloading" class="overlay_02">
        <div class="spinner_02"></div>
        <div class="progress_02"></div>
      </div>
      <v-toolbar>
        <v-toolbar-title>문서 조회</v-toolbar-title>
        <v-btn class="close" icon @click="visibleViewerPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <iframe :src="url" width="1200" height="840" style="padding: 10px;"></iframe>
    </v-card>
  </v-dialog>

  <!-- 검토용인쇄 팝업 -->
  <v-dialog v-model="bVisibleDctReviewPrintPopup" style="width: 800px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>검토용인쇄</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctReviewPrintPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctReviewPrintPopup v-if="bVisibleDctReviewPrintPopup" :args="dctReviewPrintPopupArgs"
        :toggleFunc="dctReviewPrintPopupToggleFunc" :returnFunc="dctReviewPrintPopupReturnFunc"></DctReviewPrintPopup>
    </v-card>
  </v-dialog>

  <!-- 문서처리 팝업 -->
  <v-dialog v-if="!updateCardData.saveLoading" v-model="bVisibleDctProcPopup" style="width: 800px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>문서처리</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctProcPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctApprovalProcPopup v-if="bVisibleDctProcPopup" :args="dctProcPopupArgs" :toggleFunc="dctProcPopupToggleFunc"
        :returnFunc="dctProcPopupReturnFunc" :saveFunc="callDraftToggle" :received="isReceivedDoc"></DctApprovalProcPopup>
    </v-card>
  </v-dialog>

  <!-- 결재선지정 팝업 -->
  <v-dialog v-model="bVisibleDctPathDirectProcPopup" style="width: 850px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>결재선지정</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctPathDirectProcPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctPathPopup v-if="bVisibleDctPathDirectProcPopup" :args="dctPathDirectProcPopupArgs"
        :toggleFunc="dctPathDirectPopupToggleFunc" :returnFunc="dctPathDirectPopupReturnFunc"></DctPathPopup>
    </v-card>
  </v-dialog>

  <!-- 수신지정 팝업 -->
  <v-dialog v-model="bVisibleDctRecvPopup" style="width: 650px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>수신처지정</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctRecvPopup = false"
         >
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctRecvPopup v-if="bVisibleDctRecvPopup" :args="dctRecvPopupArgs"
        :toggleFunc="dctRecvPopupToggleFunc" :returnFunc="dctRecvPopupReturnFunc"></DctRecvPopup>
    </v-card>
  </v-dialog>

  <!-- 열람지정 팝업 -->
  <v-dialog v-model="bVisibleDctReadDirectProcPopup" style="width: 1000px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>열람자지정</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctReadDirectProcPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctReaderPopup v-if="bVisibleDctReadDirectProcPopup" :args="dctReadDirectProcPopupArgs"
        :toggleFunc="dctReadDirectPopupToggleFunc" :returnFunc="dctReadDirectPopupReturnFunc"></DctReaderPopup>
    </v-card>
  </v-dialog>

  <!-- 로고/심볼 팝업 -->
  <v-dialog v-model="bVisibleDctLogoSymbolProcPopup" style="width: 1000px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>로고/심볼 지정</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctLogoSymbolProcPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctSymbolPopup v-if="bVisibleDctLogoSymbolProcPopup" :args="dctLogoSymbolProcPopupArgs"
        :toggleFunc="dctLogoSymbolPopupToggleFunc" :returnFunc="dctLogoSymbolPopupReturnFunc"></DctSymbolPopup>
    </v-card>
  </v-dialog>

  <!-- 관련근거 팝업 -->
  <v-dialog v-model="bVisibleDctReferencePopup" style="width: 1000px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>관련근거</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctReferencePopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctReferencePopup v-if="bVisibleDctReferencePopup" :args="dctReferencePopupArgs"
        :toggleFunc="dctReferencePopupToggleFunc" :returnFunc="dctReferencePopupReturnFunc"></DctReferencePopup>
    </v-card>
  </v-dialog>

  <!-- 기안문 버전별 조회 팝업 -->
  <v-dialog v-model="bVisibleHwpCtrlPopup" style="width: 1000px">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>기안문 버전별 조회</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleHwpCtrlPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <HwpCtrlPopup if="bVisibleHwpCtrlPopup" :args="hwpCtrlPopupArgs"
        :toggleFunc="hwpCtrlPopupToggleFunc" :returnFunc="hwpCtrlPopupReturnFunc" :received="isReceivedDoc"></HwpCtrlPopup>
    </v-card>
  </v-dialog>

  <!-- 임시저장 팝업 -->
  <v-dialog v-if="!updateCardData.saveLoading" v-model="bVisibleDctTempPopup" style="width: 650px;">
    <v-card>
      <v-toolbar>
        <v-toolbar-title>알림</v-toolbar-title>
        <v-btn class="close" icon @click="bVisibleDctTempPopup = false">
          <v-icon></v-icon>
        </v-btn>
      </v-toolbar>
      <DctTempPopup
        v-if="bVisibleDctTempPopup"
        :args="dctTempPopupArgs"
        :toggleFunc="dctTempPopupToggleFunc"
        :returnFunc="dctTempPopupReturnFunc"
      ></DctTempPopup>
    </v-card>
  </v-dialog>

  <div v-if="updateCardData.saveLoading" class="overlay">
    <div class="spinner"></div>
  </div>
</template>

<script setup>
import console from "console";
import { useMainStore } from '/src/store/Main';
const mainStore = useMainStore()
const { contentWidth, storeContentHeight } = storeToRefs(mainStore)

import {
  ref,
  onBeforeMount,
  onBeforeUnmount,
  onMounted,
  onUnmounted,
  computed,
  watch,
  watchEffect,
  nextTick,
} from "vue";
import { useRoute, useRouter } from "vue-router";
import { API } from "@/api";
import { getValueByKey, getExtension, viewDocument, getFileIcon, url } from "@/utils/Utils.js";
import {
  setQueries,
  setCondiChanged,
  setCondiClear,
  changeCondi,
  checkCondiWithoutPageNum,
} from "@/utils/Query.js";
import _ from "lodash";
import dayjs from 'dayjs';
import DctReviewPrintPopup from "./DctReviewPrintPopup.vue";
import DctApprovalProcPopup from "./DctApprovalProcPopup.vue";
import DctPathPopup from "./DctPathPopup.vue";
import DctRecvPopup from "./DctRecvPopup.vue";
import DctReaderPopup from "./DctReaderPopup.vue";
import DctSymbolPopup from "./DctSymbolPopup.vue";
import DctReferencePopup from "./DctReferencePopup.vue";
import HwpCtrlPopup from "./HwpCtrlPopup.vue";
import DctTempPopup from "./DctTempPopup.vue";
import { storeToRefs } from 'pinia';
import { useLoginStore } from '/src/store/Login';
import { createFile, makeUrlForBlob, SaveToDisk } from "@/utils/File.js";
import { transformStatus, transformState, transformAsktype, transformDate } from "@/utils/TransFormLabelDataUtil.js"

// 기안문 관련
import HwpCtrl from "./HwpCtrl.vue";
const childRef = ref(null);

const draftToggle = ref({ data: "N" });
const draftView = ref({ data: "N" });
const writeDraftDoc = ref(() => {
  if (updateCardData.value.drafturl != "") {
    if (draftToggle.value.data == 'Y') {
      draftToggle.value.data = "N";
    } else {
      draftToggle.value.data = "Y";
    }
  } else {
    alert("조회 할 기안문이 없습니다.");
  }
});
const rewriteFlag = ref({data: "N"});
const rewriteDraftDoc = ref(() => {
  if (draftToggle.value.data == 'Y') {
    draftToggle.value.data = "N";
  } else {
    draftToggle.value.data = "Y";
  }
});
const deleteDraftDoc = ref(() => {
  updateCardData.value.drafturl = "";
  updateCardData.value.draftdocttl = "";
  rewriteFlag.value.data = "Y";
})

const callDraftToggle = (data, apprSave) => {
  if (childRef.value) {
    childRef.value.draftToggle(data, apprSave);
  }
}

const name = ref("BmsDctSendWaitCard"); 
const params = ref({});
const query = ref({});

// 검토용인쇄 Popup 관련
const bVisibleDctReviewPrintPopup = ref(false);
const dctReviewPrintPopupReturn = ref({});
const dctReviewPrintPopupArgs = ref({});
const dctReviewPrintPopupToggleFunc = ref(() => {
  updateCardData.value.docid = updateCardData.value.resdocid;
  dctReviewPrintPopupArgs.value = updateCardData.value;
  bVisibleDctReviewPrintPopup.value = !bVisibleDctReviewPrintPopup.value;
});
const dctReviewPrintPopupReturnFunc = ref((retValue) => {
  dctReviewPrintPopupToggleFunc.value();
  dctReviewPrintPopupReturn.value = retValue;
});

// 문서처리 Popup 관련
const bVisibleDctProcPopup = ref(false);
const dctProcPopupReuturn = ref({});
const dctProcPopupArgs = ref({});
const dctProcPopupToggleFunc = ref(() => {
  // alert('문서처리 Popup');

  // 저장로딩 true
  updateCardData.value.saveLoading = true;
  
  if (draftToggle.value.data == 'N') {
    // for (let i = 0; i < updateCardData.value.pathList.length; i++) {
    //   if (updateCardData.value.pathList[i].objid == getUserLoginData.value.userid) {
    //     updateCardData.value.pathList[i].mainver = i == 0 ? 1 : updateCardData.value.pathList[i - 1].mainver;
    //     updateCardData.value.mainver = updateCardData.value.pathList[i].mainver;
    //   }
    // }

    draftToggle.value.data = 'Y';
  } else {
    callDraftToggle('Y');
  }

  // if(draftToggle.value.data == 'Y') {
  //   childRef.value.saveFile(updateCardData.value.docttl, "Y");
  // } else {
  //   for (let i = 0; i < updateCardData.value.pathList.length; i++) {
  //     if (updateCardData.value.pathList[i].objid == getUserLoginData.value.userid) {
  //       updateCardData.value.pathList[i].mainver = i == 0 ? 1 : updateCardData.value.pathList[i - 1].mainver;
  //       updateCardData.value.mainver = updateCardData.value.pathList[i].mainver;
  //     }
  //   }
  // }

  if (makeAlert() == false) {
    return;
  }

  dctProcPopupArgs.value = updateCardData.value;
  // dctProcPopupArgs.value.files = attachFileData.value;
  dctProcPopupArgs.value.files = newAttachFileArray.value;
  bVisibleDctProcPopup.value = !bVisibleDctProcPopup.value;
});
const dctProcPopupReturnFunc = ref((retValue) => {
  dctProcPopupToggleFunc.value();
  dctProcPopupReuturn.value = retValue;
});

// 결재선지정 Popup 관련
const bVisibleDctPathDirectProcPopup = ref(false);
const dctPathDirectPopupReturn = ref({});
const dctPathDirectProcPopupArgs = ref({});
const dctPathDirectPopupToggleFunc = ref(() => {
  const pathList = [...updateCardData.value.pathList];
  //이미 결재된거 + 본인 안넘겨주도록
  let pathList2 = [];
  for(let i=0; i<pathList.length; i++) {
    if(pathList[i].sign == ""){
      pathList2.push(pathList[i]);
    }
  }

  const modifiedPathList = pathList2.slice(1).map(item => {
    return {
      orgid: item.deptid,
      userid: item.objid,
      username: item.objname,
      orgname: item.deptname,
      grade: item.dutyname,
      askTemp: item.askTemp,
      asktype: item.asktype,
      identifier: `${item.deptid}-${item.objid}`
    };
  });
  dctPathDirectProcPopupArgs.value.path = modifiedPathList;
  dctPathDirectProcPopupArgs.value.myid = getUserLoginData.value.userid;
  dctPathDirectProcPopupArgs.value.me = me.value;
  bVisibleDctPathDirectProcPopup.value = !bVisibleDctPathDirectProcPopup.value;
});
const dctPathDirectPopupReturnFunc = ref((retValue) => {
  dctPathDirectPopupToggleFunc.value();
  if (retValue != "") {
    // insertInitialPath();
    
    // 서명이 없는 항목 삭제 (본인 제외)
    let count = 0;
    for (let j = updateCardData.value.pathList.length - 1; j > 0; j--) {
      if (updateCardData.value.pathList[j].sign == "") {
        if(updateCardData.value.pathList[j].objid != me.value.objid) {
          updateCardData.value.pathList.splice(j, 1);
        }
      } else {
        count++;
      }
    }

    // 팝업 리턴값 세팅
    let pathList = [];
    for (let i = 0; i < retValue.length; i++) {
      const path = {
        askTemp: "", // asktype에 해당하는 viewing 값 임시 저장
        asktype: "", // 요청유형 (기안: DEA11, 검토: DEA21, 협조: DEA31, 결재: DEA41, 재검토: DEA43, 1인결재: DEA45, 전결: DEA51, 대결: DEA61, 전대결: DEA71)
        acttype: "", // 처리유형 (결재선지정 한 경우 - 결재: DEA41, 1인결재: DEA45)
        objid: "", // 객체ID
        objname: "", // 객체이름
        deptid: "", // 부서ID
        deptname: "", // 부서이름
        dutyname: "", // 직위직급이름
        dutynamedesc: "", // 직위직급이름 상세
        electsignid: "", // 전자서명ID
        reportdt: "", // 보고일자
        mainver: "", // 메인버전
        pathorder: "", //  경로순서
        pathstate: "", // 상황 (실제로는 state 컬럼) (처리대기: DCST1, 미개봉: DCST2, 개봉: DCST3, 처리중: DCST4, 처리완료: DCST5)
        sign: "",
      };
      path.askTemp = retValue[i].askTemp;
      path.asktype = retValue[i].asktype;
      path.objid = retValue[i].userid;
      path.objname = retValue[i].username;
      path.deptid = retValue[i].orgid;
      path.deptname = retValue[i].orgname;
      path.dutyname = retValue[i].grade;
      path.dutynamedesc = retValue[i].grade + "/" + retValue[i].username;
      path.pathorder = i + 2;
      path.pathstate = 'DCST1';
      pathList.push(path);
    }

    // push
    for (let k = 0; k < retValue.length; k++) {
      updateCardData.value.pathList.push(pathList[k]);
    }

    const requests = []; // 기안
    const reviewers = []; // 검토, 협조
    const approvers = []; // 전결, 결재

    // '기안','검토', ... , '결재'에 따라 재정렬
    updateCardData.value.pathList.forEach((path) => {
      switch (path.askTemp) {
        case '기안':
          requests.push(path);
          break;
        case '검토':
        case '협조':
          reviewers.push(path);
          break;
        case '전대결':
        case '대결':
        case '전결':
        case '결재':
          approvers.push(path);
          break;
        default:
          break;
      }
    });

    updateCardData.value.pathList = requests.concat(reviewers, approvers);
    updatePathOrder();
  }
  callDraftToggle("Y");
});

const updatePathOrder = () => {
  updateCardData.value.pathList.forEach((path, index) => {
    path.pathorder = index + 1;
  });
};

/* ======================== 수신지정 팝업 ======================== */
const bVisibleDctRecvPopup = ref(false)
const dctRecvPopupReuturn = ref({})
const dctRecvPopupArgs = ref({})
const dctRecvPopupToggleFunc = ref(() => { 
  if(chkbox.value == false) {
    updateCardData.value.gubun = 'DOPT2'
  } else {
    updateCardData.value.gubun = 'DOPT1'
  }

  const recvList = [...updateCardData.value.recvList];
  const modifiedRecvList = recvList.map(item => {
    const gubun = item.apprrecvgubun === "RDR08" ? '원내' : (item.apprrecvgubun === "RDR02" ? '외부' : '대내');
    return {
      receiverid: item.receiverid,
      apprrecvtempflag: item.apprrecvtempflag,
      receiverSymbol: item.recsymbol,
      apprrecvgubun: item.apprrecvgubun,
      gubun,
      apprrecvdeptname: item.apprrecvdeptname,
      apprrecvrefdeptname: item.apprrecvrefdeptname,
    };
  });
  dctRecvPopupArgs.value = modifiedRecvList;
  bVisibleDctRecvPopup.value = !bVisibleDctRecvPopup.value;
})
const dctRecvPopupReturnFunc = ref((retValue) => {
  dctRecvPopupToggleFunc.value()
  if(retValue != ""){
    updateCardData.value.recvList = [];
    for(let i = 0; i < retValue.length; i++){
      const receiver = {};
      receiver.receiverid = retValue[i].receiverid;
      receiver.apprrecvtempflag = retValue[i].apprrecvtempflag;
      receiver.apprrecvseq = i+1;
      receiver.apprrecvgubun = retValue[i].apprrecvgubun;
      receiver.apprrecvdeptname = retValue[i].apprrecvdeptname;
      receiver.apprrecvrefdeptname = retValue[i].apprrecvrefdeptname;
      receiver.recsymbol = retValue[i].receiverSymbol;
      updateCardData.value.recvList.push(receiver);
    }
  }else{
    updateCardData.value.recvList = [];
  }
  callDraftToggle("Y");
});


const sendOrgNameData = ref([
  { key: '원자력통제기술원', value: '원자력통제기술원' },
]);

const updateSendOrgName = () => {
  let obj = {};
  obj.key = getUserLoginData.value.deptname;
  obj.value = getUserLoginData.value.deptname;
  sendOrgNameData.value.push(obj);
}

const changeSendName = () => {
  if (updateCardData.value.sendOrgNameData == '원자력통제기술원') {
    updateCardData.value.sendNameData = updateCardData.value.sendOrgNameData + '장'
  } else {
    updateCardData.value.sendNameData = updateCardData.value.sendOrgNameData + '부서장'
  }
}
/* ============================================================ */

// 열람지정 Popup 관련
const reader = ref("");
const bVisibleDctReadDirectProcPopup = ref(false);
const dctReadDirectPopupReturn = ref({});
const dctReadDirectProcPopupArgs = ref({});
const dctReadDirectPopupToggleFunc = ref(() => {
  const rcvsList = [...updateCardData.value.rcvsList];
  const modifiedRcvsList = rcvsList.slice(1).map(item => {
    return {
      orgid: item.rcvdeptid,
      userid: item.rcvsuserid,
      username: item.rcvsusername,
      orgname: item.rcvdeptname,
      grade: item.rcvdutyname,
      identifier: `${item.rcvdeptid}-${item.rcvsuserid}`
    };
  });
  dctReadDirectProcPopupArgs.value.rcvs = modifiedRcvsList;
  dctReadDirectProcPopupArgs.value.myid = getUserLoginData.value.userid;
  bVisibleDctReadDirectProcPopup.value = !bVisibleDctReadDirectProcPopup.value;
  reader.value = "";
});
const dctReadDirectPopupReturnFunc = ref((retValue) => {
  dctReadDirectPopupToggleFunc.value();
  if (retValue != "") {
    rcvsMe();
    for (let i = 0; i < retValue.length; i++) {
      const rcvs = {};
      rcvs.rcvsuserid = retValue[i].userid;
      rcvs.rcvsusername = retValue[i].username;
      rcvs.rcvsinseq = i + 1;
      rcvs.rcvdeptid = retValue[i].orgid;
      rcvs.rcvdeptname = retValue[i].orgname;
      rcvs.rcvdutyname = retValue[i].grade;
      updateCardData.value.rcvsList.push(rcvs);
    }
  } else {
    rcvsMe();
  }
});

const rcvsMe = () => {
  const initialRcvs = updateCardData.value.rcvsList[0];
  updateCardData.value.rcvsList = [];
  updateCardData.value.rcvsList.push(initialRcvs);
}

// 로고/심볼 Popup 관련
const bVisibleDctLogoSymbolProcPopup = ref(false);
const dctLogoSymbolPopupReturn = ref({});
const dctLogoSymbolProcPopupArgs = ref({});
const dctLogoSymbolPopupToggleFunc = ref(() => {
  bVisibleDctLogoSymbolProcPopup.value = !bVisibleDctLogoSymbolProcPopup.value;
});
const dctLogoSymbolPopupReturnFunc = ref((retValue) => {
  dctLogoSymbolPopupToggleFunc.value();
  dctLogoSymbolPopupReturn.value = retValue;

  updateCardData.value.logourl = dctLogoSymbolPopupReturn.value.logourl;
  updateCardData.value.logopath = dctLogoSymbolPopupReturn.value.logopath;
  updateCardData.value.symbolurl = dctLogoSymbolPopupReturn.value.symbolurl;
  updateCardData.value.symbolpath = dctLogoSymbolPopupReturn.value.symbolpath;

  callDraftToggle("Y");
});

/* ======================== 관련근거 팝업 ======================== */
const bVisibleDctReferencePopup = ref(false)
const dctReferencePopupReuturn = ref({})
const dctReferencePopupArgs = ref({})
const dctReferencePopupToggleFunc = ref(() => {
  bVisibleDctReferencePopup.value = !bVisibleDctReferencePopup.value;
  dctReferencePopupArgs.value = updateCardData.value.refList;
})
const dctReferencePopupReturnFunc = ref((retValue) => {
  if(retValue.delRef.length > 0) {
    updateCardData.value.delRef = retValue.delRef;
  }

  if (retValue.ref.length == 0) {
    updateCardData.value.refList = [];
  }

  dctReferencePopupToggleFunc.value()
  if (retValue.ref != "") {
    if (updateCardData.value.refList[0] != undefined) {
      // if (updateCardData.value.refList[0].infotype == "") {
        updateCardData.value.refList = [];
      // }
    }
    for (let idx = 0; idx < retValue.ref.length; idx++) {
      const exist = {};
      if (retValue.ref[idx].hasOwnProperty('infomationid')) {
        exist.flepath = retValue.ref[idx].flepath;
        exist.gubunTemp = retValue.ref[idx].gubunTemp;
        exist.infomationid = retValue.ref[idx].infomationid;
        exist.infotext = retValue.ref[idx].infotext;
        exist.infotitle = retValue.ref[idx].infotitle;
        exist.infotype = retValue.ref[idx].infotype;
        exist.resultid = retValue.ref[idx].resultid;
        exist.sfilename = retValue.ref[idx].sfilename;
        updateCardData.value.refList.push(exist);
      } else {
        const self = {};
        const choice = {};
        if (retValue.ref[idx].gubunTemp == "직접입력") {
          self.gubunTemp = retValue.ref[idx].gubunTemp,
          self.infotext = retValue.ref[idx].docttl,
          self.infotitle = retValue.ref[idx].docttl,
          self.infotype = "P",
          self.fileexist = retValue.ref[idx].file != '' ? 'Y' : 'N',
          self.file = retValue.ref[idx].file,
          updateCardData.value.refList.push(self);
        } else {
          choice.gubunTemp = retValue.ref[idx].gubunTemp,
          choice.infotext = retValue.ref[idx].docid,
          choice.infotitle = retValue.ref[idx].docttl,
          choice.infotype = "T",
          choice.fileexist = 'N',
          updateCardData.value.refList.push(choice);
        }
      }
    }
  }
})

const deleteRef = (idx) => {
  const infomationid = updateCardData.value.refList[idx].infomationid;
  updateCardData.value.delRef.push(infomationid);
  updateCardData.value.refList.splice(idx, 1);
};

// 관련근거 첨부 다운로드
const downloadRefFile = async(idx) => {
  const file = updateCardData.value.refList[idx].file;
  if (file) {
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(file);
    link.download = file.name;
    link.click();
  } else {
    alert('File content not found.');
  }
};
/* ============================================================ */

const route = useRoute();
const router = useRouter();
const urlPaths = ref("");
const selectDctPathDefCondi = {
  docid: "",
  sortDirection: "ASC",
  sortItem: "docid",
};
const selectDctPathCondi = ref({ ...selectDctPathDefCondi });
const selectDctPathCondiCheck = ref({
  value: { ...selectDctPathDefCondi },
  flag: { ...selectDctPathDefCondi },
});
const getDctRecvDefCondi = {
  docid: "",
  sortDirection: "ASC",
  sortItem: "docid",
};
const getDctRecvCondi = ref({ ...getDctRecvDefCondi });
const getDctRecvCondiCheck = ref({
  value: { ...getDctRecvDefCondi },
  flag: { ...getDctRecvDefCondi },
});


// Move Function
const moveToBmsDctwaitlist = () => {
  router.push({
    name: "BmsDctwaitlist",
  });
};


const updateCardData = ref({

  // 1) bms_dct_rdoc table
  docttl: "", // 비밀명
  gubun: "", // 구분 (내부결재용: DOPT1, 시행용사본: DOPT2)
  state: "", // 상황 (작성중: DRDS1, 처리중: DRDS2, 종료: DRDS3, 발송: DRDS4, 발송안함: DRDS5, 반송: DRDS6)
  open: "", // 공개 (공개: OPEN, 부분공개: PART, 비공개: CLOSE)
  readrangetype: "", // 열람범위유형 (기관: DDEP1, 실국: DDEP2, 부서: DDEP3)
  emergency: "", // 긴급도 (대내: N, 대외: Y, 대내외: T)
  keyword: "", // 키워드
  authorid: "",
  authorname: "", // 담당자명
  authordutyname: "", // 담당자 직위직급
  authordeptname: "", // 담당자 부서이름
  tempflag: "", // 임시 flag (임시: Y, 처리요청: N)
  oridocid: "", // 원본 docid

  // 2) bms_dct_summary table
  docgubun: "A", // 비밀구분 (발송: A, 시행: E)
  summary: "", // 요약

  // 3) bms_dct_attr table
  secgubun: "", // 비밀구분 (일반행정: 00, 군사: 01, 군사훈련: 02, 전시: 03, 한미연합: 04, 한미연합연습: 05, 전시(행정기관): 06)
  seclevel: "", // 비밀등급 (2급: 2, 3급: 3, 대외비: 4, 일반: 5)
  prsrvterm: "", // 보존기간 (1년: 001, 3년: 003, 5년: 005, 10년: 010, 30년: 030, 준영구: 040, 영구: 050)
  orgnprotdt: "", // 원본보호기간
  orgnnextlevel: "", // 원본차기비밀등급 (2급: 2, 3급: 3, 대외비: 4, 일반: 5)
  orgnreclass: "", // 원본재분류 (재분류: 21)
  copyprotdt: "", // 사본보호기간
  copynextlevel: "", // 사본차기비밀등급 (2급: 2, 3급: 3, 대외비: 4)
  copyreclass: "", // 사본재분류 (재분류: 21. 파기: 23, 존안: 20)
  docno: "", // 문서번호
  mgmtno: "", // 관리번호
  copyno: "", // 사본번호

  // 4) bms_dct_path table
  pathList: [
    {
      askTemp: "", // asktype에 해당하는 viewing 값 임시 저장
      asktype: "", // 요청유형 (기안: DEA11, 검토: DEA21, 협조: DEA31, 결재: DEA41, 재검토: DEA43, 1인결재: DEA45, 전결: DEA51, 대결: DEA61, 전대결: DEA71)
      acttype: "", // 처리유형 (결재선지정 한 경우 - 결재: DEA41, 1인결재: DEA45)
      objid: "", // 객체ID
      objname: "", // 객체이름
      deptid: "", // 부서ID
      deptname: "", // 부서이름
      dutyname: "", // 직위직급이름
      dutynamedesc: "", // 직위직급이름 상세
      electsignid: "", // 전자서명ID
      reportdt: "", // 보고일자
      mainver: "", // 메인버전
      pathorder: "", //  경로순서
      pathstate: "", // 상황 (실제로는 state 컬럼) (처리대기: DCST1, 미개봉: DCST2, 개봉: DCST3, 처리중: DCST4, 처리완료: DCST5)
      sign: "",
    }
  ],

  // 5) bms_dct_opinion table (문서처리시에만. 임시저장에는X)
  opinionpathorder: "", //  경로순서 (실제로는 pathorder 컬럼)
  opinion: "",  // 의견

  // 6) bms_dct_appr_recv table
  recvList: [
    {
      receiverid: "",    // 수신처ID
      apprrecvtempflag: "", // 임시FLAG (실제로는 tempflag 컬럼)
      apprrecvseq: "", // 일련번호 (실제로는 seq 컬럼)
      apprrecvdeptname: "",  // 부서이름 (실제로는 deptname 컬럼)
      apprrecvrefdeptname: "",  // 참조부서이름 (실제로는 refdeptname 컬럼)
      recsymbol: "", // 외부 수신처 기호
    }
  ],

  // 7) bms_dct_rdoc_rcvs table
  rcvsList: [
    {
      rcvsuserid: "",    // 사용자ID (실제로는 userid 컬럼)
      rcvsusername: "",      // 사용자이름 (실제로는 username 컬럼)
      rcvdeptid: "",      // 부서이름 (실제로는 deptid 컬럼)
      rcvdeptname: "",      // 부서이름 (실제로는 deptname 컬럼)
      rcvdutyname: "",      // 직위직급이름 (실제로는 dutyname 컬럼)
      rcvsinseq: "",      // 등록일련번호 (실제로는 inseq 컬럼)
    }
  ],

  // 8) bms_task_infomation_temp table (문서처리 일때는 bms_task_infomation table)
  refList: [
    {
      infomationid: "", // 정보출처ID
      resultid: "", // 실적ID
      infotext: "", // 정보출처제목 (비밀문서는 docid, 직접입력은 입력받은 근거)
      infotitle: "", // 정보출처제목 (비밀문서는 docttl, 직접입력은 입력받은 근거)
      infotype: "", // 정보출처유형 (비밀문서는 T, 직접입력은 P)
      file: "", // FILE 객체 저장용
      fileexist: "", // 파일여부 체크용
      sfilename: "", // bms_dct_file 에서 조회해온 실제파일명
    }
  ],

  // 9) bms_dct_hist_card table (문서처리시에만. 임시저장에는X)
  apprreqid: "", // 결재요청 ID
  histgubun: "", // 이력구분 (기안: 01, 결재: 02)
  deptid: "", // 소속코드
  deptname: "", // 소속명
  dutyname: "", // 직위직급명
  username: "", // 사용자명
  histstatus: "", // 이력상태 (결재 요청: 010, 결재: 011, 반려: 012)
  etc: "", // 기타
  electsignid: "", // 전자서명 ID

  // 10) bms_dct_file table
  fileList: [
    {
      fleid: "", // 첨부파일 ID
      sfilename: "", // 파일고유이름
      gubun: "", // 구분
      flettl: "", // 첨부명
      flesize: "", // 첨부크기
      fleorder: "", // 첨부순서
      isviewtype: "", // 비전자 첨부구분
      fleurl: "", // 첨부 url
      flepath: "", // 첨부 경로
    }
  ],
  delFileids: [], // 기존 첨부파일 삭제 용도

  delRef: [], // 관련근거 삭제용

  // 공통
  resdocid: "",
  resuserid: "",
  setBtnYn: "N",
  resasktype: "",
  mainver: 1,

  // 기안문 url
  drafturl: "",
  draftdocttl: "",
  draftcontent: "",

  // 로고/심볼 url
  logourl: "",
  logopath: "",
  symbolurl: "",
  symbolpath: "",

  // 시행정보
  sendOrgNameData: "",
  sendNameData: "",

  // 붙임파일 관련
  newFileList: [],
  delFileList: [],

  // 검토용인쇄확인 flag
  printflag: "N",

  // 웹한글기안기 저장 로딩
  saveLoading: false,

  // 오프라인 flag
  offlineflag: "N",

  // 비전자문서 flag
  paperflag: "",
});

const chkbox = ref(false);
const changeChkbox = () => {
  if (chkbox.value == true) {
    updateCardData.value.gubun = 'DOPT1';
    updateCardData.value.recvList = [];
    updateCardData.value.sendOrgNameData = "";
    updateCardData.value.sendNameData = "";
    updateCardData.value.copyreclass = "";
    updateCardData.value.copyprotdt = "";
    copyDate.value.year = "";
    copyDate.value.month = "";
    copyDate.value.day = "";
    copyProtDtPeriodSelected.value = "";
  } else {
    updateCardData.value.gubun = 'DOPT2';
  }
  // chkbox.value = !chkbox.value;
}

// 비밀등급 Select-Box
const secLevelData = [
  { secLevelDataKey: '선택', secLevelDataValue: ''},
  { secLevelDataKey: 'II급', secLevelDataValue: '2'},
  { secLevelDataKey: 'III급', secLevelDataValue: '3'},
  { secLevelDataKey: '일반', secLevelDataValue: '5'},
];

// 보존기간 Select-Box
const prsRvTermData = [
  {prsRvTermDataKey: '선택', prsRvTermDataValue: ''},
  {prsRvTermDataKey: '1년', prsRvTermDataValue: '001'},
  {prsRvTermDataKey: '3년', prsRvTermDataValue: '003'},
  {prsRvTermDataKey: '5년', prsRvTermDataValue: '005'},
  {prsRvTermDataKey: '10년', prsRvTermDataValue: '010'},
  {prsRvTermDataKey: '30년', prsRvTermDataValue: '030'},
  {prsRvTermDataKey: '준영구', prsRvTermDataValue: '040'},
  {prsRvTermDataKey: '영구', prsRvTermDataValue: '050'},
];

// 보존기간 validation check
const updatePrsrvterm = () => {
  if (parseInt(updateCardData.value.prsrvterm) < parseInt(orgnProtDtPeriodSelected.value) + 1) {
    alert('예고문 보존기간은 원본 보호기간보다 작을 수 없습니다.');
    updateCardData.value.prsrvterm = "";
    return;
  }
  if ((parseInt(updateCardData.value.prsrvterm) < parseInt(copyProtDtPeriodSelected.value) + 1) && chkbox.value == false) {
    alert('예고문 보존기간은 사본 보호기간보다 작을 수 없습니다.');
    updateCardData.value.prsrvterm = "";
    return;
  }
}


/* ======================== 원본보호기간 ======================== */
const orgnProtDtSelected = ref("날짜지정");
const orgnProtDtData = ["날짜지정", "직접입력"];

const orgnProtDtPeriodSelected = ref("");
const orgnProtDtPeriodData = [
  {key: '선택', value:''},
  {key: '1년', value:'0'},
  {key: '2년', value:'1'},
  {key: '3년', value:'2'},
  {key: '4년', value:'3'},
  {key: '5년', value:'4'},
  {key: '6년', value:'5'},
  {key: '7년', value:'6'},
  {key: '8년', value:'7'},
  {key: '9년', value:'8'},
  {key: '10년', value:'9'},
];

const orgnDate = ref({
  year: "",
  month: "",
  day: "",
})

// 날짜지정
const getOrgnDate = () => {
  const currentYear = new Date().getFullYear();
  const futureYear = currentYear + parseInt(orgnProtDtPeriodSelected.value);
  const comparisonDate = dayjs(`${futureYear}-12-31`, 'YYYY-MM-DD');

  if(parseInt(updateCardData.value.prsrvterm) < parseInt(orgnProtDtPeriodSelected.value)+1){
    alert('예고문 보존기간은 원본 보호기간보다 작을 수 없습니다.');
    orgnProtDtPeriodSelected.value = "";
    orgnDate.value = {};
    return
  }
  if((parseInt(orgnProtDtPeriodSelected.value) < parseInt(copyProtDtPeriodSelected.value)) && chkbox.value == false){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    orgnProtDtPeriodSelected.value = "";
    orgnDate.value = {};
    return;
  }
  if(comparisonDate.isBefore(dayjs(copyUserDate.value, 'YYYY-MM-DD')) && chkbox.value == false){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    orgnProtDtPeriodSelected.value = "";
    orgnDate.value = {};
    return;
  }
  orgnDate.value.year = futureYear;
  orgnDate.value.month = '12';
  orgnDate.value.day = '31';
  updateCardData.value.orgnprotdt = orgnDate.value.year+orgnDate.value.month+orgnDate.value.day;
}

const updateOrgnDt = () => {
  orgnProtDtPeriodSelected.value = "";
  orgnUserDate.value = "";
  orgnDate.value.year = "";
  orgnDate.value.month = "";
  orgnDate.value.day = "";
}
const orgnUserDate = ref("");

// 직접입력
const getOrgnUserDate = () => {
  const currentYear = new Date().getFullYear();
  const futureYear = currentYear + parseInt(copyProtDtPeriodSelected.value);
  const comparisonDate = dayjs(`${futureYear}-12-31`, 'YYYY-MM-DD');

  if(parseInt(updateCardData.value.prsrvterm) < parseInt(orgnUserDate.value.slice(0,4))+1 - new Date().getFullYear()){
    alert('예고문 보존기간은 원본 보호기간보다 작을 수 없습니다.');
    orgnUserDate.value = "";
    orgnDate.value = {};
    return
  }
  if(dayjs(orgnUserDate.value, 'YYYY-MM-DD').isBefore(dayjs(copyUserDate.value, 'YYYY-MM-DD')) && chkbox.value == false){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    orgnUserDate.value = "";
    orgnDate.value = {};
    return;
  }
  if(dayjs(orgnUserDate.value, 'YYYY-MM-DD').isBefore(comparisonDate) && chkbox.value == false){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    orgnUserDate.value = "";
    orgnDate.value = {};
    return;
  }
  updateCardData.value.orgnprotdt = orgnUserDate.value.replace(/-/g, '');
}

const orgnReclassSelected = ref("선택");
const orgnReclass = ['선택','일반문서'];
const orgnReclassData = [
  { key: '선택', value: ''},
  { key: '재분류', value: '21'},
  { key: '이관', value: '22'}, // 임시 설정
];

const selectedOrgnReClass = (key) => {
  if(key == '21'){
    orgnReclassSelected.value = "일반문서"
  }else{
    orgnReclassSelected.value = "선택"
  }
}

/* ============================================================ */

/* ======================== 사본보호기간 ======================== */
const copyProtDtSelected = ref("날짜지정");
const copyProtDtData = ["날짜지정", "직접입력"];

const copyProtDtPeriodSelected = ref("");
const copyProtDtPeriodData = [
  {key: '선택', value:''},
  {key: '1년', value:'0'},
  {key: '2년', value:'1'},
  {key: '3년', value:'2'},
  {key: '4년', value:'3'},
  {key: '5년', value:'4'},
  {key: '6년', value:'5'},
  {key: '7년', value:'6'},
  {key: '8년', value:'7'},
  {key: '9년', value:'8'},
  {key: '10년', value:'9'},
];

const copyDate = ref({
  year: "",
  month: "",
  day: "",
})

// 날짜지정
const getCopyDate = () => {
  const currentYear = new Date().getFullYear();
  const futureYear = currentYear + parseInt(copyProtDtPeriodSelected.value);
  const comparisonDate = dayjs(`${futureYear}-12-31`, 'YYYY-MM-DD');

  if((parseInt(updateCardData.value.prsrvterm) < parseInt(copyProtDtPeriodSelected.value)+1) && chkbox.value == false){
    alert('예고문 보존기간은 사본 보호기간보다 작을 수 없습니다.');
    copyProtDtPeriodSelected.value = "";
    copyDate.value = {};
    return;
  }
  if((parseInt(orgnProtDtPeriodSelected.value) < parseInt(copyProtDtPeriodSelected.value)) && chkbox.value == false){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    copyProtDtPeriodSelected.value = "";
    copyDate.value = {};
    return;
  }
  if(dayjs(orgnUserDate.value, 'YYYY-MM-DD').isBefore(comparisonDate) && chkbox.value == false){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    copyProtDtPeriodSelected.value = "";
    copyDate.value = {};
    return;
  }
  copyDate.value.year = futureYear;
  copyDate.value.month = '12';
  copyDate.value.day = '31';
  updateCardData.value.copyprotdt = copyDate.value.year+copyDate.value.month+copyDate.value.day;
}

const updateCopyDt = () => {
  copyProtDtPeriodSelected.value = "";
  copyUserDate.value = "";
  copyDate.value.year = "";
  copyDate.value.month = "";
  copyDate.value.day = "";
}
const copyUserDate = ref("");

// 직접입력
const getCopyUserDate = () => {
  const currentYear = new Date().getFullYear();
  const futureYear = currentYear + parseInt(orgnProtDtPeriodSelected.value);
  const comparisonDate = dayjs(`${futureYear}-12-31`, 'YYYY-MM-DD');

  if((parseInt(updateCardData.value.prsrvterm) < parseInt(copyUserDate.value.slice(0,4))+1 - new Date().getFullYear()) && chkbox.value == false){
    alert('예고문 보존기간은 사본 보호기간보다 작을 수 없습니다.');
    copyUserDate.value = "";
    copyDate.value = {};
    return;
  }
  if(dayjs(orgnUserDate.value, 'YYYY-MM-DD').isBefore(dayjs(copyUserDate.value, 'YYYY-MM-DD')) && chkbox.value == false){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    copyUserDate.value = "";
    copyDate.value = {};
    return;
  }
  if(comparisonDate.isBefore(dayjs(copyUserDate.value, 'YYYY-MM-DD')) && chkbox.value == false){
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    copyUserDate.value = "";
    copyDate.value = {};
    return;
  }
  updateCardData.value.copyprotdt = copyUserDate.value.replace(/-/g, '');
}

const copyReclassSelected = ref("선택");
const copyReclass = ['선택','일반문서'];
const copyReclassData = [
  { key: '선택', value: ''},
  { key: '존안', value: '20'},
  { key: '재분류', value: '21'},
  { key: '파기', value: '23'},
];

const selectedCopyReClass = (key) => {
  if(key == '21'){
    copyReclassSelected.value = "일반문서"
  }else{
    copyReclassSelected.value = "선택"
  }
}
/* ============================================================ */


// 표제부 긴급도 Select-Box
const emergencyData = [
  { emergencyDataKey: '일반', emergencyDataValue: 'N' },
  { emergencyDataKey: '지급', emergencyDataValue: 'Z' },
  { emergencyDataKey: '긴급', emergencyDataValue: 'Y' },
  { emergencyDataKey: '초긴급', emergencyDataValue: 'X' },
];

const makeAlert = () => {
  if (!updateCardData.value.seclevel) {
    alert('비밀등급을 선택해주세요.');
    return false;
  }
  if (!updateCardData.value.prsrvterm) {
    alert('예고문 보존기간을 선택해주세요.');
    return false;
  }
  if (!updateCardData.value.orgnprotdt) {
    alert('원본 보호기간을 선택해주세요.');
    return false;
  }
  if (!updateCardData.value.copyprotdt && chkbox.value == false) {
    alert('사본 보호기간을 선택해주세요.');
    return false;
  }
  if (parseInt(updateCardData.value.prsrvterm) < parseInt(orgnProtDtPeriodSelected.value) + 1) {
    alert('예고문 보존기간은 원본 보호기간보다 작을 수 없습니다.');
    return false;
  }
  if ((parseInt(updateCardData.value.prsrvterm) < parseInt(copyProtDtPeriodSelected.value) + 1) && chkbox.value == false) {
    alert('예고문 보존기간은 사본 보호기간보다 작을 수 없습니다.');
    return false;
  }
  if ((parseInt(orgnProtDtPeriodSelected.value) < parseInt(copyProtDtPeriodSelected.value)) && chkbox.value == false) {
    alert('사본 보호기간은 원본 보호기간보다 클 수 없습니다.');
    return false;
  }
  if (orgnProtDtSelected.value != '직접입력' && !updateCardData.value.orgnreclass) {
    alert('원본 보호기간 조건을 선택해주세요.');
    return false;
  }
  if (copyProtDtSelected.value != '직접입력' && !updateCardData.value.copyreclass && chkbox.value == false) {
    alert('사본 보호기간 조건을 선택해주세요.');
    return false;
  }
  if (!updateCardData.value.docttl) {
    alert('표제부 제목을 입력해주세요.');
    return false;
  }
  if (formatRecvList.value == "" && chkbox.value == false) {
    alert('수신지정을 해주세요');
    return false;
  }
  if(rewriteFlag.value.data == "Y") {
    if (!updateCardData.value.draftcontent) {
      alert('본문을 작성해주세요.');
      return false;
    }
  }
};

/* ======================== 첨부파일 ======================== */
const pondRef = ref(null);
const removePond = (id) => {
  pondRef.value.removeFile(id);
};

const clickFile = (event) => {
  toggleViewerPopup(event, getUserLoginData.value.userid);
};

const handleFileAdd = (error, data) => {
  const size = data.fileSize;
  const dupliNameIdx = attachFileData.value.findIndex(attach => attach.file.name === data.filename);
  if(size == 0){
    alert("크기가 0인 파일은 등록이 불가능합니다.");
    removePond(data.id);
  }else if(size >= 2000000000){
    alert("붙임 파일은 2GB까지 등록 가능합니다.");
    removePond(data.id);
  }else if(dupliNameIdx != -1){
    alert("이미 등록된 파일입니다.");
    removePond(data.id);
  }else{
    attachFileData.value.push({file: data.file, id: data.id});
    updateCardData.value.newFileList.push({ file: data.file, id: data.id });
  }
};

const handleFileRemove = (error, data) => {
  const index = attachFileData.value.findIndex(attach => attach.id === data.id);
  if (index !== -1) {
    attachFileData.value.splice(index, 1);
    updateCardData.value.newFileList.splice(index, 1);
    updateCardData.value.delFileList.push(index);
  }
};

const newAttachFileArray = computed(() => {
  return attachFileData.value.map(item => item.file);
});

// const attachArr = ref([]);
const attachFileData = ref([]);
// const attachIdx = ref(0);

// const addAttach = () => {
//   attachArr.value.push({id: `fileUpload-${attachIdx.value}`});
//   attachIdx.value++;
//   openFileUpload();
// }

// const openFileUpload = () => {
//   nextTick(() => {
//     let fileUpload = document.getElementById(`fileUpload-${attachIdx.value - 1}`);
//     if (fileUpload != null) {
//       fileUpload.click();
//     }
//   })
// };

// const delAttach = (id) => {
//   const index = attachArr.value.findIndex(attach => attach.id === id);
//   if (index !== -1) {
//     attachArr.value.splice(index, 1);
//     attachFileData.value.splice(index, 1);
//     updateCardData.value.newFileList.splice(index, 1);
//     updateCardData.value.delFileList.push(index);
//   }
//   attachIdx.value--;
// }

// const addFiles = (e, idx) => {
//   if (e.target.files[0].size < 2000000000) {
//     const file = e.target.files[0];
//     attachFileData.value[idx] = file;
//     updateCardData.value.newFileList[idx] = file;
//   } else {
//     alert("붙임 파일은 2GB까지 등록 가능합니다.");
//     attachArr.value.splice(idx, 1);
//     attachIdx.value--;
//   }
// };

const delExistingAttach = (idx) => {
  const fleid = updateCardData.value.fileList[idx].fleid;
  updateCardData.value.fileList.splice(idx, 1);
  updateCardData.value.delFileids.push(fleid);
  updateCardData.value.delFileList.push(idx);
}

const downloadFile = async (file) => {
  try {
    var fileInfo = {
      uploadPath: file.flepath,
      orgFilename: file.sfilename,
    };

    const response = await API.fileAPISbms.fileDownloadApi(fileInfo);
    SaveToDisk(makeUrlForBlob(response.data), fileInfo.orgFilename);
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

// const downloadFileAll = async(files) => {
//   try {
//     for (const file of files) {
//       var fileInfo = {
//         uploadPath: file.flepath,
//         orgFilename: file.sfilename,
//       };

//       const response = await API.fileAPISbms.fileDownloadApi(fileInfo);
//       SaveToDisk(makeUrlForBlob(response.data), fileInfo.orgFilename);
//     }
//   } catch (error) {
//     console.error(error);
//     alert("Server Error");
//   }
// };

// const downloadFileById = async (id) => {
//   const index = attachArr.value.findIndex(attach => attach.id === id);
//   if (index !== -1) {
//     const file = attachFileData.value[index];
//     if (file) {
//       const link = document.createElement('a');
//       link.href = window.URL.createObjectURL(file);
//       link.download = file.name;
//       link.click();
//     } else {
//       alert('File content not found.');
//     }
//   }
// }
/* ============================================================ */

const docidData = ref({ docid: "" });

const loginStore = useLoginStore()
const { getUserLoginData, getUserLoginCondi } = storeToRefs(loginStore)
const { getUserLogin } = storeToRefs(loginStore)

onBeforeMount(() => {
  params.value = route.params; // to do    // params
  query.value = route.query; // to do    // query
  // getDctRdocCondi.value.docid = query.value.docid;
  if (Array.isArray(route.query.docid) && route.query.docid.length > 0) {
    docidData.value.docid = route.query.docid[0];
  } else {
    docidData.value.docid = route.query.docid;
  }
  updateCardData.value.resdocid = docidData.value.docid;
});


onMounted(async () => {
  window.sessionStorage.setItem('waitFlag', 'N');
  await checkOrder();

  if(checkYN == "Y") {
    updateCardData.value.resuserid = getUserLoginData.value.userid;
    updateCardData.value.deptid = getUserLoginData.value.deptid;
    updateCardData.value.deptname = getUserLoginData.value.deptname;
    updateCardData.value.dutyname = getUserLoginData.value.grade;
    updateCardData.value.username = getUserLoginData.value.username;
    // updateCardData.value.authorid = getUserLoginData.value.userid;
    await getDctRdoc();
    await getDctSummary();
    await getDctAttr();
    await selectDctPath();
    if(updateCardData.value.oridocid == '') {
      await getDctRecv();
    }
    await selectDctReader();
    await selectTaskInfo();
    await selectDctFile();

    if (isReceivedDoc.value == true) {
      recvDocidData.value.docid = updateCardData.value.oridocid;
      if (recvDocidData.value.docid != undefined && recvDocidData.value.docid != null && recvDocidData.value.docid != "") {
        await getDctAttrOridocid();
        await getDctRecvOridocid();
        await selectDctPathOridocid();
      }
    }
  } else {
    alert("이전 결재자가 결재 진행중입니다.");
    moveToBmsDctwaitlist();
  }
  updateSendOrgName();
});

watch(route, async (route) => {
  setQueries(route, selectDctPathCondi.value, selectDctPathDefCondi);
  await selectDctPath(selectDctPathCondi.value.pageNum);
});

watch(
  () => _.cloneDeep(selectDctPathCondi.value),
  (newVal, oldVal) => {
    setCondiChanged(selectDctPathCondiCheck.value, newVal, oldVal);
  }
);

watch(route, async (route) => {
  setQueries(route, getDctRecvCondi.value, getDctRecvDefCondi);
  await getDctRecv(getDctRecvCondi.value.pageNum);
});

watch(
  () => _.cloneDeep(getDctRecvCondi.value),
  (newVal, oldVal) => {
    setCondiChanged(getDctRecvCondiCheck.value, newVal, oldVal);
  }
);

watchEffect(() => { });


// 1) bms_dct_rdoc table
const getDctRdoc = async () => {
  try {
    const response = await API.dctAPI.getDctRdoc({
      ...docidData.value
    },
      urlPaths.value
    );
    const data = response.data;
    updateCardData.value.docttl = data.docttl;
    updateCardData.value.gubun = data.gubun;
    updateCardData.value.state = data.state;
    updateCardData.value.open = data.open;
    updateCardData.value.readrangetype = data.readrangetype;
    if (data.emergency !== "n") {
      updateCardData.value.emergency = data.emergency;
    }
    updateCardData.value.keyword = data.keyword;
    updateCardData.value.authorname = data.authorname;
    updateCardData.value.authordutyname = data.authordutyname;
    updateCardData.value.authordeptname = data.authordeptname;
    updateCardData.value.tempflag = data.tempflag;
    updateCardData.value.authorid = data.authorid;
    updateCardData.value.oridocid = data.oridocid;
    updateCardData.value.indt = data.indt;
    if(updateCardData.value.oridocid == undefined) {
      updateCardData.value.oridocid = "";
    } else { // 접수받은 문서의 경우 내부생성으로 고정
      updateCardData.value.gubun = 'DOPT1';
    }

    if(updateCardData.value.gubun == 'DOPT1') {
      chkbox.value = true;
    } else {
      chkbox.value = false;
    }

    updateCardData.value.indt = data.indt;
    updateCardData.value.reportdt = data.reportdt;
    updateCardData.value.paperflag = data.paperflag;
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// 2) bms_dct_summary table
const getDctSummary = async () => {
  try {
    const response = await API.dctAPI.getDctSummary({
      ...docidData.value
    },
      urlPaths.value
    );
    const data = response.data;
    if (data.summary != undefined) {
      updateCardData.value.docgubun = data.docgubun;
      updateCardData.value.summary = data.summary;
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const isReceivedDoc = ref(false);

// 3) bms_dct_attr table
const getDctAttr = async () => {
  try {
    const response = await API.dctAPI.getDctAttr({
      ...docidData.value
    },
      urlPaths.value
    );
    const data = response.data;
    updateCardData.value.secgubun = data.secgubun;
    updateCardData.value.seclevel = data.seclevel;
    updateCardData.value.prsrvterm = data.prsrvterm;
    updateCardData.value.orgnprotdt = data.orgnprotdt;
    updateCardData.value.orgnnextlevel = data.orgnnextlevel;
    updateCardData.value.orgnreclass = data.orgnreclass;
    updateCardData.value.copyprotdt = data.copyprotdt;
    updateCardData.value.copynextlevel = data.copynextlevel;
    updateCardData.value.copyreclass = data.copyreclass;
    updateCardData.value.docno = data.docno;
    updateCardData.value.mgmtno = data.mgmtno;
    updateCardData.value.copyno = data.copyno;
    isReceivedDoc.value = updateCardData.value.copyno != null && !updateCardData.value.copyno.includes("원") ? true : false;
    if (updateCardData.value.orgnprotdt) {
      sliceDates(updateCardData.value.orgnprotdt, orgnDate.value);
      orgnProtDtPeriodSelected.value = (parseInt(orgnDate.value.year) - parseInt(updateCardData.value.indt.slice(0,4))).toString();
    }
    if (updateCardData.value.copyprotdt) {
      sliceDates(updateCardData.value.copyprotdt, copyDate.value);
      copyProtDtPeriodSelected.value = (parseInt(copyDate.value.year) - parseInt(updateCardData.value.indt.slice(0,4))).toString();
    }

  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

const sliceDates = (dateString, target) => {
  const year = parseInt(dateString.slice(0, 4));
  const month = parseInt(dateString.slice(4, 6));
  const day = parseInt(dateString.slice(6, 8));
  target.year = year;
  target.month = month;
  target.day = day;
}

// 4) bms_dct_path table
const selectDctPath = async () => {
  try {
    const response = await API.dctAPI.selectDctPath({
      ...docidData.value
    },
      urlPaths.value
    );
    updateCardData.value.pathList = [];
    for (let i = 0; i < response.data.length; i++) {
      const upd = {};
      upd.askTemp = transformAsktype(response.data[i].asktype);
      upd.asktype = response.data[i].asktype;
      upd.deptid = response.data[i].deptid;
      upd.deptname = response.data[i].deptname;
      upd.dutyname = response.data[i].dutyname;
      upd.dutynamedesc = response.data[i].dutynamedesc;
      upd.electsignid = response.data[i].electsignid;
      upd.reportdt = response.data[i].reportdt;
      if(i!=0 && response.data[i].mainver == 1) {
        response.data[i].mainver = response.data[i-1].mainver;
      }
      upd.mainver = response.data[i].mainver;
      upd.pathorder = response.data[i].pathorder;
      upd.pathstate = response.data[i].state;
      upd.objid = response.data[i].objid;
      upd.objname = response.data[i].objname;
      upd.sign = response.data[i].reportdt != null ? response.data[i].objname : "";
      upd.opinion = response.data[i].opinion;
      upd.acttype = response.data[i].acttype;

      // 처리결과 
      if (upd.reportdt != "" && upd.reportdt != null && upd.reportdt != undefined) {
        if (upd.asktype == "DEA11") {
          upd.reportresult = "기안";
        } else {
          if (upd.acttype == "DEA43" && (upd.asktype == "DEA41" || upd.asktype == "DEA51" || upd.asktype == "DEA61" || upd.asktype == "DEA71")) {
            upd.reportresult = "반려"
          } else {
            upd.reportresult = "승인";
          }
        }
      } else {
        upd.reportresult = "";
      }

      updateCardData.value.pathList.push(upd);

      if(response.data[i].objid == getUserLoginData.value.userid) {
        updateCardData.value.mainver = response.data[i].mainver;
      }
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// 6) bms_dct_appr_recv
const getDctRecv = async () => {
  try {
    const response = await API.dctAPI.getDctRecv({
      ...docidData.value
    },
      urlPaths.value
    );
    updateCardData.value.recvList = [];
    for (let i = 0; i < response.data.length; i++) {
      const upd = {};
      upd.receiverid = response.data[i].receiverid;
      upd.apprrecvtempflag = response.data[i].tempflag;
      upd.apprrecvseq = response.data[i].seq;
      upd.apprrecvgubun = response.data[i].gubun;
      upd.apprrecvdeptname = response.data[i].deptname;
      upd.apprrecvrefdeptname = response.data[i].refdeptname;
      updateCardData.value.recvList.push(upd);
      if(response.data[0].distdeptname != null){
        updateCardData.value.sendOrgNameData = response.data[0].distdeptname;
        updateCardData.value.sendNameData = response.data[0].distname;
      }
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// 수신자
const formatRecvList = computed(() => {
  if(updateCardData.value.recvList.length == 0 || updateCardData.value.recvList[0].apprrecvdeptname == ""){
    return
  }else{
    const formattedRecvList = updateCardData.value.recvList.map((item) => {
      const refDeptName = item.apprrecvrefdeptname ? `(${item.apprrecvrefdeptname})` : '';
      return `${item.apprrecvdeptname}${refDeptName}`;
    });
    return formattedRecvList.join(', ');
  }
});


// 7) bms_dct_rdoc_rcvs
const selectDctReader = async () => {
  try {
    const response = await API.dctAPI.selectDctReader({
      ...docidData.value
    },
      urlPaths.value
    );
    updateCardData.value.rcvsList = [];
    for (let i = 0; i < response.data.length; i++) {
      const upd = {};
      upd.rcvsuserid = response.data[i].userid;
      upd.rcvsusername = response.data[i].username;
      upd.rcvdeptid = response.data[i].deptid;
      upd.rcvdeptname = response.data[i].deptname;
      upd.rcvdutyname = response.data[i].dutyname;
      upd.rcvsinseq = response.data[i].inseq;
      updateCardData.value.rcvsList.push(upd);
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// 열람권자
const formatRcvsList = computed(() => {
  const formattedRcvsList = updateCardData.value.rcvsList.map((item) => {
    return `${item.rcvsusername}(${item.rcvdutyname}/${item.rcvdeptname})`;
  });
  return formattedRcvsList.join(', ');
});

// 8) bms_task_infomation_temp table
const selectTaskInfo = async () => {
  try {
    const response = await API.dctAPI.selectTaskInfo({
      ...docidData.value
    },
      urlPaths.value
    );
    updateCardData.value.refList = [];
    for (let i = 0; i < response.data.length; i++) {
      const upd = {};
      upd.infomationid = response.data[i].infomationid;
      upd.resultid = response.data[i].resultid;
      upd.infotext = response.data[i].infotext;
      upd.infotitle = response.data[i].infotitle;
      upd.infotype = response.data[i].infotype;
      upd.sfilename = response.data[i].sfilename;
      upd.flepath = response.data[i].flepath;
      if (response.data[i].infotype == 'P') {
        upd.gubunTemp = '직접입력'
      } else if (response.data[i].infotype == 'T') {
        upd.gubunTemp = '비밀문서'
      }
      updateCardData.value.refList.push(upd);
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

// 10) bms_dct_file table
const selectDctFile = async () => {
  try {
    const response = await API.dctAPI.selectDctFile({
      ...docidData.value
    },
      urlPaths.value
    );
    updateCardData.value.fileList = [];
    updateCardData.value.draftfile = [];
    
    // 붙임파일 전체 (이력용)
    updateCardData.value.fileListAll = [];
    for (let i = 0; i < response.data.length; i++) {
      if (response.data[i].gubun == 'DFT02') { // 붙임파일만
        const upd = {};
        upd.fleid = response.data[i].fleid;
        upd.sfilename = response.data[i].sfilename;
        upd.gubun = response.data[i].gubun;
        upd.flettl = response.data[i].flettl;
        upd.flesize = response.data[i].flesize;
        upd.fleorder = response.data[i].fleorder;
        upd.isviewtype = response.data[i].isviewtype;
        upd.fleurl = response.data[i].fleurl;
        upd.flepath = response.data[i].flepath;
        upd.inid = response.data[i].inid;
        upd.indt = response.data[i].indt;
        updateCardData.value.fileListAll.push(upd);
        
        let indtArr = [];
        for (let i = 0; i < response.data.length; i++) {
          if (response.data[i].gubun == 'DFT02') {
            indtArr.push(Number(response.data[i].indt));
          }
        }

        let maxIndt = Math.max(...indtArr);
        if (Number(response.data[i].indt) == maxIndt) {
          updateCardData.value.fileList.push(upd);
        }
      } else {
        let indtArr = [];
        for (let i = 0; i < response.data.length; i++) {
          if (response.data[i].gubun == 'DFT01') {
            indtArr.push(Number(response.data[i].indt));
          }
        }

        let maxIndt = Math.max(...indtArr);
        if(Number(response.data[i].indt) == maxIndt) {
          updateCardData.value.draftdocttl = response.data[i].sfilename;
          updateCardData.value.drafturl = response.data[i].fleurl;
        }

        updateCardData.value.draftfile.push(response.data[i]);
      }
    }
  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
};

let checkYN = "N";
const me = ref({asktype: "", pathorder: "", state: "", objid: ""});
const checkOrder = async() => {
  // 내가 결재순번인지 체크
  try {
    const response = await API.dctAPI.selectDctPath({
      ...docidData.value
    },
      urlPaths.value
    );

    for (let i = 0; i < response.data.length; i++) {
      if (response.data[i].objid == getUserLoginData.value.userid) {
        me.value.asktype = response.data[i].asktype;
        me.value.pathorder = response.data[i].pathorder;
        me.value.state = response.data[i].state;
        me.value.objid = response.data[i].objid;
        updateCardData.value.opinionpathorder = me.value.pathorder;
      }
    }

    for (let i = 0; i < response.data.length; i++) {
      if(response.data[i].pathorder < me.value.pathorder) {
        if (response.data[i].asktype == 'DEA11') {
          checkYN = "Y";
        } else {
          checkYN = "N";
          if (response.data[i].state == 'DCST5') {
            checkYN = "Y";
          }
        }
      }
    }

    if (me.value.asktype == 'DEA31' || updateCardData.value.oridocid != '') {
      draftView.value.data = 'Y';
    }
  } catch (error) {
    console.log(error);
  }
}

// 기안문 버전별 조회 팝업 관련
const bVisibleHwpCtrlPopup = ref(false)
const hwpCtrlPopupArgs = ref({})
const hwpCtrlPopupToggleFunc = ref((pathData) => {
  if (updateCardData.value.draftfile.length > 0) {
    if (pathData != null && pathData != undefined) {
      // 전체 조회 데이터 넘겨줌
      hwpCtrlPopupArgs.value.data = updateCardData.value;

      // asktype 넘겨줌
      hwpCtrlPopupArgs.value.asktype = pathData.asktype;

      // userid 넘겨줌
      hwpCtrlPopupArgs.value.objid = pathData.objid;

      // url 넘겨줌
      let arr = [];
      for (let i = 0; i < updateCardData.value.pathList.length; i++) {
        let object = {};
        object.id = updateCardData.value.pathList[i].objid;

        for (let j = 0; j < updateCardData.value.draftfile.length; j++) {
          if (object.id == updateCardData.value.draftfile[j].inid) {
            object.url = updateCardData.value.draftfile[j].fleurl;
            break;
          } else {
            object.url = "";
          }
        }
        arr.push(object);
      }

      for (let i = 0; i < arr.length; i++) {
        if (arr[i].id == pathData.objid) {
          if (arr[i].url != '') {
            hwpCtrlPopupArgs.value.url = arr[i].url;
          } else {
            // let urlString = '';
            // for(let j= arr.length-1; j>=0; j--) {
            //   if(arr[j].url != '') {
            //     urlString = arr[j].url;
            //     break;
            //   }
            // }
            // hwpCtrlPopupArgs.value.url = urlString;
            if (i != 0) {
              let count = 1;
              while (true) {
                if(count == arr.length) {
                  break;
                }
                hwpCtrlPopupArgs.value.url = arr[i - count].url;

                if (hwpCtrlPopupArgs.value.url == '') {
                  count++;
                } else {
                  break;
                }
              }
            }
          }
        }
      }
    }
    bVisibleHwpCtrlPopup.value = !bVisibleHwpCtrlPopup.value;
  } else {
    alert("조회 할 기안문이 없습니다.");
  }
})
const hwpCtrlPopupReturnFunc = ref((retValue) => {
  hwpCtrlPopupToggleFunc.value();
})



// 임시저장 팝업
const bVisibleDctTempPopup = ref(false)
const dctTempPopupReuturn = ref({})
const dctTempPopupArgs = ref({})
const dctTempPopupToggleFunc = ref(() => {
  // if (draftToggle.value.data == 'Y') {
  //   childRef.value.saveFile(updateCardData.value.docttl, 'Y');
  // }

  if (makeAlert() == false) {
    return;
  }

  // 저장로딩 true
  updateCardData.value.saveLoading = true;

  if (draftToggle.value.data == 'N') {
    draftToggle.value.data = 'Y';
  } else {
    callDraftToggle('Y');
  }

  if (chkbox.value == true) {
    updateCardData.value.gubun = 'DOPT1'
  } else {
    updateCardData.value.gubun = 'DOPT2'
  }

  dctTempPopupArgs.value.data = updateCardData.value;
  // dctTempPopupArgs.value.file = attachFileData.value;
  dctTempPopupArgs.value.file = newAttachFileArray.value;
  dctTempPopupArgs.value.flag = '1'; // 0: 기안 임시저장, 1: 결재 임시저장
  bVisibleDctTempPopup.value = !bVisibleDctTempPopup.value;
})
const dctTempPopupReturnFunc = ref((retValue) => {
  dctTempPopupToggleFunc.value()
  dctTempPopupReuturn.value = retValue;
})

// ShieldViewer Popup
const docloading = ref(false);
const visibleViewerPopup = ref(false);
const toggleViewerPopup = async (file, userid) => {
  docloading.value = true;
  visibleViewerPopup.value = !visibleViewerPopup.value;
  await viewDocument(file, userid, 'N,N', '0');
  document.querySelector('iframe').addEventListener('load', function() {
    docloading.value = false;
  });
}

// left, top 숨김/펼침 버튼 클릭 시
// 기안문 width, height 변경되도록
const compKey = ref(0);

watch(() => contentWidth.value, (newWidth) => {
  compKey.value++;
});

watch(() => storeContentHeight.value, (newHeight) => {
  compKey.value++;
});


// 접수문서 시행정보 조회
const recvDocidData = ref({ docid: "" });

const oriAttrData = ref({});
const oriPathList = ref([]);
const oriPathData = ref("");
const oriRecvList = ref([]);
const oriRecvData = ref({});

const getDctAttrOridocid = async () => {
  try {
    const response = await API.dctAPI.getDctAttr({
      ...recvDocidData.value
    },
      urlPaths.value
    );
    oriAttrData.value = response.data;
    let docnoArr = oriAttrData.value.docno.split('-');
    oriAttrData.value.docnosplit = docnoArr[0];

  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const selectDctPathOridocid = async () => {
  try {
    const response = await API.dctAPI.selectDctPath({
      ...recvDocidData.value
    },
      urlPaths.value
    );
    oriPathList.value = response.data;

    oriPathData.value = formatPathList.value;

  } catch (error) {
    console.log(error);
    alert("Server Error");
  }

}

const getDctRecvOridocid = async () => {
  try {
    const response = await API.dctAPI.getDctRecv({
      ...recvDocidData.value
    },
      urlPaths.value
    );
    oriRecvList.value = response.data;

    for (let i = 0; i < oriRecvList.value.length; i++) {
      // if (oriRecvList.value[i].deptid == getUserLoginData.value.deptid) {
      //   oriRecvData.value = oriRecvList.value[i];
      // }
      oriRecvData.value = oriRecvList.value[0];
    }

  } catch (error) {
    console.log(error);
    alert("Server Error");
  }
}

const formatPathList = computed(() => {
  const formattedPathList = oriPathList.value.map((item) => {
    return `${item.dutynamedesc}(${transformAsktype(item.asktype)} ${transformDate(item.reportdt)})`;
  });
  return formattedPathList.join(', ');
});

</script>