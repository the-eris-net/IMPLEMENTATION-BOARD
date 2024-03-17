package org.example.springimplementationboard.comment;

public record CommentRequest(
        Long id,
        Long boardId,
        String body
) {
}
