package com.scorezone.scorezone.controller;

import com.scorezone.scorezone.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
