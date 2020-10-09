package se.experis.tidsbanken.server.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Handles forwarding of routing for the frontend and backend
 */
public class RedirectToClientFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * Forwards the request if it contains api, css, js or favicon
     * Otherwise we forward it to the frontend router
     * @param servletRequest HttpServletRequest
     * @param servletResponse HttpServletResponse
     * @param filterChain Filter Chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        final String requestURI = req.getRequestURI();
        if (requestURI.startsWith("/api") || requestURI.startsWith("/css") || requestURI.startsWith("/js") || requestURI.startsWith("/favicon") ){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        servletRequest.getRequestDispatcher("/").forward(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
