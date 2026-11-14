package com.rahul.learning.ems.notification.filters;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final String HEADER = "X-Correlation-ID";
    private static final String MDC_KEY = "correlationId";

    private static final Pattern VALID_ID =
            Pattern.compile("[A-Za-z0-9._-]{1,64}");

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String incomingId = request.getHeader(HEADER);

        String correlationId =
                incomingId != null && VALID_ID.matcher(incomingId).matches()
                        ? incomingId
                        : UUID.randomUUID().toString();

        String previousCorrelationId = MDC.get(MDC_KEY);

        try {
            MDC.put(MDC_KEY, correlationId);

            response.setHeader(HEADER, correlationId);

            filterChain.doFilter(request, response);

        } finally {
            if (previousCorrelationId == null) {
                MDC.remove(MDC_KEY);
            } else {
                MDC.put(MDC_KEY, previousCorrelationId);
            }
        }
    }
}