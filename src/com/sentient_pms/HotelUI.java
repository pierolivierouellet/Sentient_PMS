package com.sentient_pms;

import javax.swing.*;
import java.awt.*;

public class HotelUI {

    private Hotel hotel;

    public HotelUI() {
        hotel = new Hotel();

        JFrame frame = new JFrame("Hotel PMS");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout
        frame.setLayout(new BorderLayout());

        // Output area
        JTextArea output = new JTextArea();
        output.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(output);

        // Buttons
        JButton viewRoomsBtn = new JButton("View Rooms");

        viewRoomsBtn.addActionListener(e -> {
            output.setText(""); // clear

            for (Room room : hotel.getRooms()) {
                output.append(room.toString() + "\n");
            }
        });

        // Panel for buttons
        JPanel panel = new JPanel();
        panel.add(viewRoomsBtn);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}