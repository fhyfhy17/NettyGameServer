package com.wolf.shoot.service.rpc.server;

import com.wolf.shoot.common.annotation.RpcService;
import com.wolf.shoot.common.constant.GlobalConstants;
import com.wolf.shoot.common.constant.Loggers;
import com.wolf.shoot.common.constant.ServiceName;
import com.wolf.shoot.common.util.ClassScanner;
import com.wolf.shoot.manager.LocalMananger;
import com.wolf.shoot.service.IService;
import com.wolf.shoot.service.Reloadable;
import com.wolf.shoot.service.rpc.serialize.protostuff.ProtostuffSerialize;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jwp on 2017/3/7.
 */
@Service
public class RpcMethodRegistry implements Reloadable, IService {

    public static Logger logger = Loggers.serverLogger;

    public ClassScanner messageScanner = new ClassScanner();

    private ConcurrentHashMap<String, Object> registryMap = new ConcurrentHashMap<String, Object>();

    @Override
    public String getId() {
        return ServiceName.RpcMethodRegistry;
    }

    @Override
    public void startup() throws Exception {
        loadPackage(GlobalConstants.MessageCommandConstants.RpcNameSpace,
                GlobalConstants.MessageCommandConstants.Ext);
    }

    @Override
    public void shutdown() throws Exception {

    }

    @Override
    public void reload() throws Exception {
        loadPackage(GlobalConstants.MessageCommandConstants.RpcNameSpace,
                GlobalConstants.MessageCommandConstants.Ext);
    }

    public void loadPackage(String namespace, String ext) throws Exception {
        String[] fileNames = messageScanner.scannerPackage(namespace, ext);
        // 加载class,获取协议命令
        if(fileNames != null) {
            for (String fileName : fileNames) {
                String realClass = namespace
                        + "."
                        + fileName.subSequence(0, fileName.length()
                        - (ext.length()));
                Class<?> messageClass = Class.forName(realClass);

                logger.info("rpc load:" + messageClass);
                String interfaceName = messageClass.getAnnotation(RpcService.class).value().getName();

                ProtostuffSerialize rpcSerialize = LocalMananger.getInstance().getLocalSpringBeanManager().getProtostuffSerialize();
                Object serviceBean = (Object) rpcSerialize.newInstance(messageClass);
                registryMap.put(interfaceName, serviceBean);
            }
        }
    }

    public Object getServiceBean(String className){
        return registryMap.get(className);
    }

}
