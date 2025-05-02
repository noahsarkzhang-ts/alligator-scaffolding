package org.noahsark.ws.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noahsark.common.constant.ClientTypeConstants;
import org.noahsark.event.PushGpsInfoEvent;
import org.noahsark.gw.ws.WsGwServerApp;
import org.noahsark.nats.configuration.NatsConfiguraion;
import org.noahsark.online.manager.OnlineManager;
import org.noahsark.online.pojo.dto.EndUser;
import org.noahsark.online.pojo.dto.MultiResults;
import org.noahsark.rpc.common.constant.Cmd;
import org.noahsark.rpc.common.dispatcher.Dispatcher;
import org.noahsark.rpc.common.dispatcher.DispatcherFactory;
import org.noahsark.rpc.common.dispatcher.WorkQueue;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.mq.nats.NatsmqProxy;
import org.noahsark.rpc.mq.nats.NatsmqProxyHolder;
import org.noahsark.rpc.mq.nats.NatsmqTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 推送事件测试类
 *
 * @author zhangxt
 * @date 2025/05/02 17:39
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WsGwServerApp.class)
public class PushEventTest {

    @Autowired
    private NatsConfiguraion natsConfig;
    @Autowired
    private OnlineManager onlineManager;

    @Value("${mq.topic.rpc}")
    private String rpcTopic;

    @Before
    public void setup() {
        WorkQueue workQueue = new WorkQueue();
        workQueue.setMaxQueueNum(100);
        workQueue.setMaxThreadNum(2);
        workQueue.init("mq-rpc");

        String dispatcherName = "mq-rpc";
        Dispatcher dispatcher = DispatcherFactory.getDispatcher(dispatcherName);

        // 初始化 MQ RPC Proxy
        if (natsConfig.isEnable()) {
            List<NatsmqTopic> topics = new ArrayList<>();
            NatsmqTopic topic = new NatsmqTopic();
            // TODO 读取配置文件
            topic.setTopic(rpcTopic);
            topics.add(topic);

            NatsmqProxy mqProxy = new NatsmqProxy(topics, workQueue, dispatcher);
            mqProxy.start();

            NatsmqProxyHolder.natsMqProxy = mqProxy;

            Runtime.getRuntime().addShutdownHook(new Thread(() -> mqProxy.shutdown()));
            log.info("start rpc mqProxy server!!!");
        } else {
            log.info("RPC mqProxy server is not started, because nats is disable.");
        }

    }

    @Test
    public void pushGpsInfo() {
        EndUser user = new EndUser();
        user.setUserId("1821383295201558529");
        user.setType((short) 3);
        user.setClientType(ClientTypeConstants.CLIENT_TYPE_WEB);

        List<EndUser> list = new ArrayList<>();
        list.add(user);

        PushGpsInfoEvent event = new PushGpsInfoEvent();
        event.setTs(System.currentTimeMillis());
        event.setSubjectId("10001");
        event.setType((short) 1);
        event.setLat(BigDecimal.valueOf(45.5));
        event.setLon(BigDecimal.valueOf(50.50));

        MultiResults results = onlineManager.sendSync(list, Cmd.CMD_PUSH_GPS, event);
        log.info("results: {}", JsonUtils.toJson(results));
    }
}
