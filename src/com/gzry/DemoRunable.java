package com.gzry;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * Created by Huan on 2018/5/9.
 */
public class DemoRunable implements Runnable{

    private String productKey = "iphoneX";
    private String userName;

    public DemoRunable(String userName){
        this.userName = userName;
    }

    Jedis jedis = new Jedis("localhost");

    @Override
    public void run() {

        jedis.watch(productKey);//watch 监视一个key，当事物执行之前这个key发生了改变，事物会被打断
        String value = jedis.get(productKey);
        int num = Integer.valueOf(value);

        if (num <= 100 && num >= 1){
            //开启事务
            Transaction tx = jedis.multi();
            tx.incrBy(productKey, -1);//减少一个商品数据量
            List<Object> list = tx.exec();//提交本次事务,如果商品数量发生改变，则会返回null
            if (list == null || list.size() == 0){
                System.out.println(userName+"商品抢购失败！");
            }else{
                for (Object su : list){
                    System.out.println(userName+"("+su.toString()+"）商品抢购成功！抢购成功人数:"+(1-(num-100)));
                }
            }
        }
        else{
            System.out.println(userName+"商品抢购完成！");
        }
        jedis.close();
    }
}
