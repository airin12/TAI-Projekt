<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<style type="text/css">
.bs-example {
	margin: 20px;
}
</style>
</head>

<body>

	<script>
		window.onload = function() {
			var radioWeek = document.getElementById("optradioWeek");
			radioWeek.onclick = function() {
				window.location.href = "${pageContext.request.contextPath}/user/top_videos_10?time=week";
			};

			var radioDay = document.getElementById("optradioDay");
			radioDay.onclick = function() {
				window.location.href = "${pageContext.request.contextPath}/user/top_videos_10?time=day";
			};
		};

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

	<h2>${title}</h2>

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
				<div class="panel-heading">Top 10 videos</div>
				<table class="table table-hover">
					<col width="85%">
					<col width="15%">
					<tr>
						<th>Video name</th>
						<th>Views</th>
					</tr>
					<c:forEach items="${videos}" var="video" varStatus="loop">

						<tr>
							<td><c:out value="${video.key}" /></td>
							<td><c:out value="${video.value}" /></td>
						</tr>

					</c:forEach>

				</table>
				<div class="bs-example">
					<p>Time</p>
					<form>
						<div class="radio">
							<label><input type="radio" name="optradio" id="optradioWeek">Last week</label>
						</div>
						<div class="radio">
							<label><input type="radio" name="optradio" id="optradioDay">Last day</label>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
