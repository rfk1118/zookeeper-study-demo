package chapter05.recipes;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.time.LocalTime;


public class RecipesDemo9 {


    static String path = "/zk-RecipesDemo9";

    static class Runner implements Runnable {

        private String name;

        Runner(String name) {
            this.name = name;
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
                DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(curatorFramework, path, 4);

                Thread.sleep(Math.round(Math.random() * 3000));
                barrier.enter();
                System.out.println(sessionId + "," + LocalTime.now() + "进入Barrier");
                Thread.sleep(Math.round(Math.random() * 3000));
                barrier.leave();
                System.out.println(sessionId + "," + LocalTime.now() + "退出");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 6; i++) {
            new Thread(new Runner("" + i)).start();
        }

    }
}
