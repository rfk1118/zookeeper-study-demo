package chapter05.recipes;

import org.apache.curator.test.TestingCluster;
import org.apache.curator.test.TestingZooKeeperServer;

import java.util.concurrent.TimeUnit;

public class TestingClusterDemo {


    public static void main(String[] args) throws Exception {

        TestingCluster cluster = new TestingCluster(7);
        cluster.start();

        TimeUnit.SECONDS.sleep(10);
        TestingZooKeeperServer leader = null;
        for (TestingZooKeeperServer server : cluster.getServers()) {
            System.out.println(server.getInstanceSpec().getServerId());
            System.out.println(server.getQuorumPeer().getServerState());

            System.out.println(server.getInstanceSpec().getDataDirectory().getAbsolutePath());

            if ("leading".equals(server.getQuorumPeer().getServerState())) {
                leader = server;
            }
        }

        leader.kill();
        System.err.println("leader kill");

        // leaderelection  ......
        for (TestingZooKeeperServer server : cluster.getServers()) {
            System.out.println(server.getInstanceSpec().getServerId());
            System.out.println(server.getQuorumPeer().getServerState());
            System.out.println(server.getInstanceSpec().getDataDirectory().getAbsolutePath());
        }

        TimeUnit.SECONDS.sleep(10);

        System.err.println("leaderelection");

        for (TestingZooKeeperServer server : cluster.getServers()) {
            System.out.println(server.getInstanceSpec().getServerId());
            System.out.println(server.getQuorumPeer().getServerState());
            System.out.println(server.getInstanceSpec().getDataDirectory().getAbsolutePath());
        }

        TimeUnit.SECONDS.sleep(10);

        cluster.stop();


    }
}