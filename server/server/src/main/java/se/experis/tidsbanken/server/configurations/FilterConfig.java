package se.experis.tidsbanken.server.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.experis.tidsbanken.server.filters.AuthenticationFilter;
import se.experis.tidsbanken.server.filters.RedirectToClientFilter;
import se.experis.tidsbanken.server.filters.RequestAttemptFilter;
import se.experis.tidsbanken.server.services.AuthorizationService;
import se.experis.tidsbanken.server.services.LoginAttemptService;

@Configuration
public class FilterConfig {

    @Autowired private AuthorizationService authorizationService;
    @Autowired private LoginAttemptService loginAttemptService;

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter(){
        FilterRegistrationBean<AuthenticationFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new AuthenticationFilter(authorizationService));
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<RequestAttemptFilter> requestAttemptFilter() {
        FilterRegistrationBean<RequestAttemptFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestAttemptFilter(loginAttemptService));
        registrationBean.addUrlPatterns("*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<RedirectToClientFilter> redirectToClientFilterFilter() {
        FilterRegistrationBean<RedirectToClientFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RedirectToClientFilter());
        registrationBean.addUrlPatterns("*");
        registrationBean.setOrder(0);
        return registrationBean;
    }
}
