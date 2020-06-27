<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit meal</title>
</head>
<body>
    <form action="meals" method="post">
        <strong>
            ${param.mealId != null ? 'Update meal #': 'Create meal'}${param.mealId}
        </strong>
        <hr>
        <span>Calories</span>
        <br>
        <input type="text" name="calories" value="${param.calories}"  required pattern="[0-9]+">
        <br>
        <span>Description</span>
        <br>
        <input type="text" name="description" value="${param.description}" required>
        <br>
        <span>Date</span>
        <br>
        <input type="datetime-local" name="dateTime" value="${param.dateTime}" required>
        <br>
        <input type="hidden" name="action" value="${param.action}">
        <input type="hidden" name="mealId" value="${param.mealId}">
        <button type="submit">Submit</button>
        <button onclick="window.history.back()">Cancel</button>
    </form>
</body>
</html>
