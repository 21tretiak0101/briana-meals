<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="by.ttre16.enterprise.util.ActionType" %>
<html>
<head>
    <title><spring:message code="app.title"/></title>
</head>
<body>
    <form action="/meals/save/${meal != null ? ActionType.UPDATE : ActionType.CREATE}"
          method="post">
        <strong>
            <c:choose>
                <c:when test="${meal.id == null}">
                    <spring:message code="meal.add"/>
                </c:when>
                <c:otherwise>
                    <spring:message code="meal.edit"/>
                </c:otherwise>
            </c:choose>
            <span>#${meal.id}</span>
        </strong>
        <hr>
        <span><spring:message code="meal.calories"/></span>
        <br>
        <label>
            <input type="text" name="calories" value="${meal.calories}" required pattern="[0-9]+">
        </label>
        <br>
        <span><spring:message code="meal.description"/></span>
        <br>
        <label>
            <input type="text" name="description" value="${meal.description}" required>
        </label>
        <br>
        <span><spring:message code="meal.dateTime"/></span>
        <br>
        <label>
            <input type="datetime-local" name="dateTime" value="${meal.dateTime}" required>
        </label>
        <br>
        <input type="hidden" name="mealId" value="${meal.id}">
        <button type="submit"><spring:message code="common.save"/></button>
        <button onclick="window.history.back()">Cancel</button>
    </form>
</body>
</html>
