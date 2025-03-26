package org.noahsark.rpc.socket.ws.client;

import com.google.gson.JsonParser;
import org.junit.Test;
import org.noahsark.rpc.common.remote.CommandCallback;
import org.noahsark.rpc.common.remote.Request;
import org.noahsark.rpc.common.remote.Response;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.socket.session.Session;
import org.noahsark.rpc.socket.ws.pojo.UserInfo;
import org.noahsark.rpc.socket.ws.server.WebSocketServerTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/22
 */
public class WebSocketClientTest {

    private static Logger logger = LoggerFactory.getLogger(WebSocketClientTest.class);

    @Test
    public void testRequestResponse() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("1002");
        userInfo.setUserName("allen");
        userInfo.setPassword("pwd");

        Request request = new Request.Builder()
                .cmd(1000)
                .type(Response.REQUEST)
                .data(userInfo)
                .seqId(1)
                .build();

        sendRequest(request);
    }

    @Test
    public void testReconnection() {
        String url = System.getProperty("url", "ws://192.168.3.107:9090/websocket");

        WebSocketClient client = new WebSocketClient(url);
        client.connect();

        try {
            TimeUnit.HOURS.sleep(1);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void sendRequest(Request request) {

        String url = System.getProperty("url", "ws://192.168.3.107:9090/websocket");

        WebSocketClient client = new WebSocketClient(url);
        client.registerProcessor(new InviteUserProcessor());

        // case 1:
        client.connect();

        try {

            TimeUnit.SECONDS.sleep(5);

            client.invoke(request, new CommandCallback() {
                @Override
                public void callback(Object result, int currentFanout, int fanout) {

                    logger.info("result = " + ((result instanceof byte[]) ? new JsonParser().parse(new String((byte[]) result)).getAsJsonObject() : result));
                }

                @Override
                public void failure(Throwable cause, int currentFanout, int fanout) {
                    cause.printStackTrace();
                }
            }, 300000);

            logger.info("Send request:{}", JsonUtils.toJson(request));

            TimeUnit.HOURS.sleep(1);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            client.shutdown();
        }
    }

    @Test
    public void multiServerTest() {

        List<String> urls = new ArrayList<>();

        urls.add("ws://192.168.9.103:9090/websocket");
        urls.add("ws://192.168.9.103:9091/websocket");

        WebSocketClient client = new WebSocketClient(urls);

        try {

            client.connect();

            TimeUnit.SECONDS.sleep(120);


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            client.shutdown();
        }
    }

}
