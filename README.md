# OTel collector 

## Authors 

- [Kacper Ćwiertnia](https://github.com/kacpercwiertnia)
- [Mikołaj Pajor](https://github.com/Pejdzor)
- [Arkadiusz Mincberger](https://github.com/ArkadiuszMin)
- [Szymon Woźniak](https://github.com/szWozniak)

## Introduction

The OpenTelemetry (OTel) Collector is a crucial component of the OpenTelemetry observability framework. It acts as a central point for collecting, processing, and exporting telemetry data (traces, metrics, and logs) from applications and infrastructure. It's designed to be configurable, extensible, and vendor-agnostic, allowing for seamless integration with various observability backends. 

## Technology stack

- **Python** – drone flight simulator responsible for generating telemetry data.  
- **RabbitMQ** – message broker facilitating communication between the simulator and the backend.  
- **Java (Spring Boot)** – backend service that receives data from the simulator, stores it in the database, and exposes it via REST API.  
- **H2** – database which persists telemetry data received from the backend.  
- **React** – fronted that visualizes drone data retrieved from the backend.  
- **OpenTelemetry** – generates metrics from both the simulator and the backend.  
- **OTel Collector** – collects telemetry data (metrics) from applications via OTLP protocol.  
- **Grafana** – visualizes metrics collected by the OTel Collector.

## Architecture overview

The system consists of a drone flight simulator written in Python, which generates flight data and sends it to a RabbitMQ message broker. The Java Spring Boot backend receives this data from RabbitMQ and processes flight information.

In addition to data processing, the simulator sends system resource usage metrics (CPU and memory) to the OpenTelemetry Collector using the OTLP/gRPC protocol. The backend also exports its default OpenTelemetry metrics to the same Collector.

The OpenTelemetry Collector, running in a Docker container, collects all incoming telemetry and forwards it to Grafana for visualization.

![](./images/architecture.png)