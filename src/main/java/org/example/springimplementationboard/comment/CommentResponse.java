package org.example.springimplementationboard.comment;

import java.util.List;

public record CommentResponse(
        List<CommentDto> data
) {
}
