package com.sentient_pms.repository;

import com.sentient_pms.model.Guest;
import com.sentient_pms.model.Reservation;
import com.sentient_pms.model.Room;
import com.sentient_pms.model.RoomStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {
    List<Reservation> reservations = new ArrayList<Reservation>();

    public ReservationRepository() {
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

    public List<Reservation> getReservations() { return reservations; }

    public List<Reservation> getPastReservations() {
        List<Reservation> result = new ArrayList<Reservation>();
        LocalDate today = LocalDate.now();

        for (Reservation r : reservations) {
            if (r.getCheckOutDate().isBefore(today)) {
                result.add(r);
            }
        }
        return result;
    }

    public List<Reservation> getUpcomingReservations() {
        List<Reservation> result = new ArrayList<Reservation>();
        LocalDate today = LocalDate.now();

        for (Reservation r : reservations) {
            if (r.getCheckInDate().isAfter(today)) {
                result.add(r);
            }
        }
        return result;
    }

    public List<Reservation> getTodayReservations() {
        List<Reservation> result = new ArrayList<Reservation>();
        LocalDate today = LocalDate.now();

        for (Reservation r : reservations) {
            if (r.getCheckInDate().isEqual(today) ||
                    r.getCheckOutDate().isEqual(today)) {
                result.add(r);
            }
        }
        return result;
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }
}