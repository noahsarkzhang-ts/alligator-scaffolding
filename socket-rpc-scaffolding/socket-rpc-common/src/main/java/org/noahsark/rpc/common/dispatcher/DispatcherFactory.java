package org.noahsark.rpc.common.dispatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * Dispathcer工厂类
 *
 * @author zhangxt
 * @date 2024/12/20 15:07
 **/
public class DispatcherFactory {

    private static Map<String, Dispatcher> dispatcherMap = new HashMap<>();

    public static synchronized Dispatcher getDispatcher(String name) {
        Dispatcher dispatcher = dispatcherMap.get(name);

        if (dispatcher == null) {
            dispatcher = new Dispatcher();
            dispatcherMap.put(name, dispatcher);
        }

        return dispatcher;
    }
}
