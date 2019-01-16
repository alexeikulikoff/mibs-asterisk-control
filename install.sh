#!/bin/bash

echo "Install Asterisk Control... "

JAR="mibs-asterisk-control.jar"
YML="application.yml"

SIP_CONTROL="sip-asterisk-control.conf"
KNOWN_HOST="known_hosts"

SRC_CONFIG=$PWD"/src/main/resources/"
DST_CONFIG="/usr/local/etc/mibs-asterisk-control/"


BUILD_DIR=$PWD"/build/libs/"

INSTALL_DIR="/usr/local/bin/"


SRC_CONFIG_YML=$SRC_CONFIG$YML
DST_CONFIG_YML=$DST_CONFIG$YML

DST_JAR=$INSTALL_DIR$JAR
SRC_JAR=$BUILD_DIR$JAR

ZER=10

copy_file(){ 

echo "Copy  $1 -> $2"	
cp $1 $2

if [ -e $2 ]
then
	echo "Copy sucessfull..."
else
	echo "Copy fail"
	exit 1
fi
}

if [ ! -e $DST_CONFIG  ]
then
	echo "Create directory "$DST_CONFIG
	mkdir $DST_CONFIG
fi
		

copy_file "$SRC_CONFIG_YML" "$DST_CONFIG_YML"
copy_file "$SRC_JAR" "$DST_JAR"
copy_file "$PWD/$SIP_CONTROL" "$DST_CONFIG$SIP_CONTROL"
copy_file "$PWD/$KNOWN_HOST" "$DST_CONFIG$KNOWN_HOST"



