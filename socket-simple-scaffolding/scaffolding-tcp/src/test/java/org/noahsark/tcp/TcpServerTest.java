package org.noahsark.tcp;

import org.junit.Test;
import org.noahsark.tcp.server.TcpServer;

import java.util.concurrent.TimeUnit;

/**
 * Tcp服务器测试类
 *
 * @author zhangxt
 * @date 2025/03/16 12:23
 **/
public class TcpServerTest {
    @Test
    public void testServer() throws InterruptedException {
        TcpServer server = new TcpServer("localhost", 9090);

        server.start();

        TimeUnit.HOURS.sleep(1);
    }

}
