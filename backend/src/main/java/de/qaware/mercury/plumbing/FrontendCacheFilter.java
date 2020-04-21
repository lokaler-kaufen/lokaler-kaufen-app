package de.qaware.mercury.plumbing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "mercury.frontend.enable-caching", havingValue = "true")
@Slf4j
class FrontendCacheFilter extends OncePerRequestFilter {
    /**
     * Maps from extension to the Cache-Control header.
     * <p>
     * See https://developer.mozilla.org/en-US/docs/Web/HTTP/Caching
     */
    private static final Map<String, String> CACHE_SETTINGS = Map.of(
        ".css", "public, max-age=31536000, immutable",
        ".js", "public, max-age=31536000, immutable"
    );

    public FrontendCacheFilter() {
        log.info("Enabling caching of frontend assets");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/") || request.getRequestURI().equals("/index.html")) {
            // index.html must always be revalidated
            response.setHeader("Cache-Control", "must-revalidate");
        } else {
            String cacheHeader = getCacheControlHeader(getExtension(request.getRequestURI()));
            if (cacheHeader != null) {
                response.setHeader("Cache-Control", cacheHeader);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Nullable
    private String getExtension(String uri) {
        int lastDot = uri.lastIndexOf('.');
        if (lastDot == -1) {
            return null;
        }
        return uri.substring(lastDot);
    }

    @Nullable
    private String getCacheControlHeader(@Nullable String extension) {
        if (extension == null) {
            return null;
        }

        return CACHE_SETTINGS.get(extension);
    }
}
