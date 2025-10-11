# Guía Rápida de Configuración - HelpMe

## 📱 PASOS PARA VER LA APLICACIÓN

### PASO 1: Abrir el Proyecto en Android Studio
1. Abre Android Studio
2. File → Open
3. Selecciona la carpeta: C:\Users\eliza\AndroidStudioProjects\MyApplication
4. Espera a que cargue el proyecto

### PASO 2: Sincronizar Dependencias (MUY IMPORTANTE)
1. En Android Studio, busca la barra superior
2. Haz clic en el icono de elefante (Gradle) o ve a:
   File → Sync Project with Gradle Files
3. ESPERA a que termine de descargar todas las dependencias
   (Esto puede tomar 5-10 minutos la primera vez)
4. Verás una barra de progreso abajo que dice "Syncing..."

### PASO 3: Configurar Firebase (RÁPIDO - Modo Desarrollo)

#### Opción A: Usar Firebase de Prueba (Recomendado para empezar)
1. Ve a: https://console.firebase.google.com/
2. Haz clic en "Agregar proyecto"
3. Nombre: "HelpMe" (o el que prefieras)
4. Desactiva Google Analytics (no lo necesitas por ahora)
5. Clic en "Crear proyecto" - espera 30 segundos
6. Clic en "Continuar"

#### Configurar la App Android:
1. En Firebase Console, clic en el ícono de Android (</>) 
2. Package name: com.example.myapplication
3. App nickname: HelpMe
4. Clic en "Registrar app"
5. DESCARGA el archivo google-services.json
6. COPIA ese archivo y pégalo en:
   C:\Users\eliza\AndroidStudioProjects\MyApplication\app\
   (Reemplaza el que está ahí)
7. Clic en "Siguiente" → "Siguiente" → "Continuar en la consola"

#### Habilitar Servicios de Firebase:
1. En el menú izquierdo, clic en "Authentication"
2. Clic en "Comenzar"
3. Clic en "Correo electrónico/contraseña"
4. Activa el primer switch (Correo electrónico/contraseña)
5. Clic en "Guardar"

6. En el menú izquierdo, clic en "Realtime Database"
7. Clic en "Crear base de datos"
8. Selecciona tu ubicación (United States por defecto)
9. Selecciona "Comenzar en modo de prueba"
10. Clic en "Habilitar"

11. En el menú izquierdo, clic en "Storage"
12. Clic en "Comenzar"
13. Clic en "Siguiente" → "Listo"

### PASO 4: Configurar Google Maps API

#### Opción Rápida (Para Desarrollo):
1. Ve a: https://console.cloud.google.com/
2. Selecciona tu proyecto de Firebase (HelpMe)
3. En el menú izquierdo, busca "APIs y Servicios" → "Biblioteca"
4. Busca "Maps SDK for Android"
5. Clic en "HABILITAR"
6. Ve a "APIs y Servicios" → "Credenciales"
7. Clic en "+ CREAR CREDENCIALES" → "Clave de API"
8. COPIA la API Key que te da
9. Clic en "Restringir clave" (opcional, puedes hacerlo después)

#### Pegar la API Key:
1. Abre: C:\Users\eliza\AndroidStudioProjects\MyApplication\app\src\main\AndroidManifest.xml
2. Busca la línea 23 que dice: android:value="YOUR_GOOGLE_MAPS_API_KEY"
3. Reemplaza YOUR_GOOGLE_MAPS_API_KEY con tu API Key
4. Ejemplo: android:value="AIzaSyC1234567890abcdefghijklmnop"
5. Guarda el archivo (Ctrl + S)

### PASO 5: Preparar Dispositivo

#### Opción A: Usar Emulador (Recomendado para empezar)
1. En Android Studio, clic en "Device Manager" (icono de teléfono en la barra derecha)
2. Si no tienes emulador, clic en "+ Create Device"
3. Selecciona "Pixel 6" o cualquier dispositivo moderno
4. Clic en "Next"
5. Descarga una imagen del sistema (recomendado: "Tiramisu" - API 33)
6. Clic en "Next" → "Finish"
7. Inicia el emulador (botón ▶️)

IMPORTANTE para el emulador:
- Ve a Settings del emulador → Location
- Activa "Location" para que funcione el GPS

#### Opción B: Usar Dispositivo Físico (Mejor para GPS y cámara)
1. Activa "Opciones de desarrollo" en tu teléfono:
   - Ajustes → Acerca del teléfono
   - Toca 7 veces en "Número de compilación"
2. Ve a Opciones de desarrollo
3. Activa "Depuración USB"
4. Conecta tu teléfono con USB
5. Acepta la autorización en tu teléfono

### PASO 6: EJECUTAR LA APP ▶️

1. En Android Studio, verifica que no haya errores:
   - Build → Make Project
   - Espera a que compile
   
2. Asegúrate de tener un dispositivo seleccionado:
   - En la barra superior verás un dropdown con dispositivos
   - Selecciona tu emulador o dispositivo físico

3. Clic en el botón verde ▶️ "Run 'app'" (o presiona Shift + F10)

4. ESPERA a que compile e instale (1-3 minutos la primera vez)

5. ¡La app se abrirá automáticamente en tu dispositivo!

### PASO 7: Probar la Aplicación

#### Primera Vez - Registro:
1. Verás la pantalla de Login
2. Clic en "¿No tienes cuenta? Regístrate"
3. Ingresa un email: test@example.com
4. Ingresa una contraseña: 123456
5. Clic en "Registrarse"
6. ¡Listo! Estarás en el Home

#### Crear un Reporte de Prueba:
1. Clic en "Crear Reporte"
2. Selecciona tipo: "Seguridad"
3. Descripción: "Prueba de reporte"
4. Clic en "Obtener Ubicación Actual"
   - Acepta los permisos de ubicación
5. (Opcional) Clic en "Seleccionar Foto"
   - Acepta permisos de galería
   - Selecciona una foto
6. Clic en "Crear Reporte"
7. ¡Verás el mensaje de éxito!

#### Ver en el Mapa:
1. Vuelve al Home (flecha atrás)
2. Clic en "Ver Mapa"
3. ¡Verás un marcador en el mapa con tu reporte!

#### Ver Lista y Métricas:
1. Prueba "Lista de Reportes" para ver todos
2. Prueba "Métricas" para ver estadísticas

## 🆘 SI ALGO NO FUNCIONA

### Error: "Could not find google-services.json"
→ Verifica que el archivo esté en: app/google-services.json (no en la raíz)

### Error: "API Key is invalid"
→ Verifica que copiaste bien la API Key en AndroidManifest.xml
→ Asegúrate de habilitar Maps SDK for Android en Google Cloud Console

### Error: "Cannot resolve symbol Firebase..."
→ Sync Project with Gradle Files otra vez
→ Espera a que termine completamente

### Error: "App keeps crashing"
→ Abre Logcat en Android Studio (abajo) para ver el error
→ Probablemente sea por falta de google-services.json o API Key

### El mapa no se muestra
→ Verifica la API Key
→ Verifica que Maps SDK for Android esté habilitado
→ En emulador, activa Location en Settings

### No puedo obtener ubicación
→ En emulador: Settings → Location → Activar
→ O usa el panel de Extended Controls (... botón) → Location
→ En dispositivo físico: Activa GPS en configuración

## 📱 RESUMEN RÁPIDO

1. ✅ Abrir proyecto en Android Studio
2. ✅ Sync Project with Gradle Files
3. ✅ Configurar Firebase (crear proyecto + descargar google-services.json)
4. ✅ Habilitar Auth + Realtime DB + Storage en Firebase
5. ✅ Obtener Google Maps API Key
6. ✅ Pegar API Key en AndroidManifest.xml
7. ✅ Iniciar emulador o conectar dispositivo
8. ✅ Run 'app' (botón verde ▶️)
9. ✅ Registrarse en la app
10. ✅ ¡Crear tu primer reporte!

## ⏱️ Tiempo estimado: 20-30 minutos

La mayoría del tiempo es esperar que Gradle descargue dependencias y que Firebase se configure.


