package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;
import org.zerock.board.repository.search.SearchBoardRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {
    // 한 개의 로우(object) 내에 object[]로 나옴
    // select b, w: b(Board Entity 클래스), w(Board Entity 클래스 안에 있는 writer 필드)
    // from Board b : Board Entity 클래스를 통해 조회 할것이며 Board Entity 클래스의 이름을 b로 변수 선언.
    // left join b.writer w: b.writer(FK) 즉, Member Entity 클래스의 PK과 연관관계(Member Entity 클래스와 join)
    // where b.bno =:bno: @Param("bno") Long bno에 의해 파라미터로 넘어온 bno의 값을 기준으로 조회
    @Query("select b, w from Board b left join b.writer w where b.bno =:bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    // Board Entity 클래스와 Reply Entity 클래스는 연관 관계가 없기 때문에 on을 이용하여 연관관계를 형성해야 한다.
    // on r.board = b: Reply Entity 클래스 안에 있는 board 필드와 Board Entity 클래스와 연관관계 형성
    @Query("select b, r from Board b left join Reply r on r.board = b where b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);
    
    @Query(value = "select b, w, count(r) " +
            " from Board b " + 
            " left join b.writer w " + 
            " left join Reply r on r.board = b " +
            " group by b",
            countQuery = "select count(b) from Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable); // 목록 화면에 필요한 데이터

    @Query("select b, w, count(r) " +
            " from Board b left join b.writer w " +
            " left outer join Reply r on r.board = b" +
            " where b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);
}
