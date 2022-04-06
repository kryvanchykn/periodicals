package com.periodicals.web.servlets;

import com.periodicals.dao.daoFactory.DaoFactory;
import com.periodicals.entities.Magazine;
import com.periodicals.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/add-subscription")
public class AddSubscriptionServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(AddSubscriptionServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        List<Magazine> magazines = (List<Magazine>) req.getSession().getAttribute("magazines");
        String buttonClicked = req.getParameter("button_clicked");
        Magazine clickedMagazine = null;

        for(Magazine m: magazines){
            if(m.getName().equals(buttonClicked)){
                clickedMagazine = m;
            }
        }

        try {
            if (user != null) {
                assert clickedMagazine != null;
                req.getSession().setAttribute("magazineName", clickedMagazine.getName());

                boolean isSubscribed = DaoFactory.createSubscriptionDao().isSubscribed(user.getId(), clickedMagazine.getId());
                boolean notEnoughMoney = user.getBalance() - clickedMagazine.getPrice() < 0;

                if (isSubscribed) {
                    getServletContext().getRequestDispatcher("/WEB-INF/jsp/is_subscribed.jsp").forward(req, resp);
                } else if (notEnoughMoney) {
                    req.getSession().setAttribute("notEnoughMoney", clickedMagazine.getPrice() - user.getBalance());
                    getServletContext().getRequestDispatcher("/WEB-INF/jsp/not_enough_money.jsp").forward(req, resp);
                } else {
                    user.setBalance(user.getBalance() - clickedMagazine.getPrice());
                    req.getSession().setAttribute("user", user);
                    req.getSession().setAttribute("isActive", null);

                    DaoFactory.createSubscriptionDao().addSubscription(user.getId(), clickedMagazine.getId(), clickedMagazine.getPrice());
                    getServletContext().getRequestDispatcher("/WEB-INF/jsp/successfully_subscribed.jsp").forward(req, resp);
                }
            }
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
        }
    }
}