package org.tuxdevelop.spring.batch.lightmin.server.fe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.journal.JournalModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.service.JournalsFeService;

import java.util.List;

@Controller
public class JournalsController {

    private final JournalsFeService journalsFeService;

    public JournalsController(final JournalsFeService journalsFeService) {
        this.journalsFeService = journalsFeService;
    }

    @GetMapping(value = "/journals")
    public void init(final Model model) {
        final List<JournalModel> journals = this.journalsFeService.getAll();
        model.addAttribute("journals", journals);
    }
}
