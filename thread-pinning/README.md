# thread pinning

Virtual threads are usually unmounted during blocking operations e.g. database call.
But there are two scenarios in which the virtual thread cannot be unmounted during blocking operations.

- When it executes code inside a synchronized block or method
- When it executes a native method or a foreign function

Given the above scenarios, the thread will stay pinned to its carrier thread.
This is concept is called ***Pinning***.
---
This project implements a case where 2 virtual threads run a synchronized job.<br> <br>
The maximum number of platform threads available to the scheduler is set to 1.<br>
When `useShower()` is called, java sees the `synchronized` keyword and will pin the virtual thread to the carrier thread.  

````java
 synchronized void useShower() throws InterruptedException {
            log(" Start showering");
            sleep(Duration.ofSeconds(5L));
            log(" Completed showering");
        }
````

- Build the project `mvn clean verify`
- Run the application `mvn exec:java`
- Try removing the `synchronized` keyword and rerun the application. Anything you notice?


