package chapter05.defaultzk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class AuthDelete {

    public static final String PATH = "/zk-book-auth-test01";

    public static final String PATH2 = "/zk-book-auth-test01/child";


    public static void main(String[] args) {
        try {
            ZooKeeper zooKeeper1 = new ZooKeeper("localhost:2181", 5000, new ExistApiSyncUsage());
            zooKeeper1.addAuthInfo("digest", "foo:true".getBytes());
            zooKeeper1.create(PATH, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
            zooKeeper1.create(PATH2, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ZooKeeper zooKeeper2 = new ZooKeeper("localhost:2181", 5000, new ExistApiSyncUsage());
            zooKeeper2.delete(PATH2, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            ZooKeeper zooKeeper3 = new ZooKeeper("localhost:2181", 5000, new ExistApiSyncUsage());
            zooKeeper3.addAuthInfo("digest", "foo:true".getBytes());
            zooKeeper3.delete(PATH2, -1);
            zooKeeper3.delete(PATH, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
