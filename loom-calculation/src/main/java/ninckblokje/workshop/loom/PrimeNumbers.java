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

package ninckblokje.workshop.loom;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class PrimeNumbers {

    public static void main(String[] args) {
        var startTime = System.currentTimeMillis();

//        try (var executorService = Executors.newThreadPerTaskExecutor(Thread.ofPlatform().factory())) {
        try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {

            var futures = new ArrayList<Future<?>>();
            for (int i = 0; i < 10_000; i++) {
                futures.add(
                        executorService.submit(() -> {
                            primeNumbersTill(2500)
                                    .forEach(PrimeNumbers::printPrimeNumber);
                        }));
            }

            futures.forEach(PrimeNumbers::get);
        }

        System.out.printf("Done in %d ms%n", System.currentTimeMillis() - startTime);
    }

    public static void get(Future<?> future) {
        try {
            future.get();
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void printPrimeNumber(int primeNumber) {
        var msg = Thread.currentThread().isVirtual()
                ? "Virtual thread %d: %d".formatted(Thread.currentThread().threadId(), primeNumber)
                : "Thread %d: %d".formatted(Thread.currentThread().threadId(), primeNumber);
        System.out.println(msg);
    }

    public static IntStream primeNumbersTill(int n) {
        var msg = Thread.currentThread().isVirtual()
                ? "Virtual thread %d: Starting".formatted(Thread.currentThread().threadId())
                : "Thread %d: Starting".formatted(Thread.currentThread().threadId());
        System.out.println(msg);

        try {
            Thread.sleep(60_000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        return IntStream.rangeClosed(2, n)
                .filter(PrimeNumbers::isPrime);
    }

    private static boolean isPrime(int number) {
        return IntStream.rangeClosed(2, (int) (Math.sqrt(number)))
                .allMatch(n -> number % n != 0);
    }
}