package chapter05.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorDemo1 {

    public static void main(String[] args) {
        RetryPolicy  policy = new ExponentialBackoffRetry(1000,3);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", policy);
        curatorFramework.start();
    }
}
