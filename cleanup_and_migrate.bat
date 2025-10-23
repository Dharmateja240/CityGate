@echo off
echo === Data Cleanup and Migration ===
echo.

cd /d "%~dp0"

echo Compiling cleanup classes...
javac -cp . backend\CleanupAndMigrate.java backend\DirectMongoDBConnector.java backend\User.java

if %ERRORLEVEL% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Running cleanup and migration...
echo.

java -cp . backend.CleanupAndMigrate

echo.
echo Cleanup and migration complete!
pause