package chapter05.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CuratorDemo8 {

    static RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
    static CuratorFramework curatorFramework = CuratorFrameworkFactory
            .builder()
            .connectString("localhost:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(policy)
            .build();

    static String path = "/zk-CuratorDemo8";

    static final CountDownLatch countDownLatch = new CountDownLatch(2);

    static final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws Exception {
        curatorFramework.start();

        System.out.println(Thread.currentThread().getName());


        curatorFramework.create().
                creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .inBackground((client, event) -> {
                    System.out.println("event" + event.getResultCode() + ",type" + event.getType());
                    System.out.println(Thread.currentThread().getName());
                    countDownLatch.countDown();
                }).forPath(path, "init".getBytes());

        curatorFramework.create().
                creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .inBackground((client, event) -> {
                    System.out.println("event" + event.getResultCode() + ",type" + event.getType());
                    System.out.println(Thread.currentThread().getName());
                    countDownLatch.countDown();
                }).forPath(path, "init".getBytes());

        countDownLatch.await();
        TimeUnit.SECONDS.sleep(10);
        executorService.shutdown();

    }
}
