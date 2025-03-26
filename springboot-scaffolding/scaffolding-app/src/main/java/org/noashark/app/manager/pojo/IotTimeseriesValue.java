package org.noashark.app.manager.pojo;

import lombok.Data;

/**
 * IOT 时序数据值
 *
 * @author zhangxt
 * @date 2023/11/02 14:18
 **/
@Data
public class IotTimeseriesValue {

    private Object value;

    private long ts;
}
