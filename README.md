# Velisphere-Frontend
Frontend Module (Tigerspice), Worker (Chai), Utilities and Examples for the Velisphere IoT System

Written in Java, requires Google Web Toolkit (GWT) 2.7 to compile to Javascript. It does work but is buggy and incomplete. Use GWT compiler to create a .WAR file and deploy on Apache Tomcat.

In order to make it work, you need to run the backend server as well (see seperate repository) and configure the IP addresses in the config file accordingly. Send me an E-Mail if you need help.

Screenshots of the Web UI can be found on my old project website http://www.connectedthingslab.com/

Requires GWT, Vertica Community Edition and several other (open source) libraries

Licensed under GPLv2.

For 3rd party libraries, licensing terms and copyright of the respective owner apply (see readme files).

Structure is as follows:

GWT (Google Web Toolkit) based Frontend
---------------------------------------
TigerSpice_dev

Worker (processing the AMQP message queue)
------------------------------------------
VeliChai_dev 

REST Interface (endpoints that can't use AMQP can use REST via this servlet)
----------------------------------------------------------------------------
Toucan

Utilities
---------
Blender: A small servlet that provides a REST interface that Tigerspice, Chai and other components can call to receive the IP addresses of each other in order to communicate

MailService: A small servlet used to send e-mail alerts when triggered by the worker

MontanaManager: Small Java app to pre-fill the VoltDB and Vertica Databases with sample data

ToucanClient: Java app I used to test some features of the Toucan REST interface (essentially used to connect and do HTTP PUT/POST/GET on Toucan)

Examples
--------
BatteryAgent (read notebook battery level and send to Velisphere), Blubber (a simple chat client using Velisphere), BlubberMobile (same as blubber, but for android), VeliFS (simulates sending telemetry data and receiving remote commands via Velisphere using Microsoft Flight Simulator X / LockheedMartin Prepar3d and SimConnect) and DemoLinuxScreenshotEngine (sending a screenshot of a linux desktop every 5 seconds to velisphere) are examples, some of them are outdated. Better and up to date examples can be found in the VeliSphere Client SDK Repository.

Obsolete
--------
veli-gwt-virtualization is obsolete and needs to be removed



