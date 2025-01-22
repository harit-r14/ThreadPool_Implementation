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
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPool {

    // Submit
    // Shutdown
//    Future<>
//    Callable

    //FutureTask.java



    Queue<Runnable> taskQueue = new LinkedList<>(); //shared variable
    ArrayList<Thread> threads = new ArrayList<>();
    Lock taskQueueLock = new ReentrantLock();


    int sizeOfPool;
    boolean isShutdown = false;

    ThreadPool(int sizeOfPool){
        this.sizeOfPool = sizeOfPool;

        Runnable task = new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    System.out.println(Thread.currentThread().getName() + " waiting for the lock");
                    taskQueueLock.lock();
                    System.out.println(Thread.currentThread().getName() + " acquired lock");
                    if (taskQueue.isEmpty()) {  // If TaskQueue is empty then it will wait until new task is added and will call notify
                        try {
                            System.out.println(Thread.currentThread().getName() + " is waiting for task to run");
                            wait();  // wait for new task
                        } catch (InterruptedException e) {
                            System.out.println(Thread.currentThread().getName() + " is interrupted");
                        }
                    }
                    else {
                        System.out.println(Thread.currentThread().getName() + " has accepted the new task");
                        Runnable newTask = taskQueue.poll();

                        System.out.println(Thread.currentThread().getName() + " released lock");
                        taskQueueLock.unlock(); // thread has accessed the taskQeueue so no need to keep lock while running the task. If it was released after .run() then other thread has to wait for the completion of task to get the new task. Which doesn't make sense.
                        newTask.run();
                    }
                }
            }
        };

        for(int i = 0 ; i < sizeOfPool ; i++)
        {
            threads.add(new Thread(task));
            System.out.println("Thread " + i + " is created");
        }

        for(int i = 0 ; i < sizeOfPool ; i++)
        {
            threads.get(i).start();
        }
    }


    public void submit(Runnable newTask)
    {
        if(isShutdown)
        {
            throw new RuntimeException("Thread pool is shutdown");
        }
        taskQueue.offer(newTask); //exception throw when queue is full (Note : implement remaining)
        notify();
    }

    public void shutdown()
    {
        isShutdown = true;
    }

    public void shutdownNow()
    {
        isShutdown = true;
        for(int i = 0 ; i < sizeOfPool ; i++)
        {
            threads.get(i).interrupt();
        }
    }

}