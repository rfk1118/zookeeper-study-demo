package chapter05;

import chapter05.listener.ZookeeperWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 *
 * chapter05.ZookeeperSyncUsage --->State:CONNECTING sessionid:0x0 local:null remoteserver:null lastZxid:0 xid:1 sent:0 recv:0 queuedpkts:0 pendingresp:0 queuedevents:0
 * chapter05.listener.ZookeeperWatcher --->receive watch eventWatchedEvent state:SyncConnected type:None path:null
 * firstNode/hello
 * secondNode/hello0000000003
 * chapter05.ZookeeperSyncUsage --->zk session is established
 *
 */
public class ZookeeperSyncUsage {

    public static final CountDownLatch countDownLatch = new CountDownLatch(1);


    public static void main(String[] args) {
        try {
            // client request connection to server
            ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 15000, new ZookeeperWatcher(countDownLatch));

            System.out.println(ZookeeperSyncUsage.class.getName() + " --->" + zooKeeper);
            countDownLatch.await();

            byte[] bytes = "".getBytes();
            String path = "/hello";
            String result = zooKeeper.create(path, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.printf("firstNode%s%n", result);

            result = zooKeeper.create(path, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.printf("secondNode%s%n", result);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.printf("%s --->zk session is established%n", ZookeeperSyncUsage.class.getName());
    }
}
