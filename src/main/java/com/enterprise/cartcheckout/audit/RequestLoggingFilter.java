package com.enterprise.cartcheckout.audit;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
            
        if (request instanceof HttpServletRequest httpServletRequest && response instanceof HttpServletResponse httpServletResponse) {
            
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpServletResponse);

            long startTime = System.currentTimeMillis();

            try {
                chain.doFilter(requestWrapper, responseWrapper);
            } finally {
                long duration = System.currentTimeMillis() - startTime;
                String method = requestWrapper.getMethod();
                String uri = requestWrapper.getRequestURI();
                int status = responseWrapper.getStatus();

                log.info("API Request [{} {}] - Status: {} - Duration: {}ms", method, uri, status, duration);

                if (log.isTraceEnabled()) {
                    String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
                    if (!requestBody.isEmpty()) {
                        log.trace("Request Body: {}", requestBody);
                    }
                }

                responseWrapper.copyBodyToResponse();
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
