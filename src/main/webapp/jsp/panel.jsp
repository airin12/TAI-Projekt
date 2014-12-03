<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
</head>

<body>
	<script>
		function formSubmit() {
			document.getElementById("logoutForm").submit();
		}
	</script>

	<div class="bs-docs-grid">
		<div class="row show-grid">
			<div class="col-md-2 col-md-offset-10">
				<c:if test="${pageContext.request.userPrincipal.name != null}">
			Welcome : ${pageContext.request.userPrincipal.name} | <a href="javascript:formSubmit()"> Logout</a>
				</c:if>
			</div>
		</div>
	</div>

	<h2>Youtube Analizer User Panel</h2>

	<c:url value="/j_spring_security_logout" var="logoutUrl" />

	<!-- csrt for log out-->
	<form action="${logoutUrl}" method="post" id="logoutForm">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form>





	<div class="row">
		<div class="col-xs-2">
			<div class="panel panel-default">
				<div class="panel-heading">Available panels</div>
				<ul class="list-group">
					<li><a href="#" class="list-group-item"> User Panel </a></li>

					<security:authorize access="hasRole('ROLE_ANALYST')">
						<li><a href="/youtube.analizer/analyst" class="list-group-item"> Analyst Panel </a>
					</security:authorize>

				</ul>
			</div>
		</div>
		<div class="col-xs-9">
			<div class="panel panel-default">
				<div class="panel-heading">Available analysis</div>
				<table class="table table-hover">
					<col width="95%">
					<col width="5%">
					<tr>
						<th>Analisys name</th>
						<th></th>
					</tr>
					<c:forEach items="${analysis}" var="analysis_list" varStatus="loop">

						<tr>
							<td><c:out value="${analysis_list}" /></td>
							<td><a class="btn btn-default" href="/youtube.analizer/analysis?index=${loop.index}">
									<span class="glyphicon glyphicon-eye-open"></span>
								</a></td>
						</tr>

					</c:forEach>

				</table>
			</div>
		</div>
	</div>


</body>
</html>
