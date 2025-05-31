# PowerShell build script for Spakbor Hills Java Game Project
# This script replaces Gradle for building and running the game

param(
    [string]$Command = ""
)

# Variables
$SRC_DIR = "SRC"
$BUILD_DIR = "build"
$CLASSES_DIR = "build\classes"
$MAIN_CLASS = "SRC.MAIN.Main"
$JAR_NAME = "SpakborHills.jar"

function Show-Help {
    Write-Host "Spakbor Hills Java Game - PowerShell Build Script" -ForegroundColor Green
    Write-Host ""
    Write-Host "Usage: .\build.ps1 [command]" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Commands:" -ForegroundColor Cyan
    Write-Host "  compile   - Compile all Java source files"
    Write-Host "  run       - Compile and run the game"
    Write-Host "  jar       - Create executable JAR file"
    Write-Host "  run-jar   - Create and run JAR file"
    Write-Host "  clean     - Remove build artifacts"
    Write-Host "  rebuild   - Clean and recompile"
    Write-Host "  quick     - Quick compile and run"
    Write-Host "  test      - Test compilation only"
    Write-Host "  check-java - Check Java installation"
    Write-Host "  help      - Show this help message"
    Write-Host ""
    Write-Host "Requirements:" -ForegroundColor Magenta
    Write-Host "  - Java Development Kit (JDK) 11 or higher"
    Write-Host "  - PATH environment variable must include javac and java"
    Write-Host ""
}

function Test-JavaInstallation {
    Write-Host "Checking Java installation..." -ForegroundColor Cyan
    try {
        $javaVersion = java -version 2>&1
        $javacVersion = javac -version 2>&1
        Write-Host "Java installation found:" -ForegroundColor Green
        Write-Host $javaVersion
        Write-Host $javacVersion
        return $true
    }
    catch {
        Write-Host "ERROR: Java is not properly installed or not in PATH" -ForegroundColor Red
        return $false
    }
}

function Initialize-BuildDirectory {
    Write-Host "Creating build directories..." -ForegroundColor Cyan
    if (!(Test-Path $BUILD_DIR)) {
        New-Item -ItemType Directory -Path $BUILD_DIR | Out-Null
    }
    if (!(Test-Path $CLASSES_DIR)) {
        New-Item -ItemType Directory -Path $CLASSES_DIR | Out-Null
    }
}

function Clean-Build {
    Write-Host "Cleaning build directory..." -ForegroundColor Cyan
    if (Test-Path $BUILD_DIR) {
        Remove-Item -Path $BUILD_DIR -Recurse -Force
    }
    Write-Host "Clean completed!" -ForegroundColor Green
}

function Compile-Sources {
    Initialize-BuildDirectory
    Write-Host "Compiling Java source files..." -ForegroundColor Cyan
    
    # Get all Java files
    $javaFiles = @(
        "$SRC_DIR\MAIN\*.java",
        "$SRC_DIR\ENTITY\*.java",
        "$SRC_DIR\TILES\*.java",
        "$SRC_DIR\MAP\*.java",
        "$SRC_DIR\MAP\NPC_HOUSE\*.java",
        "$SRC_DIR\UI\*.java",
        "$SRC_DIR\ITEMS\*.java",
        "$SRC_DIR\OBJECT\*.java",
        "$SRC_DIR\TIME\*.java",
        "$SRC_DIR\SEASON\*.java",
        "$SRC_DIR\WEATHER\*.java",
        "$SRC_DIR\INVENTORY\*.java",
        "$SRC_DIR\SHIPPINGBIN\*.java",
        "$SRC_DIR\STORE\*.java",
        "$SRC_DIR\COOKING\*.java",
        "$SRC_DIR\CHEAT\*.java",
        "$SRC_DIR\ENDGAME\*.java",
        "$SRC_DIR\DATA\*.java",
        "$SRC_DIR\UTIL\*.java",
        "$SRC_DIR\MAIN\MENU\*.java"
    )
    
    # Compile all Java files
    $compileCommand = "javac -d $CLASSES_DIR -cp . " + ($javaFiles -join " ")
    
    try {
        Invoke-Expression $compileCommand
        Write-Host "Compilation completed successfully!" -ForegroundColor Green
        return $true
    }
    catch {
        Write-Host "ERROR: Compilation failed!" -ForegroundColor Red
        Write-Host $_.Exception.Message -ForegroundColor Red
        return $false
    }
}

function Run-Game {
    if (!(Compile-Sources)) {
        return
    }
    Write-Host "Starting Spakbor Hills game..." -ForegroundColor Cyan
    java -cp "build/classes;RES;." $MAIN_CLASS
}

function Create-Jar {
    if (!(Compile-Sources)) {
        return
    }
    Write-Host "Creating JAR file..." -ForegroundColor Cyan
    try {
        jar cfm "$BUILD_DIR\$JAR_NAME" manifest.txt -C $CLASSES_DIR .
        Write-Host "JAR file created: $BUILD_DIR\$JAR_NAME" -ForegroundColor Green
        return $true
    }
    catch {
        Write-Host "ERROR: JAR creation failed!" -ForegroundColor Red
        return $false
    }
}

function Run-Jar {
    if (!(Create-Jar)) {
        return
    }
    Write-Host "Running JAR file..." -ForegroundColor Cyan
    java -jar "$BUILD_DIR\$JAR_NAME"
}

# Main script logic
switch ($Command.ToLower()) {
    "" {
        Write-Host "No command specified." -ForegroundColor Yellow
        Show-Help
    }
    "help" {
        Show-Help
    }
    "check-java" {
        Test-JavaInstallation
    }
    "clean" {
        Clean-Build
    }
    "compile" {
        Compile-Sources
    }
    "run" {
        Run-Game
    }
    "jar" {
        Create-Jar
    }
    "run-jar" {
        Run-Jar
    }
    "rebuild" {
        Clean-Build
        Compile-Sources
    }
    "quick" {
        Run-Game
    }
    "test" {
        if (Compile-Sources) {
            Write-Host "Test compilation successful!" -ForegroundColor Green
        }
    }
    default {
        Write-Host "Unknown command: $Command" -ForegroundColor Red
        Show-Help
    }
}
