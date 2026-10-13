# Kiosco Erick Android

Proyecto Android nativo para gestión básica de kiosco con lectura de códigos de barras, productos, carrito de venta y stock local.

## Compilar desde GitHub Actions

El flujo está en `.github/workflows/android.yml`. Subí los archivos y carpetas de este proyecto a la raíz de un repositorio GitHub cuya rama sea `main`. En la pestaña **Actions**, ejecutá **Compilar APK Kiosco Erick**. Si finaliza correctamente, descargá el artefacto `Kiosco-Erick-APK`.

El flujo usa las herramientas Android preinstaladas en el runner de GitHub y no fuerza una descarga de una versión inexistente de Command-line Tools.

## Datos

Los productos y ventas se guardan localmente en el dispositivo mediante SharedPreferences; no se sincronizan automáticamente entre teléfonos. Es una app inicial, no un sistema contable/fiscal.
