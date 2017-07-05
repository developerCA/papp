<!DOCTYPE html>
<html lang="en" data-ng-app="clipApp">
	<head>
		<meta charset="utf-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
		<meta name="description" content="{{app.description}}"/>
		<meta name="keywords" content="app, responsive, angular, bootstrap, dashboard, admin"/>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" /> 
		<meta name="HandheldFriendly" content="true" /> 
		<meta name="apple-touch-fullscreen" content="yes" />
		<title data-ng-bind="pageTitle()">Citas</title>
	
		<!-- Bootstrap -->
		<link rel="stylesheet" href="static/bower_components/bootstrap/dist/css/bootstrap.min.css"/>
		<!-- Font Awesome -->
		<link rel="stylesheet" href="static/bower_components/font-awesome/css/font-awesome.min.css"/>
		<!-- Themify Icons -->
		<link rel="stylesheet" href="static/bower_components/themify-icons/css/themify-icons.css"/>
		<!-- Loading Bar -->
		<link rel="stylesheet" href="static/bower_components/angular-loading-bar/build/loading-bar.min.css"/>
		<!-- Animate Css -->
		<link rel="stylesheet" href="static/bower_components/animate.css/animate.min.css"/>
    	<link rel="stylesheet" href="static/bower_components/fullcalendar/dist/fullcalendar.css"/>
		<link rel="stylesheet" href="static/bower_components/angular-block-ui/dist/angular-block-ui.min.css"/>

		<!-- Clip-Two CSS -->
		<link rel="stylesheet" href="static/assets/css/styles.css"/>
		<link rel="stylesheet" href="static/assets/css/plugins.css"/>
		<!-- Clip-Two Theme -->
		<link rel="stylesheet" data-ng-href="static/assets/css/themes/theme-5.css" />
		
		
		
		
	</head>
	<body ng-controller="AppCtrl">
		<div ui-view="" id="app" ng-class="{'app-mobile' : app.isMobile, 'app-navbar-fixed' : app.layout.isNavbarFixed, 'app-sidebar-fixed' : app.layout.isSidebarFixed, 'app-sidebar-closed':app.layout.isSidebarClosed, 'app-footer-fixed':app.layout.isFooterFixed}"></div>

		<!-- jQuery -->
		<script src="static/bower_components/jquery/dist/jquery.min.js"></script>
		<!-- Fastclick -->
		<script src="static/bower_components/fastclick/lib/fastclick.js"></script>
		<!-- Angular -->
		
		<script src="static/bower_components/angular/angular.min.js"></script>
			<!-- Restangular -->
	
	
		<script src="static/bower_components/angular-cookies/angular-cookies.min.js"></script>
		<script src="static/bower_components/angular-animate/angular-animate.min.js"></script>		
		<script src="static/bower_components/angular-touch/angular-touch.min.js"></script>
		<script src="static/bower_components/angular-sanitize/angular-sanitize.min.js"></script>
		<script src="static/bower_components/angular-ui-router/release/angular-ui-router.min.js"></script>
		<!-- Angular storage -->
		<script src="static/bower_components/ngstorage/ngStorage.min.js"></script>
		<!-- Angular Translate -->
		<script src="static/bower_components/angular-translate/angular-translate.min.js"></script>
		<script src="static/bower_components/angular-translate-loader-url/angular-translate-loader-url.min.js"></script>
		<script src="static/bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files.min.js"></script>
		<script src="static/bower_components/angular-translate-storage-local/angular-translate-storage-local.min.js"></script>
		<script src="static/bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.min.js"></script>
		<!-- oclazyload -->
		<script src="static/bower_components/oclazyload/dist/ocLazyLoad.min.js"></script>
		<!-- breadcrumb -->
		<script src="static/bower_components/angular-breadcrumb/dist/angular-breadcrumb.min.js"></script>
		<!-- UI Bootstrap -->
		<script src="static/bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>
		<!-- Loading Bar -->
		<script src="static/bower_components/angular-loading-bar/build/loading-bar.min.js"></script>
		<!-- Angular Scroll -->
		<script src="static/bower_components/angular-scroll/angular-scroll.min.js"></script>
	
		<script src="static/bower_components/angular-i18n/angular-locale_es.js"></script>
		
		<script type="text/javascript" src="static/bower_components/angular-block-ui/dist/angular-block-ui.min.js"></script>
		
		<!-- Clip-Two Scripts -->
		<script src="static/assets/js/app.js"></script>
		<script src="static/assets/js/main.js"></script>
		<script src="static/assets/js/config.constant.js"></script>
		<script src="static/assets/js/config.router.js"></script>
		
		<script src="static/assets/js/externos/lodash/lodash.js"></script>
		<script src="static/assets/js/externos/restangular/dist/restangular.js"></script>
		
		
		<!-- Clip-Two Directives -->
		<script src="static/assets/js/directives/toggle.js"></script>
		<script src="static/assets/js/directives/perfect-scrollbar.js"></script>
		<script src="static/assets/js/directives/empty-links.js"></script>
		<script src="static/assets/js/directives/sidebars.js"></script>
		<script src="static/assets/js/directives/off-click.js"></script>
		<script src="static/assets/js/directives/full-height.js"></script>
		<script src="static/assets/js/directives/panel-tools.js"></script>
		<script src="static/assets/js/directives/char-limit.js"></script>
		<script src="static/assets/js/directives/dismiss.js"></script>
		<script src="static/assets/js/directives/compare-to.js"></script>
		<script src="static/assets/js/directives/select.js"></script>
		<script src="static/assets/js/directives/messages.js"></script>
		<script src="static/assets/js/directives/chat.js"></script>
		<script src="static/assets/js/directives/sparkline.js"></script>
		<script src="static/assets/js/directives/touchspin.js"></script>
		<script src="static/assets/js/directives/file-upload.js"></script>
		<script src="static/assets/js/directives/menu-to.js"></script>
		<!-- Clip-Two Controllers -->				
		<script src="static/assets/js/controllers/mainCtrl.js"></script>
		<script src="static/assets/js/controllers/inboxCtrl.js"></script>
		<script src="static/assets/js/controllers/bootstrapCtrl.js"></script>
		
	 	
	 	
		
		
		
		
	</body>
</html>