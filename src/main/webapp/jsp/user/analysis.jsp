<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<script src="http://student.agh.edu.pl/~bogusz/lib/Chart.js"></script>
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
					<li><a href="/youtube.analizer/user/panel" class="list-group-item"> User Panel </a></li>

					<security:authorize access="hasRole('ROLE_ANALYST')">
						<li><a href="/youtube.analizer/analyst/panel" class="list-group-item"> Analyst Panel </a>
					</security:authorize>

				</ul>
			</div>
		</div>
		<div class="col-xs-9">
			<div class="panel panel-default">
				<div class="panel-heading">Analysis</div>
				<canvas id="analysis" width="600" height="400"></canvas>

			</div>
		</div>
	</div>


	<script>
		var labels =<c:out value="${labels}"/>
		var data =<c:out value="${data}"/>
		var buyerData = {
			labels : labels,
			datasets : [ {
				fillColor : "rgba(172,194,132,0.4)",
				strokeColor : "#ACC26D",
				pointColor : "#fff",
				pointStrokeColor : "#9DB86D",
				data : data
			} ]
		}

		var buyers = document.getElementById('analysis').getContext('2d');
		new Chart(buyers).Line(buyerData);
	</script>


</body>
</html>
