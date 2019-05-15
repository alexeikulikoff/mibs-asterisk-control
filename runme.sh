#!/bin/bash

sudo ./asterisk-control.sh stop
./gradlew grunt_customers build
sudo ./install.sh
sudo ./asterisk-control.sh start

