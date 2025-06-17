-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 19-12-2024 a las 23:19:03
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
-- Estructura de tabla para la tabla `verdura`
--

CREATE TABLE `verdura` (
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
-- Volcado de datos para la tabla `verdura`
--

INSERT INTO `verdura` (`id`, `codigo`, `existencia`, `iva`, `nombre`, `precio`, `precio_final`, `categoria_id`, `descuento`) VALUES
(5, '300', 49, 5.00, 'Cebolla cabezona', 850, 892.5, 1, 0),
(94, '301', 80, 5.00, 'Ajo', 2500, 2500, 1, 0),
(95, '302', 80, 19.00, 'Apio', 2500, 2975, 1, 0),
(96, '303', 50, 19.00, 'Brocoli', 8000, 9520, 1, 0),
(97, '304', 80, 5.00, 'Cebolla larga', 5000, 5000, 1, 0),
(98, '305', 50, 19.00, 'Cilantro', 5000, 5000, 1, 0),
(99, '306', 80, 5.00, 'Cilantro cimarron', 2500, 2625, 1, 0),
(100, '307', 80, 19.00, 'Esparragos', 2500, 2500, 1, 0),
(101, '309', 80, 19.00, 'Espinaca', 2500, 2500, 1, 0),
(102, '310', 20, 5.00, 'Lechuga', 2500, 2500, 1, 0),
(103, '311', 80, 5.00, 'Mazorca', 2500, 2625, 1, 0),
(104, '313', 50, 5.00, 'Papa criolla', 450, 450, 1, 0),
(105, '314', 50, 5.00, 'Papa', 4500, 4500, 1, 0),
(106, '315', 50, 5.00, 'Pepino', 4560, 4560, 1, 0),
(107, '317', 80, 5.00, 'Pimenton', 1581, 1660.05, 1, 0),
(108, '318', 80, 5.00, 'Platano amarillo', 2000, 2100, 1, 0),
(109, '320', 80, 5.00, 'Platano verde', 1850, 1942.5, 1, 0),
(110, '321', 80, 5.00, 'Remolacha', 2500, 2625, 1, 0),
(111, '322', 80, 5.00, 'Tomate', 2500, 2500, 1, 0),
(112, '323', 50, 5.00, 'Yuca', 3200, 3360, 1, 0),
(113, '325', 80, 19.00, 'Zanahoria', 3250, 3867.5, 1, 0);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `verdura`
--
ALTER TABLE `verdura`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKiupaaqjw1xmrmu79mbx3prir9` (`categoria_id`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `verdura`
--
ALTER TABLE `verdura`
  ADD CONSTRAINT `FKiupaaqjw1xmrmu79mbx3prir9` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
