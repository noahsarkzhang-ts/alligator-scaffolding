/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.noahsark.tcp.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.IdleStateHandler;
import org.noahsark.socket.handler.ConnectionInactiveHandler;
import org.noahsark.socket.handler.ServerBizServiceHandler;
import org.noahsark.socket.handler.ServerIdleStateTrigger;
import org.noahsark.socket.remote.AbstractRemotingServer;
import org.noahsark.socket.remote.RemoteOption;
import org.noahsark.tcp.handler.CommandDecoder;
import org.noahsark.tcp.handler.CommandEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TCP 服务器初始化类
 * @author zhangxt
 * @date 2021/4/3
 */
public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {

    private static Logger log = LoggerFactory.getLogger(TcpServerInitializer.class);

    private SslContext sslCtx;

    public TcpServerInitializer(AbstractRemotingServer server) {
        try {
            if (server.option(RemoteOption.SSL_ENABLE)) {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } else {
                sslCtx = null;
            }

        } catch (Exception ex) {
            log.info("catch an exception!", ex);
        }
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
        pipeline.addLast(new IdleStateHandler(300, 0, 0));
        pipeline.addLast(new ServerIdleStateTrigger());
        pipeline.addLast(new ConnectionInactiveHandler());
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new CommandDecoder());
        pipeline.addLast(new CommandEncoder());
        pipeline.addLast(new ServerBizServiceHandler());
    }
}
