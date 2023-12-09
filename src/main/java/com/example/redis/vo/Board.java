package com.example.redis.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Board {

    private Long id;
    private String title;
    private String content;

    public void merge(Board board) {
        this.title = board.title;
        this.content = board.content;
    }

    @Builder
    public Board(Long id, String title, String content) {
        this.id = id;
        this.title =title;
        this.content = content;
    }
}
