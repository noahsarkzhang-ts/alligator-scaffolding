package org.noahsark.tcp;

import org.junit.Test;
import org.noahsark.tcp.client.TcpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Tcp客户端测试类
 *
 * @author zhangxt
 * @date 2025/03/16 12:24
 **/
public class TcpClientTest {

    private static Logger log = LoggerFactory.getLogger(TcpClientTest.class);

    @Test
    public void testClient() throws InterruptedException {
        TcpClient client = new TcpClient("localhost", 9090);

        client.connect();

        TimeUnit.SECONDS.sleep(5);

        String msg = "{\"id\":\"1212\"}";
        client.sendMessage(msg);

        log.info("Send msg:{}", msg);

        TimeUnit.HOURS.sleep(1);
    }
}
