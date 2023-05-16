package com.sbi.expo.bo.base.exceptions.handler;

import com.sbi.expo.bo.base.ResponseBase;
import com.sbi.expo.bo.base.SpringContextHolder;
import com.sbi.expo.bo.base.constant.MessageConstant;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

/**
 * ErrorMessageHandler
 *
 * @author Ming.G
 * @date 2022-01-11
 */
@Slf4j
public class ErrorMessageHandler {

    private static final MessageSource messageSource;

    private static final String MESSAGE_NO_ERROR = "No errors when calling.";
    private static final String MESSAGE_DEFAULT = "Request parameter exception.";

    static {
        messageSource = SpringContextHolder.getBean(MessageSource.class);
    }

    private static final int MESSAGE_DEFAULT_ERROR_CODE =
            Integer.parseInt(MessageConstant.CODE_10100);

    private static final Pattern LEVEL_0_PATTERN = Pattern.compile("(NotBlank|NotNull|NotEmpty)");
    private static final Pattern LEVEL_1_PATTERN = Pattern.compile("(Length|Min|Max)");

    private static final int LEVEL_0 = 0;
    private static final int LEVEL_1 = 1;
    private static final int LEVEL_DEFAULT = 9;

    private static int getErrorMsgLevel(String code) {
        if (StringUtils.isEmpty(code)) {
            return LEVEL_DEFAULT;
        }
        if (LEVEL_0_PATTERN.matcher(code).matches()) {
            return LEVEL_0;
        } else if (LEVEL_1_PATTERN.matcher(code).matches()) {
            return LEVEL_1;
        } else {
            return LEVEL_DEFAULT;
        }
    }

    public static String getMessage(String code) {
        return getMessage(code, null, Locale.getDefault());
    }

    public static String getMessage(String code, Locale locale) {
        return getMessage(code, null, locale);
    }

    public static String getMessage(String code, Object[] args, Locale locale) {
        String errorMsg = null;
        try {
            errorMsg = messageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            log.warn("No such error message from message config, code is {}", code);
        }
        return errorMsg;
    }

    public static ResponseBase<?> validationError(BindingResult result) {
        if (!result.hasErrors()) {
            throw new IllegalArgumentException(MESSAGE_NO_ERROR);
        }
        return getFirstError(result);
    }

    private static ResponseBase<?> getFirstError(BindingResult result) {
        ObjectError objectError =
                result.getAllErrors().stream()
                        .filter(Objects::nonNull)
                        .min(Comparator.comparingInt(o -> getErrorMsgLevel(o.getCode())))
                        .orElseThrow(() -> new IllegalArgumentException(MESSAGE_NO_ERROR));
        String message;
        String code = message = objectError.getDefaultMessage();
        if (!NumberUtils.isCreatable(code)) {
            code = null;
        }
        return StringUtils.isEmpty(code)
                ? ResponseBase.failed(
                        MESSAGE_DEFAULT_ERROR_CODE,
                        StringUtils.isEmpty(message) ? MESSAGE_DEFAULT : message)
                : ResponseBase.failed(
                        Integer.parseInt(code),
                        messageSource.getMessage(
                                code,
                                objectError.getArguments(),
                                MESSAGE_DEFAULT,
                                Locale.getDefault()));
    }
}
