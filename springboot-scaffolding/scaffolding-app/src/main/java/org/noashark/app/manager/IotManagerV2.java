package org.noashark.app.manager;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.noashark.app.configruation.UcConfiguration;
import org.noashark.app.manager.pojo.IotTimeseriesKeyValue;
import org.noashark.app.manager.pojo.IotTimeseriesValue;
import org.noashark.common.exception.CommonException;
import org.noashark.util.JsonUtils;
import org.noashark.util.OkHttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Thingsboard IOT 管理器
 *
 * @author zhangxt
 * @date 2023/10/31 18:28
 **/
@Slf4j
@Component
public class IotManagerV2 implements TokenHolder.Tokenable {

    @Autowired
    private UcConfiguration config;

    private TokenHolder tokenHolder;

    public IotManagerV2() {
        tokenHolder = new TokenHolder();

        tokenHolder.setProvider(this::getToken);
    }

    public String saveOrUpdateServerAttrs(String host, String deviceId, String attrs) {
        return saveOrUpdateAttrs(host, deviceId, IotConstants.SERVER_SCOPE, attrs);
    }

    public String saveOrUpdateSharedAttrs(String host, String deviceId, String attrs) {
        return saveOrUpdateAttrs(host, deviceId, IotConstants.SHARED_SCOPE, attrs);
    }

    /**
     * 添加或更新服务端或共享端属性
     *
     * @param host     服务器
     * @param deviceId 设备名称
     * @param data     时序数组
     * @return 返回结果
     */
    public String saveOrUpdateTimeseries(String host, String deviceId, List<IotTimeseriesKeyValue> data) {
        try {
            String endpoint = "api/plugins/telemetry/%s/%s/timeseries/%s";

            String entityType = "DEVICE";
            String scope = "ANY";

            endpoint = String.format(endpoint, entityType, deviceId, scope);

            String uri = host + endpoint;

            Map<String, Object> params = new HashMap<>();
            params.put("scope", scope);

            Optional<JsonElement> request = sendRequest(host, uri, params, JsonUtils.toJson(data), OkHttpUtils.HttpMethod.POST);
            if (request.isPresent()) {

                return JsonUtils.toJson(request.get());
            }

            return "OK";
        } catch (CommonException ucEx) {
            throw ucEx;
        } catch (Exception ex) {
            throw new CommonException(-1, ex.getMessage(), ex);
        }
    }

    /**
     * 从客户端更新时序数据
     *
     * @param host        服务器
     * @param accessToken 设备名称
     * @param data        时序数组
     * @return 返回结果
     */
    public String updateTelemetry(String host, String accessToken, List<IotTimeseriesKeyValue> data) {
        try {
            String endpoint = "api/v1/%s/telemetry";

            endpoint = String.format(endpoint, accessToken);

            String uri = host + endpoint;

            Optional<JsonElement> request = sendDirectRequest(host, uri, null, JsonUtils.toJson(data), OkHttpUtils.HttpMethod.POST);
            if (request.isPresent()) {

                return JsonUtils.toJson(request.get());
            }

            return "OK";
        } catch (CommonException ucEx) {
            throw ucEx;
        } catch (Exception ex) {
            throw new CommonException(-1, ex.getMessage(), ex);
        }
    }


    /**
     * 查询设备的最近时序值
     *
     * @param host               iot服务地址
     * @param deviceId           设备id
     * @param keys               查询的key列表，为空查询所有。格式为：active,inactivityAlarmTime
     * @param useStrictDataTypes 是否开启严格格式，若为false,则值转换为string,若为true,则不转换
     * @return 时序列表
     */
    public Map<String, List<IotTimeseriesValue>> getDeviceLatestTimeseries(String host, String deviceId,
                                                                           String keys, Boolean useStrictDataTypes) {
        try {
            String endpoint = "api/plugins/telemetry/%s/%s/values/timeseries";

            String entityType = "DEVICE";

            endpoint = String.format(endpoint, entityType, deviceId);

            String uri = host + endpoint;

            Map<String, String> headers = getHeaders(host);

            Map<String, Object> params = new HashMap<>();
            if (!StringUtils.isEmpty(keys)) {
                params.put("keys", keys);
            }
            if (useStrictDataTypes != null) {
                params.put("useStrictDataTypes", useStrictDataTypes);
            }

            String response = OkHttpUtils.get(uri, headers, params);

            log.info("uri: {} , response:{}", uri, response);

            Optional<JsonElement> jsonElement = preResponse(response);

            if (jsonElement.isEmpty()) {
                throw new CommonException(-1, "GetDeviceLatestTimeseries response is null");
            }

            JsonObject jsonObject = jsonElement.get().getAsJsonObject();

            Type timeType = new TypeToken<HashMap<String, List<IotTimeseriesValue>>>() {
            }.getType();
            Map<String, List<IotTimeseriesValue>> map = JsonUtils.fromJson(jsonObject, timeType);

            return map;

        } catch (CommonException ucEx) {
            throw ucEx;
        } catch (Exception ex) {
            throw new CommonException(-1, ex.getMessage(), ex);
        }
    }

    /**
     * 添加或更新服务端或共享端属性
     *
     * @param host     服务器
     * @param deviceId 设备名称
     * @param scope    scope
     * @param attrs    属性
     * @return 返回结果
     */
    public String saveOrUpdateAttrs(String host, String deviceId, String scope, String attrs) {
        try {
            String endpoint = "api/plugins/telemetry/%s/%s";

            endpoint = String.format(endpoint, deviceId, scope);

            String uri = host + endpoint;

            Optional<JsonElement> request = sendRequest(host, uri, null, attrs, OkHttpUtils.HttpMethod.POST);
            if (request.isPresent()) {

                JsonObject jsonObject = request.get().getAsJsonObject();

                return jsonObject.get("result").getAsString();
            }

            return "";
        } catch (CommonException ucEx) {
            throw ucEx;
        } catch (Exception ex) {
            throw new CommonException(-1, ex.getMessage(), ex);
        }
    }

    /**
     * 删除设备
     *
     * @param host     IOT服务地址
     * @param deviceId 设备id
     * @return 结果
     */
    public String delDevice(String host, String deviceId) {
        try {
            String endpoint = "api/device/%s";
            endpoint = String.format(endpoint, deviceId);

            String uri = host + endpoint;

            Optional<JsonElement> request = sendRequest(host, uri, null, null
                    , OkHttpUtils.HttpMethod.DELETE);

            return "OK";
        } catch (CommonException ucEx) {
            throw ucEx;
        } catch (Exception ex) {
            throw new CommonException(-1, ex.getMessage(), ex);
        }

    }


    private Optional<JsonElement> sendRequest(String host, String uri, Map<String, Object> queryParams, String params, OkHttpUtils.HttpMethod httpMethod) {
        Map<String, String> headers = getHeaders(host);

        String response = null;

        switch (httpMethod) {
            case POST:
                response = OkHttpUtils.postJson(uri, headers, queryParams, params);
                break;
            case PATCH:
                response = OkHttpUtils.patchJson(uri, headers, params);
                break;
            case DELETE:
                response = OkHttpUtils.delete(uri, headers, queryParams, params);
                break;
            default:
                break;
        }

        log.info("uri:{} ,response:{}", uri, response);

        return preResponse(response);
    }

    private Optional<JsonElement> sendDirectRequest(String host, String uri, Map<String, Object> queryParams, String params, OkHttpUtils.HttpMethod httpMethod) {

        String response = null;

        switch (httpMethod) {
            case POST:
                response = OkHttpUtils.postJson(uri, null, queryParams, params);
                break;
            case PATCH:
                response = OkHttpUtils.patchJson(uri, null, params);
                break;
            case DELETE:
                response = OkHttpUtils.delete(uri, null, queryParams, params);
                break;
            default:
                break;
        }

        log.info("uri:{} ,response:{}", uri, response);

        return preResponse(response);
    }

    @Override
    public TokenHolder.TokenExpires getToken(String host) {

        try {
            String endpoint = "api/auth/login";
            String uri = host + endpoint;

            Map<String, String> params = new HashMap<>();
            params.put("username", config.getIotUsername());
            params.put("password", config.getIotPwd());

            String response = OkHttpUtils.postJson(uri, null, null, JsonUtils.toJson(params));

            log.info("uri:{},response:{}", uri, response);

            Optional<JsonElement> result = preResponse(response);

            if (result.isEmpty()) {
                throw new CommonException(-1, "Login response is null");
            }

            JsonObject jsonObject = result.get().getAsJsonObject();

            String token = jsonObject.get("token").getAsString();

            // 默认为2.5小时
            int expires = (int) (2.5 * 3600 * 1000);

            try {
                DecodedJWT jwt = JWT.decode(token);

                Date iat = jwt.getIssuedAt();
                Date exp = jwt.getExpiresAt();

                expires = (int) (exp.getTime() - iat.getTime());

            } catch (Exception ex) {
                log.warn("Parsing iat or exp Exception.", ex);
            }

            TokenHolder.TokenExpires tokenExpires = new TokenHolder.TokenExpires();
            tokenExpires.setToken(token);
            tokenExpires.setExpires(expires);

            log.info("token:{},expires:{}", token, expires);

            return tokenExpires;

        } catch (CommonException ucEx) {
            throw ucEx;
        } catch (Exception ex) {
            throw new CommonException(-1, ex.getMessage(), ex);
        }
    }

    private Optional<JsonElement> preResponse(String response) {

        if (!StringUtils.isEmpty(response)) {

            JsonElement element = JsonParser.parseString(response);

            if (element.isJsonObject()) {
                JsonObject jsonObject = element.getAsJsonObject();

                if (jsonObject.get("status") != null) {
                    int status = jsonObject.get("status").getAsInt();
                    String message = jsonObject.get("message").getAsString();

                    throw new CommonException(1000, String.format("Result Invalid:%d:%s", status, message));
                }

                return Optional.of(element);
            } else {
                return Optional.of(element);
            }

        } else {
            return Optional.ofNullable(null);
        }
    }

    private Map<String, String> getHeaders(String host) {

        String token = tokenHolder.getToken(host);

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("X-Authorization", "Bearer " + token);

        return headers;
    }

    public static class IotConstants {

        // 服务端属性
        public static final String SERVER_SCOPE = "SERVER_SCOPE";

        // 共享端属性
        public static final String SHARED_SCOPE = "SHARED_SCOPE ";

        // 客户端属性
        public static final String CLIENT_SCOPE = "CLIENT_SCOPE";

        // 服务端属性字段
        public static final String FIELD_SERVER = "server";

        // 共享端属性字段
        public static final String FIELD_SHARED = "shared";

        // 客户端属性字段
        public static final String FIELD_CLIENT = "client";

    }
}
