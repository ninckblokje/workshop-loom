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

package ninckblokje.workshop.loom.scopedvalues;

import jdk.incubator.concurrent.ScopedValue;

import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        mainWithThreadLocals();
        mainWithScopedValues();
    }

    static void mainWithScopedValues() {
        // var worker = new ScopedValueWorker();

        // try (var executor = Executors.newThreadPerTaskExecutor(Thread.ofPlatform().name("pthread-", 20).factory())) {
        //     executor.submit(() -> ScopedValue.where(ScopedValueWorker.CTX, new Context("12")).run(worker::doWork));
        //     executor.submit(() -> ScopedValue.where(ScopedValueWorker.CTX, new Context("34")).run(worker::doWork));
        // }
    }

    static void mainWithThreadLocals() {
        var worker = new ThreadLocalWorker();

        try (var executor = Executors.newThreadPerTaskExecutor(Thread.ofPlatform().name("pthread-", 10).factory())) {
            executor.submit(() -> worker.doWork(new Context("56")));
            executor.submit(() -> worker.doWork(new Context("78")));
        }
    }
}
