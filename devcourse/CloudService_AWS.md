# CloudService AWS

## 목차

[TOC]

# 소개

## Cloud Service

### 왜 클라우드 서비스인가?

- 클라우드를 사용하지 않을 경우 **기업 인프라 환경이 매우 복잡해짐**

- **클라우드 컴퓨팅**
  - 인터넷 기반의 컴퓨팅
  - 인터넷을 통해 사용자에게 제공하는 인프라, 플랫폼, 소프트웨어
  - 인터넷 통신망 어딘가에서 컴퓨팅 자원을 원하는 대로 가져다 사용
    - 마치 구름에 싸인 것처럼 보이지 않는 컴퓨팅 자원을 사용 가능
    - 내부에 무엇이 있는지, 보거나 알지 않더라도 편하게 사용 가능
    - 인터넷만 있다면 사용 가능
  - 특징
    - On Demand
    - 대규모 확장성
    - 종량제 과금
    - 관리 편의성



### Service model 기준으로 본 유형(service model)

- **IaaS(Infrastructure as a Service)**
  - 서버, 네트워킹, 스토리지와 데이터 센터 공간 등의 컴퓨팅 자원을 제공
  - 종량제 방식 제공
- **PaaS(Platform as a Service)**
  - IaaS + OS, Middleware, Runtime 추가
  - 기본 하드웨어, 소프트웨어, 프로비저닝, 호스팅 등을 구매, 관리하지 않고도
    클라우드 애플리케이션을 빌드하여 제공하는 전 과정을 지원하는 클라우드 기반 환경
  - AWS Elastic Beanstalk, Google App Engine

- **SaaS(Software as a Service)**
  - 모든 기능이 동작하는 SW를 제공

- 비교
  - 다음 요소들에 대한 관리 책임으로 살펴보자
    - 요소1: Applications, Data
    - 요소2: Runtime, Middleware, O/S
    - 요소3: Virtualization, Servers, Storage, Networking
  - On Premises(own server)
    - 위의 모든 요소를 직접 관리해야
  - IAAS(virtual machines)
    - 요소3은 클라우드 서비스 제공자가 관리
  - PAAS(app service)
    - 요소2, 3을 클라우드 서비스 제공자가 관리
  - SAAS(O365)
    - 요소 1, 2, 3 모두를 클라우드 서비스 제공자가 관리



### Deploy 기준으로 본 유형(deployment model)

- **Public Cloud**
  - 누구든지 사용 가능
  - 사용한 만큼 비용 지불

- **Private Cloud**
  - 기업/기관 내부에서만 사용 가능한 클라우드 컴퓨팅 환경 구축
- **Hybrid Cloud**
  - 혼합 구조



### 클라우드 서비스 제품

- 목록

  - 아마존 AWS; Amazon Web Service

  - 마이크로소프트 애저(Azure)

  - 구글 GCP; Google Cloud Platform

  - 오라클 OCI; Oracle Cloud Infrastructure

  - IBM 클라우드

  - 알리바바 클라우드

  - KT 클라우드

  - 네이버 NCP; Naver Cloud Platform



## 클라우드 서비스 기본 개념

### Virtualization(가상화)

- 물리적 컴퓨터 하드웨어를 효율적으로 활용하게 해주는 프로세스
- 클라우드 컴퓨팅의 기반
- 장점
  - 리소스 효율성
  - 관리 편의성
  - 가동 중단 시간 최소화
  - 프로비저닝 고속화



### 가상 머신(VM; Virtual Machine)

- 소프트웨어 형식으로 물리적 컴퓨팅을 시뮬레이션하는 가상 환경



### 하이퍼바이저(Hypervisor)

- VM을 코디네이션하는 소프트웨어 계층

- VM과 기반 물리적 하드웨어 사이의 인터페이스 역할을 수행

- 실행에 필요한 물리 리소스에 액세스할 수 있게끔 보장

- 메모리공간, 컴퓨팅 사이클에 영향을 줘서 VM간 상호 간섭이 없게끔 보장

- 유형

  - 유형1: "베어메탈 "하이퍼바이저

    - 기반 물리적 리소스와 상호작용함으로써 기존 운영체제를 모두 대체

    ```mermaid
    flowchart TD
    A((OS)) <--> B
    B[/HYPERVISOR\] <--> C
    C{{HARDWARE}}
    ```

    

  - 유형2 하이퍼바이저

    - 기존 OS에서 애플리케이션으로 실행됨

      ```mermaid
      flowchart TD
      A((OS)) <--> B
      B[/HYPERVISOR\] <--> D
      D[/OS/] <--> C
      C{{HARDWARE}}
      ```

      



### 가상화의 유형

- 데스크탑 가상화
- 네트워크 가상화
- 스토리지 가상화
- 데이터 가상화
- 애플리케이션 가상화
- 데이터 센터 가상화
- CPU 가상화
- GPU 가상화
- Linux 가상화
- 클라우드 가상화



### 가상화 vs 컨테이너화

- 서버 가상화는 하드웨어에서 전체 컴퓨터를 재생성
  - 전체 OS를 다시 실행
  - OS는 하나의 애플리케이션 실행
- 컨테이너화
  - 애플리케이션 & 애플리케이션이 의존하는 소프트웨어 라이브러리 & 환경 변수 등만 실행하며
    기반 OS 커널을 공유함
  - 보다 작고, 배치 속도가 빠름
    - 일부분만 추상화, 경량화된 가상화 기술



### CDN

- CDN; Content Delivery Network
  - 지리적 제약 없이 전 세계 사용자에게 빠르고 안전하게 컨텐츠를 전송할 수 있는 컨텐츠 전송 기술
  - 서버와 사용자 사이의 물리적인 거리를 줄여 컨텐츠 로딩에 소요되는 시간을 최소화
  - 각 지역에 캐시 서버(PoP; Points of Presence)를 분산 배치
    - 근접 사용자의 요청에는 원본 서버가 아닌 캐시 서버가 컨텐츠 전달
    - Original Server와 Edge Server

- CDN이 필요한 경우
  - 인터넷을 통해 비즈니스를 운영
  - 웹 사이트에서 이미지, 동영상 파일 등의 컨텐츠를 제공할 경우



### 스냅샷

- 스냅샷(snapshot)
  - 특정 시점의 스토리지 파일 시스템을 포착해 보관
  - 장애나 데이터 손상 시 스냅샷을 생성한 시점으로 데이터를 복구
- 백업과 스냅샷
  - 백업의 경우 원본 데이터를 그대로 복사해 다른 곳에 저장
  - 스냅샷은 백업과 달리 초기 생성 / 데이터 변경이 있기 전에는 스토리지 공간을 차지하지 않음
- 메타데이터의 복사본에 해당하기에
  - 생성 시간이 짧고
  - 빠르게 데이터 복원 가능



### 데이터 센터와 리전

- Data Center
  - 수 많은 서버들을 한데 모아 네트워크로 연결해 놓은 시설
  - IDC; Internet Data Center
  - CDC; Cloud Data Center

- Region
  - 데이터 센터가 위치한 지역
  - 대상 고객의 지역과 자원을 생성할 리전이 최대한 가까워야
  - 국가마다 자원 생성 비용이 다름



#### AZ; Availability Zone(가용 영역)

- 하나의 리전은 둘 이상의 AZ로 구성됨