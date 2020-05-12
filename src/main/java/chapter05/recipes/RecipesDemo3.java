package chapter05.recipes;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * 72057595758444618,16:49:40.947成为master
 * org.apache.curator.framework.imps.CuratorFrameworkImpl@6773e6ee
 * 72057595758444618,16:50:40.949释放权限
 * 04 杀掉进程
 * 72057595758444618,16:52:10.755成为master
 * 5秒后进行选举
 * 杀掉进程
 */
public class RecipesDemo3 {
    static final RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
    static final CuratorFramework curatorFramework = CuratorFrameworkFactory
            .builder()
            .connectString("localhost:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(policy)
            .build();

    static final String path = "/zk-RecipesDemo3-master";

    public static void main(String[] args) throws Exception {
        curatorFramework.start();
        TimeUnit.SECONDS.sleep(30);
        LeaderSelector leaderSelector = new LeaderSelector(curatorFramework, path, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                long sessionId = client.getZookeeperClient().getZooKeeper().getSessionId();
                System.out.println(sessionId + "," + LocalTime.now() + "成为master");
                TimeUnit.MINUTES.sleep(1);
                System.out.println(sessionId + "," + LocalTime.now() + "释放权限");
            }
        });
        leaderSelector.autoRequeue();
        leaderSelector.start();
        TimeUnit.MINUTES.sleep(10);
    }
}
