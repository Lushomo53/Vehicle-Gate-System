# Vehicle Gate System

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A comprehensive Java desktop application designed to manage and log vehicle entry and exit transactions at institutional gates, such as corporate offices, university campuses, or secure facilities.

## Table of Contents

- [About](#about)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Installation](#installation)
- [Database Setup](#database-setup)
- [Running the Application](#running-the-application)
- [Usage Guide](#usage-guide)
- [Security and Validation](#security-and-validation)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)
- [Future Enhancements](#future-enhancements)

## About

The Vehicle Gate System addresses the need for a reliable, digital record-keeping solution for gate traffic. It replaces manual logbooks or basic spreadsheets with a persistent database and an intuitive graphical interface. The application is built with JavaFX, providing a modern user experience, and uses a MySQL/MariaDB backend to ensure data integrity and allow for historical querying and reporting.

## Features

### Core Functionality

- **Vehicle Entry Registration**: Record details of vehicles entering the premises, including timestamp, vehicle number, and purpose.
- **Vehicle Exit Processing**: Log vehicle departures, automatically pairing them with entry records to calculate duration if needed.
- **Transaction History**: View a complete, searchable log of all entry and exit events stored in the database.
- **Data Search and Filter**: Quickly find specific records by vehicle number, date range, or transaction type.

### Technical Features

- **Graphical User Interface (GUI)**: A responsive and user-friendly interface built with JavaFX and styled with CSS.
- **Persistent Data Storage**: All transactions are stored in a relational database (MySQL/MariaDB), ensuring data is not lost between application sessions.
- **Structured Project**: Organized codebase following standard Java and Gradle conventions for maintainability.

## Technology Stack

- **Programming Language**: Java
- **User Interface Framework**: JavaFX
- **Styling**: CSS
- **Database**: MySQL or MariaDB
- **Build & Dependency Management**: Gradle
- **Recommended IDE**: IntelliJ IDEA, Eclipse, or NetBeans

## Installation

### Prerequisites

Ensure the following software is installed and configured on your system:

1. **Java Development Kit (JDK)**: Version 17 or higher
2. **Database Server**: MySQL or MariaDB server instance
3. **Git**: For cloning the repository

### Steps

1. Clone the repository to your local machine:

   ```bash
   git clone https://github.com/Lushomo53/Vehicle-Gate-System.git
   cd Vehicle-Gate-System
   ```

## Database Setup

The application requires a pre-configured database schema to function correctly.

1. **Create the Database**: Access your MySQL server (e.g., via command line or a tool like phpMyAdmin) and execute:

   ```sql
   CREATE DATABASE vehicle_gate;
   ```

2. **Import the Schema**: Locate the SQL script files in the `db_schema/` directory of the project. Import the main `schema.sql` file into your newly created database:

   ```bash
   mysql -u [your_username] -p vehicle_gate < db_schema/schema.sql
   ```
   
   *You will be prompted for your database password.*

3. **Configure Connection**: The application needs to know how to connect to your database.
   - Copy the file `.env.example` and rename the copy to `.env`
   - Open the `.env` file and update the database connection variables with your specific details:
   
     ```
     DB_HOST=localhost
     DB_PORT=3306
     DB_NAME=vehicle_gate
     DB_USER=your_database_username
     DB_PASS=your_database_password
     ```

## Running the Application

You can run the application using the included Gradle wrapper, which handles dependencies and the build process.

1. Navigate to the project's root directory (where the `gradlew` file is located)
2. Run the application with the following command:

   ```bash
   ./gradlew run
   ```
   
   *On Windows, use `gradlew.bat run`*

Alternatively, you can import the project into your preferred IDE (like IntelliJ IDEA) as a Gradle project and run the main class from there.

## Usage Guide

1. **Launch**: Start the application. You should be presented with the main dashboard.
2. **Register Entry**: To log a vehicle entering, navigate to the entry section, input the vehicle's details (e.g., license plate number, driver name, purpose), and submit.
3. **Register Exit**: To log a vehicle leaving, go to the exit section, typically by searching for the vehicle's active entry record, and confirm the exit.
4. **View History**: Use the history or logs section to browse all past transactions. Utilize search and filter options to find specific records.

## Security and Validation

- **Input Validation**: User input in forms is validated to prevent incorrect data types or obviously invalid entries from being submitted.
- **Database Security**: Database credentials are managed through the `.env` configuration file, which should never be committed to version control. The provided `.env.example` file serves as a safe template.
- **Secure Queries**: The application utilizes prepared statements for all database queries. This is a critical practice to prevent SQL injection attacks, ensuring user input is treated as data, not executable code.

## Project Structure

```
Vehicle-Gate-System/
├── gradle/                          # Gradle wrapper files
├── src/
│   └── main/
│       ├── java/                    # Application source code
│       └── resources/               # FXML files, CSS, images
├── db_schema/                       # SQL scripts for database setup
├── .env.example                     # Template for environment configuration
├── build.gradle.kts                 # Gradle build script
├── settings.gradle.kts
├── gradlew                          # Gradle wrapper for Unix/Linux
├── gradlew.bat                      # Gradle wrapper for Windows
└── README.md                        # This file
```

## Contributing

Contributions to improve the Vehicle Gate System are welcome. If you would like to contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

Please ensure any code contributions follow the existing project style and conventions.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file in the repository root for the full text.

## Future Enhancements

The system can be extended with several advanced features:

- **Reporting Module**: Add functionality to export transaction logs to PDF or CSV format for archival or analysis.
- **Network API**: Develop a REST API backend to allow integration with other systems (like a mobile app for remote logging).
- **Enhanced UI/UX**: Implement data visualization charts for daily traffic or use a more modern UI toolkit.
- **Role-Based Access Control**: Add authentication and authorization for different user roles (guards, administrators, etc.).
- **Automated Notifications**: Send alerts for suspicious activities or vehicles overstaying their expected duration.
- **Integration with CCTV**: Link vehicle records with camera footage for enhanced security.

---

**Note**: For issues, questions, or feature requests, please open an issue on the [GitHub repository](https://github.com/Lushomo53/Vehicle-Gate-System/issues).
