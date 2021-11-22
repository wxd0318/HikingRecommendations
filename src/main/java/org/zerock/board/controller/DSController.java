package org.zerock.board.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("ds")
@Log4j2
public class DSController {
    @GetMapping("main2")
    public void main() {
        log.info("main.......");
    }

    @GetMapping("Jang")
    public void Jang() {
        log.info("Jang........");
    }

    @GetMapping("BaekYang")
    public void BaekYang() {
        log.info("BaekYang.......");
    }

    @GetMapping("GeumJeong")
    public void GeumJeong() {
        log.info("GeumJeong........");
    }

    @GetMapping("GeumRyeon")
    public void GeumRyeon() { log.info("GeumRyeon.......");}



}


