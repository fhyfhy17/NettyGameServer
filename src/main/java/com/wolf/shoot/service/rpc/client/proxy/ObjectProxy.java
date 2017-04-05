package com.wolf.shoot.service.rpc.client.proxy;

import com.wolf.shoot.common.constant.Loggers;
import com.wolf.shoot.manager.LocalMananger;
import com.wolf.shoot.service.net.RpcRequest;
import com.wolf.shoot.service.rpc.client.RpcContextHolder;
import com.wolf.shoot.service.rpc.client.RpcContextHolderObject;
import com.wolf.shoot.service.rpc.client.RpcServiceDiscovery;
import com.wolf.shoot.service.rpc.client.AbstractRpcConnectManager;
import com.wolf.shoot.service.rpc.client.RPCFuture;
import com.wolf.shoot.service.rpc.client.net.RpcClient;
import org.slf4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

public class ObjectProxy<T> implements InvocationHandler{
    private Logger logger = Loggers.rpcLogger;
    private Class<T> clazz;

    public ObjectProxy(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //基本上用不到equql, hashcode, tostring等底层函数
//        if (Object.class == method.getDeclaringClass()) {
//            String name = method.getName();
//            if ("equals".equals(name)) {
//                return proxy == args[0];
//            } else if ("hashCode".equals(name)) {
//                return System.identityHashCode(proxy);
//            } else if ("toString".equals(name)) {
//                return proxy.getClass().getName() + "@" +
//                        Integer.toHexString(System.identityHashCode(proxy)) +
//                        ", with InvocationHandler " + this;
//            } else {
//                throw new IllegalStateException(String.valueOf(method));
//            }
//        }

        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        if(logger.isDebugEnabled()) {
            // Debug
            logger.debug(method.getDeclaringClass().getName());
            logger.debug(method.getName());
            for (int i = 0; i < method.getParameterTypes().length; ++i) {
                logger.debug(method.getParameterTypes()[i].getName());
            }
            for (int i = 0; i < args.length; ++i) {
                logger.debug(args[i].toString());
            }
        }

        RpcContextHolderObject rpcContextHolderObject = RpcContextHolder.getContext();
        RpcServiceDiscovery rpcServiceDiscovery = LocalMananger.getInstance().getLocalSpringServiceManager().getRpcServiceDiscovery();
        AbstractRpcConnectManager abstractRpcConnectManager = rpcServiceDiscovery.getRpcConnectMannger(rpcContextHolderObject.getBoEnum());
        RpcClient rpcClient = abstractRpcConnectManager.chooseClient(rpcContextHolderObject.getServerId());
        RPCFuture rpcFuture = rpcClient.sendRequest(request);
        return rpcFuture.get();
    }
}
