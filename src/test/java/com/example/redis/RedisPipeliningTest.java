package com.example.redis;

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


    @Test
    void testExecutePipeline() {
        // given
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        String givenKey = "item_list";
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
    }
}
