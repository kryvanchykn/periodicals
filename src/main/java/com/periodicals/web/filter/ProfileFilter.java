package com.periodicals.web.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Redirects not logged users to login page when accessing to pages that use profile
 */
@WebFilter({"/profile"})
public class ProfileFilter extends HttpFilter {
    private static final Logger log = LogManager.getLogger(ProfileFilter.class);

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        log.debug("ProfileFilter was executed");
        if (req.getSession().getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/login");
        } else {
            chain.doFilter(req, res);
        }
    }
}
