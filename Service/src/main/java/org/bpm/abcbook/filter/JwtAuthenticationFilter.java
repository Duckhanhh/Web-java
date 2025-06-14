package org.bpm.abcbook.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.bpm.abcbook.controller.UserSessionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final String[] PUBLIC_ENDPOINTS = {
            "/users",
            "/auth/login",
            "/auth/introspect",
            "/login.jsf",
            "/javax.faces.resource/**",
            "/resources/**",
            "/error",
            "*.jsf",
            "*.xhtml",
            "*.css",
            "*.js",
            "*.png",
            "*.jpg",
            "*.gif",
            "*.ico"
    };

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        // Nếu là endpoint public thì bỏ qua
//        if (!uri.endsWith("/orderManagement.jsf") && !uri.endsWith("/bookManagement.jsf")
//                && !uri.endsWith("/inventoryManagement.jsf") && !uri.endsWith("/dashboard.jsf") && !uri.endsWith("/importBookManagement.jsf")) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        try {
            UserSessionBean userSessionBean = applicationContext.getBean(UserSessionBean.class);
            String token = userSessionBean.getToken();
            if (token != null && !token.isEmpty()) {
                logger.debug("Adding JWT token to request header");
                HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request) {
                    @Override
                    public String getHeader(String name) {
                        if ("Authorization".equalsIgnoreCase(name)) {
                            return "Bearer " + token;
                        }
                        return super.getHeader(name);
                    }

                    @Override
                    public Enumeration<String> getHeaders(String name) {
                        if ("Authorization".equalsIgnoreCase(name)) {
                            return Collections.enumeration(Collections.singletonList("Bearer " + token));
                        }
                        return super.getHeaders(name);
                    }

                    @Override
                    public Enumeration<String> getHeaderNames() {
                        Map<String, String> headers = new HashMap<>();
                        Enumeration<String> originalHeaders = super.getHeaderNames();
                        while (originalHeaders.hasMoreElements()) {
                            String headerName = originalHeaders.nextElement();
                            headers.put(headerName, super.getHeader(headerName));
                        }
                        headers.put("Authorization", "Bearer " + token);
                        return Collections.enumeration(headers.keySet());
                    }
                };
                filterChain.doFilter(requestWrapper, response);
                return;
            }
        } catch (Exception e) {
            logger.debug("No active session found: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
} 