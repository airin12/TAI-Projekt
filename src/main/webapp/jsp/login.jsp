<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
</head>

<body>
	<section id="login">
		<div class="container" style="width: 400px;">
			<div class="row">
				<div class="col-xs-12">
					<div class="form-wrap">
						<h3>Welcome to Youtube Analizer!</h3>
						<c:if test="${not empty error}">
							<div class="alert alert-danger alert-error">${error}</div>
						</c:if>
						<c:if test="${not empty msg}">
							<div class="alert alert-info">${msg}</div>
						</c:if>
						<c:url value="/j_spring_security_check" var="loginUrl" />
						<form role="form" action="${loginUrl}" method="post" id="login-form" autocomplete="off">
							<div class="form-group">
								<label for="username" class="sr-only">Email</label> <input type="text" name="username" id="email" class="form-control"
									placeholder="Username">
							</div>
							<div class="form-group">
								<label for="key" class="sr-only">Password</label> <input type="password" name="password" id="key" class="form-control"
									placeholder="Password">
							</div>
							<input type="submit" id="btn-login" class="btn btn-custom btn-lg btn-block" value="Log in"> <input type="hidden"
								name="${_csrf.parameterName}" value="${_csrf.token}" />
						</form>
						<hr>
					</div>
				</div>
			</div>
		</div>
	</section>
</body>
</html>
