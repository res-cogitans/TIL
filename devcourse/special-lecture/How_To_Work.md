# How To Work

- 특강: 이두현
  - 쏘카 개발팀장
  - 前 라이엇게임즈 코리아 개발팀장
  - 前 네이버 수석



[TOC]

## 개관

- SOCAR Service Engineering에서도 도입, 시험 중인 방법론 소개

- 각 개발자는 회사가 원하는 것을 개발해야

  - 회사 -> 팀 -> 개인 모두의 alignment가 맞아야 함

- 로드맵

  >  **Know Why, What and When**

  - 위를 알기 위해 필요한 것이 로드맵
  - Team focus에 공감, 몰입할 수 있어야 함
    - 공통의 목적을 위해서 개별 엔지니어들은 기술 역량을 키워야 함

  - 층위

    1. Mothly: 전략
    2. Bi-weekly: Business / Monthly: 성과 공유
    3. 2Weeks Sprint

    - 세 층위의 네 단계의 수행 및 검토가 순환적으로 이루어짐



## 스프린트

- **오해: 스프린트는 일을 나누기 위한 단위**
- **스프린트는 이름 그대로 달려야 함**
  - 우리 회사의 목표를 위해서
  - 단거리 질주(sprint)를 중첩하여 장거리를 달려 나가야(marathon)
- 기존 방법론과의 차이
  - 종래의 방법론에서는 `Proposal -> Design -> Coding` (폭포수)
  - 스프린트는 MVP를 만드는 방식
  - MVP; Minimum Viable Product
    - Core Value
    - DEMO for Feedbacks
  - MVP는 유저 스토리로 구성
    - 유저 스토리의 형식
      - `AS` a passport subscriber,
      - `I WANT TO` know where to park at my destinations(s)
      - `SO THAT` I save my cost in my trip
    - 가장 핵심이 되는 유저 스토리를 MVP로 봄

- 과정

  - 유저스토리 / 백로그
    - 유저스토리 리뷰
      - 과도하게 큰 유저 스토리를 쪼개는 등 유저 스토리를 검토
  - Task planning
    - 테스크명은 유저스토리처럼 굳이 이름을 맞추지 말자
      - 오히려 엔지니어가 이해할 수 있는 형태 좋음
    - 구성원
      - PO
      - TL: 아키텍트
      - Engineers
      - QA
    - 시간 측정
      - 플래닝 포커 등 이용
    - 유저 스토리를 소리 내어 읽으면서 모두 alignment해야
  - Sprint  planning

  - 데일리 스크럼
    - Daily Sharing
    - 진행 상황, 이슈 공유
  - Demo
    - 0번 스프린트에서는 환경 세팅이 이루어짐
      - 환경세팅 + 유저스토리 작성을 추천
      - MVP는 그래서 스프린트1혹은 2에서 나옴
    - 결과물에 따라 Pivoting할 수도 있음
    - 퍼포먼스 테스트도 해볼 것
    - 아키텍처 검토
      - 흐름 살펴보기
      - UML의 관계에 대해서 명확한 이해
  - Retro
    - Sprint Retro, 스프린트 회고
    - 포스트잇을 이용하여 중복 사항을 모아보자
    - 꼭 `하나` 고쳐보고 싶은 것을 찾아보자: 다음 스프린트에서의 개선 사항
  - Release

- Total Ownership
  - 모두가 기획/디자인/개발/검증하는 프로덕트에 대해 Total Owenrship 가짐
  - 신뢰 자율에 기반, 책임, 권한을 갖고 의사 결정
  - 유저스토리/백로그: PM이 결정
  - 테스크 플래닝: TL이 결정
  - 데일리 스프린트: TL이 결정
  - 데모/회고/릴리즈: QA이 결정

- 스프린트의 목표
  - 가치중심적으로 업무
  - 데모 후에 많은 이야기를 나눔: 피드백
  - 개선점 탐색
  - 문제를 조기 파악, 수정
  - 개발 속도 가속
  - 병목 없애기
  - 업무 만족도
  - 주도적 문제제기와 수정

- 배포일을 정해두었음
  - 개발자들의 휴식을 위해
  - 예측 가능성을 위해
  - 