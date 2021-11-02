package org.zerock.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer") //@ToString은 항상 exclude, exclude: 해당 속성값으로 지정된 변수는 toString()에서 제외하기 떄문에 지연 로딩을 할 때는 반드시 지정
public class Board extends BaseEntity{ // 게시물
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment: 게시물 번호 자동 생성 및 증가
    private Long bno;
    private String bSel;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY) // 데이터베이스상에서 외래키(FK)의 관계로 연결된 엔티티 클래스에 설정, fetch는 지연 로딩
    private Member writer; //연관관계 지정

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
