package chapter05;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GetDataApiWithSync implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zooKeeper;

    private static Stat stat = new Stat();

    public static void main(String[] args) {
        String path = "/" + GetDataApiWithSync.class.getSimpleName() + new Random().nextInt(100000);
        try {
            zooKeeper = new ZooKeeper("localhost:2181", 5000, new GetDataApiWithSync());
            countDownLatch.await();
            zooKeeper.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("main" + new String(zooKeeper.getData(path, true, stat)));
            System.out.println("main" + stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());
            zooKeeper.setData(path, "123".getBytes(), -1);
            TimeUnit.MINUTES.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                countDownLatch.countDown();
            }
            if (event.getType() == Event.EventType.NodeDataChanged) {
                try {
                    System.out.println("event" + new String(zooKeeper.getData(event.getPath(), true, stat)));
                    System.out.println("event" + stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
