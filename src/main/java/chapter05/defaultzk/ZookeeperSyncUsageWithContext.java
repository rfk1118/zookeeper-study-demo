package chapter05.defaultzk;

import chapter05.listener.ZookeeperWatcher;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * chapter05.ZookeeperSyncUsageWithContext --->State:CONNECTING sessionid:0x0 local:null remoteserver:null lastZxid:0 xid:1 sent:0 recv:0 queuedpkts:0 pendingresp:0 queuedevents:0
 * chapter05.listener.ZookeeperWatcher --->receive watch eventWatchedEvent state:SyncConnected type:None path:null
 * chapter05.ZookeeperSyncUsageWithContext --->zk session is established
 * create path/ZookeeperSyncUsageWithContext,i am context,/ZookeeperSyncUsageWithContext,0
 * create path/ZookeeperSyncUsageWithContext,i am context,null,-110
 * create path/ZookeeperSyncUsageWithContext,i am context,/ZookeeperSyncUsageWithContext0000000005,0
 */
public class ZookeeperSyncUsageWithContext {

    public static final CountDownLatch countDownLatch = new CountDownLatch(1);


    public static void main(String[] args) {
        try {
            // client request connection to server
            ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 15000, new ZookeeperWatcher(countDownLatch));

            System.out.println(ZookeeperSyncUsageWithContext.class.getName() + " --->" + zooKeeper);
            countDownLatch.await();

            byte[] bytes = "".getBytes();
            String path = "/ZookeeperSyncUsageWithContext";
            // create Success  this will call back with  0
            zooKeeper.create(path, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new IStringCallBack(), "i am context");
            // node have exist  this will call bask with -114
            zooKeeper.create(path, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new IStringCallBack(), "i am context");
            // create Success  this will call back with  0
            zooKeeper.create(path, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, new IStringCallBack(), "i am context");

            System.out.printf("%s --->zk session is established%n", ZookeeperSyncUsageWithContext.class.getName());

            TimeUnit.SECONDS.sleep(20);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    static class IStringCallBack implements AsyncCallback.StringCallback {

        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            System.out.printf("create path%s,%s,%s,%d%n", path, ctx, name, rc);
        }
    }
}
