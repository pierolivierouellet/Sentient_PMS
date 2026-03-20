package com.sentient_pms;

import java.time.LocalDate;
import java.util.Random;

public class Reservation {

    private static Random random = new Random();

    private long id;
    private Guest guest;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status; // NEW

    public Reservation(Guest guest, Room room,
                       LocalDate checkInDate, LocalDate checkOutDate) {

        this.id = 1000000000L + (long)(random.nextDouble() * 9000000000L);
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = "BOOKED";
    }

    public long getId() { return id; }
    public Room getRoom() { return room; }
    public Guest getGuest() { return guest; }

    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }

    public boolean isCheckedIn() {
        return status.equals("CHECKED_IN");
    }

    public void checkIn() {
        status = "CHECKED_IN";
    }

    public void checkOut() {
        status = "CHECKED_OUT"; // ✅ FIXED
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Guest: " + guest.getFullName() +
                " | Room: " + room.getRoomNumber() +
                " | Check-in: " + checkInDate +
                " | Check-out: " + checkOutDate +
                " | Status: " + status;
    }
}