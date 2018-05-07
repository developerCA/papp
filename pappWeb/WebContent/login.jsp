<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt'%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>

<title>PAPP</title>
<meta name="description"
	content="app, web app, responsive, responsive layout, admin, admin panel, admin dashboard, flat, flat ui, ui kit, AngularJS, ui route, charts, widgets, components" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />

<!-- Bootstrap -->
<link rel="stylesheet"
	href="bower_components/bootstrap/dist/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet"
	href="bower_components/font-awesome/css/font-awesome.min.css">
<!-- Packet CSS -->
<link rel="stylesheet" href="assets/css/styles.css">
<link rel="stylesheet" href="assets/css/plugins.css">

<style>
.panel-heading {
	padding: 5px 15px;
}

.panel-footer {
	padding: 1px 15px;
	color: #A0A0A0;
}

.profile-img {
	width: 96px;
	height: 96px;
	margin: 0 auto 10px;
	display: block;
	-moz-border-radius: 50%;
	-webkit-border-radius: 50%;
	border-radius: 50%;
}

body {
	background: url('assets/images/comaco_colors.png') fixed;
	background-size: cover;
	padding: 0;
	margin: 0;
}

.vertical-offset-100 {
	padding-top: 100px;
}
</style>


</head>
<body>

	<div class="container" style="margin-top: 40px;">
		
		<div class="row">
			<div class="col-sm-12 col-md-12 col-lg-12">
				<center><h3>Sistema de Gesti&oacute;n Institucional P.A.P</h3></center>
			</div>
		</div>
	
		<div class="row">
			<div class="col-sm-6 col-md-4 col-md-offset-4">

				<div class="">
					<div class="panel-body">

						<form name="f" action="<c:url value='j_spring_security_check'/>"
							method="POST" style="margin-bottom: 0px !important;"
							class="form-horizontal">
							<fieldset>
								<div class="row">
									<div class="center-block">
										<center>
											<img src="assets/images/logoffaa.png" alt="" />
										</center>
										<br />
									</div>
								</div>
								<div class="row">
									<div class="col-sm-12 col-md-10  col-md-offset-1">
										<div class="form-group">
											<div class="input-group">
												<span class="input-group-addon">
													<i class="glyphicon glyphicon-user"></i>
												</span>
												<input type="text" name="j_username" id="j_username"
													class="form-control" style="background: #FFF;"
													placeholder="Usuario" onkeyup="this.value=this.value.toUpperCase()"
													value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'>
											</div>
										</div>
										<div class="form-group">
											<div class="input-group">
												<span class="input-group-addon">
													<i class="glyphicon glyphicon-lock"></i>
												</span>
												<input type="password" name="j_password" id="j_password"
													class="form-control" placeholder="Contraseña" />
											</div>
										</div>

										<c:if test="${not empty param.login_error}">
											<div class="alert alert-danger">
												<strong>Acceso negado!</strong> Usuario y contraseña
												incorrectos..
											</div>
										</c:if>

										<div class="form-group">
											<input type="submit" class="btn btn-lg btn-primary btn-block"
												value="Entrar" />
										</div>
									</div>
								</div>
							</fieldset>
						</form>
					</div>

				</div>
			</div>
		</div>
	</div>

</body>
</html>