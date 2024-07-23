package com.example.redis.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BoardRepository { // 테스트용이니 자료구조에 그냥 저장

    private final List<Board> boardList = new ArrayList<>();

    public void putBoard(Board board) {
        delete(board.getId());
        this.boardList.add(board);
    }

    public void delete(Long id) {
        boardList.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .ifPresent(boardList::remove);
    }

    public Board findById(Long id) {
        return boardList.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("invalid board id"));
    }

    public List<Board> findAll() {
        return boardList;
    }
}
