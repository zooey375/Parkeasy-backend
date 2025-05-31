package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping(value = {"/", "/{path:^(?!api|parking-lots|images).*}/**"})
    public String forward() {
        return "forward:/index.html";
    }
}
