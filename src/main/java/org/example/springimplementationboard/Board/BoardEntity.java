package org.example.springimplementationboard.Board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.springimplementationboard.comment.CommentEntity;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String body;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted;
    /**
     * mappedBy를 정하지 않으면 중계테이블을 생성함
     * OneToMany의 기본 FetchType : LAZY
     * ManyToOne의 기본 FetchType : EAGER
     */
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @SQLRestriction("is_deleted=false")
    private List<CommentEntity> comments = new ArrayList<>();

    public BoardEntity(Long id) {
        this.id = id;
    }

    public BoardEntity(Long id, String title, String body) {
        this(id);
        this.title = title;
        this.body = body;
    }

    public BoardEntity(Long id, String title, String body, boolean isDeleted) {
        this(id, title, body);
        this.isDeleted = isDeleted;
    }
}
