# ethereum-indexer

Ethereum 메인넷의 데이터를 인덱싱해서  
**특정 날짜 기준으로 계정의 ETH 및 ERC20 토큰 잔고·거래 내역을 조회**할 수 있는 인덱서입니다.

---

## 개요
- Ethereum 메인넷의 블록 데이터를 주기적으로 수집·저장합니다.
- 각 날짜별 **마지막 블록 번호**를 `ethereum_daily_blocks` 테이블에 저장해, 과거 시점 기준 잔고/거래 조회를 가능하게 합니다.
- 계정 주소, 토큰 주소, 날짜를 기준으로 ETH 및 ERC20 잔고와 해당 날짜의 거래 내역을 조회하는 API를 제공합니다.

---

## 요구사항
- **입력**
  - `account` : 조회 대상 계정 주소 (Ethereum Address)
  - `token_address` : `ETH` 혹은 ERC20 토큰 주소 (예: `0xae7a...D7fE84`)
  - `date` : UTC+0 기준 `YYYYMMDD` 형식의 일자 (예: `20241201`)
- **출력**
  - JSON 형식으로 잔액 및 입출금 내역을 반환합니다.
  - 예시:
    ```json
    {
      "balance": "잔액 (소수점 포함)",
      "transfer_history": [
        {
          "from": "발신 주소",
          "to": "수신 주소",
          "value": "전송된 토큰 값 (소수점 포함)",
          "timestamp": "Unix Timestamp"
        }
      ]
    }
    ```
- **동작 조건**
  - 입력받은 `date` 기준 **해당 일의 마지막 블록**에서 계정의 ETH 또는 ERC20 잔액을 계산합니다.
  - 같은 날짜의 **입출금 내역(전송 이벤트)** 을 함께 조회해 반환합니다.
  - ERC20 토큰의 잔액 및 전송 값은 **소수점 4자리까지** 표기합니다.
  - **최대 최근 7일 이내**의 데이터만 조회 대상으로 합니다.
  - Infura API를 사용해 온체인 데이터를 조회하며, 예상치 못한 입력값에 대한 예외 처리 및 조회 성능을 고려해 설계했습니다.

> 보유량 추적 API 및 데이터 적재 방식에 대한 상세 설계 문서는 [여기](https://picturesque-whimsey-543.notion.site/API-16a13ef34b9b80c2ae2afe80c03e8d08)에서 확인할 수 있습니다.

---

## 주요 기능
- **블록 및 트랜잭션 인덱싱**
  - 스케줄러가 최신 블록까지 순차적으로 `block / transaction / log / ERC20 transfer` 데이터를 적재합니다.
- **일별 마지막 블록 관리**
  - 날짜 범위를 순회하며, 각 날짜의 가장 마지막 블록을 조회해 `ethereum_daily_blocks` 테이블에 저장합니다.
  - 이 블록 번호를 기준으로 “해당 날짜 기준 잔고/거래 내역”을 계산합니다.
- **계정 토큰 정보 조회 API**
  - 계정 주소 + 토큰 주소 + 날짜를 기준으로
    - 잔고
    - 소수점 자릿수
    - 해당 날짜의 거래 내역(ETH 또는 ERC20)
    를 조회하는 API를 제공합니다.
- **캐싱**
  - Redis 캐시를 이용해 동일한 요청에 대한 응답 속도를 높입니다.

---

## 기술 스택
- Java 17, Spring Boot
- JPA(Hibernate), PostgreSQL
- Redis
- Gradle
- Infura (Ethereum 메인넷 RPC Provider)

---

## 설계 포인트
- **Facade + Strategy 패턴**
  - `EvmBlockV1FacadeService`, `EvmAccountTokenV1FacadeService`로 외부 요청 흐름을 단순화합니다.
  - 체인별/토큰타입별로 `EvmChainV1Strategy`, `EvmTokenV1Strategy`를 구현해 확장성을 고려했습니다.
- **일별 마지막 블록 계산**
  - 블록 인덱싱 이후, 날짜 범위를 순회하며
  - `ethereum_block`에서 해당 날짜의 마지막 블록을 조회(`findFirstByDateOrderByTimeDesc`)하고
  - 이를 `ethereum_daily_blocks`에 저장해, 과거 시점 기준 잔고 조회가 가능하도록 했습니다.
- **테스트**
  - Facade/Service 레벨 테스트를 통해 블록 처리 플로우와 스케줄러/전략 동작을 검증했습니다.

---

## 데이터 모델 설계 요약
- **블록 데이터** : `ethereum_blocks`
  - 블록 메타데이터를 저장하며, `number`에 유니크 인덱스를 두어 블록 번호 기준 조회를 최적화했습니다.
- **일별 마지막 블록** : `ethereum_daily_blocks`
  - 날짜별 마지막 블록 번호를 저장해 **과거 일자 기준 잔고 계산의 기준점**으로 사용합니다.
- **트랜잭션/토큰 전송 내역** : `ethereum_transactions`, `ethereum_erc_20_transfers`
  - `block_date + 주소 + 성공 여부/컨트랙트 주소` 조합의 인덱스를 통해  
    “특정 일자, 특정 계정, 특정 토큰” 기준의 입출금 내역을 빠르게 조회할 수 있도록 설계했습니다.
- **로그 데이터** : `ethereum_logs`
  - Raw 로그를 저장하고, ERC-20 Transfer 이벤트 파싱 및 향후 이벤트 확장에 사용됩니다.

자세한 컬럼/인덱스 설계 및 샘플 데이터는 `docs/database.md`에서 확인할 수 있습니다.

---

## 실행 방법 (요약)

### 요구 사항
- JDK 17 이상
- PostgreSQL
- Redis

### 사전 설정
1. 프로젝트 루트에 `config/infura.yml` 생성 후 Infura API 키 설정
   ```yaml
   infura:
     apiKey: YOUR_INFURA_API_KEY
   ```

2. `src/main/resources/application.yml`에서 DB 및 Redis 설정 값 수정
   - DB: host, port, database 이름
   - Redis: host, port, password

3. 초기 데이터 적재
   - 최근 N일 동안의 블록/트랜잭션/로그/ERC20 transfer 데이터 적재
   - 이후 일별 마지막 블록 번호(`ethereum_daily_blocks`) 생성
   - 이 과정이 완료되어야 API를 통해 잔고 조회가 가능합니다.

### 실행
```bash
./gradlew bootrun
```

---

## 패키지 구조
- `client` : Infura 등 외부 시스템(RPC) 호출 클라이언트
- `config` : 애플리케이션 설정
- `controller` : REST API 엔드포인트
- `entity` : JPA 엔터티
- `exception` : 커스텀 예외 및 에러 타입
- `facade` : 여러 서비스/전략을 조합한 비즈니스 진입점
- `model` : 요청/응답/DTO/외부 API 결과 모델
- `repository` : JPA Repository 인터페이스
- `scheduler` : 블록/트랜잭션/로그 적재 스케줄러
- `service` : 비즈니스 로직
- `spec` : 클라이언트 추상화 인터페이스
- `util` : 공통 유틸리티

---

## API 문서 및 테스트
- 애플리케이션 실행 후 `http://localhost:8080/docs` 접속 시 Swagger UI에서 API 명세를 확인할 수 있습니다.
- 데이터 적재 후 `/http/accountToken.http` 파일을 이용해 예시 요청을 바로 테스트할 수 있습니다.