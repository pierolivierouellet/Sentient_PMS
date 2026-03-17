package com.sentient_pms;

import java.time.LocalDate;

public class Reservation {

    private static int counter = 1;

    private int id;
    private String guestName;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private boolean checkedIn;

    public Reservation(String guestName, Room room,
                       LocalDate checkInDate, LocalDate checkOutDate) {

        this.id = counter++;
        this.guestName = guestName;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.checkedIn = false;
    }

    public int getId() { return id; }
    public String getGuestName() { return guestName; }
    public Room getRoom() { return room; }
    public boolean isCheckedIn() { return checkedIn; }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void checkIn() {
        checkedIn = true;
    }

    public void checkOut() {
        checkedIn = false;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Guest: " + guestName +
                " | Room: " + room.getRoomNumber() +
                " | Check-in: " + checkInDate +
                " | Check-out: " + checkOutDate +
                " | Status: " + (checkedIn ? "Checked-in" : "Not checked-in");
    }
}