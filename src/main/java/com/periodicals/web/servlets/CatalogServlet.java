package com.periodicals.web.servlets;

import com.periodicals.dao.daoFactory.DaoFactory;
import com.periodicals.dao.utils.SqlConstants;
import com.periodicals.entities.Category;
import com.periodicals.entities.Locale;
import com.periodicals.entities.Magazine;
import com.periodicals.exceptions.DBException;
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

@WebServlet("/catalog")
public class CatalogServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(CatalogServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.setAttribute("sortTypes", SqlConstants.sortingTypes);

        int magazinesOnPage = req.getParameter("magazinesOnPage") == null ? 3 : Integer.parseInt(req.getParameter("magazinesOnPage"));
        int page = req.getParameter("magazinesOnPage") == null ? 0 : Integer.parseInt(req.getParameter("page"));
        String sortBy = req.getParameter("sortBy") == null ? "name" : req.getParameter("sortBy");
        String searchMagazine = req.getParameter("searchMagazine") == null ? "" : req.getParameter("searchMagazine");
        int categoryId = req.getParameter("category") == null ? 0 : Integer.parseInt(req.getParameter("category"));
        Locale locale = Locale.valueOf((String) session.getAttribute("locale"));

        log.info(1);
        try {
            List<Category> categories = DaoFactory.createCategoryDao().findAllCategories(locale.getId());
            session.setAttribute("categories", categories);

            List<Magazine> magazines;
            int maxPage;
            log.info(2);

            if (categoryId == 0) {
                magazines = DaoFactory.createMagazineDao().findSortedMagazinesOnPage(
                        sortBy, searchMagazine,  magazinesOnPage, page, locale.getId());
                maxPage = DaoFactory.createMagazineDao().getMagazinesNumber(searchMagazine, locale.getId());
            } else {
                magazines = DaoFactory.createMagazineDao().findSortedMagazinesByCategoryOnPage(
                        Integer.parseInt(req.getParameter("category")), sortBy, searchMagazine,
                        magazinesOnPage, page, locale.getId());
                maxPage = DaoFactory.createMagazineDao().getMagazinesNumberInCategory(
                        categoryId, searchMagazine, locale.getId());
            }
            maxPage = maxPage != 0 && maxPage % magazinesOnPage == 0 ? maxPage / magazinesOnPage - 1 : maxPage / magazinesOnPage;
            log.info(3);

            session.setAttribute("maxPage", maxPage);
            session.setAttribute("magazines", magazines);
            session.setAttribute("sortBy", sortBy);
            session.setAttribute("searchMagazine", searchMagazine);
        } catch (DBException e) {
            log.error("Exception: " + e.getMessage());
        }

        req.getRequestDispatcher("/WEB-INF/jsp/catalog.jsp").forward(req, resp);
    }


}
