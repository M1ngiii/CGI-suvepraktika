package com.cgi.backend.service;

import com.cgi.backend.model.Flight;
import com.cgi.backend.model.Seat;
import com.cgi.backend.repository.FlightRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeatRecommendationService {
    private final FlightRepository flightRepository;

    public SeatRecommendationService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    private static final List<List<Character>> SEAT_GROUPS = List.of(
            List.of('A', 'B', 'C'), // Istmed on kolmestes gruppides, seega arvestame nendega kui võimalik
            List.of('D', 'E', 'F'),
            List.of('G', 'H', 'I') // Kui lennuk on suur
    );

    public List<Seat> recommendSeats(UUID flightId, int count, Boolean window, Boolean legroom, Boolean nearExit, Boolean adjacentOnly) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found"));

        List<Seat> available = flight.getSeats().stream()
                .filter(seat -> !seat.isOccupied())
                .filter(seat -> window == null || seat.isWindow() == window)
                .filter(seat -> legroom == null || seat.isLegroom() == legroom)
                .filter(seat -> nearExit == null || seat.isNearExit() == nearExit)
                .sorted(Comparator.comparing(Seat::getSeatNumber))
                .toList();

        if (adjacentOnly != null && adjacentOnly) {
            for (int i = 0; i <= available.size() - count; i++) {
                List<Seat> group = available.subList(i, i + count);
                if (areAdjacentSeatsInSameGroup(group)) {
                    return group;
                }
            }
        }

        return available.stream().limit(count).collect(Collectors.toList());
    }

    private boolean areAdjacentSeatsInSameGroup(List<Seat> seats) {
        if (seats.isEmpty()) return false;

        String row = seats.get(0).getSeatNumber().replaceAll("[^0-9]", "");
        List<Character> letters = seats.stream()
                .map(s -> s.getSeatNumber().replaceAll("[0-9]", "").charAt(0))
                .sorted()
                .toList();

        // kõik peavad olema samas reas
        if (!seats.stream().allMatch(s -> s.getSeatNumber().startsWith(row))) return false;

        // kas nad kuuluvad samasse gruppi ja on järjest
        for (List<Character> group : SEAT_GROUPS) {
            if (group.containsAll(letters)) {
                for (int i = 1; i < letters.size(); i++) {
                    if (letters.get(i) != letters.get(i - 1) + 1) return false;
                }
                return true;
            }
        }

        return false;
    }
}