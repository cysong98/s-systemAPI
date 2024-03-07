/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

/* TransFormLabelDataUtil.js */
/* for data labeling... */

// bms_dct_enf_doc 테이블 docstatus 컬럼
function transformStatus(value) {
  if (!value || value === '') {
    return '';
  }
    if (value === 'EDS01') return '수신';
    if (value === 'EDS02') return '배부';
    if (value === 'EDS03') return '재배부';
    if (value === 'EDS04') return '재배부요청';
    if (value === 'EDS05') return '접수';
    if (value === 'EDS06') return '반송대기';
    if (value === 'EDS07') return '이첩';
    if (value === 'EDS08') return '경유';
    if (value === 'EDS09') return '접수대기';
    if (value === 'EDS10') return '재지정요청';
    if (value === 'EDS11') return '종료';
    if (value === 'EDS12') return '접수';//담당변경접수
    if (value === 'EDS40') return '반송종료';
    if (value === 'EDS41') return '재발송';
    return value;  // 변환하지 않는 값들은 그대로 반환
};

// bms_dct_rdoc 테이블 state 컬럼
function transformState(value) {
  if (!value || value === '') {
    return '';
  }
    if (value === 'DRDS0') return '발송보류';
    if (value === 'DRDS1') return '작성중';
    if (value === 'DRDS2') return '처리중';
    if (value === 'DRDS3') return '종료';
    if (value === 'DRDS4') return '발송대기';
    if (value === 'DRDS5') return '발송안함';
    if (value === 'DRDS6') return '반송대기';
    if (value === 'DRDS7') return '예약발송';
    if (value === 'DRDS8') return '발송완료';
    if (value === 'DRDS9') return '발송실패';
    return value;  // 변환하지 않는 값들은 그대로 반환
  };


// bms_dct_path 테이블 asktype 컬럼
function transformAsktype(value) {
  if (!value || value === '') {
    return '';
  }
  if (value === 'DEA11') return '기안';
  if (value === 'DEA21') return '검토';
  if (value === 'DEA31') return '협조';
  if (value === 'DEA41') return '결재';
  if (value === 'DEA43') return '반려';
  if (value === 'DEA45') return '1인결재';
  if (value === 'DEA51') return '전결';
  if (value === 'DEA61') return '대결';
  if (value === 'DEA71') return '전대결';
  return value;
};

// bms_dct_mgmt_regi 테이블 notiprsrvterm 컬럼
function transformNotiprsrvterm(value) {
  if (!value || value === '') {
    return '';
  }
  if (value === '001') return '1년';
  if (value === '003') return '3년';
  if (value === '005') return '5년';
  if (value === '010') return '10년';
  if (value === '030') return '30년';
  if (value === '040') return '준영구';
  if (value === '050') return '영구';
  return value;
};

// yyyymmddhhmmss 데이터를 yyyy.mm.dd hh:mm 으로 변환
function transformDate(input) {
  if (!input || input === '') { //값이 비어있을경우 공백반환
    return '';
  }

  let hour = '00'; // 기본값으로 '00' 설정
  let minute = '00'; // 기본값으로 '00' 설정

  const year = input.substring(0, 4);
  const month = input.substring(4, 6);
  const day = input.substring(6, 8);

  // 입력된 데이터의 길이가 10 이상인 경우에만 시간과 분을 설정
  if (input.length >= 10) {
    hour = input.substring(8, 10);
  }
  if (input.length >= 12) {
    minute = input.substring(10, 12);
  }

  if (input.length === 8) {
    return `${year}.${month}.${day}`;
  }else{
    return `${year}.${month}.${day} ${hour}:${minute}`;
  }
  
};


// bms_dct_rdoc 테이블 gubun 컬럼
function transformRdocGubun(value) {
  if (!value || value === '') {
    return '';
  }
  if (value === 'DOPT1') return '내부결재용';
  if (value === 'DOPT2') return '시행용';
  return value;
};

// bms_dct_mgmt_regi 테이블 seclevel 컬럼
function transformSeclevel(value) {
  if (!value || value === '') {
    return '';
  }
  if (value === '2') return 'II급';
  if (value === '3') return 'III급';
  if (value === '4') return '대외비';
  if (value === '5') return '일반';
  return value;
};


// bms_dct_mgmt_regi 테이블 secstatus 컬럼
function transformSecstatus(value) {
  if (!value || value === '') {
    return '';
  }
  if (value === 'MGT00') return '';
  if (value === 'MGT20') return '재분류요청중';
  if (value === 'MGT21') return '재분류승인';
  if (value === 'MGT22') return '재분류';
  if (value === 'MGT30') return '존안요청중';
  if (value === 'MGT31') return '존안승인';
  if (value === 'MGT32') return '존안';
  if (value === 'MGT35') return '존안비전자삭제';
  if (value === 'MGT40') return '파기요청중';
  if (value === 'MGT41') return '파기승인';
  if (value === 'MGT42') return '파기';
  if (value === 'MGT50') return '인계인수중';
  if (value === 'MGT51') return '인계완료';
  if (value === 'MGT52') return '인수완료';
  if (value === 'MGT60') return '이관대기요청중';
  if (value === 'MGT61') return '이관대기승인';
  if (value === 'MGT62') return '이관대기';
  if (value === 'MGT70') return '이관연기요청중';
  if (value === 'MGT71') return '이관연기승인';
  if (value === 'MGT72') return '이관연기';
  if (value === 'MGT80') return '내부이관요청중';
  if (value === 'MGT81') return '내부이관승인';
  if (value === 'MGT82') return '내부이관';
  if (value === 'MGT85') return '이관비전자삭제';
  if (value === 'MGT90') return '외부이관';
  if (value === 'MGT91') return '등급변경';
  if (value === 'MGT99') return '반송';
  return value;
};

// bms_trn_object 테이블 status 컬럼
function transformObjStatus(value) {
  if (!value || value === '') {
    return '';
  }
  if (value === 'TRS01') return '결재중';
  if (value === 'TRS02') return '인계완료';
  if (value === 'TRS03') return '인수중';
  if (value === 'TRS04') return '인수완료';
  if (value === 'TRS05') return '반려';
  return value;
};

// bms_dct_enf_history 테이블 actgubun  컬럼
function transformEnfHisActgubun(value) {
  if (!value || value === '') {
    return '';
  }
  if (value === 'EPA01') return '배부';
  if (value === 'EPA02') return '재배부';
  if (value === 'EPA03') return '재배부요청';
  if (value === 'EPA04') return '접수';
  if (value === 'EPA05') return '반송';
  if (value === 'EPA06') return '이첩';
  if (value === 'EPA07') return '열람';
  if (value === 'EPA08') return '업무담당지정';
  if (value === 'EPA09') return '업무관리지정';
  if (value === 'EPA10') return '열람지정';
  if (value === 'EPA11') return '수신';
  if (value === 'EPA12') return '담당확인';
  if (value === 'EPA13') return '확인';
  if (value === 'EPA14') return '재분류요청';
  if (value === 'EPA15') return '경유';
  if (value === 'EPA16') return '과제카드지정';
  if (value === 'EPA17') return '재지정요청';
  if (value === 'EPA18') return '발송안함';
  return value;
};

// bms_dct_path 테이블 state 컬럼
function transformPathState(value) {
  if (!value || value === '') {
    return '';
  }
    if (value === 'DCST5') return '처리완료';
    return value;
};

// bms_dct_rdoc 테이블 emergency 컬럼
function transformEmergency(value) {
  if (!value || value === '') {
    return '';
  }
    if (value === 'N') return '';
    if (value === 'Z') return '지급';
    if (value === 'Y') return '긴급';
    if (value === 'X') return '초긴급';
    return value;
};

// bms_dct_hist_card 테이블 histstatus 컬럼
function transHistStatus(value) {
  if (!value || value === '') {
    return '';
  }
    if (value === '010') return '요청';
    if (value === '011') return '결재';
    if (value === '012') return '반려';
    if (value === '013') return '보류';
    if (value === '014') return '전결';
    if (value === '015') return '대결';
    if (value === '016') return '실행'; // 추가함
    if (value === '017') return '권한 회수'; // 추가함
    if (value === '018') return '요청서 회수'; // 추가함
    if (value === '041') return '열람요청 인가';
    if (value === '042') return '열람요청 비인가';
    if (value === '051') return '인쇄요청 검토';
    if (value === '052') return '인쇄요청 시행';
    if (value === '053') return '인쇄요청 열람';
    if (value === '056') return '검토용 인쇄';
    if (value === '057') return '시행용 인쇄';
    if (value === '058') return '열람용 인쇄';
    if (value === '059') return '원격지열람';
    if (value === '061') return '재사용요청 인가';
    if (value === '062') return '재사용요청 비인가';

    // 추가함
    if (value === '071') return '인쇄요청 인가';
    if (value === '072') return '인쇄요청 비인가';
    if (value === '073') return '인쇄실행';
    if (value === '074') return '파쇄실행';
    if (value === '081') return '반출요청 인가';
    if (value === '082') return '반출요청 비인가';
    if (value === '083') return '반출실행';
    if (value === '084') return '반납요청';
    if (value === '085') return '반납실행';
    if (value === '091') return '재분류요청 인가';
    if (value === '092') return '재분류요청 비인가';
    if (value === '101') return '파기요청 인가';
    if (value === '102') return '파기요청 비인가';
    if (value === '111') return '존안요청 인가';
    if (value === '112') return '존안요청 비인가';
    if (value === '121') return '이관대기요청 인가';
    if (value === '122') return '이관대기요청 비인가';
    if (value === '131') return '이관연기요청 인가';
    if (value === '132') return '이관연기요청 비인가';
    if (value === '141') return '이관요청 인가';
    if (value === '142') return '이관요청 비인가';
    if (value === '151') return '인계담당';
    if (value === '152') return '인수담당';
    if (value === '153') return '인계결재';
    if (value === '154') return '인수결재';
    return value;
};

// bms_dct_hist_card 테이블 histgubun 컬럼
function transHistGubun(value) {
  if (!value || value === '') {
    return '';
  }
    if (value === '01') return '기안';
    if (value === '02') return '결재';
    if (value === '03') return '수정';
    if (value === '04') return '등록';
    if (value === '05') return '열람';
    if (value === '06') return '인쇄';
    if (value === '07') return '재사용';
    if (value === '08') return '시행';
    if (value === '09') return '발신';
    if (value === '10') return '수신';
    if (value === '11') return '배부';
    if (value === '12') return '재배부';
    if (value === '13') return '반송';
    if (value === '14') return '접수';
    if (value === '15') return '검토필';
    if (value === '16') return '재분류';
    if (value === '17') return '존안';
    if (value === '18') return '이관연기';
    if (value === '19') return '이관';
    if (value === '20') return '파기';
    if (value === '21') return '회수';
    if (value === '22') return '인계인수';
    if (value === '23') return '복제복사';
    if (value === '24') return '대출';
    if (value === '25') return '지출';
    if (value === '26') return '대체';
    if (value === '27') return '추가';
    if (value === '28') return '이관대기';
    if (value === '29') return '내부이관';
    if (value === '30') return '이송';
    if (value === '31') return '경유';
    if (value === '32') return '반납';
    if (value === '33') return 'RFID확인';
    if (value === '34') return '발송보류';
    if (value === '35') return '발송안함';
    if (value === '36') return '재발송';
    if (value === '37') return '담당변경';
    if (value === '38') return '오프라인발송';
    return value;
};

// bms_dct_auth_card 테이블 authgubun 컬럼
function transAuthGubun(value) {
  if (!value || value === '') {
    return '';
  }
    if (value === '01') return '열람';
    if (value === '02') return '재사용';
    if (value === '03') return '인쇄';
    if (value === '04') return '대출';
    if (value === '05') return '지출';
    return value;
};

function transVgroupIcon(value) {
  let result = [];
  if (!value || value === '')
    return result;

  if(value.vgroupid !== undefined) {
    const vgroupidList = value.vgroupid.split(", ");
    vgroupidList.map((el) => {
      if(el === '4001') result.push({content: "시스템", color: "#333333", vgroupname: "시스템관리자"});
      else if(el === '4003') result.push("");
      else if(el === '4005') result.push("");
      else if(el === '5101') result.push("");
      else if(el === '5210') result.push({content: "문서", color: "#d5171e", vgroupname: "문서과 비밀관리자"});
      else if(el === '5220') result.push({content: "처리", color: "#3f48cc", vgroupname: "처리과 비밀관리자"});
      else if(el === '6010') result.push({content: "파일", color: "#333333", vgroupname: "파일 반출 담당자"});
      else if(el === '8010') result.push({content: "존안", color: "#ff974f", vgroupname: "존안관리담당자"});
      else if(el === '8015') result.push({content: "존안", color: "#d5171e", vgroupname: "존안관리부서장"});
      else if(el === '8030') result.push({content: "기관", color: "#333333", vgroupname: "기관보안관리자"});
      else if(el === '8050') result.push({content: "이관", color: "#ff974f", vgroupname: "이관관리담당자"});
      else if(el === '8255') result.push({content: "이관", color: "#d5171e", vgroupname: "이관관리부서장"});
      else if(el === '9001') result.push("");
      else if(el === '3001') result.push({content: "전체", color: "#333333", vgroupname: "전체기관관리자"});
      else if(el === '5010') result.push("");
      else if(el === '6001') result.push("");
      else if(el === '7050') result.push("");
      else if(el === '8000') result.push("");
      else if(el === '8100') result.push("");
    });
  }
  
  let vgroupnameList = value.vgroupname.split(", ");
  for(let i=0; i<vgroupnameList.length; i++) {
    if(vgroupnameList[i] === '부서장') {
      result.push({content: "부서장", color: "#6ec300"});
      break;
    }
  }

  return result;
}

export { transformStatus, transformState, transformAsktype, transformDate, transformNotiprsrvterm, transformRdocGubun, transformSeclevel, transformSecstatus, transformObjStatus, transformEnfHisActgubun, transformPathState, transformEmergency, transHistStatus, transHistGubun, transAuthGubun, transVgroupIcon }