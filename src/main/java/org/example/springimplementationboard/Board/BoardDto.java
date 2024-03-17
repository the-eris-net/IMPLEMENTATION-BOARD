package org.example.springimplementationboard.Board;

import org.example.springimplementationboard.comment.CommentDto;

import java.util.List;

public record BoardDto(
        Long id,
        String title,
        String body,
        List<CommentDto> comments
) {
}
