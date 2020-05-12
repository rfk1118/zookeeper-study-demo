package chapter05.defaultzk;

import chapter05.listener.ZookeeperWatcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * chapter05.ZookeeperUsageSimpleWithPassWd - session1-->0
 * chapter05.ZookeeperUsageSimpleWithPassWd - password1 -->[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
 * chapter05.listener.ZookeeperWatcher --->receive watch eventWatchedEvent state:SyncConnected type:None path:null
 *
 *
 * chapter05.ZookeeperUsageSimpleWithPassWd - session2-->1
 * chapter05.ZookeeperUsageSimpleWithPassWd - password2 -->[116, 101, 115, 116]
 *
 * œ
 * chapter05.ZookeeperUsageSimpleWithPassWd - session3-->0
 * chapter05.ZookeeperUsageSimpleWithPassWd - password3 -->[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
 * chapter05.listener.ZookeeperWatcher --->receive watch eventWatchedEvent state:Expired type:None path:null
 * chapter05.listener.ZookeeperWatcher --->receive watch eventWatchedEvent state:SyncConnected type:None path:null
 */
public class ZookeeperUsageSimpleWithPassWd {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        try {
            // client request connection to server
            ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 15000, new ZookeeperWatcher(countDownLatch));

            // Get Session and password
            long sessionId = zooKeeper.getSessionId();
            byte[] sessionPassword = zooKeeper.getSessionPasswd();

            System.out.printf("%s - session1-->%d%n", ZookeeperUsageSimpleWithPassWd.class.getName(), sessionId);
            System.out.printf("%s - password1 -->%s%n", ZookeeperUsageSimpleWithPassWd.class.getName(), Arrays.toString(sessionPassword));


            countDownLatch.await();

            // error sessionId and password
            zooKeeper = new ZooKeeper("localhost:2181", 15000,
                    new ZookeeperWatcher(countDownLatch), 1L, "test".getBytes());

            System.out.printf("%s - session2-->%d%n", ZookeeperUsageSimpleWithPassWd.class.getName(), zooKeeper.getSessionId());
            System.out.printf("%s - password2 -->%s%n", ZookeeperUsageSimpleWithPassWd.class.getName(), Arrays.toString(zooKeeper.getSessionPasswd()));

            // right session and password
            zooKeeper = new ZooKeeper("localhost:2181", 15000,
                    new ZookeeperWatcher(countDownLatch), sessionId, sessionPassword);

            System.out.printf("%s - session3-->%d%n", ZookeeperUsageSimpleWithPassWd.class.getName(), zooKeeper.getSessionId());
            System.out.printf("%s - password3 -->%s%n", ZookeeperUsageSimpleWithPassWd.class.getName(), Arrays.toString(zooKeeper.getSessionPasswd()));

            TimeUnit.MINUTES.sleep(1000);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.printf("%s --->zk session is established%n", ZookeeperUsageSimpleWithPassWd.class.getName());
    }
}
