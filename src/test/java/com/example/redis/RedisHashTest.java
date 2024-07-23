package com.example.redis;

import com.example.redis.domain.Board;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class RedisHashTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    private final String testRedisKey = "t_test";

    @BeforeEach // 데이터 10개 세팅
    void setup() throws JsonProcessingException {
        HashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash();

        for (int i = 0 ; i < 10 ; i++) {
            Long id = System.currentTimeMillis();
            Board board = Board.builder()
                    .id(id)
                    .build();

            hashOps.put(testRedisKey, String.valueOf(i), objectMapper.writeValueAsString(board));
        }
    }

    @AfterEach // 모든 필드 데이터 삭제
    void destroy() {
        stringRedisTemplate.delete(testRedisKey); // 키 자체 삭제
    }

    @Test
    @DisplayName("delete 으로 특정 필드 or 전체 필드 삭제 가능")
    void testDelete() {
        // given
        HashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash();
        String redisKey = "test_key";
        Map<String, String> map = new HashMap<>();
        map.put("a", "a");
        map.put("b", "b");
        hashOps.putAll(redisKey, map);

        // when
        hashOps.delete(redisKey, hashOps.keys(redisKey).toArray()); // 모든 필드 삭제
        // stringRedisTemplate.delete(redisKey);

        // then
        Map<String, String> result = hashOps.entries(redisKey);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("entries 로 해시키에 대해 Map 자료구조로 모든 필드 조회 가능, HGETALL (key)")
    void testEntries() {
        // given
        HashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash();

        // when
        Map<String, String> map = hashOps.entries(testRedisKey);

        // then
        Assertions.assertNotEquals(0, map.entrySet().size());
    }

    @Test
    @DisplayName("get 으로 해시키에 대해 특정 필드 데이터 조회 가능, HGET (key) (field)")
    void testGet() {
        // given
        HashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash();

        // when
        String value = hashOps.get(testRedisKey, "1");

        // then
        Assertions.assertNotNull(value);
        Assertions.assertNotEquals(0, value.length());
    }

    @Test
    @DisplayName("put 메서드는 필드에 기존 데이터가 있다면 value를 덮어 씌운다, HSET (key) (field) (val)")
    void testPut() {
        // given
        HashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash();
        String origin = "origin";
        String after = "after";
        String hashKey = "test_key";
        hashOps.put(testRedisKey, hashKey, origin);

        // when
        hashOps.put(testRedisKey, hashKey, after);

        // then
        String result = hashOps.get(testRedisKey, hashKey);
        Assertions.assertEquals(after, result);
    }

    @Test
    @DisplayName("putAll 메서드는 없으면 신규 추가, 있으면 덮어 씌운다.")
    void testPutAll() {
        // given
        String hashKey = "test2";
        HashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash();
        Map<String, String> origin = new HashMap<>();
        origin.put("a_hash_key", "a");
        origin.put("b_hash_key", "b");
        origin.put("c_hash_key", "c");

        hashOps.putAll(hashKey, origin);

        // when
        Map<String,String> newDataMap = new HashMap<>();
        newDataMap.put("d_hash_key", "d");
        newDataMap.put("d_hash_key", "d2");
        hashOps.putAll(hashKey, newDataMap);

        // then
        Map<String, String> map = hashOps.entries(hashKey);
        Assertions.assertEquals(4, map.entrySet().size()); // 총 데이터 4개
        Assertions.assertEquals(map.get("d_hash_key"), "d2"); // d_hash_key 는 덮어씌워져서 d2여야함
    }
}
