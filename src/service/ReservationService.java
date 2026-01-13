package service;

import java.sql.*;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.util.Scanner;

public class ReservationService {
    public static void reserveRoom(Connection connection, Scanner scanner) {
        try {

            System.out.print("Enter guest name: ");
            String guestName = scanner.next();
            scanner.nextLine();
            System.out.print("Enter room number: ");
            int roomNumber = scanner.nextInt();
            System.out.print("Enter contact number: ");
            String contactNumber = scanner.next();
            if (!isRoomAvailable(connection, roomNumber)) {
                System.out.println("Room already booked. Please choose another room.");
                return;
            }
            connection.setAutoCommit(false);
            String sql ="Insert into  reservations ( guest_name, room_number, contact_number) values(?,?,?) ";
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, guestName);
                ps.setInt(2, roomNumber);
                ps.setString(3, contactNumber);
                int affectedRows = ps.executeUpdate();

                if (affectedRows > 0) {
                    connection.commit();
                    System.out.println("Reservation successful!");
                } else {
                    connection.rollback();
                    System.out.println("Reservation failed.");
                }
            }
        } catch (SQLException e) {
            try
            {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    public static void viewReservations(Connection connection) throws SQLException {
        String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet resultSet = ps.executeQuery() ){

            System.out.println("Current Reservations:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number      | Reservation Date        |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

            while (resultSet.next()) {
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

                // Format and display the reservation data in a table-like format
                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                        reservationId, guestName, roomNumber, contactNumber, reservationDate);
            }

            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
        }

    }
    public static void getRoomNumber(Connection connection, Scanner scanner) {
        try {

            System.out.print("Enter reservation ID: ");
            int reservationId = scanner.nextInt();
            System.out.print("Enter guest name: ");
            String guestName = scanner.next();

            String sql = "select room_number from reservations where reservation_id=? and guest_name=?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, reservationId);
                ps.setString(2, guestName);
                try (ResultSet resultSet = ps.executeQuery()) {

                    if (resultSet.next()) {
                        int roomNumber = resultSet.getInt("room_number");
                        System.out.println("Room number for Reservation ID " + reservationId +
                                " and Guest " + guestName + " is: " + roomNumber);
                    } else {
                        System.out.println("Reservation not found for the given ID and guest name.");
                    }
                }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }
    public static void updateReservation(Connection connection, Scanner scanner) {
        try {

            System.out.print("Enter reservation ID to update: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }
            connection.setAutoCommit(false);
            System.out.print("Enter new guest name: ");
            String newGuestName = scanner.nextLine();
            System.out.print("Enter new room number: ");
            int newRoomNumber = scanner.nextInt();
            System.out.print("Enter new contact number: ");
            String newContactNumber = scanner.next();

            String sql=" update reservations set guest_name=?, room_number=?, contact_number=? where reservation_id=?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, newGuestName);
                ps.setInt(2, newRoomNumber);
                ps.setString(3, newContactNumber);
                ps.setInt(4, reservationId);
                int affectedRows = ps.executeUpdate();

                if (affectedRows > 0) {
                    connection.commit();
                    System.out.println("Reservation updated successfully!");
                } else {
                    connection.rollback();
                    System.out.println("Reservation update failed.");
                }
            }
        } catch (SQLException e) {
            try
            {
                connection.rollback();
                System.out.println("Rolled Back. update failed.");

            }
            catch ( SQLException ex)
            {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void deleteReservation(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter reservation ID to delete: ");
            int reservationId = scanner.nextInt();

            if (!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }
                connection.setAutoCommit(false);
            String sql = "DELETE FROM reservations WHERE reservation_id = ?" ;

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, reservationId);
                int affectedRows = ps.executeUpdate();

                if (affectedRows > 0) {
                    connection.commit();
                    System.out.println("Reservation deleted successfully!");
                } else {
                    connection.rollback();
                    System.out.println("Reservation deletion failed.");
                }
            }
        } catch (SQLException e) {
            try
            {
                connection.rollback();
                System.out.println("Rolled Back. update failed.");
            }
            catch ( SQLException ex)
            {
                    ex.printStackTrace();
            }
            e.printStackTrace();
        }
        finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static boolean reservationExists(Connection connection, int reservationId) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = ?";

            try (PreparedStatement ps = connection.prepareStatement(sql))

                    {
                        ps.setInt(1, reservationId);

                        try (ResultSet resultSet = ps.executeQuery()) {
                            return resultSet.next();
                        }// If there's a result, the reservation exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }


    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i = 5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(1000);
            i--;
        }
        System.out.println();
        System.out.println("ThankYou For Using Hotel Reservation System!!!");
    }
    public static boolean isRoomAvailable(Connection connection, int roomNumber) {
        String sql = "SELECT 1 FROM reservations WHERE room_number=?";
            try (PreparedStatement ps= connection.prepareStatement(sql)) {
                ps.setInt(1, roomNumber);

                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        return false;
                    }
                    else {
                        return true;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
    }
}
