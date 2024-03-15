#!/bin/zsh

# This script is used to set up the development environment for the native mobile app
# Reverse proxy for adb to port 8080 to allow for physical device testing
adb reverse tcp:8080 tcp:8080
