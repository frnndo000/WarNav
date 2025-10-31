# WarNav

WarNav es un juego arcade 2D de disparos (shooter) desarrollado en Java con LibGDX para la asignatura de Programación Avanzada (ICI2241).

El objetivo es controlar una nave de guerra, rescatar soldados perdidos en una zona de conflicto y destruir enemigos para acumular el mayor puntaje posible.

## Pre-requisitos

Antes de comenzar, asegúrate de tener instalado el siguiente software:
* **Java JDK 11:** El proyecto está configurado para usar esta versión de Java.
* **Eclipse IDE:** Se recomienda "Eclipse IDE for Java Developers" o "Eclipse IDE for Enterprise Java and Web Developers".
* **Git:** Para clonar el repositorio.

## Instalación y Configuración en Eclipse

Sigue estos pasos para importar y ejecutar el proyecto correctamente. Este método (clonar manualmente y luego importar) es el más seguro.

### 1. Clonar el Repositorio (Manualmente)

Primero, clonaremos el proyecto desde la terminal, fuera de Eclipse, para tener control total.

1.  Abre una terminal (Git Bash, CMD, o PowerShell).
2.  Navega a la carpeta donde guardas tus proyectos (ej. `C:\Users\TuUsuario\eclipse-workspace`).
3.  Ejecuta el siguiente comando para clonar el repositorio:
    ```sh
    git clone https://github.com/frnndo000/WarNav.git
    ```
4.  Verifica que la carpeta `WarNav` se haya creado en tu explorador de archivos.

### 2. Importar como Proyecto Gradle

Ahora, con el proyecto ya en tu PC, le diremos a Eclipse cómo importarlo correctamente.

1.  Inicia Eclipse IDE.
2.  Ve al menú **File > Import...**.
3.  En el asistente de importación, expande la carpeta **Gradle** (¡NO la carpeta Git!) y selecciona **Existing Gradle Project**.
4.  Presiona **Next**.
5.  En el campo **Project root directory**, presiona **Browse...** y navega hasta la carpeta `WarNav` que acabas de clonar en el paso anterior.
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
