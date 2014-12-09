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
					<li><a href="/youtube.analizer/user/panel" class="list-group-item"> User Panel </a></li>
					<security:authorize access="hasRole('ROLE_ANALYST')">
						<li><a href="#" class="list-group-item"> Analyst Panel </a></li>
					</security:authorize>
				</ul>
			</div>
		</div>
		<div class="col-xs-9">
			<div class="bs-example">
				<ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#delete_analysis">Delete analysis</a></li>
					<li><a data-toggle="tab" href="#create_analysis">Create analysis</a></li>
				</ul>
				<div class="tab-content">
					<div id="delete_analysis" class="tab-pane fade in active">
						<div class="panel panel-default">
							<div class="panel-heading">Users analysis</div>
							<table class="table table-hover">
								<col width="95%">
								<col width="5%">
								<tr>
									<th>Analysis name</th>
									<th></th>
								</tr>
								<c:forEach items="${analysis}" var="analysis_list" varStatus="loop">

									<tr>
										<td><c:out value="${analysis_list}" /></td>
										<td><a class="btn btn-default" href="/youtube.analizer/analyst/remove?title=${analysis_list}"> <span
												class="glyphicon glyphicon-remove"></span>
										</a></td>
									</tr>

								</c:forEach>

							</table>
						</div>
					</div>
					<div id="create_analysis" class="tab-pane fade">
						<div class="panel panel-default">
							<div class="panel-heading">Create analysis</div>
							<div class="bs-example">
								<form:form commandName="report" method="post" action="${pageContext.request.contextPath}/analyst/panel" role="form">

									<div class="form-group">
										<form:label path="title">Title</form:label>
										<form:input path="title" class="form-control" placeholder="Insert title" />

										<form:label path="channelId">Channel Id</form:label>
										<form:input path="channelId" class="form-control" placeholder="Insert channel id" />

										<form:label path="analysisStartDate">Start date</form:label>
										<form:input path="analysisStartDate" class="form-control" placeholder="Insert start date" />

										<form:label path="analysisEndDate">End date</form:label>
										<form:input path="analysisEndDate" class="form-control" placeholder="Insert end date" />

										<form:label path="analysisEndDate">Analysis type</form:label>
										<form:select path="analysis" class="form-control">
											<form:option value="views_over_time">Views over time analysis</form:option>
											<form:option value="top_videos_10">Top 10 videos analysis</form:option>
											<form:option value="top_videos_all">All videos analysis</form:option>
										</form:select>

									</div>
									<button type="submit" class="btn btn-default">Save</button>
								</form:form>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
</body>
</html>
