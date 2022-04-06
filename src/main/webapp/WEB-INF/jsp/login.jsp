<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>

<html>
<head>
    <title><fmt:message key="login.title"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet" type="text/css">
</head>

<body>
    <%@include file="header.jspf" %>

    <form class="box" action="login" method="post" id="form" style="margin-top: 3%">
        <h1><fmt:message key="login.label"/></h1>
        <input type="text" name="login" id="login" placeholder="<fmt:message key="login.username.placeholder"/>" required>
        <input type="password" name="password" id="password" placeholder="<fmt:message key="login.password.placeholder"/>" required>
        <div class="g-recaptcha" data-sitekey="6Le1Ck4fAAAAACQqpJt-M9PZGvk-_5kgcIeS3BBT"></div>
        <div id="error"></div>
        <input type="submit" name="" value="<fmt:message key="login.submit.button"/>">
        <a href="${pageContext.request.contextPath}/signup" style="color: #f2f2f2" ><fmt:message key="login.message.ask.link"/>?</a>
    </form>

    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    <script src="${pageContext.request.contextPath}/static/js/recaptcha.js"></script>
</body>
</html>
