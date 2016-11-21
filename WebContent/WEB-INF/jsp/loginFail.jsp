<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/theme/css/loginStyle.css"
	type="text/css">
<title>Login Fail</title>
</head>
<body>

	<div class="container">

		<form id="signup" method="post">

			<div class="header">

				<h3>Log In</h3>
			</div>

			<div class="sep"></div>

			<div class="inputs">

				<spring:bind path="user.username">
					<input type="text" placeholder="username"
						name="${status.expression}" value="${status.value}">
				</spring:bind>

				<spring:bind path="user.password">
					<input type="password" placeholder="password"
						name="${status.expression}" value="${status.value}">
				</spring:bind>

				<input type="submit" id="submit">

			</div>
			<div>Username or password are incorrect</div>
		</form>

	</div>
	​
</body>