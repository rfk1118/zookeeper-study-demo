package chapter05.recipes;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;

public class TestintServer {

    final static String path = "/zookeeper";

    public static void main(String[] args) throws Exception {
        TestingServer testServer = new TestingServer(2182, new File("/Users/renfakai/github/zk-test-server"));

        testServer.start();
        
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("localhost:2182")
                .sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

                curatorFramework.start();

        System.out.println(curatorFramework.getChildren().forPath(path));

        TimeUnit.MINUTES.sleep(10);
        testServer.close();

    }
}