package com.periodicals.web.servlets;

import com.periodicals.dao.daoFactory.DaoFactory;
import com.periodicals.entities.Locale;
import com.periodicals.entities.User;
import com.periodicals.exceptions.AppException;
import com.periodicals.exceptions.DBException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import com.periodicals.validation.DataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/profile/replenish-balance")
public class ReplenishBalanceServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(ReplenishBalanceServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/replenish_balance.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        Locale locale = Locale.valueOf((String) session.getAttribute("locale"));

        try {

            DataValidator.validateMoneyAmount(req.getParameter("sum"));
            List<Locale> locales = DaoFactory.createLocaleDao().findAllLocales();
            double sum = Double.parseDouble(req.getParameter("sum"));
            for(Locale l: locales){
                if (l.getId() == locale.getId()){
                    sum *= l.getExchangeRate();
                }
            }
            double balance = DaoFactory.createUserDao().replenishBalance(user.getId(), sum);
            user.setBalance(balance);
            session.setAttribute("balance", balance);
        } catch (DBException | IllegalArgumentException e) {
            log.error("Exception: " + e.getMessage());
            req.getSession().setAttribute("errorMessage", e.getMessage());
            throw new AppException(e);
        }
        resp.sendRedirect(req.getContextPath() + "/profile/replenish-balance");
    }
}