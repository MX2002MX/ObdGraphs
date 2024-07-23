# `ObdGraphs` is an Android application intended to collect and display vehicle telemetry.

![CI](https://github.com/tzebrowski/ObdGraphs/actions/workflows/build.yml/badge.svg)

## About

`ObdGraphs` is an Android application intended to display car
telemetry  using `ELM327|STNxxx` compatible adapters.
It is build on top of the [ObdMetrics](https://github.com/tzebrowski/ObdMetrics "ObdMetrics") library and it collects and visualizes telemetry data 
generated by vehicle sensors. 
It has native support for the Alfa Romeo engines like `1.75 TBI` and `2.0 GME`.
Application offers integration with `Android Auto` and it is available in google play https://play.google.com/store/apps/details?id=org.obd.graphs.my.giulia

## Features

* Native support for the Alfa Romeo engines: `1.75 TBI`, `2.0 GME`, available in 4C, Giulietta, Giulia and Stelvio
* Collecting telemetry from different vehicle components within single session, e.g: `ECU`, `TCU`
* Native support for `Android Auto`  
* Supports `WIFI`, `Bluetooth`, `USB` `ELM327` and `STNxxx` (`STN1170`,`STN2120`) compatible adapters 
* Recordings -  The application saves all user trips on the storage. At any time user can open it and check telemetry collected in the past.
* Configurable views -  The application offers few different kind of gauges which can visualize telemetry in the real-time, e.g: Boost, MAF, OIL temp 
* Vehicle profiles - The application allows to create configurable vehicle profiles which can contains vehicle specific type settings.
* Up to 8 configurable virtual screens for `Gauge View`
* Up to 5 configurable virtual screens for `Graph View`
* Reading and cleaning DTC
* Reading vehicle metadata e.g: VIN


## Views

Application offers few configurable screens which visualize vehicle telemetry data.

### Android Auto (Trip Info Dashboard)
|                                                     |                                                     |
|-----------------------------------------------------|-----------------------------------------------------|
| ![Alt text](./res/aa_3.jpg?raw=true "Android Auto") | 



### Android Auto (Giulia Renderer)
|                                                     |                                                     |
|-----------------------------------------------------|-----------------------------------------------------|
| ![Alt text](./res/aa_3.jpg?raw=true "Android Auto") | ![Alt text](./res/aa_2.jpg?raw=true "Android Auto") | 


### Android Auto (Gauge Renderer)
|                                                     |                                                     |
|-----------------------------------------------------|-----------------------------------------------------|
| ![Alt text](./res/aa_4.jpg?raw=true "Android Auto") | ![Alt text](./res/aa_5.jpg?raw=true "Android Auto") | 




### Mobile

|                                                                   |                                            |
|-------------------------------------------------------------------|--------------------------------------------|
| ![Alt text](./res/Screenshot_phone_2.png?raw=true "") | ![Alt text](./res/Screenshot_phone_1.png?raw=true "")  | 
| ![Alt text](./res/Screenshot_8.png?raw=true "") | ![Alt text](./res/Screenshot_phone_4.png?raw=true "")  |

### Tablet
|                                                      |                                                      |
|------------------------------------------------------|------------------------------------------------------|
| ![Alt text](./res/Screenshot_3.png?raw=true "Gauge") | ![Alt text](./res/Screenshot_6.png?raw=true "Graph") | 


## Road map
* Performance optimization
* Support for PIDs creation through application

## Instructions

* [Adding new PIDs to query and displaying on AA virtual screen](doc/guides/query_new_pid/query_new_pid.md)
* [Change vehicle profile](doc/guides/change_vehicle_profile/change_vehicle_profile.md)