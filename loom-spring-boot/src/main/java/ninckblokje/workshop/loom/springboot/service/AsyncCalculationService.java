package ninckblokje.workshop.loom.springboot.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncCalculationService {

    private final CalculationService service;

    public AsyncCalculationService(CalculationService service) {
        this.service = service;
    }

    @Async
    public CompletableFuture<List<Integer>> primeNumbersTill(int endRange) {
        var result = service.primeNumbersTill(endRange).boxed()
            .collect(Collectors.toList());
        
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<Double> calcPi(long iterations) {
        var result = service.calcPi(iterations);
        
        return CompletableFuture.completedFuture(result);
    }
}
