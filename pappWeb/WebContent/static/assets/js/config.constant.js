'use strict';

/**
 * Config constant
 */
app.constant('APP_MEDIAQUERY', {
    'desktopXL': 1200,
    'desktop': 992,
    'tablet': 768,
    'mobile': 480
});
app.constant('JS_REQUIRES', {
    //*** Scripts
    scripts: {
        //*** Javascript Plugins
        'modernizr': ['static/bower_components/components-modernizr/modernizr.js'],
        'moment': ['static/bower_components/moment/min/moment.min.js'],
        'spin': 'static/bower_components/spin.js/spin.js',

        //*** jQuery Plugins
        'perfect-scrollbar-plugin': ['static/bower_components/perfect-scrollbar/js/perfect-scrollbar.jquery.min.js', 'static/bower_components/perfect-scrollbar/css/perfect-scrollbar.min.css'],
        'ladda': ['static/bower_components/ladda/dist/ladda.min.js', 'static/bower_components/ladda/dist/ladda-themeless.min.css'],
        'sweet-alert': ['static/bower_components/sweetalert/lib/sweet-alert.js', 'static/bower_components/sweetalert/lib/sweet-alert.css'],
        'chartjs': 'static/bower_components/chartjs/Chart.min.js',
        'jquery-sparkline': 'static/bower_components/jquery.sparkline.build/dist/jquery.sparkline.min.js',
        'ckeditor-plugin': 'static/bower_components/ckeditor/ckeditor.js',
        'jquery-nestable-plugin': ['static/bower_components/jquery-nestable/jquery.nestable.js'],
        'touchspin-plugin': ['static/bower_components/bootstrap-touchspin/dist/jquery.bootstrap-touchspin.min.js', 'static/bower_components/bootstrap-touchspin/dist/jquery.bootstrap-touchspin.min.css'],
		'spectrum-plugin': ['static/bower_components/spectrum/spectrum.js', 'static/bower_components/spectrum/spectrum.css'],
		
        //*** Controllers
        'dashboardCtrl': 'static/assets/js/controllers/dashboardCtrl.js',
        'iconsCtrl': 'static/assets/js/controllers/iconsCtrl.js',
        'vAccordionCtrl': 'static/assets/js/controllers/vAccordionCtrl.js',
        'ckeditorCtrl': 'static/assets/js/controllers/ckeditorCtrl.js',
        'laddaCtrl': 'static/assets/js/controllers/laddaCtrl.js',
        'ngTableCtrl': 'static/assets/js/controllers/ngTableCtrl.js',
        'cropCtrl': 'static/assets/js/controllers/cropCtrl.js',
        'asideCtrl': 'static/assets/js/controllers/asideCtrl.js',
        'toasterCtrl': 'static/assets/js/controllers/toasterCtrl.js',
        'sweetAlertCtrl': 'static/assets/js/controllers/sweetAlertCtrl.js',
        'mapsCtrl': 'static/assets/js/controllers/mapsCtrl.js',
        'chartsCtrl': 'static/assets/js/controllers/chartsCtrl.js',
        'calendarCtrl': 'static/assets/js/controllers/calendarCtrl.js',
        'nestableCtrl': 'static/assets/js/controllers/nestableCtrl.js',
        'validationCtrl': ['static/assets/js/controllers/validationCtrl.js'],
        'userCtrl': ['static/assets/js/controllers/userCtrl.js'],
        'selectCtrl': 'static/assets/js/controllers/selectCtrl.js',
        'wizardCtrl': 'static/assets/js/controllers/wizardCtrl.js',
        'uploadCtrl': 'static/assets/js/controllers/uploadCtrl.js',
        'treeCtrl': 'static/assets/js/controllers/treeCtrl.js',
        'inboxCtrl': 'static/assets/js/controllers/inboxCtrl.js',
        'xeditableCtrl': 'static/assets/js/controllers/xeditableCtrl.js',
        'chatCtrl': 'static/assets/js/controllers/chatCtrl.js',
        'dynamicTableCtrl': 'static/assets/js/controllers/dynamicTableCtrl.js',
        'NotificationIconsCtrl': 'static/assets/js/controllers/notificationIconsCtrl.js',
        'MenuToCtrl':'static/assets/js/controllers/menuToCtrl.js',
        'UsuarioFactory':'static/assets/js/factory/usuariosFactory.js',
        'SucursalFactory':'static/assets/js/factory/sucursalesFactory.js',
        'ClienteFactory':'static/assets/js/factory/clientesFactory.js',
        'ServicioFactory':'static/assets/js/factory/serviciosFactory.js',
        'ColaboradorFactory':'static/assets/js/factory/colaboradoresFactory.js',
        'CitaFactory':'static/assets/js/factory/citasFactory.js',
        'ColaboradorCtrl':'static/assets/js/controllers/colaboradorCtrl.js',
        'ServiciosCtrl':'static/assets/js/controllers/serviciosCtrl.js',
        'EmpresaFactory':'static/assets/js/factory/empresaFactory.js',
        'EmpresaCtrl':'static/assets/js/controllers/empresaCtrl.js',
        'SucursalCtrl':'static/assets/js/controllers/sucursalCtrl.js',
        'UsuarioCtrl':'static/assets/js/controllers/usuarioCtrl.js',
        'PrincipalCtrl':'static/assets/js/controllers/principalCtrl.js',
        
        
        
           
          
        //*** Filters
        'htmlToPlaintext': 'static/assets/js/filters/htmlToPlaintext.js'
    },
    //*** angularJS Modules
    modules: [{
        name: 'angularMoment',
        files: ['static/bower_components/angular-moment/angular-moment.min.js']
    }, {
        name: 'toaster',
        files: ['static/bower_components/AngularJS-Toaster/toaster.js', 'static/bower_components/AngularJS-Toaster/toaster.css']
    }, {
        name: 'angularBootstrapNavTree',
        files: ['static/bower_components/angular-bootstrap-nav-tree/dist/abn_tree_directive.js', 'static/bower_components/angular-bootstrap-nav-tree/dist/abn_tree.css']
    }, {
        name: 'angular-ladda',
        files: ['static/bower_components/angular-ladda/dist/angular-ladda.min.js']
    }, {
        name: 'ngTable',
        files: ['static/bower_components/ng-table/dist/ng-table.min.js', 'static/bower_components/ng-table/dist/ng-table.min.css']
    }, {
        name: 'ui.select',
        files: ['static/bower_components/angular-ui-select/dist/select.min.js', 'static/bower_components/angular-ui-select/dist/select.min.css', 'static/bower_components/select2/dist/css/select2.min.css', 'static/bower_components/select2-bootstrap-css/select2-bootstrap.min.css', 'static/bower_components/selectize/dist/css/selectize.bootstrap3.css']
    }, {
        name: 'ui.mask',
        files: ['static/bower_components/angular-ui-utils/mask.min.js']
    }, {
        name: 'ngImgCrop',
        files: ['static/bower_components/ngImgCrop/compile/minified/ng-img-crop.js', 'static/bower_components/ngImgCrop/compile/minified/ng-img-crop.css']
    }, {
        name: 'angularFileUpload',
        files: ['static/bower_components/angular-file-upload/angular-file-upload.min.js']
    }, {
        name: 'ngAside',
        files: ['static/bower_components/angular-aside/dist/js/angular-aside.min.js', 'static/bower_components/angular-aside/dist/css/angular-aside.min.css']
    }, {
        name: 'truncate',
        files: ['static/bower_components/angular-truncate/src/truncate.js']
    }, {
        name: 'oitozero.ngSweetAlert',
        files: ['static/bower_components/angular-sweetalert-promised/SweetAlert.min.js']
    }, {
        name: 'monospaced.elastic',
        files: ['static/bower_components/angular-elastic/elastic.js']
    }, {
        name: 'ngMap',
        files: ['static/bower_components/ngmap/build/scripts/ng-map.min.js']
    }, {
        name: 'tc.chartjs',
        files: ['static/bower_components/tc-angular-chartjs/dist/tc-angular-chartjs.min.js']
    }, {
        name: 'flow',
        files: ['static/bower_components/ng-flow/dist/ng-flow-standalone.min.js']
    }, {
        name: 'uiSwitch',
        files: ['static/bower_components/angular-ui-switch/angular-ui-switch.min.js', 'static/bower_components/angular-ui-switch/angular-ui-switch.min.css']
    }, {
        name: 'ckeditor',
        files: ['static/bower_components/angular-ckeditor/angular-ckeditor.min.js']
    }, {
        name: 'mwl.calendar',
        files: ['static/bower_components/angular-bootstrap-calendar/dist/js/angular-bootstrap-calendar-tpls.js', 'static/bower_components/angular-bootstrap-calendar/dist/css/angular-bootstrap-calendar.min.css', 'static/assets/js/config/config-calendar.js']
    }, {
        name: 'ng-nestable',
        files: ['static/bower_components/ng-nestable/src/angular-nestable.js']
    }, {
        name: 'vAccordion',
        files: ['static/bower_components/v-accordion/dist/v-accordion.min.js', 'static/bower_components/v-accordion/dist/v-accordion.min.css']
    }, {
        name: 'xeditable',
        files: ['static/bower_components/angular-xeditable/dist/js/xeditable.min.js', 'static/bower_components/angular-xeditable/dist/css/xeditable.css', 'static/assets/js/config/config-xeditable.js']
    }, {
        name: 'checklist-model',
        files: ['static/bower_components/checklist-model/checklist-model.js']
    }, {
        name: 'angular-notification-icons',
        files: ['static/bower_components/angular-notification-icons/dist/angular-notification-icons.min.js', 'static/bower_components/angular-notification-icons/dist/angular-notification-icons.min.css']
    }, {
        name: 'angularSpectrumColorpicker',
        files: ['static/bower_components/angular-spectrum-colorpicker/dist/angular-spectrum-colorpicker.min.js']
    }]
});
