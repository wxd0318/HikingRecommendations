package org.zerock.board.controller;

import com.sun.istack.logging.Logger;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("company")
@Log4j2
public class CompanyController {
    @GetMapping("company")
    public void company() {log.info("company");}

}