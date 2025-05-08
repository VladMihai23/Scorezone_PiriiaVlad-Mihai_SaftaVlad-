package com.scorezone.scorezone.repository;

import com.scorezone.scorezone.model.TournamentMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TournamentMatchRepository extends JpaRepository<TournamentMatch, Long> {
    List<TournamentMatch> findByTournamentId(Long tournamentId);
}
