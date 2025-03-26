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

package org.noahsark.rpc.socket.ws.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.noahsark.rpc.common.dispatcher.Dispatcher;
import org.noahsark.rpc.common.dispatcher.WorkQueue;
import org.noahsark.rpc.socket.hander.ConnectionInactiveHandler;
import org.noahsark.rpc.socket.hander.ServerBizServiceHandler;
import org.noahsark.rpc.socket.hander.ServerIdleStateTrigger;
import org.noahsark.rpc.socket.remote.AbstractRemotingServer;
import org.noahsark.rpc.socket.remote.RemoteOption;
import org.noahsark.rpc.socket.ws.handler.WebSocketIndexPageHandler;
import org.noahsark.rpc.socket.ws.handler.WebsocketDecoder;
import org.noahsark.rpc.socket.ws.handler.WebsocketEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.List;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    private static Logger log = LoggerFactory.getLogger(WebSocketServerInitializer.class);

    private static final String WEBSOCKET_PATH = "/websocket";

    private SSLContext sslCtx;

    private WorkQueue workQueue;

    private List<ChannelHandler> preHandlers;

    private Dispatcher dispatcher;

    /**
     * 构造函数
     *
     * @param server server
     */
    public WebSocketServerInitializer(AbstractRemotingServer server) {
        try {
            if (server.option(RemoteOption.SSL_ENABLE)) {
                try (InputStream keyStore = new FileInputStream(server.option(RemoteOption.SSL_KEY_FILE))) {
                    KeyStore ks = KeyStore.getInstance("JKS");
                    ks.load(keyStore, server.option(RemoteOption.SSL_KEY_PWD).toCharArray());

                    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                    kmf.init(ks, server.option(RemoteOption.SSL_KEY_PWD).toCharArray());

                    sslCtx = SSLContext.getInstance("TLSv1");
                    sslCtx.init(kmf.getKeyManagers(), null, null);
                } catch (Exception ex) {
                    log.error("Create ssl Context error:", ex);
                    throw ex;
                }
            } else {
                sslCtx = null;
            }

            workQueue = server.getWorkQueue();
            preHandlers = server.getPreHandlers();
            dispatcher = server.getDispatcher();

        } catch (Exception ex) {
            log.info("catch an exception!", ex);
        }
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (sslCtx != null) {
            SSLEngine sslEngine = sslCtx.createSSLEngine();
            sslEngine.setUseClientMode(false);
            sslEngine.setNeedClientAuth(false);
            pipeline.addLast(new SslHandler(sslEngine));
        }
        pipeline.addLast(new IdleStateHandler(30, 0, 0));
        pipeline.addLast(new ServerIdleStateTrigger());
        pipeline.addLast(new ConnectionInactiveHandler());
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true, 65536,
                false, false, false, 10000L));
        pipeline.addLast(new WebSocketIndexPageHandler(WEBSOCKET_PATH));
        pipeline.addLast(new WebsocketDecoder());
        pipeline.addLast(new WebsocketEncoder());

        if (preHandlers != null && preHandlers.size() > 0) {
            preHandlers.forEach(handler -> pipeline.addLast(handler));
        }

        pipeline.addLast(new ServerBizServiceHandler(workQueue, dispatcher));
    }
}
