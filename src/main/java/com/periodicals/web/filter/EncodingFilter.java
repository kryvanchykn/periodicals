package com.periodicals.web.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * The {@code EncodingFilter} class
 * is an implementation of {@code Filter} interface.
 * Sets character encoding UTF-8 to each request and response objects.
 */
@WebFilter(filterName = "EncodingFilter", urlPatterns = {"/*"})
public class EncodingFilter implements Filter {

    private static final Logger log = LogManager.getLogger(EncodingFilter.class);

    private String encoding;

    public void init(FilterConfig fConfig) {
        encoding = fConfig.getInitParameter("encoding");
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        log.debug("EncodingFilter was executed");
        String requestEncoding = request.getCharacterEncoding();
        if (requestEncoding == null) {
            request.setCharacterEncoding(encoding);
        }
        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}
