package chapter05.defaultzk;

import chapter05.listener.ZookeeperWatcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ZookeeperUsageSimple {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        try {
            // client request connection to server
            ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 15000, new ZookeeperWatcher(countDownLatch));

            System.out.printf("%s --->%s%n", ZookeeperUsageSimple.class.getName(), zooKeeper);
            countDownLatch.await();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.printf("%s --->zk session is established%n", ZookeeperUsageSimple.class.getName());
    }
}
