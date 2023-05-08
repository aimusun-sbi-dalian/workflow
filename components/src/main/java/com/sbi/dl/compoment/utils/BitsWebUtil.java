package com.sbi.dl.compoment.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * WebUtils
 *
 * @author Ming.G
 * @date 2022-01-06
 */
@Slf4j
public class BitsWebUtil {

    private static final String CONTENT_TYPE = "Content-type";

    private static final String TRUE_CLIENT_IP = "True-Client-IP";
    private static final String X_FORWARDED_FOR = "x-forwarded-for";

    private static final String MESSAGE_CANNOT_OBTAIN_REQUEST =
            "The local thread cannot obtain the request information";

    public static boolean isPostRequest(HttpServletRequest request) {
        return HttpMethod.POST.name().equals(request.getMethod());
    }

    public static String getIpAddress(HttpServletRequest request) {
        if (StringUtils.isNotEmpty(request.getHeader(TRUE_CLIENT_IP))) {
            return request.getHeader(TRUE_CLIENT_IP);
        } else if (StringUtils.isNotEmpty(request.getHeader(X_FORWARDED_FOR))) {
            return request.getHeader(X_FORWARDED_FOR);
        }
        return request.getRemoteAddr();
    }

    public static String getServerHostname() {
        InetAddress iAddress;
        try {
            iAddress = InetAddress.getLocalHost();
            return iAddress.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("Exception occurred {}", e.getMessage(), e);
        }
        return null;
    }

    public static String getLocalhostName() {
        String hostName = "";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
            log.info("The local hostName is: {}", hostName);
        } catch (Exception e) {
            log.warn("Failed to get hostname", e);
        }
        return hostName;
    }

    /**
     * get request from RequestContextHolder
     *
     * @author Ming.G
     * @date 2022-01-07
     */
    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == attrs) {
            throw new IllegalStateException(MESSAGE_CANNOT_OBTAIN_REQUEST);
        }
        return attrs.getRequest();
    }

    /**
     * get jwt from request header
     *
     * @author Ming.G
     * @date 2022-5-23
     */
}
