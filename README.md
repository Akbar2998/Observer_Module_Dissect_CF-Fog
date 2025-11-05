# Observer Module for DISSECT-CF-Fog

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

**A dynamic task management system for IoT-Fog-Cloud simulation environments**

[Features](#features) • [Architecture](#architecture) • [Installation](#installation) • [Usage](#usage) • [Performance](#performance)

</div>

---

## Overview

The **Observer Module** is an advanced execution management system developed as part of the [DISSECT-CF-Fog](https://github.com/sed-inf-u-szeged/DISSECT-CF-Fog) framework. This module enhances simulation job execution through dynamic scheduling, parallel processing, and real-time monitoring capabilities.

### Master's Thesis Project

This work was completed as a Master's thesis at the **University of Szeged, Institute of Informatics** (2024) under the supervision of Dr. Andras Markus.

**Thesis Title:** *Development of observer module supporting IoT-Fog-Cloud simulations in DISSECT-CF-Fog*

---

## Key Features

### 🚀 Performance Enhancements
- **30% reduction** in job processing times
- **Dynamic scheduling** based on real-time system data
- **Parallel execution** using Java ExecutorService thread pools
- Improved resource utilization and system throughput

### 🎯 Core Capabilities
- **Real-time Monitoring** - Continuous observation of simulation job lifecycle
- **Adaptive Scheduling** - Dynamic task allocation without manual intervention
- **Scalable Architecture** - Handles varying workloads automatically
- **Process Isolation** - Secure and independent job execution
- **Timeout Management** - Automatic termination of long-running jobs

### 🔧 Technical Features
- Configurable thread pool sizes
- Multiple scaling strategies
- Batch job processing
- Asynchronous file uploads
- Automatic Object ID matching
- Comprehensive logging system

---

## Architecture

### System Components

```
┌─────────────────────────────────────────────────────────────┐
│                      Observer Module                         │
│  ┌────────────────────┐      ┌─────────────────────────┐   │
│  │ ScheduledExecutor  │──────│  JobObserverService     │   │
│  │ (Job Checker)      │      │  (Core Logic)           │   │
│  └────────────────────┘      └─────────────────────────┘   │
│           │                              │                   │
│           ▼                              ▼                   │
│  ┌────────────────────┐      ┌─────────────────────────┐   │
│  │ MongoDB Database   │◄─────│  ExecutorService        │   │
│  │ (Job Queue)        │      │  (Thread Pool)          │   │
│  └────────────────────┘      └─────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                               │
                               ▼
                  ┌────────────────────────────┐
                  │  DISSECT-CF-Fog Executor   │
                  │  (Simulation Engine)       │
                  └────────────────────────────┘
```

### Workflow

1. **Job Detection** - Periodic database scanning for submitted jobs
2. **Job Submission** - Dynamic allocation to thread pool
3. **Parallel Execution** - Concurrent job processing
4. **Status Management** - Real-time job status updates
5. **Result Storage** - Persistence of simulation results

---

## Technology Stack

| Component | Technology | Purpose |
|-----------|-----------|---------|
| **Backend Framework** | Spring Boot | Robust and scalable application foundation |
| **Database** | MongoDB | High-performance NoSQL data storage |
| **Concurrency** | Java ExecutorService | Multi-threading and task scheduling |
| **Build Tool** | Maven | Dependency management and build automation |
| **Language** | Java | Core programming language |

---

## Installation

### Prerequisites

- **Java JDK** 11 or higher
- **Maven** 3.6+
- **MongoDB** 4.0+
- **Node.js** 14+ (for utility scripts)

### Setup Steps

1. **Clone the repository**
```bash
git clone https://github.com/Akbar2998/Observer_Module_Dissect_CF_Fog.git
cd Observer_Module_Dissect_CF_Fog
```

2. **Install MongoDB** (if not already installed)
```bash
# Using Docker
docker run -d -p 27017:27017 --name mongodb mongo:latest

# Or install locally based on your OS
```

3. **Configure database connection**

Edit `src/main/resources/application.properties`:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/dissect
job.observer.check.interval=1
job.executor.thread.pool.size=5
job.scheduler.pool.size=1
```

4. **Initialize database**
```bash
cd path/to/web-application/utils
node mongo-setup.js
```

5. **Build the Executor module** (dependency)
```bash
cd path/to/executor-module
mvn clean install
```

6. **Build the Observer module**
```bash
cd path/to/observer-module
mvn clean install
```

---

## Usage

### Running the Observer Module

1. **Start MongoDB** (if not running)
```bash
docker start mongodb
# or
mongod
```

2. **Launch the Observer**
```bash
cd observer-module
java -jar target/observer-module-1.0.0-SNAPSHOT.jar
```

### Configuration Options

The Observer module can be configured via `application.properties`:

| Property | Description | Default |
|----------|-------------|---------|
| `job.observer.check.interval` | Database polling interval (seconds) | 1 |
| `job.executor.thread.pool.size` | Number of concurrent execution threads | 5 |
| `job.scheduler.pool.size` | Scheduler thread pool size | 1 |

### Uploading Jobs

Use the job uploader utility to submit simulation jobs:

```bash
cd web-application/utils
node job-uploader.js
```

This script supports:
- Batch job uploads (default: 30 jobs)
- Asynchronous file processing
- Automatic configuration file linking

### Monitoring Jobs

Monitor job execution using the provided monitoring script:

```bash
cd web-application/utils
node monitor-mongo.js
```

Outputs:
- `jobStatus.xls` - Excel file with job status timeline
- `jobStatus.log` - Detailed execution logs

---

## Performance

### Benchmark Results

The Observer module demonstrates significant performance improvements over traditional scheduled execution:

| Configuration | Growth Rate (jobs/sec) | Performance Gain |
|--------------|------------------------|------------------|
| **Observer (1,5,1)** | 0.366 | **+144%** vs Scheduled 1s |
| Observer (10,5,1) | 0.097 | Baseline |
| Observer (10,10,3) | 0.095 | Baseline |
| Scheduled 1 sec | 0.150 | Reference |
| Scheduled 5 sec | 0.094 | -37% |
| Scheduled 10 sec | 0.062 | -59% |

### Key Performance Metrics

- **Job Processing Time**: 30% reduction on average
- **System Throughput**: Up to 6x improvement over slowest configuration
- **Resource Utilization**: Significant improvement through dynamic scheduling
- **Scalability**: Handles varying loads without manual intervention

---

## Development

### Project Structure

```
observer-module/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── hu/u_szeged/inf/fog/simulator/observer/
│   │   │       ├── ObserverApplication.java
│   │   │       └── service/
│   │   │           └── JobObserverService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── target/
```

### Building from Source

```bash
# Compile
mvn clean compile

# Run tests
mvn test

# Package
mvn package

# Install to local Maven repository
mvn install
```

### Key Classes

- **`ObserverApplication.java`** - Main entry point and application configuration
- **`JobObserverService.java`** - Core service implementing job detection and execution logic
- **`SimulatorJobDao.java`** - Database access layer (inherited from Executor module)

---

## Integration with DISSECT-CF-Fog

This module extends the [DISSECT-CF-Fog](https://github.com/sed-inf-u-szeged/DISSECT-CF-Fog) framework, which provides:

- **Simulator Core** - Discrete-event simulation engine for IoT-Fog-Cloud systems
- **Web Application** - Angular-based configuration interface
- **Executor Module** - Simulation job execution engine
- **Predictor UI** - Time series analysis and forecasting
- **Converter** - Interoperability with CloudSim and iFogSim

### Observer Module Advantages

The Observer module replaces the traditional `@Scheduled` annotation approach with:

1. **Dynamic Scheduling** - Adaptive job execution based on workload
2. **Parallel Processing** - Concurrent simulation execution
3. **Better Resource Management** - Reduced idle time and improved utilization
4. **Scalability** - Automatic adaptation to varying loads

---

## Research Context

### Problem Statement

Managing simulation job lifecycles in IoT-Fog-Cloud environments is challenging due to:
- Dynamic and distributed system nature
- Need for efficient resource allocation
- Real-time processing requirements
- Failure handling complexity

### Solution Approach

The Observer module addresses these challenges through:
- **Real-time monitoring** of job queues
- **Adaptive scheduling** based on system state
- **Thread pool management** for parallel execution
- **Configurable scaling strategies**

### Results

- Enhanced task management efficiency
- Improved system responsiveness
- Better scalability without manual configuration
- Optimized resource utilization

---

## Contributing

Contributions are welcome! This project is part of ongoing research at the University of Szeged.

### How to Contribute

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Future Enhancements

Potential areas for improvement:

- **Machine Learning Integration** - Predictive job execution time optimization
- **Advanced Monitoring** - Enhanced alerting and visualization
- **Distributed Execution** - Multi-node support for large-scale simulations
- **REST API** - External job submission and monitoring interface

---

## Citation

If you use this work in your research, please cite:

```bibtex
@mastersthesis{makhmanazarov2024observer,
  author  = {Akbar Makhmanazarov},
  title   = {Development of observer module supporting IoT-Fog-Cloud simulations in DISSECT-CF-Fog},
  school  = {University of Szeged, Institute of Informatics},
  year    = {2024},
  type    = {Master's Thesis},
  supervisor = {Dr. Andras Markus}
}
```

---

## License

This project is part of the DISSECT-CF-Fog framework. Please refer to the main [DISSECT-CF-Fog repository](https://github.com/sed-inf-u-szeged/DISSECT-CF-Fog) for licensing information.

---

## Acknowledgments

- **Supervisor**: Dr. Andras Markus, Assistant Professor, University of Szeged
- **Institution**: University of Szeged, Institute of Informatics
- **Research Group**: IoT Cloud Research Group
- **Parent Project**: [DISSECT-CF-Fog](https://github.com/sed-inf-u-szeged/DISSECT-CF-Fog)

---

## Contact

**Akbar Makhmanazarov**
- GitHub: [@Akbar2998](https://github.com/Akbar2998)
- University: University of Szeged
- Program: Computer Science MSc

---

## References

1. DISSECT-CF-Fog Framework - https://github.com/sed-inf-u-szeged/DISSECT-CF-Fog
2. Spring Framework - https://spring.io/
3. MongoDB - https://www.mongodb.com/
4. Java ExecutorService - https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html

---

<div align="center">

**Made with ❤️ at University of Szeged**

*For IoT-Fog-Cloud Simulation Research*

</div>
