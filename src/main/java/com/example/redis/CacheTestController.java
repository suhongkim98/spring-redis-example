package com.example.redis;

import com.example.redis.vo.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CacheTestController {

    private final CacheTestService cacheTestService;

    @PostMapping("/boards")
    public Long createBoard() {
        Long id = cacheTestService.createBoard("hello", "world");
        cacheTestService.clearBoardsCache();
        return id;
    }

    @PutMapping("/boards/{id}")
    public void updatedBoard(@PathVariable(value = "id") Long id) {
        cacheTestService.updateBoard(id, "hello22", "world");
        cacheTestService.clearBoardsCache();
    }

    @DeleteMapping("/boards/{id}")
    public void deleteBoard(@PathVariable(value = "id") Long id) {
        cacheTestService.deleteById(id);
        cacheTestService.clearBoardsCache();
    }

    @GetMapping("/boards/{id}")
    public Board getBoard(@PathVariable(value = "id") Long id) {
        return cacheTestService.findById(id);
    }

    @GetMapping("/boards/cacheable/{id}")
    public Board getBoardCacheable(@PathVariable(value = "id") Long id) {
        return cacheTestService.findByIdCacheable(id);
    }

    @GetMapping("/boards")
    public List<Board> getBoards() {
        return cacheTestService.findAll();
    }

    @GetMapping("/boards/cacheable")
    public List<Board> getBoardsCacheable() {
        return cacheTestService.findAllCacheable();
    }

    @DeleteMapping("/boards/cacheable")
    public void deleteCacheList() {
        cacheTestService.clearBoardsCache();
    }
}
