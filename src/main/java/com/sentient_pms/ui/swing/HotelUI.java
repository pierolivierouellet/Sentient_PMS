package com.sentient_pms.ui.swing;

import com.sentient_pms.model.Guest;
import com.sentient_pms.model.RoomStatus;
import com.sentient_pms.repository.ReservationRepository;
import com.sentient_pms.repository.RoomRepository;
import com.sentient_pms.model.Reservation;
import com.sentient_pms.model.Room;
import com.sentient_pms.service.ReservationService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class HotelUI {

    private RoomRepository roomRepository;
    private ReservationRepository reservationRepository;
    private ReservationService reservationService;
    private JPanel outputPanel;

    public HotelUI() {

        roomRepository = new RoomRepository();
        reservationRepository = new ReservationRepository();
        reservationService = new ReservationService(reservationRepository, roomRepository);


        JFrame frame = new JFrame("Sentient PMS");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // OUTPUT PANEL (supports buttons)
        outputPanel = new JPanel();
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(outputPanel);

        JPanel panel = new JPanel(new GridLayout(4, 3));

        JButton roomsBtn = new JButton("View Rooms");
        JButton reservationMenuBtn = new JButton("Reservations");
        JButton createBtn = new JButton("Create Reservation");
        JButton checkInBtn = new JButton("Check-In (Manual)");
        JButton checkOutBtn = new JButton("Check-Out (Manual)");
        JButton inHouseBtn = new JButton("In-House Guests");
        JButton changeStatusBtn = new JButton("Change Room Status");
        JButton batchCleanBtn = new JButton("Batch Clean Rooms");
        JButton gridBtn = new JButton("Room Grid");
        JButton exitBtn = new JButton("Exit");

        // VIEW ROOMS
        roomsBtn.addActionListener(e -> {
            outputPanel.removeAll();
            for (Room r : roomRepository.getRooms()) {
                outputPanel.add(new JLabel(r.toString()));
            }
            refreshUI();
        });

        // RESERVATIONS MENU
        reservationMenuBtn.addActionListener(e -> {

            String[] options = {"Past", "Today", "Upcoming"};

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

            outputPanel.removeAll();

            List<Reservation> list = null;

            if (choice == 0) list = this.reservationRepository.getPastReservations();
            if (choice == 1) list = this.reservationRepository.getTodayReservations();
            if (choice == 2) list = this.reservationRepository.getUpcomingReservations();

            if (list != null) {

                for (Reservation r : list) {

                    JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JLabel label = new JLabel(r.toString());
                    row.add(label);

                    if (choice == 1) {

                        JButton actionBtn;

                        if (r.getStatus().equals("BOOKED")) {

                            actionBtn = new JButton("Check-In");

                            actionBtn.addActionListener(ev -> {
                                reservationRepository.checkIn(r.getId());
                                refreshTodayView();
                            });

                        } else if (r.getStatus().equals("CHECKED_IN")) {

                            actionBtn = new JButton("Check-Out");

                            actionBtn.addActionListener(ev -> {

                                int confirm = JOptionPane.showConfirmDialog(
                                        null,
                                        "Confirm check-out for this guest?",
                                        "Confirm Check-Out",
                                        JOptionPane.YES_NO_OPTION
                                );

                                if (confirm == JOptionPane.YES_OPTION) {
                                    reservationRepository.checkOut(r.getId());
                                    refreshTodayView();
                                }
                            });

                        } else {
                            actionBtn = new JButton("Done");
                            actionBtn.setEnabled(false);
                        }

                        row.add(actionBtn);
                    }

                    outputPanel.add(row);
                }
            }

            refreshUI();
        });

        // CREATE RESERVATION
        createBtn.addActionListener(e -> {
            try {
                String first = JOptionPane.showInputDialog("First name:");
                String last = JOptionPane.showInputDialog("Last name:");
                int room = Integer.parseInt(JOptionPane.showInputDialog("Room number:"));
                LocalDate in = LocalDate.parse(JOptionPane.showInputDialog("Check-in (YYYY-MM-DD):"));
                LocalDate out = LocalDate.parse(JOptionPane.showInputDialog("Check-out (YYYY-MM-DD):"));

                this.reservationService.createReservation(first, last, room, in, out);

                outputPanel.removeAll();
                outputPanel.add(new JLabel("Reservation created."));
                refreshUI();

            } catch (Exception ex) {
                outputPanel.removeAll();
                outputPanel.add(new JLabel("Invalid input."));
                refreshUI();
            }
        });

        // MANUAL CHECK-IN
        checkInBtn.addActionListener(e -> {
            try {
                long id = Long.parseLong(JOptionPane.showInputDialog("Reservation ID:"));
                reservationRepository.checkIn(id);

                outputPanel.removeAll();
                outputPanel.add(new JLabel("Checked in."));
                refreshUI();
            } catch (Exception ex) {
                outputPanel.removeAll();
                outputPanel.add(new JLabel("Invalid input."));
                refreshUI();
            }
        });

        // MANUAL CHECK-OUT (WITH CONFIRMATION)
        checkOutBtn.addActionListener(e -> {
            try {
                long id = Long.parseLong(JOptionPane.showInputDialog("Reservation ID:"));

                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Confirm check-out for this guest?",
                        "Confirm Check-Out",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    reservationRepository.checkOut(id);

                    outputPanel.removeAll();
                    outputPanel.add(new JLabel("Checked out."));
                    refreshUI();
                }

            } catch (Exception ex) {
                outputPanel.removeAll();
                outputPanel.add(new JLabel("Invalid input."));
                refreshUI();
            }
        });

        // IN-HOUSE
        inHouseBtn.addActionListener(e -> {
            outputPanel.removeAll();

            for (Reservation r : reservationRepository.getReservations()) {
                if (r.isCheckedIn()) {
                    outputPanel.add(new JLabel(r.toString()));
                }
            }

            refreshUI();
        });

        // CHANGE ROOM STATUS
        changeStatusBtn.addActionListener(e -> {
            try {
                int room = Integer.parseInt(JOptionPane.showInputDialog("Room number:"));

                String[] options = {"AVAILABLE", "DIRTY", "OUT_OF_ORDER"};
                int choice = JOptionPane.showOptionDialog(
                        null,
                        "Select status:",
                        "Room Status",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (choice >= 0) {
                    roomRepository.setRoomStatus(room, RoomStatus.valueOf(options[choice]));
                }

                outputPanel.removeAll();
                outputPanel.add(new JLabel("Room updated."));
                refreshUI();

            } catch (Exception ex) {
                outputPanel.removeAll();
                outputPanel.add(new JLabel("Invalid input."));
                refreshUI();
            }
        });

        // BATCH CLEAN
        batchCleanBtn.addActionListener(e -> {
            try {
                int start = Integer.parseInt(JOptionPane.showInputDialog("Start room:"));
                int end = Integer.parseInt(JOptionPane.showInputDialog("End room:"));

                roomRepository.cleanRoomsRange(start, end);

                outputPanel.removeAll();
                outputPanel.add(new JLabel("Rooms cleaned."));
                refreshUI();

            } catch (Exception ex) {
                outputPanel.removeAll();
                outputPanel.add(new JLabel("Invalid input."));
                refreshUI();
            }
        });

        // ROOM GRID
        gridBtn.addActionListener(e -> {

            JFrame gridFrame = new JFrame("Room Grid");
            gridFrame.setSize(400, 400);
            gridFrame.setLayout(new GridLayout(0, 5));

            for (Room room : roomRepository.getRooms()) {

                JButton btn = new JButton(String.valueOf(room.getRoomNumber()));

                switch (room.getRoomStatus()) {
                    case AVAILABLE:
                        btn.setBackground(Color.GREEN);
                        break;
                    case OCCUPIED:
                        btn.setBackground(Color.RED);
                        break;
                    case DIRTY:
                        btn.setBackground(Color.YELLOW);
                        break;
                    case OUT_OF_ORDER:
                        btn.setBackground(Color.DARK_GRAY);
                        break;
                }

                btn.setOpaque(true);
                btn.setBorderPainted(false);

                btn.addActionListener(ev ->
                        JOptionPane.showMessageDialog(null, room.toString())
                );

                gridFrame.add(btn);
            }

            gridFrame.setVisible(true);
        });

        // EXIT
        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(roomsBtn);
        panel.add(reservationMenuBtn);
        panel.add(createBtn);
        panel.add(checkInBtn);
        panel.add(checkOutBtn);
        panel.add(inHouseBtn);
        panel.add(changeStatusBtn);
        panel.add(batchCleanBtn);
        panel.add(gridBtn);
        panel.add(exitBtn);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void refreshUI() {
        outputPanel.revalidate();
        outputPanel.repaint();
    }

    private void refreshTodayView() {

        outputPanel.removeAll();

        List<Reservation> list = reservationRepository.getTodayReservations();

        for (Reservation r : list) {

            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel label = new JLabel(r.toString());
            row.add(label);

            JButton actionBtn;

            if (r.getStatus().equals("BOOKED")) {

                actionBtn = new JButton("Check-In");

                actionBtn.addActionListener(ev -> {
                    reservationRepository.checkIn(r.getId());
                    refreshTodayView();
                });

            } else if (r.getStatus().equals("CHECKED_IN")) {

                actionBtn = new JButton("Check-Out");

                actionBtn.addActionListener(ev -> {

                    int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "Confirm check-out for this guest?",
                            "Confirm Check-Out",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        reservationRepository.checkOut(r.getId());
                        refreshTodayView();
                    }
                });

            } else {
                actionBtn = new JButton("Done");
                actionBtn.setEnabled(false);
            }

            row.add(actionBtn);
            outputPanel.add(row);
        }

        refreshUI();
    }
}

