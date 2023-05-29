# loom-http-client

This program shows the power and using virtual threads with IO and a slow service (it uses [httpbin.org](https://httpbin.org) with a delay of 10 seconds)

- Run the program and notice the time it takes with 10 platform threads
- In the file [Main](src/main/java/ninckblokje/workshop/loom/httpclient/Main.java) change the executor service
  - It must create a new executor service using the method `newVirtualThreadPerTaskExecutor()`
  - No max number of virtual threads is required
- Run the program again and notice the difference in time
- Increase the number of requests to 250
- Run the program again, is there any difference in time?

What causes the performance difference?
