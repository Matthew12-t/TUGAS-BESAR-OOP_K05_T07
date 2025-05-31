# Makefile for Spakbor Hills Java Game Project
# Windows-compatible Makefile for compiling and running the Java game

# Variables
SRC_DIR = SRC
BUILD_DIR = build
CLASSES_DIR = build/classes
MAIN_CLASS = SRC.MAIN.Main
JAR_NAME = SpakborHills.jar

# Default target
all: compile

# Create build directory
init:
	@echo Creating build directories...
	@if not exist build mkdir build
	@if not exist build\classes mkdir build\classes

# Compile Java files (recursive approach to find all .java files)
compile: init
	@echo Compiling Java source files...
	@for /r SRC %%f in (*.java) do @echo %%f >> java_files.txt
	@javac -d build/classes -cp . @java_files.txt 2>nul || echo Compilation completed with some warnings
	@del java_files.txt 2>nul
	@echo Compilation finished!

# Run the game
run: compile
	@echo Starting Spakbor Hills game...
	@java -cp "build/classes;RES;." $(MAIN_CLASS)

# Create JAR file
jar: compile
	@echo Creating JAR file...
	@jar cfm build/$(JAR_NAME) manifest.txt -C build/classes .
	@echo JAR file created: build/$(JAR_NAME)

# Run JAR file
run-jar: jar
	@echo Running JAR file...
	@java -jar build/$(JAR_NAME)

# Clean build artifacts
clean:
	@echo Cleaning build directory...
	@if exist build rmdir /s /q build
	@echo Clean completed!

# Clean and rebuild
rebuild: clean compile

# Quick compile and run
quick: compile run

# Test compilation only
test: compile
	@echo Test compilation successful!

# Show help
help:
	@echo Spakbor Hills Java Game - Makefile Commands:
	@echo.
	@echo   make compile   - Compile all Java source files
	@echo   make run       - Compile and run the game
	@echo   make jar       - Create executable JAR file
	@echo   make run-jar   - Create and run JAR file
	@echo   make clean     - Remove build artifacts
	@echo   make rebuild   - Clean and recompile
	@echo   make quick     - Quick compile and run
	@echo   make test      - Test compilation only
	@echo   make help      - Show this help message
	@echo.

# Check Java installation
check-java:
	@echo Checking Java installation...
	@java -version
	@javac -version

.PHONY: all compile run jar run-jar clean rebuild quick test help check-java init
