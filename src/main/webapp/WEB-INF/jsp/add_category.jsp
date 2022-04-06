<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>

<html>
<head>
    <title>
        <fmt:message key="header.category.redactor.link"/>
    </title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<%@include file="header.jspf" %>

<div class="create_magazine_wrapper">
    <form method="post" action="${pageContext.request.contextPath}/category-redactor/add-category" class="create_magazine_box" style="height: auto;">
        <h2><fmt:message key="header.category.redactor.link"/></h2>
        <br>

        <c:forEach var="localeVar" items="${sessionScope.locales}">
            <div class="localized">
                <input name="name_${fn:toLowerCase(localeVar)}" id="name_${fn:toLowerCase(localeVar)}"
                       type="text"
                       placeholder="<fmt:message key="redactor.name.placeholder"/>${fn:toLowerCase(localeVar)}" required/>

               <br>
            </div>
        </c:forEach>

        <input name="action" value="1" style="display: none">
        <button type="submit"><fmt:message key="category.redactor.add.category.button"/></button>
    </form>
</div>

</body>
</html>
