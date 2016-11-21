<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>    

<!DOCTYPE html>
<html>
<head>
<title>Magni Rez 2016</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
	<body>
		<spring:url value="/login" var="Login"/>
		<a class="login" href="${Login}">Login</a><br>
	</body>
</html>

	