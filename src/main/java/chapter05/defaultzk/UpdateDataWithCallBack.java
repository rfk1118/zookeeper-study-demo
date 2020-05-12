package chapter05.defaultzk;

import chapter05.listener.ZookeeperWatcher;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * chapter05.listener.ZookeeperWatcher --->receive watch eventWatchedEvent state:SyncConnected type:None path:null
 * chapter05.listener.ZookeeperWatcher --->receive watch eventWatchedEvent state:SyncConnected type:NodeDataChanged path:/GetDataApiWithSync43500
 * /GetDataApiWithSync43500update success
 */
public class UpdateDataWithCallBack {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);


    public static void main(String[] args) {
        final String path = "/" + GetDataApiWithSync.class.getSimpleName() + new Random().nextInt(100000);
        try {
            ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 5000, new ZookeeperWatcher(countDownLatch));

            // wait
            countDownLatch.await();
            zooKeeper.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            zooKeeper.getData(path, true, null);

            //  this will use callBack Listener
            zooKeeper.setData(path, "456".getBytes(), -1, new IStatCallBack(), null);

            TimeUnit.MINUTES.sleep(100);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static class IStatCallBack implements AsyncCallback.StatCallback {
        @Override
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            if (rc == 0) {
                System.out.println(path + "update success");
            } else {
                System.out.println(path + "update fail" + rc);
            }
        }
    }
}
