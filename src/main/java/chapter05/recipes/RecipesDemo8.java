package chapter05.recipes;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.time.LocalTime;


public class RecipesDemo8 {

    static DistributedBarrier barrier;

    static final String path = "/zk-RecipesDemo8";

    static class Runner implements Runnable {

        Runner() {
        }

        @Override
        public void run() {

            try {
                CuratorFramework curatorFramework = CuratorFrameworkFactory
                        .builder()
                        .connectString("localhost:2181")
                        .sessionTimeoutMs(5000)
                        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                        .build();
                curatorFramework.start();
                long sessionId = curatorFramework.getZookeeperClient().getZooKeeper().getSessionId();
                barrier = new DistributedBarrier(curatorFramework, path);
                System.out.println(sessionId + "," + LocalTime.now() + "setBarrier");
                barrier.setBarrier();
                barrier.waitOnBarrier();
                System.out.println(sessionId + "," + LocalTime.now() + "start");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 3; i++) {
            new Thread(new Runner()).start();
            Thread.sleep(2000);
        }
        barrier.removeBarrier();

    }


}
