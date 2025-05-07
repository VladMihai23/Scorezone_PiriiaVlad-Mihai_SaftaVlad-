package com.scorezone.scorezone.controller;


import com.scorezone.scorezone.model.Tournament;
import com.scorezone.scorezone.service.TournamentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/tournament")
public class TournamentController {


    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
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
    public String updateTournament(@ModelAttribute Tournament tournament) {
        tournamentService.saveTournament(tournament);
        return "redirect:/tournament";
    }

    @PostMapping("/delete/{id}")
    public String deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return "redirect:/tournament";
    }
}
