package com.periodicals.web.servlets;

import com.periodicals.dao.daoFactory.DaoFactory;
import com.periodicals.entities.User;
import com.periodicals.exceptions.AppException;
import com.periodicals.exceptions.DBException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/login")
public class LogInServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(LogInServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        try {
            User user = DaoFactory.createUserDao().logIn(login, password);
            if (user == null) {
                throw new IllegalArgumentException("error.page.login.no.user");
            } else {
                req.getSession().setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + "/profile");
            }
        } catch (DBException | IllegalArgumentException e) {
            log.error("Exception: " + e.getMessage());
            req.getSession().setAttribute("errorMessage", e.getMessage());
            throw new AppException(e);
        }
    }
}