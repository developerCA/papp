'use strict';

/**
 * Config for the router
 **/
app.config(['$stateProvider', '$urlRouterProvider', '$controllerProvider', '$compileProvider', '$filterProvider', '$provide', '$ocLazyLoadProvider', 'JS_REQUIRES',
function ($stateProvider, $urlRouterProvider, $controllerProvider, $compileProvider, $filterProvider, $provide, $ocLazyLoadProvider, jsRequires) {

    app.controller = $controllerProvider.register;
    app.directive = $compileProvider.directive;
    app.filter = $filterProvider.register;
    app.factory = $provide.factory;
    app.service = $provide.service;
    app.constant = $provide.constant;
    app.value = $provide.value;

    // LAZY MODULES

    $ocLazyLoadProvider.config({
        debug: false,
        events: true,
        modules: jsRequires.modules
    });

    // APPLICATION ROUTES
    // -----------------------------------
    // For any unmatched url, redirect to /app/dashboard
    $urlRouterProvider.otherwise("/app/dashboard");
    //
    // Set up the states
    $stateProvider.state('app', {
        url: "/app",
        templateUrl: "assets/views/app.html",
        resolve: loadSequence('chartjs', 'chart.js', 'chatCtrl','MenuCtrl','SeguridadFactory',"ngTable","EjerciciosCtrl","EjerciciosFactory"),
        abstract: true
    }).state('app.dashboard', {
        url: "/dashboard",
        templateUrl: "assets/views/dashboard.html",
        resolve: loadSequence('d3', 'ui.knob', 'countTo', 'dashboardCtrl'),
        title: 'Dashboard',
        ncyBreadcrumb: {
            label: '.'
        }
    }).state('app.pagelayouts', {
        url: '/ui',
        template: '<div ui-view class="fade-in-up"></div>',
        title: 'Page Layouts',
        ncyBreadcrumb: {
            label: 'Page Layouts'
        }
    }).state('app.pagelayouts.fixedheader', {
        url: "/fixed-header",
        templateUrl: "assets/views/dashboard-2.html",
        resolve: loadSequence('d3', 'ui.knob', 'countTo', 'dashboardCtrl'),
        title: 'Fixed Header',
        ncyBreadcrumb: {
            label: 'Fixed Header'
        },
        controller: function ($scope) {
            $scope.setLayout();
            $scope.app.layout.isNavbarFixed = true;
        }
    }).state('app.pagelayouts.fixedsidebar', {
        url: "/fixed-sidebar",
        templateUrl: "assets/views/dashboard-3.html",
        resolve: loadSequence('d3', 'ui.knob', 'countTo', 'dashboardCtrl'),
        title: 'Fixed Sidebar',
        ncyBreadcrumb: {
            label: 'Fixed Sidebar'
        },
        controller: function ($scope) {
            $scope.setLayout();
            $scope.app.layout.isSidebarFixed = true;
        }
    }).state('app.pagelayouts.fixedheadersidebar', {
        url: "/fixed-header-and-sidebar",
        templateUrl: "assets/views/dashboard-4.html",
        resolve: loadSequence('d3', 'ui.knob', 'countTo', 'dashboardCtrl'),
        title: 'Fixed Header &amp; Sidebar',
        ncyBreadcrumb: {
            label: 'Fixed Header & Sidebar'
        },
        controller: function ($scope) {
            $scope.setLayout();
            $scope.app.layout.isSidebarFixed = true;
            $scope.app.layout.isNavbarFixed = true;
        }
    }).state('app.pagelayouts.fixedfooter', {
        url: "/fixed-footer",
        templateUrl: "assets/views/dashboard-5.html",
        resolve: loadSequence('d3', 'ui.knob', 'countTo', 'dashboardCtrl'),
        title: 'Fixed Footer',
        ncyBreadcrumb: {
            label: 'Fixed Footer'
        },
        controller: function ($scope) {
            $scope.setLayout();
            $scope.app.layout.isFooterFixed = true;
        }
    }).state('app.pagelayouts.boxedpage', {
        url: "/boxed-page",
        templateUrl: "assets/views/dashboard.html",
        resolve: loadSequence('dashboardCtrl', 'd3', 'ui.knob'),
        title: 'Boxed Page',
        ncyBreadcrumb: {
            label: 'Boxed Page'
        }
    }).state('app.ejercicios', {
        url: "/ejerciciosfiscales",
        templateUrl: "assets/views/papp/ejerciciosFiscales.html",
        resolve: loadSequence('ngTable','EjerciciosCtrl','EjerciciosFactory'),
        title: 'Ejercicios Fiscales',
        
        ncyBreadcrumb: {
            label: 'Ejercicios Fiscales'
        }
    }).state('app.grupomedida', {
        url: "/grupos-medida",
        templateUrl: "assets/views/papp/gruposMedida.html",
        resolve: loadSequence('ngTable','GruposMedidaCtrl','GruposMedidaFactory'),
        title: 'Grupos Medida',
        
        ncyBreadcrumb: {
            label: 'Grupos Medida'
        }
    }).state('app.unidadesmedida', {
        url: "/unidades-medida",
        templateUrl: "assets/views/papp/unidadesMedida.html",
        resolve: loadSequence('ngTable','UnidadesMedidaCtrl','UnidadesMedidaFactory','GruposMedidaFactory'),
        title: 'Unidades Medida',        
        ncyBreadcrumb: {
            label: 'Unidades Medida'
        }
    }).state('app.parametros', {
        url: "/param",
        templateUrl: "assets/views/papp/parametros.html",
        resolve: loadSequence('ngTable','ParametroCtrl','ParametroFactory'),
        title: 'Parametros',
        
        ncyBreadcrumb: {
            label: 'Parametros'
        }
    }).state('app.consecutivos', {
        url: "/consecutivos",
        templateUrl: "assets/views/papp/consecutivos.html",
        resolve: loadSequence('ngTable','ConsecutivoCtrl','ConsecutivoFactory'),
        title: 'Consecutivos',
        
        ncyBreadcrumb: {
            label: 'Consecutivos'
        }    
    }).state('app.tipoidentificacion', {
        url: "/tipo-identificacion",
        templateUrl: "assets/views/papp/tipoIdentificacion.html",
        resolve: loadSequence('ngTable', 'TipoIdentificacionCtrl', 'TipoIdentificacionFactory'),
        title: 'Tipo Identificacion',
        ncyBreadcrumb: {
            label: 'Tipo Identificacion'
        }
    }).state('app.obras', {
        url: "/obras",
        templateUrl: "assets/views/papp/obras.html",
        resolve: loadSequence('ngTable', 'ObrasCtrl', 'ObrasFactory'),
        title: 'Obras',
        ncyBreadcrumb: {
            label: 'Obras'
        }
    }).state('app.fuentefin', {
        url: "/fuentefin",
        templateUrl: "assets/views/papp/fuentefinanciamiento.html",
        resolve: loadSequence('ngTable','FuenteCtrl','FuenteFactory'),
        title: 'Fuentes de Financiamiento',
        ncyBreadcrumb: {
            label: 'Fuentes de Financiamiento'
        }
    }).state('app.organismo', {
        url: "/organismo",
        templateUrl: "assets/views/papp/organismo.html",
        resolve: loadSequence('ngTable','OrganismoCtrl','OrganismoFactory'),
        title: 'Organismos',
        ncyBreadcrumb: {
            label: 'Organismos'
        } 
    }).state('app.clasesregistro', {
        url: "/claseregistro",
        templateUrl: "assets/views/papp/clasesregistro.html",
        resolve: loadSequence('ngTable','ClaseRegistroCtrl','ClaseRegistroFactory'),
        title: 'Clases de Registro',
        ncyBreadcrumb: {
            label: 'Clases de Registro'
        }     
    
    }).state('app.procedimientos', {
        url: "/procedimientos",
        templateUrl: "assets/views/papp/procedimientos.html",
        resolve: loadSequence('ngTable', 'ProcedimientoCtrl', 'ProcedimientoFactory'),
        title: 'Modulo de Procedimientos',
        ncyBreadcrumb: {
            label: 'Modulo de Procedimientos'
        }
    }).state('app.subitem', {
        url: "/subitems",
        templateUrl: "assets/views/papp/subitem.html",
        resolve: loadSequence(
        		'ngTable', 'SubItemCtrl','SubItemsFactory',
        		'ModalItemCtrl','ItemFactory',
        		'ModalUnidadMedidaCtrl','UnidadesMedidaFactory'
		),
        title: 'Modulo de Sub Items',
        ncyBreadcrumb: {
            label: 'Modulo de Sub Items'
        }
    
    }).state('app.item', {
        url: "/items",
        templateUrl: "assets/views/papp/item.html",
        resolve: loadSequence('ngTable', 'ItemCtrl', 'ModalItemCtrl', 'ItemFactory'),
        title: 'Modulo de Items',
        ncyBreadcrumb: {
            label: 'Modulo de Items'
        }
    }).state('app.socionegocio', {
        url: "/socionegocio",
        templateUrl: "assets/views/papp/socioNegocio.html",
        resolve: loadSequence('ngTable','SociosNegocioCtrl','SociosNegocioFactory'),
        title: 'Socios Negocio',
        
        ncyBreadcrumb: {
            label: 'Socios Negocio'
        }
    }).state('app.tipoproducto', {
        url: "/tipo-producto",
        templateUrl: "assets/views/papp/tipoProducto.html",
        resolve: loadSequence('ngTable', 'TipoProductoCtrl', 'TipoProductoFactory'),
        title: 'Modulo de Tipo Productos',
        ncyBreadcrumb: {
            label: 'Modulo de Tipo Productos'
        }
    }).state('app.clasesmodificacion', {
        url: "/clasesmodificacion",
        templateUrl: "assets/views/papp/clasesmodificacion.html",
        resolve: loadSequence('ngTable','ClaseModificacionCtrl','ClaseModificacionFactory'),
        title: 'Clases de Modificacion',
        ncyBreadcrumb: {
            label: 'Clases de Modificacion'
        }   
    }).state('app.tipodocumento', {
        url: "/tipodocumento",
        templateUrl: "assets/views/papp/tipoDocumento.html",
        resolve: loadSequence('ngTable','TipoDocumentoCtrl','TipoDocumentoFactory'),
        title: 'Tipos de Documento',
        ncyBreadcrumb: {
            label: 'Tipos de Documento'
        } 
    }).state('app.tiporegimen', {
        url: "/tiporegimen",
        templateUrl: "assets/views/papp/tipoRegimen.html",
        resolve: loadSequence('ngTable','TipoRegimenCtrl','TipoRegimenFactory'),
        title: 'Tipos de Regimen',
        ncyBreadcrumb: {
            label: 'Tipos de Regimen'
        } 
    }).state('app.nivelorganico', {
        url: "/nivelorganico",
        templateUrl: "assets/views/papp/nivelOrganico.html",
        resolve: loadSequence('ngTable','NivelOrganicoCtrl','NivelOrganicoFactory'),
        title: 'Nivel Organico',
        ncyBreadcrumb: {
            label: 'Nivel Organico'
        } 
    }).state('app.grupojerarquico', {
        url: "/grupojerarquico",
        templateUrl: "assets/views/papp/grupoJerarquico.html",
        resolve: loadSequence('ngTable','GrupoJerarquicoCtrl','GrupoJerarquicoFactory'),
        title: 'Grupo Jerarquico',
        ncyBreadcrumb: {
            label: 'Grupo Jerarquico'
        }  
    }).state('app.cargos', {
        url: "/cargos",
        templateUrl: "assets/views/papp/cargos.html",
        resolve: loadSequence('ngTable','CargoCtrl','CargoFactory'),
        title: 'Cargos',
        ncyBreadcrumb: {
            label: 'Cargos'
        }  
    
    }).state('app.unidades', {
        url: "/unidades",
        templateUrl: "assets/views/papp/unidades.html",
        resolve: loadSequence('ngTable','UnidadCtrl','UnidadFactory','ModalInstitutoEntidadCtrl','InstitutoEntidadFactory' ),
        title: 'Unidades',
        ncyBreadcrumb: {
            label: 'Unidades'
        } 
    }).state('app.divgeografica', {
        url: "/divisionesgeo",
        templateUrl: "assets/views/papp/divisiones.html",
        resolve: loadSequence(
    		'ngTable','DivisionGeograficaCtrl','DivisionGeograficaFactory',
    		'ModalDivisionGeograficaCtrl'),
        title: 'Divisiones Geograficas',
        ncyBreadcrumb: {
            label: 'Divisiones Geograficas'
        } 

    }).state('app.grados', {
        url: "/grados",
        templateUrl: "assets/views/papp/grados.html",
        resolve: loadSequence('ngTable','GradoCtrl','GrupoJerarquicoFactory','GradoFactory'),
        title: 'Grados',
        ncyBreadcrumb: {
            label: 'Grados'
        }     
    }).state('app.clasificaciones', {
        url: "/clasificacion",
        templateUrl: "assets/views/papp/clasificaciones.html",
        resolve: loadSequence('ngTable','ClasificacionCtrl','ClasificacionFactory'),
        title: 'Clasificaciones',
        ncyBreadcrumb: {
            label: 'Clasificaciones'
        }     
    }).state('app.fuerzas', {
        url: "/fuerzas",
        templateUrl: "assets/views/papp/fuerzas.html",
        resolve: loadSequence('ngTable','FuerzaCtrl','ModalClasificacionCtrl','FuerzaFactory', 'ClasificacionFactory'),
        title: 'Fuerzas',
        ncyBreadcrumb: {
            label: 'Fuerzas'
        }    
    }).state('app.escalasremuneracion', {
        url: "/escalasremuneracion",
        templateUrl: "assets/views/papp/escalasRemuneracion.html",
        resolve: loadSequence('ngTable','EscalaRemuneracionCtrl','EscalaRemuneracionFactory'),
        title: 'Escalas',
        ncyBreadcrumb: {
            label: 'Escalas'
        } 
    }).state('app.menuseguridades', {
        url: "/menuseguridades",
        templateUrl: "assets/views/papp/menuSeguridades.html",
        resolve: loadSequence('ngTable','MenuSeguridadesCtrl','MenuPadreCtrl','MenuSeguridadesFactory',
        		'ModalPerfilesPermisosCtrl','PerfilesPermisosFactory',
        		'ModalPermisosCtrl','PermisosFactory'),
        title: 'Menu',
        ncyBreadcrumb: {
            label: 'Menu'
        } 
    }).state('app.permisos', {
        url: "/permisos",
        templateUrl: "assets/views/papp/permisos.html",
        resolve: loadSequence('ngTable','PermisosCtrl','PermisosFactory'),
        title: 'Permisos',
        ncyBreadcrumb: {
            label: 'Permisos'
        } 
    }).state('app.perfiles', {
        url: "/perfiles",
        templateUrl: "assets/views/papp/perfiles.html",
        resolve: loadSequence('ngTable','PerfilesCtrl','PerfilesFactory',
        		'ModalPermisosCtrl','PermisosFactory'),
        title: 'Perfiles',
        ncyBreadcrumb: {
            label: 'Perfiles'
        } 
    }).state('app.usuarios', {
        url: "/usuarios",
        templateUrl: "assets/views/papp/usuarios.html",
        resolve: loadSequence('ngTable','UsuariosCtrl','UsuariosFactory',
        		'UsuariosUnidadesCtrl','UnidadFactory',
        		'ModalNombrePerfilCtrl','PerfilesFactory',
        		'ModalSocioNegocioEmpleadosCtrl','SociosNegocioFactory',
        		'ModalUnidadActivasCtrl',
        		'ModalUnidadCortoCtrl','UnidadFactory'),
        title: 'Usuarios',
        ncyBreadcrumb: {
            label: 'Usuarios'
        } 
    }).state('app.cambiarcontrasena', {
        url: "/cambiarcontrasena",
        templateUrl: "assets/views/papp/cambiarContrasena.html",
        resolve: loadSequence('ngTable','CambiarContrasenaCtrl','CambiarContrasenaFactory'),
        title: 'Cambiar Contrasena',
        ncyBreadcrumb: {
            label: 'Cambiar Contrasena'
        } 
    }).state('app.cambiarcontrasenaunico', {
        url: "/cambiarcontrasenaunico",
        templateUrl: "assets/views/papp/cambiarContrasenaUnico.html",
        resolve: loadSequence('ngTable','CambiarContrasenaUnicoCtrl','CambiarContrasenaUnicoFactory'),
        title: 'Cambiar Contrasena',
        ncyBreadcrumb: {
            label: 'Cambiar Contrasena'
        } 
    }).state('app.institucion', {
        url: "/institucion",
        templateUrl: "assets/views/papp/institucion.html",
        resolve: loadSequence(
    		'ngTable','InstitucionCtrl','InstitucionFactory',
    		'ModalDivisionGeograficaCtrl','DivisionGeograficaFactory'
		),
        title: 'Institución',
        ncyBreadcrumb: {
            label: 'Institución'
        }
    }).state('app.plannacional', {
        url: "/plannacional",
        templateUrl: "assets/views/papp/plannacional.html",
        resolve: loadSequence('ngTable','PlanNacionalCtrl','PlanNacionalFactory'),
        title: 'Plan Nacional',
        ncyBreadcrumb: {
            label: 'Plan Nacional'
        }
    }).state('app.gradofuerza', {
        url: "/gradofuerza",
        templateUrl: "assets/views/papp/gradofuerza.html",
        resolve: loadSequence(
    		'ngTable','GradoFuerzaCtrl','ModalFuerzaCtrl',
    		'ModalGradoCtrl','GradoFuerzaFactory','FuerzaFactory',
    		'GradoFactory','GrupoJerarquicoFactory','ModalGradoFuerzaSuperiorCtrl'
		),
        title: 'Grado - Fuerza',
        ncyBreadcrumb: {
            label: 'Grado - Fuerza'
        }
    }).state('app.especialidades', {
        url: "/especialidades",
        templateUrl: "assets/views/papp/especialidades.html",
        resolve: loadSequence('ngTable','EspecialidadesCtrl','ModalFuerzaCtrl','EspecialidadesFactory','FuerzaFactory'),
        title: 'Grado - Fuerza',
        ncyBreadcrumb: {
            label: 'Grado - Fuerza'
        }
    }).state('app.estructuraorganica', {
        url: "/estructuraorganica",
        templateUrl: "assets/views/papp/estructuraorganica.html",
        resolve: loadSequence(
        		'ngTable','EstructuraOrganicaCtrl','EstructuraOrganicaFactory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalInstitutoEntidadCtrl','InstitutoEntidadFactory',
        		'ModalUnidadActivasCtrl','UnidadFactory',
        		'ModalNivelOrganicoCtrl','NivelOrganicoFactory',
        		'ModalCargoCtrl','CargoFactory',
        		'ModalGradoFuerzaCtrl','GradoFuerzaFactory',
        		'ModalEspecialidadesCtrl','EspecialidadesFactory',
        		'ModalClasificacionCtrl','ClasificacionFactory',
        		'ModalSocioNegocioEmpleadosCtrl','SociosNegocioFactory'
		),
        title: 'Estructura Orgánica',
        ncyBreadcrumb: {
            label: 'Estructura Orgánica'
        }
    }).state('app.planificacionestrategica', {
        url: "/planificacionestrategica",
        templateUrl: "assets/views/papp/planificacionEstrategica.html",
        resolve: loadSequence(
        		'ngTable','PlanificacionEstrategicaCtrl','PlanificacionEstrategicaFactory',
        		'ModalInstitucionCtrl','InstitucionFactory'
		),
        title: 'Planificación Estratégica',
        ncyBreadcrumb: {
            label: 'Planificación Estratégica'
        }
    }).state('app.matrizindicadores', {
        url: "/matrizindicadores",
        templateUrl: "assets/views/papp/matrizIndicadores.html",
        resolve: loadSequence(
        		'ngTable','MatrizIndicadoresCtrl','MatrizIndicadoresFactory',
        		'ModalInstitucionCtrl','InstitucionFactory'
		),
        title: 'Matriz de Indicadores',
        ncyBreadcrumb: {
            label: 'Matriz de Indicadores'
        }
    }).state('app.registrometas', {
        url: "/registrometas",
        templateUrl: "assets/views/papp/registroMetas.html",
        resolve: loadSequence(
        		'ngTable','RegistroMetasCtrl','RegistroMetasFactory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalProgramaCtrl','ProgramaFactory'
		),
        title: 'Registro Metas',
        ncyBreadcrumb: {
            label: 'Registro Metas'
        }
    }).state('app.matrizprogramacionanualpoliticapublica', {
        url: "/matrizprogramacionanualpoliticapublica",
        templateUrl: "assets/views/papp/matrizProgramacionAnualPoliticaPublica.html",
        resolve: loadSequence(
        		'ngTable','MatrizProgramacionAnualPoliticaPublicaCtrl','MatrizProgramacionAnualPoliticaPublicaFactory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalProgramaCtrl','ProgramaFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalProyectoCtrl','ProyectoFactory',
        		'ModalActividadCtrl','ActividadFactory'
		),
        title: 'Matriz Programacion Anual de la Politica Publica',
        ncyBreadcrumb: {
            label: 'Matriz Programacion Anual de la Politica Publica'
        }
    }).state('app.matrizformulacionactividadproyecto', {
        url: "/matrizformulacionactividadproyecto",
        templateUrl: "assets/views/papp/matrizFormulacionActividadProyecto.html",
        resolve: loadSequence(
        		'ngTable','MatrizFormulacionActividadProyectoCtrl','MatrizFormulacionActividadProyectoFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalActividadCtrl','ActividadFactory',
        		'ModalUnidadCortoCtrl','UnidadFactory'
		),
        title: 'Matriz de Formulación de Actividad sin Proyecto',
        ncyBreadcrumb: {
            label: 'Matriz de Formulación de Actividad sin Proyecto'
        }
    }).state('app.matrizdesglosadaprogramacionanual', {
        url: "/matrizdesglosadaprogramacionanual",
        templateUrl: "assets/views/papp/matrizDesglosadaProgramacionAnual.html",
        resolve: loadSequence(
        		'ngTable','MatrizDesglosadaProgramacionAnualCtrl','MatrizDesglosadaProgramacionAnualFactory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalInstitutoEntidadCtrl','InstitutoEntidadFactory',
        		'ModalProgramaCtrl','ProgramaFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalProyectoCtrl','ProyectoFactory',
        		'ModalActividadCtrl','ActividadFactory',
        		'ModalItemCtrl','ItemFactory',
        		'ModalFuenteFinanciamientoCtrl','FuenteFinanciamientoFactory'
		),
        title: 'Matriz Programación Anual de la Política Publica',
        ncyBreadcrumb: {
            label: 'Matriz Programación Anual de la Política Publica'
        }
    }).state('app.ejecucionmetas', {
        url: "/ejecucionmetas",
        templateUrl: "assets/views/papp/ejecucionMetas.html",
        resolve: loadSequence(
    		'ngTable','EjecucionMetasCtrl','EjecucionMetasFactory',
    		'ModalDivisionGeograficaCtrl','DivisionGeograficaFactory'
		),
        title: 'Registro Ejecucion de Metas',
        ncyBreadcrumb: {
            label: 'Registro Ejecucion de Metas'
        }
    }).state('app.formulacionestrategica', {
        url: "/formulacionestrategica",
        templateUrl: "assets/views/papp/formulacionEstrategica.html",
        resolve: loadSequence(
        		'ngTable','FormulacionEstrategicaCtrl','FormulacionEstrategicaFactory',
        		'ModalObjetivosCtrl','ObjetivosFactory',
        		'ModalFuerzaCtrl','FuerzaFactory',
        		'ModalSocioNegocioCtrl','SociosNegocioFactory',
        		'ModalIndicadoresCtrl','IndicadoresFactory',
        		'ModalIndicadoresActividadCtrl','IndicadoresActividadFactory',
        		'ModalUnidadCtrl','UnidadFactory'
		),
        title: 'Formulacion Estrategica',
        ncyBreadcrumb: {
            label: 'Formulacion Estrategica'
        }
    }).state('app.planificacionue', {
        url: "/planificacionue",
        templateUrl: "assets/views/papp/planificacionUE.html",
        resolve: loadSequence(
        		'ngTable', 'PlanificacionUECtrl', 'PlanificacionUEFactory',
        		'ModalItemCtrl', 'ItemFactory',
        		'ModalObraCtrl', 'ObrasFactory',
        		'ModalFuenteFinanciamientoCtrl', 'FuenteFactory',
        		'ModalOrganismoCtrl', 'OrganismoFactory',
        		'ModalDivisionGeograficaCtrl', 'DivisionGeograficaFactory',
        		'ModalSubItemCtrl', 'SubItemsFactory',
        		'ModalTipoProductoCtrl', 'TipoProductoFactory',
        		'ModalProcedimientoCtrl', 'TipoProcedimientoFactory',
        		'ModalTipoRegimenCtrl', 'ComunTipoRegimenFactory',
        		'ModalUnidadMedidaCtrl', 'UnidadesMedidaFactory',
        		'AprobacionPlanificacionFactory',
        		'PUECtrl'
		),
        title: 'Planificación UE',
        ncyBreadcrumb: {
            label: 'Planificación UE'
        }
    }).state('app.planificacionue1', {
        url: "/planificacionue1",
        templateUrl: "assets/views/papp/planificacionUE1.html",
        resolve: loadSequence(
        		'ngTable', 'PlanificacionUECtrl1', 'PlanificacionUEFactory',
        		'ModalItemCtrl', 'ItemFactory',
        		'ModalObraCtrl', 'ObrasFactory',
        		'ModalFuenteFinanciamientoCtrl', 'FuenteFactory',
        		'ModalOrganismoCtrl', 'OrganismoFactory',
        		'ModalDivisionGeograficaCtrl', 'DivisionGeograficaFactory',
        		'ModalSubItemCtrl', 'SubItemsFactory',
        		'ModalTipoProductoCtrl', 'TipoProductoFactory',
        		'ModalProcedimientoCtrl', 'TipoProcedimientoFactory',
        		'ModalTipoRegimenCtrl', 'ComunTipoRegimenFactory',
        		'ModalUnidadMedidaCtrl', 'UnidadesMedidaFactory',
        		'AprobacionPlanificacionFactory',
        		'PUECtrl'
		),
        title: 'Planificación UE',
        ncyBreadcrumb: {
            label: 'Planificación UE'
        }
    }).state('app.aprobacionplanificacion', {
        url: "/aprobacionplanificacion",
        templateUrl: "assets/views/papp/aprobacionPlanificacion.html",
        resolve: loadSequence(
        		'ngTable', 'AprobacionPlanificacionCtrl', 'AprobacionPlanificacionFactory'
		),
        title: 'Aprobacion Planificacion',
        ncyBreadcrumb: {
            label: 'Aprobacion Planificacion'
        }
    }).state('app.mantenerindicadores', {
        url: "/mantenerindicadores",
        templateUrl: "assets/views/papp/mantenerIndicadores.html",
        resolve: loadSequence(
        		'ngTable','MantenerIndicadoresCtrl','MantenerIndicadoresFactory',
        		'ModalUnidadMedidaCtrl','UnidadesMedidaFactory'
		),
        title: 'Mantener Indicadores',
        ncyBreadcrumb: {
            label: 'Mantener Indicadores'
        }
    }).state('app.estructurapartidapresupuestaria', {
        url: "/estructurapartidapresupuestaria",
        templateUrl: "assets/views/papp/estructuraPartidaPresupuestaria.html",
        resolve: loadSequence(
        		'ngTable','EstructuraPartidaPresupuestariaCtrl','EstructuraPartidaPresupuestariaFactory'
		),
        title: 'Estructura Partida Presupuestaria',
        ncyBreadcrumb: {
            label: 'Estructura Partida Presupuestaria'
        }
    }).state('app.planificacioninstitucional', {
        url: "/planificacioninstitucional",
        templateUrl: "assets/views/papp/planificacionInstitucional.html",
        resolve: loadSequence(
        		'ngTable','PlanificacionInstitucionalCtrl','PlanificacionInstitucionalFactory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalMetasCtrl','MetasFactory'
		),
        title: 'Planificación Institucional',
        ncyBreadcrumb: {
            label: 'Planificación Institucional'
        }
    }).state('app.copiarcontenidoejerciciofiscal', {
        url: "/copiarcontenidoejerciciofiscal",
        templateUrl: "assets/views/papp/copiarContenidoEjercicioFiscal.html",
        resolve: loadSequence('ngTable','CopiarContenidoEjercicioFiscalCtrl','ModalEjerciciosFiscalesCtrl','CopiarContenidoEjercicioFiscalFactory','EjercicioFiscalFactory'),
        title: 'Copiar Contenido Ejercicio Fiscal',
        ncyBreadcrumb: {
            label: 'Copiar Contenido Ejercicio Fiscal'
       } 
    
    }).state('app.gradoescala', {
        url: "/gradoescala",
        templateUrl: "assets/views/papp/gradoEscala.html",
        resolve: loadSequence('ngTable','GradoEscalaCtrl','GradoEscalaFactory','ModalFuerzaCtrl', 'FuerzaFactory', 'ModalGradoFuerzaCtrl', 'GrupoJerarquicoFactory', 'GradoFuerzaFactory','ModalEscalaCtrl', 'EscalaRemuneracionFactory'),
        title: 'Grado Escala',
        
        ncyBreadcrumb: {
            label: 'Grado Escala'
        }
    }).state('app.cargoescala', {
        url: "/cargoescala",
        templateUrl: "assets/views/papp/cargoEscala.html",
        resolve: loadSequence('ngTable','CargoEscalaCtrl','CargoEscalaFactory','ModalEscalaCtrl', 'EscalaRemuneracionFactory', 'ModalCargoCtrl', 'CargoFactory'),
        title: 'Grado Escala',
        
        ncyBreadcrumb: {
            label: 'Grado Escala'
        }
    }).state('app.empleado', {
        url: "/empleado",
        templateUrl: "assets/views/papp/empleado.html",
        resolve: loadSequence(
        		'ngTable','EmpleadoCtrl','EmpleadoFactory',
        		'ModalGradoEscalaCtrl','GradoEscalaFactory',
        		'ModalEspecialidadesCtrl','EspecialidadesFactory',
        		'ModalClasificacionCtrl','ClasificacionFactory',
        		'ModalTipoIdentificacionCtrl','TipoIdentificacionFactory',
        		'ModalCargoEscalaCtrl','CargoEscalaFactory'
		),
        title: 'Empleados',
        ncyBreadcrumb: {
            label: 'Empleados'
        }
    }).state('app.proveedorruc', {
        url: "/proveedorruc",
        templateUrl: "assets/views/papp/proveedorruc.html",
        resolve: loadSequence(
        		'ngTable','ProveedorRucCtrl','ProveedorRucFactory',
        		'ModalTipoIdentificacionCtrl','TipoIdentificacionFactory'
		),
        title: 'Proveedores Juridicos',
        ncyBreadcrumb: {
            label: 'Proveedores Juridicos'
        }
    }).state('app.certificacionesfondos', {
        url: "/certificacionesfondos",
        templateUrl: "assets/views/papp/certificacionesfondos.html",
        resolve: loadSequence(
        		'ngTable','CertificacionesFondosCtrl','CertificacionesFondosFactory',
        		'ModalUnidadCortoCtrl','UnidadFactory',
        		'ModalClaseGastoCtrl','ClaseGastoFactory',
        		'ModalClaseDocumentoCtrl','ClaseDocumentoFactory',
        		'ModalTipoDocumentoCtrl','TipoDocumentoFactory',
        		'ModalCertificacionesFondoLiquidacionManuaCtrl',
        		'ModalCertificacionesFondosLineasCtrl'
		),
        title: 'Certificado de Fondos',
        ncyBreadcrumb: {
            label: 'Certificado de Fondos'
        }
    }).state('app.aprobarcertificacionesfondos', {
        url: "/aprobarcertificacionesfondos",
        templateUrl: "assets/views/papp/aprobarCertificacionesFondos.html",
        resolve: loadSequence(
        		'ngTable','AprobarCertificacionesFondosCtrl','AprobarCertificacionesFondosFactory',
        		'ModalUnidadCortoCtrl','UnidadFactory',
        		'ModalClaseGastoCtrl','ClaseGastoFactory',
        		'ModalClaseDocumentoCtrl','ClaseDocumentoFactory',
        		'ModalTipoDocumentoCtrl','TipoDocumentoFactory',
        		'ModalCertificacionesFondoLiquidacionManuaCtrl'
		),
        title: 'Aprobación Certificado de Fondos',
        ncyBreadcrumb: {
            label: 'Aprobación Certificado de Fondos'
        }
    }).state('app.ordengasto', {
        url: "/ordengasto",
        templateUrl: "assets/views/papp/ordenGasto.html",
        resolve: loadSequence(
        		'ngTable','OrdenGastoCtrl','OrdenGastoFactory',
        		'ModalUnidadCortoCtrl','UnidadFactory',
        		'ModalCertificacionesFondoLiquidacionManuaCtrl','CertificacionesFondosFactory',
        		'ModalProveedorCtrl','ProveedorFactory',
        		'ModalCertificacionesFondosCtrl','CertificacionesFondosFactory',
        		'ModalOrdenGastoLineasCtrl','OrdenGastoLineasFactory',
        		'ModalClaseGastoCtrl','ClaseGastoFactory',
        		'ModalClaseDocumentoCtrl','ClaseDocumentoFactory',
        		'ModalContratoCtrl','ContratoFactory'
		),
        title: 'Orden de Gasto',
        ncyBreadcrumb: {
            label: 'Orden de Gasto'
        }
    }).state('app.ordendevengo', {
        url: "/ordendevengo",
        templateUrl: "assets/views/papp/ordenDevengo.html",
        resolve: loadSequence(
        		'ngTable','OrdenDevengoCtrl','OrdenDevengoFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalTipoDocumentoCtrl','TipoDocumentoFactory',
        		'ModalCertificacionesFondoLiquidacionManuaCtrl','CertificacionesFondosFactory',
        		'ModalOrdenDevengoLineasCtrl','OrdenDevengoLineasFactory',
        		'ModalOrdenGastoCtrl','OrdenGastoFactory'
		),
        title: 'Orden de Devengo',
        ncyBreadcrumb: {
            label: 'Orden de Devengo'
        }
    }).state('app.ordenreversion', {
        url: "/ordenreversion",
        templateUrl: "assets/views/papp/ordenReversion.html",
        resolve: loadSequence(
        		'ngTable','OrdenReversionCtrl','OrdenReversionFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalTipoDocumentoCtrl','TipoDocumentoFactory',
        		'ModalCertificacionesFondoLiquidacionManuaCtrl',
        		'CertificacionesFondosFactory',
        		'ModalOrdenDevengoLineasCtrl','OrdenDevengoLineasFactory',
        		'ModalOrdenGastoCtrl','OrdenGastoFactory','OrdenDevengoFactory',
        		'ModalOrdenReversionLineasCtrl', 'OrdenReversionLineasFactory',
        		'ModalCertificacionesFondosLineasCtrl'
		),
        title: 'Orden de Reversion',
        ncyBreadcrumb: {
            label: 'Orden de Reversion'
        }
    }).state('app.reformas', {
        url: "/reformas",
        templateUrl: "assets/views/papp/reformas.html",
        resolve: loadSequence(
        		'ngTable','ReformasCtrl','ReformasFactory',
        		'ModalUnidadCortoCtrl','UnidadFactory',
        		'ModalItemCtrl','ModalItemNivelActividadCtrl','ItemFactory',
        		'ModalCertificacionesFondoLiquidacionManuaCtrl',
        		'CertificacionesFondosFactory',
        		'ModalReformasLineasCtrl1',
        		'ModalReformasLineasDistMetaCtrl',
        		'ModalReformasLineasItemCtrl',
        		'ModalReformasLineasSubItemCtrl'
		),
        title: 'Reformas',
        ncyBreadcrumb: {
            label: 'Reformas'
        }
    }).state('app.reformasalasmetas', {
        url: "/reformasalasmetas",
        templateUrl: "assets/views/papp/reformasALasMetas.html",
        resolve: loadSequence(
        		'ngTable','ReformasALasMetasCtrl','ReformasALasMetasFactory',
        		'ModalUnidadCortoCtrl','UnidadFactory',
        		'ModalItemCtrl','ItemFactory',
        		'ModalReformasALasMetasLineasCtrl'
		),
        title: 'Reformas',
        ncyBreadcrumb: {
            label: 'Reformas'
        }
    }).state('app.contratos', {
        url: "/contrato",
        templateUrl: "assets/views/papp/contrato.html",
        resolve: loadSequence(
        		'ngTable','ContratoCtrl','ContratoFactory'
		),
        title: 'Contrato',
        ncyBreadcrumb: {
            label: 'Contrato'
        }
    }).state('app.reporteP01', {
        url: "/reporteP01",
        templateUrl: "assets/views/papp/reporteP01.html",
        resolve: loadSequence(
        		'ngTable','ReporteP01Ctrl','ReporteP01Factory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalInstitutoEntidadCtrl','InstitutoEntidadFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalProgramaCtrl','ProgramaFactory',
        		'ModalProyectoReporteCtrl','ProyectoReporteFactory',
        		'ModalActividadReporteCtrl','ActividadReporteFactory',
        		'ModalSubActividadReporteCtrl','SubActividadReporteFactory',
        		'ModalTareaReporteCtrl','TareaReporteFactory',
        		'ModalSubTareaReporteCtrl','SubTareaReporteFactory',
        		'ModalItemsReporteCtrl','ItemsReporteFactory',
        		'ModalSubItemsReporteCtrl','SubItemsReporteFactory',
        		'ModalFuenteFinanciamientoCtrl','FuenteFinanciamientoFactory',
        		'ModalGeograficoCtrl','GeograficoFactory',
        		'ModalOrganismoCtrl','OrganismoFactory'
		),
        title: 'Reporte P01',
        ncyBreadcrumb: {
            label: 'Reporte P01'
        }
    }).state('app.reporteP02', {
        url: "/reporteP02",
        templateUrl: "assets/views/papp/reporteP02.html",
        resolve: loadSequence(
        		'ngTable','ReporteP02Ctrl','ReporteP01Factory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalInstitutoEntidadCtrl','InstitutoEntidadFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalProgramaCtrl','ProgramaFactory',
        		'ModalProyectoReporteCtrl','ProyectoReporteFactory',
        		'ModalActividadReporteCtrl','ActividadReporteFactory',
        		'ModalSubActividadReporteCtrl','SubActividadReporteFactory',
        		'ModalTareaReporteCtrl','TareaReporteFactory',
        		'ModalSubTareaReporteCtrl','SubTareaReporteFactory',
        		'ModalItemsReporteCtrl','ItemsReporteFactory',
        		'ModalSubItemsReporteCtrl','SubItemsReporteFactory',
        		'ModalFuenteFinanciamientoCtrl','FuenteFinanciamientoFactory',
        		'ModalGeograficoCtrl','GeograficoFactory',
        		'ModalOrganismoCtrl','OrganismoFactory'
		),
        title: 'Reporte P02',
        ncyBreadcrumb: {
            label: 'Reporte P02'
        }
    }).state('app.reporteP03', {
        url: "/reporteP03",
        templateUrl: "assets/views/papp/reporteP03.html",
        resolve: loadSequence(
        		'ngTable','ReporteP03Ctrl','ReporteP01Factory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalInstitutoEntidadCtrl','InstitutoEntidadFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalProgramaCtrl','ProgramaFactory',
        		'ModalProyectoReporteCtrl','ProyectoReporteFactory',
        		'ModalActividadReporteCtrl','ActividadReporteFactory',
        		'ModalSubActividadReporteCtrl','SubActividadReporteFactory',
        		'ModalTareaReporteCtrl','TareaReporteFactory',
        		'ModalSubTareaReporteCtrl','SubTareaReporteFactory',
        		'ModalItemsReporteCtrl','ItemsReporteFactory',
        		'ModalSubItemsReporteCtrl','SubItemsReporteFactory',
        		'ModalFuenteFinanciamientoCtrl','FuenteFinanciamientoFactory',
        		'ModalGeograficoCtrl','GeograficoFactory',
        		'ModalOrganismoCtrl','OrganismoFactory'
		),
        title: 'Reporte P03',
        ncyBreadcrumb: {
            label: 'Reporte P03'
        }
    }).state('app.reporteP04', {
        url: "/reporteP04",
        templateUrl: "assets/views/papp/reporteP04.html",
        resolve: loadSequence(
        		'ngTable','ReporteP04Ctrl','ReporteP01Factory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalInstitutoEntidadCtrl','InstitutoEntidadFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalProgramaCtrl','ProgramaFactory',
        		'ModalProyectoReporteCtrl','ProyectoReporteFactory',
        		'ModalActividadReporteCtrl','ActividadReporteFactory',
        		'ModalSubActividadReporteCtrl','SubActividadReporteFactory',
        		'ModalTareaReporteCtrl','TareaReporteFactory',
        		'ModalSubTareaReporteCtrl','SubTareaReporteFactory',
        		'ModalItemsReporteCtrl','ItemsReporteFactory',
        		'ModalSubItemsReporteCtrl','SubItemsReporteFactory',
        		'ModalFuenteFinanciamientoCtrl','FuenteFinanciamientoFactory',
        		'ModalGeograficoCtrl','GeograficoFactory',
        		'ModalOrganismoCtrl','OrganismoFactory'
		),
        title: 'Reporte P04',
        ncyBreadcrumb: {
            label: 'Reporte P04'
        }
    }).state('app.reporteP04pac', {
        url: "/reporteP04pac",
        templateUrl: "assets/views/papp/reporteP04pac.html",
        resolve: loadSequence(
        		'ngTable','ReporteP04pacCtrl','ReporteP01Factory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalInstitutoEntidadCtrl','InstitutoEntidadFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalProgramaCtrl','ProgramaFactory',
        		'ModalProyectoReporteCtrl','ProyectoReporteFactory',
        		'ModalActividadReporteCtrl','ActividadReporteFactory',
        		'ModalSubActividadReporteCtrl','SubActividadReporteFactory',
        		'ModalTareaReporteCtrl','TareaReporteFactory',
        		'ModalSubTareaReporteCtrl','SubTareaReporteFactory',
        		'ModalItemsReporteCtrl','ItemsReporteFactory',
        		'ModalSubItemsReporteCtrl','SubItemsReporteFactory',
        		'ModalFuenteFinanciamientoCtrl','FuenteFinanciamientoFactory',
        		'ModalGeograficoCtrl','GeograficoFactory',
        		'ModalOrganismoCtrl','OrganismoFactory'
		),
        title: 'Reporte P04 PAC',
        ncyBreadcrumb: {
            label: 'Reporte P04 PAC'
        }
    }).state('app.reporteP05', {
        url: "/reporteP05",
        templateUrl: "assets/views/papp/reporteP05.html",
        resolve: loadSequence(
        		'ngTable','ReporteP05Ctrl','ReporteP01Factory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalInstitutoEntidadCtrl','InstitutoEntidadFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalProgramaCtrl','ProgramaFactory',
        		'ModalProyectoReporteCtrl','ProyectoReporteFactory',
        		'ModalActividadReporteCtrl','ActividadReporteFactory',
        		'ModalSubActividadReporteCtrl','SubActividadReporteFactory',
        		'ModalTareaReporteCtrl','TareaReporteFactory',
        		'ModalSubTareaReporteCtrl','SubTareaReporteFactory',
        		'ModalItemsReporteCtrl','ItemsReporteFactory',
        		'ModalSubItemsReporteCtrl','SubItemsReporteFactory',
        		'ModalFuenteFinanciamientoCtrl','FuenteFinanciamientoFactory',
        		'ModalGeograficoCtrl','GeograficoFactory',
        		'ModalOrganismoCtrl','OrganismoFactory'
		),
        title: 'Reporte P05',
        ncyBreadcrumb: {
            label: 'Reporte P05'
        }
    }).state('app.reporteP06', {
        url: "/reporteP06",
        templateUrl: "assets/views/papp/reporteP06.html",
        resolve: loadSequence(
        		'ngTable','ReporteP06Ctrl','ReporteP06Factory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalInstitutoEntidadCtrl','InstitutoEntidadFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalProgramaCtrl','ProgramaFactory',
        		'ModalProyectoReporteCtrl','ProyectoReporteFactory',
        		'ModalActividadReporteCtrl','ActividadReporteFactory',
        		'ModalSubActividadReporteCtrl','SubActividadReporteFactory',
        		'ModalTareaReporteCtrl','TareaReporteFactory',
        		'ModalSubTareaReporteCtrl','SubTareaReporteFactory',
        		'ModalItemsReporteCtrl','ItemsReporteFactory',
        		'ModalSubItemsReporteCtrl','SubItemsReporteFactory',
        		'ModalFuenteFinanciamientoCtrl','FuenteFinanciamientoFactory',
        		'ModalGeograficoCtrl','GeograficoFactory',
        		'ModalOrganismoCtrl','OrganismoFactory'
		),
        title: 'Reporte P06',
        ncyBreadcrumb: {
            label: 'Reporte P06'
        }
    }).state('app.reporteS01', {
        url: "/reporteS01",
        templateUrl: "assets/views/papp/reporteS01.html",
        resolve: loadSequence(
        		'ngTable','ReporteS01Ctrl','ReporteS01Factory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalInstitutoEntidadCtrl','InstitutoEntidadFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalProgramaCtrl','ProgramaFactory',
        		'ModalProyectoReporteCtrl','ProyectoReporteFactory',
        		'ModalActividadReporteCtrl','ActividadReporteFactory',
        		'ModalSubActividadReporteCtrl','SubActividadReporteFactory',
        		'ModalTareaReporteCtrl','TareaReporteFactory',
        		'ModalSubTareaReporteCtrl','SubTareaReporteFactory',
        		'ModalItemsReporteCtrl','ItemsReporteFactory',
        		'ModalSubItemsReporteCtrl','SubItemsReporteFactory',
        		'ModalFuenteFinanciamientoCtrl','FuenteFinanciamientoFactory',
        		'ModalGeograficoCtrl','GeograficoFactory',
        		'ModalOrganismoCtrl','OrganismoFactory',
        		'ModalObjetivoOperacionalReporteCtrl','ObjetivoOperacionalReporteFactory',
        		'ModalObjetivoEstrategicoReporteCtrl','ObjetivoEstrategicoReporteFactory'
		),
        title: 'Reporte S01',
        ncyBreadcrumb: {
            label: 'Reporte S01'
        }
    }).state('app.reporteS02', {
        url: "/reporteS02",
        templateUrl: "assets/views/papp/reporteS02.html",
        resolve: loadSequence(
        		'ngTable','ReporteS02Ctrl','ReporteS01Factory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalInstitutoEntidadCtrl','InstitutoEntidadFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalProgramaCtrl','ProgramaFactory',
        		'ModalProyectoReporteCtrl','ProyectoReporteFactory',
        		'ModalActividadReporteCtrl','ActividadReporteFactory',
        		'ModalSubActividadReporteCtrl','SubActividadReporteFactory',
        		'ModalTareaReporteCtrl','TareaReporteFactory',
        		'ModalSubTareaReporteCtrl','SubTareaReporteFactory',
        		'ModalItemsReporteCtrl','ItemsReporteFactory',
        		'ModalSubItemsReporteCtrl','SubItemsReporteFactory',
        		'ModalFuenteFinanciamientoCtrl','FuenteFinanciamientoFactory',
        		'ModalGeograficoCtrl','GeograficoFactory',
        		'ModalOrganismoCtrl','OrganismoFactory',
        		'ModalObjetivoOperacionalReporteCtrl','ObjetivoOperacionalReporteFactory',
        		'ModalObjetivoEstrategicoReporteCtrl','ObjetivoEstrategicoReporteFactory'
		),
        title: 'Reporte S02',
        ncyBreadcrumb: {
            label: 'Reporte S02'
        }
    }).state('app.reporteS03', {
        url: "/reporteS03",
        templateUrl: "assets/views/papp/reporteS03.html",
        resolve: loadSequence(
        		'ngTable','ReporteS03Ctrl','ReporteS01Factory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalInstitutoEntidadCtrl','InstitutoEntidadFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalProgramaCtrl','ProgramaFactory',
        		'ModalProyectoReporteCtrl','ProyectoReporteFactory',
        		'ModalActividadReporteCtrl','ActividadReporteFactory',
        		'ModalSubActividadReporteCtrl','SubActividadReporteFactory',
        		'ModalTareaReporteCtrl','TareaReporteFactory',
        		'ModalSubTareaReporteCtrl','SubTareaReporteFactory',
        		'ModalItemsReporteCtrl','ItemsReporteFactory',
        		'ModalSubItemsReporteCtrl','SubItemsReporteFactory',
        		'ModalFuenteFinanciamientoCtrl','FuenteFinanciamientoFactory',
        		'ModalGeograficoCtrl','GeograficoFactory',
        		'ModalOrganismoCtrl','OrganismoFactory',
        		'ModalObjetivoOperacionalReporteCtrl','ObjetivoOperacionalReporteFactory',
        		'ModalObjetivoEstrategicoReporteCtrl','ObjetivoEstrategicoReporteFactory'
		),
        title: 'Reporte S03',
        ncyBreadcrumb: {
            label: 'Reporte S03'
        }
    }).state('app.reporteE04', {
        url: "/reporteE04",
        templateUrl: "assets/views/papp/reporteE04.html",
        resolve: loadSequence(
        		'ngTable','ReporteE04Ctrl','ReporteP01Factory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalInstitutoEntidadCtrl','InstitutoEntidadFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalProgramaCtrl','ProgramaFactory',
        		'ModalProyectoReporteCtrl','ProyectoReporteFactory',
        		'ModalActividadReporteCtrl','ActividadReporteFactory',
        		'ModalSubActividadReporteCtrl','SubActividadReporteFactory',
        		'ModalTareaReporteCtrl','TareaReporteFactory',
        		'ModalSubTareaReporteCtrl','SubTareaReporteFactory',
        		'ModalItemsReporteCtrl','ItemsReporteFactory',
        		'ModalSubItemsReporteCtrl','SubItemsReporteFactory',
        		'ModalFuenteFinanciamientoCtrl','FuenteFinanciamientoFactory',
        		'ModalGeograficoCtrl','GeograficoFactory',
        		'ModalOrganismoCtrl','OrganismoFactory'
		),
        title: 'Reporte E04',
        ncyBreadcrumb: {
            label: 'Reporte E04'
        }
    }).state('app.reporteE05', {
        url: "/reporteE05",
        templateUrl: "assets/views/papp/reporteE05.html",
        resolve: loadSequence(
        		'ngTable','ReporteE05Ctrl','ReporteP01Factory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalInstitutoEntidadCtrl','InstitutoEntidadFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalProgramaCtrl','ProgramaFactory',
        		'ModalProyectoReporteCtrl','ProyectoReporteFactory',
        		'ModalActividadReporteCtrl','ActividadReporteFactory',
        		'ModalSubActividadReporteCtrl','SubActividadReporteFactory',
        		'ModalTareaReporteCtrl','TareaReporteFactory',
        		'ModalSubTareaReporteCtrl','SubTareaReporteFactory',
        		'ModalItemsReporteCtrl','ItemsReporteFactory',
        		'ModalSubItemsReporteCtrl','SubItemsReporteFactory',
        		'ModalFuenteFinanciamientoCtrl','FuenteFinanciamientoFactory',
        		'ModalGeograficoCtrl','GeograficoFactory',
        		'ModalOrganismoCtrl','OrganismoFactory'
		),
        title: 'Reporte E05',
        ncyBreadcrumb: {
            label: 'Reporte E05'
        }
    }).state('app.reporteE06', {
        url: "/reporteE06",
        templateUrl: "assets/views/papp/reporteE06.html",
        resolve: loadSequence(
        		'ngTable','ReporteE06Ctrl','ReporteP01Factory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalInstitutoEntidadCtrl','InstitutoEntidadFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalProgramaCtrl','ProgramaFactory',
        		'ModalProyectoReporteCtrl','ProyectoReporteFactory',
        		'ModalActividadReporteCtrl','ActividadReporteFactory',
        		'ModalSubActividadReporteCtrl','SubActividadReporteFactory',
        		'ModalTareaReporteCtrl','TareaReporteFactory',
        		'ModalSubTareaReporteCtrl','SubTareaReporteFactory',
        		'ModalItemsReporteCtrl','ItemsReporteFactory',
        		'ModalSubItemsReporteCtrl','SubItemsReporteFactory',
        		'ModalFuenteFinanciamientoCtrl','FuenteFinanciamientoFactory',
        		'ModalGeograficoCtrl','GeograficoFactory',
        		'ModalOrganismoCtrl','OrganismoFactory'
		),
        title: 'Reporte E06 LISTADO DE REFORMAS A LA PROGRAMACIÓN ANUAL DE POLÍTICA PÚBLICA',
        ncyBreadcrumb: {
            label: 'Reporte E06 LISTADO DE REFORMAS A LA PROGRAMACIÓN ANUAL DE POLÍTICA PÚBLICA'
        }
    }).state('app.reporteE07resumen', {
        url: "/reporteE07resumen",
        templateUrl: "assets/views/papp/reporteE07resumen.html",
        resolve: loadSequence(
        		'ngTable','ReporteE07resumenCtrl','ReporteP01Factory',
        		'ModalInstitucionCtrl','InstitucionFactory',
        		'ModalInstitutoEntidadCtrl','InstitutoEntidadFactory',
        		'ModalUnidadCtrl','UnidadFactory',
        		'ModalProgramaCtrl','ProgramaFactory',
        		'ModalProyectoReporteCtrl','ProyectoReporteFactory',
        		'ModalActividadReporteCtrl','ActividadReporteFactory',
        		'ModalSubActividadReporteCtrl','SubActividadReporteFactory',
        		'ModalTareaReporteCtrl','TareaReporteFactory',
        		'ModalSubTareaReporteCtrl','SubTareaReporteFactory',
        		'ModalItemsReporteCtrl','ItemsReporteFactory',
        		'ModalSubItemsReporteCtrl','SubItemsReporteFactory',
        		'ModalFuenteFinanciamientoCtrl','FuenteFinanciamientoFactory',
        		'ModalGeograficoCtrl','GeograficoFactory',
        		'ModalOrganismoCtrl','OrganismoFactory'
		),
        title: 'Reporte E07 RESUMEN DE REFORMAS',
        ncyBreadcrumb: {
            label: 'Reporte E07 RESUMEN DE REFORMAS'
        }
    }).state('app.reporteO01', {
        url: "/reporteO01",
        templateUrl: "assets/views/papp/reporteO01.html",
        resolve: loadSequence(
        		'ngTable','ReporteO01Ctrl','ReporteP01Factory'
		),
        title: 'Reporte O01',
        ncyBreadcrumb: {
            label: 'Reporte O01'
        }
    }).state('app.reporteO02', {
        url: "/reporteO02",
        templateUrl: "assets/views/papp/reporteO02.html",
        resolve: loadSequence(
        		'ngTable','ReporteO02Ctrl','ReporteP01Factory'
		),
        title: 'Reporte O02',
        ncyBreadcrumb: {
            label: 'Reporte O02'
        }
    }).state('app.reporteO03', {
        url: "/reporteO03",
        templateUrl: "assets/views/papp/reporteO03.html",
        resolve: loadSequence(
        		'ngTable','ReporteO03Ctrl','ReporteP01Factory'
		),
        title: 'Reporte O03',
        ncyBreadcrumb: {
            label: 'Reporte O03'
        }
    }).state('app.reporteO04', {
        url: "/reporteO04",
        templateUrl: "assets/views/papp/reporteO04.html",
        resolve: loadSequence(
        		'ngTable','ReporteO04Ctrl','ReporteP01Factory'
		),
        title: 'Reporte O04',
        ncyBreadcrumb: {
            label: 'Reporte O04'
        }
    }).state('app.reporteO05', {
        url: "/reporteO05",
        templateUrl: "assets/views/papp/reporteO05.html",
        resolve: loadSequence(
        		'ngTable','ReporteO05Ctrl','ReporteP01Factory'
		),
        title: 'Reporte O05',
        ncyBreadcrumb: {
            label: 'Reporte O05'
        }
    }).state('app.layouts', {
        url: "/layouts",
        templateUrl: "assets/views/layouts.html",
        title: 'Layouts',
        ncyBreadcrumb: {
            label: 'Layouts'
        }
    }).state('app.ui', {
        url: '/ui',
        template: '<div ui-view class="fade-in-up"></div>',
        title: 'UI Elements',
        ncyBreadcrumb: {
            label: 'UI Elements'
        }
    }).state('app.ui.elements', {
        url: '/elements',
        templateUrl: "assets/views/ui_elements.html",
        title: 'Elements',
        icon: 'ti-layout-media-left-alt',
        ncyBreadcrumb: {
            label: 'Elements'
        }
    }).state('app.ui.buttons', {
        url: '/buttons',
        templateUrl: "assets/views/ui_buttons.html",
        title: 'Buttons',
        resolve: loadSequence('laddaCtrl'),
        ncyBreadcrumb: {
            label: 'Buttons'
        }
    }).state('app.ui.links', {
        url: '/links',
        templateUrl: "assets/views/ui_links.html",
        title: 'Link Effects',
        ncyBreadcrumb: {
            label: 'Link Effects'
        }
    }).state('app.ui.icons', {
        url: '/icons',
        templateUrl: "assets/views/ui_icons.html",
        title: 'Font Awesome Icons',
        ncyBreadcrumb: {
            label: 'Font Awesome Icons'
        },
        resolve: loadSequence('iconsCtrl')
    }).state('app.ui.lineicons', {
        url: '/line-icons',
        templateUrl: "assets/views/ui_line_icons.html",
        title: 'Linear Icons',
        ncyBreadcrumb: {
            label: 'Linear Icons'
        },
        resolve: loadSequence('iconsCtrl')
    }).state('app.ui.lettericons', {
        url: '/letter-icons',
        templateUrl: "assets/views/ui_letter_icons.html",
        title: 'Letter Icons',
        ncyBreadcrumb: {
            label: 'Letter Icons'
        }
    }).state('app.ui.modals', {
        url: '/modals',
        templateUrl: "assets/views/ui_modals.html",
        title: 'Modals',
        ncyBreadcrumb: {
            label: 'Modals'
        },
        resolve: loadSequence('asideCtrl')
    }).state('app.ui.toggle', {
        url: '/toggle',
        templateUrl: "assets/views/ui_toggle.html",
        title: 'Toggle',
        ncyBreadcrumb: {
            label: 'Toggle'
        }
    }).state('app.ui.tabs_accordions', {
        url: '/accordions',
        templateUrl: "assets/views/ui_tabs_accordions.html",
        title: "Tabs & Accordions",
        ncyBreadcrumb: {
            label: 'Tabs & Accordions'
        },
        resolve: loadSequence('vAccordionCtrl')
    }).state('app.ui.panels', {
        url: '/panels',
        templateUrl: "assets/views/ui_panels.html",
        title: 'Panels',
        ncyBreadcrumb: {
            label: 'Panels'
        }
    }).state('app.ui.notifications', {
        url: '/notifications',
        templateUrl: "assets/views/ui_notifications.html",
        title: 'Notifications',
        ncyBreadcrumb: {
            label: 'Notifications'
        },
        resolve: loadSequence('toasterCtrl', 'sweetAlertCtrl', 'notificationIconsCtrl', 'notifyCtrl', 'ngNotify')
    }).state('app.ui.sliders', {
        url: '/sliders',
        templateUrl: "assets/views/ui_sliders.html",
        title: 'Sliders',
        ncyBreadcrumb: {
            label: 'Sliders'
        },
        resolve: loadSequence('sliderCtrl')
    }).state('app.ui.treeview', {
        url: '/treeview',
        templateUrl: "assets/views/ui_tree.html",
        title: 'TreeView',
        ncyBreadcrumb: {
            label: 'Treeview'
        },
        resolve: loadSequence('angularBootstrapNavTree', 'treeCtrl')
    }).state('app.ui.knob', {
        url: '/knob',
        templateUrl: "assets/views/ui_knob.html",
        title: 'Knob component',
        ncyBreadcrumb: {
            label: 'Knob component'
        },
        resolve: loadSequence('d3', 'ui.knob', 'knobCtrl')
    }).state('app.ui.media', {
        url: '/media',
        templateUrl: "assets/views/ui_media.html",
        title: 'Media',
        ncyBreadcrumb: {
            label: 'Media'
        }
    }).state('app.ui.nestable', {
        url: '/nestable2',
        templateUrl: "assets/views/ui_nestable.html",
        title: 'Nestable List',
        ncyBreadcrumb: {
            label: 'Nestable List'
        },
        resolve: loadSequence('jquery-nestable-plugin', 'ng-nestable', 'nestableCtrl')
    }).state('app.ui.typography', {
        url: '/typography',
        templateUrl: "assets/views/ui_typography.html",
        title: 'Typography',
        ncyBreadcrumb: {
            label: 'Typography'
        }
    }).state('app.table', {
        url: '/table',
        template: '<div ui-view class="fade-in-up"></div>',
        title: 'Tables',
        ncyBreadcrumb: {
            label: 'Tables'
        }
    }).state('app.table.basic', {
        url: '/basic',
        templateUrl: "assets/views/table_basic.html",
        title: 'Basic Tables',
        ncyBreadcrumb: {
            label: 'Basic'
        }
    }).state('app.table.responsive', {
        url: '/responsive',
        templateUrl: "assets/views/table_responsive.html",
        title: 'Responsive Tables',
        ncyBreadcrumb: {
            label: 'Responsive'
        }
    }).state('app.table.dynamic', {
        url: '/dynamic',
        templateUrl: "assets/views/table_dynamic.html",
        title: 'Dynamic Tables',
        ncyBreadcrumb: {
            label: 'Dynamic'
        },
        resolve: loadSequence('dynamicTableCtrl')
    }).state('app.table.data', {
        url: '/data',
        templateUrl: "assets/views/table_data.html",
        title: 'ngTable',
        ncyBreadcrumb: {
            label: 'ngTable'
        },
        resolve: loadSequence('ngTable', 'ngTableCtrl')
    }).state('app.table.export', {
        url: '/export',
        templateUrl: "assets/views/table_export.html",
        title: 'Table'
    }).state('app.form', {
        url: '/form',
        template: '<div ui-view class="fade-in-up"></div>',
        title: 'Forms',
        ncyBreadcrumb: {
            label: 'Forms'
        }
    }).state('app.form.elements', {
        url: '/elements',
        templateUrl: "assets/views/form_elements.html",
        title: 'Forms Elements',
        ncyBreadcrumb: {
            label: 'Elements'
        },
        resolve: loadSequence('monospaced.elastic', 'ui.mask', 'touchspin-plugin', 'selectCtrl')
    }).state('app.form.pickers', {
        url: '/pickers',
        templateUrl: "assets/views/form_pickers.html",
        title: 'Pickers',
        ncyBreadcrumb: {
            label: 'Pickers'
        },
        resolve: loadSequence('dateRangeCtrl', 'spectrum-plugin', 'angularSpectrumColorpicker')
    }).state('app.form.xeditable', {
        url: '/xeditable',
        templateUrl: "assets/views/form_xeditable.html",
        title: 'Angular X-Editable',
        ncyBreadcrumb: {
            label: 'X-Editable'
        },
        resolve: loadSequence('xeditable', 'checklist-model', 'xeditableCtrl')
    }).state('app.form.texteditor', {
        url: '/editor',
        templateUrl: "assets/views/form_text_editor.html",
        title: 'Text Editor',
        ncyBreadcrumb: {
            label: 'Text Editor'
        },
        resolve: loadSequence('ckeditor-plugin', 'ckeditor', 'ckeditorCtrl')
    }).state('app.form.wizard', {
        url: '/wizard',
        templateUrl: "assets/views/form_wizard.html",
        title: 'Form Wizard',
        ncyBreadcrumb: {
            label: 'Wizard'
        },
        resolve: loadSequence('wizardCtrl', 'ngNotify')
    }).state('app.form.validation', {
        url: '/validation',
        templateUrl: "assets/views/form_validation.html",
        title: 'Form Validation',
        ncyBreadcrumb: {
            label: 'Validation'
        },
        resolve: loadSequence('validationCtrl')
    }).state('app.form.cropping', {
        url: '/image-cropping',
        templateUrl: "assets/views/form_image_cropping.html",
        title: 'Image Cropping',
        ncyBreadcrumb: {
            label: 'Image Cropping'
        },
        resolve: loadSequence('ngImgCrop', 'cropCtrl', 'jcrop-plugin', 'crop2Ctrl')
    }).state('app.form.upload', {
        url: '/file-upload',
        templateUrl: "assets/views/form_file_upload.html",
        title: 'Multiple File Upload',
        ncyBreadcrumb: {
            label: 'File Upload'
        },
        resolve: loadSequence('angularFileUpload', 'uploadCtrl')
    }).state('app.pages', {
        url: '/pages',
        template: '<div ui-view class="fade-in-up"></div>',
        title: 'Pages',
        ncyBreadcrumb: {
            label: 'Pages'
        }
    }).state('app.pages.user', {
        url: '/user',
        templateUrl: "assets/views/pages_user_profile.html",
        title: 'User Profile',
        ncyBreadcrumb: {
            label: 'User Profile'
        },
        resolve: loadSequence('flow', 'userCtrl')
    }).state('app.pages.invoice', {
        url: '/invoice',
        templateUrl: "assets/views/pages_invoice.html",
        title: 'Invoice',
        ncyBreadcrumb: {
            label: 'Invoice'
        }
    }).state('app.pages.timeline', {
        url: '/timeline',
        templateUrl: "assets/views/pages_timeline.html",
        title: 'Timeline',
        ncyBreadcrumb: {
            label: 'Timeline'
        },
        resolve: loadSequence('ngMap')
    }).state('app.pages.calendar', {
        url: '/calendar',
        templateUrl: "assets/views/pages_calendar.html",
        title: 'Calendar',
        ncyBreadcrumb: {
            label: 'Calendar'
        },
        resolve: loadSequence('mwl.calendar', 'calendarCtrl')
    }).state('app.pages.messages', {
        url: '/messages',
        templateUrl: "assets/views/pages_messages.html",
        resolve: loadSequence('inboxCtrl')
    }).state('app.pages.messages.inbox', {
        url: '/inbox/:inboxID',
        templateUrl: "assets/views/pages_inbox.html",
        controller: 'ViewMessageCrtl'
    }).state('app.pages.blank', {
        url: '/blank',
        templateUrl: "assets/views/pages_blank_page.html",
        ncyBreadcrumb: {
            label: 'Starter Page'
        }
    }).state('app.utilities', {
        url: '/utilities',
        template: '<div ui-view class="fade-in-up"></div>',
        title: 'Utilities',
        ncyBreadcrumb: {
            label: 'Utilities'
        }
    }).state('app.utilities.search', {
        url: '/search',
        templateUrl: "assets/views/utility_search_result.html",
        title: 'Search Results',
        ncyBreadcrumb: {
            label: 'Search Results'
        }
    }).state('app.utilities.pricing', {
        url: '/pricing',
        templateUrl: "assets/views/utility_pricing_table.html",
        title: 'Pricing Table',
        ncyBreadcrumb: {
            label: 'Pricing Table'
        }
    }).state('app.maps', {
        url: "/maps",
        templateUrl: "assets/views/maps.html",
        resolve: loadSequence('ngMap', 'mapsCtrl'),
        title: "Maps",
        ncyBreadcrumb: {
            label: 'Maps'
        }
    }).state('app.charts', {
        url: "/charts",
        templateUrl: "assets/views/charts.html",
        resolve: loadSequence('chartjs', 'chart.js', 'chartsCtrl'),
        title: "Charts",
        ncyBreadcrumb: {
            label: 'Charts'
        }
    }).state('error', {
        url: '/error',
        template: '<div ui-view class="fade-in-up"></div>'
    }).state('error.404', {
        url: '/404',
        templateUrl: "assets/views/utility_404.html",
    }).state('error.500', {
        url: '/500',
        templateUrl: "assets/views/utility_500.html",
    })

	// Login routes

	.state('login', {
	    url: '/login',
	    template: '<div ui-view class="fade-in-right-big smooth"></div>',
	    abstract: true
	}).state('login.signin', {
	    url: '/signin',
	    templateUrl: "assets/views/login_login.html"
	}).state('login.forgot', {
	    url: '/forgot',
	    templateUrl: "assets/views/login_forgot.html"
	}).state('login.registration', {
	    url: '/registration',
	    templateUrl: "assets/views/login_registration.html"
	}).state('login.lockscreen', {
	    url: '/lock',
	    templateUrl: "assets/views/login_lock_screen.html"
	})

	// Landing Page route
	.state('landing', {
	    url: '/landing-page',
	    template: '<div ui-view class="fade-in-right-big smooth"></div>',
	    abstract: true,
	    resolve: loadSequence('jquery-appear-plugin', 'ngAppear', 'countTo')
	}).state('landing.welcome', {
	    url: '/welcome',
	    templateUrl: "assets/views/landing_page.html"
	});
    // Generates a resolve object previously configured in constant.JS_REQUIRES (config.constant.js)
    function loadSequence() {
        var _args = arguments;
        return {
            deps: ['$ocLazyLoad', '$q',
			function ($ocLL, $q) {
			    var promise = $q.when(1);
			    for (var i = 0, len = _args.length; i < len; i++) {
			        promise = promiseThen(_args[i]);
			    }
			    return promise;

			    function promiseThen(_arg) {
			        if (typeof _arg == 'function')
			            return promise.then(_arg);
			        else
			            return promise.then(function () {
			                var nowLoad = requiredData(_arg);
			                if (!nowLoad)
			                    return $.error('Route resolve: Bad resource name [' + _arg + ']');
			                return $ocLL.load(nowLoad);
			            });
			    }

			    function requiredData(name) {
			        if (jsRequires.modules)
			            for (var m in jsRequires.modules)
			                if (jsRequires.modules[m].name && jsRequires.modules[m].name === name)
			                    return jsRequires.modules[m];
			        return jsRequires.scripts && jsRequires.scripts[name];
			    }
			}]
        };
    }
}]);

String.prototype.extractNumber = function () {
    return Number(this.replace(/(?!-)[^0-9.]/g, ""));
};

app.directive('decimal', [function () {
    return {
        require: '?ngModel',
        link: function (scope, elem, attrs, ctrl) {
            if (!ctrl) return;

            ctrl.$formatters.unshift(function (a) {
            	if (undefined === a) {
            		return "";
            	}
            	var plainNumber;
            	try {
            		plainNumber = Number(a.replace(/(?!-)[^0-9.]/g, ""));
            	} catch (e) {
            		return "ERROR";
            	}
                return plainNumber.toFixed(attrs.decimal);
            });

            elem.bind('blur', function(event) {
            	var plainNumber;
            	var fuente = elem.val().replace(",", ".");
            	try {
            		plainNumber = Number(fuente.replace(/(?!-)[^0-9.]/g, ""));
            	} catch (e) {
            		elem.value = "ERROR";
            		return;
            	}
            	elem.val(plainNumber.toFixed(attrs.decimal).replace(",", "."));
            });
        }
    };
}]);

app.directive('format', ['$filter', function ($filter) {
    return {
        require: '?ngModel',
        link: function (scope, elem, attrs, ctrl) {
            if (!ctrl) return;

            ctrl.$formatters.unshift(function (a) {
                return $filter(attrs.format)(ctrl.$modelValue)
            });

            elem.bind('blur', function(event) {
                var plainNumber = elem.val().replace(/[^\d|\-+|\.+]/g, '');
                elem.val($filter(attrs.format)(plainNumber));
            });
        }
    };
}]);

app.directive('numericOnly', function(){
    return {
        require: 'ngModel',
        link: function(scope, element, attrs, modelCtrl) {

            var regex = /^[1-9]\d*(((.\d{3}){1})?(\,\d{0,2})?)$/;
            modelCtrl.$parsers.push(function (inputValue) {

                var transformedInput = inputValue;
                if (regex.test(transformedInput)) {
                    //console.log('passed the expression...');
                    modelCtrl.$setViewValue(transformedInput);
                    modelCtrl.$render();
                    return transformedInput;
                } else {
                    //console.log('did not pass the expression...');
                    transformedInput = transformedInput.substr(0, transformedInput.length-1);
                    modelCtrl.$setViewValue(transformedInput);
                    modelCtrl.$render();
                    return transformedInput;
                }
            });
        }
    };
});

app.directive('mayusculas', function(){
    return {
        require: 'ngModel',
        link: function(scope, element, attrs, modelCtrl) {
            modelCtrl.$parsers.push(function (inputValue) {
            	return inputValue.toUpperCase();
            });
        }
    };
});

angular.module('numfmt-error-module', [])

.run(function($rootScope) {
  $rootScope.typeOf = function(value) {
    return typeof value;
  };
})

.directive('stringToNumber', function() {
  return {
    require: 'ngModel',
    link: function(scope, element, attrs, ngModel) {
      ngModel.$parsers.push(function(value) {
    	console.log('stringToNumber.$parsers: ' + value);
        return '' + value;
      });
      ngModel.$formatters.push(function(value) {
      	console.log('stringToNumber.$formatters: ' + value);
        return parseFloat(value, 10);
      });
    }
  };
});
