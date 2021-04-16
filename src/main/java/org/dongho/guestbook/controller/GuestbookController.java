package org.dongho.guestbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dongho.guestbook.dto.PageRequestDTO;
import org.dongho.guestbook.service.GuestbookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {

    private final GuestbookService serivce;

    @GetMapping({"/" , "/list"})
    public String list(PageRequestDTO pageRequestDTO , Model model){

        log.info("list.........." + pageRequestDTO);

        log.info("---------------------------");
        log.info(serivce.getList(pageRequestDTO));

        model.addAttribute("list" , serivce.getList(pageRequestDTO));

        return "/guestbook/list";
    }

    @GetMapping("/register")
    public void register(){
        log.info("register...");
    }


}
