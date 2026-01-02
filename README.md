
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

- Vehicle Entry Registration: Record details of vehicles entering the premises, including timestamp, vehicle number, and purpose.  
- Vehicle Exit Processing: Log vehicle departures, automatically pairing them with entry records to calculate duration if needed.  
- Transaction History: View a complete, searchable log of all entry and exit events stored in the database.  
- Data Search and Filter: Quickly find specific records by vehicle number, date range, or transaction type.  

### Technical Features

- Graphical User Interface (GUI): A responsive and user-friendly interface built with JavaFX and styled with CSS.  
- Persistent Data Storage: All transactions are stored in a relational database (MySQL/MariaDB), ensuring data is not lost between application sessions.  
- Structured Project: Organized codebase following standard Java and Gradle conventions for maintainability.  

## Technology Stack

- Programming Language: Java  
- User Interface Framework: JavaFX  
- Styling: CSS  
- Database: MySQL or MariaDB  
- Build & Dependency Management: Gradle  
- Recommended IDE: IntelliJ IDEA, Eclipse, or NetBeans  

## Installation

### Prerequisites

Ensure the following software is installed and configured on your system:

1. Java Development Kit (JDK): Version 17 or higher.  
2. Database Server: MySQL or MariaDB server instance.  
3. Git: For cloning the repository.  

### Steps

1. Clone the repository to your local machine:

```bash
git clone https://github.com/Lushomo53/Vehicle-Gate-System.git
cd Vehicle-Gate-System

Database Setup

The application requires a pre-configured database schema to function correctly.

1. Create the Database: Access your MySQL server (e.g., via command line or a tool like phpMyAdmin) and execute:



CREATE DATABASE vehicle_gate;

2. Import the Schema: Locate the SQL script files in the db_schema/ directory of the project. Import the main schema.sql file into your newly created database:



mysql -u [your_username] -p vehicle_gate < db_schema/schema.sql

(You will be prompted for your database password.)

3. Configure Connection: The application needs to know how to connect to your database.

Copy the file .env.example and rename the copy to .env.

Open the .env file and update the database connection variables with your specific details:




DB_HOST=localhost
DB_PORT=3306
DB_NAME=vehicle_gate
DB_USER=your_database_username
DB_PASS=your_database_password

Running the Application

You can run the application using the included Gradle wrapper, which handles 
