
package com.example.securitystarter.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component

public class RequestLoggingFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        log.info("User accessing endpoint: {}", request.getRequestURI());

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
