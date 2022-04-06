package com.periodicals.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DataValidationTest {

    @ParameterizedTest
    @MethodSource("invalidLogin")
    void testInvalidLogin(String login){
        assertThrows(IllegalArgumentException.class, () -> DataValidator.validateLogin(login));
    }

    @ParameterizedTest
    @MethodSource("validLogin")
    void testValidLogin(String login){
        Assertions.assertDoesNotThrow(() -> DataValidator.validateLogin(login));
    }


    @ParameterizedTest
    @MethodSource("invalidEmail")
    void testInvalidEmail(String email){
        assertThrows(IllegalArgumentException.class, () -> DataValidator.validateEmail(email));
    }

    @ParameterizedTest
    @MethodSource("validEmail")
    void testValidEmail(String email){
        Assertions.assertDoesNotThrow(() -> DataValidator.validateEmail(email));
    }


    @ParameterizedTest
    @MethodSource("invalidPhoneNumber")
    void testInvalidPhoneNumber(String phone){
        assertThrows(IllegalArgumentException.class, () -> DataValidator.validatePhoneNumber(phone));
    }

    @ParameterizedTest
    @MethodSource("validPhoneNumber")
    void testValidPhoneNumber(String phone){
        Assertions.assertDoesNotThrow(() -> DataValidator.validatePhoneNumber(phone));
    }


    @ParameterizedTest
    @MethodSource("invalidPassword")
    void testInvalidPassword(String password){
        assertThrows(IllegalArgumentException.class, () -> DataValidator.validatePassword(password));
    }

    @ParameterizedTest
    @MethodSource("validPassword")
    void testValidPassword(String amount){
        Assertions.assertDoesNotThrow(() -> DataValidator.validatePassword(amount));
    }


    @ParameterizedTest
    @MethodSource("invalidMoneyAmount")
    void testInvalidMoneyAmount(String amount){
        assertThrows(IllegalArgumentException.class, () -> DataValidator.validateMoneyAmount(amount));
    }

    @ParameterizedTest
    @MethodSource("validMoneyAmount")
    void testValidMoneyAmount(String password){
        Assertions.assertDoesNotThrow(() -> DataValidator.validateMoneyAmount(password));
    }


    @ParameterizedTest
    @MethodSource("invalidString")
    void testInvalidString(String string){
        assertThrows(IllegalArgumentException.class, () -> DataValidator.validateString(string));
    }

    @ParameterizedTest
    @MethodSource("validString")
    void testValidString(String string){
        Assertions.assertDoesNotThrow(() -> DataValidator.validateString(string));
    }


    @ParameterizedTest
    @MethodSource("invalidText")
    void testInvalidText(String text){
        assertThrows(IllegalArgumentException.class, () -> DataValidator.validateText(text));
    }

    @ParameterizedTest
    @MethodSource("validText")
    void testValidText(String text){
        Assertions.assertDoesNotThrow(() -> DataValidator.validateText(text));
    }


    @ParameterizedTest
    @MethodSource("invalidURL")
    void testInvalidURL(String url){
        assertThrows(IllegalArgumentException.class, () -> DataValidator.validateURL(url));
    }

    @ParameterizedTest
    @MethodSource("validURL")
    void testValidURL(String url){
        Assertions.assertDoesNotThrow(() -> DataValidator.validateURL(url));
    }


    static List<String> invalidLogin() {
        return Arrays.asList("", "   ", "a", "01234567890123456789a", ".", "!!!", "абвгд", "login_", "log in", "log..in");
    }

    static List<String> validLogin(){
        return Arrays.asList("login", "LOGIN", "login1", "Login", "log.in", "log_in", "log-in", "login_123", "12345");
    }


    static List<String> invalidEmail() {
        return Arrays.asList("", "   ", "a", "012345678", ".", "!!!", "абвгд", "abc.def@mail.c", "abc..def@mail.com",
                ".abc@mail.com");
    }

    static List<String> validEmail(){
        return Arrays.asList("abc-d@mail.com", "abc.def@mail.com", "abc@mail.com", "abc_def@mail.com", "abc.def@mail-archive.com",
                "abc.def@mail.org", "abc.def@mail.com");
    }


    static List<String> invalidPhoneNumber() {
        return Arrays.asList("", "   ", "a", "0123", "012345678", "(012)-34-56-789", "123-456-78-99", "012-345-67-890",
                "012-34-56-789");
    }

    static List<String> validPhoneNumber(){
        return Arrays.asList("012-345-67-89", "011-111-11-11");
    }


    static List<String> invalidPassword() {
        return Arrays.asList("", "   ", "a", "0123", "012345678", "password1.", "Password1", "Password.",
                "PASSWORD1.", "Pass word1.", "пароль1.", "Password00000000000000000");
    }

    static List<String> validPassword(){
        return Arrays.asList("Password1.", "Password1!", "Password1@", "Password1$", "Пароль1%", "Пароль1&",
                "Пароль1&", "Пароль1*", "ПАРОЛь123()", "пАААроль1-+=", "паролЬ1^");
    }


    static List<String> invalidMoneyAmount() {
        return Arrays.asList("", "   ", "a", "12_34", "12.34a", "12,34a", "a12.34", "12,345", "12.345", "-12.34",
                "12..34", "1, 23", "1 .23", "1.23 ");

    }

    static List<String> validMoneyAmount(){
        return Arrays.asList("1.23", "1,23", "12.34", "12.34", "0.10", "100.2", "100");
    }


    static List<String> invalidString() {
       return Arrays.asList("", "   ", "...", makeStringByLength(50));
    }

    static List<String> validString(){
        return Arrays.asList("a", "a ", "a.", "a123", "aaa", "a123...", makeStringByLength(45));
    }

    static List<String> invalidText() {
        return Arrays.asList("", "   ", "...", makeStringByLength(600));
    }

    static List<String> validText(){
        return Arrays.asList("a", "a ", "a.", "a123", "aaa", "a123...", makeStringByLength(500));
    }

    static List<String> invalidURL() {
        return Arrays.asList("", "   ", "...", "aaaa", "a123", "https://", "https://abc");
    }

    static List<String> validURL(){
        return Arrays.asList("https://www.apple.com/", "https://www.apple.com/mac/",  "https://study.com/",
                "https://www.apple.com/shop/buy-mac/macbook-pro/14-inch-space-gray-10-core-cpu-16-core-gpu-1tb#",
               "https://mail.google.com/mail/u/0/?fs=1&tf=cm&source=mailto&su=Question+from+Website&to=questions@ocps.net");
    }


    static String makeStringByLength(int length){
        return IntStream.range(0, length).mapToObj(i -> "a").collect(Collectors.joining(""));
    }
}
