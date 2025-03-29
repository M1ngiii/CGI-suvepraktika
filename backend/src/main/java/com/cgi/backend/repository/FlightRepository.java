package com.cgi.backend.repository;

import com.cgi.backend.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FlightRepository extends JpaRepository<Flight, UUID> {
    // Hiljem päringud kui vaja.
}