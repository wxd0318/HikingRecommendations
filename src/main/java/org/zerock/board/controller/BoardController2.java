package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.BoardService;

// @RestController: 순수하게 데이터만을 반환하기 위해
@Controller // 해당 클래스가 Controller임을 나타내기 위한 어노테이션, View를 반환하기 위해 사용
@RequestMapping("board2") // 요청에 대해 어떤 Controller, 어떤 메소드가 처리할지를 맵핑하기 위한 어노테이션, URL을 컨트롤러의 메서드와 매핑할 때 사용
@Log4j2
@RequiredArgsConstructor // 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성, 의존성 주입(Dependency Injection) 편의성을 위해서 사용
public class BoardController2 {

    private final BoardService boardService;

    @GetMapping("list2")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("list2..........." + pageRequestDTO);
        // model을 이용하여 list.html 파일에 result(key)의 데이터(목록 처리, 페이징 정렬 등)를 전달
        model.addAttribute("result", boardService.getList(pageRequestDTO));
        log.info(boardService.getList(pageRequestDTO));
    }

    @GetMapping("register2")
    public void register() {
        log.info("register get...");
    }

    @PostMapping("register2")
    public String registerPost(BoardDTO dto, RedirectAttributes redirectAttributes) {
        log.info("dto...." + dto);

        Long bno = boardService.register(dto);

        log.info("BNO: " + bno);

        redirectAttributes.addFlashAttribute("msg", bno);

        return "redirect:/board2/list2";
    }

    @GetMapping({"read2", "modify2"})
    public void read2(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model) {
        log.info("bno: " + bno);

        BoardDTO boardDTO = boardService.get(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
    }

    @PostMapping("remove")
    public String remove(long bno, RedirectAttributes redirectAttributes) {
        log.info("bno: " + bno);
        boardService.removeWithReplies(bno);
        redirectAttributes.addFlashAttribute("msg", bno);
        return "redirect:/board2/list2";
    }

    @PostMapping("modify2")
    public String modify(BoardDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes) {
        log.info("post modify..............");
        log.info("dto: " + dto);

        boardService.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("bno", dto.getBno());

        return "redirect:/board2/read2";
    }
}

