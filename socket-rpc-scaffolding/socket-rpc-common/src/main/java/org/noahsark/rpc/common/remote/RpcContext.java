package org.noahsark.rpc.common.remote;

import com.google.common.collect.Maps;
import org.noahsark.rpc.common.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class RpcContext {

    private static Logger log = LoggerFactory.getLogger(RpcPromise.class);

    private ChannelHolder session;

    private RpcCommand command;

    /**
     * 上下文里面的拓展字段
     */
    private Map<String, String> ext = Maps.newHashMap();

    public RpcContext() {
    }

    public RpcContext(Builder builder) {
        this.command = builder.command;
        this.session = builder.session;
    }

    /**
     * 发送响应
     *
     * @param repsponse repsponse
     */
    public void sendResponse(RpcCommand repsponse) {
        log.info("send response:{}", JsonUtils.toJson(repsponse));

        session.write(repsponse);
    }

    public ChannelHolder getSession() {
        return session;
    }

    public void setSession(ChannelHolder session) {
        this.session = session;
    }

    public RpcCommand getCommand() {
        return command;
    }

    public void setCommand(RpcCommand command) {
        this.command = command;
    }

    public Map<String, String> getExt() {
        return ext;
    }

    public static class Builder {
        private ChannelHolder session;

        private RpcCommand command;

        public Builder session(ChannelHolder session) {
            this.session = session;
            return this;
        }

        public Builder command(RpcCommand command) {
            this.command = command;
            return this;
        }

        public RpcContext build() {
            return new RpcContext(this);
        }
    }
}
