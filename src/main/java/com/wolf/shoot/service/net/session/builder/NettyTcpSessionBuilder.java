package com.wolf.shoot.service.net.session.builder;

import com.wolf.shoot.service.net.session.ISession;
import com.wolf.shoot.service.net.session.NettyTcpSession;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Service;

/**
 * Created by jwp on 2017/2/9.
 * 创造tcpsession 同时标记channel上的sessionId
 */
@Service
public class NettyTcpSessionBuilder implements ISessionBuilder {

    public static final AttributeKey<Long> channel_sessionId       = AttributeKey
            .valueOf("channel_sessionId");

    @Override
    public ISession buildSession(Channel channel) {
        NettyTcpSession nettyTcpSession = new NettyTcpSession(channel);
        channel.attr(channel_sessionId).set(nettyTcpSession.getSessionId());
        return nettyTcpSession ;
    }
}
