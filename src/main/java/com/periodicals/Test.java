package com.periodicals;

import com.periodicals.dao.daoFactory.DaoFactory;
import com.periodicals.entities.CategoryLocalization;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet("/test")
public class Test extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Map<Integer, String> map = new HashMap<>();
            map.put(1, "news");
            map.put(2, "новини");
            CategoryLocalization c = new CategoryLocalization();
            c.setNames(map);
            resp.getWriter().println(DaoFactory.createCategoryDao().findAllCategories(1));
        } catch (Exception e) {
            resp.getWriter().println(e);
            e.printStackTrace();
        }
    }

}
