package org.noahsark.rpc.mq.nats.manager;

import org.noahsark.rpc.common.remote.Response;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.util.CollectionsUtils;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.mq.MqMultiRequest;
import org.noahsark.rpc.mq.MqMultiResult;
import org.noahsark.rpc.mq.common.MqCommonConstants;
import org.noahsark.rpc.mq.common.MqResultConstants;
import org.noahsark.rpc.mq.nats.NatsmqProxy;
import org.noahsark.rpc.mq.nats.NatsmqProxyHolder;
import org.noahsark.rpc.mq.nats.pojo.EndUser;
import org.noahsark.rpc.mq.nats.pojo.MultiResults;
import org.noahsark.rpc.mq.nats.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息下发管理器
 *
 * @author zhangxt
 * @date 2024/05/11 20:02
 **/
public class MqCmdManager {

    private static Logger log = LoggerFactory.getLogger(MqCmdManager.class);

    /**
     * 向多个用户发送命令，且等待结果（建议用户不超过5个）
     *
     * @param users        用户列表
     * @param cmd          命令
     * @param data         数据
     * @param topic        发送topic
     * @param repliedTopic 接收topic
     * @return 响应结果
     */
    public static MultiResults sendSync(List<EndUser> users, int cmd, Object data,
                                        String topic, String repliedTopic) {
        MultiResults multiResults = new MultiResults();

        try {

            if (CollectionsUtils.isEmpty(users)) {
                multiResults.setCode(MqResultConstants.USER_NOT_ONLINE_CODE);
                multiResults.setMsg(MqResultConstants.USER_NOT_ONLINE_MSG);

                return multiResults;
            }

            log.info("sendSync multiUsers:{}", JsonUtils.toJson(users));

            MqMultiRequest msg = buildMultiRequest(users, cmd, RpcCommand.REQUEST, data, topic, repliedTopic);

            NatsmqProxy proxy = NatsmqProxyHolder.vsspMqProxy;

            log.info("Send a multi request:{}/{}", cmd, JsonUtils.toJson(msg));
            Object response = proxy.sendSync(msg, MqCommonConstants.RPC_TIMEOUT_MILLIS);
            log.info("Receive a multi response:{}", JsonUtils.toJson(response));

            MqMultiResult mqMultiResult = null;
            if (response != null) {
                mqMultiResult = convertToMultiResult(response);
            }

            if (mqMultiResult == null) {
                multiResults.setCode(MqResultConstants.REQUEST_NO_RESULT_CODE);
                multiResults.setMsg(MqResultConstants.REQUEST_NO_RESULT_MSG);

                return multiResults;
            }

            multiResults = toMultiResult(mqMultiResult);

        } catch (Exception ex) {
            log.warn("Catch an exception in sendSync.", ex);

            multiResults.setCode(MqResultConstants.REQUEST_EXCEPTION_CODE);
            multiResults.setMsg(MqResultConstants.REQUEST_EXCEPTION_MSG);
        }

        return multiResults;
    }

    /**
     * 单向发送命令，不用等待结果
     *
     * @param userList     用户列表
     * @param cmd          命令
     * @param data         数据
     * @param topic        发送topic
     * @param repliedTopic 接收topic
     * @return 响应结果
     */
    public static MultiResults sendOneway(List<EndUser> userList, int cmd, Object data,
                                          String topic, String repliedTopic) {
        MultiResults multiResults = new MultiResults();

        try {

            log.info("sendOneway multiUsers:{}", JsonUtils.toJson(userList));

            if (userList.isEmpty()) {
                multiResults.setCode(MqResultConstants.USER_NOT_ONLINE_CODE);
                multiResults.setMsg(MqResultConstants.USER_NOT_ONLINE_MSG);

                return multiResults;
            }

            MqMultiRequest msg = buildMultiRequest(userList, cmd, RpcCommand.ONEWAY, data, topic, repliedTopic);

            NatsmqProxy proxy = NatsmqProxyHolder.vsspMqProxy;

            log.info("Send a multi one-way request:{}/{}", cmd, JsonUtils.toJson(msg));

            proxy.sendOneway(msg);

        } catch (Exception ex) {
            log.warn("Catch an exception in sendSync.", ex);

            multiResults.setCode(MqResultConstants.REQUEST_EXCEPTION_CODE);
            multiResults.setMsg(MqResultConstants.REQUEST_EXCEPTION_MSG);
        }

        return multiResults;
    }

    /**
     * 向单个用户发送命令，不用等待结果
     *
     * @param user         用户列表
     * @param cmd          命令
     * @param data         数据
     * @param topic        发送topic
     * @param repliedTopic 接收topic
     * @return 响应结果
     */
    public static Result sendOneway(EndUser user, int cmd, Object data, String topic, String repliedTopic) {
        Result result = new Result();

        try {

            List<EndUser> users = new ArrayList<>();
            users.add(user);

            MqMultiRequest msg = buildMultiRequest(users, cmd, RpcCommand.ONEWAY, data, topic, repliedTopic);

            NatsmqProxy proxy = NatsmqProxyHolder.vsspMqProxy;

            log.info("Send a one-way request:{}/{}", cmd, data);
            proxy.sendOneway(msg);

        } catch (Exception ex) {
            log.warn("Catch an exception in sendSync.", ex);

            result.setCode(MqResultConstants.REQUEST_EXCEPTION_CODE);
            result.setMsg(MqResultConstants.REQUEST_EXCEPTION_MSG);
        }

        return result;
    }

    /**
     * 向单个用户发送命令，且等待结果
     *
     * @param user         用户列表
     * @param cmd          命令
     * @param data         数据
     * @param topic        发送topic
     * @param repliedTopic 接收topic
     * @return 响应结果
     */
    public Result sendSync(EndUser user, int cmd, Object data, String topic, String repliedTopic) {

        Result result = new Result();

        try {

            List<EndUser> users = new ArrayList<>();
            users.add(user);

            MqMultiRequest msg = buildMultiRequest(users, cmd, RpcCommand.REQUEST, data, topic, repliedTopic);

            NatsmqProxy proxy = NatsmqProxyHolder.vsspMqProxy;

            log.info("Send a request:{}/{}", cmd, data);
            Object response = proxy.sendSync(msg, MqCommonConstants.RPC_TIMEOUT_MILLIS);
            log.info("Receive a response:{}", JsonUtils.toJson(response));

            MqMultiResult mqMultiResult = null;
            if (response != null) {
                mqMultiResult = convertToMultiResult(response);
            }

            if (mqMultiResult == null) {
                result.setCode(-1);
                result.setMsg("No result from client.");

                return result;
            }

            Map<String, Response> failResults = mqMultiResult.getFailResults();
            if (failResults.size() > 0) {
                result.setCode(MqResultConstants.REQUEST_NO_RESULT_CODE);
                result.setMsg(MqResultConstants.REQUEST_NO_RESULT_MSG);

                return result;
            }

        } catch (Exception ex) {
            log.warn("Catch an exception in sendSync.", ex);

            result.setCode(MqResultConstants.REQUEST_EXCEPTION_CODE);
            result.setMsg(MqResultConstants.REQUEST_EXCEPTION_MSG);
        }

        return result;
    }

    private static MqMultiRequest buildMultiRequest(List<EndUser> users, int cmd,
                                                    byte cmdType, Object data, String topic, String localTopic) {

        List<MqMultiRequest.EndUser> multiUsers = new ArrayList<>();

        users.forEach(user -> {
            MqMultiRequest.EndUser endUser = new MqMultiRequest.EndUser();
            endUser.setUserId(user.getUserId());
            endUser.setType(user.getType());
            endUser.setClientType(user.getClientType());

            multiUsers.add(endUser);
        });

        MqMultiRequest.Builder builder = new MqMultiRequest.Builder();
        MqMultiRequest request = builder.cmd(cmd)
                .topic(topic)
                .repliedTopic(localTopic)
                .type(cmdType)
                .data(data)
                .targets(multiUsers).build();

        return request;

    }

    private static MqMultiResult convertToMultiResult(Object origin) {

        String middle = JsonUtils.toJson(((Response) origin).getData());

        MqMultiResult result = JsonUtils.fromJson(middle, MqMultiResult.class);

        return result;
    }

    private static MultiResults toMultiResult(MqMultiResult mqMultiResult) {

        MultiResults multiResults = new MultiResults();
        Map<String, Response> successResults = mqMultiResult.getSuccessResults();
        Map<String, Result> newSuccessResults = new HashMap<>();
        successResults.forEach((key, value) -> {
            newSuccessResults.put(key, new Result());
        });
        multiResults.setSuccessResults(newSuccessResults);

        Map<String, Response> failResults = mqMultiResult.getFailResults();
        Map<String, Result> newFailResults = new HashMap<>();
        failResults.forEach((key, value) -> {
            Result result = new Result();
            result.setCode(value.getCode());
            result.setMsg(value.getMsg());

            newFailResults.put(key, result);
        });
        multiResults.setFailResults(newFailResults);

        return multiResults;
    }

}


