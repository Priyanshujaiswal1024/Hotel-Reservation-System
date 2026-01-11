# 🏨 Hotel Reservation System (Java + JDBC)

A robust console-based Hotel Reservation System developed using **Java** and **JDBC**. This project demonstrates real-world database connectivity, CRUD operations, and modular software design.

---

## 📌 Project Overview
This project serves as a practical implementation of how Java applications interact with relational databases. It provides a menu-driven interface for managing hotel room bookings efficiently.

---

## ✨ Features
- **Reserve a Room:** Add new guest details and room assignments to the database.
- **View All Reservations:** Display a complete list of current bookings.
- **Fetch Reservation:** Search for specific room numbers using a Reservation ID and Guest Name.
- **Update Details:** Modify existing reservation information (Guest Name, Room Number, etc.).
- **Delete Reservations:** Remove completed or cancelled bookings from the system.
- **Menu-Driven UI:** A user-friendly console interface for easy navigation.

---

## 🛠️ Technologies Used
- **Language:** Java (JDK 8+)
- **Database:** MySQL
- **Driver:** JDBC (mysql-connector-j)
- **IDE:** IntelliJ IDEA

---

## 📂 Project Structure
```text
src/
├── db/
│   └── DBConnection.java       # Manages the JDBC connection to MySQL
├── service/
│   └── ReservationService.java  # Contains business logic for hotel operations
└── ui/
    └── HotelReservationSystem.java  # The main entry point and user interface