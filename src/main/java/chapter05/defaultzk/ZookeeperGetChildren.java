package chapter05.defaultzk;

import chapter05.listener.ZookeeperWatcherCallBack;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * [c1]
 * ReGet child[c1, c2]
 */
public class ZookeeperGetChildren {

    public static final CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zookeeper;

    public static void main(String[] args) {
        String path = "/zk-book-test2";
        try {
            zookeeper = new ZooKeeper("localhost:2181", 5000,
                    new ZookeeperWatcherCallBack(countDownLatch, zookeeper));
            countDownLatch.await();
            zookeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zookeeper.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            List<String> children = zookeeper.getChildren(path, true);
            System.out.println(children);
            zookeeper.create(path + "/c2", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            TimeUnit.SECONDS.sleep(1000);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
