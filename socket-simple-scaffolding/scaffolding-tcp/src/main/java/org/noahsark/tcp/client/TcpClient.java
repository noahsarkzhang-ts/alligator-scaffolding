package org.noahsark.tcp.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.noahsark.socket.remote.AbstractRemotingClient;
import org.noahsark.socket.remote.ServerInfo;

/**
 * TCP 客户端
 * @author zhangxt
 * @date 2021/3/7 17:23
 */
public class TcpClient extends AbstractRemotingClient {

    public TcpClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected ChannelInitializer<SocketChannel> getChannelInitializer(
        AbstractRemotingClient server) {
        return new ClientHandlersInitializer(this);
    }

    @Override
    public void sendMessage(String command) {

        this.channel.writeAndFlush(command);
    }

    @Override
    public ServerInfo convert(String url) {

        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setOriginUrl(url);

        String[] parts = url.split(":");

        serverInfo.setHost(parts[0]);
        serverInfo.setPort(Integer.valueOf(parts[1]));

        return serverInfo;
    }

    public static void main(String[] args) {
        TcpClient tcpClient = new TcpClient("localhost", 2222);
        tcpClient.connect();
    }

}
