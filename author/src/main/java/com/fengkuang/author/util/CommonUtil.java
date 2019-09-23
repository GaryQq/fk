package com.fengkuang.author.util;

import com.fengkuang.author.enums.ResultCode;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaichong
 * 公共工具类
 */
public class CommonUtil {

    /**
     * 校验是否有空参数
     *
     * @param objects
     * @return
     */
    public static boolean hasNullParams(Object... objects) {
        for (Object obj : objects) {
            if (null == obj || StringUtils.equals("null", obj.toString()) || StringUtils.isBlank(obj.toString())) {
                return true;
            }
        }
        return false;
    }

    public static Map<String, Object> makeResult(ResultCode code) {
        Map<String, Object> result = new HashMap<>(2);
        if (null != code) {
            result.put("code", code.getCode());
            result.put("message", code.getMessage());
        }
        return result;
    }

    public static Map<String, Object> makeResult(ResultCode code, Object data) {
        Map<String, Object> result = CommonUtil.makeResult(code);
        result.put("data", data);
        return result;
    }
}
