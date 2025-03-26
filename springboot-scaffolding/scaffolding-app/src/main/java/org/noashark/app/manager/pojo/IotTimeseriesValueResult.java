package org.noashark.app.manager.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * IOT 时序数据结果对象
 *
 * @author zhangxt
 * @date 2023/11/29 18:22
 **/
@Data
public class IotTimeseriesValueResult {

    /**
     * 值列表
     */
    private Map<String, List<IotTimeseriesValue>> values;

    /**
     * 健描述
     */
    private Map<String, KeyAndDesc> keys;
}
