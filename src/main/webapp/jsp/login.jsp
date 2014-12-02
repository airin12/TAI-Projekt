<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
</head>

<body>
	<section id="login">
    <div class="container" style="width:400px;">
    	<div class="row">
    	    <div class="col-xs-12">
        	    <div class="form-wrap">
                <h3>Welcome to Youtube Analizer!</h3>
                <c:url value="/j_spring_security_check" var="loginUrl" />
                    <form role="form" action="${loginUrl}"  method="post" id="login-form" autocomplete="off">
                        <div class="form-group">
                            <label for="username" class="sr-only">Email</label>
                            <input type="text" name="email" id="email" class="form-control" placeholder="Email">
                        </div>
                        <div class="form-group">
                            <label for="key" class="sr-only">Password</label>
                            <input type="password" name="key" id="key" class="form-control" placeholder="Password">
                        </div>
                        <input type="submit" id="btn-login" class="btn btn-custom btn-lg btn-block" value="Log in">
                    </form>
                    <hr>
        	    </div>
    		</div>
    	</div>
    </div> 
</section>
</body>
</html>
