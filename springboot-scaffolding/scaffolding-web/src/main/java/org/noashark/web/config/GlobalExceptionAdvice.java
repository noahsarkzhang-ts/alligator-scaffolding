package org.noashark.web.config;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import lombok.extern.slf4j.Slf4j;
import org.noashark.common.exception.CommonException;
import org.noashark.common.pojo.Response;
import org.noashark.i18n.I18nUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({Exception.class})
    public Response allException(Exception ex) {
        log.error("Catch an exception.", ex);
        return Response.fail(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response argumentNotValid(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        return Response.fail(fieldError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public Response bindException(BindException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        return Response.fail(fieldError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(DuplicateKeyException.class)
    public Response duplicateKey(DuplicateKeyException ex) {
        String localizedMessage = ex.getMostSpecificCause().getLocalizedMessage();
        String result = "%s 已存在！";
        if (localizedMessage != null) {
            try {
                localizedMessage = localizedMessage.replaceAll("\n", "");
                String value = getResult(localizedMessage);
                result = String.format(result, value);
            } catch (Exception e) {
                log.error("truncate exception message error, message: {}", localizedMessage);
            }
        }
        return Response.fail(result);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NullPointerException.class)
    public Response nullPointer(NullPointerException ex) {
        log.error("Catch an exception.", ex);
        return Response.fail("该信息不存在或已被删除");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(TokenExpiredException.class)
    public Response tokenExpiredException(TokenExpiredException ex) {
        log.error("Catch an exception.", ex);
        return new Response.Builder()
                .msg("token已过期")
                .code(HttpStatus.FORBIDDEN.value())
                .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(SignatureVerificationException.class)
    public Response signatureVerificationException(SignatureVerificationException ex) {
        log.error("Catch an exception.", ex);
        return new Response.Builder<String>()
                .msg("token非法")
                .code(HttpStatus.FORBIDDEN.value())
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(CommonException.class)
    public Response commonException(CommonException ex) {
        log.error("Catch an exception.", ex);
        return new Response.Builder()
                .msg(I18nUtils.get(ex.getMessage()))
                .code(ex.getCode())
                .build();
    }

    private String getResult(String str) {
        String[] split = str.split("=");
        String keyStr = split[0];
        keyStr = keyStr.substring(keyStr.indexOf("(") + 1, keyStr.indexOf(")"));
        String[] keys = keyStr.split(",");
        String resStr = split[1];
        resStr = resStr.substring(resStr.indexOf("(") + 1, resStr.indexOf(")"));
        if (keys.length == 1) {
            return resStr;
        }

        String[] results = resStr.split(",");
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i].trim(), results[i].trim());
        }
        List<String> discardKeys = new ArrayList<>();
        discardKeys.add("customer_id");
        discardKeys.add("deleted");
        discardKeys.add("tenant_id");
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            if (discardKeys.contains(it.next().getKey())) {
                it.remove();
            }
        }
        return String.join("-", map.values());
    }
}
