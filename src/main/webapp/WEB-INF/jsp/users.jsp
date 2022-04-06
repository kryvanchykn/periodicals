<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<%@ page import="com.periodicals.dao.utils.SqlConstants" %>

<%
    session.getAttribute(String.valueOf(SqlConstants.ADMIN));
%>

<jsp:useBean id="users" scope="session" type="java.util.List"/>
<jsp:useBean id="user" scope="session" type="com.periodicals.entities.User"/>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>


<html>
<head>
    <title><fmt:message key="users.header"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
    <%@include file="header.jspf" %>

    <div class="box magazine_redactor" style="width: 600px; margin-top: 3%;">
        <div class="table_wrapper">
            <h2><fmt:message key="users.all.user.label"/>:</h2>
            <table class="table">
                <thead>
                <tr>
                    <th><fmt:message key="users.login.label"/></th>
                    <th><fmt:message key="users.email.label"/></th>
                    <th><fmt:message key="users.phone.label"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${sessionScope.users}" var="userVar">
                    <tr>
                        <td><c:out value="${userVar.login}" /></td>
                        <td><c:out value="${userVar.email}" /></td>
                        <td><c:out value="${userVar.phone}"/></td>
                        <td>
                        <c:if test="${userVar.roleId != SqlConstants.ADMIN}">
                            <form class="redactor-button" method="post" action="${pageContext.request.contextPath}/users">
                                <input name="buttonValue" value="${userVar.roleId == 1 ? true : false}" style="display: none">
                                <input name="userId" value="${userVar.id}" style="display: none">
                                <fmt:message key="users.block.button" var="block"/>
                                <fmt:message key="users.unblock.button" var="unblock"/>
                                <input type="submit" name="" value="${userVar.roleId == 1 ? block : unblock}" />
                            </form>
                        </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</body>
</html>
