package org.noahsark.rpc.mq.nats;

import org.noahsark.rpc.mq.SendCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RabbitMQ 发送回调
 *
 * @author zhangxt
 * @date 2021/9/29
 */
public abstract class NatsmqSendCallback implements SendCallback<NatsmqSendResult> {

    public static class DefaultNatsmqSendCallback extends NatsmqSendCallback {

        private static Logger logger = LoggerFactory.getLogger(DefaultNatsmqSendCallback.class);

        @Override
        public void onSuccess(NatsmqSendResult var1) {
            logger.info("send message successfully: {}", var1.getMsgId());
        }

        @Override
        public void onException(Throwable var1) {
            logger.warn("send message fail.", var1);
        }
    }

}
