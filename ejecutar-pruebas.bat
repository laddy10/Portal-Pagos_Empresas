@echo off
REM Doble clic: pide clave/sesion de Google si faltan y ejecuta las pruebas del GeneralRunner.
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0ejecutar-pruebas.ps1"
pause
