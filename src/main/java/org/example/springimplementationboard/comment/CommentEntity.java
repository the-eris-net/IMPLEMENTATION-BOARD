package org.example.springimplementationboard.comment;

import jakarta.persistence.*;
import lombok.*;
import org.example.springimplementationboard.Board.BoardEntity;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String body;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted;
    //        @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    private BoardEntity board;
}
