# scoped-values

This project implements a simple flow where `ThreadLocal` is used to propagate a context. This context contains a user id.

The worker instance starts the process and forks multiple threads for sub tasks. In the `ThreadLocal` scenario the context is stored in the `ThreadLocal` instance in the worker instance. The sub tasks can directly access this variable. The context is propagated to newly forked threads using the `InheritableThreadLocal` instance.

In this assignment the same setup is going to be replicated using `ScopedValue` instead of `ThreadLocal`. In both cases structured concurrency is used in the sub task to fork the threads.

- Execute the program
- In the class `ScopedValueWorker`:
  - Change the variable `CTX` to be of type `ScopedValue<Context>` and init it
- In the class `Main`:
  - Remove the comments in the method `mainWithScopedValues` so that the commented out code will be executed
- Execute the program and fix the compilation error
- Execute the program again
