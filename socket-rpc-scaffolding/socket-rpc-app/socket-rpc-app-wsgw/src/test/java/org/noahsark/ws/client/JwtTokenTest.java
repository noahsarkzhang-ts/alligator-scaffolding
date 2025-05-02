package org.noahsark.ws.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.noahsark.util.JwtValidator;

import java.util.HashMap;
import java.util.Map;

/**
 * Jwt 测试token
 *
 * @author zhangxt
 * @date 2024/04/05 16:46
 **/
@Slf4j
public class JwtTokenTest {

    @Test
    public void createToken() {
        String appSecret = "Aiujd86Bc99";
        int expirationDays = 7;

        JwtValidator validator = new JwtValidator(appSecret, expirationDays);
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", 3);
        payload.put("customerId",1821383295130255362L);
        payload.put("productId", "app_01");
        payload.put("timestamp", System.currentTimeMillis());

        String token = validator.createToken(payload);
        log.info("token:{}", token);
    }

}
