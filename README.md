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

```
mini-control-empleados/
├── 📂 controlador/     # Controladores REST y Web
├── 📂 entidades/       # Modelos JPA/Hibernate
├── 📂 repositorios/    # Repositorios de datos
├── 📂 servicio/        # Lógica de negocio
├── 📂 ServiceImpl/     # Implementaciones de servicios
├── 📂 resources/       # CSS, JS, Imágenes
│   ├── 📂 static/       # Recursos estáticos
│   └── 📂 templates/    # Plantillas Thymeleaf
└── 📂 BD/              # Scripts de base de datos
```        

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

## 📸 Galería de Capturas de Pantallaa

### 🔐 Autenticación y Seguridad

-   🛡️ Sistema de Login Seguro
  
![Image](https://github.com/user-attachments/assets/b972f9d0-49a5-4509-8e07-bd65d554bbfa)

### 🏠 Panel Principal

   - 🎛️ Dashboard Ejecutivo
![Image](https://github.com/user-attachments/assets/80980388-d32a-474a-8d7a-d6d163ee82b7)

### 💰 Módulo de Ventas

-    🛒 Terminal Punto de Venta (POS)
     
![Image](https://github.com/user-attachments/assets/72c7cf82-bb87-4ca3-8349-345a122cbc86)

 ### 📈 Reportes y Análisis de Ventas

![Image](https://github.com/user-attachments/assets/a7cb209e-ea65-4a98-9c2f-9458c0070072)


### 📦 Gestión de Inventarios

-   📋 Control General de Inventarioo
  
![Image](https://github.com/user-attachments/assets/9f0541e9-686d-491f-b234-aa1c40d72155)


### 🥫 Gestión de Productos No Perecederos

![Image](https://github.com/user-attachments/assets/f5b74b19-c1b4-432d-b51b-117b5f694b39)


### 🏷️ Sistema de Categorización

![Image](https://github.com/user-attachments/assets/a654494c-9dc9-424d-b640-6749f63eb4ec)


### 🛍️ Módulo de Compras

-   📝 Gestión de Pedidos y Compras
  
![Image](https://github.com/user-attachments/assets/ec56fe19-380e-4aec-aac2-e77777848cc0)


### 👥 Administración de Usuarios

-  🔧 Panel de Gestión de Usuarios
  
![Image](https://github.com/user-attachments/assets/2d3eb161-3c03-409c-bf3a-1c0cb0290b3b)


### 💼 Sistema Contable Integral

-   📊 Dashboard Contable Principal
  
![Image](https://github.com/user-attachments/assets/b671fe04-2818-45b4-920a-7d6076325afc)


### 💸 Módulo de Ventas Contables

![Image](https://github.com/user-attachments/assets/18083a49-ec2f-4335-8b95-2c9c2367baab)

### 🏭 Gestión de Proveedores

![Image](https://github.com/user-attachments/assets/42c5c7eb-bd13-41d4-b1cf-59672ba27297)

### 🧾 Control de IVA y Tributación

![Image](https://github.com/user-attachments/assets/b13a1589-7e47-4fbf-905c-80487705a857)

### 📈 Análisis Financiero Avanzado

![Image](https://github.com/user-attachments/assets/af6ed7ab-4991-4dfd-af0a-5563f4ba441a)


## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## 👨‍💻 Autor

- *Julián Pinto* - [GitHub](https://github.com/julianpinto15)  
- *Santiago Barbosa* - [GitHub](https://github.com/BARBOSA191919)  
- *Diego Martinez* - [GitHub](https://github.com/Difa98)  
- *Nicolas Sanabria* - [GitHub](https://github.com/NicolasSE05)

## 🌟 ¿Te gusta el proyecto?

Si este proyecto te ha sido útil, ¡dale una ⭐ en GitHub!
*[⭐ Star en GitHub](https://github.com/BARBOSA191919/Software-supermercado-P.)*

---

<div align="center">
  <strong>Desarrollado con ❤ para la gestión eficiente de tu negocio</strong>
</div>
