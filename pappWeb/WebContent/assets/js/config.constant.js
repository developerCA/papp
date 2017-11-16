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
        'd3': 'bower_components/d3/d3.min.js',

        //*** jQuery Plugins
        'chartjs': 'bower_components/chartjs/Chart.min.js',
        'ckeditor-plugin': 'bower_components/ckeditor/ckeditor.js',
        'jquery-nestable-plugin': ['bower_components/jquery-nestable/jquery.nestable.js'],
        'touchspin-plugin': ['bower_components/bootstrap-touchspin/dist/jquery.bootstrap-touchspin.min.js', 'bower_components/bootstrap-touchspin/dist/jquery.bootstrap-touchspin.min.css'],
        'jquery-appear-plugin': ['bower_components/jquery-appear/build/jquery.appear.min.js'],
        'spectrum-plugin': ['bower_components/spectrum/spectrum.js', 'bower_components/spectrum/spectrum.css'],
		'jcrop-plugin': ['bower_components/Jcrop/js/jquery.Jcrop.min.js', 'bower_components/Jcrop/css/jquery.Jcrop.min.css'],
		
		
        //*** Controllers
        'dashboardCtrl': 'assets/js/controllers/dashboardCtrl.js',
        'iconsCtrl': 'assets/js/controllers/iconsCtrl.js',
        'vAccordionCtrl': 'assets/js/controllers/vAccordionCtrl.js',
        'ckeditorCtrl': 'assets/js/controllers/ckeditorCtrl.js',
        'laddaCtrl': 'assets/js/controllers/laddaCtrl.js',
        'ngTableCtrl': 'assets/js/controllers/ngTableCtrl.js',
        'cropCtrl': 'assets/js/controllers/cropCtrl.js',
        'asideCtrl': 'assets/js/controllers/asideCtrl.js',
        'toasterCtrl': 'assets/js/controllers/toasterCtrl.js',
        'sweetAlertCtrl': 'assets/js/controllers/sweetAlertCtrl.js',
        'mapsCtrl': 'assets/js/controllers/mapsCtrl.js',
        'chartsCtrl': 'assets/js/controllers/chartsCtrl.js',
        'calendarCtrl': 'assets/js/controllers/calendarCtrl.js',
        'nestableCtrl': 'assets/js/controllers/nestableCtrl.js',
        'validationCtrl': ['assets/js/controllers/validationCtrl.js'],
        'userCtrl': ['assets/js/controllers/userCtrl.js'],
        'selectCtrl': 'assets/js/controllers/selectCtrl.js',
        'wizardCtrl': 'assets/js/controllers/wizardCtrl.js',
        'uploadCtrl': 'assets/js/controllers/uploadCtrl.js',
        'treeCtrl': 'assets/js/controllers/treeCtrl.js',
        'inboxCtrl': 'assets/js/controllers/inboxCtrl.js',
        'xeditableCtrl': 'assets/js/controllers/xeditableCtrl.js',
        'chatCtrl': 'assets/js/controllers/chatCtrl.js',
        'dynamicTableCtrl': 'assets/js/controllers/dynamicTableCtrl.js',
        'notificationIconsCtrl': 'assets/js/controllers/notificationIconsCtrl.js',
        'dateRangeCtrl': 'assets/js/controllers/daterangeCtrl.js',
        'notifyCtrl': 'assets/js/controllers/notifyCtrl.js',
        'sliderCtrl': 'assets/js/controllers/sliderCtrl.js',
        'knobCtrl': 'assets/js/controllers/knobCtrl.js',
        'crop2Ctrl': 'assets/js/controllers/crop2Ctrl.js',
        'MenuCtrl':'assets/js/controllers/papp/menuCtrl.js',
        'SeguridadFactory':'assets/js/factory/seguridadFactory.js',
        'EjerciciosCtrl':'assets/js/controllers/papp/ejerciciosFiscalesCtrl.js',
        'EjerciciosFactory':'assets/js/factory/ejerciciosFiscalesFactory.js',
        'GruposMedidaCtrl':'assets/js/controllers/papp/gruposMedidaCtrl.js',
        'GruposMedidaFactory':'assets/js/factory/gruposMedidaFactory.js',
        'UnidadesMedidaCtrl':'assets/js/controllers/papp/unidadesMedidaCtrl.js',
        'UnidadesMedidaFactory': 'assets/js/factory/unidadesMedidaFactory.js',
        'TipoIdentificacionCtrl': 'assets/js/controllers/papp/tipoIdentificacionCtrl.js',
        'TipoIdentificacionFactory': 'assets/js/factory/tipoIdentificacionFactory.js',
        'ObrasCtrl': 'assets/js/controllers/papp/obrasCtrl.js',
        'ObrasFactory': 'assets/js/factory/obrasFactory.js',
        'ParametroCtrl':'assets/js/controllers/papp/parametroCtrl.js',
        'ParametroFactory':'assets/js/factory/parametroFactory.js',
        'ConsecutivoCtrl':'assets/js/controllers/papp/consecutivoCtrl.js',
        'ConsecutivoFactory':'assets/js/factory/consecutivoFactory.js',
        'FuenteCtrl':'assets/js/controllers/papp/fuentefinanciamientoCtrl.js',
        'FuenteFactory':'assets/js/factory/fuentefinanciamientoFactory.js',
        'OrganismoCtrl':'assets/js/controllers/papp/organismoCtrl.js',
        'OrganismoFactory':'assets/js/factory/organismoFactory.js',
        'ClaseRegistroCtrl':'assets/js/controllers/papp/claseRegistroCtrl.js',
        'ClaseRegistroFactory':'assets/js/factory/claseRegistroFactory.js',
        'ProcedimientoCtrl': 'assets/js/controllers/papp/procedimientoCtrl.js',
        'ProcedimientoFactory': 'assets/js/factory/procedimientoFactory.js',
        'TipoProductoCtrl': 'assets/js/controllers/papp/tipoProductoCtrl.js',
        'TipoProductoFactory': 'assets/js/factory/tipoProductoFactory.js',
        'ItemCtrl': 'assets/js/controllers/papp/itemCtrl.js',
        'ItemFactory': 'assets/js/factory/itemFactory.js',
        'ClaseModificacionCtrl':'assets/js/controllers/papp/claseModificacionCtrl.js',
        'ClaseModificacionFactory':'assets/js/factory/claseModificacionFactory.js',
        'TipoDocumentoCtrl':'assets/js/controllers/papp/tipoDocumentoCtrl.js',
        'TipoDocumentoFactory':'assets/js/factory/tipoDocumentoFactory.js',
        'TipoRegimenCtrl':'assets/js/controllers/papp/tipoRegimenCtrl.js',
        'TipoRegimenFactory':'assets/js/factory/tipoRegimenFactory.js',
        'NivelOrganicoCtrl':'assets/js/controllers/papp/nivelOrganicoCtrl.js',
        'NivelOrganicoFactory':'assets/js/factory/nivelOrganicoFactory.js',
        'GrupoJerarquicoCtrl':'assets/js/controllers/papp/grupoJerarquicoCtrl.js',
        'GrupoJerarquicoFactory':'assets/js/factory/grupoJerarquicoFactory.js',
        'CargoCtrl':'assets/js/controllers/papp/cargoCtrl.js',
        'CargoFactory':'assets/js/factory/cargoFactory.js',
        'UnidadCtrl':'assets/js/controllers/papp/unidadCtrl.js',
        'UnidadFactory':'assets/js/factory/unidadFactory.js',
        'DivisionGeograficaCtrl':'assets/js/controllers/papp/divisionGeograficaCtrl.js',
        'DivisionGeograficaFactory':'assets/js/factory/divisionGeograficaFactory.js',
        'ModalDivisionGeograficaCtrl':'assets/js/controllers/papp/modales/modalDivisionGeograficaCtrl.js',
        'GradoCtrl':'assets/js/controllers/papp/gradoCtrl.js',
        'GradoFactory':'assets/js/factory/gradoFactory.js',
        'ClasificacionCtrl':'assets/js/controllers/papp/clasificacionCtrl.js',
        'ClasificacionFactory':'assets/js/factory/clasificacionFactory.js',
        'FuerzaCtrl':'assets/js/controllers/papp/fuerzaCtrl.js',
        'FuerzaFactory':'assets/js/factory/fuerzaFactory.js',
        'EscalaRemuneracionCtrl':'assets/js/controllers/papp/escalaRemuneracionCtrl.js',
        'EscalaRemuneracionFactory':'assets/js/factory/escalaRemuneracionFactory.js',
        'MenuSeguridadesCtrl':'assets/js/controllers/papp/menuSeguridadesCtrl.js',
        'MenuSeguridadesFactory':'assets/js/factory/menuSeguridadesFactory.js',
        'MenuPadreCtrl':'assets/js/controllers/papp/modales/menuPadreCtrl.js',   
        'PermisosCtrl':'assets/js/controllers/papp/permisosCtrl.js',
        'PermisosFactory':'assets/js/factory/permisosFactory.js',
        'PerfilesCtrl':'assets/js/controllers/papp/perfilesCtrl.js',
        'PerfilesFactory':'assets/js/factory/perfilesFactory.js',
        'PerfilesPermisosCtrl':'assets/js/controllers/papp/modales/perfilesPermisosCtrl.js',   
        'UsuariosCtrl':'assets/js/controllers/papp/usuariosCtrl.js',
        'UsuariosFactory':'assets/js/factory/usuariosFactory.js',
        'UsuariosUnidadesCtrl':'assets/js/controllers/papp/modales/usuariosUnidadesCtrl.js',   
        'CambiarContrasenaCtrl':'assets/js/controllers/papp/cambiarContrasenaCtrl.js',
        'CambiarContrasenaFactory':'assets/js/factory/cambiarContrasenaFactory.js',
        'InstitucionCtrl':'assets/js/controllers/papp/institucionCtrl.js',
        'InstitucionFactory':'assets/js/factory/institucionFactory.js',
        'PlanNacionalCtrl':'assets/js/controllers/papp/plannacionalCtrl.js',
        'PlanNacionalFactory':'assets/js/factory/plannacionalFactory.js',
        'GradoFuerzaCtrl':'assets/js/controllers/papp/gradofuerzaCtrl.js',
        'GradoFuerzaFactory':'assets/js/factory/gradofuerzaFactory.js',
        'ModalFuerzaCtrl':'assets/js/controllers/papp/modales/modalFuerzaCtrl.js',   
        'ModalGradoCtrl':'assets/js/controllers/papp/modales/modalGradoCtrl.js',   
        'EspecialidadesCtrl':'assets/js/controllers/papp/especialidadesCtrl.js',
        'EspecialidadesFactory':'assets/js/factory/especialidadesFactory.js',
        'EstructuraOrganicaCtrl':'assets/js/controllers/papp/estructuraorganicaCtrl.js',
        'EstructuraOrganicaFactory':'assets/js/factory/estructuraorganicaFactory.js',
        'IndicadoresCtrl':'assets/js/controllers/papp/indicadoresCtrl.js',
        'IndicadoresFactory':'assets/js/factory/indicadoresFactory.js',
        'ModalUnidadMedidaCtrl':'assets/js/controllers/papp/modales/modalUnidadMedidaCtrl.js',   
        'ModalClasificacionCtrl':'assets/js/controllers/papp/modales/modalClasificacionCtrl.js',
        'ModalItemCtrl':'assets/js/controllers/papp/modales/modalItemCtrl.js',
        'SubItemCtrl': 'assets/js/controllers/papp/subitemCtrl.js',
        'SubItemsFactory': 'assets/js/factory/subitemFactory.js',
        'ModalSubItemCtrl':'assets/js/controllers/papp/modales/modalSubItemCtrl.js',        
        'ModalUnidadCtrl':'assets/js/controllers/papp/modales/modalUnidadesCtrl.js',
        'SociosNegocioCtrl': 'assets/js/controllers/papp/socioNegocioCtrl.js',
        'SociosNegocioFactory': 'assets/js/factory/socioNegocioFactory.js',
        'CopiarContenidoEjercicioFiscalCtrl':'assets/js/controllers/papp/copiarContenidoEjercicioFiscalCtrl.js',
        'CopiarContenidoEjercicioFiscalFactory':'assets/js/factory/copiarContenidoEjercicioFiscalFactory.js',
        'ModalEjerciciosFiscalesCtrl':'assets/js/controllers/papp/modales/modalEjerciciosFiscalesCtrl.js',
        'EjercicioFiscalFactory':'assets/js/factory/ejercicioFiscalFactory.js',
        'GradoEscalaCtrl': 'assets/js/controllers/papp/gradoEscalaCtrl.js',
        'GradoEscalaFactory': 'assets/js/factory/gradoEscalaFactory.js',
        'ModalFuerzaCtrl':'assets/js/controllers/papp/modales/modalFuerzaCtrl.js',
        'ModalGradoCtrl':'assets/js/controllers/papp/modales/modalGradoCtrl.js',
        'CargoEscalaCtrl': 'assets/js/controllers/papp/cargoEscalaCtrl.js',
        'CargoEscalaFactory': 'assets/js/factory/cargoEscalaFactory.js',
        'ModalEscalaCtrl':'assets/js/controllers/papp/modales/modalEscalaCtrl.js',
        'ModalCargoCtrl':'assets/js/controllers/papp/modales/modalCargoCtrl.js',
        'EmpleadoCtrl':'assets/js/controllers/papp/empleadoCtrl.js',
        'EmpleadoFactory':'assets/js/factory/empleadoFactory.js',
        'ModalGradoFuerzaCtrl':'assets/js/controllers/papp/modales/modalGradoFuerzaCtrl.js',
        
        
    },
    //*** angularJS Modules
    modules: [{
        name: 'toaster',
        files: ['bower_components/AngularJS-Toaster/toaster.js', 'bower_components/AngularJS-Toaster/toaster.css']
    }, {
        name: 'angularBootstrapNavTree',
        files: ['bower_components/angular-bootstrap-nav-tree/dist/abn_tree_directive.js', 'bower_components/angular-bootstrap-nav-tree/dist/abn_tree.css']
    }, {
        name: 'ngTable',
        files: ['bower_components/ng-table/dist/ng-table.min.js', 'bower_components/ng-table/dist/ng-table.min.css']
    }, {
        name: 'ui.mask',
        files: ['bower_components/angular-ui-utils/mask.min.js']
    }, {
        name: 'ngImgCrop',
        files: ['bower_components/ngImgCrop/compile/minified/ng-img-crop.js', 'bower_components/ngImgCrop/compile/minified/ng-img-crop.css']
    }, {
        name: 'angularFileUpload',
        files: ['bower_components/angular-file-upload/angular-file-upload.min.js']
    }, {
        name: 'monospaced.elastic',
        files: ['bower_components/angular-elastic/elastic.js']
    }, {
        name: 'ngMap',
        files: ['bower_components/ngmap/build/scripts/ng-map.min.js']
    }, {
        name: 'chart.js',
        files: ['bower_components/angular-chart.js/dist/angular-chart.min.js', 'bower_components/angular-chart.js/dist/angular-chart.min.css']
    }, {
        name: 'flow',
        files: ['bower_components/ng-flow/dist/ng-flow-standalone.min.js']
    }, {
        name: 'ckeditor',
        files: ['bower_components/angular-ckeditor/angular-ckeditor.min.js']
    }, {
        name: 'mwl.calendar',
        files: ['bower_components/angular-bootstrap-calendar/dist/js/angular-bootstrap-calendar-tpls.js', 'bower_components/angular-bootstrap-calendar/dist/css/angular-bootstrap-calendar.min.css', 'assets/js/config/config-calendar.js']
    }, {
        name: 'ng-nestable',
        files: ['bower_components/ng-nestable/src/angular-nestable.js']
    }, {
        name: 'ngNotify',
        files: ['bower_components/ng-notify/dist/ng-notify.min.js', 'bower_components/ng-notify/dist/ng-notify.min.css']
    }, {
        name: 'xeditable',
        files: ['bower_components/angular-xeditable/dist/js/xeditable.min.js', 'bower_components/angular-xeditable/dist/css/xeditable.css', 'assets/js/config/config-xeditable.js']
    }, {
        name: 'checklist-model',
        files: ['bower_components/checklist-model/checklist-model.js']
    }, {
        name: 'ui.knob',
        files: ['bower_components/ng-knob/dist/ng-knob.min.js']
    }, {
        name: 'ngAppear',
        files: ['bower_components/angular-appear/build/angular-appear.min.js']
    }, {
        name: 'countTo',
        files: ['bower_components/angular-count-to-0.1.1/dist/angular-filter-count-to.min.js']
    }, {
        name: 'angularSpectrumColorpicker',
        files: ['bower_components/angular-spectrum-colorpicker/dist/angular-spectrum-colorpicker.min.js']
    }]
});