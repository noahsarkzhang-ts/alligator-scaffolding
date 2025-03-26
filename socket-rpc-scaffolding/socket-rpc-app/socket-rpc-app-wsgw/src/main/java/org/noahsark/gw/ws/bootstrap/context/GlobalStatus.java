package org.noahsark.gw.ws.bootstrap.context;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class GlobalStatus {

    private int load;

    private static class GlobalStatusHolder {
        private static final GlobalStatus INSTANCE = new GlobalStatus();
    }

    private GlobalStatus() {
        this.load = 0;
    }

    public static GlobalStatus getInstance() {
        return GlobalStatusHolder.INSTANCE;
    }

    public synchronized void increment() {
        load++;
    }

    public synchronized void decrement() {
        load--;
    }

    public synchronized int get() {
        return load;
    }
}
