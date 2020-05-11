package chapter05.listener;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @author renfakai
 */
public class ZookeeperWatcher implements Watcher {

    private final CountDownLatch countDownLatch;

    public ZookeeperWatcher(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println(this.getClass().getName() + " --->" + "receive watch event" + event);
        if (Objects.equals(event.getState(), Event.KeeperState.SyncConnected)) {
            countDownLatch.countDown();
        }
    }

}
