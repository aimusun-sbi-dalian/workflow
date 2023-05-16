package com.sbi.expo.cust.base.exceptions.handler;

import com.sbi.expo.cust.base.ResponseBase;
import com.sbi.expo.cust.base.constant.MessageConstant;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * BoSeedExceptionHandler
 *
 * @author Ming.G
 * @date 2022-07-07
 */
@Slf4j
@ControllerAdvice
public class BoSeedExceptionHandler extends BaseExceptionHandler {

    @Override
    String getSystemErrorCode() {
        return MessageConstant.CODE_20000;
    }

    @Override
    String getOptimisticLockErrorCode() {
        return MessageConstant.CODE_20001;
    }

    @Override
    String getNotFoundErrorCode() {
        return MessageConstant.CODE_20003;
    }

    @Override
    String getUniqueConstraintErrorCode() {
        return MessageConstant.CODE_20004;
    }

    @Override
    String getParameterErrorCode() {
        return MessageConstant.CODE_10100;
    }

    @Override
    String getConstraintParameterErrorCode() {
        return MessageConstant.CODE_10101;
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ResponseBase<?>> handleAccessDeniedException(Exception e, Locale locale) {
        printWarnException(e);
        return response(MessageConstant.CODE_20002, locale);
    }
}
