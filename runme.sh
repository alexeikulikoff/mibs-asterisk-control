#!/bin/bash

sudo ./asterisk-control.sh stop
#sudo ./gradlew grunt_outbound build
sudo ./gradlew build
sudo ./install.sh
sudo ./asterisk-control.sh start

