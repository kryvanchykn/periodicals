package com.periodicals.web.servlets;

import com.periodicals.dao.daoFactory.DaoFactory;
import com.periodicals.entities.Locale;
import com.periodicals.entities.SubscriptionInfo;
import com.periodicals.entities.User;
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

@WebServlet("/profile/subscription")
public class SubscriptionServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(SubscriptionServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if(session.getAttribute("isActive") == null) {

            User user = (User) session.getAttribute("user");
            Locale locale = Locale.valueOf((String) session.getAttribute("locale"));

            try {
                List<SubscriptionInfo> subscriptionInfo = DaoFactory.createSubscriptionDao().findActiveSubscriptionsInfoByUserId(user.getId(), locale.getId());
                session.setAttribute("subscriptionInfo", subscriptionInfo);
            } catch (Exception e) {
                log.error("Exception: " + e.getMessage());
            }
        }

        req.getRequestDispatcher("/WEB-INF/jsp/subscription.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        User user = (User) session.getAttribute("user");
        boolean isActive = req.getParameter("isActive") == null || Boolean.parseBoolean(req.getParameter("isActive"));
        Locale locale = session.getAttribute("locale") == null ? Locale.EN : Locale.valueOf((String) session.getAttribute("locale"));

        List<SubscriptionInfo> subscriptionInfo;
        try {
            if (isActive) {
                subscriptionInfo = DaoFactory.createSubscriptionDao().findActiveSubscriptionsInfoByUserId(user.getId(), locale.getId());
            }   else {
                subscriptionInfo = DaoFactory.createSubscriptionDao().findExpiredSubscriptionsInfoByUserId(user.getId(), locale.getId());
            }
            session.setAttribute("subscriptionInfo", subscriptionInfo);
            session.setAttribute("isActive", isActive);
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + ("/profile/subscription"));
    }
}
