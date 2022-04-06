<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>

<jsp:useBean id="todayDate" class="java.util.Date"/>
<jsp:useBean id="categories" scope="session" type="java.util.List"/>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>

<html>
<head>
    <title>
       <fmt:message key="redactor.add.magazine.header"/>
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
        <form method="post" action="${pageContext.request.contextPath}/magazines-redactor/add-magazine" class="create_magazine_box">
            <h2><fmt:message key="redactor.add.magazine.header"/>            </h2>
            <br>

            <c:forEach var="localeVar" items="${sessionScope.locales}">
                <div class="localized">
                    <input name="name_${fn:toLowerCase(localeVar)}" id="name_${fn:toLowerCase(localeVar)}"
                           type="text"
                           placeholder="<fmt:message key="redactor.name.placeholder"/>${fn:toLowerCase(localeVar)}" required>

                    <textarea name="description_${fn:toLowerCase(localeVar)}" class=""
                              id="description_${fn:toLowerCase(localeVar)}" cols="30" rows="10"
                              placeholder="<fmt:message key="redactor.description.placeholder"/>${fn:toLowerCase(localeVar)}" required></textarea>

                    <input name="publisher_${fn:toLowerCase(localeVar)}" id="name_${fn:toLowerCase(localeVar)}"
                           type="text" placeholder="<fmt:message key="redactor.publisher.placeholder"/>${fn:toLowerCase(localeVar)}" required>
                    <br>
                </div>
            </c:forEach>

            <span style="color: white;"><fmt:message key="redactor.category.label"/>:</span>
            <label>
                <select name="category_id" class="category_select" required>
                    <c:forEach var="category" items="${categories}">
                        <option value="${category.id}">
                                ${category.name}
                        </option>
                    </c:forEach>
                </select>
            </label>

            <br>
            <input name="price" class="" id="price" type="text"
                   placeholder="<fmt:message key="redactor.price.placeholder"/>" required/>
            <br>
            <input name="publication_date" class="" id="publication_date" type="text" max="<fmt:formatDate value="${todayDate}" pattern="yyyy-MM-dd"/>"
                   onfocus="(this.type='date')" placeholder="<fmt:message key="redactor.publication.date.placeholder"/>" required/>
            <br>
            <textarea name="image_url" class="" id="image_url" cols="30" rows="5"
                      placeholder="<fmt:message key="redactor.image.placeholder"/>" required></textarea>
            <br>
            <input name="action" value="1" style="display: none">
            <button type="submit"><fmt:message key="redactor.add.magazine.button"/></button>
        </form>
    </div>

</body>
</html>
