package org.noashark.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.noashark.common.exception.CommonException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OkClient Utils
 *
 * @author zhangxt
 * @date 2023/08/07 14:03
 **/
@Slf4j
public class OkHttpUtils {

    private static final int CONNECT_TIMEOUT_SECOND = 60;
    private static final int READ_TIMEOUT_SECOND = 60;
    private static final int WRITE_TIMEOUT_SECOND = 60;

    private static OkHttpClient client;
    private static TrustManager[] trustAllCerts;
    private static SSLContext sslContext;

    static {
        try {
            trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };


            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // 创建OkHttpClient对象, 并设置超时时间
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .connectTimeout(CONNECT_TIMEOUT_SECOND, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT_SECOND, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT_SECOND, TimeUnit.SECONDS)
                    .addInterceptor(new CallInterceptor())
                    .build();
        } catch (Exception ex) {
            log.warn("OKClient Init Exception.", ex);
        }
    }

    /**
     * 同步POST请求
     *
     * @param url        请求地址
     * @param bodyParams 请求参数
     */
    public static String postForm(String url, Map<String, String> headers, Map<String, Object> queryParams, Map<String, String> bodyParams) {


        // 1 创建OkHttpClient对象
        // 2 构建请求体

        RequestBody body;

        if (bodyParams != null) {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            bodyParams.forEach((key, value) -> {
                builder.addFormDataPart(key, value);
            });

            body = builder.build();
        } else {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            body = RequestBody.create(mediaType, "");
        }

        return commonRequestJson(url, headers, queryParams, body, HttpMethod.POST);
    }

    /**
     * 同步POST请求
     *
     * @param url        请求地址
     * @param bodyParams 请求参数
     */
    public static String postJson(String url, Map<String, String> headers, Map<String, Object> queryParams, String bodyParams) {

        log.info("params:{}", bodyParams);

        // 1 创建OkHttpClient对象
        // 2 构建请求体
        RequestBody body;
        MediaType mediaType = MediaType.parse("application/json");
        if (bodyParams != null) {
            body = RequestBody.create(mediaType, bodyParams);
        } else {
            body = RequestBody.create(mediaType, "");
        }

        return commonRequestJson(url, headers, queryParams, body, HttpMethod.POST);
    }

    /**
     * 同步POST请求
     *
     * @param url    请求地址
     * @param params 请求参数
     */
    public static String patchJson(String url, Map<String, String> headers, String params) {

        log.info("params:{}", params);

        // 1 创建OkHttpClient对象
        // 2 构建请求体

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, params != null ? params : "");

        return commonRequestJson(url, headers, null, body, HttpMethod.PATCH);
    }

    public static String commonRequestJson(String url, Map<String, String> headers, Map<String, Object> queryParams, RequestBody body, HttpMethod httpMethod) {

        try {

            // 构建Request对象
            Request.Builder reqBuilder = new Request.Builder();

            if (headers != null) {
                headers.forEach((key, value) -> {
                    reqBuilder.addHeader(key, value);
                });
            }

            if (queryParams != null) {

                url = buildUrlParams(url, queryParams);
            }

            switch (httpMethod) {
                case POST:
                    reqBuilder.post(body);
                    break;
                case PATCH:
                    reqBuilder.patch(body);
                    break;
                case DELETE:
                    reqBuilder.delete(body);
                    break;
                default:
                    break;
            }

            reqBuilder.url(url);

            Request req = reqBuilder.build();

            // 4 发起请求获取响应值
            return request(req);
        } catch (CommonException ucEx) {
            throw ucEx;
        } catch (Exception ex) {
            log.warn("The request failed, and the exception information was:", ex);
            throw new CommonException(-1, ex.getMessage(), ex);
        }

    }

    public static String delete(String url, Map<String, String> headers, Map<String, Object> queryParams,
                                String bodyParams) {

        RequestBody body;
        MediaType mediaType = MediaType.parse("application/json");
        if (bodyParams != null) {
            body = RequestBody.create(mediaType, bodyParams);
        } else {
            body = RequestBody.create(mediaType, "");
        }

        return commonRequestJson(url, headers, queryParams, body, HttpMethod.DELETE);
    }


    /**
     * 同步GET请求
     */
    public static String get(String url, Map<String, String> headers, Map<String, Object> params) {

        url = buildUrlParams(url, params);

        return get(url, headers);
    }

    private static String buildUrlParams(String url, Map<String, Object> params) {
        log.debug("URL:{}", url);

        if (params == null || params.isEmpty()) {
            return url;
        }

        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(url).newBuilder();

        params.forEach((key, value) -> {
            urlBuilder.addQueryParameter(key, value.toString());
        });

        url = urlBuilder.build().toString();

        return url;
    }

    /**
     * 同步GET请求
     */
    public static String get(String url, Map<String, String> headers) {

        try {
            Request.Builder builder = new Request.Builder();

            // get 方法
            builder.get();

            if (headers != null && headers.size() >= 1) {
                headers.forEach((key, value) -> {
                    builder.addHeader(key, value);
                });
            }

            Request req = builder.url(url).build();

            return request(req);

        } catch (CommonException ucEx) {
            throw ucEx;
        } catch (Exception ex) {
            log.warn("The request failed, and the exception information was:", ex);
            throw new CommonException(-1, ex.getMessage(), ex);
        }

    }

    public static String upload(String url, Map<String, String> headers, Map<String, Object> queryParams,
                                String multiPart, String fileName, File file) {
        try {
            Request.Builder builder = new Request.Builder();

            if (headers != null && headers.size() >= 1) {
                headers.forEach((key, value) -> {
                    builder.addHeader(key, value);
                });
            }

            if (queryParams != null && queryParams.size() >= 1) {
                url = buildUrlParams(url, queryParams);
            }

            RequestBody fileBody = RequestBody.create(MediaType.parse("application/from-data"), file);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(multiPart, fileName, fileBody)
                    .build();

            Request req = builder.url(url)
                    .post(requestBody)
                    .build();

            return request(req);

        } catch (CommonException ucEx) {
            throw ucEx;
        } catch (Exception ex) {
            log.warn("The request failed, and the exception information was:", ex);
            throw new CommonException(-1, ex.getMessage(), ex);
        }
    }

    public static String upload(String url, Map<String, String> headers, Map<String, Object> queryParams,
                                String multiPart, String fileName, byte[] data) {
        try {
            Request.Builder builder = new Request.Builder();

            if (headers != null && headers.size() >= 1) {
                headers.forEach((key, value) -> {
                    builder.addHeader(key, value);
                });
            }

            if (queryParams != null && queryParams.size() >= 1) {
                url = buildUrlParams(url, queryParams);
            }

            RequestBody fileBody = RequestBody.create(MediaType.parse("application/from-data"), data);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(multiPart, fileName, fileBody)
                    .build();

            Request req = builder.url(url)
                    .post(requestBody)
                    .build();

            return request(req);

        } catch (CommonException ucEx) {
            throw ucEx;
        } catch (Exception ex) {
            log.warn("The request failed, and the exception information was:", ex);
            throw new CommonException(-1, ex.getMessage(), ex);
        }
    }

    public static byte[] download(String url, Map<String, String> headers, Map<String, Object> queryParams) {
        try {
            Request.Builder builder = new Request.Builder();

            // get 方法
            builder.get();

            if (headers != null && headers.size() >= 1) {
                headers.forEach((key, value) -> {
                    builder.addHeader(key, value);
                });
            }

            if (queryParams != null && queryParams.size() >= 1) {
                url = buildUrlParams(url, queryParams);
            }

            Request req = builder.url(url).build();

            return requestBytes(req);

        } catch (CommonException ucEx) {
            throw ucEx;
        } catch (Exception ex) {
            log.warn("The request failed, and the exception information was:", ex);
            throw new CommonException(-1, ex.getMessage(), ex);
        }
    }

    public static String request(Request req) {
        try {
            Response response = execute(req);

            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                if (response.body() != null) {
                    log.warn("response: {}", response.body().string());
                }
                throw new CommonException(response.code(), "The request is unsuccessful, the error code is: " + response.code());
            }

        } catch (CommonException ucEx) {
            log.warn("The request failed, and the exception information was: {}", ucEx.getMessage());

            throw ucEx;
        } catch (Exception ex) {
            log.warn("The request failed, and the exception information was: ", ex);

            throw new CommonException(-1, ex.getMessage(), ex);
        }
    }

    /**
     * 同步请求
     */
    public static Response execute(Request request) throws IOException {
        return client.newCall(request).execute();
    }

    public static byte[] requestBytes(Request req) {
        try {
            Response response = execute(req);

            if (response.isSuccessful()) {
                return response.body().bytes();
            } else {
                if (response.body() != null) {
                    log.warn("response: {}", response.body().string());
                }
                throw new CommonException(response.code(), "The request is unsuccessful, the error code is: " + response.code());
            }

        } catch (CommonException ucEx) {
            log.warn("The request failed, and the exception information was: {}", ucEx.getMessage());

            throw ucEx;
        } catch (Exception ex) {
            log.warn("The request failed, and the exception information was: ", ex);

            throw new CommonException(-1, ex.getMessage(), ex);
        }
    }

    /**
     * 开启异步线程访问网络, 需要返回结果
     */
    public static void enqueue(Request request, Callback callback) {
        client.newCall(request).enqueue(callback);
    }

    /**
     * 开启异步线程访问网络,不需要返回结果( Callback 返回为空)
     */
    public static void enqueue(Request request) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.info("The request failed, and the exception information was: {} ", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                log.info("Request successfully!");
            }
        });
    }

    @Slf4j
    private static class CallInterceptor implements Interceptor {

        /**
         * 统计接口完成时间
         */
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            long begin = System.currentTimeMillis();
            Response response = chain.proceed(request);
            long end = System.currentTimeMillis();

            log.debug("{} ,Total time spent on interface calls: {} ms", request.url().toString(), end - begin);

            return response;
        }
    }

    public enum HttpMethod {
        POST, PATCH, GET, PUT, DELETE
    }


}

