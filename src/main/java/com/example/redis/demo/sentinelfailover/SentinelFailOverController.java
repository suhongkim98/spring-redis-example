package com.example.redis.demo.sentinelfailover;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 시나리오
 * 대량의 유저가 랜덤으로 Redis 에 데이터 생성, 삭제, 읽기를 시도한다. (부하테스트 도구 활용)
 * 도중에 레디스 센티널 마스터 노드가 여러번 죽는다.
 * 페일오버 후 Redis 데이터 정합성 확인
 */
@RestController
@RequiredArgsConstructor
public class SentinelFailOverController {

    private final SentinelFailOverService sentinelFailOverService;

    @PostMapping("/sentinel-test")
    public void sentinelFailOverTestCreate() {
        sentinelFailOverService.create();
    }

    @DeleteMapping("/sentinel-test")
    public void sentinelFailOverTestDelete() {
        sentinelFailOverService.delete();
    }

    @GetMapping("/sentinel-test")
    public List<String> sentinelFailOverTestRead() {
        return sentinelFailOverService.read();
    }
}
