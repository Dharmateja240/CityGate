@echo off
echo === MongoDB Data Import Script ===
echo.
echo This script will import user registration data to MongoDB
echo Database: info
echo Collection: user_registrations
echo.

if not exist "mongodb_backup\user_registrations.json" (
    echo Error: No registration data found!
    echo Please run the application and register some users first.
    pause
    exit /b 1
)

echo Importing data to MongoDB...
mongoimport --db info --collection user_registrations --file mongodb_backup\user_registrations.json --jsonArray

if %ERRORLEVEL% == 0 (
    echo.
    echo SUCCESS: User registration data imported successfully!
    echo.
    echo You can verify the data using MongoDB shell:
    echo   mongo
    echo   use info
    echo   db.user_registrations.find().pretty()
) else (
    echo.
    echo ERROR: Failed to import data to MongoDB
    echo Make sure MongoDB is running and mongoimport is available in PATH
)

echo.
pause