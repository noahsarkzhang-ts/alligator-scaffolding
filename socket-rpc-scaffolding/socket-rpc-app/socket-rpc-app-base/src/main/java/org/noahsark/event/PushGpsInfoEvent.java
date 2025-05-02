package org.noahsark.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 推送GPS数据
 *
 * @author zhangxt
 * @date 2024/05/21 10:30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushGpsInfoEvent implements Serializable {

    /**
     * 类型,1:设备，2：管理员；3：用户
     */
    private Short type;

    /**
     * 类型为1为sn;
     * 类型为2为管理员id
     * 类型为3为用户id
     */
    private String subjectId;

    /**
     * 经度
     */
    private BigDecimal lon;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * gcj02 经度
     */
    private BigDecimal gcj02Lon;

    /**
     * gcj02纬度
     */
    private BigDecimal gcj02Lat;

    /**
     * bd09 经度
     */
    private BigDecimal bd09Lon;

    /**
     * bd09 纬度
     */
    private BigDecimal bd09Lat;

    /**
     * 时间戳
     */
    private Long ts;
}
