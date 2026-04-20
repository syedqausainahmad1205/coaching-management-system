# Coaching Management System

A production-ready **Java Swing + SQL** desktop application for managing a coaching center.

## Features
- Dashboard with live statistics
- Student Management (CRUD + search)
- Course Management (CRUD + search)
- Instructor Management (CRUD + search)
- Enrollment Management
- Attendance Tracking
- Fee/Payment Management
- Reports and analytics
- Role-based login entry for Student and Teacher accounts
- Input validation and user-friendly errors
- MVC-style layering: GUI, service, DAO, model

## Tech Stack
- Java 17
- Swing
- SQLite (SQL database)
- Maven

## Project Structure
- `src/main/java/com/coaching/model` – domain models
- `src/main/java/com/coaching/dao` – SQL data access
- `src/main/java/com/coaching/service` – business logic
- `src/main/java/com/coaching/gui` – Swing views/controllers
- `src/main/java/com/coaching/util` – config, DB pool, validation, logging, setup
- `database/schema.sql` – DB initialization script
- `config/db.properties` – DB configuration

## Setup
1. Ensure Java 17+ and Maven are installed.
2. Configure database in `config/db.properties`.
3. Build and test:
   ```bash
   mvn clean test
   ```
4. Run:
   ```bash
   mvn exec:java
   ```

The app auto-initializes tables from `database/schema.sql` on startup.

## Login
- Launch the app and choose **Student** or **Teacher** on the login screen.
- Credentials are based on records from **Students** and **Instructors** modules.
- While creating records, set a password (minimum 6 characters). During update, leave password blank to keep the current one.
