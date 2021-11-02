package org.zerock.board.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// PageRequestDTO 클래스에서 처리한 데이터를 전달받아 웹 브라우저에서 표현하기 위해 데이터 전송을 담당하는 클래스
@Data
public class PageResultDTO<DTO, EN> {
    // 메인정보, DTO 리스트, DTO 타입으로 저장
    private List<DTO> dtoList;
    // 총 페이지 번호
    private int totalPage;
    // 현재 페이지 번호
    private int page;
    // 목록 사이즈
    private int size;
    //시작 페이지 번호, 끝 페이지 번호
    private int start, end;
    // 이전, 다음
    private boolean prev, next;
    // 페이지 번호 목록
    private List<Integer> pageList;

    // Page<EN>: Page<EN> 타입을 이용해서 생성할 수 있도록 생성자로 작성
    // Function<EN, DTO>: 엔티티 객체들을 DTO로 변환해주는 기능
    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();

        makePageList(result.getPageable());
    }

    // 페이지 리스트 만들기
    private void makePageList(Pageable pageable) {
        this.page = pageable.getPageNumber() + 1; // 0부터 시작하므로 1을 추가
        this.size = pageable.getPageSize();

        // temp end page
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;
        start = tempEnd - 9;
        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd : totalPage;
        next = totalPage > tempEnd;
        // boxed(): primitive(원시) 타입을 wrapper 타입으로 박싱하여 반환
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
