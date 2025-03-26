package org.noahsark.rpc.mq.nats;

import org.junit.Before;
import org.junit.Test;
import org.noahsark.rpc.common.dispatcher.Dispatcher;
import org.noahsark.rpc.common.dispatcher.DispatcherFactory;
import org.noahsark.rpc.common.dispatcher.WorkQueue;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.mq.MqMultiRequest;
import org.noahsark.rpc.mq.nats.manager.NatsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Natsmq proxy测试类
 * @author zhangxt
 * @date 2025/03/23 14:54
 **/
public class NatsmqSenderProxyTest {

    private static Logger log = LoggerFactory.getLogger(NatsmqSenderProxyTest.class);

    private static final String TOPIC = "app-mq";

    private static final String REPLIED_TOPIC = "app-gw";

    @Before
    public void setup() {
        String servers = "nats://192.168.3.107:4222";

        NatsManager nm = NatsManager.getInstance();
        Properties properties = new Properties();
        properties.put("io.nats.client.servers", servers);

        log.info("Start to connect nats servers:{}", servers);

        // 建立连接
        nm.connect(properties);
    }
    @Test
    public void sendMessage() throws InterruptedException {

        WorkQueue workQueue = new WorkQueue();
        workQueue.setMaxQueueNum(100);
        workQueue.setMaxThreadNum(5);
        workQueue.init("mq");

        String dispatcherName = "mq";
        Dispatcher dispatcher = DispatcherFactory.getDispatcher(dispatcherName);

        NatsmqTopic topic = new NatsmqTopic();
        topic.setTopic(REPLIED_TOPIC);

        List<NatsmqTopic> topics = new ArrayList<>();
        topics.add(topic);

        NatsmqProxy proxy = new NatsmqProxy(topics,workQueue,dispatcher);

        proxy.start();

        List<MqMultiRequest.EndUser> multiUsers = new ArrayList<>();
        MqMultiRequest.EndUser user = new MqMultiRequest.EndUser();
        user.setUserId("10001");
        user.setClientType((short)1);
        user.setType((short) 1);

        multiUsers.add(user);

        String localTopic = "gw";
        Map<String,Object> data = new HashMap<>();
        data.put("id","12222");

        MqMultiRequest request = new MqMultiRequest.Builder()
                .cmd(100)
                .seqId(1)
                .topic(TOPIC)
                .repliedTopic(localTopic)
                .type(RpcCommand.REQUEST)
                .data(data)
                .targets(multiUsers).build();

        proxy.sendOneway(request);

        TimeUnit.HOURS.sleep(1);

    }



}
