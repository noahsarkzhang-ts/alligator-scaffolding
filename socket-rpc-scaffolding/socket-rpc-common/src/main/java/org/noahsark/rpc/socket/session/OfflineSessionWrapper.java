package org.noahsark.rpc.socket.session;

/**
 * 下线会话包装器
 *
 * @author zhangxt
 * @date 2024/06/29 16:18
 **/
public class OfflineSessionWrapper implements Comparable<OfflineSessionWrapper> {

    /**
     * 下线时间
     */
    private long ts;

    /**
     * 会话
     */
    private Session session;

    public OfflineSessionWrapper() {
    }

    public OfflineSessionWrapper(long ts, Session session) {
        this.ts = ts;
        this.session = session;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public int compareTo(OfflineSessionWrapper other) {
        return Long.valueOf(this.ts - other.ts).intValue();
    }
}
