package org.noahsark.rpc.common.dispatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class Dispatcher {

    private Map<String, AbstractProcessor> processors = new HashMap<>();

    /*private static class DispatcherHolder {
        private static final Dispatcher instance = new Dispatcher();
    }*/

    public Dispatcher() {}

    /*public static Dispatcher getInstance() {
        return DispatcherHolder.instance;
    }*/

    public AbstractProcessor getProcessor(String name) {
        return processors.get(name);
    }

    public void register(String name, AbstractProcessor processor) {
        processors.put(name, processor);
    }

    public void unregister(String name) {
        processors.remove(name);
    }

}
