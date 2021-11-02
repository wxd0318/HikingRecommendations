package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    // 게시글 삭제 시 댓글들 삭제
    @Modifying // @Query 어노테이션으로 작성된 수정, 삭제 쿼리 메소드를 사용할 때 필요, 즉, 조회 쿼리를 제외하고 데이터에 변경이 일어나는 INSERT, UPDATE, DELETE 쿼리에서 사용
    @Query("delete from Reply r where r.board.bno =:bno ") // 게시물 삭제하기 전 댓글 삭제
    void deleteByBno(@Param("bno") Long bno); // Reply Entity 클래스에서 파라미터로 넘어온 bno의 데이터를 삭제
    // 게시물로 댓글 목록 가져오기
    List<Reply> getRepliesByBoardOrderByRno(Board board);
}
