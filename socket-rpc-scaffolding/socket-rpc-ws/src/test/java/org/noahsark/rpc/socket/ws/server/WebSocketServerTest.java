package org.noahsark.rpc.socket.ws.server;

import org.junit.Test;
import org.noahsark.rpc.common.dispatcher.Dispatcher;
import org.noahsark.rpc.common.dispatcher.DispatcherFactory;
import org.noahsark.rpc.common.dispatcher.WorkQueue;
import org.noahsark.rpc.socket.ws.pojo.Constants;
import org.noahsark.rpc.socket.ws.processor.UserLoginProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/22
 */
public class WebSocketServerTest {

    private static Logger logger = LoggerFactory.getLogger(WebSocketServerTest.class);

    @Test
    public void testServer() {
        String host = "192.168.3.107";
        int port = 9090;
        String dispatcherName = Constants.DEFAULT_DISPATCHER_NAME;

        // 请求
        // request = {"className":"inviter","method":"login","requestId":1,"version":"V1.0","payload":{"userName":"allan","password":"test"}}

        final WebSocketServer webSocketServer = new WebSocketServer(host, port, dispatcherName);
        // webSocketServer.init();

        WorkQueue workQueue = new WorkQueue();
        workQueue.setMaxQueueNum(100);
        workQueue.setMaxThreadNum(5);
        workQueue.init("gw");
        webSocketServer.setWorkQueue(workQueue);

        Dispatcher dispatcher = DispatcherFactory.getDispatcher(dispatcherName);
        webSocketServer.setDispatcher(dispatcher);

        webSocketServer.init();

        UserLoginProcessor processor = new UserLoginProcessor();
        processor.register();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                webSocketServer.shutdown();
            }
        });

        webSocketServer.start();

        try {
            TimeUnit.MINUTES.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
