package com.example.redis;

import com.example.redis.domain.Board;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * StringRedisTemplate 은 직렬화 역직렬화 시 String 사용
 * 문자열에 특화된 템플릿 제공
 */
@SpringBootTest
public class StringRedisTemplateTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * redisTemplate에는 redis가 제공하는 list, set, sortedSet, hash... 와 같은 다양한 command를 지원하기 위한 opsFor* method가 있다.
     */
    @Test
    void testOpsFor() {
        /**
         * opsForValue	Strings를 쉽게 Serialize / Deserialize 해주는 Interface
         * opsForList	List를 쉽게 Serialize / Deserialize 해주는 Interface
         * opsForSet	Set를 쉽게 Serialize / Deserialize 해주는 Interface
         * opsForZSet	ZSet를 쉽게 Serialize / Deserialize 해주는 Interface
         * opsForHash	Hash를 쉽게 Serialize / Deserialize 해주는 Interface
         */
        // given
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String givenKey = "testKey";
        String givenValue = "testValue";

        // when
        stringValueOperations.set(givenKey, givenValue);

        // then
        String value = stringValueOperations.get(givenKey);
        Assertions.assertEquals(givenValue, value);
    }

    // 확장성을 위해 객체도 jsonString 으로 변환하는 방식으로 StringRestTemplate 으로 처리하는 것이 좋아보임
    // (나중엔 자바 애플리케이션이 아닐 수 있기에)
    @Test
    void testSerializeObjectToString() throws JsonProcessingException {
        // given
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        Board board = Board.builder()
                .id(1L)
                .title("title")
                .build();
        String givenKey = "item";

        // when
        String jsonString = objectMapper.writeValueAsString(board);
        stringValueOperations.set(givenKey, jsonString);
        Board result = objectMapper.readValue(stringValueOperations.get(givenKey), Board.class);

        // then
        Assertions.assertEquals(board, result);
    }
}
