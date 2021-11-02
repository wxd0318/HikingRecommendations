package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;

import java.util.function.Function;

@Service
@RequiredArgsConstructor // 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성, 의존성 주입(Dependency Injection) 편의성을 위해서 사용
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository; // 자동 주입, 반드시 final 사용
    private final ReplyRepository replyRepository; // 자동 주입, 반드시 final 사용

    @Override
    public Long register(BoardDTO dto) {
        // Entity 객체로 변환 전 로그 출력
        log.info(dto);
        // Entity 객체로 변환 후 Board Entity 클래스 타입의 변수에 대입
        Board board = dtoToEntity(dto);
        // JPA를 이용하여 데이터베이스에 board 객체의 데이터를 저장(insert)
        repository.save(board);
        // board 객체의 bno를 반환(글번호)
        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);
        
        // 1) 메소드 선언, Object[] 타입을 파라미터로 받아서 BoardDTO 타입으로 반환
        // 2) 각각 Entity 타입을 DTO 타입으로 변환 후 fn 변수에 대입
        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board)en[0], (Member)en[1], (Long)en[2]));

        // Pageable 인터페이스 타입으로 페이징 및 정렬 처리된 객체는 Page<Entity> 인터페이스 타입으로 받아야 한다.
//        Page<Object[]> result = repository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));

        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending()));
        
        // result: 검색 타입, 검색 값, 검색한 값의 정렬 조건으로 검색 후 가져온 결과 값
        // fn: 각각의 필드들에 대한 Entity 객체를 DTO 객체로 변환 후 Object[]에 저장한 결과 값
        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO get(Long bno) {
        // repository.getBoardByBno(bno): 게시물 번호, 제목, 내용, 작성자 이메일, 작성자 이름, 등록 시간, 수정 시간, 댓글 개수의 정보를 하나의 객체로 가져옴
        Object result = repository.getBoardByBno(bno);
        // Board Entity 클래스 타입, Member Entity 클래스 타입, Reply Entity 타입을 객체 배열 형태로 분할해서 저장
        Object[] arr = (Object[]) result;
        // 각각의 Entity 객체를 DTO 객체 타입으로 변환 후 반환
        return entityToDTO((Board)arr[0], (Member) arr[1], (Long) arr[2]);
    }

    @Transactional // 해당 메서드를 하나의 트랜잭션으로 처리하라는 의미, no Session 발생 시 데이터베이스와 연결 생성
    @Override
    public void removeWithReplies(Long bno) { // 삭제 기능 구현
        // 댓글 부터 삭제
        replyRepository.deleteByBno(bno);
        // 게시글 삭제
        repository.deleteById(bno);
    }

    @Transactional // 해당 메서드를 하나의 트랜잭션으로 처리하라는 의미, no Session 발생 시 데이터베이스와 연결 생성
    @Override
    public void modify(BoardDTO boardDTO) { // 게시물 수정 기능 구현
        // JPA를 이용하여 파라미터로 넘오온 boardDTO 객체 안에 있는 bno를 가진 데이터를 Board Entity 클래스 변수에 저장
        Board board = repository.getOne(boardDTO.getBno());
        
        // 파라미터로 넘어온 DTO 타입의 객체 안에 있는 제목과 내용을 변경 
        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());

        // JPA를 이용하여 Board 객체를 데이터베이스에 수정(update문) 결과를 저장
        repository.save(board);
    }
}
