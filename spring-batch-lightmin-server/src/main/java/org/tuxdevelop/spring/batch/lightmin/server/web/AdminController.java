package org.tuxdevelop.spring.batch.lightmin.server.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends CommonController {
    
    public AdminController() {
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void initAdmin(final Model model) {

    }

}
