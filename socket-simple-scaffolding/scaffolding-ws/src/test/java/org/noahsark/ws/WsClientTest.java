package org.noahsark.ws;

import org.junit.Test;
import org.noahsark.ws.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Ws client测试类
 *
 * @author zhangxt
 * @date 2025/03/16 17:50
 **/
public class WsClientTest {

    private static Logger log = LoggerFactory.getLogger(WsClientTest.class);

    @Test
    public void testClient() throws InterruptedException {

        String serverUrl = "ws://localhost:9090/websocket";

        WebSocketClient client = new WebSocketClient(serverUrl);

        client.connect();

        TimeUnit.SECONDS.sleep(5);

        String msg = "{\"id\":\"1212\"}";
        client.sendMessage(msg);

        log.info("Send message:{}", msg);

        TimeUnit.HOURS.sleep(1);
    }
}
