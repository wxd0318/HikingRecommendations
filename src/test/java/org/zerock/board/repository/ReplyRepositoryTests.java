package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest // 스프링부트 어플리케이션 테스트에 필요한 거의 모든 의존성을 제공
public class ReplyRepositoryTests { // 댓글 테스트 클래스
    @Autowired // 객체의 의존성 자동 주입
    private ReplyRepository replyRepository;
    
    @Test // 테스트를 수행하는 메소드, 각각의 테스트가 서로 영향을 주지 않고 독립적으로 실행됨
    public void insertReply() { // 댓글 데이터 추가
        IntStream.rangeClosed(1, 300).forEach(i -> {
            //1부터 100까지의 임의의 번호
            long bno = (long) (Math.random() * 100) + 1;

            Board board = Board.builder().bno(bno).build();

            Reply reply = Reply.builder()
                    .text("Reply..." + i)
                    .board(board) // 1~100 임의의 게시물 번호
                    .replyer("guest")
                    .build();

            replyRepository.save(reply);
        });
    }

    @Test
    public void readReply1() { // 댓글 조회 테스트
        Optional<Reply> result = replyRepository.findById(1L);

        Reply reply = result.get();

        System.out.println(reply);
        System.out.println(reply.getBoard());
    }

    @Test
    public void testListByBoard() {
        List<Reply> replyList = replyRepository.getRepliesByBoardOrderByRno(Board.builder().bno(97L).build());
        replyList.forEach(reply -> System.out.println(reply));
    }
}
