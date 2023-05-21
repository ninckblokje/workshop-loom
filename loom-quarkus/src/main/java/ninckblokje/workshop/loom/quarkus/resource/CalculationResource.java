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

package ninckblokje.workshop.loom.quarkus.resource;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import ninckblokje.workshop.loom.quarkus.service.CalculationService;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/calc")
public class CalculationResource {

    private final CalculationService service;

    public CalculationResource(CalculationService service) {
        this.service = service;
    }

    @GET
    @Path("/all")
    @Produces(APPLICATION_JSON)
    public Uni<Map<String, Object>> getAll(
            @QueryParam("endRange") @DefaultValue("2500") int endRange,
            @QueryParam("iterations") @DefaultValue("10000000") long iterations
    ) {
        var calcPiUni = Uni.createFrom()
                .item(service.calcPi(iterations));
        var primeNumbersTillUni = Uni.createFrom()
                .item(service.primeNumbersTill(endRange));

        return Uni.combine().all()
                .unis(calcPiUni, primeNumbersTillUni)
                .combinedWith(this::combineResults);
    }

    Map<String, Object> combineResults(List<?> objects) {
        var pi = objects.get(0);
        var primeNumbers = objects.get(1);

        return Map.of(
                "pi", pi,
                "prime", primeNumbers
        );
    }

    @GET
    @Path("/pi")
    @Blocking
    @RunOnVirtualThread
    public double getPi(@QueryParam("iterations") @DefaultValue("10000000") long iterations) {
        return service.calcPi(iterations);
    }

    @GET
    @Path("/prime")
    @Produces(APPLICATION_JSON)
    @Blocking
    @RunOnVirtualThread
    public IntStream getPrimeNumbers(
            @QueryParam("endRange") @DefaultValue("2500") int endRange
    ) {
        return service.primeNumbersTill(endRange);
    }
}
