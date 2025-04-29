package com.scorezone.scorezone.controller;

import com.scorezone.scorezone.service.CompetitionStatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class CompetitionStatisticsController {

    private final CompetitionStatisticsService competitionStatisticsService;

    public CompetitionStatisticsController(CompetitionStatisticsService competitionStatisticsService) {
        this.competitionStatisticsService = competitionStatisticsService;
    }

    @GetMapping("/competitions/statistics")
    public String showCompetitionStatistics(Model model) {
        List<Map<String, Object>> statistics = competitionStatisticsService.fetchCompetitionStatistics();
        model.addAttribute("statistics", statistics);
        return "competitionstatistics";
    }
}
