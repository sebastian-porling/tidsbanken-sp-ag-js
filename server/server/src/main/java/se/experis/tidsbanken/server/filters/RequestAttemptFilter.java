package se.experis.tidsbanken.server.filters;

import org.springframework.http.HttpStatus;
import se.experis.tidsbanken.server.services.LoginAttemptService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter for login attempts.
 * Returns too_many_requests if the ip is blocked
 */
public class RequestAttemptFilter implements Filter {

    private final LoginAttemptService loginAttemptService;

    /**
     * Constructor
     * @param loginAttemptService Login attempt Service
     */
    public RequestAttemptFilter(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * Checks if the login attempt is blocked and
     * returns appropriate response when blocked
     * or forwards the request if not blocked
     * @param servletRequest HttpServletRequest
     * @param servletResponse HttpServletResponse
     * @param filterChain Filter Chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        if (!loginAttemptService.checkFailedLoginAttempt(req)) {
            ((HttpServletResponse) servletResponse).sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "To many requests" +
                    ". Your account is blocked. Try again later.");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
