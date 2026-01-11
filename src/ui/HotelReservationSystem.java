package ui;

import db.DBConnection;
import service.ReservationService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class HotelReservationSystem {
    static void main(String[] args) throws SQLException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        Connection connection = DBConnection.getConnection();


        while (true) {
            System.out.println();
            System.out.println("HOTEL MANAGEMENT SYSTEM");

            System.out.println("1. Reserve a room");
            System.out.println("2. View Reservations");
            System.out.println("3. Get Room Number");
            System.out.println("4. Update Reservations");
            System.out.println("5. Delete Reservations");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    ReservationService.reserveRoom(connection, scanner);
                    break;
                case 2:

                        ReservationService.viewReservations(connection);

                    break;
                case 3:
                    ReservationService.getRoomNumber(connection, scanner);
                    break;
                case 4:
                    ReservationService.updateReservation(connection, scanner);
                    break;
                case 5:
                    ReservationService.deleteReservation(connection, scanner);
                    break;
                case 0:

                        ReservationService.exit();

                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }


    }

}

