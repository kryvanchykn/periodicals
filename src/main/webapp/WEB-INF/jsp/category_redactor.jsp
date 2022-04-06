<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page isELIgnored="false" %>

<jsp:useBean id="categoryLocalizations" scope="session" type="java.util.List"/>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>


<html>
<head>
    <title><fmt:message key="category.redactor.header"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<%@include file="header.jspf" %>

<div class="box magazines_redactor" style="width: 600px;">
    <c:if test="${sessionScope.categories.size() == 0}">
        <h2><fmt:message key="category.redactor.no.categories.label"/></h2>
    </c:if>

    <c:if test="${sessionScope.categories.size() != 0}">
        <div class="table_wrapper">
            <h2><fmt:message key="category.redactor.categories.label"/>:</h2>
            <table class="table">
                <thead>
                <tr>
                    <c:forEach var="localeVar" items="${sessionScope.locales}">
                        <th><fmt:message key="redactor.name.placeholder"/>${fn:toLowerCase(localeVar)}</th>
                    </c:forEach>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="category" items="${sessionScope.categoryLocalizations}" >
                    <tr>
                        <c:forEach var="localeVar" items="${sessionScope.locales}">
                            <td><c:out value="${category.names.get(localeVar.id)}" /></td>
                        </c:forEach>
                        <td>
                            <form class="redactor-button" method="post" action="${pageContext.request.contextPath}/category-redactor">
                                <input name="buttonValue" value="0" style="display: none">
                                <input name="categoryId" value="${category.id}" style="display: none">
                                <input type="submit" name="" value="<fmt:message key="redactor.edit.button"/>">
                            </form>
                        </td>
                        <td>
                            <form class="redactor-button" method="post" action="${pageContext.request.contextPath}/category-redactor">
                                <input name="buttonValue" value="-1" style="display: none">
                                <input name="categoryId" value="${category.id}" style="display: none">
                                <input type="submit" name="" value="<fmt:message key="redactor.delete.button"/>">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
</div>

<form class="box redactor-button-add-magazine" method="get" style="position: absolute; top: 500px; padding: 0 40px; width: 600px;"
      action="${pageContext.request.contextPath}/category-redactor/add-category">
    <input name="buttonValue" value="1" style="display: none">
    <input type="submit" name="" value="<fmt:message key="category.redactor.add.category.button"/>">
</form>

</body>
</html>
