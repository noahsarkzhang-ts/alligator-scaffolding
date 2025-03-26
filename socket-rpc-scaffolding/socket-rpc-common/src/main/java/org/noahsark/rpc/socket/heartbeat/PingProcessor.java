package org.noahsark.rpc.socket.heartbeat;

import org.noahsark.rpc.common.dispatcher.AbstractProcessor;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.remote.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class PingProcessor extends AbstractProcessor<Void> {

    private static Logger log = LoggerFactory.getLogger(PingProcessor.class);


    @Override
    protected void execute(Void request, RpcContext context) {
        // log.info("Receive a ping message: {}", JsonUtils.toJson(request));

        RpcCommand command = CommonHeartbeatFactory.getPong(context.getCommand());

        context.sendResponse(command);
    }

    @Override
    protected Class<Void> getParamsClass() {
        return Void.class;
    }

    @Override
    protected int getCmd() {
        return 10;
    }
}
