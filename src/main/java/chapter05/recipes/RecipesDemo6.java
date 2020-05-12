package chapter05.recipes;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

public class RecipesDemo6 {
    static RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
    static CuratorFramework curatorFramework = CuratorFrameworkFactory
            .builder()
            .connectString("localhost:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(policy)
            .build();

    static String path = "/zk-RecipesDemo6";


    public static void main(String[] args) throws Exception {
        curatorFramework.start();
        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(curatorFramework, path, new RetryNTimes(3, 1000));
        AtomicValue<Integer> rc = atomicInteger.add(8);
        System.out.println(rc.succeeded());

        System.out.println(rc.preValue() + "," + rc.postValue());
    }


}
