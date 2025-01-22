import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main{
    public static void main(String[] args){
        ThreadPool threadPool = new ThreadPool(5);

        for(int i = 0 ; i < 10 ; i++)
        {
            int finalI = i;
            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Task " + finalI + " is running by " + Thread.currentThread().getName());
                }
            });
        }
    }
}