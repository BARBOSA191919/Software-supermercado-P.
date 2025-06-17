-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 19-12-2024 a las 23:18:49
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
-- Estructura de tabla para la tabla `carnes`
--

CREATE TABLE `carnes` (
  `id` bigint NOT NULL,
  `codigo` int NOT NULL,
  `existencia` float NOT NULL,
  `iva` decimal(19,2) NOT NULL,
  `nombre` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `precio` double NOT NULL,
  `precio_final` float NOT NULL,
  `categoria_id` bigint NOT NULL,
  `descuento` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `carnes`
--

INSERT INTO `carnes` (`id`, `codigo`, `existencia`, `iva`, `nombre`, `precio`, `precio_final`, `categoria_id`, `descuento`) VALUES
(69, 400, 50, 19.00, 'Alas', 5000, 5000, 4, 0),
(70, 401, 50, 19.00, 'Bagre', 25000, 25000, 4, 0),
(71, 402, 50, 19.00, 'Bife de chorizo', 5000, 5000, 4, 0),
(72, 403, 80, 19.00, 'Carne de cerdo', 3500, 3500, 4, 0),
(73, 404, 80, 19.00, 'Carne de pollo', 2500, 2500, 4, 0),
(74, 405, 80, 19.00, 'Carne molida', 3450, 3450, 4, 0),
(75, 406, 80, 19.00, 'Chuleta', 2500, 2500, 4, 0),
(76, 407, 80, 19.00, 'Costilla de cerdo', 2500, 2500, 4, 0),
(77, 407, 80, 19.00, 'Costilla de res', 4500, 4500, 4, 0),
(78, 408, 80, 19.00, 'Higado', 4500, 4500, 4, 0),
(79, 409, 80, 19.00, 'Lomo ancho y lomo fino', 5000, 5000, 4, 0),
(80, 410, 80, 19.00, 'Lomo de cerdo', 5000, 5000, 4, 0),
(81, 411, 80, 19.00, 'Menudencia', 2500, 2500, 4, 0),
(82, 412, 80, 19.00, 'Mojarra', 2500, 2500, 4, 0),
(83, 413, 50, 19.00, 'Muslos y piernas', 2500, 2500, 4, 0),
(84, 415, 50, 19.00, 'Pargo rojo', 5000, 5000, 4, 0),
(85, 414, 80, 19.00, 'Pechuga', 2500, 2500, 4, 0),
(86, 415, 80, 19.00, 'Pernil', 2500, 2500, 4, 0),
(87, 417, 50, 19.00, 'Pescado y mariscos', 2000, 2000, 4, 0),
(88, 418, 80, 19.00, 'Punta de anca', 1500, 1500, 4, 0),
(89, 419, 50, 19.00, 'Res', 2500, 2500, 4, 0),
(90, 420, 2500, 19.00, 'róbalo', 1500, 1785, 4, 0),
(91, 421, 50, 19.00, 'Sobrebarriga', 3500, 4165, 4, 0),
(92, 422, 80, 19.00, 'tilapia', 7500, 7500, 4, 0),
(93, 423, 80, 5.00, 'Tocino', 1450, 1522.5, 4, 0);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `carnes`
--
ALTER TABLE `carnes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKm2icyxmnfgdlnkex9cdephgnf` (`categoria_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `carnes`
--
ALTER TABLE `carnes`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=94;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
