package com.chensoul.security.spring;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
public class SecurityUtils {
    private static final List<String> CLIENT_IP_HEADER_NAMES = Arrays.asList("X-Forwarded-For",
            "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR");
    private static final String LOCAL_IP4 = "127.0.0.1";
    private static final String LOCAL_IP6 = "0:0:0:0:0:0:0:1";

    public static UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        } else {
            throw new RuntimeException("YOU_AREN_T_AUTHORIZED_TO_PERFORM_THIS_OPERATION");
        }
    }

    public static String getClientIP(HttpServletRequest request) {
        String ip = null;
        for (String header : CLIENT_IP_HEADER_NAMES) {
            ip = request.getHeader(header);
            if (ip!=null) {
                break;
            }
        }

        if (ip==null) {
            ip = request.getRemoteAddr();
        }

        ip = ip.split(",")[0];
        return handleIpv6(ip);
    }

    public static String handleIpv6(String ip) {
        if (ip.equals(LOCAL_IP6)) {
            return LOCAL_IP4;
        }
        return ip;
    }

}
