# 🛒 Sistema de Gestión Integral de Supermercado

> Un sistema completo de gestión empresarial desarrollado en *Spring Boot* para la administración eficiente de supermercados y tiendas de retail.

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0+-green.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.0+-brightgreen.svg)](https://www.thymeleaf.org/)

## 🌟 Características Principales

### 📊 *Gestión Completa de Inventario*
- Control en tiempo real de productos (Frutas, Verduras, Carnes)
- Alertas automáticas de stock mínimo
- Seguimiento de movimientos de inventario
- Gestión de proveedores y compras

### 💰 *Sistema de Ventas y Facturación*
- Punto de venta (POS) integrado
- Facturación electrónica
- Gestión de devoluciones
- Reportes de ventas detallados

### 👥 *Administración de Personal*
- Control de empleados y roles
- Gestión de turnos de trabajo
- Sistema de autenticación y autorización
- Múltiples niveles de acceso

### 📈 *Reportes y Contabilidad*
- Dashboard de métricas en tiempo real
- Reportes financieros detallados
- Análisis de rentabilidad por producto
- Exportación de datos

## 🚀 Tecnologías Utilizadas

- *Backend:* Spring Boot, Spring Security, Spring Data JPA
- *Frontend:* Thymeleaf, HTML5, CSS3, JavaScript
- *Base de Datos:* MySQL
- *Arquitectura:* MVC (Model-View-Controller)
- *Seguridad:* Autenticación basada en roles
- *Email:* Integración para notificaciones

## 📁 Estructura del Proyecto


mini-control-empleados/
├── 📂 controlador/          # Controladores REST y Web
├── 📂 entidades/           # Modelos JPA/Hibernate
├── 📂 repositorios/        # Repositorios de datos
├── 📂 servicio/           # Lógica de negocio
├── 📂 ServiceImpl/        # Implementaciones de servicios
├── 📂 resources/
│   ├── 📂 static/         # CSS, JS, Imágenes
│   └── 📂 templates/      # Plantillas Thymeleaf
└── 📂 BD/                 # Scripts de base de datos


## ⚡ Funcionalidades por Módulo

### 🥩 *Gestión de Carnes*
- Registro de productos cárnicos
- Control de fechas de vencimiento
- Precios dinámicos por temporada

### 🍎 *Frutería y Verdulería (Fruver)*
- Inventario de productos frescos
- Control de calidad y rotación
- Gestión de pérdidas por deterioro

### 🧾 *Facturación Inteligente*
- Facturación electrónica DIAN
- Múltiples métodos de pago
- Integración con datafonos

### 📊 *Business Intelligence*
- KPIs en tiempo real
- Análisis de tendencias de venta
- Predicción de demanda

## 🛠 Instalación y Configuración

### Prerrequisitos
- Java 17 o superior
- MySQL 8.0+
- Maven 3.6+

### Pasos de instalación

1. *Clonar el repositorio*
   bash
   git clone https://github.com/tu-usuario/sistema-supermercado.git
   cd sistema-supermercado
   

2. *Configurar base de datos*
   - Crear base de datos MySQL
   - Ejecutar scripts en carpeta BD/
   - Configurar application.properties

3. *Ejecutar la aplicación*
   bash
   mvn spring-boot:run
   

4. *Acceder al sistema*
   - URL: http://localhost:8080
   - Usuario admin por defecto

## 🎯 Casos de Uso

✅ *Pequeños supermercados*  
✅ *Tiendas de barrio*  
✅ *Fruterías especializadas*  
✅ *Minimercados*  
✅ *Tiendas de conveniencia*  

## 📸 Capturas de Pantalla

### Dashboard Principal
![Dashboard](https://via.placeholder.com/800x400/4CAF50/white?text=Dashboard+Principal)

### Punto de Venta
![POS](https://via.placeholder.com/800x400/2196F3/white?text=Sistema+POS)

### Gestión de Inventario
![Inventario](https://via.placeholder.com/800x400/FF9800/white?text=Control+Inventario)

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (git checkout -b feature/AmazingFeature)
3. Commit tus cambios (git commit -m 'Add some AmazingFeature')
4. Push a la rama (git push origin feature/AmazingFeature)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## 👨‍💻 Autor

*Julián Pinto* - [GitHub](https://github.com/julianpinto15)

## 🌟 ¿Te gusta el proyecto?

Si este proyecto te ha sido útil, ¡dale una ⭐ en GitHub!

---

<div align="center">
  <strong>Desarrollado con ❤ para la gestión eficiente de tu negocio</strong>
</div>