package com.periodicals.web.servlets;

import com.periodicals.dao.daoFactory.DaoFactory;
import com.periodicals.dao.utils.Utils;
import com.periodicals.entities.User;
import com.periodicals.exceptions.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(ProfileServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (req.getParameter("logout") != null) {
            Utils.logout(req, resp);
            return;
        }

        try {
            User user = (User) session.getAttribute("user");
            Double balance = DaoFactory.createUserDao().getBalance(user.getId());
            session.setAttribute("balance", balance);
        } catch (DBException e) {
            log.error("Exception: " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(req, resp);
    }
}
