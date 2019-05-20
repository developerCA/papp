'use strict';

app.controller('ReporteS01Controller', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","reporteS01Factory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, reporteS01Factory) {

	$scope.objeto = {};
	$scope.institucioncodigo = null;

	$scope.limpiarListas = function(pos) {
		$scope.objeto.programaid = null;
		$scope.npprograma = '';
		$scope.programaActivo = false;

		if (pos == 3) return;

		$scope.objeto.unidadid = null;
		$scope.npunidad = '';
		$scope.unidadActivo = false;

		if (pos == 2) return;

		$scope.objeto.entidadid = null;
		$scope.npentidad = '';
		$scope.entidadActivo = false;
	}

	$scope.institucionActivo = false;
	$scope.abrirInstitucion = function() {
		//console.log($rootScope.ejefiscal);
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalInstitucion.html',
			controller : 'ModalInstitucionController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.institucionid = obj.id;
			$scope.npinstitucion = obj.codigo + " - " + obj.nombre;
			$scope.institucioncodigo = obj.codigo;
			$scope.institucionnombre = obj.nombre;
			$scope.institucionActivo = true;
			$scope.limpiarListas(1);
		}, function() {
		});
	};

	$scope.entidadActivo = false;
	$scope.abrirEntidad = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalInstitutoEntidad.html',
			controller : 'ModalInstitutoEntidadController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				},
				institucioncodigo : function() {
					return $scope.institucioncodigo;
				} 
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.entidadid = obj.institucionentid;
			$scope.npentidad = obj.codigo + ' - ' + obj.nombre;
			$scope.entidadActivo = true;
			$scope.limpiarListas(2);
		}, function() {
		});
	};

	$scope.unidadActivo = false;
	$scope.abrirUnidad = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalUnidadArbol.html',
			controller : 'ModalUnidadArbolController',
			size : 'lg',
			resolve : {
				instituicionFuente : function() {
					return null; //$scope.objeto.institucionid;
				},
				institucionentidad : function() {
					return $scope.objeto.entidadid;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.unidadid = obj.id;
			$scope.npunidad = obj.codigoorganico + ' - ' + obj.nombre;
			$scope.unidadActivo = true;
			$scope.limpiarListas(3);
		}, function() {
		});
	};

	$scope.programaActivo = false;
	$scope.abrirPrograma = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalPrograma.html',
			controller : 'ModalProgramaController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.programaid = obj.id;
			$scope.npprograma = obj.codigo + ' - ' + obj.nombre;
			$scope.programaActivo=true;
		}, function() {
		});
	};

	$scope.proyectoActivo = false;
	$scope.abrirProyecto = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalProyectoReporte.html',
			controller : 'ModalProyectoReporteController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				},
				npunidad : function() {
					return $scope.objeto.unidadid;
				},
				npprogramaid : function() {
					return $scope.objeto.programaid;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.proyectoid = obj.id;
			$scope.npproyecto = obj.codigo + ' - ' + obj.nombre;
			$scope.proyectoActivo = true;
		}, function() {
		});
	};

	$scope.actividadActivo = false;
	$scope.abrirActividad = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalActividadReporte.html',
			controller : 'ModalActividadReporteController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				},
				npunidad : function() {
					return $scope.objeto.unidadid;
				},
				npprogramaid : function() {
					return $scope.objeto.programaid;
				},
				npproyectoid : function() {
					return $scope.objeto.programaid;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.actividadid = obj.id;
			$scope.objeto.npactividad = obj.codigo + ' - ' + obj.nombre;
			$scope.actividadActivo = true;
		}, function() {
		});
	};

	$scope.subactividadActivo = false;
	$scope.abrirSubActividad = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalSubActividadReporte.html',
			controller : 'ModalSubActividadReporteController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				},
				npunidad : function() {
					return $scope.objeto.unidadid;
				},
				nivelactividadpadreid : function() {
					return $scope.objeto.actividadid;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.subactividadid = obj.id;
			$scope.objeto.npsubactividad = obj.codigo + ' - ' + obj.nombre;
			$scope.subactividadActivo = true;
		}, function() {
		});
	};

	$scope.tareaActivo = false;
	$scope.abrirTarea = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalTareaReporte.html',
			controller : 'ModalTareaReporteController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				},
				npunidad : function() {
					return $scope.objeto.unidadid;
				},
				nivelactividadpadreid : function() {
					return $scope.objeto.actividadid;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.tareaunidadid = obj.id;
			$scope.objeto.nptarea = obj.codigo + ' - ' + obj.nombre;
			$scope.tareaActivo = true;
		}, function() {
		});
	};

	$scope.subtareaActivo = false;
	$scope.abrirSubtarea = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalSubTareaReporte.html',
			controller : 'ModalSubTareaReporteController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				},
				npunidad : function() {
					return $scope.objeto.unidadid;
				},
				nivelactividadpadreid : function() {
					return $scope.objeto.actividadid;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.subtareaid = obj.id;
			$scope.objeto.npsubtarea = obj.codigo + ' - ' + obj.nombre;
			$scope.subtareaActivo = true;
		}, function() {
		});
	};

	$scope.itemsActivo = false;
    $scope.abrirItems=function(){
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalItemsReporte.html',
			controller : 'ModalItemsReporteController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				},
				npunidad : function() {
					return $scope.objeto.unidadid;
				},
				nivelactividadpadreid : function() {
					return $scope.objeto.actividadid;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.itemid = obj.id;
			$scope.objeto.npitem = obj.codigo + ' - ' + obj.nombre;
			$scope.itemsActivo = true;
		}, function() {
		});
	};

	$scope.subitemsActivo = false;
	$scope.abrirSubItems = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalSubItemsReporte.html',
			controller : 'ModalSubItemsReporteController',
			size : 'lg',
			resolve: {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				},
				npunidad : function() {
					return $scope.objeto.unidadid;
				},
				nivelactividadpadreid : function() {
					return $scope.objeto.actividadid;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.subitemid = obj.id;
			$scope.npsubitem = obj.codigo + ' - ' + obj.nombre;
			$scope.subitemsActivo = true;
		}, function() {
		});
	};

    $scope.abrirFuenteFinanciamiento=function(){
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalFuenteFinanciamiento.html',
			controller : 'ModalFuenteFinanciamientoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.fuentefinanciamientoid = obj.id;
			$scope.npfuentefinanciamiento = obj.codigo + ' - ' + obj.nombre;			
		}, function() {
		});
	};

	$scope.form = {
        submit: function (form) {
//        	if (!$scope.objeto.actividadid) {
//        		SweetAlert.swal("Reporte P1!", "Seleccione una Actividad", "error");
//        		return;
//        	}
//        	if (!$scope.objeto.institucionid) {
//        		SweetAlert.swal("Reporte P1!", "Seleccione una Institucion", "error");
//        		return;
//        	}
        	var url = "/pappWeb/rest/reportes/consultar/s01/ejerciciofiscal=" + $rootScope.ejefiscal;
        	if ($scope.objeto.institucionid != undefined && $scope.objeto.institucionid != null) url += "&institucionid=" + $scope.objeto.institucionid;
        	if ($scope.objeto.entidadid != undefined && $scope.objeto.entidadid != null) url += "&institucionentid=" + $scope.objeto.entidadid;
        	if ($scope.objeto.unidadid != undefined && $scope.objeto.unidadid != null) url += "&unidadid=" + $scope.objeto.unidadid;
        	if ($scope.objeto.programaid != undefined && $scope.objeto.programaid != null) url += "&programaid=" + $scope.objeto.programaid;
        	if ($scope.objeto.proyectoid != undefined && $scope.objeto.proyectoid != null) url += "&proyectoid=" + $scope.objeto.proyectoid;
        	if ($scope.objeto.actividadid != undefined && $scope.objeto.actividadid != null) url += "&actividadid=" + $scope.objeto.actividadid;
        	if ($scope.objeto.subactividadid != undefined && $scope.objeto.subactividadid != null) url += "&subactividadid=" + $scope.objeto.subactividadid;
        	if ($scope.objeto.tareaunidadid != undefined && $scope.objeto.tareaunidadid != null) url += "&tareaunidadid=" + $scope.objeto.tareaunidadid;
        	if ($scope.objeto.itemid != undefined && $scope.objeto.itemid != null) url += "&itemid=" + $scope.objeto.itemid;
        	if ($scope.objeto.subitemid != undefined && $scope.objeto.subitemid != null) url += "&subitemid=" + $scope.objeto.subitemid;
        	if ($scope.objeto.fuentefinanciamientoid != undefined && $scope.objeto.fuentefinanciamientoid != null) url += "&fuentefinanciamientoid=" + $scope.objeto.fuentefinanciamientoid;
			console.log(url);
		    window.open(url); //, '_blank');
        },
        reset: function (form) {
            $scope.myModel = angular.copy($scope.master);
            form.$setPristine(true);
            $scope.edicion=false;
			$scope.divVista=false;
            $scope.objeto={};
        }
    };

	function toStringDate(fuente) {
		if (fuente == null) {
			return null;
		}
		try {
			var parts = fuente.toISOString();
			parts = parts.split('T');
			parts = parts[0].split('-');
		} catch (err) {
			return null;
		}
		return parts[2] + "/" + parts[1] + "/" + parts[0]; 
	}

	$scope.popupnpFechainicio = {
	    opened: false
	};
	$scope.opennpFechainicio = function() {
	    $scope.popupnpFechainicio.opened = true;
	}
	$scope.popupnpFechafin = {
	    opened: false
	};
	$scope.opennpFechafin = function() {
	    $scope.popupnpFechafin.opened = true;
	}
} ]);
