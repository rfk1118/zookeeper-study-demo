package chapter05.zkclient;

import org.I0Itec.zkclient.ZkClient;

public class CreateSession {

    public static void main(String[] args) {
        ZkClient client = new ZkClient("localhost:2181",5000);
        System.out.println(client);
    }
}
