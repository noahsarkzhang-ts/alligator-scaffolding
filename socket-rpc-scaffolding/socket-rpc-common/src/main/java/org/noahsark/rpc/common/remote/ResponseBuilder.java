package org.noahsark.rpc.common.remote;

/**
 * 构造响应接口
 *
 * @author zhangxt
 * @date 2025/03/21 20:55
 **/
public interface ResponseBuilder {

    /**
     * 构造响应消息
     * @param code 返回值
     * @param msg 描述
     * @return Response对象
     */
    Response build(int code, String msg);
}
