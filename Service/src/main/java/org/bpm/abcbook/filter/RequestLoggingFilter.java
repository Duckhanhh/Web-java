package org.bpm.abcbook.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.bpm.abcbook.controller.AuthenticationController.logger;

@Component
public class RequestLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        logger.info("Request URI: {}", httpRequest.getRequestURI());
        logger.info("Authorization header: {}", httpRequest.getHeader("Authorization"));
        chain.doFilter(request, response);
    }
}
