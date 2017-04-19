package com.wolf.shoot.net.client.rpc;

import com.wolf.shoot.TestStartUp;
import com.wolf.shoot.common.constant.BOEnum;
import com.wolf.shoot.service.rpc.client.*;
import com.wolf.shoot.service.rpc.client.proxy.AsyncRpcProxy;
import com.wolf.shoot.service.rpc.service.client.HelloService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;

/**
 * Created by jwp on 2017/3/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:bean/*.xml")
public class HelloCallbackTest {

    @Autowired
    private RpcProxyService rpcProxyService;

    @Before
    public void init() {
        TestStartUp.startUp();
    }

    @Test
    public void test() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        try {
            AsyncRpcProxy proxy = (AsyncRpcProxy) rpcProxyService.createAsync(HelloService.class);
            RpcContextHolderObject rpcContextHolderObject = new RpcContextHolderObject(BOEnum.WORLD, 8001);
            RpcContextHolder.setContextHolder(rpcContextHolderObject);
            RPCFuture rpcFuture = proxy.call("hello", "xiaoming");
            rpcFuture.addCallback(new AsyncRPCCallback() {
                @Override
                public void success(Object result) {
                    System.out.println(result);
                    countDownLatch.countDown();
                }

                @Override
                public void fail(Exception e) {
                    System.out.println(e);
                    countDownLatch.countDown();
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("End");
    }

    @After
    public void setTear() {
        if (rpcProxyService != null) {
            try {
                rpcProxyService.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
