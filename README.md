# workshop-loom

In this repository the assignments for the SynTouch Loom workshop (by Qun Wang and Jeroen Ninck Blok) can be found. Loom is the name of the OpenJDK project for bringing virtual threads into the JVM. More information can be found here: https://openjdk.org/projects/loom/

Project Loom is implemented using these JEP's:

- JDK 19:
  - [JEP-425](https://openjdk.org/jeps/425)
  - [JEP-428](https://openjdk.org/jeps/428)
- JDK 20:
  - [JEP-429](https://openjdk.org/jeps/429)
  - [JEP-436](https://openjdk.org/jeps/436)
  - [JEP-437](https://openjdk.org/jeps/437)

Virtual threads bring two more new features to the JVM:
- Structured concurrency
- Scoped values

## Prerequisites

This project can either be run locally or using a devcontainer.

### Local

- Download & install [JDK 20](https://adoptium.net/temurin/releases/)
- Download & install [IntelliJ](https://www.jetbrains.com/idea/)
- Configure JDK 20 in IntelliJ

## Devcontainer

- Install [Visual Studio Code](https://code.visualstudio.com/) with [Remote Development Extension Pack](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.vscode-remote-extensionpack)
- Install [Docker](https://www.docker.com/)
- Open the repository in Visual Studio Code and start the devcontainer

## Assignments

There are three assignments which show the capabilities (and API's) of virtual threads, structured concurrency and scoped values.

1. [loom-http-client](loom-http-client/README.md): Simple HTTP client used with virtual threads
1. [loom-spring-boot](loom-spring-boot/README.md): Implement virtual threads and structured concurrency in a Spring Boot 3.0 application
1. [thread-pinning](thread-pinning/README.md): Thread pinning using synchronized code
1. [scoped-values](scoped-values/README.md): Implement scoped values (next the `ThreadLocal`) in a regular Java app
1. [loom-quarkus](loom-quarkus/README.md): Use the Quarkus virtual thread API

## Tips & trics

- To start a Spring Boot application using Maven: `mvn spring-boot:run`
- To start a Java application using Maven: `mvn exec:java`
- To start a Quarkus application using Maven: `mvn quarkus:dev`
