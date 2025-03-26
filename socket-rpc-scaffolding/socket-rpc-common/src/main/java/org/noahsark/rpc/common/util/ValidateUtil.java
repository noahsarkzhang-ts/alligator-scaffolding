package org.noahsark.rpc.common.util;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * @author: Allen
 * @desc: 参数校验工具
 * @date: 2021/9/3
 */
public class ValidateUtil {

    // 校验类，全局用
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 单个对象校验
     */
    public static <T> ValidateResult valid(T data) {
        Set<ConstraintViolation<T>> validateSet = validator.validate(data);
        ValidateResult validateResult = new ValidateResult();
        validateResult.setSuccess(validateSet.size() == 0);
        if (!validateResult.isSuccess()) {
            validateResult.setMsg(validateSet.iterator().next().getMessage());
        }
        return validateResult;
    }

    /**
     * 对象数组校验
     */
    public static <T> ValidateResult validList(List<T> list) {
        ValidateResult validateResult = new ValidateResult();
        validateResult.setSuccess(true);
        for (T t : list) {
            Set<ConstraintViolation<T>> validateSet = validator.validate(t);
            validateResult.setSuccess(validateSet.size() == 0);
            if (!validateResult.isSuccess()) {
                validateResult.setMsg(validateSet.iterator().next().getMessage());
                return validateResult;
            }
        }
        return validateResult;
    }

}
