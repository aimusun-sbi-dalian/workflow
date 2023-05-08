package com.sbi.dl.compoment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

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
    private T errors;

    public static final int SUCCESS_CODE = 1;
    public static final String SUCCESS_MSG = "ok";

    private static final Map<String, Object> EMPTY_MAP = new HashMap<>(0);

    @JsonIgnore
    public boolean isSuccess() {
        return SUCCESS_CODE == this.code;
    }

    public static <T> ResponseBase<T> ok() {
        return (ResponseBase<T>) restResult(EMPTY_MAP, SUCCESS_CODE, SUCCESS_MSG, EMPTY_MAP);
    }

    public static <T> ResponseBase<T> ok(T data) {
        return restResultOk(data, SUCCESS_CODE, SUCCESS_MSG);
    }

    public static <T> ResponseBase<T> failed(int code, String msg) {
        return (ResponseBase<T>) failed(code, msg, EMPTY_MAP);
    }

    public static <T> ResponseBase<T> failed(int code, T errors) {
        return restResultFailed(code, null, errors);
    }


    public static <T> ResponseBase<T> failed(int code, String msg, T errors) {
        return restResultFailed(code, msg, errors);
    }

    private static <T> ResponseBase<T> restResultFailed(int code, String msg, T errors) {
        return restResult(null, code, msg, errors);
    }

    private static <T> ResponseBase<T> restResultOk(T data, int code, String msg) {
        return restResult(data, code, msg, null);
    }

    private static <T> ResponseBase<T> restResult(T data, int code, String msg, T errors) {
        ResponseBase<T> apiResult = new ResponseBase<>();
        apiResult.code = code;
        apiResult.msg = msg;
        apiResult.data = data;
        apiResult.errors = errors;
        return apiResult;
    }
}
