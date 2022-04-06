<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>


<html>
<head>
    <title><fmt:message key="signup.title"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet" type="text/css">
</head>

<body>
    <%@include file="header.jspf" %>

    <form class="box" action="signup" method="post" style="margin-top: 3%;">
        <h1><fmt:message key="signup.label"/></h1>
        <div class="tooltip">
            <input type="text" name="login" id="login" placeholder="<fmt:message key="login.username.placeholder"/>" required="">
            <span class="tooltiptext"><fmt:message key="signup.login.input.hint"/></span>
        </div>
        <input type="text" name="email" id="email" placeholder="<fmt:message key="signup.email.placeholder"/>" required="">
        <div class="tooltip">
            <input type="text" name="phone" id="phone" placeholder="<fmt:message key="signup.phone.placeholder"/>" required="">
            <span class="tooltiptext"><fmt:message key="signup.phone.input.hint"/>: <br> 0XX-XXX-XX-XX</span>
        </div>
        <div class="tooltip">
            <input type="password" name="password" id="password" placeholder="<fmt:message key="login.password.placeholder"/>" required="">
            <span class="tooltiptext"><fmt:message key="signup.password.input.hint"/></span>
        </div>
        <input type="password" name="repeatPassword" id="repeatPassword" placeholder="<fmt:message key="signup.repeat.password.placeholder"/>" required="">
        <input type="submit" name="" value="<fmt:message key="signup.submit.button"/>">
        <span><a href="${pageContext.request.contextPath}/login" style="color: white" ><fmt:message key="signup.message.ask.link"/>?</a></span>
    </form>
</body>
</html>
