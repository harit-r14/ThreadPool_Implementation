import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPool threadPool = new ThreadPool(6);

      Future<?> futureTask1 =  threadPool.submit(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return Thread.currentThread().getName() + "This Was Callable : Task1";
        });


        Future<?> futureTask2 = threadPool.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return Thread.currentThread().getName() +  " This Was Callable : Task2";
        });


        Future<?> futureTask3 =  threadPool.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return Thread.currentThread().getName() +" This Was Callable : Task3";
        });

        threadPool.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println( Thread.currentThread().getName() + "This Was Runnable : Task4");
        });

        threadPool.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " This Was Runnable : Task5");
        });


        threadPool.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " This Was Runnable : Task6");
        });

        System.out.println(futureTask2.get());
        System.out.println(futureTask3.get());

//        futureTask1.get();

//        System.out.println("...Last Statement....");

        threadPool.shutdown();
        threadPool.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " This Was Runnable");
        });

        System.out.println(futureTask1.get());
//
        System.out.println("wowww");



    }
}
