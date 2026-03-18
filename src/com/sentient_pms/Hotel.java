package com.sentient_pms;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Hotel {

    private List<Reservation> reservations = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();

    public Hotel() {
        initializeRooms();
    }

    private void initializeRooms() {
        for (int i = 101; i <= 130; i++) {
            rooms.add(new Room(i, RoomType.STANDARD));
        }
        System.out.println("Hotel initialized with 30 rooms.");
    }

    public Room findRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    // 🔥 NEW: DATE CONFLICT CHECK
    private boolean isRoomBooked(Room room, LocalDate checkIn, LocalDate checkOut) {

        for (Reservation r : reservations) {

            if (r.getRoom().equals(room)) {

                LocalDate existingCheckIn = r.getCheckInDate();
                LocalDate existingCheckOut = r.getCheckOutDate();

                // overlap logic
                if (checkIn.isBefore(existingCheckOut) && checkOut.isAfter(existingCheckIn)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void createReservation(String guestName, int roomNumber,
                                  LocalDate checkIn, LocalDate checkOut) {

        Room room = findRoomByNumber(roomNumber);

        if (room == null) {
            System.out.println("Room not found.");
            return;
        }

        // 🔥 UPDATED CHECK (date-based)
        if (isRoomBooked(room, checkIn, checkOut)) {
            System.out.println("Room already booked for these dates.");
            return;
        }

        Reservation reservation = new Reservation(guestName, room, checkIn, checkOut);
        reservations.add(reservation);

        System.out.println("Reservation created successfully!");
    }

    public void viewReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }

        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    public void checkIn(int reservationId) {
        for (Reservation r : reservations) {
            if (r.getId() == reservationId) {

                Room room = r.getRoom();

                if (!room.isAvailable()) {
                    System.out.println("Room not available.");
                    return;
                }

                r.checkIn();
                room.bookRoom();

                System.out.println("Guest checked in.");
                return;
            }
        }
        System.out.println("Reservation not found.");
    }

    public void checkOut(int reservationId) {
        for (Reservation r : reservations) {
            if (r.getId() == reservationId) {

                Room room = r.getRoom();

                r.checkOut();
                room.checkOut();

                System.out.println("Guest checked out.");
                return;
            }
        }
        System.out.println("Reservation not found.");
    }

    public void viewInHouseGuests() {
        boolean found = false;

        for (Reservation r : reservations) {
            if (r.isCheckedIn()) {
                System.out.println(r);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No guests currently in-house.");
        }
    }

    // 🔥 NEW: VIEW ALL ROOMS
    public void viewAllRooms() {
        for (Room room : rooms) {
            System.out.println(room);
        }
    }
    
    public List<Room> getRooms() {
        return rooms;
    }
    
}