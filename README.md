# Kiosco Erick — proyecto Android

Este es el código fuente inicial de la aplicación Android.

## Funciones incluidas
- Escáner de códigos de barras usando la cámara (ZXing).
- Pitido del escáner y pitido al agregar un producto reconocido.
- Alta de producto al escanear un código desconocido.
- Guarda nombre, código, precio y stock localmente en el celular.
- Carrito, total y registro de ventas.
- Descuenta stock al confirmar una venta.

## Importante
Este ZIP contiene el **proyecto fuente**, no un APK compilado. Para obtener el APK hay que compilarlo con Android Studio (requiere Android SDK y conexión para descargar dependencias).

## Compilar
1. En una computadora, instalá Android Studio.
2. Abrí la carpeta `KioscoErickAndroid` en Android Studio.
3. Aceptá la sincronización de Gradle y descargá las dependencias.
4. Menú **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
5. Android Studio mostrará la ubicación del APK generado.

## Limitaciones de esta primera versión
- Los datos se guardan en el almacenamiento privado de esta instalación. Si desinstalás la app o borrás sus datos, podés perderlos; no hay copia en la nube todavía.
- Las ventas registran el contador y la última venta, no un libro completo de cada venta.
- No valida precios ni productos por internet: cada código se registra una vez manualmente y después se reconoce en ese celular.
- Antes de usar en un negocio real, probar el flujo completo y hacer copias de seguridad.

## Compilar desde el celular usando GitHub Actions (sin AndroidIDE)
1. En GitHub, creá un repositorio nuevo llamado `KioscoErick` (podés dejarlo privado).
2. Subí todos los archivos y carpetas de este proyecto al repositorio, incluida la carpeta oculta `.github/workflows`.
3. Esperá a que termine la ejecución del flujo **Compilar APK de Kiosco Erick** en la pestaña **Actions**.
4. Abrí la ejecución completada y descargá el artefacto `Kiosco-Erick-APK`. GitHub lo entrega como ZIP; adentro estará `app-debug.apk`.
5. Extraé ese ZIP en el celular y tocá `app-debug.apk` para instalarlo.

La compilación se realiza en GitHub, no en el teléfono. El APK es de depuración y no está firmado para publicar en Play Store.
