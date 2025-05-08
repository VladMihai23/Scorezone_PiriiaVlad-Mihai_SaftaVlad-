package com.scorezone.scorezone.controller;

import com.scorezone.scorezone.model.Team;
import com.scorezone.scorezone.model.Tournament;
import com.scorezone.scorezone.model.TournamentMatch;
import com.scorezone.scorezone.service.TournamentMatchService;
import com.scorezone.scorezone.service.TournamentService;
import com.scorezone.scorezone.service.TournamentTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/tournament/match")
@PreAuthorize("hasRole('ADMIN')")
public class TournamentMatchController {

    private final TournamentMatchService tournamentMatchService;
    private final TournamentService tournamentTournamentService;
    private final TournamentTeamService tournamentTeamService;

    @Autowired
    public TournamentMatchController(TournamentMatchService tournamentMatchService,
                                     TournamentService tournamentTournamentService,
                                     TournamentTeamService tournamentTeamService) {
        this.tournamentMatchService = tournamentMatchService;
        this.tournamentTournamentService = tournamentTournamentService;
        this.tournamentTeamService = tournamentTeamService;
    }

    @PostMapping("/add")
    public String addMatch(@RequestParam Long tournamentId,
                           @RequestParam Long team1Id,
                           @RequestParam Long team2Id,
                           @RequestParam String status) {

        if (team1Id.equals(team2Id)) {

            return "redirect:/tournament/" + tournamentId + "/teams?error=sameTeam";
        }

        TournamentMatch match = new TournamentMatch();
        match.setTournament(tournamentTournamentService.getTournamentById(tournamentId));
        match.setTeam1(tournamentTeamService.getTeamById(team1Id));
        match.setTeam2(tournamentTeamService.getTeamById(team2Id));
        match.setStatus(status != null ? status : "Scheduled");
        match.setTeam1Score(0);
        match.setTeam2Score(0);

        tournamentMatchService.saveMatch(match);
        return "redirect:/tournament/" + tournamentId + "/teams";
    }

    @PostMapping("/update")
    public String updateScores(@RequestParam Long id,
                               @RequestParam int team1Score,
                               @RequestParam int team2Score) {

        TournamentMatch match = tournamentMatchService.getMatchById(id);
        if (match == null) {
            return "redirect:/tournament";
        }

        match.setTeam1Score(team1Score);
        match.setTeam2Score(team2Score);
        tournamentMatchService.saveMatch(match);

        Long tournamentId = match.getTournament().getId();
        return "redirect:/tournament/" + tournamentId + "/teams";
    }

    @PostMapping("/delete/{id}")
    public String deleteMatch(@PathVariable Long id) {
        TournamentMatch match = tournamentMatchService.getMatchById(id);
        if (match == null) {
            return "redirect:/tournament";
        }

        Long tournamentId = match.getTournament().getId();
        tournamentMatchService.deleteMatch(id);
        return "redirect:/tournament/" + tournamentId + "/teams";
    }

    @GetMapping("/{tournamentId}/teams")
    public String viewTournamentTeams(@PathVariable Long tournamentId, Model model) {
        System.out.println("Fetching teams for tournament ID: " + tournamentId);


        Set<Team> teams = tournamentMatchService.getTeamsAssignedToTournament(tournamentId);

        System.out.println("Teams fetched: " + teams.size());

        model.addAttribute("teams", teams);


        Tournament tournament = tournamentTournamentService.getTournamentById(tournamentId);
        List<TournamentMatch> matches = tournamentMatchService.getMatchesByTournamentId(tournamentId);

        model.addAttribute("tournament", tournament);
        model.addAttribute("matches", matches);

        return "tournament-matches";
    }






}
