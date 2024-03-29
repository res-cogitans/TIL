## 빌드

- 고급 언어를 컴퓨터가 이해할 수 있는 기계어(Machine Code)로 변환하는 과정
- 빌드 과정(build process)에는 여러 종류가 있다:
  - 컴파일 방식
  - 인터프리터 방식
  - 하이브리드 방식




### 컴파일

- 작성한 소스코드를 한꺼번에 번역하여 실행파일로 만듦

- preprocessing -> compilation -> assemble -> linking

- 전처리(preprocessing)

  - main이 실행되기 이전에 실행하는 작업
  - `#include`

- 컴파일(compilation)

  - 컴파일러: 컴파일하는 프로그램

  - 저수준 언어 / 중간 언어로 번역: 다수의 경우 어셈블리어

    - 광의의 컴파일: 전처리 ~ 어셈블: 목적 파일을 생성하는 과정

      이 경우 빌드 = 컴파일 + 링크

- 어셈블(assemble)
  - 어셈블러: 어셈블리어 -> 기계어
  - 목적 파일(Object File) 생성: 목적 파일은 아직 실행 가능한 상태는 아님
- 링크/링킹(linking)
  - Object Files를 라이버리와 연결,
  - 실행 가능한 파일: executable file로 만듦 --> .exe

- 컴파일 언어

  - 빌드가 완료된 파일은 번역 없이 실행 가능 --> 실행 속도 빠름
    - JVM의 경우는?

  - 대형 프로젝트일수록 전체 빌드 시간이 오래 걸림 -> 작은 변화를 반영하기가 힘듦
    - InteliJ의 경우 html 파일은 별개 였던것으로 기억함 -> 자바가 컴파일 언어니까
  - 플램폼 의존적 -> cpu 의존성, OS 마다 다른 라이브러리 때문
    - 자바 및 JVM의 장점



### 인터프리트

- 한 명령 단위마다 기계어로 번역, 실행
- 목적 파일의 생성 없음
- 인터프리터: 번역해주는 프로그램, 환경
- 인터프리터 언어
  - 플랫폼 독립적: 인터프리터만 깔면 끝
  - 수정에 유리
  - 컴파일 언어보다 느림
  - 보안성이 안 좋음
    - exe로 만드는 컴파일 방식의 경우
      - 파일을 역 엔지니어링 하긴 힘들고
      - 역 엔지니어링 한다 해도 원본 소스 코드가 그대로 나오지는 않기에 보안성이 확보되지만
    - 인터프리트 방식은 컴파일 방식에 비해 보안성이 떨어질 수 밖에 없다.



### 하이브리드

- 바이트 코드 언어(Byte Code Language)
- 자바!
- 고급 언어 -> 바이트 코드(bytecode): 컴파일
- VM이 바이트코드 -> 기계어: 런타임
- 바이트코드: 일종의 중간 언어
  - 컴파일 언어와 달리 하드웨어가 직접 처리하지 않고 가상 머신이 처리
- VM
  - JVM, CLR(C# .NET)
  - 컴파일과 혼용되는 JIT
- 컴파일 언어와 달리 하드웨어 직접 못 건드림
- execute(code, os)라 생각하면 쉽겠네



## 자바의 빌드 툴

- 빌드를 자동화하기 위한 툴

- 종류
  - Ant
    - 과거에 많이 사용했음
    - 개발자마다 작성하는 Ant 스크립트가 조금씩 다르기 때문에 표준화된 스크립트에 밀림
  - Maven: 자바 특화
  - Gradle: 메이븐보다 더 간결한 문법 제공
  - 현재는 Maven, Gradle 많이 사용하며 Maven -> Gradle로 이동하는 추세다.
- 의존성 관리
  - 자바 애플리케이션을 개발하는 데 필요한 외부 라이브러리(jar)를 내려받아주는 역할
  - 메이븐/그레이들의 transitive dependencies
    - 외부 라이브러리에 딸려 오는 의존성까지 관리해줌



## 참고 자료

- [Stranger's LAB](https://st-lab.tistory.com/176) 의 프로그래밍 언어와 빌드 과정 [Build Process]

  빌드 개념에 대한 전반적 정보 + 빌드 과정의 종류에 대해 잘 설명되어 있습니다.
