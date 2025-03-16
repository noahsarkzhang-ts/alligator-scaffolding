/*
 * Copyright 2014 The Netty Project
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
package org.noahsark.ws.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import org.noahsark.socket.remote.AbstractRemotingClient;
import org.noahsark.socket.remote.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * Websocket 客户端
 *
 * @author zhangxt
 * @date 2021/3/7
 */
public final class WebSocketClient extends AbstractRemotingClient {

    private static Logger log = LoggerFactory.getLogger(WebSocketClient.class);

    public WebSocketClient() {
    }

    public WebSocketClient(String url) {
        super(url);
    }


    @Override
    protected ChannelInitializer<SocketChannel> getChannelInitializer(
            AbstractRemotingClient server) {
        return new WebSocketClientInitializer(this);
    }

    public void sendMessage(WebSocketFrame frame) {
        this.channel.writeAndFlush(frame);
    }

    @Override
    public void sendMessage(String command) {

        this.channel.writeAndFlush(command);
    }

    @Override
    public ServerInfo convert(String url) {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setOriginUrl(url);

        try {

            URI uri = new URI(url);

            String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
            int port = uri.getPort();

            serverInfo.setHost(host);
            serverInfo.setPort(port);
            serverInfo.setUri(uri);

        } catch (Exception ex) {
            log.warn("catch an exception.", ex);

        }

        return serverInfo;
    }

    public static void main(String[] args) throws Exception {

    }
}
