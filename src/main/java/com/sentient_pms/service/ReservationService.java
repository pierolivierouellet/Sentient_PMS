package com.sentient_pms.service;

import com.sentient_pms.model.Guest;
import com.sentient_pms.model.Reservation;
import com.sentient_pms.model.Room;
import com.sentient_pms.repository.ReservationRepository;
import com.sentient_pms.repository.RoomRepository;

import java.time.LocalDate;

public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    public void createReservation(String firstName, String lastName, int roomNumber,
                                  LocalDate checkIn, LocalDate checkOut) {

        Room room = roomRepository.findRoomByNumber(roomNumber);

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

        reservationRepository.addReservation(reservation);
        System.out.println("Reservation created.");
    }

    public boolean isRoomBooked(Room room, LocalDate checkIn, LocalDate checkOut) {
        for (Reservation r : reservationRepository.getReservations()) {
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

}