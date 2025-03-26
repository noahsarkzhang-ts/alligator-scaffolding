package org.noahsark.online.pojo.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * <p>
 * 网关在线
 * </p>
 *
 * @author allen
 * @since 2024-03-19
 */
@Getter
@Setter
public class ServerOnlineDTO implements Serializable {

    /**
     * 主键,使用雪花id
     */
    private Long id;

    /**
     * 服务id
     */
    private String serverId;

    /**
     * 通信mq topic
     */
    private String topic;

    /**
     * 状态,1：在线,2：离线
     */
    private Short status;

    /**
     * 登陆时间
     */
    private LocalDateTime loginTime;

    /**
     * 上次心跳时间
     */
    private LocalDateTime lastPingTime;

}