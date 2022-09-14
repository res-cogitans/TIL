# DP; Dynamic Programming

- 동적 계획법
  - Richard. E. Bellman
  - dynamic 이라는 말의 의미와는 딱히 상관 없음
- **중복되는 부분 문제**
  - 분할 정복처럼 문제를 더 작은 문제로 나누어 해결함
  - 동적 계획법의 경우 **부분 문제의 결과를 재사용**하여 속도 향상을 꾀함
    - 캐시(cache): 이미 계산한 값을 저장해 두는 장소
    - 중복되는 부분 문제(overlapping subproblems): 두 번 이상 계산되는 부분 문제
- 조합 폭발(combinatorial explosion)
  - 분할의 깊이가 깊어질수록 계산 중복 회수가 지수적으로 증가
  - 동적 계획법이 유용한 이유
- 메모이제이션(memoization)
  - 기존 계산 결과를 재사용
  - 참조적 투명 함수(referntial transparent function)에만 적용 가능
    - 참조적 투명성(referential transparency)을 가진 함수
    - 입력이 고정되어 있을 때 결과가 항상 같음