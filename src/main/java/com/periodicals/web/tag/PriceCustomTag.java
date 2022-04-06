package com.periodicals.web.tag;

import com.periodicals.dao.daoFactory.DaoFactory;
import com.periodicals.entities.Locale;
import com.periodicals.exceptions.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Defines tag for displaying price
 */
public class PriceCustomTag extends TagSupport {
    private static final Logger log = LogManager.getLogger(PriceCustomTag.class.getName());

    private  String localeName;
    private double price;

    @Override
    public int doStartTag() {
        JspWriter out = pageContext.getOut();

        try {
            Locale locale = DaoFactory.createLocaleDao().findLocaleByLang(localeName);
            out.print(String.format("%.2f", price/locale.getExchangeRate()) + locale.getPriceSign());
        } catch (IOException | DBException e) {
            log.error("Error in PriceCustomTag", e);        }

        return EVAL_BODY_INCLUDE;
    }

    public String getLocaleName() {
        return localeName;
    }

    public void setLocaleName(String localeName) {
        this.localeName = localeName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
