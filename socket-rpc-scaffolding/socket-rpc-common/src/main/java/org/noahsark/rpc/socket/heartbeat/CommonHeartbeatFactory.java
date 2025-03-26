package org.noahsark.rpc.socket.heartbeat;

import org.noahsark.rpc.common.remote.Response;
import org.noahsark.rpc.common.remote.RpcCommand;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class CommonHeartbeatFactory implements HeartbeatFactory<RpcCommand> {

    private PingPayloadGenerator payloadGenerator;

    @Override
    public RpcCommand getPing() {

        /*Object payload;
        if (payloadGenerator != null) {
            payload = payloadGenerator.getPayload();
        } else {
            Ping hearBeat = new Ping();
            hearBeat.setLoad(0);

            payload = hearBeat;
        }*/

        RpcCommand command = new RpcCommand.Builder()
                .seqId(1)
                .cmd(10)
                .type(RpcCommand.REQUEST)
                .data(null)
                .build();

        return command;
    }

    @Override
    public PingPayloadGenerator getPayloadGenerator() {
        return this.payloadGenerator;
    }

    @Override
    public void setPayloadGenerator(PingPayloadGenerator payload) {
        this.payloadGenerator = payload;

    }

    /**
     * 根据 Ping 生成 Pong
     *
     * @param ping 心跳
     * @return 响应
     */
    public static RpcCommand getPong(RpcCommand ping) {

        Response command = new Response.Builder()
                .seqId(ping.getSeqId())
                .cmd(ping.getCmd())
                .code(Response.SUCCESS)
                .msg(Response.SUCCESS_MESSAGE)
                .type(RpcCommand.RESPONSE)
                .data(null)
                .build();

        return command;
    }
}
