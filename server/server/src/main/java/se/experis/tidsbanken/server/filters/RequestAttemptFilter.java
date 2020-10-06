package se.experis.tidsbanken.server.filters;

import org.springframework.http.HttpStatus;
import se.experis.tidsbanken.server.services.LoginAttemptService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestAttemptFilter implements Filter {

    private final LoginAttemptService loginAttemptService;

    public RequestAttemptFilter(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

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
