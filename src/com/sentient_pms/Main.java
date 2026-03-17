package com.sentient_pms;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Hotel hotel = new Hotel();

        while (true) {

            System.out.println("\n=== HOTEL PMS ===");
            System.out.println("1. View Reservations");
            System.out.println("2. Create Reservation");
            System.out.println("3. Check-In Guest");
            System.out.println("4. Check-Out Guest");
            System.out.println("5. View In-House Guests");
            System.out.println("6. View All Rooms"); // ✅ NEW
            System.out.println("7. Exit"); // shifted
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();

            switch (choice) {

                case 1:
                    hotel.viewReservations();
                    break;

                case 2:
                    scanner.nextLine(); // clear buffer

                    System.out.print("Guest name: ");
                    String name = scanner.nextLine();

                    System.out.print("Room number: ");
                    int room = scanner.nextInt();

                    System.out.print("Check-in date (YYYY-MM-DD): ");
                    LocalDate checkIn = LocalDate.parse(scanner.next());

                    System.out.print("Check-out date (YYYY-MM-DD): ");
                    LocalDate checkOut = LocalDate.parse(scanner.next());

                    hotel.createReservation(name, room, checkIn, checkOut);
                    break;

                case 3:
                    System.out.print("Reservation ID: ");
                    int checkInId = scanner.nextInt();
                    hotel.checkIn(checkInId);
                    break;

                case 4:
                    System.out.print("Reservation ID: ");
                    int checkOutId = scanner.nextInt();
                    hotel.checkOut(checkOutId);
                    break;

                case 5:
                    hotel.viewInHouseGuests();
                    break;

                case 6:
                    hotel.viewAllRooms(); // ✅ NEW FEATURE
                    break;

                case 7:
                    System.out.println("Goodbye.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}

