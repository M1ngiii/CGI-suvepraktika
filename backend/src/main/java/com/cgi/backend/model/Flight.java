package com.cgi.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// Loome lendude mudeli
@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "departure_location")
    private String from;

    @Column(name = "arrival_location")
    private String to;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String planeModel;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "flight_id")
    private List<Seat> seats;

    @Column
    @ElementCollection
    private List<Integer> exitRows;

    public Flight() {}

    public Flight(UUID id, String from, String to, LocalDateTime departureTime, double price, List<Seat> seats, String planeModel, List<Integer> exitRows) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.departureTime = departureTime;
        this.price = price;
        this.seats = seats;
        this.planeModel = planeModel;
        this.exitRows = exitRows;
    }

    // Getterid ja setterid
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public List<Seat> getSeats() {
        return seats;
    }
    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public String getPlaneModel() {
        return planeModel;
    }
    public void setPlaneModel(String planeModel) {
        this.planeModel = planeModel;
    }

    public List<Integer> getExitRows() {
        return exitRows;
    }
    public void setExitRows(List<Integer> exitRows) {
        this.exitRows = exitRows;
    }
}