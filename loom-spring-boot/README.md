# loom-spring-boot

This Spring Boot application does some CPU intensive calculations in three REST API's:
- `/calc/pi`: Calculate's PI based upon a number of iterations
- `/calc/prime`: Calculate prime numbers with a provided end
- `/calc/all`: Combines both using Spring's `@Async` and a `CompletableFuture`

The application uses platform threads. When you run the `verify` stage a JMeter tests (which can be found in the [jmeter](src/test/jmeter/) folder).

- Run the Maven command `verify` and save the output (note a CSV is also generated)
- Start the application manually
- Test the operations using the HTTP requests from the [http](http/) folder
  - Notice the thread name in the logging
- Stop the application

First we need to make Spring Boot use virtual threads instead of platform threads:

- Add a new method to the class [ExecutorConfig](src/main/java/ninckblokje/workshop/loom/springboot/config/ExecutorConfig.java) which provides a `ThreadFactory` configured to create virtual threads:

````java
    private ThreadFactory virtualThreadFactory() {
        log.info("Creating virtual thread factory");
        return Thread.ofVirtual()
                .name("vthread-", 0)
                .factory();
    }
````

- In the methods `asyncTaskExecutor` and `tomcatProtocolHandlerCustomizer` call the new factory method
- Run the Maven command `verify` and save the output (note a CSV is also generated)
- Start the application manually
- Test the operations using the HTTP requests from the [http](http/) folder
  - Notice the thread name in the logging
- Stop the application

The calculations in the [CalculationService](src/main/java/ninckblokje/workshop/loom/springboot/service/CalculationService.java) will block the CPU and therefor the thread for the duration of the calculation. We can make our calculations more friendly by allowing other threads to also take CPU time. This can be done using the method `Thread.yield`.

- In the method `calcPi` change the for loop to after each iteration call `Thread.yield()`
- In the method `primeNumbersTill` change the stream to call `Thread.yield()` after the filter in a `peek` call
- Run the Maven command `verify` and save the output (note a CSV is also generated)
- Start the application manually
- Test the operations using the HTTP requests from the [http](http/) folder
  - Notice the thread name in the logging
- Stop the application

The class [AsyncCalculationService](src/main/java/ninckblokje/workshop/loom/springboot/service/AsyncCalculationService.java) is a wrapper class around [CalculationService](src/main/java/ninckblokje/workshop/loom/springboot/service/CalculationService.java). It makes the methods asynchronous (using Spring's `@Async`) and returns a `CompletableFuture`. This can be replaced with structured concurrency

- Delete the class [AsyncCalculationService](src/main/java/ninckblokje/workshop/loom/springboot/service/AsyncCalculationService.java)
- Change the implementation of the method `getAll` so that it uses structured concurrency:

````java
    @GetMapping("/all")
    public Map<String, Object> getAll(
            @RequestParam(defaultValue = "2500") int endRange,
            @RequestParam(defaultValue = "10000000") long iterations
    ) throws InterruptedException, ExecutionException {

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var piFuture = scope.fork(() -> service.calcPi(iterations));
            var primeFuture = scope.fork(() -> service.primeNumbersTill(endRange));

            scope.join();
            scope.throwIfFailed();

            return Map.of(
                    "pi", piFuture.get(),
                    "prime", primeFuture.get()
            );
        }
    }
````

- Run the Maven command `verify` and save the output (note a CSV is also generated)
- Start the application manually
- Test the operations using the HTTP requests from the [http](http/) folder
  - Notice the thread name in the logging
- Stop the application
