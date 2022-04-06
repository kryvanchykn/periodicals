<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>

<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="Content-Language" content="en">
    <title>Title</title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet" type="text/css">

</head>
<body>
    <%@include file="WEB-INF/jsp/header.jspf" %>
    <div class="box" style="width: 400px;">
        <form class="lang-form">
            <select name="locale" class="lang-select">
                <c:forEach var="localeVar" items="${sessionScope.locales}">
                    <option class="option" ${sessionScope.locale == localeVar ? "selected" : ""}>${localeVar}</option>
                </c:forEach>
            </select>
        </form>
        <h2><fmt:message key="welcome.page.welcome.label"/>!</h2>
        <h4><fmt:message key="welcome.page.description.label"/>.</h4>
        <h4><fmt:message key="welcome.page.wish.label"/>!</h4>
    </div>
    <script src="${pageContext.request.contextPath}/static/js/select-lang.js"></script>

</body>
</html>