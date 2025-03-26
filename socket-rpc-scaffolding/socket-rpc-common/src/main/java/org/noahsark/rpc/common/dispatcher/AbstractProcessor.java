package org.noahsark.rpc.common.dispatcher;

import com.google.gson.JsonElement;

import org.noahsark.rpc.common.constant.WsConstants;
import org.noahsark.rpc.common.remote.Response;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.remote.RpcContext;
import org.noahsark.rpc.common.remote.RpcRequest;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.common.util.ValidateResult;
import org.noahsark.rpc.common.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */

public abstract class AbstractProcessor<T> implements Runnable {

    private static Logger log = LoggerFactory.getLogger(AbstractProcessor.class);

    private static final int PARAM_ERROR_CODE = 11;

    private RpcRequest request;

    //private Dispatcher dispatcher = Dispatcher.getInstance();

    protected String getProcessName() {
        return ":" + this.getCmd();
    }

    @PostConstruct
    public void register() {
        getDispatcher().register(getProcessName(), this);
    }

    public void register(String dispatcherName) {
        DispatcherFactory.getDispatcher(dispatcherName).register(getProcessName(), this);
    }

    public void unregister() {
        getDispatcher().unregister(getProcessName());
    }

    public void process(RpcRequest request) {
        this.request = request;
        run();
    }

    public void process(RpcRequest request, ExecutorService executorService) {
        this.request = request;
        executorService.execute(this);
    }

    protected RpcRequest getRequest() {
        return this.request;
    }

    @Override
    public void run() {
        RpcRequest rpcRequest = this.getRequest();
        T request = null;
        RpcCommand command = rpcRequest.getRequest();

        try {
            Object params = rpcRequest.getRequest().getData();

            if (!Void.class.equals(getParamsClass())) {
                if (params instanceof JsonElement) {
                    request = JsonUtils.fromJson((JsonElement) params, getParamsClass());
                } /*else if (params.getClass().isArray()) {
                    request = (T) JsonUtils.fromJsonList(params.toString(), getListParamsClass());
                }*/

                // log.debug("receive a request: {}", JsonUtils.toJson(params));
            }
            if (Void.class.equals(getParamsClass()) || checkParam(request, rpcRequest.getContext())) {
                long currentTime = System.currentTimeMillis();
                execute(request, rpcRequest.getContext());
                log.debug("execute used time : {}", System.currentTimeMillis() - currentTime);
            }

        } catch (Exception ex) {
            log.error("catch an exception!", ex);

            Response res = new Response();
            // TODO
//            NatsmqTopic repliedTopic = null;
//            if (command instanceof MqMultiRequest) {
//                repliedTopic = ((MqMultiRequest) command).getRepliedTopic();
//            }
//
//            MqMultiResponse res = new MqMultiResponse.Builder()
//                    .seqId(command.getSeqId())
//                    .cmd(command.getCmd())
//                    .type(RpcCommand.RESPONSE)
//                    .code(Response.FAIL)
//                    .topic(repliedTopic)
//                    .msg(ex.getMessage())
//                    .build();

            rpcRequest.getContext().sendResponse(res);

        }

    }

    /**
     * 参数校验，失败返回
     *
     * @param request 请求参数
     * @param context 请求上下文
     * @return boolean
     */
    protected boolean checkParam(T request, RpcContext context) {

        ValidateResult valid;
        if (request instanceof List) {
            valid = ValidateUtil.validList((List) request);
        } else {
            valid = ValidateUtil.valid(request);
        }
        if (!valid.isSuccess()) {
            context.sendResponse(Response.buildCommonResponse(context.getCommand(), PARAM_ERROR_CODE,
                    valid.getMsg()));
        }
        return valid.isSuccess();
    }

    protected abstract void execute(T request, RpcContext context);

    /**
     * 请求参数的类型，用于Json的反序列化
     *
     * @return 类型
     */
    protected abstract Class<T> getParamsClass();

    /**
     * 请求参数的类型，用于Json的反序列化
     *
     * @return 类型
     */
    protected Class<T> getListParamsClass() {
        return null;
    }

    /**
     * 一个请求由两个部分组成：类名 + 方法名
     *
     * @return 请求对应的类的方法
     */
    protected abstract int getCmd();

    private Dispatcher getDispatcher() {
        String name = getDispatcherName();

        return DispatcherFactory.getDispatcher(name);
    }

    protected String getDispatcherName() {
        return WsConstants.DEFAULT_DISPATCHER_NAME;
    }

}

