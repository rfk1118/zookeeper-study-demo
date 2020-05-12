package chapter05.zkclient;

import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.TimeUnit;

public class ZkclientWithListener {


    public static void main(String[] args) throws Exception {

        String path = "/ZkclientWithListener1";
        ZkClient client = new ZkClient("localhost:2181", 5000);
        client.subscribeChildChanges(path, (s, list) -> System.out.println("path:->" + s + ",children:" + list.toString()));

        client.createPersistent(path);
        TimeUnit.SECONDS.sleep(3);
        System.out.println(client.getChildren(path));
        TimeUnit.SECONDS.sleep(3);

        client.createPersistent(path + "/hellworld");
        TimeUnit.SECONDS.sleep(3);
        client.delete(path + "/hellworld");
        TimeUnit.SECONDS.sleep(3);
        client.delete(path);
        TimeUnit.SECONDS.sleep(3);

    }
}
