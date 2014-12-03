<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
<style type="text/css">
.bs-example {
	margin: 20px;
}
</style>
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

	<h2>Youtube Analizer Analyst Panel</h2>

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
					<a href="/youtube.analizer/panel" class="list-group-item"> User Panel </a>
					<security:authorize access="hasRole('ROLE_ANALYST')">
						<a href="#" class="list-group-item"> Analyst Panel </a>
					</security:authorize>
				</ul>
			</div>
		</div>
		<div class="col-xs-9">
			<div class="bs-example">
				<ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#delete_analysis">Delete analysis</a></li>
					<li class="dropdown"><a data-toggle="dropdown" class="dropdown-toggle" href="#">Create analysis <b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a data-toggle="tab" href="#dropdown1">Type1</a></li>
							<li><a data-toggle="tab" href="#dropdown2">Type2</a></li>
							<li><a data-toggle="tab" href="#dropdown3">Type3</a></li>
						</ul></li>

				</ul>
				<div class="tab-content">
					<div id="delete_analysis" class="tab-pane fade in active">
						<div class="panel panel-default">
							<div class="panel-heading">Delete analysis</div>
							<table class="table table-hover">
								<tr>
									<th>Analisys name</th>
									<th>Type</th>
								</tr>
								<c:forEach items="${analysis}" var="analysis_list">

									<tr>
										<td><c:out value="${analysis_list.name}" /></td>
										<td><c:out value="${analysis_list.type}" /></td>
									</tr>

								</c:forEach>

							</table>
						</div>
					</div>
					<div id="dropdown1" class="tab-pane fade">
						<h3>Type 1</h3>
					</div>
					<div id="dropdown2" class="tab-pane fade">
						<h3>Type 2</h3>
					</div>
					<div id="dropdown3" class="tab-pane fade">
						<h3>Type 3</h3>
					</div>
				</div>
			</div>

		</div>
	</div>
</body>
</html>
