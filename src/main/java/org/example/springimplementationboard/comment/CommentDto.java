package org.example.springimplementationboard.comment;


public record CommentDto(
        Long id,
        Long boardId,
        String body
) {
}
