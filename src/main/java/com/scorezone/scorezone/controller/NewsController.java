package com.scorezone.scorezone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.scorezone.scorezone.service.NewsService;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class NewsController {
    private final NewsService newsService;
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/")
    public String showFootballNews(Model model) {

        System.out.println("News page accessed.");

        model.addAttribute("articles", newsService.fetchFootballNews());
        return "home";
    }
}
