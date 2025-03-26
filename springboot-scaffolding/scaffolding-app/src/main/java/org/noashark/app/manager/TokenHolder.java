package org.noashark.app.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Token容器类
 *
 * @author zhangxt
 * @date 2023/08/09 14:02
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public final class TokenHolder {

    private Tokenable provider;

    private Map<String, HolderValue> holders = new HashMap<>();

    public String getToken(String host) {

        HolderValue holderValue = holders.get(host);

        if (holderValue == null) {
            holderValue = new HolderValue();
            holders.put(host, holderValue);
        }

        return holderValue.getToken(provider, host);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenExpires {
        // 超时时长 ms
        private int expires;

        private String token;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HolderValue {
        // 超时时长 ms
        private int expires;

        // 容错时长，1000ms = 1S
        private int toleranceTime = 1000;

        // 获取 token 的时间
        private long startTime;

        // token
        private String token;

        public String getToken(Tokenable provider, String host) {

            if (StringUtils.isEmpty(this.token)) {

                resetToken(provider, host);

                return this.token;
            }

            long currentTime = System.currentTimeMillis();

            // token 在有效期
            if (currentTime <= this.startTime + this.expires - this.toleranceTime) {

                return this.token;
            }

            resetToken(provider, host);

            return this.token;
        }


        private void resetToken(Tokenable provider, String host) {
            TokenExpires tokenExpires = provider.getToken(host);

            this.token = tokenExpires.token;
            this.expires = tokenExpires.expires;
            this.startTime = System.currentTimeMillis();
        }


    }

    public interface Tokenable {
        TokenExpires getToken(String host);
    }

}
