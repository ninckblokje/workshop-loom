# loom-quarkus

The `loom-quarkus` applications is a Quarkus version of the `loom-spring-boot` project. There are three REST operations:
- `/calc/pi`: Calculate's PI based upon a number of iterations
- `/calc/prime`: Calculate prime numbers with a provided end
- `/calc/all`: Combines both

The endpoints `/calc/pi` and `/calc/prime` have been marked as blocking. We are going to make them run in a virtual thread.

Quarkus offers a solution for this ontop of there reactive REST stack (based upon [SmallRye Mutiny](https://smallrye.io/smallrye-mutiny)), by annotating a JAX-RS class or method with `@RunOnVirtualThread`. For more information see this guide: https://quarkus.io/guides/virtual-threads

- Use the requests in [CalculationController.http](http/CalculationController.http) file to see the current working
- In the class [CalculationResource](src/main/java/ninckblokje/workshop/loom/quarkus/resource/CalculationResource.java):
  - Remove the `Uni` code from the method `getPi` and make sure the methods returns a `double`
  - Remove the `Uni` code from the method `getPrimeNumbers` and make sure the methods returns a `java.util.stream.IntStream`
  - Annotate both methods with `@RunOnVirtualThread`
- Run the `loom-quarkus` application
- Use the requests in [CalculationController.http](http/CalculationController.http) file to test the changes
