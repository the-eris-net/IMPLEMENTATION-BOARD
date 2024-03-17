package org.example.springimplementationboard.Board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    @Query("SELECT b FROM BoardEntity b LEFT JOIN FETCH b.comments WHERE b.id = :id")
    Optional<BoardEntity> findById(@Param("id") Long id);

    @Query("SELECT b FROM BoardEntity b LEFT JOIN FETCH b.comments")
    Page<BoardEntity> findAll(Pageable pageable);

    Slice<BoardEntity> findAllByIdLessThan(@Param("id") Long id, Pageable pageable);
}
