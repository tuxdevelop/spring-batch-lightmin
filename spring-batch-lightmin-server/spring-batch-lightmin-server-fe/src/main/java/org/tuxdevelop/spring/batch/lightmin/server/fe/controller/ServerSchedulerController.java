package org.tuxdevelop.spring.batch.lightmin.server.fe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServerSchedulerController extends CommonController {

    @GetMapping("/server-schedulers")
    public void init() {

    }

}
