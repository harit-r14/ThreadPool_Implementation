/*
    https://stackoverflow.com/questions/2324030/java-thread-reuse
    (need to use lock while getting new task.)

    https://www.baeldung.com/java-concurrent-locks



    Use Case	                        Use Runnable	Use Callable
    Task doesn't return a result	            ✅ Yes	❌ No
    Task returns a result	                    ❌ No	✅ Yes
    Task may throw checked exceptions	        ❌ No	✅ Yes
    Simpler, fire-and-forget tasks	            ✅ Yes	❌ No
*/

/*
Next Plan :

Accept the runnable or callable  through submit method ( require 2 submit method - overloading)

will wrap this runnable or callable in FutureTask


will push  FutureTask it in some queue (will follow FIFO)


now thread will futuretask from the queue and will run().



 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;


public class  ThreadPool {

    private final Queue<FutureTask<?>> taskQueue = new LinkedList<>();
    private final List<Thread> threads = new ArrayList<>();
    private final Object lock = new Object();
    private boolean isShutdown = false;
    private boolean isShutdownNow = false;

    public ThreadPool(int sizeOfPool) {
        for (int i = 0; i < sizeOfPool; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    FutureTask<?> taskToRun = null;

                    synchronized (lock) {
                        while (taskQueue.isEmpty() && !(isShutdown || isShutdownNow)) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                return;
                            }
                        }

                        if (isShutdownNow) {
                            System.out.println(Thread.currentThread().getName() + " is shutting down now");
                            return;
                        }

                        if (isShutdown && taskQueue.isEmpty()) {
                            System.out.println(Thread.currentThread().getName() + " is shutting down");
                            return;
                        }

                        taskToRun = taskQueue.poll();
                    }

                    if (taskToRun != null) {
                        taskToRun.run();
                    }
                }
            });
            thread.start();
            threads.add(thread);
        }
    }

    public FutureTask<?> submit(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("Thread pool is shutdown");
        }
        FutureTask<?> futureTask = new FutureTask<>(task, null);
        synchronized (lock) {
            taskQueue.add(futureTask);
            lock.notify();
        }
        return futureTask;
    }

    public Future<?> submit(Callable<?> task) {
        if (isShutdown) {
            throw new IllegalStateException("Thread pool is shutdown");
        }
        FutureTask<?> futureTask = new FutureTask<>(task);

        synchronized (lock) {
            taskQueue.add(futureTask);
            lock.notify();
        }

        return futureTask;
    }

    public void shutdown() {
        synchronized (lock) {
            isShutdown = true;
            lock.notifyAll();
        }
    }

    public List<Runnable> shutdownNow() {

        isShutdownNow = true;
        for (Thread thread : threads) {
            thread.interrupt();
        }

        synchronized (lock) {
            List<Runnable> remainingTask = new ArrayList<>();

            while (!taskQueue.isEmpty()) {
                remainingTask.add(() -> {
                    taskQueue.poll().run();
                });
            }
            return remainingTask;
        }
    }
}
