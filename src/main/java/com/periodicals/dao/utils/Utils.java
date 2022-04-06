package com.periodicals.dao.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    /**
     * hash array of bytes using SHA-512
     *
     * @param inputBytes what to encode
     * @return hashed string
     */
    public static String hash(byte[] inputBytes) {
        String hashValue;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(inputBytes);
            byte[] digestedBytes = md.digest();
            hashValue = DatatypeConverter.printHexBinary(digestedBytes).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        return hashValue;
    }

    /**
     * compare passwords
     * @return true if passwords are equal, false otherwise
     */
    public static boolean comparePasswords(String password1, String password2){
        return password1.equals(password2);
    }

    /**
     * invalidate session and redirect to login page
     */
    public static void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String lang = (String) req.getSession().getAttribute("locale");
        req.getSession().invalidate();
        req.getSession().setAttribute("locale", lang);
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
