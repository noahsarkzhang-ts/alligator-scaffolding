package org.noahsark.ws;

import org.junit.Test;
import org.noahsark.ws.server.WebSocketServer;

import java.util.concurrent.TimeUnit;

/**
 * Ws Server测试类
 *
 * @author zhangxt
 * @date 2025/03/16 17:51
 **/
public class WsServerTest {

    @Test
    public void testServer() throws InterruptedException {
        WebSocketServer server = new WebSocketServer("localhost", 9090);

        server.start();

        TimeUnit.HOURS.sleep(1);
    }
}
