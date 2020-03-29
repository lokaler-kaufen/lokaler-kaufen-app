package de.qaware.mercury.plumbing;

import de.qaware.mercury.business.random.RNG;
import de.qaware.mercury.util.Hex;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
class TraceIdFilter extends OncePerRequestFilter {
    private static final String MDC_TRACE_ID_FIELD = "traceId";
    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    private final RNG rng;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Generate trace id
        String traceId = Hex.bin2Hex(rng.nextBytes(128 / 8));
        // Put the id on the MDC for logging
        MDC.put(MDC_TRACE_ID_FIELD, traceId);

        log.debug("Start handling request");
        // Add the trace id as a header to the response
        response.addHeader(TRACE_ID_HEADER, traceId);

        // Execute the other filters
        filterChain.doFilter(request, response);

        log.debug("Finished handling request");
    }
}
