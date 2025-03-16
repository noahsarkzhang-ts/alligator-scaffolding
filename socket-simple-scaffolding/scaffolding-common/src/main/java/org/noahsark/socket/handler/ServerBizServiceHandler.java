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
package org.noahsark.socket.handler;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.noahsark.socket.processor.IProcessor;
import org.noahsark.socket.processor.ServerProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 服务器通用处理类
 *
 * @author zhangxt
 * @date 2021/5/13
 */
public class ServerBizServiceHandler extends SimpleChannelInboundHandler<String> {

    private static Logger log = LoggerFactory.getLogger(ServerBizServiceHandler.class);

    public final static ListeningExecutorService service = MoreExecutors.listeningDecorator(newFixedThreadPool());

    private IProcessor processor = new ServerProcessor();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String command) throws Exception {

        try {
            log.info("receive a request: {}", command);

            service.submit(() -> processor.execute(ctx, command));

        } catch (Exception ex) {
            log.warn("catch an exception:{}", ex);
        }

    }

    // 创建自定义业务线程池，用于非阻塞处理长耗时业务
    protected static ExecutorService newFixedThreadPool() {
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("netty-business-%d")
                .setDaemon(true)
                .build();

        return new ThreadPoolExecutor(
                10,// 核心线程数
                20,//线程池中的能拥有的最多线程数
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1000), threadFactory);//1000表示用于缓存任务的阻塞队列，其实理解为最大并发量
    }

}
