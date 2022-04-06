package com.periodicals.web.servlets;

import com.periodicals.dao.daoFactory.DaoFactory;
import com.periodicals.dao.utils.Utils;
import com.periodicals.entities.User;
import com.periodicals.exceptions.AppException;
import com.periodicals.validation.DataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(SignUpServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");
        String repeatPassword = req.getParameter("repeatPassword");

        try {
            if (!DaoFactory.createUserDao().isLoginUnique(login)) {
                throw new IllegalArgumentException("error.page.not.unique.login");
            }

            if (!Utils.comparePasswords(password, repeatPassword)) {
                throw new IllegalArgumentException("error.page.different.passwords");
            }

            DataValidator.validateLogin(login);
            DataValidator.validatePhoneNumber(phone);
            DataValidator.validateEmail(email);
            DataValidator.validatePassword(password);

            User user = new User();
            user.setLogin(login);
            user.setPassword(password);
            user.setPhone(phone);
            user.setEmail(email);

            DaoFactory.createUserDao().signUp(user);
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
            req.getSession().setAttribute("errorMessage", e.getMessage());
            throw new AppException(e);
        }

        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
