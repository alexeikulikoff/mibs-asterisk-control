#!/bin/bash

sudo ./asterisk-control.sh stop
./gradlew grunt_cons build
sudo ./install.sh
sudo ./asterisk-control.sh start

