package com.periodicals.web.servlets;

import com.periodicals.dao.daoFactory.DaoFactory;
import com.periodicals.entities.*;
import com.periodicals.exceptions.AppException;
import com.periodicals.validation.DataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet("/category-redactor/add-category")
public class AddCategoryServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(AddCategoryServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/add_category.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            CategoryLocalization category = new CategoryLocalization();
            String name;
            Map<Integer, String> names = new HashMap<>();

            for(Locale locale : Locale.values()){
                name = req.getParameter("name_" + locale.name().toLowerCase());
                DataValidator.validateString(name);
                names.put(locale.getId(), name);
            }

            category.setNames(names);
            DaoFactory.createCategoryDao().addCategory(category);
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
            req.getSession().setAttribute("errorMessage", e.getMessage());
            throw new AppException(e);
        }

        resp.sendRedirect(req.getContextPath() + ("/category-redactor/add-category"));
    }
}
