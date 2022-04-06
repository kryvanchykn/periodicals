<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<jsp:useBean id="categories" scope="session" type="java.util.List"/>
<jsp:useBean id="sortTypes" scope="session" type="java.util.HashMap"/>
<jsp:useBean id="magazines" scope="session" type="java.util.List"/>
<jsp:useBean id="searchMagazine" scope="session" type="java.lang.String"/>
<jsp:useBean id="maxPage" scope="session" type="java.lang.Integer"/>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>

<html>
<head>
    <title><fmt:message key="header.catalog.link"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
    <%@include file="header.jspf" %>

    <div class="select_sort">
        <form class="select-sort-form" action="<c:url value="/catalog"/>" method="get">
            <span>
                <input type="search" name="searchMagazine" placeholder= <fmt:message key="catalog.search.magazine.placeholder"/>>
            </span>

            <span>
                <fmt:message key="catalog.category.select"/>:
                <label>
                    <select name="category" class="form-select">
                        <option value="0"><fmt:message key="catalog.order.param.all.magazines"/></option>
                        <c:forEach var="category" items="${categories}">
                            <option ${param.category == category.id ? "selected" : ""} value="${category.id}">
                                    ${category.name}
                            </option>
                        </c:forEach>
                    </select>
                </label>
            </span>

            <span>
                <fmt:message key="catalog.sort.select"/>:
                <select name="sortBy" class="form-select">
                    <c:forEach var="sort" items="${sortTypes}">
                        <option ${param.sortBy == sort.value ? "selected" : ""} value="${sort.value}"><fmt:message key="${sort.key}"/></option>
                    </c:forEach>
                </select>
            </span>

            <span>
                <fmt:message key="catalog.show.on.page.select"/>:
                <select name="magazinesOnPage" class="form-select">
                    <c:forTokens items="3,6,9" delims="," var="item">
                        <option ${param.magazinesOnPage == item ? "selected" : ""}>${item}</option>
                    </c:forTokens>
                </select>
            </span>

            <select name="page" style="display: none">
                <option value="0" selected></option>
            </select>

            <input type="submit" value=<fmt:message key="catalog.show.button"/> />
        </form>
    </div>

    <c:set var="magazines" value="${magazines}" scope="session"/>
    <div class="magazines-wrapper">
        <div class="magazines">
            <c:forEach var="magazine" items="${magazines}">
                <%@include file="magazines_for_catalog.jspf" %>
            </c:forEach>
        </div>
    </div>


    <nav class="pagination">
        <span class="first-page">
            <a class=${param.page == 0 ? "disabled" : "page-active"}
               href="${pageContext.request.contextPath}/catalog?category=${param.category}&sortBy=${param.sortBy}&page=${0}&magazinesOnPage=${param.magazinesOnPage}">&laquo;</a>
        </span>
        <span class="page">
            <a class=${param.page == 0 ? "disabled" : "page-active"}
               href="${pageContext.request.contextPath}/catalog?category=${param.category}&sortBy=${param.sortBy}&page=${param.page-1}&magazinesOnPage=${param.magazinesOnPage}"
               tabindex="-1">
                <fmt:message key="pagination.previous"/>
            </a>
        </span>
        <span class="all-pages">
        <c:forEach var="num" begin="0" end="${maxPage}">
            <a class=${param.page == num ? "current-page" : "page-active"}
               href="${pageContext.request.contextPath}/catalog?category=${param.category}&sortBy=${param.sortBy}&page=${num}&magazinesOnPage=${param.magazinesOnPage}"> ${num+1} </a>
        </c:forEach>
        </span>
        <span class="page">
            <a class=${param.page == maxPage ? "disabled" : "page-active"}
               href="${pageContext.request.contextPath}/catalog?category=${param.category}&sortBy=${param.sortBy}&page=${param.page+1}&magazinesOnPage=${param.magazinesOnPage}">
                <fmt:message key="pagination.next"/>
            </a>
        </span>
        <span class="last-page">
            <a class=${param.page == maxPage ? "disabled" : "page-active"}
                href="${pageContext.request.contextPath}/catalog?category=${param.category}&sortBy=${param.sortBy}&page=${maxPage}&magazinesOnPage=${param.magazinesOnPage}">&raquo;</a>
        </span>

    </nav>

</body>
</html>
