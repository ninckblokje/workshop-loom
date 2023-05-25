package ninckblokje.workshop.loom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static java.lang.Thread.ofVirtual;
import static java.lang.Thread.sleep;


public class Main
{
    static final Logger logger = LoggerFactory.getLogger(Main.class);
    static void log(String message) {
        logger.info("{} | " + message, Thread.currentThread());
    }

     public static void main( String[] args ) throws InterruptedException {
        setOneCarrierThread();
        var firstPerson = ofVirtual().name("Person 1 shower thread").start(shower());
        var secondPerson = ofVirtual().name("Person 2 shower thread").start(shower());
        firstPerson.join();
        secondPerson.join();
    }

    private static void setOneCarrierThread() {
        System.setProperty("jdk.virtualThreadScheduler.parallelism", "1");
        System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "1");
        System.setProperty("jdk.virtualThreadScheduler.minRunnable", "1");
        System.setProperty("jdk.tracePinnedThreads", "full");
        log("jdk.virtualThreadScheduler.parallelism: " + System.getProperty("jdk.virtualThreadScheduler.parallelism"));
        log("jdk.virtualThreadScheduler.maxPoolSize: " + System.getProperty("jdk.virtualThreadScheduler.maxPoolSize"));
        log("jdk.virtualThreadScheduler.minRunnable: " + System.getProperty("jdk.virtualThreadScheduler.minRunnable"));
        log("jdk.tracePinnedThreads: " + System.getProperty("jdk.tracePinnedThreads"));
    }

    static class Bathroom {
        /**
         * If you remove synchronized a virtual thread won't be pinned to the carrier thread
         */
        synchronized void useShower() throws InterruptedException {
            log(" Start showering");
            sleep(Duration.ofSeconds(5L));
            log(" Completed showering");
        }
    }

    static Bathroom bathroom = new Bathroom();

    static Runnable shower() {
        return () -> {
            try {
                bathroom.useShower();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
