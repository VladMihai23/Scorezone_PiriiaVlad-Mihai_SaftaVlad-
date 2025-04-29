package com.scorezone.scorezone.controller;

import com.scorezone.scorezone.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/teams")
    public String showTeams(Model model) {
        model.addAttribute("teamsByLeague", teamService.fetchTeamsFromImportantLeagues());
        return "teams";
    }

    @GetMapping("/teams/statistics")
    public String showTeamStatistics(@RequestParam("team") String teamInfo, Model model) {
        String[] parts = teamInfo.split("-");
        int teamId = Integer.parseInt(parts[0]);
        int leagueId = Integer.parseInt(parts[1]);

        var statistics = teamService.fetchTeamStatistics(teamId, leagueId);
        model.addAttribute("statistics", statistics);
        return "teamstatistics";
    }
}