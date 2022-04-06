
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>

<html>
<head>
    <title><fmt:message key="error.page.title"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet" type="text/css">

</head>

<body>
<%@include file="header.jspf" %>

    <div class="box" style="width: 400px;">
        <h2><fmt:message key="error.page.wrong.label"/></h2>
        <h3><fmt:message key="error.page.try.again.label"/></h3>
        <h4><fmt:message key="${sessionScope.errorMessage}"/></h4>
        <h4>${sessionScope.errorMessage}</h4>
    </div>

</body>
</html>