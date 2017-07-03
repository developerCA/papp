<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt' %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
  	<head>
		<!--meta Content-Type="text/html; charset=UTF-8">
		<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'-->
		<meta Content-Type="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="css/images/favicon.png">
		<title>PAPP</title>
	</head>
  	<body onLoad="document.f.j_username.focus();" class="texture" style="opacity:1;margin-left:0px;">
		<div id="cl-wrapper" class="login-container">
  			<div class="middle-login">
  				<div class="block-flat">
  					
	  				<div>
	  					<form name="f" action="<c:url value='j_spring_security_check'/>" method="POST" style="margin-bottom:0px !important;" class="form-horizontal">
							<div class="content">
								<c:if test="${not empty param.login_error}">
									<h4 class="color-danger">Usuario o clave incorrecta</h4>
								</c:if>
								<c:if test="${not empty param.loggedout}">
									<h4 class="color-danger">Su sesion se ha cerrado</h4>
								</c:if>
								<h4 class="title">Bienvenid@</h4>
								<div class="form-group">
									<div class="col-sm-12">
										<div class="input-group">
											<span class="input-group-addon"><i class="fa fa-user"></i></span>
											<input type="text" name="j_username" id="j_username" class="form-control" style="background:#FFF;" placeholder="Usuario" value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'/>
										</div>
									</div>
								</div>
								<div class="form-group">
									<div class="col-sm-12">
										<div class="input-group">
											<span class="input-group-addon"><i class="fa fa-lock"></i></span>
											<input type="password" name="j_password" id="j_password" class="form-control" placeholder="Contraseña"/>
										</div>
									</div>
								</div>
									
							</div>
							<div class="foot">
								<button class="btn btn-primary" type="submit">
									<span class="fa fa-arrow-right"></span>&nbsp;&nbsp;Ingresar al sistema
								</button>
							</div>
						</form>
	  				</div>
  				</div>
  				<div class="text-center out-links"><a href="#">&copy; 2017 COMACO</a></div>
  			</div>
  		</div>
  </body>
</html>