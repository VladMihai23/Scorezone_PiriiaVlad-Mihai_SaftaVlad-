package com.scorezone.scorezone.service;

import com.scorezone.scorezone.model.Team;
import com.scorezone.scorezone.model.Tournament;
import com.scorezone.scorezone.model.TournamentMatch;
import com.scorezone.scorezone.repository.TournamentMatchRepository;
import com.scorezone.scorezone.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TournamentMatchService {

    private final TournamentMatchRepository tournamentMatchRepository;
    private final TournamentRepository tournamentRepository;

    @Autowired
    public TournamentMatchService(TournamentMatchRepository tournamentMatchRepository,
                                  TournamentRepository tournamentRepository) {
        this.tournamentMatchRepository = tournamentMatchRepository;
        this.tournamentRepository = tournamentRepository;
    }

    public List<TournamentMatch> getMatchesByTournamentId(Long tournamentId) {
        return tournamentMatchRepository.findByTournamentId(tournamentId);
    }

    public TournamentMatch getMatchById(Long id) {
        return tournamentMatchRepository.findById(id).orElse(null);
    }

    public void saveMatch(TournamentMatch match) {
        tournamentMatchRepository.save(match);
    }

    public void deleteMatch(Long id) {
        tournamentMatchRepository.deleteById(id);
    }

    public Set<Team> getTeamsAssignedToTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findByIdWithTeams(tournamentId).orElse(null);
        if (tournament == null) {
            System.out.println("No tournament found with id: " + tournamentId);
            return Set.of();
        }


        System.out.println("Found tournament with id: " + tournamentId);
        System.out.println("Teams in this tournament: " + tournament.getTeams().size());

        return new HashSet<>(tournament.getTeams());
    }




}

