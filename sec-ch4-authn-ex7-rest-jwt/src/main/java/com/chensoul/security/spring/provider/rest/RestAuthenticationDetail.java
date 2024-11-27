package com.chensoul.security.spring.provider.rest;

import com.chensoul.security.spring.SecurityUtils;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;

@Data
public class RestAuthenticationDetail implements Serializable {
    private final String serverAddress;
    private final String clientAddress;

    public RestAuthenticationDetail(HttpServletRequest request) {
        this.clientAddress = SecurityUtils.getClientIP(request);
        this.serverAddress = SecurityUtils.handleIpv6(request.getLocalAddr());
    }

}
