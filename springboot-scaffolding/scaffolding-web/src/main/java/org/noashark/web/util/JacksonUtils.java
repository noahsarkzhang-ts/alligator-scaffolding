package org.noashark.web.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Jackson工具类
 *
 * @author zhangxt
 * @date 2024/05/18 18:33
 **/
@Component
public class JacksonUtils {

    @Autowired
    private ObjectMapper objectMapper;

    public <T> List<T> fromJsonList(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
