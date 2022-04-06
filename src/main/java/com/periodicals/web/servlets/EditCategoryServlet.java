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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet("/category-redactor/edit-category")
public class EditCategoryServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(EditCategoryServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/edit_category.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();

        try{
            CategoryLocalization categoryLocalization = new CategoryLocalization();
            String name;
            Map<Integer, String> names = new HashMap<>();
            for(Locale locale : Locale.values()){
                name = req.getParameter("name_" + locale.name().toLowerCase());
                DataValidator.validateString(name);
                names.put(locale.getId(), name);
            }
            categoryLocalization.setId((Integer) session.getAttribute("categoryId"));
            categoryLocalization.setNames(names);

            DaoFactory.createCategoryDao().updateCategory(categoryLocalization);
            session.setAttribute("categoryLocalization", categoryLocalization);

        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
            req.getSession().setAttribute("errorMessage", e.getMessage());
            throw new AppException(e);
        }

        resp.sendRedirect(req.getContextPath() + ("/category-redactor/edit-category"));
    }
}
