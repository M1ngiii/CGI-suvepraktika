package com.cgi.backend.controller;

import com.cgi.backend.model.Flight;
import com.cgi.backend.model.Seat;
import com.cgi.backend.service.FlightService;
import com.cgi.backend.service.SeatRecommendationService;

import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/flights")
@CrossOrigin(origins = "*") // kui front-end jookseb
public class FlightController {

    private final FlightService flightService;
    private final SeatRecommendationService seatRecommendationService;

    public FlightController(FlightService flightService, SeatRecommendationService seatRecommendationService) {
        this.flightService = flightService;
        this.seatRecommendationService = seatRecommendationService;
    }

    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }

    @GetMapping
    public List<Flight> getFilteredFlights(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo
    ) {
        return flightService.getFilteredFlights(from, to, minPrice, maxPrice, dateFrom, dateTo);
    }

    @GetMapping("/{id}")
    public Flight getFlightById(@PathVariable UUID id) {
        return flightService.getFlightById(id);
    }

    @PutMapping("/{id}/seats")
    public void updateSeats(
            @PathVariable UUID id,
            @RequestBody List<UUID> seatIds
    ) {
        flightService.updateSeats(id, seatIds);
    }

    @GetMapping("/{id}/seats/recommendations")
    public List<Seat> recommendSeats(
            @PathVariable UUID id,
            @RequestParam int count,
            @RequestParam(required = false) Boolean isWindow,
            @RequestParam(required = false) Boolean isLegroom,
            @RequestParam(required = false) Boolean isNearExit,
            @RequestParam(required = false) Boolean adjacentOnly
    ) {
        return seatRecommendationService.recommendSeats(id, count, isWindow, isLegroom, isNearExit, adjacentOnly);
    }
}