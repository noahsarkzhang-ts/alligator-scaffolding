package org.noahsark.online.manager;

import org.noahsark.common.constant.ClientTypeConstants;
import org.noahsark.common.constant.CommonConstants;
import org.noahsark.common.constant.ResultConstants;
import org.noahsark.online.cache.ServerOnlineCache;
import org.noahsark.online.cache.SubjectOnlineCache;
import org.noahsark.online.configuration.OnlineConfiguration;
import org.noahsark.online.pojo.dto.EndUser;
import org.noahsark.online.pojo.dto.MultiResults;
import org.noahsark.online.pojo.dto.Result;
import org.noahsark.online.pojo.po.ServerOnline;
import org.noahsark.online.pojo.po.SubjectOnline;
import org.noahsark.online.service.IServerOnlineService;
import org.noahsark.online.service.ISubjectOnlineService;
import org.noahsark.rpc.common.remote.Response;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.mq.MqMultiRequest;
import org.noahsark.rpc.mq.MqMultiResult;
import org.noahsark.rpc.mq.nats.NatsmqProxy;
import org.noahsark.rpc.mq.nats.NatsmqProxyHolder;
import org.noahsark.util.CollectionsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 消息下发管理器
 *
 * @author zhangxt
 * @date 2024/05/11 20:02
 **/
@Component
public class OnlineManager {

    private static Logger log = LoggerFactory.getLogger(OnlineManager.class);

    @Autowired
    private SubjectOnlineCache subjectCache;

    @Autowired
    private ServerOnlineCache serverCache;

    @Autowired
    private ISubjectOnlineService subjectOnlineService;

    @Autowired
    private OnlineConfiguration onlineConfiguration;

    @Autowired
    private IServerOnlineService serverOnlineService;

    /**
     * 向多个用户发送命令，且等待结果（建议用户不超过5个）
     *
     * @param userList 用户列表
     * @param cmd      命令
     * @param data     数据
     * @return 响应结果
     */
    public MultiResults sendSync(List<EndUser> userList, int cmd, Object data) {
        MultiResults multiResults = new MultiResults();

        try {
            Map<String, List<EndUser>> multiUsers = demultiplex(userList);

            log.info("sendSync multiUsers:{}", JsonUtils.toJson(multiUsers));

            /*if (multiUsers.size() > 1) {
                multiResults.setCode(ResultConstants.MULTI_SERVER_NOT_SUPPORTED_CODE);
                multiResults.setMsg(ResultConstants.MULTI_SERVER_NOT_SUPPORTED_MSG);

                return multiResults;
            }*/

            if (multiUsers.isEmpty()) {
                multiResults.setCode(ResultConstants.USER_NOT_ONLINE_CODE);
                multiResults.setMsg(ResultConstants.USER_NOT_ONLINE_MSG);

                return multiResults;
            }

            List<String> topics = new ArrayList<>();
            multiUsers.forEach((topic, users) -> {
                topics.add(topic);
            });

            //String topic = topics.get(0);

            // 并发请求
            List<MultiResults> resultList = topics.parallelStream().map(topic -> {

                MultiResults partResults = new MultiResults();

                try {
                    List<EndUser> users = multiUsers.get(topic);

                    MqMultiRequest msg = buildMultiRequest(users, cmd, RpcCommand.REQUEST, data, topic);

                    NatsmqProxy proxy = NatsmqProxyHolder.vsspMqProxy;

                    log.info("Send a multi request:{}/{}", cmd, JsonUtils.toJson(msg));
                    Object response = proxy.sendSync(msg, CommonConstants.RPC_TIMEOUT_MILLIS);
                    log.info("Receive a multi response:{}", JsonUtils.toJson(response));

                    MqMultiResult mqMultiResult = null;
                    if (response != null) {
                        mqMultiResult = convertToMultiResult(response);
                    }

                    if (mqMultiResult == null) {
                        partResults.setCode(ResultConstants.REQUEST_NO_RESULT_CODE);
                        partResults.setMsg(ResultConstants.REQUEST_NO_RESULT_MSG);

                        return partResults;
                    }

                    log.info("mqMultiResult:{}", JsonUtils.toJson(mqMultiResult));
                    partResults = toMultiResult(mqMultiResult);
                    log.info("multiResults:{}", JsonUtils.toJson(multiResults));
                } catch (Exception ex) {
                    log.warn("Catch an exception in sendSync.", ex);
                }

                return partResults;

            }).collect(Collectors.toList());

            // 合并返回结果
            if (CollectionsUtils.isEmpty(resultList)) {
                return multiResults;
            }

            Map<String, Result> failResults = multiResults.getFailResults();
            Map<String, Result> successResults = multiResults.getSuccessResults();

            resultList.forEach(partResults -> {
                failResults.putAll(partResults.getFailResults());
                successResults.putAll(partResults.getSuccessResults());
            });

            return multiResults;

        } catch (Exception ex) {
            log.warn("Catch an exception in sendSync.", ex);

            multiResults.setCode(ResultConstants.REQUEST_EXCEPTION_CODE);
            multiResults.setMsg(ResultConstants.REQUEST_EXCEPTION_MSG);
        }

        return multiResults;

    }

    /**
     * 单向发送命令，不用等待结果
     *
     * @param userList 用户列表
     * @param cmd      命令
     * @param data     数据
     * @return 响应结果
     */
    public MultiResults sendOneway(List<EndUser> userList, int cmd, Object data) {
        MultiResults multiResults = new MultiResults();

        try {
            Map<String, List<EndUser>> multiUsers = demultiplex(userList);

            log.info("sendOneway multiUsers:{}", JsonUtils.toJson(multiUsers));

            /*if (multiUsers.size() > 1) {
                multiResults.setCode(ResultConstants.MULTI_SERVER_NOT_SUPPORTED_CODE);
                multiResults.setMsg(ResultConstants.MULTI_SERVER_NOT_SUPPORTED_MSG);

                return multiResults;
            }*/

            if (multiUsers.isEmpty()) {
                multiResults.setCode(ResultConstants.USER_NOT_ONLINE_CODE);
                multiResults.setMsg(ResultConstants.USER_NOT_ONLINE_MSG);

                return multiResults;
            }

            List<String> topics = new ArrayList<>();
            multiUsers.forEach((topic, users) -> {
                topics.add(topic);
            });

            // String topic = topics.get(0);
            // 并发请求
            topics.parallelStream().map(topic -> {
                List<EndUser> users = multiUsers.get(topic);

                MqMultiRequest msg = buildMultiRequest(users, cmd, RpcCommand.ONEWAY, data, topic);

                NatsmqProxy proxy = NatsmqProxyHolder.vsspMqProxy;

                log.info("Send a multi one-way request:{}/{}", cmd, JsonUtils.toJson(msg));
                proxy.sendOneway(msg);

                return "OK";
            }).collect(Collectors.toList());

        } catch (Exception ex) {
            log.warn("Catch an exception in sendSync.", ex);

            multiResults.setCode(ResultConstants.REQUEST_EXCEPTION_CODE);
            multiResults.setMsg(ResultConstants.REQUEST_EXCEPTION_MSG);
        }

        return multiResults;
    }

    /**
     * 向单个用户发送命令，不用等待结果
     *
     * @param user 用户列表
     * @param cmd  命令
     * @param data 数据
     * @return 响应结果
     */
    public Result sendOneway(EndUser user, int cmd, Object data) {
        Short subjectType = user.getType();
        String subjectId = user.getUserId();
        Short clientType = user.getClientType();

        Result result = new Result();

        try {
            Optional<SubjectOnline> subjectOpt = subjectCache.get(subjectId, subjectType, clientType);

            if (subjectOpt.isEmpty()
                    || subjectOpt.get().getStatus() == CommonConstants.LOGIN_STATUS_OFFLINE) {

                result.setCode(ResultConstants.USER_NOT_ONLINE_CODE);
                result.setMsg(ResultConstants.USER_NOT_ONLINE_MSG);

                return result;
            }

            SubjectOnline subject = subjectOpt.get();
            String serverId = subject.getServerId();

            Optional<ServerOnline> serverOpt = serverCache.get(serverId);
            if (!serverOnlineService.isOnline(serverOpt)) {

                result.setCode(ResultConstants.USER_NOT_ONLINE_CODE);
                result.setMsg(ResultConstants.USER_NOT_ONLINE_MSG);

                return result;
            }

            ServerOnline server = serverOpt.get();
            String topic = server.getTopic();

            List<EndUser> users = new ArrayList<>();
            users.add(user);

            MqMultiRequest msg = buildMultiRequest(users, cmd, RpcCommand.ONEWAY, data, topic);

            NatsmqProxy proxy = NatsmqProxyHolder.vsspMqProxy;

            log.info("Send a one-way request:{}/{}", cmd, data);
            proxy.sendOneway(msg);

        } catch (Exception ex) {
            log.warn("Catch an exception in sendSync.", ex);

            result.setCode(ResultConstants.REQUEST_EXCEPTION_CODE);
            result.setMsg(ResultConstants.REQUEST_EXCEPTION_MSG);
        }

        return result;
    }

    /**
     * 向单个用户发送命令，且等待结果
     *
     * @param user 用户列表
     * @param cmd  命令
     * @param data 数据
     * @return 响应结果
     */
    public Result sendSync(EndUser user, int cmd, Object data) {

        Short subjectType = user.getType();
        String subjectId = user.getUserId();
        Short clientType = user.getClientType();

        clientType = ClientTypeConstants.getDefaultClientType(clientType, subjectType);

        Result result = new Result();

        try {
            Optional<SubjectOnline> subjectOpt = subjectCache.get(subjectId, subjectType, clientType);

            if (subjectOpt.isEmpty()
                    || subjectOpt.get().getStatus() == CommonConstants.LOGIN_STATUS_OFFLINE) {

                result.setCode(ResultConstants.USER_NOT_ONLINE_CODE);
                result.setMsg(ResultConstants.USER_NOT_ONLINE_MSG);

                return result;
            }

            SubjectOnline subject = subjectOpt.get();
            String serverId = subject.getServerId();

            Optional<ServerOnline> serverOpt = serverCache.get(serverId);
            if (!serverOnlineService.isOnline(serverOpt)) {

                result.setCode(ResultConstants.USER_NOT_ONLINE_CODE);
                result.setMsg(ResultConstants.USER_NOT_ONLINE_MSG);

                return result;
            }

            ServerOnline server = serverOpt.get();
            String topic = server.getTopic();

            List<EndUser> users = new ArrayList<>();
            users.add(user);

            MqMultiRequest msg = buildMultiRequest(users, cmd, RpcCommand.REQUEST, data, topic);

            NatsmqProxy proxy = NatsmqProxyHolder.vsspMqProxy;

            log.info("Send a request:{}/{}", cmd, data);
            Object response = proxy.sendSync(msg, CommonConstants.RPC_TIMEOUT_MILLIS);
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
                //String key = String.format("%d_%d_%s", clientType, subjectType, subjectId);

                Optional<Response> failResponse = failResults.values().stream().findFirst();
                Response userResponse;
                if (failResponse.isPresent()) {
                    userResponse = failResponse.get();
                    result.setCode(userResponse.getCode());
                    result.setMsg(userResponse.getMsg());
                } else {
                    result.setCode(ResultConstants.REQUEST_NO_RESULT_CODE);
                    result.setMsg(ResultConstants.REQUEST_NO_RESULT_MSG);
                }

                return result;
            }

        } catch (Exception ex) {
            log.warn("Catch an exception in sendSync.", ex);

            result.setCode(ResultConstants.REQUEST_EXCEPTION_CODE);
            result.setMsg(ResultConstants.REQUEST_EXCEPTION_MSG);
        }

        return result;
    }

    private MqMultiRequest buildMultiRequest(List<EndUser> users, int cmd,
                                             byte cmdType, Object data, String topic) {

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
                .repliedTopic(onlineConfiguration.getRpcTopic())
                .type(cmdType)
                .data(data)
                .targets(multiUsers).build();

        return request;

    }

    private MqMultiResult convertToMultiResult(Object origin) {

        String middle = JsonUtils.toJson(((Response) origin).getData());

        MqMultiResult result = JsonUtils.fromJson(middle, MqMultiResult.class);

        return result;
    }

    /**
     * 找到用户对应的topic，并过滤不在线的设备和服务器
     *
     * @param userList 用户列表
     * @return 结果
     */
    private Map<String, List<EndUser>> demultiplex(List<EndUser> userList) {
        Map<String, List<EndUser>> result = new HashMap<>();

        userList.forEach(user -> {
            String subjectId = user.getUserId();
            Short subjectType = user.getType();
            Short clientType = user.getClientType();

            SubjectOnline subject;

            if (ClientTypeConstants.isEmpty(clientType)) {
                List<SubjectOnline> subjects = subjectOnlineService.getOnlineSubjects(subjectId, subjectType);
                if (CollectionsUtils.isEmpty(subjects)) {
                    return;
                }

                subject = subjects.get(0);
            } else {
                Optional<SubjectOnline> subjectOpt = subjectCache.get(subjectId, subjectType, clientType);
                if (subjectOpt.isEmpty()) {
                    return;
                }

                subject = subjectOpt.get();

            }

            String serverId = subject.getServerId();

            Optional<ServerOnline> serverOpt = serverCache.get(serverId);
            if (!serverOnlineService.isOnline(serverOpt)) {

                return;
            }

            ServerOnline server = serverOpt.get();
            String topic = server.getTopic();

            List<EndUser> list = result.get(topic);
            if (list != null) {
                list.add(user);
            } else {
                list = new ArrayList<>();
                list.add(user);
                result.put(topic, list);
            }

        });

        return result;
    }

    private MultiResults toMultiResult(MqMultiResult mqMultiResult) {

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


