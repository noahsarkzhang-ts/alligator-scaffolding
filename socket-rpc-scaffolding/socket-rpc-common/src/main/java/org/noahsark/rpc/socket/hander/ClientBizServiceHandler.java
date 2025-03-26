package org.noahsark.rpc.socket.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.noahsark.rpc.common.dispatcher.Dispatcher;
import org.noahsark.rpc.common.dispatcher.WorkQueue;
import org.noahsark.rpc.common.remote.Request;
import org.noahsark.rpc.common.remote.RequestHandler;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.socket.session.Connection;
import org.noahsark.rpc.socket.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class ClientBizServiceHandler extends SimpleChannelInboundHandler<RpcCommand> {

    private static Logger log = LoggerFactory.getLogger(ClientBizServiceHandler.class);

    private WorkQueue workQueue;

    private Dispatcher dispatcher;

    public ClientBizServiceHandler() {
    }

    public ClientBizServiceHandler(WorkQueue workQueue, Dispatcher dispatcher) {
        this.workQueue = workQueue;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCommand msg) {

        Connection connection = ctx.channel().attr(Connection.CONNECTION).get();
        if (connection == null) {
            log.warn("No connection,requestId : {}", msg.getCmd());
            return;
        }
        log.info("receive msg: {}", msg);

        try {
            Session session = Session.getOrCreatedSession(connection);

            if (msg.getType() == RpcCommand.REQUEST
                    || msg.getType() == RpcCommand.ONEWAY) {
                RequestHandler.processRequest((Request) msg, workQueue, dispatcher, session);
            } else {
                RequestHandler.processResponse(connection, msg);
            }

        } catch (Exception ex) {
            log.warn("catch an exception:", ex);
        }
    }
}
