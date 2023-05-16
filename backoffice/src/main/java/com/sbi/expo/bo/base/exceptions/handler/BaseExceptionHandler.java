package com.sbi.expo.bo.base.exceptions.handler;

import static org.springframework.http.HttpStatus.OK;

import com.sbi.expo.bo.base.ResponseBase;
import com.sbi.expo.bo.base.exceptions.BizPromptException;
import com.sbi.expo.commons.utils.MapHelper;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * This is an abstract basic exception handler, the project needs to implement this function, please
 * extend it and use {@link
 * org.springframework.web.bind.annotation.ControllerAdvice @ControllerAdvice}
 *
 * @author Ming.G
 */
@Slf4j
public abstract class BaseExceptionHandler {

    /**
     * get system level error code
     *
     * @return error code
     */
    abstract String getSystemErrorCode();
    /**
     * get optimistic lock error code
     *
     * @return error code
     */
    abstract String getOptimisticLockErrorCode();
    /**
     * get resource not found error code
     *
     * @return error code
     */
    abstract String getNotFoundErrorCode();
    /**
     * get unique constraint error code
     *
     * @return error code
     */
    abstract String getUniqueConstraintErrorCode();
    /**
     * get request parameter error code
     *
     * @return error code
     */
    abstract String getParameterErrorCode();
    /**
     * get request parameter constraint error code
     *
     * @return error code
     */
    abstract String getConstraintParameterErrorCode();

    void printWarnException(Exception e) {
        log.warn("Exception: ", e);
    }

    void printWarnException(String message) {
        log.warn("Exception: {}", message);
    }

    ResponseEntity<ResponseBase<?>> response(String errorCode, String message) {
        return new ResponseEntity<>(ResponseBase.failed(Integer.parseInt(errorCode), message), OK);
    }

    ResponseEntity<ResponseBase<?>> response(String errorCode, String message, Object obj) {
        return new ResponseEntity<>(
                ResponseBase.failed(Integer.parseInt(errorCode), message, obj), OK);
    }

    ResponseEntity<ResponseBase<?>> response(String errorCode, Locale locale) {
        String message = ErrorMessageHandler.getMessage(errorCode, locale);
        if (StringUtils.isEmpty(message)) {
            return new ResponseEntity<>(
                    ResponseBase.failed(
                            Integer.parseInt(getSystemErrorCode()),
                            ErrorMessageHandler.getMessage(getSystemErrorCode(), locale)),
                    OK);
        }
        return response(errorCode, message);
    }

    ResponseEntity<ResponseBase<?>> response(String errorCode, Locale locale, Object obj) {
        String message = ErrorMessageHandler.getMessage(errorCode, locale);
        if (StringUtils.isEmpty(message)) {
            return new ResponseEntity<>(
                    ResponseBase.failed(
                            Integer.parseInt(getSystemErrorCode()),
                            ErrorMessageHandler.getMessage(getSystemErrorCode(), locale)),
                    OK);
        }
        return response(errorCode, message, obj);
    }

    ResponseEntity<ResponseBase<?>> handleException(Locale locale) {
        return response(getSystemErrorCode(), locale);
    }

    // ###############################Base Exception Handling###############################

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseBase<?>> handleException(Exception e, Locale locale) {
        printWarnException(e);
        return handleException(locale);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ResponseBase<?>> handleDataIntegrityViolationException(
            DataIntegrityViolationException e, Locale locale) {
        printWarnException(e);
        return response(getUniqueConstraintErrorCode(), locale);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ResponseBase<?>> handleException(
            HttpRequestMethodNotSupportedException e, Locale locale) {
        printWarnException(e);
        return response(getNotFoundErrorCode(), locale);
    }

    @ExceptionHandler({
        MethodArgumentTypeMismatchException.class,
        ConversionFailedException.class,
        IllegalArgumentException.class
    })
    public ResponseEntity<ResponseBase<?>> handleRequestParametersException(
            Exception e, Locale locale) {
        printWarnException(e);
        return response(getParameterErrorCode(), locale);
    }

    @ExceptionHandler({OptimisticLockingFailureException.class, OptimisticLockException.class})
    public ResponseEntity<ResponseBase<?>> handleOptimisticLockingFailureException(
            Exception e, Locale locale) {
        printWarnException(e);
        return response(getOptimisticLockErrorCode(), locale);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ResponseBase<?>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e, Locale locale) {
        printWarnException(e.getLocalizedMessage());
        return response(getParameterErrorCode(), locale);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseBase<?>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, Locale locale) {
        printWarnException(e.getMessage());
        return response(getConstraintParameterErrorCode(), locale, getConstraintsError(e));
    }

    private List<Map<String, Object>> getConstraintsError(MethodArgumentNotValidException e) {
        return e.getFieldErrors().stream()
                .map(
                        error ->
                                MapHelper.initMap(
                                        "field",
                                        error.getField(),
                                        "reason",
                                        error.getDefaultMessage()))
                .toList();
    }

    // ###############################Custom Exception Handling###############################

    @ExceptionHandler({BizPromptException.class})
    public ResponseEntity<ResponseBase<?>> handleBizPromptException(
            BizPromptException e, Locale locale) {
        if (StringUtils.isNotEmpty(e.getMessage())) {
            return response(e.getMessageCode(), e.getMessage());
        }
        return response(e.getMessageCode(), locale);
    }
}
