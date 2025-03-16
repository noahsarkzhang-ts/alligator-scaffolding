package org.noahsark.tcp.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.noahsark.socket.handler.ClientBizServiceHandler;
import org.noahsark.socket.remote.AbstractRemotingClient;
import org.noahsark.tcp.handler.CommandDecoder;
import org.noahsark.tcp.handler.CommandEncoder;

/**
 * 客户端处理器初始化类
 *
 * @author zhangxt
 * @date 2021/3/7
 */
public class ClientHandlersInitializer extends ChannelInitializer<SocketChannel> {

    private AbstractRemotingClient client;

    public ClientHandlersInitializer(AbstractRemotingClient client) {
        this.client = client;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new CommandDecoder());
        pipeline.addLast(new CommandEncoder());
        pipeline.addLast(new ClientBizServiceHandler());
    }
}
