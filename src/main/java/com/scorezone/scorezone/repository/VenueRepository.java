package com.scorezone.scorezone.repository;

import com.scorezone.scorezone.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    Optional<Venue> findByName(String name);
    Optional<Venue> findByNameIgnoreCase(String name);
    List<Venue> findByEditableTrue();

}
