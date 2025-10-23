@echo off
echo === MongoDB Shell Installation Script ===
echo.
echo This script will download and install MongoDB Shell (mongosh)
echo Required for direct database connection.
echo.

set MONGOSH_VERSION=2.1.0
set MONGOSH_URL=https://downloads.mongodb.com/compass/mongosh-%MONGOSH_VERSION%-win32-x64.zip
set MONGOSH_ZIP=mongosh.zip
set MONGOSH_DIR=mongosh

echo Downloading MongoDB Shell...
powershell -Command "Invoke-WebRequest -Uri '%MONGOSH_URL%' -OutFile '%MONGOSH_ZIP%'"

if not exist "%MONGOSH_ZIP%" (
    echo ERROR: Failed to download MongoDB Shell
    pause
    exit /b 1
)

echo Extracting MongoDB Shell...
powershell -Command "Expand-Archive -Path '%MONGOSH_ZIP%' -DestinationPath '.' -Force"

if exist "mongosh-%MONGOSH_VERSION%-win32-x64" (
    ren "mongosh-%MONGOSH_VERSION%-win32-x64" "%MONGOSH_DIR%"
)

if exist "%MONGOSH_DIR%" (
    echo.
    echo SUCCESS: MongoDB Shell installed successfully!
    echo Location: %CD%\%MONGOSH_DIR%\bin\mongosh.exe
    echo.
    echo Testing connection to MongoDB...
    %MONGOSH_DIR%\bin\mongosh.exe --eval "db.runCommand('ping')" --quiet
    
    if %ERRORLEVEL% == 0 (
        echo MongoDB connection successful!
    ) else (
        echo MongoDB connection failed. Make sure MongoDB is running.
    )
) else (
    echo ERROR: Failed to extract MongoDB Shell
)

echo.
echo Cleaning up...
del "%MONGOSH_ZIP%" 2>nul

echo.
pause