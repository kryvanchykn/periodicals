package com.periodicals.web.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Redirects logged users to profile when accessing to logIn/signUp pages
 */
@WebFilter({"/login", "/signup"})
public class LogInSignUpFilter extends HttpFilter {
    private static final Logger log = LogManager.getLogger(CatalogFilter.class);

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        log.debug("LogInSignUpFilter was executed");
        if (req.getSession().getAttribute("user") != null) {
            res.sendRedirect(req.getContextPath() + "/profile");
        } else {
            chain.doFilter(req, res);
        }
    }
}
