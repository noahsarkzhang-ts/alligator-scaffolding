package org.noahsark.rpc.socket.heartbeat;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public interface PingPayloadGenerator {
    Object getPayload();
}
