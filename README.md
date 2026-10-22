# MotorMania - Gestión Móvil de Inventario Automotriz

## 📋 Descripción del Proyecto
**MotorMania** es una aplicación móvil nativa para Android desarrollada como Producto Mínimo Viable (MVP). Su objetivo principal es centralizar y optimizar la gestión operativa del inventario de un taller mecánico automotriz de tamaño pequeño. 

La aplicación permite al propietario del taller registrar productos (insumos, repuestos, lubricantes), controlar sus existencias en tiempo real, definir niveles de stock mínimo, y gestionar la ubicación física de los artículos. Con esto, se elimina la dependencia de anotaciones manuales y la memoria del dueño, mejorando la planificación de compras y la atención al cliente.

**Nota:** Este proyecto es de carácter netamente académico, utiliza datos ficticios y no contempla facturación electrónica ni integraciones con sistemas contables reales.

---

## 👨‍💻 Estudiantes
*   **Adrián Galo López Ortega**
*   **Ignacia Antonia Vasquez Erices**

**Docente:** Vicente Julio Zapata Valenzuela
**Asignatura:** Desarrollo Móvil (DSY1105)
**Sección:** 006D

---

## ✨ Funcionalidades Implementadas

La aplicación fue construida utilizando el patrón de arquitectura **MVVM (Model-View-ViewModel)** y las siguientes tecnologías: *Kotlin, Jetpack Compose, Room (SQLite), Coroutines y StateFlow*.

*   **Gestión de Productos (CRUD):** Registro, visualización, edición y eliminación de productos. Cada producto incluye campos como código único, imagen (URL), nombre, categoría, precio, cantidad disponible y stock mínimo.
*   **Validación de Formularios:** Control estricto de campos obligatorios y validación de tipos de datos (evitando precios o cantidades negativas) con mensajes de error en tiempo real.
*   **Persistencia Local:** Uso de la base de datos Room para garantizar que la información del inventario se conserve incluso si la aplicación se cierra o el dispositivo se reinicia. Funciona sin necesidad de conexión a internet.
*   **Gestión de Estado Reactivo:** Implementación de `StateFlow` para que la interfaz de usuario (UI) se actualice automáticamente ante cualquier cambio en la base de datos.
*   **Navegación Fluida:** Gestión de rutas centralizada (Navigation Compose) para transitar entre el inicio de sesión, el menú principal, la lista de productos y el escáner QR.
*   **Escáner de Códigos QR:** Integración con la cámara del dispositivo para leer códigos QR (ficticios) y agilizar la búsqueda de productos.
*   **Autenticación Básica:** Pantalla de inicio de sesión y registro de usuarios administradores del taller.

---

## 🚀 Pasos para Ejecutar el Proyecto

Para compilar y ejecutar este proyecto localmente, necesitas tener instalado **Android Studio** y el SDK de Android configurado.

Sigue estos pasos:

1.  **Clonar el repositorio:**
    Abre tu terminal o Git Bash y ejecuta:
    ```bash
    git clone https://github.com/l-AndyNic-l/MotorMania
