package org.noahsark.gw.ws.bootstrap.main;

import io.netty.channel.ChannelHandler;
import org.noahsark.gw.ws.bootstrap.config.CommonConfig;
import org.noahsark.gw.ws.bootstrap.context.ServerContext;
import org.noahsark.gw.ws.handler.AuthHandler;
import org.noahsark.nats.configuration.NatsConfiguraion;
import org.noahsark.online.service.ISubjectOnlineService;
import org.noahsark.rpc.common.constant.WsConstants;
import org.noahsark.rpc.common.dispatcher.Dispatcher;
import org.noahsark.rpc.common.dispatcher.DispatcherFactory;
import org.noahsark.rpc.common.dispatcher.WorkQueue;
import org.noahsark.rpc.mq.nats.NatsmqProxy;
import org.noahsark.rpc.mq.nats.NatsmqTopic;
import org.noahsark.rpc.socket.remote.RemoteOption;
import org.noahsark.rpc.socket.ws.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
@Component
public class WsGwCommandLineRunner implements CommandLineRunner {

    private static Logger log = LoggerFactory.getLogger(WsGwCommandLineRunner.class);

    @Autowired
    private CommonConfig config;

    @Autowired
    private NatsConfiguraion natsConfig;

    @Autowired
    private ISubjectOnlineService subjectOnlineService;


    @Autowired()
    @Qualifier("commonExecutor")
    private ThreadPoolTaskExecutor commonExecutor;

    @Override
    public void run(String... strings) {

        if (!config.isEnable()) {
            log.info("GW function is not turned on...");
            return;
        }

        String host = config.getServerConfig().getHost();
        String dispatcherName = WsConstants.DEFAULT_DISPATCHER_NAME;

        final WebSocketServer webSocketServer = new WebSocketServer(host, config.getServerConfig().getPort(), dispatcherName);

        webSocketServer
                .option(RemoteOption.THREAD_NUM_OF_QUEUE, config.getWorkQueue().getMaxThreadNum());
        webSocketServer
                .option(RemoteOption.CAPACITY_OF_QUEUE, config.getWorkQueue().getMaxQueueNum());
        webSocketServer
                .option(RemoteOption.SSL_ENABLE, config.getServerConfig().getSslConfig().getEnable());
        webSocketServer
                .option(RemoteOption.SSL_KEY_FILE, config.getServerConfig().getSslConfig().getKeystoreFile());
        webSocketServer
                .option(RemoteOption.SSL_KEY_PWD, config.getServerConfig().getSslConfig().getKeystorePwd());

        // 加入前置处理器，判断设备/用户是否登陆
        ChannelHandler authHandler = new AuthHandler();
        webSocketServer.addPreHandler(authHandler);

        WorkQueue workQueue = new WorkQueue();
        workQueue.setMaxQueueNum(config.getWorkQueue().getMaxQueueNum());
        workQueue.setMaxThreadNum(config.getWorkQueue().getMaxThreadNum());
        workQueue.init("gw");

        webSocketServer.setWorkQueue(workQueue);

        Dispatcher dispatcher = DispatcherFactory.getDispatcher(dispatcherName);
        webSocketServer.setDispatcher(dispatcher);

        webSocketServer.init();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> webSocketServer.shutdown()));

        ServerContext.server = webSocketServer;
        ServerContext.dispatcher = dispatcher;

        webSocketServer.start();
        log.info("Start server!!!");

        // 初始化 MQ RPC Proxy
        if (natsConfig.isEnable()) {
            List<NatsmqTopic> topics = new ArrayList<>();
            NatsmqTopic topic = new NatsmqTopic();
            // TODO 读取配置文件
            topic.setTopic(config.getMqTopic());
            topics.add(topic);

            NatsmqProxy mqProxy = new NatsmqProxy(topics, workQueue, dispatcher);
            mqProxy.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> mqProxy.shutdown()));
            log.info("start GW mqProxy server!!!");
        } else {
            log.info("GW mqProxy server is not started, because nats is disable.");
        }

        // 清空服务器状态
        clearServer();

    }

    private void clearServer() {
        String serverId = config.getServerId();

        commonExecutor.execute(() -> {
            try {
                log.info("execute clear server task:{}", serverId);

                subjectOnlineService.offlineByServerId(serverId);
            } catch (Exception ex) {
                log.error("catch an exception when clear server task.", ex);
            }
        });
    }
}
