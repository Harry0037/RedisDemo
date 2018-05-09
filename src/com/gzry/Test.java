package com.gzry;

import redis.clients.jedis.Jedis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Huan on 2018/5/9.
 */
public class Test {

    public static void main(String[] agrs){
        Jedis jedis = new Jedis("localhost");
        jedis.set("iphoneX", "100");
        jedis.close();

        //多线程
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 1000; i++){
            executorService.execute(new DemoRunable("user"+i));
        }

        executorService.shutdown();
    }

}
