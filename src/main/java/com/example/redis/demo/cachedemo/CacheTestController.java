package com.example.redis.demo.cachedemo;

import com.example.redis.domain.Board;
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
        return id;
    }

    @PutMapping("/boards/{id}")
    public void updatedBoard(@PathVariable(value = "id") Long id) {
        cacheTestService.updateBoard(id, "hello22", "world");
    }

    @DeleteMapping("/boards/{id}")
    public void deleteBoard(@PathVariable(value = "id") Long id) {
        cacheTestService.deleteById(id);
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
