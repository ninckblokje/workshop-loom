/*
 * Copyright (c) 2023, ninckblokje
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ninckblokje.workshop.loom.springboot.controller;

import jdk.incubator.concurrent.StructuredTaskScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/calc")
public class CalculationController {

    private static final Logger log = LoggerFactory.getLogger(CalculationController.class);

    @GetMapping("/all")
    public Map<String, Object> getAll(
            @RequestParam(defaultValue = "2500") int endRange,
            @RequestParam(defaultValue = "10000000") long iterations
    ) throws InterruptedException, ExecutionException {

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var piFuture = scope.fork(() -> calcPi(iterations));
            var primeFuture = scope.fork(() -> primeNumbersTill(endRange));

            scope.join();
            scope.throwIfFailed();

            return Map.of(
                    "pi", piFuture.get(),
                    "prime", primeFuture.get()
            );
        }
    }

    @GetMapping("/pi")
    public double getPi(@RequestParam(defaultValue = "10000000") long iterations) {
        return calcPi(iterations);
    }

    @GetMapping("/prime")
    public IntStream getPrimeNumbers(
            @RequestParam(defaultValue = "2500") int endRange
    ) {
        return primeNumbersTill(endRange);
    }

    double calcPi(long iterations) {
        var msg = Thread.currentThread().isVirtual()
                ? "Virtual thread %d: Starting".formatted(Thread.currentThread().threadId())
                : "Thread %d: Starting".formatted(Thread.currentThread().threadId());
        log.info(msg);

        double pi = 1;

        log.info("Calculating pi for {} iterations", iterations);
        for (long i = 3; i < iterations; i += 4) {
            pi = pi - (1 / (double) i) + (1 / (double) (i + 2));
            Thread.yield();
        }

        return pi * 4;
    }

    boolean isPrime(int number) {
        return IntStream.rangeClosed(2, (int) (Math.sqrt(number)))
                .allMatch(n -> number % n != 0);
    }

    IntStream primeNumbersTill(int endRange) {
        var msg = Thread.currentThread().isVirtual()
                ? "Virtual thread %d: Starting".formatted(Thread.currentThread().threadId())
                : "Thread %d: Starting".formatted(Thread.currentThread().threadId());
        log.info(msg);

        log.info("Calculating prime numbers in range 2..{}", endRange);
        return IntStream.rangeClosed(2, endRange)
                .filter(this::isPrime)
                .peek(value -> Thread.yield());
    }
}
