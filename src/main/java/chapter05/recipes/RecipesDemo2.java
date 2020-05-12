package chapter05.recipes;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.TimeUnit;

public class RecipesDemo2 {
    static final RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
    static final CuratorFramework curatorFramework = CuratorFrameworkFactory
            .builder()
            .connectString("localhost:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(policy)
            .build();

    static final String path = "/zk-RecipesDemo22";

    public static void main(String[] args) throws Exception {
        curatorFramework.start();
        final PathChildrenCache cache = new PathChildrenCache(curatorFramework, path, true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        cache.getListenable().addListener((client, event) -> {
            switch (event.getType()) {
                case CHILD_ADDED:
                    System.out.println("CHILD_ADDED" + event.getData().getPath());
                    break;
                case CHILD_UPDATED:
                    System.out.println("CHILD_UPDATED" + event.getData().getPath());
                    break;
                case CHILD_REMOVED:
                    System.out.println("CHILD_REMOVED" + event.getData().getPath());
                    break;
                case CONNECTION_SUSPENDED:
                    System.out.println("CONNECTION_SUSPENDED" + event.getData().getPath());
                    break;
                case CONNECTION_RECONNECTED:
                    System.out.println("CONNECTION_RECONNECTED" + event.getData().getPath());
                    break;
                case CONNECTION_LOST:
                    System.out.println("CONNECTION_LOST" + event.getData().getPath());
                    break;
                case INITIALIZED:
                    System.out.println("INITIALIZED" + event.getData().getPath());
                    break;
                default:
                    throw new IllegalStateException("not find handle");
            }
        });

        curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath(path);
        TimeUnit.SECONDS.sleep(10);
        curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath(path + "/c1");
        TimeUnit.SECONDS.sleep(10);

        curatorFramework.delete().forPath(path + "/c1");
        TimeUnit.SECONDS.sleep(10);
        curatorFramework.delete().forPath(path);
        TimeUnit.SECONDS.sleep(10);
    }
}
