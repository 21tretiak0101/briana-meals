<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page import="by.ttre16.enterprise.util.ActionType" %>
<%@ page import="by.ttre16.enterprise.util.DateTimeUtil" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
    <a href="index.html">Home</a>
    <br>
    <div>
        <c:set var="meals" value="${requestScope.meals}"/>
        <c:if test="${not empty meals}">
            <table>
                <thead>
                <tr>
                    <th>#</th>
                    <th>Date</th>
                    <th>Calories</th>
                    <th>Description</th>
                    <th colspan="2">Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="meal" items="${meals}">
                    <tr style="color: ${meal.excess ? 'red': 'green'};">
                        <td>${meal.id}</td>
                        <td>${DateTimeUtil.toString(meal.dateTime)}</td>
                        <td>${meal.calories}</td>
                        <td>${meal.description}</td>
                        <td>
                            <form action="meals" method="post">
                                <input value="${meal.id}" type="hidden" name="id"/>
                                <input value="${ActionType.DELETE}" type="hidden" name="action">
                                <button type="submit">Delete</button>
                            </form>
                        </td>
                        <td>
                            <form action="editMeal.jsp">
                                <input type="hidden" name="id" value="${meal.id}">
                                <input type="hidden" name="dateTime" value="${meal.dateTime}">
                                <input type="hidden" name="calories" value="${meal.calories}">
                                <input type="hidden" name="description" value="${meal.description}">
                                <input type="hidden" name="action" value="${ActionType.UPDATE}">
                                <button type="submit">Update</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${empty meals}">
            <strong>Meals not found...</strong>
        </c:if>
        <br>
        <span>
            <a href="editMeal.jsp?action=${ActionType.CREATE}">Create new meal</a>
        </span>
    </div>
</body>
</html>
