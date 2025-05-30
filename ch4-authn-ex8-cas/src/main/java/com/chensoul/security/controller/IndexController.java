package com.chensoul.security.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Value("${cas.logout.url}")
    private String casLogoutUrl;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/loggedout")
    public String loggedout(Model model) {
        model.addAttribute("casLogout", this.casLogoutUrl);
        return "loggedout";
    }

    @GetMapping("/public")
    String publicPage() {
        return "public";
    }

}
