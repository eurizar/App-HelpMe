# Descarga de APK - HelpMe App

Esta carpeta contiene el archivo APK de la aplicación HelpMe para instalación directa en dispositivos Android.

## Descargar e Instalar

### Requisitos:
- Dispositivo Android con versión 7.0 (API 24) o superior
- Permisos para instalar aplicaciones de fuentes desconocidas

### Pasos de Instalación:

1. **Descargar el APK**
   - Descarga el archivo `HelpMe-v1.0.apk` desde esta carpeta

2. **Habilitar instalación de fuentes desconocidas**
   - Ve a Configuración → Seguridad
   - Activa "Fuentes desconocidas" o "Instalar aplicaciones desconocidas"
   - En Android 8.0+, permite la instalación para tu navegador o gestor de archivos

3. **Instalar la aplicación**
   - Abre el archivo APK descargado
   - Presiona "Instalar"
   - Espera a que termine la instalación
   - Presiona "Abrir" para iniciar la app

4. **Configurar permisos**
   - La app solicitará permisos de:
     - Ubicación (necesario para reportes)
     - Cámara (para capturar fotos)
     - Almacenamiento (para seleccionar imágenes)
   - Acepta los permisos para usar todas las funcionalidades

## Configuración Inicial

Antes de usar la aplicación, necesitas:

1. **Crear una cuenta de Firebase** (si vas a usarla en producción)
2. **Configurar tu propia API Key de Google Maps** (ver documentación principal)

Para la versión de demostración incluida, puedes usar las credenciales preconfiguradas.

## Versiones Disponibles

| Versión | Fecha | Tamaño | Descripción |
|---------|-------|--------|-------------|
| v1.0 | Octubre 2025 | ~10 MB | Versión inicial del proyecto |

## Notas Importantes

- **Seguridad**: Este APK es para fines académicos y de demostración
- **Actualizaciones**: Las actualizaciones deben instalarse manualmente descargando nuevas versiones
- **Soporte**: Para problemas o dudas, contactar al equipo de desarrollo

## Problemas Comunes

### La app no se instala
- Verifica que tengas espacio suficiente en tu dispositivo
- Asegúrate de haber habilitado fuentes desconocidas
- Si ya tienes una versión instalada, desinstálala primero

### La app se cierra al abrir
- Verifica que tu Android sea versión 7.0 o superior
- Asegúrate de tener conexión a internet
- Verifica que los servicios de ubicación estén activos

### No puedo crear reportes
- Acepta todos los permisos solicitados
- Verifica tu conexión a internet
- Asegúrate de que Firebase esté correctamente configurado

---

Para más información, consulta el README.md principal del proyecto.

