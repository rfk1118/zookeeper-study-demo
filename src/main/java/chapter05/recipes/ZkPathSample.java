package chapter05.recipes;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.curator.utils.ZKPaths.PathAndNode;
import org.apache.zookeeper.ZooKeeper;

public class ZkPathSample {

    final static String path = "/curator_zkpath_sample";

    static CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("localhost:2181")
            .sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 5)).build();

    public static void main(String args[]) throws Exception {
        // start cliient
        curatorFramework.start();
        
        // Get zookeeper cliengt 
        ZooKeeper zookeeper = curatorFramework.getZookeeperClient().getZooKeeper();

        // create path
        System.out.println(ZKPaths.fixForNamespace(path, "/sub"));
        System.out.println(ZKPaths.makePath(path, "sub"));
        System.out.println(ZKPaths.getNodeFromPath(path + "/sub1"));
        PathAndNode pathAndNode = ZKPaths.getPathAndNode(path + "/sub1");
        System.out.println("node" + pathAndNode.getNode());
        System.out.println("path" + pathAndNode.getPath());
        String dirOne = path + "/childOne";
        String dirTwo = path + "/childTwo";
        ZKPaths.mkdirs(zookeeper, dirOne);
        ZKPaths.mkdirs(zookeeper, dirTwo);
        System.out.println(ZKPaths.getSortedChildren(zookeeper, path));

        ZKPaths.deleteChildren(zookeeper, path, true);

    }

}