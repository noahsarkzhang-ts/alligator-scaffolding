package org.noahsark.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 推送电量命令
 * @author zhangxt
 * @date 2024/05/21 10:33
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushPowerEvent implements Serializable {

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
     * 电量
     */
    private BigDecimal power;

    /**
     * 时间戳
     */
    private Long ts;
}
