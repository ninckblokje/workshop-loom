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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.random.RandomGenerator;

import static ninckblokje.workshop.loom.scopedvalues.ThreadLocalWorker.CTX;

public class ThreadLocalSubTask implements SubTask {

    private static final Logger log = LoggerFactory.getLogger(ThreadLocalSubTask.class);

    @Override
    public String doSubTask(boolean reverse) {
        var userId = CTX.get().userId();

        log.info("Do subtask for user {}", userId);

        try {
            Thread.sleep(Duration.ofSeconds(5));
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        log.info("Done doing subtask for user {}", userId);

        log.info("Old value for context: {}", CTX.get());
        CTX.set(new Context("Dummy %d".formatted(RandomGenerator.getDefault().nextInt(0, 2))));
        log.info("New value for context: {}", CTX.get());

        return (reverse) ? userId : new StringBuilder(userId).reverse().toString();
    }
}
