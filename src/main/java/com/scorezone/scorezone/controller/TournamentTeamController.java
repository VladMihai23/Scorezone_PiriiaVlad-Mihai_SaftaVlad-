package com.scorezone.scorezone.controller;

import com.scorezone.scorezone.model.Team;
import com.scorezone.scorezone.model.Tournament;
import com.scorezone.scorezone.service.TournamentService;
import com.scorezone.scorezone.service.TournamentTeamService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/tournament-team")
public class TournamentTeamController {

    private final TournamentTeamService tournamentTeamService;
    private final TournamentService tournamentService;

    public TournamentTeamController(TournamentTeamService tournamentTeamService,
                                    TournamentService tournamentService) {
        this.tournamentTeamService = tournamentTeamService;
        this.tournamentService = tournamentService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String createTeamForm(Model model) {
        model.addAttribute("team", new Team());
        return "createTeam";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String createTeam(@ModelAttribute("team") Team team, Model model) {
        if (tournamentTeamService.teamExistsByName(team.getName())) {
            model.addAttribute("error", "Team with this name already exists.");
            return "team/createTeam";
        }
        tournamentTeamService.addTeam(team);
        return "redirect:/tournament-team/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/delete")
    public String deleteTeam(@PathVariable Long id) {
        tournamentTeamService.deleteTeam(id);
        return "redirect:/tournament-team/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String editTeamForm(@PathVariable Long id, Model model) {
        Team team = tournamentTeamService.getTeamById(id);
        model.addAttribute("team", team);
        return "team/editTeam";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/edit")
    public String updateTeam(@PathVariable Long id, @ModelAttribute("team") Team updatedTeam) {
        tournamentTeamService.updateTeam(id, updatedTeam);
        return "redirect:/tournament-team/list";
    }

    @GetMapping("/list")
    public String listTeams(Model model) {
        model.addAttribute("teams", tournamentTeamService.getAllTeams());
        return "Team/listTeams";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/assign/{tournamentId}")
    public String manageTournamentTeams(@PathVariable Long tournamentId, Model model) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        List<Team> allTeams = tournamentTeamService.getAllTeams();
        Set<Team> assignedTeams = tournamentTeamService.getTeamsByTournament(tournamentId);

        model.addAttribute("tournament", tournament);
        model.addAttribute("allTeams", allTeams);
        model.addAttribute("assignedTeams", assignedTeams);

        return "tournament-team";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign/{tournamentId}/add")
    public String addTeamToTournament(@PathVariable Long tournamentId, @RequestParam Long teamId) {
        tournamentTeamService.addTeamToTournament(tournamentId, teamId);
        return "redirect:/tournament-team/assign/" + tournamentId;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign/{tournamentId}/remove")
    public String removeTeamFromTournament(@PathVariable Long tournamentId, @RequestParam Long teamId) {
        tournamentTeamService.removeTeamFromTournament(tournamentId, teamId);
        return "redirect:/tournament-team/assign/" + tournamentId;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign/{tournamentId}/create")
    public String createTeamInline(@PathVariable Long tournamentId,
                                   @RequestParam String teamName,
                                   Model model) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        if (tournament == null) {
            model.addAttribute("error", "Tournament not found.");
            return "error";
        }

        if (!tournamentTeamService.teamExistsByName(teamName)) {
            Team team = new Team();
            team.setName(teamName);
            tournamentTeamService.addTeam(team);
        }

        return "redirect:/tournament-team/assign/" + tournamentId;
    }



}
