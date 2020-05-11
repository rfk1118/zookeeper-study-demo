package chapter05.listener;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ZookeeperWatcherCallBack implements Watcher {

    private final CountDownLatch countDownLatch;

    private final ZooKeeper zooKeeper;

    public ZookeeperWatcherCallBack(CountDownLatch countDownLatch, ZooKeeper zooKeeper) {
        this.countDownLatch = countDownLatch;
        this.zooKeeper = zooKeeper;
    }

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                countDownLatch.countDown();
            }

            if (event.getType() == Event.EventType.NodeChildrenChanged) {
                try {
                    System.out.println("ReGet child" + zooKeeper.getChildren(event.getPath(), true));
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
