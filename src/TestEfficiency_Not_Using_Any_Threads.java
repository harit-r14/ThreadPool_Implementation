import java.util.ArrayList;
import java.util.List;

public class TestEfficiency_Not_Using_Any_Threads {
    public static int processData(int data) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return data * data;
    }

    public static List<Integer> processLargeDataset(List<Integer> dataset) {
        List<Integer> results = new ArrayList<>();
        for (Integer data : dataset) {
            results.add(processData(data));
        }
        return results;
    }

    /*
        Test : 100 seconds
     */


    public static void main(String[] args) {
        List<Integer> dataset = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            dataset.add(i);
        }

        long startTime = System.currentTimeMillis();
        List<Integer> results = processLargeDataset(dataset);
        long endTime = System.currentTimeMillis();

        System.out.println("Processing took " + (endTime - startTime) / 1000 + " seconds");
    }
}


