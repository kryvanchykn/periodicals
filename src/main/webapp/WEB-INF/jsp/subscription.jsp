<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>


<html>
<head>
    <title><fmt:message key="subscriptions.label"/></title
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet" type="text/css">
</head>

<body>
    <%@include file="header.jspf" %>

    <div class="box magazines_redactor" style="width: 45%; height: 30%;">

        <c:if test="${sessionScope.subscriptionInfo.size() == 0  && (sessionScope.isActive == null || sessionScope.isActive) == true}">
            <h2><fmt:message key="subscriptions.no.active.subscriptions.label"/></h2>
        </c:if>

        <c:if test="${sessionScope.subscriptionInfo.size() == 0  && sessionScope.isActive == false}">
            <h2><fmt:message key="subscriptions.no.expired.subscriptions.label"/></h2>
        </c:if>

        <c:if test="${sessionScope.subscriptionInfo.size() != 0}">
            <div class="table_wrapper">
                <h2><fmt:message key="subscriptions.label"/>:</h2>
                <table class="table">
                    <thead>
                    <tr>
                        <th><fmt:message key="subscriptions.name.label"/></th>
                        <th><fmt:message key="subscriptions.category.label"/></th>
                        <th><fmt:message key="subscriptions.start.date.label"/></th>
                        <th><fmt:message key="subscriptions.end.date.label"/></th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${sessionScope.subscriptionInfo}" var="subscriptionInfo">
                        <tr>
                            <td><c:out value="${subscriptionInfo.magazineName}" /></td>
                            <td><c:out value="${subscriptionInfo.magazineCategory}" /></td>
                            <td><c:out value="${subscriptionInfo.startDate}" /></td>
                            <td><c:out value="${subscriptionInfo.endDate}" /></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

        <div class=${sessionScope.subscriptionInfo.size() != 0 ? "subscription-buttons" : "subscription-buttons-empty"}>
            <form class="subscription-button" method="post" action="${pageContext.request.contextPath}/profile/subscription">
                <input name="isActive" value="true" style="display: none">
                <input type="submit" name="" value="<fmt:message key="subscriptions.active.button"/>">
            </form>
            <form class="subscription-button" method="post" action="${pageContext.request.contextPath}/profile/subscription">
                <input name="isActive" value="false" style="display: none">
                <input type="submit" name="" value="<fmt:message key="subscriptions.expired.button"/>">
            </form>
        </div>
    </div>

</body>
</html>