<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <meta charset="UTF-8">
     <title><spring:message code="app.title"/></title>
</head>
<body>
<div>
    <form action="${pageContext.request.contextPath}/auth" method="post">
        <label for="select-one">
            <spring:message code="app.login"/>
        </label>
        <select name="id" id="select-one">
            <option value="1">User</option>
            <option value="2">Admin</option>
        </select>
        <button type="submit">
            <spring:message code="common.select"/>
        </button>
    </form>
</div>
</body>
</html>
