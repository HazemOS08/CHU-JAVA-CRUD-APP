# Hospital Management System (HMS)

A robust, console-based Java and SQLite application featuring secure role-based access control, password hashing, input validation, and dynamic data management for hospital operations.

## Demo
Watch the video demonstration of the system in action

## Inspiration
This project was inspired by my  background at CHU Sainte-Justine, giving me practical insight into how hospital workflows, staff management, and secure data handling function in a real healthcare environment.

## Features & Role-Based Access Control

### Administrator Control
* Full system oversight and permissions management.
* Oversees user accounts and system configurations.

### Manager Control (with Restrictions)
* Manages department assignments and staff allocation.
* Restricted to managing regular users (non-managers/admins) strictly within their own department.

### Regular User
* View profile details, update personal information, and check assigned department schedules or tasks.

## Security & Data Integrity

* **Password Hashing:** User passwords are secured using SHA-256 hashing logic.
* **Strict Validation:** Every piece of entered data, username checks, and input fields undergo rigorous validation before interacting with the database to prevent errors and ensure integrity.

## Getting Started & Setup

1. Clone or download this repository.
2. Ensure you have Java installed on your machine.
3. Open the project in your preferred Java IDE (like IntelliJ or VsCode).
4. **Simply run `Main.java`**: The database will automatically initialize itself upon first launch.
   > **Note:** The necessary JDBC driver JAR file is located in the `db/` folder. Ensure your IDE configuration recognizes it in the build path if module/library settings require it to work properly.
### Test Credentials
* **Admin Account:** Use `admin` as the username and `admin123` as the password.
* **Manager Account:** Use `sconner` as the username and `pass123`
* **Regular User Account:** Use `jdoe` as the username and `pass123`
  Feel free to use any other credentials to test different departments/schedules.
  
## Limitations & Future Optimizations

* **Console-Based Interface:** Currently limited to a command-line interface (CLI), which could be expanded into a full GUI or web-based frontend in future iterations.
* **Query Optimization and Scalability:** Some database queries and relational joins involving staff schedules and department lookups can be further indexed and optimized for larger datasets.
