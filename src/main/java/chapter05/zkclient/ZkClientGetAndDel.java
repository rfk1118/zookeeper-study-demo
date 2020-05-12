package chapter05.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.TimeUnit;

public class ZkClientGetAndDel {

    public static void main(String[] args) throws Exception{
        String path = "/ZkClientGetAndDel";

        ZkClient client = new ZkClient("localhost:2181", 5000);
        client.createEphemeral(path,"123");
        client.subscribeDataChanges(path, new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("handleDataChange" + s + "," + o);
            }
            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("handleDataDeleted" + s);
            }
        });

        Object o = client.readData(path);
        System.out.println(o);

        client.writeData(path,"123456789");
        TimeUnit.SECONDS.sleep(10);
        client.delete(path);
        TimeUnit.SECONDS.sleep(10);


    }
}
