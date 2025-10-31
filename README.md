# WarNav

WarNav es un juego arcade 2D de disparos (shooter) desarrollado en Java con LibGDX para la asignatura de Programación Avanzada (ICI2241).

El objetivo es controlar una nave de guerra, rescatar soldados perdidos en una zona de conflicto y destruir enemigos para acumular el mayor puntaje posible.

## Pre-requisitos

Antes de comenzar, asegúrate de tener instalado el siguiente software:
* **Java JDK 11:** El proyecto está configurado para usar esta versión de Java.
* **Eclipse IDE:** Se recomienda "Eclipse IDE for Java Developers" o "Eclipse IDE for Enterprise Java and Web Developers".
* **Git:** Para clonar el repositorio.

## Instalación y Configuración en Eclipse

Sigue estos pasos para importar y ejecutar el proyecto correctamente.

### 1. Clonar el Repositorio

Abre una terminal o Git Bash en la carpeta donde quieras descargar el proyecto y ejecuta el siguiente comando:
```sh
git clone [https://github.com/frnndo000/WarNav.git](https://github.com/frnndo000/WarNav.git)
```
### 2. Importar el Proyecto en Eclipse

Este es un proyecto Gradle, por lo que debe ser importado de una manera específica:

1.  Inicia Eclipse IDE.
2.  Ve al menú **File > Import...**.
3.  En el asistente de importación, expande la carpeta **Gradle** y selecciona **Existing Gradle Project**.
4.  Presiona **Next**.
5.  En **Project root directory**, presiona **Browse...** y navega hasta la carpeta `WarNav` que acabas de clonar.
6.  Haz clic en **Finish**.

### 3. Esperar la Construcción de Gradle

La primera vez que importes, Eclipse (usando Gradle) necesitará descargar todas las dependencias del proyecto (LibGDX, etc.). Este proceso puede tardar unos minutos. Podrás ver el progreso en la consola o en la pestaña "Gradle Tasks" en la esquina inferior derecha.

---

## Cómo Ejecutar el Juego

Una vez que el proyecto se haya importado y Gradle haya terminado de construir (sin errores), sigue estos pasos para crear la configuración de ejecución:

1.  Ve al menú **Run > Run Configurations...**.
2.  Haz clic derecho en **Java Application** en el panel izquierdo y selecciona **New Launch Configuration**.
3.  En el campo **Name**, escribe un nombre (ej: `runwarnav`).
4.  En la pestaña **Main**, en el campo **Project**, presiona **Browse...** y selecciona `WarNav-lwjgl3`.
5.  En el campo **Main class**, escribe `com.mygdx.warnav.lwjgl3.Lwjgl3Launcher`.
6.  Haz clic en **Apply** y luego en **Run**.

¡La ventana del juego debería aparecer y estar lista para jugar!

## Controles del Juego

| Acción | Tecla / Botón |
| :--- | :--- |
| Mover nave | Teclas `WASD` |
| Disparar misiles | `J` |
| Recargar Munición | `K` |
| Pausar juego | `ESC` |

## Autores (Grupo 36)

* Fernando López
* Pablo Saldivia
* Etian Vargas
