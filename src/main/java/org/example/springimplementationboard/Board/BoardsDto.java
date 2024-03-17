package org.example.springimplementationboard.Board;

import java.util.List;

public record BoardsDto(
        List<BoardDto> boards,
        Long cursor,
        boolean hasMore
) {
}
