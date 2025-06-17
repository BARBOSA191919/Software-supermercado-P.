-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 19-12-2024 a las 23:19:31
-- Versión del servidor: 8.0.30
-- Versión de PHP: 7.4.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `supermercado`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `fruvert`
--

CREATE TABLE `fruvert` (
  `id` int NOT NULL,
  `codigo` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `existencia` float NOT NULL,
  `iva` decimal(19,2) NOT NULL,
  `nombre` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `precio` float NOT NULL,
  `precio_final` float NOT NULL,
  `categoria_id` bigint NOT NULL,
  `descuento` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `fruvert`
--

INSERT INTO `fruvert` (`id`, `codigo`, `existencia`, `iva`, `nombre`, `precio`, `precio_final`, `categoria_id`, `descuento`) VALUES
(3, '200', 0, 19.00, 'Aguacate', 3850, 4581.5, 2, 0),
(4, '201', 24, 19.00, 'Bananos', 1300, 1547, 2, 0),
(8, '202', 11, 5.00, 'Sandia', 2000, 2100, 2, 0),
(9, '203', 25, 0.00, 'Coco', 3000, 3000, 2, 0),
(10, '204', 1, 19.00, 'Manzana roja', 3200, 3200, 2, 0),
(11, '205', 20, 19.00, 'Manzana verde', 1500, 1500, 2, 0),
(114, '206', 80, 19.00, 'Arandanos', 2560, 3046.4, 2, 0),
(115, '207', 80, 19.00, 'Berenjena', 2500, 2975, 2, 0),
(116, '207', 80, 19.00, 'Carambolo', 2400, 2856, 2, 0),
(117, '208', 80, 19.00, 'Ciruela', 500, 595, 2, 0),
(118, '210', 50, 19.00, 'Durazno', 650, 773.5, 2, 0),
(119, '211', 50, 19.00, 'Fresa', 5600, 6664, 2, 0),
(120, '212', 80, 19.00, 'Guanabana', 2500, 2975, 2, 0),
(121, '213', 80, 19.00, 'Guayaba', 1520, 1808.8, 2, 0),
(122, '215', 20, 19.00, 'Kiwi', 2500, 2500, 2, 0),
(123, '216', 80, 19.00, 'Lulo', 2500, 2975, 2, 0),
(124, '217', 80, 19.00, 'Mandarina', 2500, 2975, 2, 0),
(125, '232', 80, 19.00, 'Mango', 1525, 1814.75, 2, 0),
(126, '219', 50, 19.00, 'Maracuya', 5006, 5957.14, 2, 0),
(127, '220', 50, 19.00, 'Melon', 2500, 2975, 2, 0),
(128, '222', 80, 19.00, 'Mora', 2500, 2975, 2, 0),
(129, '201', 50, 19.00, 'Naranja', 2650, 3153.5, 2, 0),
(130, '224', 80, 19.00, 'Papaya', 3520, 4188.8, 2, 0),
(131, '225', 50, 19.00, 'Peras', 2650, 3153.5, 2, 0),
(132, '226', 80, 19.00, 'Pitahaya Amarilla', 2560, 3046.4, 2, 0),
(133, '227', 80, 19.00, 'Pitahaya Roja', 2500, 2975, 2, 0),
(134, '228', 80, 19.00, 'Tomate de arbol', 2400, 2856, 2, 0),
(135, '229', 80, 19.00, 'Uchuva', 2500, 2975, 2, 0),
(136, '230', 90, 19.00, 'Uva verde', 3650, 4343.5, 2, 0),
(137, '331', 80, 19.00, 'Zapote', 1250, 1487.5, 2, 0);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `fruvert`
--
ALTER TABLE `fruvert`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKkcvshooaq2ijxnjcdrqdw0vaf` (`categoria_id`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `fruvert`
--
ALTER TABLE `fruvert`
  ADD CONSTRAINT `FKkcvshooaq2ijxnjcdrqdw0vaf` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
