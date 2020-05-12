package chapter05.defaultzk;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * NodeCreated/zk-book
 * NodeDataChanged/zk-book
 * NodeDeleted/zk-book
 */
public class ExistApiSyncUsage implements Watcher {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zooKeeper;

    public static void main(String[] args) {
        try {
            String path = "/zk-book";
            zooKeeper = new ZooKeeper("localhost:2181", 5000, new ExistApiSyncUsage());
            countDownLatch.await();
            zooKeeper.exists(path, true);

            zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zooKeeper.setData(path, "123".getBytes(), -1);


            zooKeeper.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zooKeeper.delete(path + "/c1", -1);
            zooKeeper.delete(path, -1);

            // sleep
            TimeUnit.MINUTES.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void process(WatchedEvent event) {
        try {
            if (Event.KeeperState.SyncConnected == event.getState()) {
                if (Event.EventType.None == event.getType() && null == event.getPath()) {
                    countDownLatch.countDown();
                    return;
                }

                switch (event.getType()) {
                    case NodeCreated:
                        System.out.println("NodeCreated" + event.getPath());
                        zooKeeper.exists(event.getPath(), true);
                        break;
                    case NodeDeleted:
                        System.out.println("NodeDeleted" + event.getPath());
                        zooKeeper.exists(event.getPath(), true);
                        break;
                    case NodeDataChanged:
                        System.out.println("NodeDataChanged" + event.getPath());
                        zooKeeper.exists(event.getPath(), true);
                        break;
                    case NodeChildrenChanged:
                        System.out.println("NodeChildrenChanged" + event.getPath());
                        zooKeeper.exists(event.getPath(), true);
                        break;
                }
            }
        } catch (Exception e) {

        }
    }
}
