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
package org.noahsark.rpc.socket.tcp.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.noahsark.rpc.socket.heartbeat.PingProcessor;
import org.noahsark.rpc.socket.remote.AbstractRemotingServer;

/**
 * TCP 服务器
 *
 * @author zhangxt
 * @date 2021/4/3
 */
public class TcpServer extends AbstractRemotingServer {

    public TcpServer(String host, int port) {
        super(host, port);

        registerDefaultProcessor();

    }

    public TcpServer(String host, int port, String dispatcherName) {
        super(host, port, dispatcherName);

        registerDefaultProcessor();
    }

    @Override
    protected ChannelInitializer<SocketChannel> getChannelInitializer(AbstractRemotingServer server) {
        return new TcpServerInitializer(this);
    }

    private void registerDefaultProcessor() {
        this.registerProcessor(new PingProcessor());
    }

    private void unregisterDefaultProcessor() {
        this.unregisterProcessor(new PingProcessor());
    }

    public static void main(String[] args) {

    }
}
