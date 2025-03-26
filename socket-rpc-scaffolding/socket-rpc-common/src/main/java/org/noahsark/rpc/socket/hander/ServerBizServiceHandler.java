/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

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
public class ServerBizServiceHandler extends SimpleChannelInboundHandler<RpcCommand> {

    private static Logger log = LoggerFactory.getLogger(ServerBizServiceHandler.class);

    private WorkQueue workQueue;

    private Dispatcher dispatcher;

    public ServerBizServiceHandler(WorkQueue workQueue, Dispatcher dispatcher) {
        this.workQueue = workQueue;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCommand command) {

        try {
            log.info("receive a request: {}", command);
            Session session = Session.getOrCreatedSession(ctx.channel());
            Connection connection = session.getConnection();

            if (command.getType() == RpcCommand.REQUEST
                    || command.getType() == RpcCommand.ONEWAY) {
                RequestHandler.processRequest((Request) command, workQueue, dispatcher, session);
            } else {
                RequestHandler.processResponse(connection, command);
            }

        } catch (Exception ex) {
            log.warn("catch an exception:", ex);
        }

    }

    public WorkQueue getWorkQueue() {
        return workQueue;
    }

    public void setWorkQueue(WorkQueue workQueue) {
        this.workQueue = workQueue;
    }
}
