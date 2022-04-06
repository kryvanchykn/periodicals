package com.periodicals.web.servlets;

import com.periodicals.dao.daoFactory.DaoFactory;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/category-redactor")
public class CategoryRedactorServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(CategoryRedactorServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        try {
            List<CategoryLocalization> categoryLocalizations = new ArrayList<>();
            List<Category> categories = DaoFactory.createCategoryDao().findAllCategories(Locale.EN.getId());
            for(Category c: categories){
                categoryLocalizations.add(DaoFactory.createCategoryDao().findCategoryById(c.getId()));
            }
            session.setAttribute("categoryLocalizations", categoryLocalizations);

        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
        }

        req.getRequestDispatcher("/WEB-INF/jsp/category_redactor.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();

        try {
            int buttonValue = Integer.parseInt(req.getParameter("buttonValue"));
            switch (buttonValue) {
                case -1 -> {
                    DaoFactory.createCategoryDao().deleteCategory(Integer.parseInt(req.getParameter("categoryId")));
                    resp.sendRedirect(req.getContextPath() + ("/category-redactor"));
                }
                case 0 -> {
                    int categoryId = Integer.parseInt(req.getParameter("categoryId"));
                    CategoryLocalization categoryLocalization = DaoFactory.createCategoryDao().findCategoryById(categoryId);
                    session.setAttribute("categoryId", categoryId);
                    session.setAttribute("categoryLocalization", categoryLocalization);
                    resp.sendRedirect(req.getContextPath() + ("/category-redactor/edit-category"));
                }

            }
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
        }
    }
}

