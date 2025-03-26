package org.noahsark.gw.ws.bootstrap.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
@Component
@ConfigurationProperties("gw.common")
public class CommonConfig {

    private Boolean enable;
    private String serverId;

    private String mqTopic;

    private WorkQueueConfig workQueue;

    private ServerConfig serverConfig;

    public static class WorkQueueConfig {

        private int maxQueueNum;
        private int maxThreadNum;

        public int getMaxQueueNum() {
            return maxQueueNum;
        }

        public void setMaxQueueNum(int maxQueueNum) {
            this.maxQueueNum = maxQueueNum;
        }

        public int getMaxThreadNum() {
            return maxThreadNum;
        }

        public void setMaxThreadNum(int maxThreadNum) {
            this.maxThreadNum = maxThreadNum;
        }
    }

    public static class ServerConfig {

        private String host;

        private int port;

        private SslConfig sslConfig;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public SslConfig getSslConfig() {
            return sslConfig;
        }

        public void setSslConfig(SslConfig sslConfig) {
            this.sslConfig = sslConfig;
        }

    }

    public static class SslConfig {

        private Boolean enable;

        private String keystoreFile;

        private String keystorePwd;

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        public String getKeystoreFile() {
            return keystoreFile;
        }

        public void setKeystoreFile(String keystoreFile) {
            this.keystoreFile = keystoreFile;
        }

        public String getKeystorePwd() {
            return keystorePwd;
        }

        public void setKeystorePwd(String keystorePwd) {
            this.keystorePwd = keystorePwd;
        }
    }


    public WorkQueueConfig getWorkQueue() {
        return workQueue;
    }

    public void setWorkQueue(WorkQueueConfig workQueue) {
        this.workQueue = workQueue;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getMqTopic() {
        return mqTopic;
    }

    public void setMqTopic(String mqTopic) {
        this.mqTopic = mqTopic;
    }

    public Boolean isEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
