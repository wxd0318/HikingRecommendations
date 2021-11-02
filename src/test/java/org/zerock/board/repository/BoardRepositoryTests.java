package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest // 스프링부트 어플리케이션 테스트에 필요한 거의 모든 의존성을 제공
public class BoardRepositoryTests { // 게시판 테스트 클래스
    @Autowired // 객체의 의존성 자동 주입
    private BoardRepository boardRepository;

    @Test // 테스트를 수행하는 메소드, 각각의 테스트가 서로 영향을 주지 않고 독립적으로 실행됨
    public void insertBoard() { // 게시판 데이터 저장 테스트
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder()
                    .email("user"+ i + "@test.com") // 글쓴이
                    .build();

            Board board = Board.builder()
                    .title("Title..." + i) // 글제목
                    .content("Content..." + i) //글내용
                    .writer(member)
                    .build();
            boardRepository.save(board);
        });
    }

    @Transactional // 해당 메서드를 하나의 트랜잭션으로 처리하라는 의미, no Session 발생 시 데이터베이스와 연결 생성
    @Test
    public void testRead1() { // 게시판 조회 테스트
        // Optional<Entity>: Null 처리를 돕는 Optional 클래스, NullPointerException 예외 처리
        Optional<Board> result = boardRepository.findById(100L); //db에 존재하는 번호
        // get(): board 테이블에서 bno가 100인 데이터를 가져옴
        Board board = result.get();

        System.out.println(board); // board 테이블에서 bno가 100인 데이터 출력
        // board 테이블에서 bno가 100인 데이터의 writer_email과 같은 데이터를 member 테이블에서 출력
        System.out.println(board.getWriter());
    }

    @Test
    public void testReadWithWriter() { // board 테이블과 member 테이블 조인 테스트
        Object result = boardRepository.getBoardWithWriter(100L);
        Object[] arr = (Object[]) result;

        System.out.println("--------------------------------------");
        System.out.println(Arrays.toString(arr));
    }
    
    @Test
    public void testGetBoardWithReply() { // board 테이블과 reply 테이블 조인 테스트
        List<Object[]> result = boardRepository.getBoardWithReply(100L);

        for(Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
    }

    @Test
    public void testWithReplyCount() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

        result.get().forEach(row -> {
            Object[] arr = (Object[]) row;
            System.out.println(Arrays.toString(arr));
        });
    }

    @Test
    public void testRead3() {
        Object result = boardRepository.getBoardByBno(100L);

        Object[] arr = (Object[]) result;

        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void testSearch1() { // JPQL을 이용한 조회 테스트
        boardRepository.search1();
    }

    @Test
    public void testSearchPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending().and(Sort.by("title").ascending()));

        Page<Object[]> result = boardRepository.searchPage("t", "1", pageable);
    }
}
