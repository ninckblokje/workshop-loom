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

import jdk.incubator.concurrent.StructuredTaskScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class ScopedValueWorker {

    private static final Logger log = LoggerFactory.getLogger(ScopedValueWorker.class);

    public static final ThreadLocal<Context> CTX = new InheritableThreadLocal<>();

    public void doWork() {
        log.info("Do work for user {}", CTX.get().userId());

        var subTask = new ScopedValueSubTask();
        try (var scope = new StructuredTaskScope.ShutdownOnFailure(null, Thread.ofVirtual().name("vthread-", 20).factory())) {
            var subTask1 = scope.fork(() -> subTask.doSubTask(false));
            var subTask2 = scope.fork(() -> subTask.doSubTask(true));

            scope.join();
            scope.throwIfFailed();

            log.info("Results {} & {}", subTask1.get(), subTask2.get());
        } catch (ExecutionException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        log.info("Done doing work for user {}", CTX.get().userId());
    }
}
