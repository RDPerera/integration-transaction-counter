#!/bin/bash

# Script to deploy the built jar to WSO2 MI
# This script copies the built jar to the MI lib directory and cleans the dropins directory

# Define paths
PROJECT_DIR="/Users/dilanperera/Projects/integration-transaction-counter"
JAR_FILE="$PROJECT_DIR/counter/target/org.wso2.micro.integrator.usage.data.collector-0.1.0.jar"
MI_HOME="/Users/dilanperera/Downloads/MicroIntegrators/wso2mi-4.5.0"
LIB_DIR="$MI_HOME/lib"
DROPINS_DIR="$MI_HOME/dropins"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "====================================="
echo "WSO2 MI Deployment Script"
echo "====================================="

# Build the project
echo "Building project with Maven..."
cd "$PROJECT_DIR"
mvn clean install

if [ $? -ne 0 ]; then
    echo -e "${RED}Error: Maven build failed${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Maven build completed successfully${NC}"

# Check if jar file exists
if [ ! -f "$JAR_FILE" ]; then
    echo -e "${RED}Error: JAR file not found at $JAR_FILE${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Found JAR file: $JAR_FILE${NC}"

# Check if MI directories exist
if [ ! -d "$LIB_DIR" ]; then
    echo -e "${RED}Error: MI lib directory not found at $LIB_DIR${NC}"
    exit 1
fi

if [ ! -d "$DROPINS_DIR" ]; then
    echo -e "${RED}Error: MI dropins directory not found at $DROPINS_DIR${NC}"
    exit 1
fi

# Copy jar to lib directory
echo "Copying JAR to $LIB_DIR..."
cp "$JAR_FILE" "$LIB_DIR/"

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ JAR file copied successfully${NC}"
else
    echo -e "${RED}Error: Failed to copy JAR file${NC}"
    exit 1
fi

# Remove contents of dropins directory
echo "Cleaning dropins directory..."
if [ "$(ls -A $DROPINS_DIR)" ]; then
    rm -rf "$DROPINS_DIR"/*
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Dropins directory cleaned successfully${NC}"
    else
        echo -e "${RED}Error: Failed to clean dropins directory${NC}"
        exit 1
    fi
else
    echo -e "${YELLOW}Dropins directory is already empty${NC}"
fi

echo "====================================="
echo -e "${GREEN}Deployment completed successfully!${NC}"
echo "====================================="
