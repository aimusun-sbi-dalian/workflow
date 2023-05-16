package com.sbi.expo.api.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * ResponseBase
 *
 * @author Zhenming.zhang
 * @date 2021-10-12
 */
@Data
public class ResponseBase<T> {

    private int code;
    private String msg;
    private T data;

    public static final int SUCCESS_CODE = 1;
    public static final String SUCCESS_MSG = "ok";

    private static final Map<String, Object> EMPTY_MAP = new HashMap<>(0);

    @JsonIgnore
    public boolean isSuccess() {
        return SUCCESS_CODE == this.code;
    }

    public static <T> ResponseBase<T> ok() {
        return (ResponseBase<T>) restResult(EMPTY_MAP, SUCCESS_CODE, SUCCESS_MSG);
    }

    public static <T> ResponseBase<T> ok(T data) {
        return restResult(data, SUCCESS_CODE, SUCCESS_MSG);
    }

    public static <T> ResponseBase<T> failed(int errorCode, String msg) {
        return (ResponseBase<T>) failed(errorCode, msg, EMPTY_MAP);
    }

    public static <T> ResponseBase<T> failed(int errorCode, String msg, T data) {
        return restResult(data, errorCode, msg);
    }

    private static <T> ResponseBase<T> restResult(T data, int code, String msg) {
        ResponseBase<T> apiResult = new ResponseBase<>();
        apiResult.code = code;
        apiResult.msg = msg;
        apiResult.data = data;
        return apiResult;
    }
}
