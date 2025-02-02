package com.example.redis;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;
import java.util.Objects;

/**
 * https://redis.io/docs/manual/pipelining/
 */
@SpringBootTest
@Import({TestRedisConfig.class})
public class RedisPipeliningTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String givenKey = "item_list";

    @AfterEach
        // 모든 필드 데이터 삭제
    void destroy() {
        stringRedisTemplate.delete(givenKey); // 키 자체 삭제
    }

    @Test
    void testExecutePipeline() {
        // given
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        List<String> items = List.of("hello1", "hello2", "hello3");

        // when
        RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
        stringRedisTemplate.executePipelined((RedisCallback<String>) connection -> {
            for (var item : items) {
                connection.listCommands().rPush(Objects.requireNonNull(serializer.serialize(givenKey)),
                        serializer.serialize(item));
            }
            return null;
        });

        // then
        List<String> result = listOperations.range(givenKey, 0, -1);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(items.size(), result.size());

        // 파이프라이닝 내 명령어 실행 순서는 순서가 보장됨
        Assertions.assertEquals(items.get(0), result.get(0));
        Assertions.assertEquals(items.get(1), result.get(1));
        Assertions.assertEquals(items.get(2), result.get(2));
    }
}
