-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Cze 29, 2024 at 07:13 PM
-- Wersja serwera: 10.4.32-MariaDB
-- Wersja PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `shop`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `customers_tbl`
--

CREATE TABLE `customers_tbl` (
  `customer_id` int(11) NOT NULL,
  `customer_name` varchar(250) NOT NULL,
  `customer_surname` varchar(250) NOT NULL,
  `company_name` varchar(250) NOT NULL,
  `number` varchar(9) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customers_tbl`
--

INSERT INTO `customers_tbl` (`customer_id`, `customer_name`, `customer_surname`, `company_name`, `number`) VALUES
(3, 'John', 'Snowed', 'Winter', '111333555'),
(4, 'Victor', 'Mow', 'Vinis', '456654787'),
(5, 'Mike', 'Wazowsky', 'Aliens', '999555999'),
(6, 'Jacob', 'Sour', 'Sourcy', '556677858');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `orders_tbl`
--

CREATE TABLE `orders_tbl` (
  `order_id` int(11) NOT NULL,
  `id_customer` int(11) NOT NULL,
  `id_product` int(11) NOT NULL,
  `order_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `order_name` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders_tbl`
--

INSERT INTO `orders_tbl` (`order_id`, `id_customer`, `id_product`, `order_date`, `order_name`) VALUES
(2, 5, 7, '2024-06-25 10:49:02', 'Bananas'),
(3, 4, 2, '2024-06-25 10:49:37', 'Papers'),
(5, 6, 5, '2024-06-25 11:34:25', 'Butters'),
(6, 4, 3, '2024-06-25 11:36:56', 'PaperShop');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `products_tbl`
--

CREATE TABLE `products_tbl` (
  `id` int(11) NOT NULL,
  `product_name` varchar(250) NOT NULL,
  `price` varchar(250) NOT NULL,
  `qty` varchar(250) NOT NULL,
  `usage_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products_tbl`
--

INSERT INTO `products_tbl` (`id`, `product_name`, `price`, `qty`, `usage_date`) VALUES
(1, 'Bread', '2,99', '10', '2024-07-13'),
(2, 'Paper', '1,99', '15', '2028-04-26'),
(3, 'Water', '1,49', '30', '2028-05-05'),
(5, 'Butter', '4,99', '20', '2025-12-01'),
(6, 'Apple', '0,99', '25', '2024-07-15'),
(7, 'Banana', '0,99', '15', '2024-07-05');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `customers_tbl`
--
ALTER TABLE `customers_tbl`
  ADD PRIMARY KEY (`customer_id`);

--
-- Indeksy dla tabeli `orders_tbl`
--
ALTER TABLE `orders_tbl`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `id_customer` (`id_customer`),
  ADD KEY `id_product` (`id_product`);

--
-- Indeksy dla tabeli `products_tbl`
--
ALTER TABLE `products_tbl`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customers_tbl`
--
ALTER TABLE `customers_tbl`
  MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `orders_tbl`
--
ALTER TABLE `orders_tbl`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `products_tbl`
--
ALTER TABLE `products_tbl`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `orders_tbl`
--
ALTER TABLE `orders_tbl`
  ADD CONSTRAINT `orders_tbl_ibfk_1` FOREIGN KEY (`id_customer`) REFERENCES `customers_tbl` (`customer_id`),
  ADD CONSTRAINT `orders_tbl_ibfk_2` FOREIGN KEY (`id_product`) REFERENCES `products_tbl` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
