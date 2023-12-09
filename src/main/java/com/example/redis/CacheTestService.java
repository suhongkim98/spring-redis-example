package com.example.redis;

import com.example.redis.vo.Board;
import com.example.redis.vo.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * **@Cacheable**
 * : 캐시가 존재하지 않으면 메서드가 실행되고 리턴되는 데이터가 캐시에 저장됩니다. 만약 존재하면 메서드를 실행하지 않고 캐시된 값이 반환됩니다.
 * <p>
 * **@CachePut**
 * : 캐시에 데이터를 넣거나 수정시 사용하며, 메서드의 리턴값이 캐시에 없으면 저장하고 있을 경우 갱신합니다.
 * <p>
 * **@CacheEvict**
 * : 캐시를 삭제합니다.
 * <p>
 * **@Caching**
 * : 여러개의 캐시 어노테이션을 실행해야 할 때 사용합니다.
 */

/*
@Cacheable 어노테이션을 Service 혹은 Controller 에 추가가능

@Cacheable(value = "TestVo", key = "#p0", cacheManager = "cacheManager", unless = "#p0 == ''", condition = "#p0.length > 2")

- value = "TestVo" :** 캐시의 이름을 지정한다.
- key = "#p0" :** 캐시 키 지정, id에 따라 응답값이 달라지므로 저장될 Key로 id 파라미터 값을 선언, p0은 첫번째 파라미터
- cacheManager = "cacheManager" :** 위의 config에서 작성한 cacheManager 사용
- unless = "#p0 == ''" :** id가 "" 일때 캐시를 저장하지 않음
- condition = "#p0.length > 2" :** id의 length가 3 이상일 때만 캐시 저장
 */
@Service
@RequiredArgsConstructor
public class CacheTestService {

    private final BoardRepository boardRepository;

    public Long createBoard(String title, String content) {
        Long id = System.currentTimeMillis();
        Board board = Board.builder()
                .id(id)
                .title(title)
                .content(content)
                .build();

        boardRepository.putBoard(board);
        return id;
    }

    @CachePut(value = "board", key = "#p0") // 주석 빼면 업데이트 시 캐시에 갱신 안해줌
    public Board updateBoard(Long id, String title, String content) {
        Board origin = boardRepository.findById(id);
        Board board = Board.builder()
                .title(title)
                .content(content)
                .build();

        origin.merge(board);
        return origin;
    }


    public void deleteById(Long id) {
        boardRepository.delete(id);
    }

    public Board findById(Long id) {
        return boardRepository.findById(id);
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    @CacheEvict(value = "board", key = "#p0")
    public void deleteByIdWithCacheEvict(Long id) {
        deleteById(id);
    }

    @CacheEvict(value = "boards")
    public void clearBoardsCache() {

    }

    /*
    @Cacheable 어노테이션은 value 속성을 통해 캐시의 이름을 지정하고,
    key 속성을 통해 캐시 키를 생성합니다.
    즉, findById() 메서드의 인자인 id 값이 캐시 키가 됩니다.
    이후 동일한 id 값으로 호출되는 findById() 메서드는 캐시된 결과를 반환합니다.
     */
    @Cacheable(value = "board", key = "#p0")
    public Board findByIdCacheable(Long id) {
        return findById(id);
    }

    @Cacheable(value = "boards")
    public List<Board> findAllCacheable() {
        return findAll();
    }
}
