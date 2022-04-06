<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="pricetag" uri="/WEB-INF/tld/price.tld" %>
<%@ page isELIgnored="false" %>
<%@ page import="com.periodicals.dao.utils.SqlConstants" %>

<%
    session.getAttribute(String.valueOf(SqlConstants.ADMIN));
    session.getAttribute(String.valueOf(SqlConstants.BLOCKED_READER));
%>

<jsp:useBean id="user" scope="session" type="com.periodicals.entities.User"/>
<jsp:useBean id="balance" scope="session" type="java.lang.Double"/>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>


<html>
<head>
    <title><fmt:message key="profile.header"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet" type="text/css">
</head>

<body>
    <%@include file="header.jspf" %>
    <div class="box" style="margin-top: 3%;">
        <form class="lang-form">
            <select name="locale" class="lang-select">
                <c:forEach var="localeVar" items="${sessionScope.locales}">
                    <option class="option" ${sessionScope.locale == localeVar ? "selected" : ""}>${localeVar}</option>
                </c:forEach>
            </select>
        </form>
        <form action="profile/replenish-balance" method="get">
            <c:if test="${user.roleId == SqlConstants.BLOCKED_READER}">
                <h3 style="color: #f2f2f2"><fmt:message key="profile.blocked.user.label"/></h3>
            </c:if>
            <h2><fmt:message key="profile.header"/>: </h2>
            <img src="${pageContext.request.contextPath}/static/images/profileIcon.png" alt="Avatar" style="width:100%">
            <h4><fmt:message key="profile.username"/>: ${user.login}</h4>
            <h4><fmt:message key="profile.email"/>: ${user.email}</h4>
            <h4><fmt:message key="profile.phone"/>: ${user.phone}</h4>
            <c:if test="${user.roleId == SqlConstants.READER}">
                <h4><fmt:message key="profile.balance.label"/>:
                    <pricetag:priceTag
                        localeName = "${sessionScope.locale}"
                        price = "${user.balance}"
                    >
                    </pricetag:priceTag>
                </h4>
                <input type="submit" name="" value="<fmt:message key="profile.replenish.balance.button"/>">
            </c:if>
            <c:if test="${user.roleId == SqlConstants.ADMIN}">
                <h4><strong><fmt:message key="profile.role.label"/></strong></h4>
            </c:if>

            <div class="logout">
                <a href="${pageContext.request.contextPath}/profile?logout="><fmt:message key="profile.logout.button"/></a>
            </div>
        </form>
    </div>


    <script src="${pageContext.request.contextPath}/static/js/select-lang.js"></script>
</body>
</html>