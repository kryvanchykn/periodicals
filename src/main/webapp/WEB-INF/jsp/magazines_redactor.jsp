<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="pricetag" uri="/WEB-INF/tld/price.tld" %>

<%@ page isELIgnored="false" %>

<jsp:useBean id="magazineCategoryMap" scope="session" type="java.util.HashMap"/>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>


<html>
<head>
    <title><fmt:message key="magazines.redactor.header"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
    <%@include file="header.jspf" %>

    <div class="box magazines_redactor">
        <c:if test="${sessionScope.magazineCategoryMap.entrySet().size() == 0}">
            <h2><fmt:message key="magazines.redactor.no.magazines.label"/></h2>
        </c:if>

        <c:if test="${sessionScope.magazineCategoryMap.entrySet().size() != 0}">
            <div class="table_wrapper">
                <h2><fmt:message key="magazines.redactor.magazines.label"/>:</h2>
                <table class="table">
                    <thead>
                    <tr>
                        <th><fmt:message key="magazines.redactor.table.name.label"/></th>
                        <th><fmt:message key="magazines.redactor.table.category.label"/></th>
                        <th><fmt:message key="magazines.redactor.table.publisher.label"/></th>
                        <th><fmt:message key="magazines.redactor.table.price.label"/></th>
                        <th><fmt:message key="magazines.redactor.table.publication.date.label"/></th>
                        <th><fmt:message key="magazines.redactor.table.description.label"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${sessionScope.magazineCategoryMap.entrySet()}" var="magazineCategoryMap">
                        <tr>
                            <td><c:out value="${magazineCategoryMap.key.name}" /></td>
                            <td><c:out value="${magazineCategoryMap.value}" /></td>
                            <td><c:out value="${magazineCategoryMap.key.publisher}" /></td>

                            <td><pricetag:priceTag
                                    localeName = "${sessionScope.locale}"
                                    price = "${magazineCategoryMap.key.price}"
                            >
                            </pricetag:priceTag></td>
                            <td><c:out value="${magazineCategoryMap.key.publicationDate}" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${magazineCategoryMap.key.description.length() < 35}">
                                        ${magazineCategoryMap.key.description}
                                    </c:when>
                                    <c:otherwise>
                                        ${fn:substring(magazineCategoryMap.key.description, 0, 35)}...
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <form class="redactor-button" method="post" action="${pageContext.request.contextPath}/magazines-redactor">
                                    <input name="buttonValue" value="0" style="display: none">
                                    <input name="magazineId" value="${magazineCategoryMap.key.id}" style="display: none">
                                    <input type="submit" name="" value="<fmt:message key="redactor.edit.button"/>">
                                </form>
                            </td>
                            <td>
                                <form class="redactor-button" method="post" action="${pageContext.request.contextPath}/magazines-redactor">
                                    <input name="buttonValue" value="-1" style="display: none">
                                    <input name="magazineId" value="${magazineCategoryMap.key.id}" style="display: none">
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

    <form class=" box redactor-button-add-magazine" method="get" style="position: absolute; top: 500px; padding: 0 40px; width: 800px;"
          action="${pageContext.request.contextPath}/magazines-redactor/add-magazine">
        <input name="buttonValue" value="1" style="display: none">
        <input type="submit" name="" value="<fmt:message key="magazines.redactor.add.magazine.button"/>">
    </form>

</body>
</html>
