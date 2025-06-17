-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 12-02-2025 a las 16:40:18
-- Versión del servidor: 8.0.30
-- Versión de PHP: 8.1.10

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
(69, 400, 27, 5.00, 'Alas', 5000, 5000, 4, 0),
(70, 401, 100, 5.00, 'Bagre', 25000, 25000, 4, 0),
(71, 402, 43, 19.00, 'Bife de chorizo', 5000, 5000, 4, 0),
(72, 403, 80, 19.00, 'Carne de cerdo', 3500, 3500, 4, 0),
(73, 404, 80, 19.00, 'Carne de pollo', 2500, 2500, 4, 0),
(74, 405, 80, 19.00, 'Carne molida', 3450, 3450, 4, 0),
(75, 406, 80, 19.00, 'Chuleta', 2500, 2500, 4, 0),
(76, 407, 80, 19.00, 'Costilla de cerdo', 2500, 2500, 4, 0),
(77, 4020, 98, 5.00, 'Costilla de res', 4500, 4500, 4, 0),
(78, 408, 80, 19.00, 'Higado', 4500, 4500, 4, 0),
(79, 409, 59, 19.00, 'Lomo ancho y lomo fino', 5000, 5000, 4, 0),
(80, 410, 80, 19.00, 'Lomo de cerdo', 5000, 5000, 4, 0),
(81, 411, 74, 19.00, 'Menudencia', 2500, 2500, 4, 0),
(82, 412, 80, 19.00, 'Mojarra', 2500, 2500, 4, 0),
(83, 413, 50, 19.00, 'Muslos y piernas', 2500, 2500, 4, 0),
(84, 415, 80, 19.00, 'Pargo rojo', 5000, 5000, 4, 0),
(85, 414, 80, 19.00, 'Pechuga', 2500, 2500, 4, 0),
(86, 415, 80, 19.00, 'Pernil', 2500, 2500, 4, 0),
(87, 417, 50, 19.00, 'Pescado y mariscos', 2000, 2000, 4, 0),
(88, 418, 80, 19.00, 'Punta de anca', 1500, 1500, 4, 0),
(89, 419, 50, 19.00, 'Res', 2500, 2500, 4, 0),
(90, 420, 2493, 19.00, 'róbalo', 1500, 1785, 4, 0),
(91, 421, 99, 19.00, 'Sobrebarriga', 3500, 4165, 4, 0),
(92, 422, 100, 19.00, 'tilapia', 7500, 7500, 4, 0),
(93, 423, 80, 5.00, 'Tocino', 1450, 1522.5, 4, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias`
--

CREATE TABLE `categorias` (
  `id` bigint NOT NULL,
  `codigo` int NOT NULL,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `categorias`
--

INSERT INTO `categorias` (`id`, `codigo`, `nombre`) VALUES
(1, 1, 'verduras'),
(2, 2, 'frutas'),
(3, 3, 'productos'),
(4, 4, 'carnes'),
(7, 9921, 'Arroz Diana'),
(8, 1212, 'arrroz flor huila'),
(9, 12, 'Azucar');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id` bigint NOT NULL,
  `apellido` varchar(255) DEFAULT NULL,
  `cedula` int DEFAULT NULL,
  `nombre_cliente` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id`, `apellido`, `cedula`, `nombre_cliente`) VALUES
(1, 'final', 222222, 'Consumidor '),
(3, 'barbosa ', 1003865544, 'santiago '),
(6, 'pinto', 1092019201, 'Julian '),
(7, 'rivas', 222222, 'Consumidor final'),
(11, 'rivas', 223321, 'felipe '),
(14, 'pinto', 222222222, 'Julian '),
(17, 'sanabria', 393922, 'nicolas'),
(18, 'martinez', 28873282, 'diego '),
(19, 'pastrana', 32232323, 'erika'),
(20, 'sss', 23233223, 'nicolas'),
(21, 'ssasa', 232232, 'Julian ');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `compras`
--

CREATE TABLE `compras` (
  `id` bigint NOT NULL,
  `precio` double NOT NULL,
  `cantidad` int NOT NULL,
  `codigo` varchar(255) NOT NULL,
  `descuento` float NOT NULL,
  `fecha` date NOT NULL,
  `iva` float NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `total` double NOT NULL,
  `proveedor_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `compras`
--

INSERT INTO `compras` (`id`, `precio`, `cantidad`, `codigo`, `descuento`, `fecha`, `iva`, `nombre`, `total`, `proveedor_id`) VALUES
(1, 4500, 500, '001', 20, '2024-12-07', 10, ' Frijól Bola Roja', 2025000, 2),
(2, 2000, 1000, '0002', 25, '2024-12-07', 10, 'Arroz roa 500 gm', 1700000, 1),
(3, 18000, 3000, '0003', 20, '2024-12-07', 10, 'Leche alqueria 500 ml', 48600000, 3),
(4, 5000, 100, '212', 20, '2024-12-08', 10, 'Pam bimbo 500', 450000, 4),
(5, 45000, 20, '0550', 10, '2024-12-18', 19, 'Arbejas 500 g', 981000, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `contabilidad`
--

CREATE TABLE `contabilidad` (
  `id` bigint NOT NULL,
  `fecha` date DEFAULT NULL,
  `ganancias` double DEFAULT NULL,
  `total_egresos` double NOT NULL,
  `total_ingresos` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `devoluciones`
--

CREATE TABLE `devoluciones` (
  `id` bigint NOT NULL,
  `cantidad` int DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `motivo` varchar(255) DEFAULT NULL,
  `precio` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `devoluciones`
--

INSERT INTO `devoluciones` (`id`, `cantidad`, `fecha`, `motivo`, `precio`) VALUES
(1, 212, '2024-12-08', 'producto_defectuoso', 2123),
(3, 12312, '2024-12-09', 'cambio', 213123123),
(4, 9999, '2024-12-09', 'producto_defectuoso', 99999),
(5, 2312, '2024-12-09', 'producto_defectuoso', 999999),
(6, 23, '2024-12-12', 'cambio', 32000),
(10, 23, '2025-01-28', 'otro', 300000),
(11, 332, '2025-01-28', 'producto_defectuoso', 32322323),
(12, 343, '2025-01-28', 'otro', 3333333);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado`
--

CREATE TABLE `empleado` (
  `id` bigint NOT NULL,
  `apellido` varchar(255) DEFAULT NULL,
  `cedula` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `empleado`
--

INSERT INTO `empleado` (`id`, `apellido`, `cedula`, `nombre`, `telefono`) VALUES
(1, 'Oviedo', '1003865544', 'Jesus', '3217696864'),
(2, 'lopez', '321092192', 'alfonso ', '323121'),
(3, 'vargas', '1221', 'andres', '23222'),
(4, 'admin', '12921', 'admin', '2121');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `facturas`
--

CREATE TABLE `facturas` (
  `id` bigint NOT NULL,
  `fecha` date DEFAULT NULL,
  `numero_factura` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `xml_document` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `facturas_pos`
--

CREATE TABLE `facturas_pos` (
  `id` bigint NOT NULL,
  `fecha` date DEFAULT NULL,
  `monto` decimal(19,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
(3, '200', 0, 5.00, 'Aguacate', 3850, 3850, 2, 0),
(4, '201', 99, 5.00, 'Bananos', 1300, 1300, 2, 0),
(8, '202', 40, 5.00, 'Sandia', 2000, 2000, 2, 0),
(9, '203', 23, 0.00, 'Coco', 3000, 3000, 2, 0),
(10, '204', 100, 19.00, 'Manzana roja', 3200, 3200, 2, 0),
(11, '205', 20, 19.00, 'Manzana verde', 1500, 1500, 2, 0),
(114, '206', 80, 19.00, 'Arandanos', 2560, 3046.4, 2, 0),
(115, '207', 80, 19.00, 'Berenjena', 2500, 2975, 2, 0),
(116, '207', 80, 19.00, 'Carambolo', 2400, 2856, 2, 0),
(117, '208', 77, 19.00, 'Ciruela', 500, 595, 2, 0),
(118, '210', 50, 19.00, 'Durazno', 650, 773.5, 2, 0),
(119, '211', 6, 19.00, 'Fresa', 5600, 6664, 2, 0),
(120, '212', 78, 19.00, 'Guanabana', 2500, 2975, 2, 0),
(121, '213', 80, 19.00, 'Guayaba', 1520, 1808.8, 2, 0),
(122, '215', 19, 19.00, 'Kiwi', 2500, 2500, 2, 0),
(123, '216', 80, 19.00, 'Lulo', 2500, 2975, 2, 0),
(124, '217', 80, 19.00, 'Mandarina', 2500, 2975, 2, 0),
(125, '232', 75, 19.00, 'Mango', 1525, 1814.75, 2, 0),
(126, '219', 50, 19.00, 'Maracuya', 5006, 5957.14, 2, 0),
(127, '220', 47, 19.00, 'Melon', 2500, 2975, 2, 0),
(128, '222', 80, 19.00, 'Mora', 2500, 2975, 2, 0),
(129, '223', 99, 5.00, 'Naranja', 2650, 2650, 2, 0),
(130, '224', 79, 19.00, 'Papaya', 3520, 4188.8, 2, 0),
(131, '225', 49, 19.00, 'Peras', 2650, 3153.5, 2, 0),
(132, '226', 76, 19.00, 'Pitahaya Amarilla', 2560, 3046.4, 2, 0),
(133, '227', 80, 19.00, 'Pitahaya Roja', 2500, 2975, 2, 0),
(134, '228', 80, 19.00, 'Tomate de arbol', 2400, 2856, 2, 0),
(135, '229', 80, 19.00, 'Uchuva', 2500, 2975, 2, 0),
(136, '230', 89, 19.00, 'Uva verde', 3650, 4343.5, 2, 0),
(137, '331', 80, 19.00, 'Zapote', 1250, 1487.5, 2, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(733);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inventarios`
--

CREATE TABLE `inventarios` (
  `id` bigint NOT NULL,
  `cantidad` int NOT NULL,
  `codigo` varchar(255) NOT NULL,
  `comentario` varchar(255) DEFAULT NULL,
  `fecha` date NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `precio` float NOT NULL,
  `stock` float NOT NULL,
  `tipo_movimiento` varchar(255) NOT NULL,
  `categoria_id` bigint DEFAULT NULL,
  `proveedor_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pagos_datafono`
--

CREATE TABLE `pagos_datafono` (
  `id` bigint NOT NULL,
  `fecha` date NOT NULL,
  `monto` decimal(19,2) NOT NULL,
  `numero_transaccion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `factura_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

CREATE TABLE `producto` (
  `id` int NOT NULL,
  `codigo` varchar(50) NOT NULL,
  `existencia` float NOT NULL,
  `iva` decimal(19,2) DEFAULT NULL,
  `nombre` varchar(50) NOT NULL,
  `precio` float NOT NULL,
  `precio_final` float DEFAULT NULL,
  `categoria_id` bigint NOT NULL,
  `descuento` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `producto`
--

INSERT INTO `producto` (`id`, `codigo`, `existencia`, `iva`, `nombre`, `precio`, `precio_final`, `categoria_id`, `descuento`) VALUES
(218, '00012', 94, 19.00, 'Arroz Diana 500g', 2000, 2142, 3, 10),
(219, '00023', 2992, 19.00, 'Leche Alqueria 500 ml', 1850, 2091.42, 3, 5),
(220, '0003', 63, 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 3, 2),
(237, '232606', 788, 19.00, 'Arroz diana 1kg', 2200, 2618, 3, 2),
(238, '1656', 100, 19.00, '1 Arroba de Arroz Diana ', 45000, 48195, 3, 10),
(286, '182912891', 34, 19.00, '1 Arroba de Arroz Diana', 800000, 761600, 3, 20),
(318, '7702231000011', 34, 19.00, 'arroz diana 500 gr', 3200, 1904, 7, 50),
(702, '7702059405029', 46, 19.00, 'Azucar Incauca', 65000, 65000, 9, 0),
(732, '7709990158779', 50, 19.00, 'ARROZ FLOR HUILA ', 3500, 3500, 7, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto_vendido`
--

CREATE TABLE `producto_vendido` (
  `id` int NOT NULL,
  `cantidad` float DEFAULT NULL,
  `codigo` varchar(255) DEFAULT NULL,
  `iva` decimal(19,2) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `precio` float DEFAULT NULL,
  `precio_final` float DEFAULT NULL,
  `venta_id` int DEFAULT NULL,
  `descuento` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `producto_vendido`
--

INSERT INTO `producto_vendido` (`id`, `cantidad`, `codigo`, `iva`, `nombre`, `precio`, `precio_final`, `venta_id`, `descuento`) VALUES
(507, 1, '211', 19.00, 'Fresa', 5600, 6664, 506, 0),
(509, 2, '211', 19.00, 'Fresa', 5600, 6664, 508, 0),
(512, 1, '301', 5.00, 'Ajo', 2500, 2625, 510, 0),
(514, 1, '211', 19.00, 'Fresa', 5600, 6664, 513, 0),
(516, 1, '211', 19.00, 'Fresa', 5600, 6664, 515, 0),
(518, 1, '211', 19.00, 'Fresa', 5600, 6664, 517, 0),
(519, 1, '0003', 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 517, 2),
(520, 1, '232606', 19.00, 'Arroz diana 1kg', 2200, 2618, 517, 2),
(522, 1, '212', 19.00, 'Guanabana', 2500, 2975, 521, 0),
(524, 1, '0003', 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 523, 2),
(526, 1, '232606', 19.00, 'Arroz diana 1kg', 2200, 2618, 525, 2),
(528, 1, '0003', 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 527, 2),
(529, 1, '128912819', 19.00, 'arroz diana 500m gm', 3200, 3200, 527, 0),
(531, 1, '0003', 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 530, 2),
(533, 1, '0003', 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 532, 2),
(535, 1, '222', 19.00, 'Mora', 2500, 2975, 534, 0),
(536, 1, '211', 19.00, 'Fresa', 5600, 6664, 534, 0),
(537, 1, '232606', 19.00, 'Arroz diana 1kg', 2200, 2618, 534, 2),
(539, 1, '211', 19.00, 'Fresa', 5600, 6664, 538, 0),
(541, 2, '211', 19.00, 'Fresa', 5600, 6664, 540, 0),
(543, 1, '211', 19.00, 'Fresa', 5600, 6664, 542, 0),
(545, 1, '0003', 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 544, 2),
(547, 1, '211', 19.00, 'Fresa', 5600, 6664, 546, 0),
(549, 1, '411', 19.00, 'Menudencia', 2500, 2500, 548, 0),
(550, 1, '220', 19.00, 'Melon', 2500, 2975, 548, 0),
(552, 1, '232', 19.00, 'Mango', 1525, 1814.75, 551, 0),
(554, 1, '0003', 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 553, 2),
(556, 1, '211', 19.00, 'Fresa', 5600, 6664, 555, 0),
(558, 1, '400', 5.00, 'Alas', 5000, 5000, 557, 0),
(560, 1, '315', 5.00, 'Pepino', 4560, 4560, 559, 0),
(562, 1, '315', 5.00, 'Pepino', 4560, 4560, 561, 0),
(563, 1, '402', 19.00, 'Bife de chorizo', 5000, 5000, 561, 0),
(564, 1, '309', 19.00, 'Espinaca', 2500, 2500, 561, 0),
(566, 1, '212', 19.00, 'Guanabana', 2500, 2975, 565, 0),
(568, 1, '402', 19.00, 'Bife de chorizo', 5000, 5000, 567, 0),
(569, 1, '222', 19.00, 'Mora', 2500, 2975, 567, 0),
(570, 1, '215', 19.00, 'Kiwi', 2500, 2500, 567, 0),
(572, 1, '421', 19.00, 'Sobrebarriga', 3500, 4165, 571, 0),
(573, 1, '301', 5.00, 'Ajo', 2500, 2625, 571, 0),
(574, 1, '232', 19.00, 'Mango', 1525, 1814.75, 571, 0),
(575, 1, '0003', 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 571, 2),
(577, 1, '301', 5.00, 'Ajo', 2500, 2625, 576, 0),
(578, 1, '317', 5.00, 'Pimenton', 1581, 1660.05, 576, 0),
(580, 1, '211', 19.00, 'Fresa', 5600, 6664, 579, 0),
(582, 1, '317', 5.00, 'Pimenton', 1581, 1660.05, 581, 0),
(585, 1, '421', 19.00, 'Sobrebarriga', 3500, 4165, 583, 0),
(586, 1, '225', 19.00, 'Peras', 2650, 3153.5, 583, 0),
(589, 1, '226', 19.00, 'Pitahaya Amarilla', 2560, 3046.4, 587, 0),
(590, 1, '203', 0.00, 'Coco', 3000, 3000, 587, 0),
(592, 1, '211', 19.00, 'Fresa', 5600, 6664, 591, 0),
(595, 1, '211', 19.00, 'Fresa', 5600, 6664, 593, 0),
(598, 3, '208', 19.00, 'Ciruela', 500, 595, 596, 0),
(599, 4, '411', 19.00, 'Menudencia', 2500, 2500, 596, 0),
(600, 3, '226', 19.00, 'Pitahaya Amarilla', 2560, 3046.4, 596, 0),
(601, 1, '232', 19.00, 'Mango', 1525, 1814.75, 596, 0),
(602, 2, '301', 5.00, 'Ajo', 2500, 2625, 596, 0),
(605, 1, '211', 19.00, 'Fresa', 5600, 6664, 603, 0),
(606, 1, '128912819', 19.00, 'arroz diana 500m gm', 3200, 3427.2, 603, 10),
(609, 1, '421', 19.00, 'Sobrebarriga', 3500, 4165, 607, 0),
(612, 1, '211', 19.00, 'Fresa', 5600, 6664, 610, 0),
(613, 1, '0003', 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 610, 2),
(614, 1, '182912891', 19.00, '1 Arroba de Arroz Diana', 800000, 761600, 610, 20),
(617, 1, '402', 19.00, 'Bife de chorizo', 5000, 5000, 615, 0),
(618, 1, '0003', 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 615, 2),
(619, 1, '182912891', 19.00, '1 Arroba de Arroz Diana', 800000, 761600, 615, 20),
(622, 1, '0003', 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 620, 2),
(623, 1, '232606', 19.00, 'Arroz diana 1kg', 2200, 2618, 620, 2),
(624, 1, '211', 19.00, 'Fresa', 5600, 6664, 620, 0),
(625, 1, '421', 19.00, 'Sobrebarriga', 3500, 4165, 620, 0),
(628, 1, '211', 19.00, 'Fresa', 5600, 6664, 626, 0),
(631, 3, '182912891', 19.00, '1 Arroba de Arroz Diana', 800000, 761600, 629, 20),
(632, 3, '211', 19.00, 'Fresa', 5600, 6664, 629, 0),
(635, 1, '128912819', 19.00, 'arroz diana 500 gr', 3200, 1904, 633, 50),
(636, 1, '421', 19.00, 'Sobrebarriga', 3500, 4165, 633, 0),
(637, 1, '400', 5.00, 'Alas', 5000, 5000, 633, 0),
(640, 4, '0003', 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 638, 2),
(641, 1, '420', 19.00, 'róbalo', 1500, 1785, 638, 0),
(642, 1, '310', 5.00, 'Lechuga', 2500, 2500, 638, 0),
(645, 1, '211', 19.00, 'Fresa', 5600, 6664, 643, 0),
(646, 1, '0003', 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 643, 2),
(647, 1, '232', 19.00, 'Mango', 1525, 1814.75, 643, 0),
(651, 1, '211', 19.00, 'Fresa', 5600, 6664, 649, 0),
(652, 1, '128912819', 19.00, 'arroz diana 500 gr', 3200, 1904, 649, 50),
(655, 1, '211', 19.00, 'Fresa', 5600, 6664, 653, 0),
(658, 5, '222', 19.00, 'Mora', 2500, 2975, 656, 0),
(665, 2, '211', 19.00, 'Fresa', 5600, 6664, 663, 0),
(670, 2, '211', 19.00, 'Fresa', 5600, 6664, 668, 0),
(683, 1, '211', 19.00, 'Fresa', 5600, 6664, 681, 0),
(686, 1, '211', 19.00, 'Fresa', 5600, 6664, 684, 0),
(687, 1, '421', 19.00, 'Sobrebarriga', 3500, 4165, 684, 0),
(690, 1, '232606', 19.00, 'Arroz diana 1kg', 2200, 2618, 688, 2),
(691, 5, '310', 5.00, 'Lechuga', 2500, 2500, 688, 0),
(692, 10, '409', 19.00, 'Lomo ancho y lomo fino', 5000, 5000, 688, 0),
(699, 5, '310', 5.00, 'Lechuga', 2500, 2500, 697, 0),
(700, 1, '232606', 19.00, 'Arroz diana 1kg', 2200, 2618, 697, 2),
(701, 11, '409', 19.00, 'Lomo ancho y lomo fino', 5000, 5000, 697, 0),
(705, 6, '7702059405029', 19.00, 'Azucar Incauca', 65000, 65000, 703, 0),
(706, 6, '7702231000011', 19.00, 'arroz diana 500 gr', 3200, 1904, 703, 50),
(709, 6, '7702231000011', 19.00, 'arroz diana 500 gr', 3200, 1904, 707, 50),
(710, 8, '7702059405029', 19.00, 'Azucar Incauca', 65000, 65000, 707, 0),
(713, 1, '4020', 5.00, 'Costilla de res', 4500, 4500, 711, 0),
(714, 1, '211', 19.00, 'Fresa', 5600, 6664, 711, 0),
(717, 1, '211', 19.00, 'Fresa', 5600, 6664, 715, 0),
(718, 1, '4020', 5.00, 'Costilla de res', 4500, 4500, 715, 0),
(719, 1, '203', 0.00, 'Coco', 3000, 3000, 715, 0),
(720, 1, '182912891', 19.00, '1 Arroba de Arroz Diana', 800000, 761600, 715, 20),
(721, 1, '201', 5.00, 'Bananos', 1300, 1300, 715, 0),
(722, 1, '421', 19.00, 'Sobrebarriga', 3500, 4165, 715, 0),
(723, 1, '223', 5.00, 'Naranja', 2650, 2650, 715, 0),
(724, 1, '224', 19.00, 'Papaya', 3520, 4188.8, 715, 0),
(725, 1, '230', 19.00, 'Uva verde', 3650, 4343.5, 715, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proveedor`
--

CREATE TABLE `proveedor` (
  `id` bigint NOT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nombre_proveedor` varchar(255) NOT NULL,
  `telefono` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `proveedor`
--

INSERT INTO `proveedor` (`id`, `direccion`, `email`, `nombre_proveedor`, `telefono`) VALUES
(1, 'calle 38 # 7 A 51 las granjas', 'arroz23@gmail.com', 'Arroz roa S.A.S', '3217696864'),
(2, 'calle 48 # 7 A 51 las granjas', 'frijol12sas@gmail.com', 'Frijol boli roja S.O.S', '3217696864'),
(3, 'calle343# 7 A 51 las centro', 'alqueria123@gmail.com', 'Alqueria  S.O.S', '3106090707'),
(4, 'calle 38 # 7 A 51 las granjas', 'carmonabernaldiego@gmail.com', 'Pan Bimbo ', '232323232'),
(5, 'calle 21 # 7 A 51 comuneros', 'Todatini123@gmail.com', 'Today TINO', '3456899787767'),
(6, 'dasdsad', 'sbarbosarivas@gmail.commm', 'asdasdsasdas', '12321321');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reportes`
--

CREATE TABLE `reportes` (
  `id` bigint NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fecha_generacion` datetime(6) DEFAULT NULL,
  `ruta_archivo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `tipo_reporte` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reporte_venta`
--

CREATE TABLE `reporte_venta` (
  `id` int NOT NULL,
  `cantidad_productos_vendidos` int DEFAULT NULL,
  `fecha_reporte` date DEFAULT NULL,
  `subtotal_ventas` float DEFAULT NULL,
  `total_ventas` float DEFAULT NULL,
  `turno_id` bigint DEFAULT NULL,
  `venta_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `reporte_venta`
--

INSERT INTO `reporte_venta` (`id`, `cantidad_productos_vendidos`, `fecha_reporte`, `subtotal_ventas`, `total_ventas`, `turno_id`, `venta_id`) VALUES
(511, 0, '2025-01-13', NULL, 0, 1, 510),
(584, 0, '2025-01-14', NULL, 0, 1, 583),
(588, 0, '2025-01-14', NULL, 0, 1, 587),
(594, 0, '2025-01-15', NULL, 0, 1, 593),
(597, 0, '2025-01-15', NULL, 0, 1, 596),
(604, 0, '2025-01-17', NULL, 0, 1, 603),
(608, 0, '2025-01-17', NULL, 0, 1, 607),
(611, 0, '2025-01-18', NULL, 0, 1, 610),
(616, 0, '2025-01-18', NULL, 0, 1, 615),
(621, 0, '2025-01-18', NULL, 0, 1, 620),
(627, 0, '2025-01-20', NULL, 0, 1, 626),
(630, 0, '2025-01-20', NULL, 0, 1, 629),
(634, 0, '2025-01-22', NULL, 0, 1, 633),
(639, 0, '2025-01-22', NULL, 0, 1, 638),
(644, 0, '2025-01-22', NULL, 0, 1, 643),
(650, 0, '2025-01-23', NULL, 0, 2, 649),
(654, 0, '2025-01-23', NULL, 0, 2, 653),
(657, 0, '2025-01-24', NULL, 0, 2, 656),
(659, 0, '2025-01-24', 0, 0, 5, NULL),
(660, 0, '2025-01-24', 0, 0, 5, NULL),
(661, 0, '2025-01-24', 0, 0, 5, NULL),
(662, 0, '2025-01-24', 0, 0, 5, NULL),
(664, 0, '2025-01-24', NULL, 0, 2, 663),
(666, 0, '2025-01-24', 0, 0, 5, NULL),
(667, 0, '2025-01-24', 0, 0, 5, NULL),
(669, 0, '2025-01-24', NULL, 0, 3, 668),
(671, 0, '2025-01-24', 0, 0, 5, NULL),
(672, 0, '2025-01-24', 0, 0, 5, NULL),
(673, 0, '2025-01-24', 0, 0, 5, NULL),
(674, 0, '2025-01-24', 0, 0, 5, NULL),
(675, 0, '2025-01-24', 0, 0, 5, NULL),
(676, 0, '2025-01-24', 0, 0, 5, NULL),
(677, 0, '2025-01-24', 0, 0, 5, NULL),
(678, 0, '2025-01-24', 0, 0, 5, NULL),
(679, 0, '2025-01-24', 0, 0, 5, NULL),
(680, 0, '2025-01-24', 0, 0, 5, NULL),
(682, 0, '2025-01-27', NULL, 0, 5, 681),
(685, 0, '2025-01-28', NULL, 0, 5, 684),
(689, 0, '2025-01-28', NULL, 0, 5, 688),
(698, 0, '2025-01-29', NULL, 0, 5, 697),
(704, 0, '2025-02-01', NULL, 0, 5, 703),
(708, 0, '2025-02-01', NULL, 0, 5, 707),
(712, 0, '2025-02-08', NULL, 0, 5, 711),
(716, 0, '2025-02-08', NULL, 0, 5, 715);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id` bigint NOT NULL,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `nombre`) VALUES
(3, 'ADMIN'),
(1, 'CAJERO'),
(6, 'CONTADOR'),
(5, 'GERENTE');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `turnos`
--

CREATE TABLE `turnos` (
  `id` bigint NOT NULL,
  `base_dinero` double DEFAULT NULL,
  `caja` varchar(255) DEFAULT NULL,
  `fecha` datetime(6) DEFAULT NULL,
  `hora_inicio` varchar(255) DEFAULT NULL,
  `hora_salida` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `usuario_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `turnos`
--

INSERT INTO `turnos` (`id`, `base_dinero`, `caja`, `fecha`, `hora_inicio`, `hora_salida`, `nombre`, `usuario_id`) VALUES
(1, 3000000, '1', '2025-01-23 00:00:00.000000', '07:00', '19:00', 'Jesus Oviedo', 22),
(2, 6000000, '2', '2025-01-23 00:00:00.000000', '18:33', '19:33', 'alfonso lopes', 23),
(3, 2222222, '3', '2025-01-28 00:00:00.000000', '10:49', '10:49', 'andres1221', 24),
(5, 2222, '0', '2025-01-24 00:00:00.000000', '11:22', '11:22', 'admin12', 6);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` bigint NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `password_display` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `password`, `role`, `username`, `enabled`, `password_display`) VALUES
(6, '$2a$10$SMJWe3wprRh41GA09kbTG.k92jXOCfzgHeqxolM6EuMm7fFG.mEvO', NULL, 'admin', b'1', 'admin'),
(22, '$2a$10$F8Ilndm/naxT/w2HnHGzAOlEIdzb28d1.OPpSLE/7OVvNicc7JOiy', NULL, 'Jesus5544', b'1', '5544'),
(23, '$2a$10$YMw9Vu1AycsoSE7EsuYEau4GlJH8nhomWolA3xWVeLc1V0DU7m6..', NULL, 'alfonso1003', b'1', '123'),
(24, '$2a$10$fqae2DwqzUhE7jPckDPj/uFEdl/Rn5Acy2Xg980wu1TruFY98mFeC', NULL, 'andres1221', b'1', '123'),
(25, '$2a$10$clhJRMZYsWn4HRF8JtsLVur0ocjoQPH0/fRjqaBzSOi/Wvh15dj.e', NULL, 'Jesus  1003865544', b'1', '$2a$10$p1mKmoXzR.jlCjcPz4N2IuKAk55pPY2ukuU95Pp8hBFtVBmgPzftm');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_roles`
--

CREATE TABLE `usuario_roles` (
  `usuario_id` bigint NOT NULL,
  `rol_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario_roles`
--

INSERT INTO `usuario_roles` (`usuario_id`, `rol_id`) VALUES
(22, 1),
(23, 1),
(24, 1),
(25, 1),
(6, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `venta`
--

CREATE TABLE `venta` (
  `id` int NOT NULL,
  `cambio` float DEFAULT NULL,
  `efectivo` float DEFAULT NULL,
  `fechayhora` varchar(255) DEFAULT NULL,
  `cliente_id` bigint DEFAULT NULL,
  `turno_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `venta`
--

INSERT INTO `venta` (`id`, `cambio`, `efectivo`, `fechayhora`, `cliente_id`, `turno_id`) VALUES
(506, 336, 7000, '2025-01-13 09:07:25', 1, 1),
(508, 1672, 15000, '2025-01-13 09:10:54', 1, 1),
(510, 375, 3000, '2025-01-13 09:13:44', 1, 1),
(513, 13336, 20000, '2025-01-13 16:47:11', 1, 1),
(515, 1336, 8000, '2025-01-13 16:59:29', 1, 1),
(517, 363, 15000, '2025-01-13 17:06:27', 1, 1),
(521, 2025, 5000, '2025-01-13 20:51:11', 1, 1),
(523, 3645, 9000, '2025-01-13 20:52:31', 1, 1),
(525, 382, 3000, '2025-01-13 20:55:02', 1, 1),
(527, 1445, 10000, '2025-01-13 20:56:37', 1, 1),
(530, 645, 6000, '2025-01-13 20:59:09', 1, 1),
(532, 645, 6000, '2025-01-13 21:06:11', 1, 1),
(534, 2743, 15000, '2025-01-13 21:07:16', 1, 1),
(538, 3336, 10000, '2025-01-13 21:11:16', 1, 1),
(540, 1672, 15000, '2025-01-13 21:11:42', 1, 1),
(542, 336, 7000, '2025-01-13 21:12:55', 1, 1),
(544, 645, 6000, '2025-01-13 21:14:06', 1, 1),
(546, 3336, 10000, '2025-01-13 21:15:10', 1, 1),
(548, 4525, 10000, '2025-01-13 21:16:07', 1, 1),
(551, 1185.25, 3000, '2025-01-13 21:17:45', 1, 1),
(553, 645, 6000, '2025-01-13 21:18:55', 1, 1),
(555, 13336, 20000, '2025-01-13 21:20:13', 1, 1),
(557, 5000, 10000, '2025-01-13 21:22:45', 1, 1),
(559, 1440, 6000, '2025-01-13 21:23:01', 1, 1),
(561, 2940, 15000, '2025-01-13 21:24:32', 1, 1),
(565, 27025, 30000, '2025-01-13 21:25:45', 1, 1),
(567, 1525, 12000, '2025-01-13 21:26:28', 1, 1),
(571, 1040.25, 15000, '2025-01-14 07:37:25', 1, 1),
(576, 714.95, 5000, '2025-01-14 07:37:51', 1, 1),
(579, 336, 7000, '2025-01-14 07:44:27', 1, 1),
(581, 339.95, 2000, '2025-01-14 07:45:13', 1, 1),
(583, 681.5, 8000, '2025-01-14 08:25:53', 1, 1),
(587, 953.6, 7000, '2025-01-14 09:13:28', 1, 1),
(591, 336, 7000, '2025-01-15 08:41:30', 1, 1),
(593, 1336, 8000, '2025-01-15 08:48:32', 1, 1),
(596, 2011.05, 30000, '2025-01-15 09:11:37', 1, 1),
(603, 1908.8, 12000, '2025-01-17 09:09:42', 1, 1),
(607, 335, 4500, '2025-01-17 11:48:45', 1, 1),
(610, 6381, 780000, '2025-01-18 11:46:34', 1, 1),
(615, 28045, 800000, '2025-01-18 11:47:29', 1, 1),
(620, 1198, 20000, '2025-01-18 12:30:38', 1, 1),
(626, 336, 7000, '2025-01-20 08:32:58', 1, 1),
(629, 3695210, 6000000, '2025-01-20 11:10:54', 1, 1),
(633, 8931, 20000, '2025-01-22 11:11:51', 1, 1),
(638, 4295, 30000, '2025-01-22 11:20:00', 1, 1),
(643, 6166.25, 20000, '2025-01-22 11:29:30', 1, 1),
(649, 6432, 15000, '2025-01-23 18:43:31', 1, 2),
(653, 3336, 10000, '2025-01-23 19:02:24', 1, 2),
(656, 125, 15000, '2025-01-24 11:19:46', 1, 2),
(663, 1672, 15000, '2025-01-24 11:25:52', 1, 2),
(668, 1672, 15000, '2025-01-24 11:34:01', 1, 3),
(681, 336, 7000, '2025-01-27 21:03:10', 1, 5),
(684, 9171, 20000, '2025-01-28 09:56:00', 1, 5),
(688, 4882, 70000, '2025-01-28 15:15:00', 1, 5),
(697, 9882, 80000, '2025-01-29 09:29:03', 1, 5),
(703, 8576, 410000, '2025-02-01 15:16:41', 1, 5),
(707, 8576, 540000, '2025-02-01 15:21:17', 1, 5),
(711, 836, 12000, '2025-02-08 12:30:27', 1, 5),
(715, 7588.69, 800000, '2025-02-08 15:37:39', 1, 5);

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
(5, '300', 200, 5.00, 'Cebolla cabezona', 850, 850, 1, 0),
(94, '301', 10, 5.00, 'Ajo', 2500, 2625, 1, 0),
(95, '302', 78, 19.00, 'Apio', 2500, 2975, 1, 0),
(96, '303', 50, 19.00, 'Brocoli', 8000, 9520, 1, 0),
(97, '304', 80, 5.00, 'Cebolla larga', 5000, 5000, 1, 0),
(98, '305', 50, 19.00, 'Cilantro', 5000, 5000, 1, 0),
(99, '306', 80, 5.00, 'Cilantro cimarron', 2500, 2625, 1, 0),
(100, '307', 80, 19.00, 'Esparragos', 2500, 2500, 1, 0),
(101, '309', 79, 19.00, 'Espinaca', 2500, 2500, 1, 0),
(102, '310', 95, 5.00, 'Lechuga', 2500, 2500, 1, 0),
(103, '311', 80, 5.00, 'Mazorca', 2500, 2625, 1, 0),
(104, '313', 69, 5.00, 'Papa criolla', 450, 450, 1, 0),
(105, '314', 50, 5.00, 'Papa', 4500, 4500, 1, 0),
(106, '315', 48, 5.00, 'Pepino', 4560, 4560, 1, 0),
(107, '317', 78, 5.00, 'Pimenton', 1581, 1660.05, 1, 0),
(108, '318', 80, 5.00, 'Platano amarillo', 2000, 2100, 1, 0),
(109, '320', 79, 5.00, 'Platano verde', 1850, 1942.5, 1, 0),
(110, '321', 80, 5.00, 'Remolacha', 2500, 2625, 1, 0),
(111, '322', 78, 5.00, 'Tomate', 2500, 2500, 1, 0),
(112, '323', 50, 5.00, 'Yuca', 3200, 3360, 1, 0),
(113, '325', 80, 19.00, 'Zanahoria', 3250, 3867.5, 1, 0);

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
-- Indices de la tabla `categorias`
--
ALTER TABLE `categorias`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `compras`
--
ALTER TABLE `compras`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK5clsikm051qlae1d6xjkpaik6` (`proveedor_id`);

--
-- Indices de la tabla `contabilidad`
--
ALTER TABLE `contabilidad`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `devoluciones`
--
ALTER TABLE `devoluciones`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `facturas`
--
ALTER TABLE `facturas`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `facturas_pos`
--
ALTER TABLE `facturas_pos`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `fruvert`
--
ALTER TABLE `fruvert`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKkcvshooaq2ijxnjcdrqdw0vaf` (`categoria_id`);

--
-- Indices de la tabla `inventarios`
--
ALTER TABLE `inventarios`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK39lc5d5pxvm4ythim6vc3qs3f` (`categoria_id`),
  ADD KEY `FKj4pw317cn3pxiuu99vvs2b7t4` (`proveedor_id`);

--
-- Indices de la tabla `pagos_datafono`
--
ALTER TABLE `pagos_datafono`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKrrkoxnxf3yt61xb8phtdex39s` (`factura_id`);

--
-- Indices de la tabla `producto`
--
ALTER TABLE `producto`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKp4qs1pv0f18sryjo1gglm1jlb` (`categoria_id`);

--
-- Indices de la tabla `producto_vendido`
--
ALTER TABLE `producto_vendido`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKssaaq1yqijvn9q87l4y6xdcoi` (`venta_id`);

--
-- Indices de la tabla `proveedor`
--
ALTER TABLE `proveedor`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `reportes`
--
ALTER TABLE `reportes`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `reporte_venta`
--
ALTER TABLE `reporte_venta`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK7lhmbq1cax81cg86pgv8acgu8` (`turno_id`),
  ADD KEY `FK7j5vtfipvqc3en0jsy7fja488` (`venta_id`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_ldv0v52e0udsh2h1rs0r0gw1n` (`nombre`);

--
-- Indices de la tabla `turnos`
--
ALTER TABLE `turnos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKb7mrbbhqfbs65k24il9nc7dl5` (`usuario_id`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuario_roles`
--
ALTER TABLE `usuario_roles`
  ADD PRIMARY KEY (`usuario_id`,`rol_id`),
  ADD KEY `FKbt9i9yrb9ug88xnh82n9m60pr` (`rol_id`);

--
-- Indices de la tabla `venta`
--
ALTER TABLE `venta`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKpittoyb1d8jt76tv8a17wh1ke` (`cliente_id`),
  ADD KEY `FKkjm9s3xb7ea7m7s7hk06cid6l` (`turno_id`);

--
-- Indices de la tabla `verdura`
--
ALTER TABLE `verdura`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKiupaaqjw1xmrmu79mbx3prir9` (`categoria_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `carnes`
--
ALTER TABLE `carnes`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=94;

--
-- AUTO_INCREMENT de la tabla `categorias`
--
ALTER TABLE `categorias`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT de la tabla `compras`
--
ALTER TABLE `compras`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `contabilidad`
--
ALTER TABLE `contabilidad`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `devoluciones`
--
ALTER TABLE `devoluciones`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `empleado`
--
ALTER TABLE `empleado`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `facturas`
--
ALTER TABLE `facturas`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `facturas_pos`
--
ALTER TABLE `facturas_pos`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `inventarios`
--
ALTER TABLE `inventarios`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `pagos_datafono`
--
ALTER TABLE `pagos_datafono`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `proveedor`
--
ALTER TABLE `proveedor`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `reportes`
--
ALTER TABLE `reportes`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `turnos`
--
ALTER TABLE `turnos`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `carnes`
--
ALTER TABLE `carnes`
  ADD CONSTRAINT `FKm2icyxmnfgdlnkex9cdephgnf` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`);

--
-- Filtros para la tabla `compras`
--
ALTER TABLE `compras`
  ADD CONSTRAINT `FK5clsikm051qlae1d6xjkpaik6` FOREIGN KEY (`proveedor_id`) REFERENCES `proveedor` (`id`);

--
-- Filtros para la tabla `fruvert`
--
ALTER TABLE `fruvert`
  ADD CONSTRAINT `FKkcvshooaq2ijxnjcdrqdw0vaf` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`);

--
-- Filtros para la tabla `inventarios`
--
ALTER TABLE `inventarios`
  ADD CONSTRAINT `FK39lc5d5pxvm4ythim6vc3qs3f` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`),
  ADD CONSTRAINT `FKj4pw317cn3pxiuu99vvs2b7t4` FOREIGN KEY (`proveedor_id`) REFERENCES `proveedor` (`id`);

--
-- Filtros para la tabla `pagos_datafono`
--
ALTER TABLE `pagos_datafono`
  ADD CONSTRAINT `FKrrkoxnxf3yt61xb8phtdex39s` FOREIGN KEY (`factura_id`) REFERENCES `facturas_pos` (`id`);

--
-- Filtros para la tabla `producto`
--
ALTER TABLE `producto`
  ADD CONSTRAINT `FKp4qs1pv0f18sryjo1gglm1jlb` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`);

--
-- Filtros para la tabla `producto_vendido`
--
ALTER TABLE `producto_vendido`
  ADD CONSTRAINT `FKssaaq1yqijvn9q87l4y6xdcoi` FOREIGN KEY (`venta_id`) REFERENCES `venta` (`id`);

--
-- Filtros para la tabla `reporte_venta`
--
ALTER TABLE `reporte_venta`
  ADD CONSTRAINT `FK7j5vtfipvqc3en0jsy7fja488` FOREIGN KEY (`venta_id`) REFERENCES `venta` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK7lhmbq1cax81cg86pgv8acgu8` FOREIGN KEY (`turno_id`) REFERENCES `turnos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `turnos`
--
ALTER TABLE `turnos`
  ADD CONSTRAINT `FKb7mrbbhqfbs65k24il9nc7dl5` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `usuario_roles`
--
ALTER TABLE `usuario_roles`
  ADD CONSTRAINT `FKbt9i9yrb9ug88xnh82n9m60pr` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`),
  ADD CONSTRAINT `FKuu9tea04xb29m2km5lwe46ua` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `venta`
--
ALTER TABLE `venta`
  ADD CONSTRAINT `FKkjm9s3xb7ea7m7s7hk06cid6l` FOREIGN KEY (`turno_id`) REFERENCES `turnos` (`id`),
  ADD CONSTRAINT `FKpittoyb1d8jt76tv8a17wh1ke` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`);

--
-- Filtros para la tabla `verdura`
--
ALTER TABLE `verdura`
  ADD CONSTRAINT `FKiupaaqjw1xmrmu79mbx3prir9` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
