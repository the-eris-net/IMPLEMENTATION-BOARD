package org.example.springimplementationboard.comment;

import lombok.RequiredArgsConstructor;
import org.example.springimplementationboard.Board.BoardEntity;
import org.example.springimplementationboard.common.exception.DataNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentDto createComment(CommentDto commentDto) {
        CommentEntity comment = new CommentEntity(commentDto.id(), commentDto.body(), false, new BoardEntity(commentDto.boardId()));
        commentRepository.save(comment);
        System.out.println(comment);
        return new CommentDto(comment.getId(), comment.getBoard().getId(), comment.getBody());
    }

    public void deleteBoard(Long id) {
        CommentEntity existComment = commentRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException(id, "Comment not found"));
        if (existComment.isDeleted()) {
            throw new DataNotFoundException(id, "Comment is deleted");
        }
        CommentEntity deletedComment = new CommentEntity(existComment.getId(), existComment.getBody(), true, existComment.getBoard());
        commentRepository.save(deletedComment);
    }

    public CommentDto updateComment(CommentDto commentDto) {
        CommentEntity existComment = commentRepository
                .findById(commentDto.id())
                .orElseThrow(() -> new DataNotFoundException(commentDto.id(), "Comment not found"));
        if (existComment.isDeleted()) {
            throw new DataNotFoundException(commentDto.id(), "Comment is deleted");
        }
        CommentEntity updatedComment = commentRepository.save(makeCommentEntityByExistCommentAndCommentDto(commentDto, existComment));
        return new CommentDto(updatedComment.getId(), updatedComment.getBoard().getId(), updatedComment.getBody());
    }

    private CommentEntity makeCommentEntityByExistCommentAndCommentDto(CommentDto dto, CommentEntity existComment) {
        return new CommentEntity(existComment.getId(),
                dto.body() == null ? existComment.getBody() : dto.body(),
                existComment.isDeleted(),
                existComment.getBoard());
    }
}
