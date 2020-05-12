package chapter05.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class CuratorDemo6 {

    static RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
    static CuratorFramework curatorFramework = CuratorFrameworkFactory
            .builder()
            .connectString("localhost:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(policy)
            .build();

    static String path = "/zk-book/c1";

    public static void main(String[] args) throws Exception {
        curatorFramework.start();


        curatorFramework.create().
                creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path, "helloWorld".getBytes());

        Stat stat = new Stat();
        byte[] bytes = curatorFramework.getData().storingStatIn(stat).forPath(path);

        System.out.println("result--->" + new String(bytes));
        System.out.println("stat--->" + stat);

    }
}
