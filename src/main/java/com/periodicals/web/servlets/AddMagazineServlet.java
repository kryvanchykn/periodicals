package com.periodicals.web.servlets;

import com.periodicals.dao.daoFactory.DaoFactory;
import com.periodicals.entities.*;
import com.periodicals.exceptions.AppException;
import com.periodicals.exceptions.DBException;
import com.periodicals.validation.DataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet("/magazines-redactor/add-magazine")
public class AddMagazineServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(AddMagazineServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Locale locale = Locale.valueOf((String) session.getAttribute("locale"));
        try {
            List<Category> categories = DaoFactory.createCategoryDao().findAllCategories(locale.getId());
            session.setAttribute("categories", categories);
        } catch (DBException e) {
            log.error("Exception: " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/jsp/add_magazine.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            MagazineLocalization ml = new MagazineLocalization();
            Map<Integer, String> names = new HashMap<>();
            Map<Integer, String> descriptions = new HashMap<>();
            Map<Integer, String> publishers = new HashMap<>();

            for (Locale localeVar : Locale.values()) {
                DataValidator.validateString(req.getParameter("name_" + localeVar.name().toLowerCase()));
                DataValidator.validateText(req.getParameter("description_" + localeVar.name().toLowerCase()));
                DataValidator.validateString(req.getParameter("publisher_" + localeVar.name().toLowerCase()));

                names.put(localeVar.getId(), req.getParameter("name_" + localeVar.name().toLowerCase()));
                descriptions.put(localeVar.getId(), req.getParameter("description_" + localeVar.name().toLowerCase()));
                publishers.put(localeVar.getId(), req.getParameter("publisher_" + localeVar.name().toLowerCase()));
            }
            int categoryId = Integer.parseInt(req.getParameter("category_id"));

            DataValidator.validateMoneyAmount(req.getParameter("price"));
            double price = Double.parseDouble(req.getParameter("price"));

            Date publicationDate = Date.valueOf(req.getParameter("publication_date"));

            DataValidator.validateURL(req.getParameter("image_url"));
            String imageURL = req.getParameter("image_url");

            ml.setNames(names);
            ml.setDescriptions(descriptions);
            ml.setPublishers(publishers);
            ml.setCategoryId(categoryId);
            ml.setPrice(price);
            ml.setPublicationDate(publicationDate);
            ml.setImageURL(imageURL);

            DaoFactory.createMagazineDao().addMagazine(ml);

        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
            req.getSession().setAttribute("errorMessage", e.getMessage());
            throw new AppException(e);
        }

        resp.sendRedirect(req.getContextPath() + ("/magazines-redactor/add-magazine"));
    }
}
