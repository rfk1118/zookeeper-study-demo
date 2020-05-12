package chapter05.defaultzk;

import chapter05.listener.ZookeeperWatcherCallBack;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZookeeperGetChildrenWithSync {

    public static final CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zookeeper;

    public static void main(String[] args) {
        try {
            String path = "/ZookeeperGetChildrenWithSync";
            zookeeper = new ZooKeeper("localhost:2181", 5000,
                    new ZookeeperWatcherCallBack(countDownLatch, zookeeper));
            countDownLatch.await();
            zookeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zookeeper.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            zookeeper.getChildren(path, true, new ICallBack(), null);
            zookeeper.create(path + "/c2", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            TimeUnit.SECONDS.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class ICallBack implements AsyncCallback.Children2Callback {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
            System.out.printf("rc:%d,path:%s,ctx:%s,children:%s,stat:%s%n", rc, path, ctx, children, stat);
        }
    }
}
