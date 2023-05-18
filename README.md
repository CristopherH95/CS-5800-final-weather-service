# CS-5800 Final: Weather Service

This repository contains an implementation of a simulated weather forecast application,
for the final in CS-5800 (Advanced Software Engineering). This application uses fake API clients, which produce
random data in a format based on three real weather forecast APIs (weather-api, open-meteo, and weather-gov). The
strategy design pattern is utilized to implement the different processes required for receiving data from each of
these APIs. Additionally, the observer design pattern is leveraged to enable a client class to subscribe to the 
weather forecasting services and automatically receive the forecast data. A UML diagram illustrating the high level
design of how the classes interact can be found in `diagram.png` (with the original PlantUML source in `system-uml.txt`).
Examples of output from the driver program, illustrating how these classes behave, can be found in `output-1.png`,
`output-2.png`, and `output-3.png`.

## Build & Run

To build with Maven, simply navigate to the project root in the command line and run:

```shell
mvn package
```

Alternatively, IDEs such as IntelliJ should be able to build the source files using their standard build utilities.

Once built, the project can be run using the `driver` package:

```shell
java -cp ./target/final-weather-service-1.0-SNAPSHOT.jar driver.Main
```