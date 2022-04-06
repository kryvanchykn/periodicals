<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="pricetag" uri="/WEB-INF/tld/price.tld" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>

<jsp:useBean id="user" scope="session" type="com.periodicals.entities.User"/>
<jsp:useBean id="balance" scope="session" type="java.lang.Double"/>

<html>
<head>
    <title><fmt:message key="profile.replenish.balance.button"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
    <%@include file="header.jspf" %>

    <form class="box" action="${pageContext.request.contextPath}/profile/replenish-balance" method="post">
        <h2><fmt:message key="replenish.topup.user.balance.label"/>: ${user.login}</h2>
        <h4><fmt:message key="replenish.current.balance.label"/>:
            <pricetag:priceTag
                    localeName = "${sessionScope.locale}"
                    price = "${user.balance}"
            >
            </pricetag:priceTag>
        </h4>
        <h4><fmt:message key="replenish.topup.sum.label"/>
            <div class="tooltip">
                <input type="text" name="sum" id="sum" required>
                <span class="tooltiptext"><fmt:message key="replenish.topup.sum.input.hint"/></span>
            </div>
        </h4>
        <input type="submit" name="replenishBalance" value="<fmt:message key="replenish.save.changes.button"/>">
    </form>
</body>
</html>
