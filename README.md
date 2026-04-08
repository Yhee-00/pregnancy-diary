# 🤱 태교일기 포토북 서비스

> 아이에게 남기는 첫 기록 — 임신 기간의 소중한 일기를 실물 책으로 간직하세요.

## 1. 서비스 소개

**어떤 서비스인가요?**
임신 기간 동안 작성한 태교일기와 사진을 모아 실물 포토북으로 제작해주는 웹 서비스입니다.

**누구를 위한 서비스인가요?**
임신 중인 예비 부모, 아이에게 임신 기간의 기억을 실물로 남기고 싶은 분

**주요 기능**

- 날짜별 태교일기 작성
- 일기별 사진 업로드
- 작성한 일기를 포토북으로 제작 및 배송 주문

---

## 2. 실행 방법

### 사전 준비

- Java 17 이상
- Node.js 18 이상
- MariaDB (실행 후 `pregnancy_diary` 데이터베이스 생성 필요)
```bash
  CREATE DATABASE pregnancy_diary CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
  ```
### 설치 및 실행

```bash
# 저장소 클론
git clone https://github.com/Yhee-00/pregnancy-diary.git
cd pregnancy-diary

# 환경변수 설정
cp .env.example .env

# .env 파일을 열어 각 항목 입력

# Windows
Get-Content .env | ForEach-Object {
  if ($_ -match '^([^#][^=]*)=(.*)$') {
    [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2])
  }
}

# Mac / Linux
set -a && source .env && set +a

# 백엔드 실행
cd backend
./gradlew bootRun

# 프론트엔드 실행 (새 터미널)
cd frontend
npm install
npm run dev
```

### 환경변수 설명 (.env)

| 변수명              | 설명                                 |
| ------------------- | ------------------------------------ |
| `DB_URL`            | MariaDB 접속 URL                     |
| `DB_USERNAME`       | DB 사용자명                          |
| `DB_PASSWORD`       | DB 비밀번호                          |
| `SWEETBOOK_API_KEY` | 스위트북 Sandbox API Key             |
| `OPENAI_API_KEY`    | OpenAI API Key                       |
| `FILE_UPLOAD_DIR`   | 사진 업로드 경로 (기본값: ./uploads) |

---

## 3. 사용한 API 목록

| API        | 엔드포인트                           | 용도                     |
| ---------- | ------------------------------------ | ------------------------ |
| Books API  | `POST /books`                        | 포토북 생성              |
| Books API  | `POST /books/{bookUid}/cover`        | 포토북 커버 추가         |
| Books API  | `POST /books/{bookUid}/contents`     | 일기 내용 페이지 추가    |
| Books API  | `POST /books/{bookUid}/finalization` | 포토북 완성 처리         |
| Orders API | `POST /orders`                       | 포토북 인쇄 및 배송 주문 |

---

## 4. AI 도구 사용 내역

| AI 도구   | 활용 내용                                       |
| --------- | ----------------------------------------------- |
| Claude AI | 백엔드, 프론트 전반적인 구조설계                |
| ChatGPT   | open ai 연동 구조 설계 및 메세지 생성 로직 구현 |
| Gemini    | 더미 데이터 생성                                |

---

## 5. 설계 의도

**왜 이 서비스를 선택했나요?**

임신 기간은 평생 한 번뿐인 소중한 시간이며, 아이에게 남기는 첫 번째 기록이라는 점에서 높은 의미를 가집니다. 하지만 종이에 직접 기록하는 방식은 번거롭고 지속하기 어렵다는 한계가 있습니다.

이 서비스는 이러한 문제를 해결하기 위해,
일기 작성은 디지털로 간편하게 수행하면서도 최종 결과물을 포토북 형태의 실물로 제작할 수 있도록 설계하였습니다.

즉, "기록의 편의성"과 "보관의 가치"를 동시에 제공하는 것을 목표로 합니다.

**비즈니스 가능성**

임신 및 육아 시장은 감성 소비 성향이 강하며, 개인화된 콘텐츠에 대한 지불 의사가 높은 특징을 가지고 있습니다.

본 서비스는 기존 포토북 제작 시장에 "태교 일기"라는 특화된 콘텐츠를 결합하여 차별화를 시도하였으며,
향후 산부인과, 육아 커뮤니티와의 연계를 통해 B2B2C 형태로 확장 가능성이 있습니다.

또한, 디지털 기록 데이터를 기반으로 개인화된 콘텐츠(예: 주차별 메시지, 성장 기록 등)를 제공함으로써 지속적인 사용자 경험 개선이 가능합니다.

**더 시간이 있었다면 추가했을 기능**

- 임신 기간 80%이후 포토북 생성 버튼 활성화
- 완성된 포토북 미리보기
- AI 기반 태교일기 작성 도우미 (오늘의 감정, 태동 기록 등 프롬프트 제공)
- 주차별 태아 성장 정보 연동
