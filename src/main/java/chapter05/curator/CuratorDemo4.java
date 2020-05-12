package chapter05.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class CuratorDemo4 {

    static final RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
    static final CuratorFramework curatorFramework = CuratorFrameworkFactory
            .builder()
            .connectString("localhost:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(policy)
            .namespace("CuratorDemo3")
            .build();


    public static void main(String[] args) throws Exception {
        curatorFramework.start();
        curatorFramework.create().creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/CuratorDemo4/hello/world", "init".getBytes());

    }
}
