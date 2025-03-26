package org.noahsark.rpc.socket.eventbus;

import org.noahsark.rpc.common.util.TypeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public final class EventBus {

    private Map<Class<?>, List<ApplicationListener>> listenerMap = new HashMap<>();

    private ExecutorService executor;

    private EventBus() {
        executor = Executors.newFixedThreadPool(5);
    }

    private static class EventBusHolder {
        private static final EventBus INSTANCE = new EventBus();
    }

    public static EventBus getInstance() {
        return EventBusHolder.INSTANCE;
    }

    /**
     * 注册监听器
     *
     * @param listener 监听器
     */
    public void register(ApplicationListener listener) {
        Class<?> classz = TypeUtils.getFirstParameterizedType(listener);
        List<ApplicationListener> listeners = listenerMap.get(classz);

        if (listeners != null) {
            listeners.add(listener);
        } else {
            listeners = new ArrayList<>();
            listeners.add(listener);

            listenerMap.put(classz, listeners);
        }

    }

    /**
     * 发送事件
     *
     * @param event 事件
     */
    public void post(ApplicationEvent event) {
        List<ApplicationListener> listeners = listenerMap.get(event.getClass());

        if (listeners != null) {
            executor.execute(() -> listeners.stream().forEach(listener -> listener.onApplicationEvent(event)));
        }
    }

    /**
     * 关闭执行器
     */
    public void close() {
        if (executor != null) {
            executor.shutdown();
        }
    }

}
