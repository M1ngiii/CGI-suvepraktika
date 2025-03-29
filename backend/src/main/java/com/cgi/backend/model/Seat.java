package com.cgi.backend.model;

import jakarta.persistence.*;
import java.util.UUID;

// Loome istekoha mudeli
@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private boolean isWindow;

    @Column(nullable = false)
    private boolean isLegroom;

    @Column(nullable = false)
    private boolean isNearExit;

    @Column(nullable = false)
    private boolean isOccupied;

    public Seat() {}

    public Seat(String seatNumber, boolean isWindow, boolean isLegroom, boolean isNearExit, boolean isOccupied) {
        this.seatNumber = seatNumber;
        this.isWindow = isWindow;
        this.isLegroom = isLegroom;
        this.isNearExit = isNearExit;
        this.isOccupied = isOccupied;
    }

    // Getterid ja setterid
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    public boolean isWindow() {
        return isWindow;
    }
    public void setWindow(boolean window) {
        isWindow = window;
    }

    public boolean isLegroom() {
        return isLegroom;
    }
    public void setLegroom(boolean legroom) {
        isLegroom = legroom;
    }

    public boolean isNearExit() {
        return isNearExit;
    }
    public void setNearExit(boolean nearExit) {
        isNearExit = nearExit;
    }

    public boolean isOccupied() {
        return isOccupied;
    }
    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
}