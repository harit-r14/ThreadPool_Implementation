import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TestEfficiency_Used_Inbuilt_ExecutorThreadPool {

    public static int processData(int data) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return data * data;
    }

    public static List<Integer> processLargeDataset(List<Integer> dataset) throws InterruptedException, ExecutionException {
        List<Integer> results = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<Future<Integer>> futures = new ArrayList<>();

        for (Integer data : dataset) {
            futures.add(executor.submit(() -> processData(data)));
        }

        for (Future<Integer> future : futures) {
            results.add(future.get());
        }

        executor.shutdown();
        return results;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Integer> dataset = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            dataset.add(i);
        }

        /*
            Test : 10 seconds
        */

        long startTime = System.currentTimeMillis();
        List<Integer> results = processLargeDataset(dataset);
        long endTime = System.currentTimeMillis();

        System.out.println("Processing took " + (endTime - startTime) / 1000 + " seconds");
    }
}
