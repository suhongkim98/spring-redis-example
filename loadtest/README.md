# 준비

```
docker compose up -d influxdb grafana
```

# 시작

```
docker compose run k6 run /scripts/load.js
```

# 그라파나에서 인플럭스디비 연동 및 패널에서 메트릭 지표 조회

```
http://localhost:3000
```
