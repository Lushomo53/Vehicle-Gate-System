-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 02, 2026 at 11:41 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gate_system_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `entry_logs`
--

CREATE TABLE `entry_logs` (
  `id` int(11) NOT NULL,
  `car_name` varchar(100) NOT NULL,
  `model` varchar(100) NOT NULL,
  `license_plate` varchar(20) NOT NULL,
  `owner_name` varchar(150) NOT NULL,
  `entry_time` datetime NOT NULL,
  `exit_time` datetime DEFAULT NULL,
  `status` enum('IN','OUT') NOT NULL,
  `guard_username` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `entry_logs`
--

INSERT INTO `entry_logs` (`id`, `car_name`, `model`, `license_plate`, `owner_name`, `entry_time`, `exit_time`, `status`, `guard_username`) VALUES
(1, 'Toyota', 'Auris', 'ABC 1234', 'Diana Mwanambiya', '2026-01-02 22:50:51', NULL, 'IN', 'lush101'),
(2, 'Toyota', 'Auris', 'ABC 1234', 'Diana Mwanambiya', '2026-01-02 22:50:51', '2026-01-02 23:35:39', 'OUT', 'lush101'),
(3, 'Mercedes Benz', 'c200', 'BXG 7484', 'Lushomo Lungo', '2026-01-02 23:54:51', '2026-01-02 23:58:56', 'OUT', 'lush101');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(150) NOT NULL,
  `role` enum('ADMIN','GUARD','GUEST') NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `nrc` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `email`, `role`, `password_hash`, `first_name`, `last_name`, `nrc`) VALUES
(1, 'admin', 'lungolushomo21@gmail.com', 'ADMIN', 'blzg9DzuphU6xrGiPeKltw/Dw0QdqoNI8zmithlxibs=:LtYOzEcr0szIcSVhK02Djw==', 'System', 'Admin', '486517/16/1'),
(5, 'lush101', 'blantiresavgelevel21@gmail.com', 'GUARD', '+ARQJF61A9BdSr41euSF+ea8mdmJarXemOHSHoBiRpk=:O2xXhi35ZyjAmysDGj6/Yw==', 'Lushomo', 'Lungo', '486517/16/1');

-- --------------------------------------------------------

--
-- Table structure for table `vehicles`
--

CREATE TABLE `vehicles` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `model` varchar(100) NOT NULL,
  `license_plate` varchar(20) NOT NULL,
  `owner_name` varchar(150) NOT NULL,
  `latest_entry_time` datetime DEFAULT NULL,
  `latest_exit_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vehicles`
--

INSERT INTO `vehicles` (`id`, `name`, `model`, `license_plate`, `owner_name`, `latest_entry_time`, `latest_exit_time`) VALUES
(2, 'Toyota', 'Auris', 'ABC 1234', 'Diana Mwanambiya', '2026-01-02 23:02:33', '2026-01-02 23:35:39'),
(5, 'Mercedes Benz', 'c200', 'BXG 7484', 'Lushomo Lungo', '2026-01-02 23:54:51', '2026-01-02 23:58:56'),
(6, 'Honda', 'Civic', 'CBH 3374', 'George Bush', '2026-01-02 23:56:09', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `entry_logs`
--
ALTER TABLE `entry_logs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_entry_logs_license_plate` (`license_plate`),
  ADD KEY `idx_entry_logs_entry_time` (`entry_time`),
  ADD KEY `idx_entry_logs_status` (`status`),
  ADD KEY `idx_entry_license` (`license_plate`),
  ADD KEY `idx_entry_time` (`entry_time`),
  ADD KEY `idx_entry_status` (`status`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_users_role` (`role`);

--
-- Indexes for table `vehicles`
--
ALTER TABLE `vehicles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `license_plate` (`license_plate`),
  ADD KEY `idx_vehicle_license_plate` (`license_plate`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `entry_logs`
--
ALTER TABLE `entry_logs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `vehicles`
--
ALTER TABLE `vehicles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
