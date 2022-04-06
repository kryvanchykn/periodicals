package com.periodicals.web.servlets;

import com.periodicals.dao.daoFactory.DaoFactory;
import com.periodicals.dao.utils.SqlConstants;
import com.periodicals.entities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UsersServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(UsersServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        try {
            List<User> users;

            users = DaoFactory.createUserDao().findAllUsers();
            session.setAttribute("users", users);

        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
        }

        req.getRequestDispatcher("/WEB-INF/jsp/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            boolean buttonValue = Boolean.parseBoolean(req.getParameter("buttonValue"));
            int userId = Integer.parseInt(req.getParameter("userId"));

            if (buttonValue){
                DaoFactory.createUserDao().changeRole(userId, SqlConstants.BLOCKED_READER);
            } else {
                DaoFactory.createUserDao().changeRole(userId, SqlConstants.READER);
            }

        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + ("/users"));
    }
}

