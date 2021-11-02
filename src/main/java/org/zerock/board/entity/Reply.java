package org.zerock.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board") // @ToString 주의
public class Reply extends BaseEntity{ // 댓글
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;
    private String text;
    private String replyer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
}
