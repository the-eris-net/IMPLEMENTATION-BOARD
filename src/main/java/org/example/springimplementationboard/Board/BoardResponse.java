package org.example.springimplementationboard.Board;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record BoardResponse(
        List<BoardDto> data,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Boolean hasMore
) {

    public BoardResponse(List<BoardDto> data) {
        this(data, null);
    }

    public static BoardResponse of(BoardsDto boardsDto) {
        return new BoardResponse(boardsDto.boards(), boardsDto.hasMore());
    }
}
