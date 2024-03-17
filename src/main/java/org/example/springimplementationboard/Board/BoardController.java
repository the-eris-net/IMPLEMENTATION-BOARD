package org.example.springimplementationboard.Board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("")
    public BoardResponse createBoard(@RequestBody BoardRequest boardRequest) {
        BoardDto board = boardService.createBoard(new BoardDto(null, boardRequest.title(), boardRequest.body(), List.of()));
        return new BoardResponse(List.of(board));
    }

    @GetMapping("")
    public BoardResponse getBoards(
            Pageable pageable,
            @RequestParam(defaultValue = "false") boolean body,
            Long cursor
    ) {
        if (cursor != null) {
            return getBoardsByCursor(cursor, refinePageable(pageable), body);
        }
        return getBoardsByOffset(refinePageable(pageable), body);
    }

    private Pageable refinePageable(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            return PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Order.desc("id")));
        }
        return pageable;
    }

    private BoardResponse getBoardsByOffset(Pageable pageable, boolean body) {
        List<BoardDto> boards = boardService.getBoardsByOffset(pageable, body);
        return new BoardResponse(boards);
    }

    private BoardResponse getBoardsByCursor(Long cursor, Pageable pageable, boolean body) {
        BoardsDto boards = boardService.getBoardsByCursor(cursor, pageable.getPageSize(), pageable.getSort(), body);
        return BoardResponse.of(boards);
    }

    @GetMapping("/{id}")
    public BoardResponse getBoardsById(@PathVariable Long id) {
        BoardDto board = boardService.getBoardById(id);
        return new BoardResponse(List.of(board));
    }

    @PatchMapping("")
    public BoardResponse updateBoard(@RequestBody BoardRequest boardRequest) {
        BoardDto board = boardService.updateBoard(new BoardDto(boardRequest.id(), boardRequest.title(), boardRequest.body(), null));
        return new BoardResponse(List.of(board));
    }

    @DeleteMapping("/{id}")
    public BoardResponse deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return new BoardResponse(List.of());
    }
}
