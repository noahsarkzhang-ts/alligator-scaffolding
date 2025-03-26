package org.noahsark.online.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

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
@TableName("omp_server_online")
public class ServerOnline implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,使用雪花id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 服务id
     */
    private String serverId;

    /**
     * 服务器类型,1:平台网关，2：低端安全帽网关；3: 存储服务
     */
    private Short type;

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
