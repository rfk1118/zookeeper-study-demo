package chapter05.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

public class CuratorDemo5 {

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
        try {
            curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        curatorFramework.create().creatingParentContainersIfNeeded().forPath(path,"helloworld".getBytes());

        Stat stat = new Stat();
        curatorFramework.getData().storingStatIn(stat).forPath(path);

        System.out.println(stat);
        curatorFramework.delete().withVersion(stat.getVersion()).forPath(path);

        curatorFramework.getData().storingStatIn(stat).forPath(path);

    }
}
