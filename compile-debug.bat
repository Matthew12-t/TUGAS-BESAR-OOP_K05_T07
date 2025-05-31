@echo off
REM Simple build script for debugging compilation issues
echo Starting compilation test...
echo.

REM Create build directory
if not exist build mkdir build
if not exist build\classes mkdir build\classes

echo Compiling individual packages to identify issues...
echo.

echo [1/4] Compiling UTIL package...
javac -d build\classes -cp . SRC\UTIL\*.java
if errorlevel 1 (
    echo ERROR in UTIL package
    pause
    exit /b 1
)

echo [2/4] Compiling TIME, SEASON, WEATHER packages...
javac -d build\classes -cp build\classes;. SRC\TIME\*.java SRC\SEASON\*.java SRC\WEATHER\*.java
if errorlevel 1 (
    echo ERROR in TIME/SEASON/WEATHER packages
    pause
    exit /b 1
)

echo [3/4] Compiling TILES, ITEMS, OBJECT packages...
javac -d build\classes -cp build\classes;. SRC\TILES\*.java SRC\ITEMS\*.java SRC\OBJECT\*.java
if errorlevel 1 (
    echo ERROR in TILES/ITEMS/OBJECT packages
    pause
    exit /b 1
)

echo [4/4] Compiling remaining packages...
javac -d build\classes -cp build\classes;. SRC\INVENTORY\*.java SRC\DATA\*.java SRC\CHEAT\*.java SRC\ENDGAME\*.java SRC\SHIPPINGBIN\*.java SRC\STORE\*.java SRC\COOKING\*.java
if errorlevel 1 (
    echo ERROR in secondary packages
    pause
    exit /b 1
)

echo [5/6] Compiling UI packages...
javac -d build\classes -cp build\classes;. SRC\UI\*.java
if errorlevel 1 (
    echo ERROR in UI package
    pause
    exit /b 1
)

echo [6/8] Compiling ENTITY package...
javac -d build\classes -cp build\classes;. SRC\ENTITY\*.java
if errorlevel 1 (
    echo ERROR in ENTITY package
    pause
    exit /b 1
)

echo [7/8] Compiling MAP packages...
javac -d build\classes -cp build\classes;. SRC\MAP\*.java
if errorlevel 1 (
    echo ERROR in MAP package
    pause
    exit /b 1
)

echo [8/8] Compiling MAIN packages...
javac -d build\classes -cp build\classes;. SRC\MAIN\MENU\*.java
if errorlevel 1 (
    echo ERROR in MAIN\MENU package
    pause
    exit /b 1
)

javac -d build\classes -cp build\classes;. SRC\MAIN\*.java
if errorlevel 1 (
    echo ERROR in MAIN package
    pause
    exit /b 1
)

echo [9/9] Compiling NPC_HOUSE maps...
javac -d build\classes -cp build\classes;. SRC\MAP\NPC_HOUSE\*.java
if errorlevel 1 (
    echo ERROR in NPC_HOUSE package
    pause
    exit /b 1
)

echo.
echo Compilation completed successfully!
echo.
echo You can now run the game with:
echo java -cp build\classes SRC.MAIN.Main
echo.
pause
