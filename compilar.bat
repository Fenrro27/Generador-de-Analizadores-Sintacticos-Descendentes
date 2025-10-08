@echo off
REM ============================================================
REM  🚀 Compilador del proyecto Tinto
REM  Compila el código Java y genera TintoCompiler.jar
REM ============================================================

echo - Limpiando directorio bin...
if exist bin rmdir /s /q bin
mkdir bin

echo - Compilando fuentes Java...
REM Recorre todos los .java del src y los compila dentro de bin
del /q sources.txt >nul 2>&1
for /R src %%f in (*.java) do echo %%f >> sources.txt
javac -d bin @sources.txt
if errorlevel 1 (
    echo Error durante la compilacion.
    del sources.txt
    exit /b 1
)
del sources.txt

echo - Empaquetando JAR...
jar cfm TintoCompiler.jar MANIFEST.MF -C bin .
if errorlevel 1 (
    echo Error al crear el JAR.
    exit /b 1
)

echo - Limpiando binario temporal...
rmdir /s /q bin

echo Compilacion completada: TintoCompiler.jar generado con exito.
pause
