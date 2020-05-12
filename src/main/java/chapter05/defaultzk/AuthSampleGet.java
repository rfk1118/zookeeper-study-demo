package chapter05.defaultzk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.TimeUnit;

/**
 * org.apache.zookeeper.KeeperException$NoAuthException: KeeperErrorCode = NoAuth for /zk-book-auth-test3
 * 	at org.apache.zookeeper.KeeperException.create(KeeperException.java:113)
 * 	at org.apache.zookeeper.KeeperException.create(KeeperException.java:51)
 * 	at org.apache.zookeeper.ZooKeeper.getData(ZooKeeper.java:1155)
 * 	at org.apache.zookeeper.ZooKeeper.getData(ZooKeeper.java:1184)
 * 	at chapter05.AuthSampleGet.main(AuthSampleGet.java:20)
 */
public class AuthSampleGet {

    final static String PATH ="/zk-book-auth-test3";

    public static void main(String[] args) {
        try {
            ZooKeeper zooKeeper1 = new ZooKeeper("localhost:2181",5000,null);
            zooKeeper1.addAuthInfo("digest","foo:true".getBytes());
            zooKeeper1.create(PATH,"init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

            ZooKeeper zooKeeper2 = new ZooKeeper("localhost:2181",5000,null);
            byte[] data = zooKeeper2.getData(PATH, false, null);
            System.out.println(new String(data));
            TimeUnit.MINUTES.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
