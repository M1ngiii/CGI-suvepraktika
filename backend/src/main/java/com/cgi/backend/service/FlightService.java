package com.cgi.backend.service;

import com.cgi.backend.model.Flight;
import com.cgi.backend.model.Seat;
import com.cgi.backend.repository.FlightRepository;
import org.springframework.stereotype.Service;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FlightService {
    // Impordime andmebaasi
    private final FlightRepository flightRepository;

    // Lingime andmebaasi
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    // Meetod et saada kõik lennud
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    // Ühe lennu saamine
    public Flight getFlightById(UUID id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight Not Found"));
    }

    // Uuendame istet. Set occupied
    public void updateSeats(UUID flightId, List<UUID> seatIds) {
        Flight flight = getFlightById(flightId); // already throws if not found

        for (Seat seat : flight.getSeats()) {
            if (seatIds.contains(seat.getId())) {
                seat.setOccupied(true);
            }
        }

        flightRepository.save(flight);
    }

    // Filtreeritud lendude saamine
    public List<Flight> getFilteredFlights(
            String from,
            String to,
            Double minPrice,
            Double maxPrice,
            LocalDateTime dateFrom,
            LocalDateTime dateTo
    ) {
        return flightRepository.findAll().stream()
                .filter(flight -> from == null || flight.getFrom().equalsIgnoreCase(from))
                .filter(flight -> to == null || flight.getTo().equalsIgnoreCase(to))
                .filter(flight -> minPrice == null || flight.getPrice() >= minPrice)
                .filter(flight -> maxPrice == null || flight.getPrice() <= maxPrice)
                .filter(flight -> dateFrom == null || !flight.getDepartureTime().isBefore(dateFrom))
                .filter(flight -> dateTo == null || flight.getDepartureTime().isBefore(dateTo.plusDays(1)))
                .toList();
    }
}