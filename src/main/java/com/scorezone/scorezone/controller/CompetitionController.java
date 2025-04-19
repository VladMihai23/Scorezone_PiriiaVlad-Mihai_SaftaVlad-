package com.scorezone.scorezone.controller;

import com.scorezone.scorezone.service.CompetitionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CompetitionController {

    private final CompetitionService competitionService;

    public CompetitionController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @GetMapping("/competitions")
    public String showAllCompetitions(Model model) {
        model.addAttribute("competitions", competitionService.fetchAllCompetitions());
        return "competition";
    }

    @GetMapping("/competitions/search")
    public String searchCompetitions(@RequestParam("query") String query, Model model) {
        model.addAttribute("competitions", competitionService.searchCompetitionsByName(query));
        return "competition";
    }
}
