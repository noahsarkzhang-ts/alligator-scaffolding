package org.noahsark.rpc.socket.tcp.client;

import com.google.gson.JsonParser;
import org.junit.Test;
import org.noahsark.rpc.common.remote.CommandCallback;
import org.noahsark.rpc.common.remote.Request;
import org.noahsark.rpc.common.remote.Response;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.socket.tcp.pojo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/22
 */
public class TcpClientTest {

    private static Logger logger = LoggerFactory.getLogger(TcpClientTest.class);

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

    private void sendRequest(Request request) {

        String host = "192.168.3.107";
        int port = 9090;

        TcpClient client = new TcpClient(host, port);
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

}
