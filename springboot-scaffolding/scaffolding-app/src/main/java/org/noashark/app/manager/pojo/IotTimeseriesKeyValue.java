package org.noashark.app.manager.pojo;

import lombok.Data;

import java.util.Map;

/**
 * IOT 时序key/value
 *
 * @author zhangxt
 * @date 2023/11/02 15:04
 **/
@Data
public class IotTimeseriesKeyValue {

    private Long ts;

    private Map<String,Object> values;
}
