# spring-data-redis

`Redis` 데모 프로젝트입니다.

## 데모
* Redis Sentinel docker compose 구성됨
* `Cache` 데모

## 테스트코드
* 파이프라이닝 데모

# 시작하기
센티널 없이 싱글 레디스로 테스트 필요 시 `RedisConfig` 수정 및 `infra/forlocal/docker-compose.yml`을 구동
* `Redis Instance Port`: 5100, 5101, 5102
* `Redis Sentinel Port`: 5150, 5151, 5152
## 1. 로컬 도커 구동
데모앱에 코드 수정이 있었을 경우 이미지 삭제

## 2. DemoApp 빌드 및 RedisSentinel 구동
데모앱은 레디스-센티널 구성으로 연동되므로 컨테이너로 구동하여 테스트 필요
```bash
cd /infra
docker compose up -d
```

## 3. 마스터 노드에서 replication 정보 확인
```bash
info replication
```
connected slaves 수 확인

## 4. 센티널 노드에서 sentinel 정보 확인
```bash
info sentinel
sentinel masters
```

# 레디스 마스터 노드 임의 종료하는 등 failover 테스트 해보기