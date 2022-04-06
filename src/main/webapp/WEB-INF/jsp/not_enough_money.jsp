<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="pricetag" uri="/WEB-INF/tld/price.tld" %>
<%@ page isELIgnored="false" %>

<jsp:useBean id="notEnoughMoney" scope="session" type="java.lang.Double"/>
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
        <h4><fmt:message key="subscription.lack.money.label"/>!</h4>
        <h4><fmt:message key="subscription.replenish.balance.for.label"/> <pricetag:priceTag
                localeName = "${sessionScope.locale}"
                price = "${notEnoughMoney}"
        >
        </pricetag:priceTag> <fmt:message key="subscription.to.subscribe.label"/> ${magazineName}.</h4>
        <a href="${pageContext.request.contextPath}/profile/replenish-balance"><fmt:message key="subscription.top.up.balance.link"/></a>
    </div>
</body>
</html>
