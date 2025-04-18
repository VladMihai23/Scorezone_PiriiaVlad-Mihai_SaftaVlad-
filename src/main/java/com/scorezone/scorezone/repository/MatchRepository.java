package com.scorezone.scorezone.repository;

import com.scorezone.scorezone.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MatchRepository extends JpaRepository<Match, Long> {
    Optional<Match> findByFixtureId(Long FixtureId);
}

