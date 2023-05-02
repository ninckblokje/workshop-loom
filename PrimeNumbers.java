import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class PrimeNumbers {

    public static void main(String[] args) {
        var startTime = System.currentTimeMillis();

        // var executorService = Executors.newThreadPerTaskExecutor(Thread.ofPlatform().factory());
        var executorService = Executors.newVirtualThreadPerTaskExecutor();

        var futures = new ArrayList<Future<?>>();
        for (int i = 0; i < 10_000; i++) {
            futures.add(
                    executorService.submit(() -> {
                        primeNumbersTill(2500)
                                .forEach(PrimeNumbers::printPrimeNumber);
                    }));
        }

        futures.forEach(PrimeNumbers::get);

        System.out.println("Done in %d ms".formatted(System.currentTimeMillis() - startTime));
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
                .filter(x -> isPrime(x));
    }

    private static boolean isPrime(int number) {
        return IntStream.rangeClosed(2, (int) (Math.sqrt(number)))
                .allMatch(n -> number % n != 0);
    }
}