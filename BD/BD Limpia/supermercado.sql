-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 19-12-2024 a las 23:31:43
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
(77, 408, 80, 19.00, 'Costilla de res', 4500, 4500, 4, 0),
(78, 409, 80, 19.00, 'Higado', 4500, 4500, 4, 0),
(79, 410, 80, 19.00, 'Lomo ancho y lomo fino', 5000, 5000, 4, 0),
(80, 411, 80, 19.00, 'Lomo de cerdo', 5000, 5000, 4, 0),
(81, 412, 80, 19.00, 'Menudencia', 2500, 2500, 4, 0),
(82, 413, 80, 19.00, 'Mojarra', 2500, 2500, 4, 0),
(83, 414, 50, 19.00, 'Muslos y piernas', 2500, 2500, 4, 0),
(84, 415, 50, 19.00, 'Pargo rojo', 5000, 5000, 4, 0),
(85, 416, 80, 19.00, 'Pechuga', 2500, 2500, 4, 0),
(86, 417, 80, 19.00, 'Pernil', 2500, 2500, 4, 0),
(87, 418, 50, 19.00, 'Pescado y mariscos', 2000, 2000, 4, 0),
(88, 419, 80, 19.00, 'Punta de anca', 1500, 1500, 4, 0),
(89, 420, 50, 19.00, 'Res', 2500, 2500, 4, 0),
(90, 421, 2500, 19.00, 'róbalo', 1500, 1785, 4, 0),
(91, 422, 50, 19.00, 'Sobrebarriga', 3500, 4165, 4, 0),
(92, 423, 80, 19.00, 'tilapia', 7500, 7500, 4, 0),
(93, 424, 80, 5.00, 'Tocino', 1450, 1522.5, 4, 0);

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
(4, 4, 'carnes');

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
(2, 'perez', 12117922, 'Juan '),
(3, 'barbosa ', 1003865544, 'santiago '),
(4, 'asdasd', 2132, 'asdasd'),
(5, 'sdas', 122312, 'asda');

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
(2, 231, '2024-12-09', 'producto_defectuoso', 2131213),
(3, 12312, '2024-12-09', 'cambio', 213123123),
(4, 9999, '2024-12-09', 'producto_defectuoso', 99999),
(5, 2312, '2024-12-09', 'producto_defectuoso', 999999),
(6, 23, '2024-12-12', 'cambio', 32000);

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
(1, 'Oviedo', '1003865544', 'Jesus', '3217696864');

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
(128, '222', 79, 19.00, 'Mora', 2500, 2975, 2, 0),
(129, '223', 50, 19.00, 'Naranja', 2650, 3153.5, 2, 0),
(130, '224', 80, 19.00, 'Papaya', 3520, 4188.8, 2, 0),
(131, '225', 50, 19.00, 'Peras', 2650, 3153.5, 2, 0),
(132, '226', 80, 19.00, 'Pitahaya Amarilla', 2560, 3046.4, 2, 0),
(133, '227', 80, 19.00, 'Pitahaya Roja', 2500, 2975, 2, 0),
(134, '228', 80, 19.00, 'Tomate de arbol', 2400, 2856, 2, 0),
(135, '229', 80, 19.00, 'Uchuva', 2500, 2975, 2, 0),
(136, '230', 90, 19.00, 'Uva verde', 3650, 4343.5, 2, 0),
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
(286);

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
(218, '00012', 100, 19.00, 'Arroz Diana 500g', 2000, 2380, 3, 10),
(219, '0002', 2992, 19.00, 'Leche Alqueria 500 ml', 1850, 2201.5, 3, 5),
(220, '0003', 96, 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 3, 2),
(237, '232606', 796, 19.00, 'Arroz diana 1kg', 2200, 2618, 3, 2),
(238, '1656', 4, 19.00, '1 Arroba de Arroz Diana ', 45000, 48195, 3, 10);

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
(223, 1, '201', 19.00, 'Naranja', 2650, 3153.5, 221, NULL),
(224, 1, '0001', 19.00, 'Arroz Diana 500g', 2000, 2380, 221, NULL),
(225, 1, '0003', 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 221, NULL),
(226, 1, '0002', 19.00, 'Leche Alqueria 500 ml', 1850, 2201.5, 221, NULL),
(227, 1, '204', 19.00, 'Manzana roja', 3200, 3200, 221, NULL),
(232, 2, '302', 19.00, 'Apio', 2500, 2975, 230, NULL),
(235, 5, '20265', 19.00, 'Bananos', 1300, 1300, 233, NULL),
(236, 3, '0002', 19.00, 'Leche Alqueria 500 ml', 1850, 2201.5, 233, NULL),
(249, 4, '201', 19.00, 'Naranja', 2650, 3153.5, 247, NULL),
(252, 4, '320', 5.00, 'Platano verde', 1850, 1942.5, 250, NULL),
(253, 4, '20265', 19.00, 'Bananos', 1300, 1300, 250, NULL),
(262, 3, '411', 19.00, 'Menudencia', 2500, 2500, 260, NULL),
(265, 2, '0003', 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 263, NULL),
(266, 2, '206', 19.00, 'Arandanos', 2560, 3046.4, 263, NULL),
(267, 2, '401', 19.00, 'Bagre', 25000, 25000, 263, NULL),
(271, 1, '232606', 19.00, 'Arroz diana 1kg', 2200, 2618, 269, 2),
(274, 3, '232606', 19.00, 'Arroz diana 1kg', 2200, 2618, 272, 2),
(275, 3, '0002', 19.00, 'Leche Alqueria 500 ml', 1850, 2201.5, 272, 5),
(276, 4, '1656', 19.00, '1 Arroba de Arroz Diana ', 45000, 48195, 272, 10),
(282, 2, '1656', 19.00, '1 Arroba de Arroz Diana ', 45000, 48195, 280, 10),
(283, 2, '0003', 19.00, 'Frijol boliroja 500 ml', 4500, 5355, 280, 2),
(284, 1, '222', 19.00, 'Mora', 2500, 2975, 280, 0),
(285, 1, '0002', 19.00, 'Leche Alqueria 500 ml', 1850, 2201.5, 280, 5);

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
(6, 'dasdsad', 'sbarbosarivas@gmail.commm', 'asdasds', '12321321');

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
(222, 0, '2024-12-07', NULL, 0, 1, 221),
(228, 5, '2024-12-07', 14200, 16290, 1, NULL),
(229, 5, '2024-12-07', 14200, 16290, 1, NULL),
(231, 0, '2024-12-09', NULL, 0, 1, 230),
(234, 0, '2024-12-16', NULL, 0, 1, 233),
(248, 0, '2024-12-18', NULL, 0, 1, 247),
(251, 0, '2024-12-18', NULL, 0, 1, 250),
(261, 0, '2024-12-19', NULL, 0, 1, 260),
(264, 0, '2024-12-19', NULL, 0, 1, 263),
(270, 0, '2024-12-19', NULL, 0, 1, 269),
(273, 0, '2024-12-19', NULL, 0, 1, 272),
(281, 0, '2024-12-19', NULL, 0, 1, 280);

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
  `nombre` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `turnos`
--

INSERT INTO `turnos` (`id`, `base_dinero`, `caja`, `fecha`, `hora_inicio`, `hora_salida`, `nombre`) VALUES
(1, 3000000, 'cajero1', '2024-12-07 00:00:00.000000', '07:00', '19:00', 'Jesus Oviedo');

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
(22, '$2a$10$F8Ilndm/naxT/w2HnHGzAOlEIdzb28d1.OPpSLE/7OVvNicc7JOiy', NULL, 'Jesus5544', b'1', '5544');

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
(221, 3710, 20000, '2024-12-07 12:23:47', 1, 1),
(230, 50, 6000, '2024-12-09 10:18:15', 1, 1),
(233, 1895.5, 15000, '2024-12-16 15:26:10', 1, 1),
(247, 7386, 20000, '2024-12-18 11:38:46', 1, 1),
(250, 7030, 20000, '2024-12-18 12:02:41', 1, 1),
(260, 2500, 10000, '2024-12-19 09:46:01', 1, 1),
(263, 3197.2, 70000, '2024-12-19 09:52:15', 1, 1),
(269, 2382, 5000, '2024-12-19 16:13:06', 1, 1),
(272, 92761.5, 300000, '2024-12-19 16:26:10', 1, 1),
(280, 7723.5, 120000, '2024-12-19 18:30:57', 1, 1);

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
(104, '312', 50, 5.00, 'Papa criolla', 450, 450, 1, 0),
(105, '313', 50, 5.00, 'Papa', 4500, 4500, 1, 0),
(106, '314', 50, 5.00, 'Pepino', 4560, 4560, 1, 0),
(107, '315', 80, 5.00, 'Pimenton', 1581, 1660.05, 1, 0),
(108, '316', 80, 5.00, 'Platano amarillo', 2000, 2100, 1, 0),
(109, '317', 80, 5.00, 'Platano verde', 1850, 1942.5, 1, 0),
(110, '318', 80, 5.00, 'Remolacha', 2500, 2625, 1, 0),
(111, '319', 80, 5.00, 'Tomate', 2500, 2500, 1, 0),
(112, '320', 50, 5.00, 'Yuca', 3200, 3360, 1, 0),
(113, '321', 80, 19.00, 'Zanahoria', 3250, 3867.5, 1, 0);

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
  ADD PRIMARY KEY (`id`);

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
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

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
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `empleado`
--
ALTER TABLE `empleado`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

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
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

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
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- Restricciones para tablas volcadas
--

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
  ADD CONSTRAINT `FK7j5vtfipvqc3en0jsy7fja488` FOREIGN KEY (`venta_id`) REFERENCES `venta` (`id`),
  ADD CONSTRAINT `FK7lhmbq1cax81cg86pgv8acgu8` FOREIGN KEY (`turno_id`) REFERENCES `turnos` (`id`);

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
