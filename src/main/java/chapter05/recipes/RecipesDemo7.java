package chapter05.recipes;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 种子选手1号准备好了
 * 种子选手2号准备好了
 * 种子选手3号准备好了
 * 种子选手3号起跑
 * 种子选手1号起跑
 * 种子选手2号起跑
 */
public class RecipesDemo7 {

    static class Runner implements Runnable {

        private final String name;

        Runner(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println(name + "准备好了");
            try {
                RecipesDemo7.cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(name + "起跑");
        }
    }

    public static final CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(new Runner("种子选手1号"));
        executorService.submit(new Runner("种子选手2号"));
        executorService.submit(new Runner("种子选手3号"));
        executorService.shutdown();
    }


}
