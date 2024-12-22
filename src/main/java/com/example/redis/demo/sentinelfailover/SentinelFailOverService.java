package com.example.redis.demo.sentinelfailover;

import com.example.redis.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SentinelFailOverService {

    private static final String SF_PREFIX = "sf";
    private static final String SF_IDX_KEY = SF_PREFIX + ":idx";
    private static final String SF_IDX_LIST_KEY = SF_IDX_KEY + ":list";

    private final StringRedisTemplate stringRedisTemplate;

    // 레디스 List 자료구조 우측으로 인덱스 추가 및 대상 인덱스에 데이터 생성
    public void create() {
        // 인덱스 생성
        Long idx = stringRedisTemplate.opsForValue().increment(SF_IDX_KEY);

        // 데이터 생성
        String dataKey = RedisUtils.join(SF_PREFIX, idx);
        Long data = System.currentTimeMillis();
        stringRedisTemplate.opsForValue().set(dataKey, String.valueOf(data));

        // 인덱스 추가
        stringRedisTemplate.opsForList().rightPush(SF_IDX_LIST_KEY, String.valueOf(idx));
    }

    // 레디스 List 자료구조 좌측부터 인덱스 및 데이터 삭제
    public void delete() {
        // 리스트 좌측 인덱스 조회 및 삭제
        String idx = stringRedisTemplate.opsForList().leftPop(SF_IDX_LIST_KEY);
        if (!StringUtils.hasText(idx)) {
            log.warn("리스트가 비어있음");
            return;
        }

        // 데이터 삭제
        String dataKey = RedisUtils.join(SF_PREFIX, idx);
        stringRedisTemplate.delete(dataKey);
    }

    // 인덱스 전체 조회
    public List<String> read() {
        List<String> list = stringRedisTemplate.opsForList().range(SF_IDX_LIST_KEY, 0, -1);
        if (Objects.isNull(list)) {
            return new ArrayList<>();
        }
        return list;
    }
}
