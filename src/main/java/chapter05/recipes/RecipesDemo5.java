package chapter05.recipes;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * 生成订单号为17:04:10 | 681
 * 生成订单号为17:04:10 | 804
 * 生成订单号为17:04:10 | 821
 * 生成订单号为17:04:10 | 839
 * 生成订单号为17:04:10 | 854
 * 生成订单号为17:04:10 | 884
 * 生成订单号为17:04:10 | 910
 * 生成订单号为17:04:10 | 918
 * 生成订单号为17:04:10 | 932
 * 生成订单号为17:04:10 | 942
 */
public class RecipesDemo5 {
    static RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
    static CuratorFramework curatorFramework = CuratorFrameworkFactory
            .builder()
            .connectString("localhost:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(policy)
            .build();

    static String path = "/zk-RecipesDemo5";

    public static void main(String[] args) throws Exception {
        curatorFramework.start();
        final InterProcessMutex interProcessMutex = new InterProcessMutex(curatorFramework, path);
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                        interProcessMutex.acquire();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss | SSS");
                    String format = simpleDateFormat.format(new Date());
                    System.out.println("生成订单号为" + format);

                    try {
                        interProcessMutex.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        countDownLatch.countDown();
    }
}
