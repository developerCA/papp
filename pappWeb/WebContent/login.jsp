<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt'%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<meta Content-Type="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta name="description" content="{{app.description}}" />
<meta name="keywords"
	content="app, responsive, angular, bootstrap, dashboard, admin" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="HandheldFriendly" content="true" />
<meta name="apple-touch-fullscreen" content="yes" />


<link rel="shortcut icon" href="css/images/favicon.png">
<title>PAPP WEB</title>

<!-- Bootstrap -->
<link rel="stylesheet"
	href="static/bower_components/bootstrap/dist/css/bootstrap.min.css" />
<!-- Font Awesome -->
<link rel="stylesheet"
	href="static/bower_components/font-awesome/css/font-awesome.min.css" />
<!-- Themify Icons -->
<link rel="stylesheet"
	href="static/bower_components/themify-icons/css/themify-icons.css" />

<link rel="stylesheet" href="static/assets/css/styles.css" />
<link rel="stylesheet" href="static/assets/css/plugins.css" />


</head>
<body  class="texture"
	style="opacity: 1; margin-left: 0px;">
	<div id="cl-wrapper" class="login-container">
		<div class="middle-login">
			<div class="block-flat">

				<div>
					<form name="f" action="<c:url value='j_spring_security_check'/>"
						method="POST" style="margin-bottom: 0px !important;"
						class="form-horizontal">

						<div class="row">
							<div
								class="main-login col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 col-md-4 col-md-offset-4">

								<!-- start: LOGIN BOX -->
								<div class="box-login">
									<div class="logo" align="center">
										<img src="static/assets/images/ffaa.png"  alt="logo" />
									</div>

									<fieldset>
										<legend> Acceso al sistema </legend>
										<p>Por favor ingresar tu usuario y contraseña.</p>
										<div class="form-group">
											<span class="input-icon"> <input type="text"
												name="j_username" id="j_username" class="form-control"
												style="background: #FFF;" placeholder="Usuario"
												value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>' />
												<i class="fa fa-user"></i>
											</span>
										</div>
										<div class="form-group form-actions">
											<span class="input-icon"> <input type="password" name="j_password" id="j_password"
											class="form-control" placeholder="Contraseña" /> <i class="fa fa-lock"></i></span>
											<!--<a class="forgot" ui-sref="login.forgot">
								I forgot my password
							</a> </span>-->
										</div>
										
										<c:if test="${not empty param.login_error}">
										<div class="alert alert-danger">

												<strong>Acceso negado!</strong> Usuario y contraseña
												incorrectos..
											</div>
										</c:if>
							
										
											

										

										<div class="form-actions">

											<button type="submit" class="btn btn-primary pull-right">
												Ingresar <i class="fa fa-arrow-circle-right"></i>
											</button>

										</div>
										
									</fieldset>

									<!-- start: COPYRIGHT -->
									<div class="copyright">2017 &copy; Empresa S.A.</div>
									<!-- end: COPYRIGHT -->
								</div>
								<!-- end: LOGIN BOX -->
							</div>
						</div>


					</form>
				</div>
			</div>
			
		</div>
	</div>
</body>
</html>