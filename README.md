# HelpMe - Red de Ayuda Ciudadana

**HelpMe** es una aplicación móvil Android que permite a los ciudadanos reportar emergencias y situaciones comunitarias en tiempo real, incluyendo ubicación geográfica y evidencia fotográfica. La aplicación utiliza Firebase como backend y Google Maps para visualización geoespacial.

---

## Características Principales

### Autenticación de Usuarios[]()
- Login con correo electrónico y contraseña
- Registro de nuevos usuarios
- Recuperación de contraseña
- Opción "Recordar sesión" para mantener la sesión activa
- Confirmación al salir de la aplicación
- Cierre de sesión manual

### Gestión de Reportes
- **Crear reportes** con los siguientes datos:
  - Tipo de reporte (Seguridad, Infraestructura, Animal Perdido, Accidente, Otro)
  - Descripción detallada
  - Ubicación geográfica (latitud, longitud y nombre de la ciudad)
  - Fotografía adjunta
  - Estado (Pendiente/Resuelto)

- **Visualizar reportes** en dos formatos:
  - **Mapa interactivo**: Marcadores dinámicos que muestran solo reportes activos
  - **Lista filtrable**: Permite filtrar por tipo, estado y fecha

- **Actualizar estado**: Cambiar reportes de Pendiente a Resuelto y viceversa

### Visualización en Mapa
- Mapa de Google Maps con marcadores personalizados
- Botón para centrar en ubicación actual del usuario
- Contador de reportes activos
- Información detallada al seleccionar un marcador

### Métricas y Estadísticas
- Contador de reportes por tipo (Seguridad, Infraestructura, etc.)
- Contador de reportes por estado (Pendiente, Resuelto)
- Visualización clara y organizada de las estadísticas

---

## Tecnologías Utilizadas

### Lenguajes y Frameworks
- **Kotlin**: Lenguaje de programación principal
- **Jetpack Compose**: Framework moderno para UI declarativa
- **Android SDK**: API Level 24 (Android 7.0) - Target 36

### Firebase Services
- **Firebase Authentication**: Gestión de usuarios y autenticación
- **Firebase Realtime Database**: Almacenamiento de reportes en tiempo real
- **Firebase Storage**: Almacenamiento de fotografías

### APIs y Servicios de Google
- **Google Maps SDK**: Visualización de mapas
- **Google Location Services**: Obtención de ubicación GPS
- **Geocoder API**: Conversión de coordenadas a nombres de ciudades

### Librerías Adicionales
- **Navigation Compose**: Navegación entre pantallas
- **Coil**: Carga y caché de imágenes
- **Material Design 3**: Componentes de UI modernos
- **ViewModel & LiveData**: Arquitectura MVVM

---

## Estructura del Proyecto

```
app/src/main/java/com/example/myapplication/
├── model/
│   └── Report.kt                    # Modelo de datos para reportes
├── repository/
│   ├── AuthRepository.kt            # Lógica de autenticación
│   └── ReportRepository.kt          # Lógica de reportes (CRUD)
├── viewmodel/
│   ├── AuthViewModel.kt             # ViewModel de autenticación
│   └── ReportViewModel.kt           # ViewModel de reportes
├── ui/
│   ├── screens/
│   │   ├── LoginScreen.kt           # Pantalla de login/registro
│   │   ├── HomeScreen.kt            # Pantalla principal
│   │   ├── CreateReportScreen.kt    # Formulario de creación
│   │   ├── ReportListScreen.kt      # Lista de reportes
│   │   ├── MapViewScreen.kt         # Vista de mapa
│   │   └── MetricsScreen.kt         # Pantalla de métricas
│   └── theme/
│       ├── Color.kt                 # Colores del tema
│       ├── Theme.kt                 # Configuración del tema
│       └── Type.kt                  # Tipografía
├── navigation/
│   └── Screen.kt                    # Definición de rutas
├── utils/
│   └── PreferencesManager.kt        # Gestión de preferencias
└── MainActivity.kt                  # Activity principal
```

---

## Requisitos Previos

### Software Necesario
1. **Android Studio** (versión Hedgehog o superior)
2. **JDK 11** o superior
3. **SDK de Android** con API Level 24 o superior
4. Cuenta de **Firebase**
5. **Google Maps API Key**

### Configuración de Firebase
1. Crear un proyecto en [Firebase Console](https://console.firebase.google.com/)
2. Registrar la aplicación Android con el package name: `com.example.myapplication`
3. Descargar el archivo `google-services.json`
4. Colocar el archivo en: `app/google-services.json`
5. Habilitar los siguientes servicios:
   - **Authentication** (Email/Password)
   - **Realtime Database**
   - **Storage**

### Configuración de Google Maps
1. Obtener una API Key desde [Google Cloud Console](https://console.cloud.google.com/)
2. Habilitar las siguientes APIs:
   - Maps SDK for Android
   - Geocoding API
3. **IMPORTANTE**: Restringir la API Key para mayor seguridad:
   - En Google Cloud Console, ve a tu API Key
   - En "Restricciones de aplicación", selecciona "Aplicaciones Android"
   - Agrega el package name: `com.example.myapplication`
   - Agrega tu SHA-1 fingerprint
4. Agregar la API Key en `AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="TU_API_KEY_AQUI" />
   ```

> **NOTA DE SEGURIDAD**: Por razones de seguridad, la API Key incluida en este repositorio está restringida. Si clonas este proyecto, debes crear tu propia API Key de Google Maps siguiendo los pasos anteriores.

---

## Instalación y Configuración

### 1. Clonar o Descargar el Proyecto
```bash
git clone <url-del-repositorio>
cd MyApplication
```

### 2. Configurar Firebase
- Asegurarse de que `app/google-services.json` esté presente
- Verificar que el package name coincida con el de Firebase

### 3. Configurar Google Maps API Key
- Editar `app/src/main/AndroidManifest.xml`
- Reemplazar el valor de `com.google.android.geo.API_KEY` con tu API Key

### 4. Sincronizar el Proyecto
- Abrir el proyecto en Android Studio
- Esperar a que Gradle sincronice las dependencias
- Si hay errores, ejecutar: **Build → Clean Project** y luego **Build → Rebuild Project**

### 5. Ejecutar la Aplicación
- Conectar un dispositivo Android o iniciar un emulador
- Presionar el botón **Run** (▶️) en Android Studio
- Seleccionar el dispositivo de destino

---

## Permisos Requeridos

La aplicación solicita los siguientes permisos:

| Permiso | Uso |
|---------|-----|
| `INTERNET` | Conexión con Firebase y Google Maps |
| `ACCESS_FINE_LOCATION` | Obtener ubicación precisa del usuario |
| `ACCESS_COARSE_LOCATION` | Ubicación aproximada |
| `CAMERA` | Capturar fotos para reportes |
| `READ_EXTERNAL_STORAGE` | Leer imágenes de la galería (Android 12 e inferior) |
| `READ_MEDIA_IMAGES` | Leer imágenes (Android 13+) |

---

## Guía de Uso

### 1. Inicio de Sesión
- Al abrir la app, ingresar correo y contraseña
- Para nuevos usuarios, presionar **"¿No tienes cuenta? Regístrate"**
- Marcar **"Recordar sesión"** para mantener la sesión activa
- Si olvidaste tu contraseña, usar **"¿Olvidaste tu contraseña?"**

### 2. Pantalla Principal (Home)
Desde aquí puedes acceder a:
- **Crear Reporte**: Formulario para nuevo reporte
- **Ver Reportes**: Lista completa con filtros
- **Ver Mapa**: Visualización geoespacial
- **Métricas**: Estadísticas de reportes
- **Salir**: Cerrar la aplicación (con confirmación)
- **Cerrar Sesión**: Salir de la cuenta (con confirmación)

### 3. Crear Reporte
1. Seleccionar el **tipo de reporte**
2. Escribir una **descripción** detallada
3. Obtener ubicación automática o usar ubicación actual
4. **Capturar foto** desde la cámara o seleccionar de la galería
5. Presionar **"Crear Reporte"**

### 4. Ver Reportes (Lista)
- Ver todos los reportes ordenados por fecha (más recientes primero)
- Filtrar por:
  - **Tipo**: Seguridad, Infraestructura, etc.
  - **Estado**: Pendiente o Resuelto
- Cambiar estado de reportes directamente desde la lista
- Ver detalles completos incluyendo foto, ubicación y ciudad

### 5. Ver Mapa
- Visualizar todos los reportes activos como marcadores
- Presionar un marcador para ver información del reporte
- Usar el botón de ubicación para centrar en tu posición
- Ver contador de reportes activos en la parte superior

### 6. Métricas
- Visualizar cantidad de reportes por tipo
- Ver distribución entre reportes Pendientes y Resueltos
- Estadísticas actualizadas en tiempo real

---

## Arquitectura y Patrones de Diseño

### Patrón MVVM (Model-View-ViewModel)
- **Model**: Clases de datos (`Report`, enums)
- **View**: Composables de UI (Screens)
- **ViewModel**: Lógica de presentación y estado
- **Repository**: Capa de acceso a datos (Firebase)

### Flujo de Datos
```
UI (Composable) → ViewModel → Repository → Firebase
                   ↓
              StateFlow/State
                   ↓
          UI se actualiza automáticamente
```

### Gestión de Estado
- **StateFlow**: Para estados reactivos
- **MutableState**: Para estados locales de UI
- **SharedPreferences**: Para persistencia de preferencias

---

## Base de Datos (Firebase Realtime Database)

### Estructura de Datos

```json
{
  "reports": {
    "reportId1": {
      "id": "reportId1",
      "type": "SEGURIDAD",
      "description": "Descripción del reporte",
      "latitude": 14.634915,
      "longitude": -90.506882,
      "photoUrl": "https://firebasestorage.googleapis.com/...",
      "status": "PENDIENTE",
      "userId": "uid123",
      "timestamp": 1699999999999
    }
  }
}
```

### Campos del Reporte

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | String | ID único del reporte |
| `type` | String | Tipo de reporte (enum) |
| `description` | String | Descripción detallada |
| `latitude` | Double | Latitud GPS |
| `longitude` | Double | Longitud GPS |
| `photoUrl` | String | URL de la foto en Storage |
| `status` | String | Estado (PENDIENTE/RESUELTO) |
| `userId` | String | ID del usuario que creó el reporte |
| `timestamp` | Long | Timestamp de creación |

---

## Funcionalidades Implementadas

### Requisitos del Proyecto - SEGÚN DADAS POR EL CATEDRATICO

#### ✅ UI Material + Navegación
- Material Design 3 con Jetpack Compose
- Navegación intuitiva entre pantallas
- Botones claramente identificados
- Diseño responsive y moderno

#### ✅ CRUD Firebase
- **Create**: Crear nuevos reportes con validaciones
- **Read**: Listar reportes con filtros múltiples
- **Update**: Cambiar estado de reportes
- **Delete**: (Opcional) Implementable según necesidad

#### ✅ Mapa con marcadores dinámicos
- Google Maps integrado
- Marcadores que se actualizan en tiempo real
- Información detallada por marcador
- Centrado en ubicación del usuario

#### ✅ Auth + Storage
- Login y registro con Firebase Auth
- Recuperación de contraseña
- Recordar sesión
- Subida de fotos a Firebase Storage
- URLs persistentes para imágenes

#### ✅ Documentación
- README.md completo con toda la información
- Comentarios en código donde es necesario
- Estructura clara del proyecto

### Funcionalidades Adicionales

- **Confirmación de salida**: Diálogo de confirmación al salir de la app
- **Persistencia de sesión**: Opción de recordar sesión entre aperturas
- **Nombres de ciudades**: Geocodificación inversa para mostrar ubicaciones
- **Validaciones robustas**: Campos requeridos, formatos de email, etc.
- **Manejo de errores**: Toasts y Snackbars informativos
- **Carga de imágenes optimizada**: Uso de Coil para caché eficiente

---

## Manejo de Errores

La aplicación incluye manejo de errores para:

- **Errores de autenticación**: Usuario no existe, contraseña incorrecta, email inválido
- **Errores de red**: Sin conexión a internet
- **Errores de permisos**: Ubicación, cámara, almacenamiento
- **Errores de Firebase**: Problemas de conexión, escritura/lectura
- **Validaciones de formulario**: Campos vacíos, formatos incorrectos

Todos los errores se muestran al usuario mediante mensajes claros y concisos.

---

## Configuración de Reglas de Firebase

### Realtime Database Rules
```json
{
  "rules": {
    "reports": {
      ".read": "auth != null",
      ".write": "auth != null"
    }
  }
}
```

### Storage Rules
```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /reports/{allPaths=**} {
      allow read: if request.auth != null;
      allow write: if request.auth != null;
    }
  }
}
```

---

## Versiones y Compatibilidad

- **Versión de la App**: 1.0
- **Versión Mínima Android**: 7.0 (API 24)
- **Versión Target**: Android 14 (API 36)
- **Kotlin**: 1.9.x
- **Gradle**: 8.x
- **Jetpack Compose**: BOM 2024.x

---

## Generación de APK

### APK de Debug
```bash
./gradlew assembleDebug
```
El APK se generará en: `app/build/outputs/apk/debug/app-debug.apk`

### APK de Release (Firmado)
```bash
./gradlew assembleRelease
```

---

## Solución de Problemas Comunes

### Error: "Google Maps API Key inválida"
- Verificar que la API Key esté correctamente configurada en AndroidManifest.xml
- Asegurarse de que Maps SDK for Android esté habilitado en Google Cloud Console
- Verificar que el package name coincida con el registrado en Google Cloud

### Error: "Firebase not initialized"
- Verificar que google-services.json esté en app/
- Sincronizar el proyecto con Gradle
- Limpiar y reconstruir el proyecto

### La ubicación no se obtiene
- Verificar permisos de ubicación en configuración del dispositivo
- Activar GPS en el dispositivo
- Ejecutar en dispositivo físico (emuladores pueden tener problemas con GPS)

### Las imágenes no se cargan
- Verificar conexión a internet
- Verificar reglas de Firebase Storage
- Revisar permisos de almacenamiento

---

## Autor

**Facultad de Ingeniería en Sistemas**  
Proyecto 1 (Móvil) - HelpMe: Red de ayuda ciudadana

---

## Licencia

- Este proyecto es un trabajo académico desarrollado para fines educativos, por estidiantes de la Facultad de Ingeniería en Sistemas, de la universidad mariano gálvez de guatemala,
- No debe ser utilizado con fines comerciales sin autorización previa.

---

## Notas Adicionales

### Mejoras Futuras (Opcionales)
- Notificaciones push cuando un reporte cambia de estado
- Sistema de comentarios en reportes
- Votos o "me gusta" en reportes
- Filtro por distancia (reportes cercanos)
- Modo oscuro
- Soporte multiidioma
- Exportación de reportes a PDF
- Estadísticas más avanzadas con gráficos

### Consideraciones de Seguridad
- No compartir API Keys en repositorios públicos
- Usar variables de entorno para claves sensibles
- Implementar reglas de seguridad robustas en Firebase
- Validar datos tanto en cliente como en servidor

### Optimizaciones de Rendimiento
- Paginación en lista de reportes (si hay muchos datos)
- Caché de imágenes con Coil
- Lazy loading en listas
- Compresión de imágenes antes de subir

---

## Contacto y Soporte

Para preguntas, problemas o sugerencias relacionadas con este proyecto, contactar al equipo de desarrollo o al docente del curso.

Estudiantes:

| NOMBRE                           | CARNÉ        |
|----------------------------------|--------------|
| `Esvin Elizandro Urizar`         | 0902-24-3618 |
| `Herberth Eduardo Coc Chon`      | 0902-24-7727 |
| `Rony Estuardo Salvador Soc Lux` | 0902-24-4875 |
| `Elizabeth Camila Toledo Valdez` | 0902-24-7715 |
| `Jason Isrrael Ajxup Cojoc`      | 0902-24-478  |

---

**Última actualización**: Octubre 2025
