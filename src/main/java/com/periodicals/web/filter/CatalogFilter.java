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
 * Redirects incorrect access to catalog
 */
@WebFilter({"/catalog"})
public class CatalogFilter extends HttpFilter {
    private static final Logger log = LogManager.getLogger(CatalogFilter.class);

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        log.debug("CatalogFilter was executed");
        String category = req.getParameter("category");
        String sortBy = req.getParameter("sortBy");

        if (category == null || sortBy == null) {
            res.sendRedirect(req.getContextPath() + "/catalog?category=0&sortBy=price&page=0&magazinesOnPage=3");
        } else {
            chain.doFilter(req, res);
        }
    }
}
