package chapter05.recipes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;


/**
 * 生成订单号为16:59:34 | 305
 * 生成订单号为16:59:34 | 305
 * 生成订单号为16:59:34 | 306
 * 生成订单号为16:59:34 | 305
 * 生成订单号为16:59:34 | 305
 * 生成订单号为16:59:34 | 305
 * 生成订单号为16:59:34 | 305
 * 生成订单号为16:59:34 | 305
 * 生成订单号为16:59:34 | 307
 * 生成订单号为16:59:34 | 308
 */
public class RecipesDemo4 {


    final static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss | SSS");
                    String format = simpleDateFormat.format(new Date());
                    System.out.println("生成订单号为" + format);

                }
            }).start();

            countDownLatch.countDown();
        }
    }
}
