@echo off
REM Build script for Spakbor Hills Java Game Project
REM This batch file replaces Gradle for building and running the game

setlocal enabledelayedexpansion

REM Variables
set SRC_DIR=SRC
set BUILD_DIR=build
set CLASSES_DIR=build\classes
set MAIN_CLASS=SRC.MAIN.Main
set JAR_NAME=SpakborHills.jar

REM Check command line arguments
if "%1"=="" (
    echo No command specified. Use: build.bat [compile^|run^|jar^|clean^|help]
    echo Try 'build.bat help' for more options.
    goto :EOF
)

REM Handle different commands
if "%1"=="help" goto :help
if "%1"=="clean" goto :clean
if "%1"=="compile" goto :compile
if "%1"=="run" goto :run
if "%1"=="jar" goto :jar
if "%1"=="run-jar" goto :run-jar
if "%1"=="rebuild" goto :rebuild
if "%1"=="quick" goto :quick
if "%1"=="test" goto :test
if "%1"=="check-java" goto :check-java

echo Unknown command: %1
echo Use 'build.bat help' for available commands.
goto :EOF

:help
echo Spakbor Hills Java Game - Build Script Commands:
echo.
echo   build.bat compile   - Compile all Java source files
echo   build.bat run       - Compile and run the game
echo   build.bat jar       - Create executable JAR file
echo   build.bat run-jar   - Create and run JAR file
echo   build.bat clean     - Remove build artifacts
echo   build.bat rebuild   - Clean and recompile
echo   build.bat quick     - Quick compile and run
echo   build.bat test      - Test compilation only
echo   build.bat check-java - Check Java installation
echo   build.bat help      - Show this help message
echo.
echo Requirements:
echo   - Java Development Kit (JDK) 11 or higher
echo   - PATH environment variable must include javac and java
echo.
goto :EOF

:check-java
echo Checking Java installation...
java -version
javac -version
if errorlevel 1 (
    echo ERROR: Java is not properly installed or not in PATH
    exit /b 1
)
echo Java installation check completed!
goto :EOF

:init
echo Creating build directories...
if not exist "%BUILD_DIR%" mkdir "%BUILD_DIR%"
if not exist "%CLASSES_DIR%" mkdir "%CLASSES_DIR%"
goto :EOF

:clean
echo Cleaning build directory...
if exist "%BUILD_DIR%" rmdir /s /q "%BUILD_DIR%"
echo Clean completed!
goto :EOF

:compile
call :init
echo Compiling Java source files...
echo Compiling all packages...

REM Compile all Java files in one go, suppressing some warnings
javac -d %CLASSES_DIR% -cp . ^
    %SRC_DIR%\MAIN\*.java ^
    %SRC_DIR%\ENTITY\*.java ^
    %SRC_DIR%\TILES\*.java ^
    %SRC_DIR%\MAP\*.java ^
    %SRC_DIR%\MAP\NPC_HOUSE\*.java ^
    %SRC_DIR%\UI\*.java ^
    %SRC_DIR%\ITEMS\*.java ^
    %SRC_DIR%\OBJECT\*.java ^
    %SRC_DIR%\TIME\*.java ^
    %SRC_DIR%\SEASON\*.java ^
    %SRC_DIR%\WEATHER\*.java ^
    %SRC_DIR%\INVENTORY\*.java ^
    %SRC_DIR%\SHIPPINGBIN\*.java ^
    %SRC_DIR%\STORE\*.java ^
    %SRC_DIR%\COOKING\*.java ^
    %SRC_DIR%\CHEAT\*.java ^
    %SRC_DIR%\ENDGAME\*.java ^
    %SRC_DIR%\DATA\*.java ^
    %SRC_DIR%\UTIL\*.java ^
    %SRC_DIR%\MAIN\MENU\*.java 2>nul

if errorlevel 1 (
    echo ERROR: Compilation failed!
    exit /b 1
)
echo Compilation completed successfully!
goto :EOF

:run
call :compile
if errorlevel 1 goto :EOF
echo Starting Spakbor Hills game...
java -cp "build/classes;RES;." %MAIN_CLASS%
goto :EOF

:jar
call :compile
if errorlevel 1 goto :EOF
echo Creating JAR file...
jar cfm %BUILD_DIR%\%JAR_NAME% manifest.txt -C %CLASSES_DIR% .
if errorlevel 1 (
    echo ERROR: JAR creation failed!
    exit /b 1
)
echo JAR file created: %BUILD_DIR%\%JAR_NAME%
goto :EOF

:run-jar
call :jar
if errorlevel 1 goto :EOF
echo Running JAR file...
java -jar %BUILD_DIR%\%JAR_NAME%
goto :EOF

:rebuild
call :clean
call :compile
goto :EOF

:quick
call :compile
if errorlevel 1 goto :EOF
call :run
goto :EOF

:test
call :compile
if errorlevel 1 goto :EOF
echo Test compilation successful!
goto :EOF

:EOF
endlocal
