-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: May 29, 2016 at 12:31 PM
-- Server version: 10.1.10-MariaDB
-- PHP Version: 7.0.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `smart_route_finder`
--

-- --------------------------------------------------------

--
-- Table structure for table `drivers_info`
--

CREATE TABLE `drivers_info` (
  `DID` int(11) NOT NULL,
  `username` varchar(15) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `vehicle_num` varchar(255) NOT NULL,
  `vehicle_name` varchar(255) NOT NULL,
  `phone_num` varchar(20) NOT NULL,
  `address` varchar(512) NOT NULL,
  `isOnline` int(2) NOT NULL DEFAULT '0',
  `isHired` int(11) NOT NULL DEFAULT '0',
  `location` varchar(255) NOT NULL,
  `registeredOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `drivers_info`
--

INSERT INTO `drivers_info` (`DID`, `username`, `full_name`, `password`, `vehicle_num`, `vehicle_name`, `phone_num`, `address`, `isOnline`, `isHired`, `location`, `registeredOn`) VALUES
(1, 'tahir', 'MD Husnain Tahir', '000000', '123456', 'Vehicle Name', '03315450046', 'Rawalpindi', 1, 0, '33.621483:72.97385', '2016-05-02 15:12:25'),
(2, 'husnain', 'MD Husnain Tahir', '000000', '123456', 'Vehicle Name', '03315450046', 'Rawalpindi', 1, 0, '33.621483:73.23381', '2016-05-02 15:12:25');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `drivers_info`
--
ALTER TABLE `drivers_info`
  ADD PRIMARY KEY (`DID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `drivers_info`
--
ALTER TABLE `drivers_info`
  MODIFY `DID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
