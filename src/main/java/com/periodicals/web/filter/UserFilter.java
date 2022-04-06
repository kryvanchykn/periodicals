package com.periodicals.web.filter;

import com.periodicals.entities.User;
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
 * Redirects not logged users to login page and administrators to profile page
 * when accessing to pages available only for users
 */
@WebFilter({"/profile/subscription"})
public class UserFilter extends HttpFilter {
    private static final Logger log = LogManager.getLogger(UserFilter.class);

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        log.info("UserFilter was executed");
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login");
        } else if(user.getRoleId() == 3){
            res.sendRedirect(req.getContextPath() + "/profile");
        } else {
            chain.doFilter(req, res);
        }
    }
}
