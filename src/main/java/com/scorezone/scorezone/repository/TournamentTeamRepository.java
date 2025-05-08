package com.scorezone.scorezone.repository;

import com.scorezone.scorezone.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentTeamRepository extends JpaRepository<Team, Long> {
    boolean existsByName(String name);
}
