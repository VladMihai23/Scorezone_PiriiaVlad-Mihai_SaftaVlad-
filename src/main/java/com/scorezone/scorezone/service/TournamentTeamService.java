package com.scorezone.scorezone.service;

import com.scorezone.scorezone.model.Team;
import com.scorezone.scorezone.model.Tournament;
import com.scorezone.scorezone.model.TournamentMatch;
import com.scorezone.scorezone.repository.TournamentTeamRepository;
import com.scorezone.scorezone.repository.TournamentRepository;
import com.scorezone.scorezone.repository.TournamentMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TournamentTeamService {

    private final TournamentTeamRepository tournamentTeamRepository;
    private final TournamentRepository tournamentRepository;
    private final TournamentMatchRepository tournamentMatchRepository;

    @Autowired
    public TournamentTeamService(TournamentTeamRepository tournamentTeamRepository,
                                 TournamentRepository tournamentRepository,
                                 TournamentMatchRepository tournamentMatchRepository) {
        this.tournamentTeamRepository = tournamentTeamRepository;
        this.tournamentRepository = tournamentRepository;
        this.tournamentMatchRepository = tournamentMatchRepository;
    }

    public List<Team> getAllTeams() {
        return tournamentTeamRepository.findAll();
    }

    public Team getTeamById(Long id) {
        return tournamentTeamRepository.findById(id).orElse(null);
    }

    public void addTeam(Team team) {
        tournamentTeamRepository.save(team);
    }

    public void updateTeam(Long id, Team updatedTeam) {
        Team existing = getTeamById(id);
        if (existing != null) {
            existing.setName(updatedTeam.getName());
            tournamentTeamRepository.save(existing);
        }
    }

    public void deleteTeam(Long id) {
        tournamentTeamRepository.deleteById(id);
    }

    public boolean teamExistsByName(String name) {
        return tournamentTeamRepository.existsByName(name);
    }

    public Set<Team> getTeamsByTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElse(null);
        if (tournament == null) return Set.of();
        return Set.copyOf(tournament.getTeams());
    }


    public void addTeamToTournament(Long tournamentId, Long teamId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElse(null);
        Team team = tournamentTeamRepository.findById(teamId).orElse(null);

        if (tournament != null && team != null) {
            team.setTournament(tournament);
            tournamentTeamRepository.save(team);
        }
    }

    public void removeTeamFromTournament(Long tournamentId, Long teamId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElse(null);
        Team team = tournamentTeamRepository.findById(teamId).orElse(null);

        if (tournament != null && team != null && team.getTournament() != null
                && team.getTournament().getId() == tournamentId) {

            team.setTournament(null);
            tournamentTeamRepository.save(team);
        }
    }


    public void createTeamIfNotExists(String name) {
        if (!tournamentTeamRepository.existsByName(name)) {
            Team team = new Team();
            team.setName(name);
            team.setTournament(null);
            tournamentTeamRepository.save(team);
        }
    }


}
