package org.noashark.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * @author zhangxt
 * @version 1.0
 * @description: 国际化信息获取
 * @date 2020/12/30 14:01
 */
public class I18nUtils {

    private static final Supplier<MessageSource> messageSource =
            SpringContextHolder.getSupplier(MessageSource.class, "messageSource");

    private static Locale defaultLocale = new Locale("zh-CN");

    /**
     * 获取国际化标签值
     */
    public static String get(String code, Object... args) {
        return get(code, LocaleContextHolder.getLocale(), args);
    }

    /**
     * 获取国际化标签值
     */
    public static String get(String code, Locale locale, Object... args) {
        MessageSource source = messageSource.get();
        if (source == null) {
            return "";
        }
        try {

            if (locale == null) {
                locale = defaultLocale;
            }

            return source.getMessage(code, args, locale);
        } catch (NoSuchMessageException noSuchMessageException) {
            return code;
        }
    }
}
