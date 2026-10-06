package com.smartcampus.repository;

import com.smartcampus.model.CampusLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CampusLocationRepository extends JpaRepository<CampusLocation, Long> {

    Optional<CampusLocation> findByName(String name);
}