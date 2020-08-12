<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="by.ttre16.enterprise.util.web.ActionType" %>
<%@ page import="by.ttre16.enterprise.util.DateTimeUtil" %>
<%@ page import="by.ttre16.enterprise.util.web.UrlUtil" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<div>
    <form method="get" action="${pageContext.request.contextPath}${UrlUtil.MEAL_JSP_URL}">
        <input type="hidden" name="action" value="filter">
        <dl>
            <dt><spring:message code="meal.startDate"/></dt>
            <dd>
                <label>
                    <input type="date" name="startDate" value="${param.startDate}">
                </label>
            </dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.endDate"/></dt>
            <dd>
                <label>
                    <input type="date" name="endDate" value="${param.endDate}">
                </label>
            </dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.startTime"/></dt>
            <dd>
                <label>
                    <input type="time" name="startTime" value="${param.startTime}">
                </label>
            </dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.endTime"/></dt>
            <dd>
                <label>
                    <input type="time" name="endTime" value="${param.endTime}">
                </label>
            </dd>
        </dl>
        <button type="submit"><spring:message code="meal.filter"/></button>
    </form>
</div>
<div>
    <c:set var="meals" value="${meals}"/>
    <c:if test="${not empty meals}">
        <table>
            <thead>
            <tr>
                <th>#</th>
                <th></th>
                <th><spring:message code="meal.calories"/></th>
                <th><spring:message code="meal.description"/></th>
                <th colspan="2"><spring:message code="common.action"/></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="meal" items="${meals}">
                <jsp:useBean id="meal" type="by.ttre16.enterprise.dto.MealTo"/>
                <tr style="color: ${meal.excess ? 'red': 'green'};">
                    <td>${meal.id}</td>
                    <td>${DateTimeUtil.toString(meal.dateTime)}</td>
                    <td>${meal.calories}</td>
                    <td>${meal.description}</td>
                    <td>
                        <form action="${UrlUtil.MEAL_JSP_URL}/delete/${meal.id}"
                              method="post">
                            <button type="submit"><spring:message code="common.delete"/></button>
                        </form>
                    </td>
                    <td>
                        <form action="${UrlUtil.MEAL_JSP_URL}/edit/${ActionType.UPDATE}/${meal.id}">
                            <button type="submit"><spring:message code="common.update"/></button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
    <c:if test="${empty meals}">
        <strong><spring:message code="com"/></strong>
    </c:if>
    <br>
    <span>
        <a href="${UrlUtil.MEAL_JSP_URL}/edit/${ActionType.CREATE}"><spring:message code="common.add"/></a>
    </span>
</div>
</body>
</html>
