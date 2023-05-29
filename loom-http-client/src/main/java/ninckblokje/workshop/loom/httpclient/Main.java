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

package ninckblokje.workshop.loom.httpclient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;

import static java.net.http.HttpClient.Version.HTTP_1_1;

public class Main {

     public static void main(String[] args) {
          var start = LocalDateTime.now();

          try (var executorService = Executors.newFixedThreadPool(10, Thread.ofPlatform().factory())) {
               for (int i=0; i < 50; i++) {
                    int finalI = i;
                    executorService.submit(() -> callHttpBin(finalI));
               }
          }

          var end = LocalDateTime.now();

          System.out.printf("Finished in: %d seconds%n", Duration.between(start, end).getSeconds());
     }

     static void callHttpBin(int i) {
          try {
               var request = HttpRequest.newBuilder(URI.create("https://httpbin.org/delay/10"))
                       .build();

               var httpClient = HttpClient.newBuilder()
                       .version(HTTP_1_1)
                       .connectTimeout(Duration.ofSeconds(15))
                       .build();
               var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

               System.out.printf("%d: %d%n", i, response.statusCode());
          } catch (Exception ex) {
               ex.printStackTrace();
               throw new RuntimeException(ex);
          }
     }
}
