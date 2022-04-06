<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<jsp:useBean id="magazineName" scope="session" type="java.lang.String"/>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>


<html>
<head>
    <title><fmt:message key="subscription.subscribe"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet" type="text/css">
</head>

<body>
    <%@include file="header.jspf" %>

    <div class="box">
        <h4><fmt:message key="subscription.already.subscribed.label"/> ${magazineName}!</h4>
    </div>
</body>
</html>
