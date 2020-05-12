package chapter05.zkclient;

import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CreateNodePersistent {

    public static void main(String[] args) {
        ZkClient client = new ZkClient("localhost:2181", 5000);
        System.out.println(client);
        String path = "/hello/world";
        client.createPersistent(path + "/helloWorld", true);
        List<String> children = client.getChildren(path);
        System.out.println(children);
        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

