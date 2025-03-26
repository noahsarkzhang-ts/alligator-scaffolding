package org.noahsark.online.pojo.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * 多路响应结果
 *
 * @author zhangxt
 * @date 2024/05/11 20:08
 **/
public class MultiResults extends Result {

    private Map<String, Result> failResults = new HashMap<>();

    private Map<String, Result> successResults = new HashMap<>();

    public MultiResults() {
        super();
    }

    public MultiResults(Map<String, Result> failResults, Map<String, Result> successResults) {
        this.failResults = failResults;
        this.successResults = successResults;
    }

    public Map<String, Result> getFailResults() {
        return failResults;
    }

    public void setFailResults(Map<String, Result> failResults) {
        this.failResults = failResults;
    }

    public Map<String, Result> getSuccessResults() {
        return successResults;
    }

    public void setSuccessResults(Map<String, Result> successResults) {
        this.successResults = successResults;
    }
}
