package org.noahsark.rpc.socket.eventbus;

import java.io.Serializable;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class EventObject implements Serializable {

    private Object source;

    private long timestamp = System.currentTimeMillis();

    public EventObject(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
