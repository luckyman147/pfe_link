package com.pfelink.monolith.infrastructure.api;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class RequestTimingFilter implements Filter {
    public static final String START_TIME_ATTR = "requestStartTime";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            request.setAttribute(START_TIME_ATTR, System.currentTimeMillis());
        }
        chain.doFilter(request, response);
    }
}
