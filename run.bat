@echo off
:: TravelExplorer Java Application Runner
:: This script compiles and runs the Java version of TravelExplorer

echo ========================================
echo TravelExplorer Java Application
echo ========================================

:: Check if JavaFX path is set
if "%JAVAFX_HOME%"=="" (
    echo WARNING: JAVAFX_HOME environment variable not set
    echo Please set JAVAFX_HOME to your JavaFX SDK installation directory
    echo Example: set JAVAFX_HOME=C:\javafx-sdk-21.0.1
    echo.
    echo Using local JavaFX path: javafx-sdk-21.0.5\lib
    set JAVAFX_PATH=javafx-sdk-21.0.5\lib
) else (
    set JAVAFX_PATH=%JAVAFX_HOME%\lib
)

echo Using JavaFX path: %JAVAFX_PATH%

:: Check if JavaFX exists
if not exist "%JAVAFX_PATH%\javafx.controls.jar" (
    echo ERROR: JavaFX not found at %JAVAFX_PATH%
    echo Please:
    echo 1. Download JavaFX SDK from https://openjfx.io/
    echo 2. Extract it to a folder
    echo 3. Set JAVAFX_HOME environment variable to that folder
    echo 4. Or edit this script to point to the correct path
    pause
    exit /b 1
)

:: Create output directory
if not exist "out" mkdir out

echo.
echo Compiling TravelExplorer...
echo.

:: Compile all Java files
javac --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -d out backend\*.java frontend\*.java

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Compilation failed!
    echo Please check for errors above and ensure:
    echo 1. Java 17+ is installed
    echo 2. JavaFX SDK is properly installed
    echo 3. All source files are present
    pause
    exit /b 1
)

echo.
echo Compilation successful!
echo.
echo Starting TravelExplorer...
echo.

:: Copy resources if they exist
if exist "resources" (
    xcopy "resources" "out\resources" /E /I /Y >nul 2>&1
)

:: Run the application
java --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -cp out frontend.Main

echo.
echo TravelExplorer has been closed.
pause