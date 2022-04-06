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
import java.util.HashMap;
import java.util.List;

@WebServlet("/magazines-redactor")
public class MagazinesRedactorServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(MagazinesRedactorServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Locale locale = Locale.valueOf((String) session.getAttribute("locale"));

        try {
            List<Magazine> magazines;
            HashMap<Magazine, String> magazineCategoryMap = new HashMap<>();
            magazines = DaoFactory.createMagazineDao().findAllMagazines(locale.getId());

            for(Magazine m: magazines){
                magazineCategoryMap.put(m,
                        DaoFactory.createCategoryDao().findCategoryById(m.getCategoryId()).getNames().get(locale.getId()));
            }
            session.setAttribute("magazineCategoryMap", magazineCategoryMap);

        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/jsp/magazines_redactor.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();

        try {
            int buttonValue = Integer.parseInt(req.getParameter("buttonValue"));
            switch (buttonValue) {
                case -1 -> {
                    log.info("delete magazineId = " + req.getParameter("magazineId"));
                    DaoFactory.createMagazineDao().deleteMagazine(Integer.parseInt(req.getParameter("magazineId")));
                    resp.sendRedirect(req.getContextPath() + ("/magazines-redactor"));
                }
                case 0 -> {
                    int magazineId = Integer.parseInt(req.getParameter("magazineId"));
                    MagazineLocalization magazineLocalization = DaoFactory.createMagazineDao().findMagazineById(magazineId);
                    session.setAttribute("magazineId", magazineId);
                    session.setAttribute("magazineLocalization", magazineLocalization);
                    resp.sendRedirect(req.getContextPath() + ("/magazines-redactor/edit-magazine"));
                }
            }
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
        }
    }
}

