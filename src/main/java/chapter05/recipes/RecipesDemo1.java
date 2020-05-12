package chapter05.recipes;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class RecipesDemo1 {
    static final RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
    static final CuratorFramework curatorFramework = CuratorFrameworkFactory
            .builder()
            .connectString("localhost:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(policy)
            .build();

    static final String path = "/zk-RecipesDemo1";

    public static void main(String[] args) throws Exception {
        curatorFramework.start();
        curatorFramework.create().
                creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path, "helloWorld".getBytes());
        final NodeCache cache = new NodeCache(curatorFramework, path, false);
        cache.start(true);
        cache.getListenable().addListener(() -> System.out.println("Node update" + Arrays.toString(cache.getCurrentData().getData())));
        curatorFramework.setData().forPath(path, "123".getBytes());
        TimeUnit.SECONDS.sleep(5);
        curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
        TimeUnit.SECONDS.sleep(20);

    }
}
