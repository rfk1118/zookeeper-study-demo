package chapter05;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * main123
 * main104,104,0
 * 0,123
 * 104,105,1
 */
public class GetDataApiWhitSyncListener implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zooKeeper;

    private static Stat stat = new Stat();

    public static void main(String[] args) {
        String path = "/" + GetDataApiWithSync.class.getSimpleName() + new Random().nextInt(100000);
        try {
            zooKeeper = new ZooKeeper("localhost:2181", 5000, new GetDataApiWhitSyncListener());
            countDownLatch.await();
            zooKeeper.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("main" + new String(zooKeeper.getData(path, true, stat)));
            System.out.println("main" + stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());
            zooKeeper.setData(path, "123".getBytes(), -1);
            TimeUnit.MINUTES.sleep(10);
        } catch (Exception e) {
        }

    }

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                countDownLatch.countDown();
            }
            if (event.getType() == Event.EventType.NodeDataChanged) {
                zooKeeper.getData(event.getPath(), true, new ICallBackListener(), null);
            }
        }
    }

    static class ICallBackListener implements AsyncCallback.DataCallback {
        @Override
        public void processResult(int rc, java.lang.String path, Object ctx, byte[] data, Stat stat) {
            System.out.println(rc + "," + new String(data));
            System.out.println(stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());
        }
    }

}
