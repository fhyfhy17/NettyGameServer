package com.wolf.shoot.tps.rpc.single;

import com.wolf.shoot.TestStartUp;
import com.wolf.shoot.service.rpc.client.RpcProxyService;
import com.wolf.shoot.tps.rpc.RpcTpsRunable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by jiangwenping on 17/4/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:bean/*.xml")
public class SingleTest {

    @Autowired
    private RpcProxyService rpcProxyService;

    @Before
    public void init(){
        TestStartUp.startUpWithSpring();
    }

    @Test
    public void tps() throws InterruptedException {
        AtomicLong atomicLong = new AtomicLong();
        int size = 10000;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread thread = new Thread(new RpcTpsRunable(rpcProxyService, atomicLong, size, countDownLatch));
        long startTime = System.currentTimeMillis();
        thread.start();
        countDownLatch.await();
        long endTime = System.currentTimeMillis();
        long useTime = endTime - startTime;
        System.out.println("rpc 数量" + atomicLong.get() + "时间" + useTime);
    }

    @After
    public void setTear(){
        if (rpcProxyService != null) {
            try {
                rpcProxyService.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
