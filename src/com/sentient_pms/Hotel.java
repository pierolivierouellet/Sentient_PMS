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
    }

    public Room findRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    // ✅ FIXED LOGIC (same-day turnover allowed)
    private boolean isRoomBooked(Room room, LocalDate checkIn, LocalDate checkOut) {

        for (Reservation r : reservations) {

            if (!r.getRoom().equals(room)) continue;

            // Ignore checked-out stays
            if (r.getStatus().equals("CHECKED_OUT")) continue;

            LocalDate existingCheckIn = r.getCheckInDate();
            LocalDate existingCheckOut = r.getCheckOutDate();

            boolean overlap =
                    checkIn.isBefore(existingCheckOut) &&
                    checkOut.isAfter(existingCheckIn);

            if (overlap) return true;
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
        System.out.println("Reservation created.");
    }

    public void checkIn(long id) {
        for (Reservation r : reservations) {
            if (r.getId() == id) {

                if (!r.getRoom().isAvailable()) {
                    System.out.println("Room not available.");
                    return;
                }

                r.checkIn();
                r.getRoom().bookRoom();
                return;
            }
        }
    }

    public void checkOut(long id) {
        for (Reservation r : reservations) {
            if (r.getId() == id) {

                r.checkOut();
                r.getRoom().checkOut(); // DIRTY
                return;
            }
        }
    }

    public void setRoomStatus(int roomNumber, RoomStatus status) {
        Room room = findRoomByNumber(roomNumber);
        if (room != null) {
            room.setRoomStatus(status);
        }
    }

    public void cleanRoomsRange(int start, int end) {
        for (int i = start; i <= end; i++) {
            Room room = findRoomByNumber(i);
            if (room != null && room.getRoomStatus() == RoomStatus.DIRTY) {
                room.cleanRoom();
            }
        }
    }

    public List<Room> getRooms() { return rooms; }
    public List<Reservation> getReservations() { return reservations; }

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
            if (r.getCheckInDate().isEqual(today) ||
                r.getCheckOutDate().isEqual(today)) {
                result.add(r);
            }
        }
        return result;
    }
}