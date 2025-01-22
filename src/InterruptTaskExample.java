import java.util.concurrent.*;

public class InterruptTaskExample {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Create a Callable that checks for interruption
        Callable<Void> task = () -> {
            for (int i = 0; i < 1000; i++) {
                // Check if the thread was interrupted
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Task was interrupted at iteration: " + i);
//                    return null;  // Exit the task if interrupted
                }
                // Simulate some work (e.g., sleeping)
//                try {
////                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    // Handle interruption during sleep
//                    System.out.println("Task was interrupted during sleep.");
//                    return null;  // Exit the task if interrupted during sleep
//                }
            }
            System.out.println("Task completed");
            return null;
        };

        // Wrap the task in a FutureTask
        FutureTask<Void> futureTask = new FutureTask<>(task);

        // Create and start a thread to run the FutureTask
        Thread taskThread = new Thread(futureTask);
        taskThread.start();

        // Let the task run for a bit
//        Thread.sleep(500);

        // Interrupt the thread running the task
        taskThread.interrupt();

        try {
            // Attempt to get the result of the task
            futureTask.get(); // This may throw an InterruptedException if the task was interrupted
        } catch (InterruptedException e) {
            System.out.println("Main thread was interrupted while waiting.");
        } catch (ExecutionException e) {
            System.out.println("Execution exception: " + e.getMessage());
        }
    }
}
