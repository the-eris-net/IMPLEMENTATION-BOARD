package org.example.springimplementationboard.Board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springimplementationboard.comment.CommentDto;
import org.example.springimplementationboard.common.exception.DataNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardDto createBoard(BoardDto dto) {
        BoardEntity boardEntity = new BoardEntity(null, dto.title(), dto.body());
        boardRepository.save(boardEntity);
        return new BoardDto(boardEntity.getId(),
                boardEntity.getTitle(),
                boardEntity.getBody(),
                getCommentsByBoardEntity(boardEntity, true));
    }

    /**
     * pageable대신 직접 넣어서 내부에서 Pageable초기화하는 방법도 가능
     */
    public List<BoardDto> getBoardsByOffset(Pageable pageable, boolean hasBody) {
        Page<BoardEntity> pageBoardEntities = boardRepository.findAll(pageable);
        List<BoardDto> boards = pageBoardEntities
                .filter(board -> !board.isDeleted())
                .map(boardEntity -> new BoardDto(boardEntity.getId(),
                        boardEntity.getTitle(),
                        getBody(boardEntity, hasBody),
                        getCommentsByBoardEntity(boardEntity, hasBody)))
                .toList();
        log.info("page boards: {}", pageBoardEntities);
        return boards;
    }

    public BoardsDto getBoardsByCursor(Long cursor, int size, Sort sort, boolean body) {
        Pageable pageable = PageRequest.of(0, size, sort);
        Slice<BoardEntity> sliceBoardEntities = boardRepository.findAllByIdLessThan(cursor, pageable);
        List<BoardDto> boards = sliceBoardEntities
                .stream()
                .filter(board -> !board.isDeleted())
                .map(boardEntity -> new BoardDto(boardEntity.getId(),
                        boardEntity.getTitle(),
                        getBody(boardEntity, body),
                        getCommentsByBoardEntity(boardEntity, body)))
                .toList();
        log.info("slice boards: {}", sliceBoardEntities);
        return new BoardsDto(boards, getFirstBoardId(boards), sliceBoardEntities.hasNext());
    }

    private static Long getFirstBoardId(List<BoardDto> boards) {
        if (boards != null && !boards.isEmpty()) {
            return boards.get(0).id();
        }
        return null;
    }

    public BoardDto getBoardById(Long id) {
        BoardEntity boardEntity = boardRepository
                .findById(id)
                .filter(board -> !board.isDeleted())
                .orElseThrow(() -> new DataNotFoundException(id, "Board not found"));
        return new BoardDto(boardEntity.getId(),
                boardEntity.getTitle(),
                boardEntity.getBody(),
                getCommentsByBoardEntity(boardEntity, true));
    }

    public BoardDto updateBoard(BoardDto dto) {
        BoardEntity existBoard = boardRepository
                .findById(dto.id())
                .orElseThrow(() -> new DataNotFoundException(dto.id(), "Board not found"));
        if (existBoard.isDeleted()) {
            throw new DataNotFoundException(dto.id(), "Board is deleted");
        }

        BoardEntity updatedBoard = boardRepository.save(makeBoardEntityByExistBoardAndBoardDto(dto, existBoard));
        return new BoardDto(updatedBoard.getId(),
                updatedBoard.getTitle(),
                updatedBoard.getBody(),
                getCommentsByBoardEntity(updatedBoard, true));
    }

    public void deleteBoard(Long id) {
        BoardEntity existBoard = boardRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException(id, "Board not found"));
        if (existBoard.isDeleted()) {
            throw new DataNotFoundException(id, "Board is deleted");
        }
        BoardEntity deletedBoard = new BoardEntity(existBoard.getId(), existBoard.getTitle(), existBoard.getBody(), true);
        boardRepository.save(deletedBoard);
    }

    private String getBody(BoardEntity board, boolean hasBody) {
        if (hasBody) {
            return board.getBody();
        }
        return null;
    }


    private List<CommentDto> getCommentsByBoardEntity(BoardEntity board, boolean hasBody) {
        if (hasBody) {
            return board.getComments()
                    .stream()
                    .filter(commentEntity -> !commentEntity.isDeleted())
                    .map(commentEntity -> new CommentDto(commentEntity.getId(), null, commentEntity.getBody()))
                    .toList();
        }
        return null;
    }

    private BoardEntity makeBoardEntityByExistBoardAndBoardDto(BoardDto dto, BoardEntity existBoard) {
        return new BoardEntity(existBoard.getId(),
                dto.title() == null ? existBoard.getTitle() : dto.title(),
                dto.body() == null ? existBoard.getBody() : dto.body(),
                existBoard.isDeleted());
    }
}
