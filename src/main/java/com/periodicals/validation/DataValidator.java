package com.periodicals.validation;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;


import java.util.regex.Pattern;

/**
 * The {@code DataValidator} class contains methods for
 * validating an input data (data from forms).
 */
public class DataValidator {

    /**
     * The {@code String} value that is pattern for login. Login contains from 5 to 20 symbols,
     * latin letters, digits and dots, underscores or hyphens. Starts with an alphanumeric character.
     * Special symbols do not appear consecutively.
     */
    private final static String REGEX_CHECK_FOR_LOGIN = "^[a-zA-Z0-9]" +
            "([._-](?![._-])|" +
            "[a-zA-Z0-9]){3,18}" +
            "[a-zA-Z0-9]$";

    /**
     * The {@code String} value that is pattern for phone number. Phone number matches the format 0XX-XXX-XX-XX
     * and contains only digits
     */
    private final static String REGEX_CHECK_FOR_PHONE_NUMBER = "0[0-9]{2}-\\d{3}-\\d{2}-\\d{2}";

    /**
     * The {@code String} value that is pattern for password. Password contains latin or cyrillic letters(8-20),
     * at least one digit, one upper case and one lower case alphabet and special symbol (!@#$%&*()-+=^.)
     */
    private final static String REGEX_CHECK_FOR_PASSWORD = "^(?=.*[0-9])" +
            "((?=.*[a-z])|(?=.*[а-я]))" +
            "((?=.*[A-Z])|(?=.*[А-Я]))" +
            "(?=.*[.!@#$%&*()+=^-])" +
            "(?=\\S+$).{8,20}$";

    /**
     * The {@code String} value that is pattern for the amount of money. The amount of money contains integer or double value
     * with no more than two digits after decimal point(it can be dot or comma)
     */
    private final static String REGEX_CHECK_FOR_MONEY_AMOUNT = "^(\\d+((\\.|,)\\d{0,2})?)$";

    /**
     * The {@code String} value that is pattern for the string. String contains from 1 to 45 symbols.
     * It can be latin or cyrillic letters and digits
     */
    private final static String REGEX_CHECK_FOR_STRING = "^[a-zA-Zа-яА-Я0-9].{0,45}$";

    /**
     * The {@code String} value that is pattern for the text field. String contains from 1 to 500 symbols.
     * It can be latin or cyrillic letters
     */
    private final static String REGEX_CHECK_FOR_TEXT = "^[a-zA-Zа-яА-Я0-9].{0,500}$";

    /**
     * Validates input login, if it matches pattern.
     * @param login login input value.
     * @exception IllegalArgumentException login is invalid.
     */
    public static void validateLogin(String login) {
        if (!Pattern.matches(REGEX_CHECK_FOR_LOGIN, login)){
            throw new IllegalArgumentException("error.page.invalid.login");
        }
    }

    /**
     * Validates input email, if it matches pattern.
     * @param email email input value.
     * @exception IllegalArgumentException email is invalid.
     */
    public static void validateEmail(String email) {
        if (!EmailValidator.getInstance().isValid(email)){
            throw new IllegalArgumentException("error.page.invalid.email");
        }
    }

    /**
     * Validates input phone number, if it matches pattern.
     * @param phone phone number input value.
     * @exception IllegalArgumentException phone number is invalid.
     */
    public static void validatePhoneNumber(String phone) {
        if (!Pattern.matches(REGEX_CHECK_FOR_PHONE_NUMBER, phone)){
            throw new IllegalArgumentException("error.page.invalid.phone.number");
        }
    }

    /**
     * Validates input password, if it matches pattern.
     * @param password password input value.
     * @exception IllegalArgumentException password is invalid.
     */
    public static void validatePassword(String password) {
        if (!Pattern.matches(REGEX_CHECK_FOR_PASSWORD, password)){
            throw new IllegalArgumentException("error.page.invalid.password");
        }
    }

    /**
     * Validates input amount of money, if it matches pattern.
     * @param amount amount of money input value.
     * @exception IllegalArgumentException amount of money is invalid.
     */
    public static void validateMoneyAmount(String amount) {
        if (!Pattern.matches(REGEX_CHECK_FOR_MONEY_AMOUNT, amount)){
            throw new IllegalArgumentException("error.page.invalid.number");
        }
    }

    /**
     * Validates input string, if it matches pattern.
     * @param string string input value.
     * @exception IllegalArgumentException string is invalid.
     */
    public static void validateString(String  string) {
        if (!Pattern.matches(REGEX_CHECK_FOR_STRING, string)){
            throw new IllegalArgumentException("error.page.invalid.string");
        }
    }

    /**
     * Validates input text, if it matches pattern.
     * @param text text input value.
     * @exception IllegalArgumentException text is invalid.
     */
    public static void validateText(String text) {
        if (!Pattern.matches(REGEX_CHECK_FOR_TEXT, text)){
            throw new IllegalArgumentException("error.page.invalid.text");
        }
    }

    /**
     * Validates input url, if it matches pattern.
     * @param url url input value.
     * @exception IllegalArgumentException url is invalid.
     */
    public static void validateURL(String url) {
        if (!new UrlValidator().isValid(url)){
            throw new IllegalArgumentException("error.page.invalid.url");
        }
    }

}
