package com.periodicals.web.filter;

import com.periodicals.entities.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * The {@code LocaleFilter} class
 * is an implementation of {@code Filter} interface.
 * Sets locale attribute to the session.
 */
@WebFilter(filterName = "LocaleFilter", urlPatterns = {"/*"})
public class LocaleFilter implements Filter {
    private static final Logger log = LogManager.getLogger(LocaleFilter.class);

    public void init(FilterConfig arg0) {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Locale filter was executed");
        HttpServletRequest req = (HttpServletRequest) request;
        req.getSession().setAttribute("locales", Locale.values());
        String locale = req.getParameter("locale");

        if (locale != null) {
            req.getSession().setAttribute("locale", locale);
        } else if (req.getSession().getAttribute("locale") == null) {
            req.getSession().setAttribute("locale", Locale.EN.toString());
        }
        chain.doFilter(request, response);
    }

    public void destroy() {}
}
