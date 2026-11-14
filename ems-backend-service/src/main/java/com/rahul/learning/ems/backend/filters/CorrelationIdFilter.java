
package com.rahul.learning.ems.backend.filters;

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

/**
 * Servlet filter responsible for extracting and managing correlation IDs
 * within the EMS Backend application.
 *
 * <p>The filter reads the {@code X-Correlation-ID} HTTP request header
 * propagated by the API Gateway.
 *
 * <p>If the incoming correlation ID is missing or invalid, the filter
 * generates a new UUID.
 *
 * <p>The resolved correlation ID is:
 * <ul>
 *     <li>Stored in SLF4J's Mapped Diagnostic Context (MDC).</li>
 *     <li>Added to the HTTP response header.</li>
 *     <li>Made available to Logback through the {@code correlationId}
 *         MDC key.</li>
 * </ul>
 *
 * <p>This enables application logs generated during synchronous request
 * processing to include the same correlation ID without requiring
 * individual service or controller methods to pass it explicitly.
 *
 * <p>The filter also cleans up the MDC value after request processing
 * to prevent correlation IDs from leaking between requests when servlet
 * container threads are reused.
 *
 * @author
 * @since 1.0
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class CorrelationIdFilter extends OncePerRequestFilter {

    /**
     * HTTP header used to receive and propagate the correlation ID.
     */
    private static final String HEADER = "X-Correlation-ID";

    /**
     * Key used to store the correlation ID in SLF4J MDC.
     */
    private static final String MDC_KEY = "correlationId";

    /**
     * Validation pattern for correlation IDs.
     *
     * <p>Allows alphanumeric characters, periods, underscores and hyphens,
     * with a maximum length of 64 characters.
     */
    private static final Pattern VALID_ID =
            Pattern.compile("[A-Za-z0-9._-]{1,64}");

    /**
     * Processes an incoming HTTP request and establishes its correlation
     * ID in the current thread's MDC context.
     *
     * <p>The method performs the following operations:
     * <ol>
     *     <li>Reads the correlation ID from the incoming request header.</li>
     *     <li>Validates the ID and generates a UUID if it is invalid or absent.</li>
     *     <li>Stores the resolved ID in SLF4J MDC.</li>
     *     <li>Adds the correlation ID to the outgoing response header.</li>
     *     <li>Continues processing through the remaining servlet filters.</li>
     *     <li>Restores or removes the previous MDC value after processing.</li>
     * </ol>
     *
     * <p>The MDC cleanup is performed in a {@code finally} block to ensure
     * cleanup occurs even if request processing throws an exception.
     *
     * @param request the incoming HTTP servlet request
     * @param response the outgoing HTTP servlet response
     * @param filterChain the servlet filter chain used to continue request
     *                    processing
     * @throws ServletException if an error occurs during servlet processing
     * @throws IOException if an input or output error occurs
     */
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

        // Preserve any MDC value that may already exist on this thread.
        String previousCorrelationId = MDC.get(MDC_KEY);

        try {
            MDC.put(MDC_KEY, correlationId);
            response.setHeader(HEADER, correlationId);

            filterChain.doFilter(request, response);

        } finally {
            // Prevent correlation IDs leaking into another request
            // that later uses the same servlet thread.
            if (previousCorrelationId == null) {
                MDC.remove(MDC_KEY);
            } else {
                MDC.put(MDC_KEY, previousCorrelationId);
            }
        }
    }
}
