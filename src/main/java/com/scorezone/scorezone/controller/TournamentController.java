package com.scorezone.scorezone.controller;


import com.scorezone.scorezone.model.Team;
import com.scorezone.scorezone.model.Tournament;
import com.scorezone.scorezone.model.TournamentMatch;
import com.scorezone.scorezone.service.TournamentMatchService;
import com.scorezone.scorezone.service.TournamentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/tournament")
public class TournamentController {


    private final TournamentService tournamentService;
    private final TournamentMatchService tournamentMatchService;



    public TournamentController(TournamentService tournamentService, TournamentMatchService tournamentMatchService) {
        this.tournamentService = tournamentService;
        this.tournamentMatchService = tournamentMatchService;
    }

    @GetMapping
    public String listTournament(Model model) {
        model.addAttribute("tournaments", tournamentService.getAllTournaments());
        return "tournament";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("tournament", new Tournament());
        return "tournament";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String createTournament(@ModelAttribute Tournament tournament) {
        tournamentService.saveTournament(tournament);
        return "redirect:/tournament";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Tournament tournament = tournamentService.getTournamentById(id);
        model.addAttribute("tournament", tournament);
        return "tournament/edit";
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateTournament(@ModelAttribute Tournament tournament) {
        tournamentService.saveTournament(tournament);
        return "redirect:/tournament";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return "redirect:/tournament";
    }

    @GetMapping("/{id}/teams")
    public String viewTournamentTeams(@PathVariable Long id, Model model) {
        Tournament tournament = tournamentService.getTournamentById(id);
        if (tournament == null) {
            return "redirect:/tournament";
        }

        List<TournamentMatch> matches = tournamentMatchService.getMatchesByTournamentId(id);

        Set<Team> teams = matches.stream()
                .flatMap(m -> List.of(m.getTeam1(), m.getTeam2()).stream())
                .collect(Collectors.toSet());

        model.addAttribute("tournament", tournament);
        model.addAttribute("matches", matches);
        model.addAttribute("teams", teams);
        return "tournament-matches";
    }




}
