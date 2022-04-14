# MySQL 총정리

## SELECT

```SQL
SELECT 필드명1, 필드명2, ...
FROM 테이블명
WHERE 선택조건
GROUP BY 필드명1, 필드명2, ...
ORDER BY 필드명 [ASC|DESC]
LIMIT [N|1, N];
```

DISTINCT: 중복을 세지 않음(필드 앞에)

IFNULL(A, B): A컬럼의 NULL인 테이블은 B로 채워서 반환



DATE_FORMAT(대상 컬럼, 정규식)



조인





트랜잭션



뷰



Stored 프로시저



Stored Function



Trigger



Index