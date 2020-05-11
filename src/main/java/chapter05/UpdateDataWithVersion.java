package chapter05;

import chapter05.listener.ZookeeperWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * chapter05.listener.ZookeeperWatcher --->receive watch eventWatchedEvent state:SyncConnected type:None path:null
 * chapter05.listener.ZookeeperWatcher --->receive watch eventWatchedEvent state:SyncConnected type:NodeDataChanged path:/GetDataApiWithSync45661
 * 120,121,1
 * 120,122,2
 * org.apache.zookeeper.KeeperException$BadVersionException: KeeperErrorCode = BadVersion for /GetDataApiWithSync45661
 * 	at org.apache.zookeeper.KeeperException.create(KeeperException.java:115)
 * 	at org.apache.zookeeper.KeeperException.create(KeeperException.java:51)
 * 	at org.apache.zookeeper.ZooKeeper.setData(ZooKeeper.java:1270)
 * 	at chapter05.UpdateDataWithVersion.main(UpdateDataWithVersion.java:37)
 */
public class UpdateDataWithVersion {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zooKeeper;

    public static void main(String[] args) {
        final String path = "/" + GetDataApiWithSync.class.getSimpleName() + new Random().nextInt(100000);
        try {
            zooKeeper = new ZooKeeper("localhost:2181", 5000, new ZookeeperWatcher(countDownLatch));

            // wait
            countDownLatch.await();
            zooKeeper.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            zooKeeper.getData(path, true, null);

            // force flush data
            Stat stat = zooKeeper.setData(path, "456".getBytes(), -1);
            System.out.println(stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());

            // cas flush data  this will success
            Stat stat2 = zooKeeper.setData(path, "456".getBytes(), stat.getVersion());
            System.out.println(stat2.getCzxid() + "," + stat2.getMzxid() + "," + stat2.getVersion());

            // error version ,this will cas fail
            Stat stat3 = zooKeeper.setData(path, "456".getBytes(), stat.getVersion());
            System.out.println(stat3.getCzxid() + "," + stat3.getMzxid() + "," + stat3.getVersion());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
