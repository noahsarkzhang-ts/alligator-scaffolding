package org.noahsark.rpc.mq.nats.manager;

import com.google.common.base.Preconditions;
import io.nats.client.*;
import org.noahsark.rpc.mq.common.StringMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * nats 管理类
 *
 * @author zhangxt
 * @date 2024/03/15 14:52
 **/
public class NatsManager {

    private static Logger log = LoggerFactory.getLogger(NatsManager.class);

    private ExecutorService executor = Executors.newFixedThreadPool(10);

    private Map<String, StringMessageHandler> subscribers = new ConcurrentHashMap<>();

    private Map<String, Subscription> subscriptions = new ConcurrentHashMap<>();

    private String servers;

    private Connection nc;

    /**
     * 状态，1:未启动，2:启动
     */
    private int status = 1;

    private NatsManager() {
    }

    private static class NatsManagerHolder {
        private static final NatsManager INSTANCE = new NatsManager();
    }

    public static NatsManager getInstance() {
        return NatsManagerHolder.INSTANCE;
    }

    public synchronized void connect(Properties props) {

        try {
            servers = props.get("io.nats.client.servers").toString();
            Preconditions.checkNotNull(servers);

            Options options = new Options.Builder()
                    .properties(props)
                    .connectionListener(new ZasafeConnectionListener())
                    .errorListener(new ZasafeErrorHandler())
                    .maxReconnects(-1)
                    .build();

            nc = Nats.connectReconnectOnConnect(options);

            // 添加订阅
            addSubscribers();

            // 启动
            status = 2;

            log.info("Connect nats server successfully:{}", servers);

        } catch (Exception ex) {
            log.error("catch an exception when connecting servers.", ex);
        }
    }


    public void publish(String subject, byte[] body) {
        Preconditions.checkNotNull(nc);
        nc.publish(subject, body);

    }

    public synchronized void register(String subject, StringMessageHandler handler) {

        if (subscribers.containsKey(subject)) {
            return;
        }

        subscribers.put(subject, handler);
        // 启动之后
        if (status == 2) {
            this.subscribe(subject, handler);
        }
    }

    public void subscribe(String subject, StringMessageHandler handler) {
        Preconditions.checkNotNull(nc);
        Dispatcher dispatcher = nc.createDispatcher((msg) -> {
        });

        log.info("Start subscribing to a topic:{}", subject);

        Subscription subscription = dispatcher.subscribe(subject, (msg) -> {
            final String data = new String(msg.getData(), StandardCharsets.UTF_8);

            log.info("Receive an message:{},{}", msg.getSubject(), data);

            executor.execute(() -> {
                try {
                    handler.onMessage(data);
                } catch (Exception ex) {
                    log.error("Catch an exception.", ex);
                }
            });
        });

        subscriptions.put(subject, subscription);

    }

    private void addSubscribers() {
        if (subscribers.isEmpty()) {
            log.warn("No subscribers..");
            return;
        }

        subscribers.forEach((key, handler) -> {
            this.subscribe(key, handler);
        });
    }

    private static class ZasafeConnectionListener implements ConnectionListener {

        @Override
        public void connectionEvent(Connection connection, Events events) {
            log.info("Connection message:{}", events);
        }
    }

    private static class ZasafeErrorHandler implements ErrorListener {
        @Override
        public void errorOccurred(Connection conn, String error) {
            log.error("Catch an error:{}", error);
        }

        @Override
        public void exceptionOccurred(Connection conn, Exception exp) {
            log.error("Catch an exception:" + exp.getMessage(), exp);
        }
    }


}
