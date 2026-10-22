
@echo off
title RESOLVR - Vehicle Rental Management
cd /d "%~dp0"

echo Starting RESOLVR...
java -cp "out;lib/*" com.resolvr.Main

if errorlevel 1 (
    echo.
    echo RESOLVR encountered an error.
    pause
)
