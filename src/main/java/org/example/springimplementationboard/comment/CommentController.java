package org.example.springimplementationboard.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("")
    public CommentResponse createComment(@RequestBody CommentRequest commentRequest) {
        CommentDto dto = commentService.createComment(new CommentDto(null, commentRequest.boardId(), commentRequest.body()));
        return new CommentResponse(List.of(dto));
    }

    @DeleteMapping("/{id}")
    public CommentResponse deleteComment(@PathVariable Long id) {
        commentService.deleteBoard(id);
        return new CommentResponse(List.of());
    }

    @PatchMapping("")
    public CommentResponse updateBoard(@RequestBody CommentRequest commentRequest) {
        CommentDto board = commentService.updateComment(new CommentDto(commentRequest.id(), commentRequest.boardId(), commentRequest.body()));
        return new CommentResponse(List.of(board));
    }
}
