package com.wolf.shoot.service.rpc.service.server;

import com.wolf.shoot.common.annotation.RpcServiceAnnotation;
import com.wolf.shoot.common.annotation.RpcServiceBoEnum;
import com.wolf.shoot.common.constant.BOEnum;
import com.wolf.shoot.service.rpc.service.client.HelloService;
import org.springframework.stereotype.Repository;

/**
 * Created by jwp on 2017/3/7.
 */
@RpcServiceAnnotation(HelloService.class)
@RpcServiceBoEnum(bo = BOEnum.WORLD)
@Repository
public class HelloServiceImpl implements HelloService {

    public String hello(String name) {
        return "Hello! " + name;
    }
}

