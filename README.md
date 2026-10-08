# 🗺️ Aplicación Android de Mapas (Mapa2)

Aplicación móvil desarrollada para Android en **Java**, diseñada para la gestión, visualización y exploración interactiva de mapas y ubicaciones geográficas, integrando componentes nativos y servicios de Google Maps.

## 🚀 Características Principales

- **Menú de Selección Principal:** Interfaz intuitiva que permite alternar entre diferentes vistas y configuraciones de mapas.
- **Integración con Google Maps SDK:** Visualización de mapas fluidos mediante `SupportMapFragment`.
- **Detección de Ubicación GPS en Tiempo Real:** 
  - Gestión de permisos de ubicación en tiempo real (Runtime Permissions).
  - Activación de la capa de ubicación nativa (punto azul).
  - Centrado automático de la cámara en las coordenadas actuales del usuario utilizando `FusedLocationProviderClient`.
- **Marcadores y Controles de Cámara:** Animaciones y zoom personalizados sobre puntos de interés específicos.

## 🛠️ Tecnologías y Herramientas Utilizadas

- **Lenguaje:** Java
- **Entorno de Desarrollo:** Android Studio
- **Librerías y APIs:**
  - Google Maps SDK for Android (`com.google.android.gms:play-services-maps`)
  - Google Play Services Location (`com.google.android.gms:play-services-location`)
- **Arquitectura:** Actividades y Fragments estándar de Android.

## 📦 Instalación y Configuración

1. **Clona el repositorio:**
   ```bash
   git clone [https://github.com/softimelody/Aplicacion-android-Mapas.git](https://github.com/softimelody/Aplicacion-android-Mapas.git)
