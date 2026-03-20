package com.sentient_pms;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class HotelUI {

    private Hotel hotel;
    private JTextArea output;

    public HotelUI() {

        hotel = new Hotel();

        JFrame frame = new JFrame("Sentient PMS");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Output area
        output = new JTextArea();
        output.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(output);

        // Buttons panel
        JPanel panel = new JPanel(new GridLayout(3, 3));

        JButton roomsBtn = new JButton("View Rooms");
        JButton reservationMenuBtn = new JButton("Reservations");
        JButton createBtn = new JButton("Create Reservation");
        JButton checkInBtn = new JButton("Check-In");
        JButton checkOutBtn = new JButton("Check-Out");
        JButton inHouseBtn = new JButton("In-House Guests");
        JButton guestFileBtn = new JButton("Open Guest File");
        JButton exitBtn = new JButton("Exit");

        // === VIEW ROOMS ===
        roomsBtn.addActionListener(e -> {
            output.setText("");
            for (Room r : hotel.getRooms()) {
                output.append(r + "\n");
            }
        });

        // === RESERVATION MENU ===
        reservationMenuBtn.addActionListener(e -> {

            String[] options = {
                    "Past Reservations",
                    "Today's Reservations",
                    "Upcoming Reservations"
            };

            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Select category:",
                    "Reservations",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            output.setText("");

            List<Reservation> list = null;

            if (choice == 0) list = hotel.getPastReservations();
            if (choice == 1) list = hotel.getTodayReservations();
            if (choice == 2) list = hotel.getUpcomingReservations();

            if (list != null && !list.isEmpty()) {
                for (Reservation r : list) {
                    output.append(r + "\n");
                }
            } else {
                output.setText("No reservations found.\n");
            }
        });

        // === CREATE RESERVATION ===
        createBtn.addActionListener(e -> {
            try {
                String firstName = JOptionPane.showInputDialog("First name:");
                String lastName = JOptionPane.showInputDialog("Last name:");
                int room = Integer.parseInt(JOptionPane.showInputDialog("Room number:"));
                LocalDate in = LocalDate.parse(JOptionPane.showInputDialog("Check-in (YYYY-MM-DD):"));
                LocalDate out = LocalDate.parse(JOptionPane.showInputDialog("Check-out (YYYY-MM-DD):"));

                hotel.createReservation(firstName, lastName, room, in, out);

                output.setText("Reservation processed.\n");

            } catch (Exception ex) {
                output.setText("Invalid input.\n");
            }
        });

        // === CHECK-IN ===
        checkInBtn.addActionListener(e -> {
            try {
                long id = Long.parseLong(JOptionPane.showInputDialog("Reservation ID:"));
                hotel.checkIn(id);
                output.setText("Check-in attempted.\n");
            } catch (Exception ex) {
                output.setText("Invalid input.\n");
            }
        });

        // === CHECK-OUT ===
        checkOutBtn.addActionListener(e -> {
            try {
                long id = Long.parseLong(JOptionPane.showInputDialog("Reservation ID:"));
                hotel.checkOut(id);
                output.setText("Check-out attempted.\n");
            } catch (Exception ex) {
                output.setText("Invalid input.\n");
            }
        });

        // === IN-HOUSE GUESTS ===
        inHouseBtn.addActionListener(e -> {
            output.setText("");
            for (Reservation r : hotel.getReservations()) {
                if (r.isCheckedIn()) {
                    output.append(r + "\n");
                }
            }
        });

        // === OPEN GUEST FILE ===
        guestFileBtn.addActionListener(e -> {
            try {
                long id = Long.parseLong(JOptionPane.showInputDialog("Reservation ID:"));

                for (Reservation r : hotel.getReservations()) {
                    if (r.getId() == id) {

                        Guest g = r.getGuest();

                        JTextArea area = new JTextArea(g.getDetails());
                        area.setEditable(false);

                        JOptionPane.showMessageDialog(
                                null,
                                new JScrollPane(area),
                                "Guest File",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        String phone = JOptionPane.showInputDialog("Phone:");
                        String email = JOptionPane.showInputDialog("Email:");
                        String notes = JOptionPane.showInputDialog("Notes:");
                        String payment = JOptionPane.showInputDialog("Payment Info:");

                        if (phone != null) g.setPhone(phone);
                        if (email != null) g.setEmail(email);
                        if (notes != null) g.setNotes(notes);
                        if (payment != null) g.setPaymentInfo(payment);

                        output.setText("Guest updated.\n");
                        return;
                    }
                }

                output.setText("Reservation not found.\n");

            } catch (Exception ex) {
                output.setText("Invalid input.\n");
            }
        });

        // === EXIT ===
        exitBtn.addActionListener(e -> System.exit(0));

        // Add buttons
        panel.add(roomsBtn);
        panel.add(reservationMenuBtn);
        panel.add(createBtn);
        panel.add(checkInBtn);
        panel.add(checkOutBtn);
        panel.add(inHouseBtn);
        panel.add(guestFileBtn);
        panel.add(exitBtn);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}



