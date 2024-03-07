# 프로젝트 개요

| 프로젝트명 | 비밀문서 관리시스템 |
| --- | --- |
| 프로젝트 기간 | 2023.09.18 ~ 2024.02.29 |
| 프로젝트 인원 | 4명 (이범혁, 송채연, 이홍규, 박찬현) |

# 프로젝트 역할

| 이름 | 역할(맡은 도메인 등) |
| --- | --- |
| 이범혁 | PL, 웹기안기, 결재 |
| 송채연 | 공통 팝업, 기안, 오프라인접수, 생산비전자, 비밀관리 - 사용 요청,처리,예고문도래 / LDAP |
| 이홍규 | 발송, 접수, 인계인수 초기 / PDF변환 API |
| 박찬현 | 공통관리, 로그인, 실드뷰어, 인계인수 마무리 / IdGP API |

# 프로젝트 결과

## 1. 문제점

| 인덱스 | 요약 | 문제 | 개선방안 |
| --- | --- | --- | --- |
| 1 | 요청사항 번복 | - 요청사항을 반영했는데 다시 롤백하는 경우 많음
- 수정사항을 반영하려해도 다시 번복할까봐 주석처리하거나,
  미사용 기능을 분기시켜 코드 내에 남기게 되면서 코드가 불필요하게 길어지거나 복잡해짐 | 코멘트에 요청사항을 남겨서 커밋
→ 향후 요청사항이 번복되거나 변경시 참고할 수 있도록 함 |
| 2 | 설계 불분명 | - 피그마 보고 개발하지 말라고 하심
- 요청사항이 명확하지 않음 → 알아서 센스있게 개발해야함
- 개발된 모습을 보고 설계를 뒤엎음 | - 소통 활성화
- 설계 인원 추가 |
| 3 | 업무 자르는 시점 | - 개발기간이 다 끝나가는데도 추가개발을 요청함.
- 기존 개발프로그램을 통합테스트하여 마무리할 시점에 추가개발을 하다보니 또 딜레이되고, 추가개발에대한 사이드 이펙트에 대하여 또 통합테스트 및 사이드이펙트를 수정해야함
- 돈 못받고 일하는 꼴 | 통합테스트를 하는시점에서 부터는 업무를 자르고, 개발 마무리 및 문서작업에 집중할 수 있어야 함 |

## **2. AnyX 개선사항**

| 인덱스 | 요약 | 문제 | 개선방안 |
| --- | --- | --- | --- |
| 1 | 단일테이블 CRUD더라도 전체 추가 gen | 사용하는 테이블이라면 필연적으로 select 또는 insert가 들어가는 편.
→ 처음은 select만 했더라도 개발을 진행함에따라 insert나 update 등도
    필요하게 되는 경우가 다반사
※ 단순한 CRUD이더라도 번거롭게 추가 gen받고 merge해야 함
    혹은 기계적으로 손코딩을 하여도 시간이 아쉽게 느껴짐 | - 처음부터 데이터베이스에 있는 테이블 전체를 각각 단일 기능으로 CRUD gen
- 최종마감시에 필요없거나 안쓰는 코드를 제거하도록 함
→ 이로써 단순코딩의 시간낭비를 줄이고 로직구현이나 통합테스트 등에
    시간을 추가 확보함 |
| 2 | AnyX 버전관리 안됨 | 몇 달 후에 다시 vuetify 버전으로 gen 받으니까 코드에서 있어야하는
코드는 없고 없어도 되는 코드는 있는 등의 문제가 발생 | AnyX 코드 자체를 UI 프레임워크별로 모듈화 & 버전관리 필요 |
| 3 | 불필요 console.log | gen 되어서 나오는 프론트엔드 코드에 불필요한 콘솔로그가 많음
→ 결국 하나씩 다 지워야 함 | AnyX에서 처음부터 콘솔로그 삭제 or 주석처리로 변경 요망
(특히 부모 컴포넌트에서 팝업 함수들 부분) |
| 4 | 백엔드 테이블명 기반의 파일구조 | - 사용하는 테이블이 많아질 경우, 클래스(컨트롤러, 서비스 등)가
    너무 많아져서 어떤 메소드가 어디에 들어있는지 찾기 힘듦
- 동일한 동작의 메소드가 A 폴더에, B 폴더에 들어있기도 함
- 문서작업 할 때도 힘듦 | 업무 내용에 맞게 직접 입력받는 이름으로 클래스를 생성
→ 미입력시 기존 테이블명 그대로 |

## 3. 회고

- 개발 도중에 새로 필요한 CRUD를 추가할 때,
 1) 버전관리가 되지않아 이전 gen받은 코드와 서로 상이하고
 2) 단순히 몇 개만 추가하더라도 전체를 또 다시 gen을 받아야하는 상황이 발생하면서
 3) gen받은 코드를 merge 하는데에 시간이 많이 소요되어
 ⇒ 개발이 많이 진행된 상황에서는 AnyX를 이용하기 꺼려짐
- 시연일을 앞에 두고 뒤늦게 추가 개발을 요청하는 경우나 요청사항이 번복되는 경우 등이 잦았음
⇒ 설계가 분명했으면 빈도가 적을 수 있었다고 생각함
- 개발표준이 정해져 있지않아 코드를 이해하는데에 어려움이 약간 있었음
⇒ 초반에 페이지 구조나 스타일도 통일이 이루어지지않음
- 일정 관리 및 소통이 더 활성화 될 필요가 있음
- 초반부터 개발 범위가 좀 더 명확해져야 함

## 4. API 사용 내역

### **1) 웹기안기**

- 라이브러리 함수 호출 방식 (라이브러리 파일의 경우 http 요청으로 웹기안기 서버에서 받아옴)

```
<script type="text/javascript" src="https://hwp.s-system.kinac:8080/webhwpctrl/js/hwpctrlapp/utils/util.js"></script>
<script type="text/javascript" src="https://hwp.s-system.kinac:8080/webhwpctrl/js/hwpctrlapp/hwpCtrlApp.js"></script>
<script type="text/javascript" src="https://hwp.s-system.kinac:8080/webhwpctrl/js/webhwpctrl.js"></script>
```

index.html <head> 태그 안에 위와 같이 선언하여 사용

- 사용한 함수 (자세한 내용은 아래의 첨부파일 참고)

      **1. BuildWebHwpCtrl(”객체id”, “url”, callback)** : 웹기안기 객체 생성
      **2. Open(”한글 파일 url”, “HWP”, “lock:false”, callback)** : 한글 파일 열기
      **3. SaveAs(”문서제목”, “Hwp”, “”, callback)** : 한글 파일 저장
      **4. PutFieldText(”필드명”, “내용”)** : 웹기안기 특정 필드에 내용을 넣어줌
      **5. GetFieldText(”필드명”)** : 웹기안기 특정 필드의 내용을 읽어옴
      **6. MoveToField(”필드명”)** : 웹기안기의 특정 필드로 이동함
      **7. Run(”동작명”)** : 특정 동작을 실행함  ex) hwpCtrl.Run(”Delete”) - 현재 위치의 내용 삭제
      **8. InsertPicture(”이미지 파일 url”, …, callback )** : 이미지 파일을 넣어줌
      **9. CreateAction(”액션명”)** : 특정 액션을 실행함  
          ⇒ ex) hwpCtrl.CreateAction(”TableCreate”) - 표 생성, 아래처럼 사용하면 2x2 표가 생성됨

```
var tbact = hwpCtrl.CreateAction("TableCreate");
var tbset = tbact.CreateSet();
tbact.GetDefault(tbset);
tbset.SetItem("Rows", 2);
tbset.SetItem("Cols", 2);
tbact.Execute(tbset);
```

[[웹한글기안기]  Action Table__v1.1.hwp](https://prod-files-secure.s3.us-west-2.amazonaws.com/6760d9ca-9959-47f6-8801-fa15e0c582d9/1d51f44b-73fb-452f-a338-271de6887bfd/%EC%9B%B9%ED%95%9C%EA%B8%80%EA%B8%B0%EC%95%88%EA%B8%B0__Action_Table__v1.1.hwp)

[[웹한글기안기] API가이드__v2.4.hwp](https://prod-files-secure.s3.us-west-2.amazonaws.com/6760d9ca-9959-47f6-8801-fa15e0c582d9/fcda0d4c-8c7b-40ff-8e30-95fe8dc79ac5/%EC%9B%B9%ED%95%9C%EA%B8%80%EA%B8%B0%EC%95%88%EA%B8%B0_API%EA%B0%80%EC%9D%B4%EB%93%9C__v2.4.hwp)

[[웹한글기안기] Parameter Set ID_Item ID__v1.2.hwp](https://prod-files-secure.s3.us-west-2.amazonaws.com/6760d9ca-9959-47f6-8801-fa15e0c582d9/0d56428d-2936-4186-b7d8-72d8e7e090c9/%EC%9B%B9%ED%95%9C%EA%B8%80%EA%B8%B0%EC%95%88%EA%B8%B0_Parameter_Set_ID_Item_ID__v1.2.hwp)

### **2) IdGP**

- http 요청방식 / 관리자 콘솔 / 연계 서비스 (Security365 - SHIELDGate, SHIELDrive)
- 사용한 API
**1.  사용자 정보 연계 API** : IdGP API에 사용하기 위한 사용자 정보 ⇒ 실시간으로 등록, 관리
**2.  조직별 그룹정보 연동 API** : IdGP API의 그룹정보 기능 사용 ⇒ 사용자 정보 조직별 관리
**3.  사용자 인증 및 로그인 API** : IdGP API의 사용자 인증 기능 및 로그인 기능 사용
- 사용기술
**1. 사용자 정보** `권한 : **Bearer** access_token`
   1) POST 형식으로 전달받은 데이터를 토대로 사용자 정보를 추가, 삭제, 수정
   2) 특히 전달하는 아이디와 비밀번호는 문자열로 묶어서 RSA 암호화
   3) RSA 암호화에 필요한 공개키는 API를 통해 얻을 수 있도록 리스트로 보관, 개인키는 서버에 따로 비공개 보관
**2. 조직별 그룹 정보** `권한 : **Bearer** access_token`
   1) POST 형식으로 전달받은 데이터를 토대로 그룹 정보를 추가, 삭제, 수정
   2) 그룹을 생성하여 사용자를 넣음
   3) SHIELDrive에서 그룹별로 사용자 조회, 그룹별 문서 공유 시스템
**3. 사용자 인증 및 로그인** `권한 : **Bearer** access_token / **Basic** base64(client_id:secret_key)`
   1) 추가한 ID와 패스워드로 보안 클라우드 서비스(SHIELDGate)에서 인가-인증, 통과
   2) access_token 취득을 위한 인증방식
       - 앱 서비스 생성 시 반환되는 client ID, secret Key를 통한 인증 방식 (client_credentials 방식)
          ⇒ 전달받은 문자열을 base64 디코딩하여 서버의 client_id와 secret_key를 탐색, 인증
       - 사용자 ID와 패스워드를 통한 인증 방식 (authorization_code 방식)
          (1) URL 요청 ⇒ Security365 사용자 인증 ⇒ 리다이렉트 URL 뒤에 랜덤한 값의 “code”를 파라미터로 부여 `1차인증`
          (2) code 값을 이용해 사용자 인증 `2차인증`
   3) 변조되지 않은 jwt인지 검증 ⇒ jwt 파싱, 헤더의 회사 ID 등 고유값 정상적인지 확인
   4) access_token, jwt 만료일 검사
       - 만료 X ⇒ pass
       - 만료 O ⇒ 갱신 요청, 갱신 실패 시 로그인페이지 redirect
   5) Refresh Token으로 access_token 갱신
       - access_token : 실제 API 사용에 이용되는 토큰 ⇒ 만료기간 짧게
       - refresh_token : 만료된 access_token을 연장하기 위한 토큰 ⇒ 만료기간 길게
**4. 관리자 콘솔**
   1) 관리자 콘솔 접속 : 관리자 권한으로 웹 페이지 로그인 ⇒ 회사 생성, 앱 서비스 관리, 사용자 관리, 토큰 관리 등

### **3) SHIELD VIEWER (PDF 뷰어)**

- http 요청방식
- 사용한 API
**1. 문서 등록 API** : 파일 객체 혹은 파일 경로 첨부, API를 요청 ⇒ PDF로 변환, 서버 저장
**2. 문서 조회 API** : 반환값으로 받는 URL을 다시 호출하여 문서 열람
**3. 문서 변환 확인 API** : 문서 변환이 완료되었는지 반환값으로 파악 `Y : 완료 / N : 진행중 / F : 변환실패`
- 사용기술
**1. 문서 등록**
   1) 파일객체 혹은 파일경로로부터 얻은 데이터를 PDF 파일로 변환
   2) 이 때 고유한 id 값을 부여 `ex) SHA256` ⇒ 문서 조회 시 특정 파일 구분을 위함
**2. 문서 조회**
   1) 실제 PDF 파일 경로와 고유 id를 포함한 URL 경로를 DB에 저장해둠
   2) URL을 호출하면 대응되는 파일 경로를 찾아서 보여줌
**3. 문서 변환**
   1) PDF 파일 변환 진행도를 체크해서 상태 반환

### **4) PDF CONVERTER**

- http 요청방식
- 사용한 API
**1. 문서 변환 API :** 파라미터로 실제파일경로, 저장경로 담아서 API 요청 ⇒ PDF 변환 및 저장
- 사용기술
**1.  문서변환**
   1) 파라미터의 파일경로로부터 파일을 읽어 PDF 파일로 변환
   2) 파라미터의 저장경로를 읽고 변환된 PDF 파일을 해당 경로에 저장
   3) 변환 성패 여부를 String 값으로 반환

### **5) LDAP**

- crontab 스케쥴링 / LDAPTemplate
- 사용한 API
**1. LDAP 검색 API :** base와 filter를 이용해 LDAP 서버에서 원하는 데이터를 요청
- 사용기술
**1. LDAP 검색, 저장**
   1) base와 filter를 파라미터로 LDAPTemplate의 search 메소드 사용 ⇒ 서버에서 원하는 데이터를 반환
   2) 받아온 데이터를 가공하여 DB에 저장, 사용
**2. crontab 스케쥴링**
   1) crontab을 통해 변경사항이 존재할 수 있는 LDAP 데이터를 정기적으로 업데이트
**3. base, filter 예시**

         `baseDN: "o=Government of Korea, c=KR"`

         `filter: "(objectclass=*)"`

### **6) DB 암호화**

- ….

### **7) 전자서명**

- …
