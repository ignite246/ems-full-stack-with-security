
package com.rahul.learning.ems.apigateway.filters;

import java.util.UUID;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * A global Spring Cloud Gateway filter responsible for managing correlation
 * IDs across incoming HTTP requests.
 *
 * <p>The filter checks whether the incoming request contains a valid
 * {@code X-Correlation-ID} header. If the header is missing or invalid,
 * a new UUID is generated.
 *
 * <p>The resolved correlation ID is:
 * <ul>
 *     <li>Added to the outgoing request forwarded to downstream services.</li>
 *     <li>Added to the HTTP response header.</li>
 *     <li>Included in the Gateway's incoming-request log message.</li>
 * </ul>
 *
 * <p>This allows requests to be identified and traced across multiple
 * microservices using a common correlation ID.
 *
 * <p><strong>Note:</strong> This filter explicitly includes the correlation
 * ID in its log message rather than placing it in SLF4J MDC. MDC requires
 * additional care in reactive applications because execution may switch
 * between threads.
 *
 * @author
 * @since 1.0
 */
@Component
public class CorrelationIdGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger log =
            LoggerFactory.getLogger(CorrelationIdGlobalFilter.class);

    /**
     * HTTP header used to receive and propagate the correlation ID.
     */
    private static final String HEADER = "X-Correlation-ID";

    /**
     * Validation pattern for correlation IDs.
     *
     * <p>Allows alphanumeric characters, periods, underscores and hyphens,
     * with a maximum length of 64 characters.
     */
    private static final Pattern VALID_ID =
            Pattern.compile("[A-Za-z0-9._-]{1,64}");

    /**
     * Intercepts every request passing through the Gateway and resolves
     * its correlation ID.
     *
     * <p>If the incoming correlation ID is valid, it is preserved.
     * Otherwise, a new UUID is generated.
     *
     * <p>The resolved ID is added to the mutated downstream request and
     * the response headers before the request continues through the
     * remaining Gateway filters.
     *
     * @param exchange the current server web exchange containing the
     *                 incoming request and outgoing response
     * @param chain    the Gateway filter chain used to continue request
     *                 processing
     * @return a {@link Mono} that completes when downstream request
     * processing finishes
     */
    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        String incomingId =
                exchange.getRequest().getHeaders().getFirst(HEADER);

        String correlationId =
                incomingId != null && VALID_ID.matcher(incomingId).matches()
                        ? incomingId
                        : UUID.randomUUID().toString();

        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .headers(headers -> headers.set(HEADER, correlationId))
                .build();

        ServerWebExchange updatedExchange = exchange.mutate()
                .request(request)
                .build();

        updatedExchange.getResponse()
                .getHeaders()
                .set(HEADER, correlationId);

        log.info("API Gateway::Incoming request: method={}, path={}, correlationId={}",
                request.getMethod(),
                request.getURI().getPath(),
                correlationId);

        return chain.filter(updatedExchange);
    }

    /**
     * Defines the execution order of this global Gateway filter.
     *
     * <p>Using {@link Ordered#HIGHEST_PRECEDENCE} ensures that correlation
     * ID processing happens at the earliest Gateway filter order.
     *
     * @return the highest precedence order value
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
