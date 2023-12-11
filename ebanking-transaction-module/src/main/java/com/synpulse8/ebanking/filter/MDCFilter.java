package com.synpulse8.ebanking.filter;

import com.synpulse8.ebanking.security.PrincipleUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@WebFilter(filterName = "mdcFilter")
public class MDCFilter implements Filter {

    private final PrincipleUtils principleUtils;

    public MDCFilter(PrincipleUtils principleUtils) {
        this.principleUtils = principleUtils;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        try {
            var uid = principleUtils.getUid();
            MDC.put("uid", uid);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
