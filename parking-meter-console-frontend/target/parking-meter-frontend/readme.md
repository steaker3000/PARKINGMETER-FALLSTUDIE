ParkingMeter Iteration 1
------------------------

This is a possible solution for the first parking meter iteration. 

The user stories 1, 2 and 4 are implemented against a ParkingMeterDomainService Stub. The
Stub simulates the persitence layer as well as the complete domain model. 


Plugin System:
-------------

The plugin system allows to put "parking-meter plugins" to the plugin folder. A sample
swing plugin project shows how to control the parking spot selection. The swing plugin
must be build separately. 

Build:
-----

You can build the project maven clean install. The multi-maven project will
produce a target folder in the parking-meter-console-frontend folder. In the target
directory, you can find a folder the parking-meter-frontend. In this folder is the 
final parking meter build.

Run:
---
Just start the parking meter with start.bat or start.sh


