# workshop-loom

In this repository the assignments for the SynTouch Loom workshop can be found. Loom is the name of the OpenJDK project for bringing virtual threads into the JVM. More information can be found here: https://openjdk.org/projects/loom/

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

The branch `solution` contains working solutions for all assignments

## Prerequisites

This project can either be run locally or using a devcontainer.

### Local

- Download & install JDK 20
- Download & install IntelliJ
- Configure JDK 20 in IntelliJ

## Devcontainer

- Install Visual Studio Code
- Install Docker
- Open the repository in Visual Studio Code and start the devcontainer

## Assignments

There are three assignments which show the capabilities (and API's) of virtual threads, structured concurrency and scoped values.

1. [loom-spring-boot](loom-spring-boot/README.md): Implement virtual threads and structured concurrency in a Spring Boot 3.0 application
2. [scoped-values](scoped-values/README.md): Implement scoped values (next the `ThreadLocal`) in a regular Java app
3. [loom-quarkus](loom-quarkus/README.md): Use the Quarkus virtual thread API

## Tips & trics

- To start a Spring Boot application using Maven: `mvn spring-boot:run`
- To start a Java application using Maven: `mvn exec:java`
- To start a Quarkus application using Maven: `mvn quarkus:dev`
