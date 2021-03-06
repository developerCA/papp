<%@taglib prefix="sec" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html lang="en" data-ng-app="app"  xmlns:security="http://www.springframework.org/security/tags" xmlns:th="http://www.springframework.org/schema/mvc">
<head>
    <meta charset="utf-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="description" content="{{app.description}}">
    <meta name="keywords" content="app, responsive, angular, bootstrap, dashboard, admin">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta name="HandheldFriendly" content="true" />
    <meta name="apple-touch-fullscreen" content="yes" />
    <meta http-equiv="cache-control" content="max-age=0" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
	<meta http-equiv="pragma" content="no-cache" />

    <title data-ng-bind="pageTitle()">FF.AA</title>
    <!-- Bootstrap -->
    
    <link rel="stylesheet" href="bower_components/bootstrap/dist/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="bower_components/font-awesome/css/font-awesome.min.css">
    <!-- Themify Icons -->
    <link rel="stylesheet" href="bower_components/themify-icons/themify-icons.css">
    <!-- Flag Icons -->
    <link rel="stylesheet" href="bower_components/flag-icon-css/css/flag-icon.min.css">
    <!-- Loading Bar -->
    <link rel="stylesheet" href="bower_components/angular-loading-bar/build/loading-bar.min.css">
    <!-- Animate Css -->
    <link rel="stylesheet" href="bower_components/animate.css/animate.min.css">
    <!-- Perfect Scrollbar Css -->
    <link rel="stylesheet" href="bower_components/perfect-scrollbar/css/perfect-scrollbar.min.css">
    <!-- Date Range Picker Css -->
    <link rel="stylesheet" href="bower_components/bootstrap-daterangepicker/daterangepicker-bs3.css">
    <!-- Angular ui-switch Css -->
    <link rel="stylesheet" href="bower_components/angular-ui-switch/angular-ui-switch.min.css">
    <!-- Angular Toaster Css -->
    <link rel="stylesheet" href="bower_components/AngularJS-Toaster/toaster.css">

	<!-- Angular Aside -->   
    <link rel="stylesheet" href="bower_components/angular-aside/dist/css/angular-aside.min.css">

    <!-- Angular Notification Icons Css -->
    <link rel="stylesheet" href="bower_components/angular-notification-icons/dist/angular-notification-icons.min.css">
    <!-- V-Accordion Css -->
    <link rel="stylesheet" href="bower_components/v-accordion/dist/v-accordion.min.css">
    <!-- V-Button Css -->
    <link rel="stylesheet" href="bower_components/v-button/dist/v-button.min.css">
    <!-- Sweet Alert Css -->
    <link rel="stylesheet" href="bower_components/sweetalert/lib/sweet-alert.css">
    <!-- Ladda Buttons Css -->
    <link rel="stylesheet" href="bower_components/ladda/dist/ladda-themeless.min.css">
    <!-- Angular Awesome Slider Css -->
    <link rel="stylesheet" href="bower_components/angular-awesome-slider/dist/css/angular-awesome-slider.min.css">
    <!-- Slick Carousel Css -->
    <link rel="stylesheet" href="bower_components/slick-carousel/slick/slick.css">
    <link rel="stylesheet" href="bower_components/slick-carousel/slick/slick-theme.css">
    <!-- BlockUI Css -->
    <link rel="stylesheet" href="bower_components/angular-block-ui/dist/angular-block-ui.min.css"/>
     <!-- Packet CSS -->
    <link rel="stylesheet" href="assets/css/styles.css">
    <link rel="stylesheet" href="assets/css/plugins.css">
    

    <!-- Tree -->
    <link rel="stylesheet" href="bower_components/angular-ui-tree/dist/angular-ui-tree.css"/>
   
    <!-- Packet Theme -->
    <link rel="stylesheet" data-ng-href="assets/css/themes/{{ app.layout.theme }}.css" />

    <!-- Favicon -->
    <link rel="shortcut icon" href="favicon.ico" />
<script type = "text/javascript">
var seccion = {};
seccion.usuario = 'NO DEFINIDO';
</script>
<script type = "text/javascript">
seccion.usuario = '${sm_utilitario_usuarioLogin.usuario}';
seccion.rols = '${sm_utilitario_permisosusuario}';
seccion.cambiarclave = '${sm_utilitario_cambiarclave}';
</script>
    <script src="assets/js/seccion.rols.js"></script>
<script>
//console.log(seccion);
</script>
</head>
<body ng-controller="AppCtrl">
    <div ui-view id="app" class="lyt-3" ng-class="{'app-mobile' : app.isMobile, 'app-navbar-fixed' : app.layout.isNavbarFixed, 'app-sidebar-fixed' : app.layout.isSidebarFixed, 'app-sidebar-closed':app.layout.isSidebarClosed, 'app-footer-fixed':app.layout.isFooterFixed}"></div>

    <!-- jQuery -->
    <script src="bower_components/jquery/dist/jquery.min.js"></script>
    <!-- Fastclick -->
    <script src="bower_components/fastclick/lib/fastclick.js"></script>
    <!-- Modernizr -->
    <script src="bower_components/components-modernizr/modernizr.js"></script>
    <!-- Moment -->
    <script src="bower_components/moment/min/moment.min.js"></script>
    <!-- Perfect Scrollbar -->
    <script src="bower_components/perfect-scrollbar/js/min/perfect-scrollbar.jquery.min.js"></script>
    <!-- Date Range Picker -->
    <script src="bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
    <!-- Sweet Alert -->
    <script src="bower_components/sweetalert/lib/sweet-alert.min.js"></script>
    <!-- Spin -->
    <script src="bower_components/spin.js/spin.js"></script>
    <!-- Ladda Buttons -->
    <script src="bower_components/ladda/dist/ladda.min.js"></script>
    <!-- Slick Carousel -->
    <script src="bower_components/slick-carousel/slick/slick.min.js"></script>
    <!-- Angular -->
    <script src="bower_components/angular/angular.min.js"></script>
    <script src="bower_components/angular-cookies/angular-cookies.min.js"></script>
    <script src="bower_components/angular-animate/angular-animate.min.js"></script>
    <script src="bower_components/angular-touch/angular-touch.min.js"></script>
    <script src="bower_components/angular-sanitize/angular-sanitize.min.js"></script>
    <script src="bower_components/angular-ui-router/release/angular-ui-router.min.js"></script>
    <!-- Angular storage -->
    <script src="bower_components/ngstorage/ngStorage.min.js"></script>
    <!-- Angular Translate -->
    <script src="bower_components/angular-translate/angular-translate.min.js"></script>
    <script src="bower_components/angular-translate-loader-url/angular-translate-loader-url.min.js"></script>
    <script src="bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files.min.js"></script>
    <script src="bower_components/angular-translate-storage-local/angular-translate-storage-local.min.js"></script>
    <script src="bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.min.js"></script>
	<script src="bower_components/angular-ui-utils/mask.min.js"></script>

    <script src="bower_components/angular-aside/dist/js/angular-aside.min.js"></script>
        
    <!-- Restangular -->
    
    <script src="bower_components/lodash/lodash.js"></script>
	<script src="bower_components/restangular/dist/restangular.js"></script>
	<!-- Underscore -->
	<script src="bower_components/underscore/underscore-min.js"></script>
	<script src="bower_components/angular-underscore-module/angular-underscore-module.js"></script>
	
	
	
    <!-- BlockUI -->
    <script src="bower_components/angular-block-ui/dist/angular-block-ui.min.js"></script>
    <!-- Aside Strap -->
       
	<!-- oclazyload -->
    <script src="bower_components/oclazyload/dist/ocLazyLoad.min.js"></script>
    <!-- breadcrumb -->
    <script src="bower_components/angular-breadcrumb/dist/angular-breadcrumb.min.js"></script>
    <!-- angular-swipe -->
    <script src="bower_components/angular-swipe/dist/angular-swipe.min.js"></script>
    <!-- UI Bootstrap -->
    <script src="bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>
    <!-- Loading Bar -->
    <script src="bower_components/angular-loading-bar/build/loading-bar.min.js"></script>
    <!-- Angular Scroll -->
    <script src="bower_components/angular-scroll/angular-scroll.min.js"></script>
    <!-- Angular Fullscreen -->
    <script src="bower_components/angular-fullscreen/src/angular-fullscreen.js"></script>
    <!-- Angular DateRangePicker -->
    <script src="bower_components/ng-bs-daterangepicker/dist/ng-bs-daterangepicker.min.js"></script>
    <!-- Angular Truncate -->
    <script src="bower_components/angular-truncate/src/truncate.js"></script>
    <!-- Angular Moment -->
    <script src="bower_components/angular-moment/angular-moment.min.js"></script>
    <!-- Angular ui-switch -->
    <script src="bower_components/angular-ui-switch/angular-ui-switch.min.js"></script>
    <!-- Angular Toaster -->
    <script src="bower_components/AngularJS-Toaster/toaster.js"></script>
    
    <!-- V-Accordion -->
    <script src="bower_components/v-accordion/dist/v-accordion.min.js"></script>
    <!-- V-Button -->
    <script src="bower_components/v-button/dist/v-button.min.js"></script>
    <!-- Angular Sweet Alert -->
    <script src="bower_components/angular-sweetalert-promised/SweetAlert.min.js"></script>
    <!-- Angular Notification Icons -->
    <script src="bower_components/angular-notification-icons/dist/angular-notification-icons.min.js"></script>
    <!-- Angular Awesome Slider -->
    <script src="bower_components/angular-awesome-slider/dist/angular-awesome-slider.min.js"></script>
    <!-- Angular Ladda -->
    <script src="bower_components/angular-ladda/dist/angular-ladda.min.js"></script>
    <!-- Angular Slick Carousel -->
    <script src="bower_components/angular-slick-carousel/dist/angular-slick.min.js"></script>
    
    <!-- Tree-->
    <script src="bower_components/angular-ui-tree/dist/angular-ui-tree.min.js"></script>
     
    <!-- Packet Scripts -->
    <script src="assets/js/app.js"></script>
    <script src="assets/js/main.js"></script>
    <script src="assets/js/config.constant.js"></script>
    <script src="assets/js/config.router.js"></script>
    <!-- Packet Directives -->
    <script src="assets/js/directives/toggle.js"></script>
    <script src="assets/js/directives/perfect-scrollbar.js"></script>
    <script src="assets/js/directives/empty-links.js"></script>
    <script src="assets/js/directives/sidebars.js"></script>
    <script src="assets/js/directives/off-click.js"></script>
    <script src="assets/js/directives/full-height.js"></script>
    <script src="assets/js/directives/panel-tools.js"></script>
    <script src="assets/js/directives/char-limit.js"></script>
    <script src="assets/js/directives/dismiss.js"></script>
    <script src="assets/js/directives/compare-to.js"></script>
    <script src="assets/js/directives/select.js"></script>
    <script src="assets/js/directives/messages.js"></script>
    <script src="assets/js/directives/chat.js"></script>
    <script src="assets/js/directives/touchspin.js"></script>
    <script src="assets/js/directives/file-upload.js"></script>
    <script src="assets/js/directives/letter-icon.js"></script>
    <script src="assets/js/directives/landing-header.js"></script>
    <script src="assets/js/directives/ct-crop.js"></script>
    <script src="assets/js/directives/menu-to.js"></script>
    <!-- Packet Controllers -->
    <script src="assets/js/controllers/mainCtrl.js"></script>
    <script src="assets/js/controllers/inboxCtrl.js"></script>
    <script src="assets/js/controllers/bootstrapCtrl.js"></script>
</body>
</html>