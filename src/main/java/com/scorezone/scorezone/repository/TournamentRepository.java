package com.scorezone.scorezone.repository;


import com.scorezone.scorezone.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    @Query("SELECT t FROM Tournament t LEFT JOIN FETCH t.teams WHERE t.id = :id")
    Optional<Tournament> findByIdWithTeams(@Param("id") Long id);

}
