package com.scorezone.scorezone.repository;

import com.scorezone.scorezone.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    Optional<Match> findByFixtureId(Long FixtureId);
    List<Match> findByStatus(String status);
}

