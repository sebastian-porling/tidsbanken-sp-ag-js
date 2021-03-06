package se.experis.tidsbanken.server.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import se.experis.tidsbanken.server.services.AuthorizationService;
import se.experis.tidsbanken.server.services.LoginAttemptService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filters authenticated requests
 */
public class AuthenticationFilter implements Filter {

    final private AuthorizationService authorizationService;

    @Autowired
    public AuthenticationFilter(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * If the user goes to the api/login we forward them.
     * Otherwise we check if they are an authenticated user
     * @param servletRequest HttpServletRequest
     * @param servletResponse HttpServletResponse
     * @param filterChain Filter Chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        if (req.getRequestURI().startsWith("/api/login")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (!authorizationService.isAuthorized(req)) {
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not authorized to" +
                    " make this request.");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
