# Guía de Configuración - HelpMe App

Este documento explica cómo configurar el proyecto después de clonarlo desde el repositorio.

## Archivos NO incluidos en el repositorio

Por razones de seguridad, los siguientes archivos **NO están incluidos** en el repositorio y debes configurarlos manualmente:

### 1. `google-services.json`

Este archivo contiene las credenciales de Firebase y debe obtenerse desde Firebase Console.

**Pasos para obtenerlo:**

1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Selecciona el proyecto **HelpMe** (o crea uno nuevo)
3. Ve a **Configuración del proyecto** (ícono de engranaje)
4. En la sección **Tus apps**, selecciona la app Android
5. Descarga el archivo `google-services.json`
6. Coloca el archivo en: `app/google-services.json`

**Ubicación correcta:**
```
MyApplication/
├── app/
│   ├── google-services.json  ← AQUÍ
│   ├── build.gradle.kts
│   └── src/
```

### 2. Google Maps API Key

La API Key de Google Maps debe configurarse en el archivo `AndroidManifest.xml`.

**Pasos:**

1. Ve a [Google Cloud Console](https://console.cloud.google.com/)
2. Selecciona tu proyecto o crea uno nuevo
3. Ve a **APIs y servicios** → **Credenciales**
4. Crea una **API Key** o usa una existente
5. Habilita las siguientes APIs:
   - Maps SDK for Android
   - Geocoding API

6. Edita el archivo `app/src/main/AndroidManifest.xml`
7. Busca esta línea:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="TU_API_KEY_AQUI" />
   ```
8. Reemplaza `TU_API_KEY_AQUI` con tu API Key real

### 3. Configuración de Firebase

Después de agregar `google-services.json`, configura estos servicios en Firebase Console:

#### Firebase Authentication
1. Ve a **Authentication** → **Sign-in method**
2. Habilita **Email/Password**

#### Realtime Database
1. Ve a **Realtime Database**
2. Crea una base de datos
3. Selecciona modo **test** o configura estas reglas:
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

#### Firebase Storage
1. Ve a **Storage**
2. Configura estas reglas:
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

## Pasos para ejecutar el proyecto

### 1. Clonar el repositorio
```bash
git clone <url-del-repositorio>
cd MyApplication
```

### 2. Configurar archivos sensibles
- Agregar `google-services.json` (ver arriba)
- Configurar Google Maps API Key (ver arriba)

### 3. Sincronizar proyecto
- Abrir el proyecto en Android Studio
- Esperar a que Gradle sincronice
- Si hay errores: **Build** → **Clean Project** → **Rebuild Project**

### 4. Ejecutar
- Conectar un dispositivo Android o iniciar un emulador
- Presionar **Run** (▶️)

## Solución de problemas

### Error: "File google-services.json is missing"
- Verifica que el archivo esté en `app/google-services.json`
- Sincroniza el proyecto con Gradle

### Error: "Google Maps no se muestra"
- Verifica que la API Key esté correctamente configurada
- Asegúrate de habilitar Maps SDK en Google Cloud Console

### Error de autenticación en Firebase
- Verifica que Firebase Authentication esté habilitado
- Asegúrate de que el package name coincida: `com.example.myapplication`

## Contacto

Si tienes problemas con la configuración, contacta al equipo de desarrollo:

| NOMBRE                           | CARNÉ        |
|----------------------------------|--------------|
| Esvin Elizandro Urizar           | 0902-24-3618 |
| Herberth Eduardo Coc Chon        | 0902-24-7727 |
| Rony Estuardo Salvador Soc Lux   | 0902-24-4875 |
| Elizabeth Camila Toledo Valdez   | 0902-24-7715 |
| Jason Isrrael Ajxup Cojoc        | 0902-24-478  |

---

**Última actualización:** Octubre 2025

