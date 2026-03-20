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

    private boolean isRoomBooked(Room room, LocalDate checkIn, LocalDate checkOut) {
        for (Reservation r : reservations) {
            if (r.getRoom().equals(room)) {

                LocalDate existingCheckIn = r.getCheckInDate();
                LocalDate existingCheckOut = r.getCheckOutDate();

                if (checkIn.isBefore(existingCheckOut) && checkOut.isAfter(existingCheckIn)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void createReservation(String firstName, String lastName, int roomNumber,
            LocalDate checkIn, LocalDate checkOut) {

Room room = findRoomByNumber(roomNumber);

if (room == null) {
System.out.println("Room not found.");
return;
}

if (isRoomBooked(room, checkIn, checkOut)) {
System.out.println("Room already booked for these dates.");
return;
}

Guest guest = new Guest(firstName, lastName);

Reservation reservation = new Reservation(guest, room, checkIn, checkOut);
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

    public void checkIn(long reservationId) {
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

    public void checkOut(long reservationId) {
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

    public void viewAllRooms() {
        for (Room room : rooms) {
            System.out.println(room);
        }
    }

    // ✅ REQUIRED FOR UI
    public List<Room> getRooms() {
        return rooms;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
    
    public List<Reservation> getPastReservations() {
        List<Reservation> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Reservation r : reservations) {
            if (r.getCheckOutDate().isBefore(today)) {
                result.add(r);
            }
        }
        return result;
    }

    public List<Reservation> getUpcomingReservations() {
        List<Reservation> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Reservation r : reservations) {
            if (r.getCheckInDate().isAfter(today)) {
                result.add(r);
            }
        }
        return result;
    }

    public List<Reservation> getTodayReservations() {
        List<Reservation> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Reservation r : reservations) {
            if ((r.getCheckInDate().isEqual(today)) ||
                (r.getCheckOutDate().isEqual(today))) {
                result.add(r);
            }
        }
        return result;
    }
}