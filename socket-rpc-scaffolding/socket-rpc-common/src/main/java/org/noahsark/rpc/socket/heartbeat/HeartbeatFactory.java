package org.noahsark.rpc.socket.heartbeat;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public interface HeartbeatFactory<T> {

    T getPing();

    default PingPayloadGenerator getPayloadGenerator() {
        return null;
    }

    default void setPayloadGenerator(PingPayloadGenerator payload) {}

}
