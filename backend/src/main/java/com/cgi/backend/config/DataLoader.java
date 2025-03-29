package com.cgi.backend.config;

import com.cgi.backend.model.Flight;
import com.cgi.backend.model.Seat;
import com.cgi.backend.repository.FlightRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.time.LocalDate;


@Component
public class DataLoader {

    @Autowired
    private FlightRepository flightRepository;

    private final List<String> cities = List.of(
            "Tallinn", "Riga", "Helsinki", "Stockholm", "Oslo", "Copenhagen",
            "Berlin", "Amsterdam", "London", "Paris", "Rome", "Madrid", "Lisbon",
            "Vienna", "Prague", "Warsaw", "Budapest", "Brussels"
    );

    private final Random random = new Random();

    @PostConstruct
    public void loadData() {
        List<Flight> flights = new ArrayList<>();

        for (int i = 0; i < 100; i++) { // Siit saab valida, mitu lendu genereeritakse.
            String from = getRandomCity();
            String to;
            do {
                to = getRandomCity();
            } while (to.equals(from));

            boolean isBigPlane = random.nextBoolean();
            String planeModel = isBigPlane ? "LARGE" : "SMALL";  // Saaks teha süsteemi erinevatest lennukitest ja nende mahutavusest jne.
            int rowCount = isBigPlane ? 50 : 30;
            List<Integer> exitRows = List.of(1, rowCount / 2, rowCount);

            List<Seat> seats = generateSeats(rowCount, isBigPlane, exitRows);

            Flight flight = new Flight(
                null,
                from,
                to,
                LocalDate.now().atStartOfDay().plusDays(random.nextInt(30)).withHour(8 + random.nextInt(10)).withMinute(0 + random.nextInt(6)*10),
                50 + random.nextInt(250),
                seats,
                planeModel,
                exitRows
            );

            flights.add(flight);
        }

        flightRepository.saveAll(flights);
    }

    private String getRandomCity() {
        return cities.get(random.nextInt(cities.size()));
    }

    private List<Integer> getExitRows(int rowCount) {
        List<Integer> exitRows = new ArrayList<>();
        exitRows.add(1);
        exitRows.add(rowCount / 2);
        exitRows.add(rowCount);
        return exitRows;
    }

    private List<Seat> generateSeats(int rowCount, boolean isBigPlane, List<Integer> exitRows) {
        List<Seat> seats = new ArrayList<>();
        String[] columns = isBigPlane
                ? new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I"}
                : new String[]{"A", "B", "C", "D", "E", "F"};

        for (int row = 1; row <= rowCount; row++) {
            for (String col : columns) {
                String seatNumber = row + col;

                boolean isWindow = col.equals(columns[0]) || col.equals(columns[columns.length - 1]);
                boolean isLegroom = exitRows.contains(row) && (row == 1 || row == exitRows.get(1)); // Esimene ja keskmine exit row on extra legroom.
                boolean isNearExit = false; // Sätime väljapääsu juures olevad istmed
                for (int er : exitRows) {
                    if (Math.abs(er - row) <= 2) {
                        isNearExit = true;
                        break;
                    }
                }

                seats.add(new Seat(
                    seatNumber,
                    isWindow,
                    isLegroom,
                    isNearExit,
                    random.nextDouble() < 0.3  // Saab siin määrata, kui paljud on täis.
                ));
            }
        }

        return seats;
    }
}