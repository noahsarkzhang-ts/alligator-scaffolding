package org.noahsark.rpc.mq;


import org.noahsark.rpc.common.remote.Response;

import java.util.Map;

/**
 * MQ 多路响应结果
 *
 * @author zhangxt
 * @date 2024/05/09 09:10
 **/
public class MqMultiResult {

    private Map<String, Response> failResults;

    private Map<String, Response> successResults;

    public MqMultiResult() {
    }

    public Map<String, Response> getFailResults() {
        return failResults;
    }

    public void setFailResults(Map<String, Response> failResults) {
        this.failResults = failResults;
    }

    public Map<String, Response> getSuccessResults() {
        return successResults;
    }

    public void setSuccessResults(Map<String, Response> successResults) {
        this.successResults = successResults;
    }
}
