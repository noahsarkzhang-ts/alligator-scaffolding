package org.noahsark.online.manager;

import org.noahsark.common.constant.ClientTypeConstants;
import org.noahsark.online.pojo.dto.EndUser;
import org.noahsark.online.pojo.dto.MultiResults;
import org.noahsark.online.pojo.dto.MultiUserResults;
import org.noahsark.online.pojo.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 命令发送器
 *
 * @author zhangxt
 * @date 2024/05/11 22:29
 **/
@Component
public class CmdSender {
    @Autowired
    private OnlineManager onlineManager;

    public Result sendCmd(EndUser endUser, int cmd, Object data) {
        Result result = onlineManager.sendSync(endUser, cmd, data);

        return result;
    }

    public MultiUserResults sendCmd(List<EndUser> userList, int cmd, Object data) {
        MultiResults multiResults = onlineManager.sendSync(userList, cmd, data);

        return toMultiUserResults(multiResults);
    }

    public MultiUserResults sendCmdOneway(List<EndUser> userList, int cmd, Object data) {
        MultiResults multiResults = onlineManager.sendOneway(userList, cmd, data);

        return toMultiUserResults(multiResults);
    }

    private MultiUserResults toMultiUserResults(MultiResults multiResults) {
        MultiUserResults multiUserResults = new MultiUserResults();
        multiUserResults.setCode(multiResults.getCode());
        multiUserResults.setMsg(multiResults.getMsg());

        Map<EndUser, Result> userResultMap = new HashMap<>();

        Map<String, Result> successResults = multiResults.getSuccessResults();
        multiUserResults.setUserResultMap(userResultMap);

        List<EndUser> successUsers = new ArrayList<>();
        successResults.forEach((key, value) -> {
            EndUser user = new EndUser();

            String[] parts = key.split("_");
            Short type = Short.valueOf(parts[0]);
            user.setType(type);
            user.setUserId(parts[1]);
            user.setClientType(ClientTypeConstants.getDefaultClientType(type));

            successUsers.add(user);
            userResultMap.put(user, value);
        });
        multiUserResults.setSuccessUsers(successUsers);

        Map<String, Result> failResults = multiResults.getFailResults();
        List<EndUser> failUsers = new ArrayList<>();
        failResults.forEach((key, value) -> {
            EndUser user = new EndUser();

            String[] parts = key.split("_");
            Short type = Short.valueOf(parts[0]);
            user.setType(type);
            user.setUserId(parts[1]);
            user.setClientType(ClientTypeConstants.getDefaultClientType(type));

            failUsers.add(user);
            userResultMap.put(user, value);
        });
        multiUserResults.setFailUsers(failUsers);

        return multiUserResults;
    }

}
