'use strict';

app.controller('ReporteS01Controller', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","reporteS01Factory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, reporteS01Factory) {

	$scope.objeto = {};
	$scope.institucioncodigo = null;

	$scope.abrirInstitucion = function() {
		console.log($rootScope.ejefiscal);
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
		}, function() {
		});
	};

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
		}, function() {
		});
	};

	$scope.abrirUnidad = function() {
		if ($scope.institucioncodigo == null) {
			return;
		}

		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalUnidadArbol.html',
			controller : 'ModalUnidadArbolController',
			size : 'lg',
			resolve : {
				instituicionFuente : function() {
					return $scope.objeto.institucionid; //$scope.institucioncodigo;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.unidadid = obj.id;
			$scope.npunidad = obj.codigopresup + ' - ' + obj.nombre;
		}, function() {
		});
	};

	$scope.abrirPrograma = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalPrograma.html',
			controller : 'ModalProgramaController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.programaid = obj.id;
			$scope.npprograma = obj.codigo + ' - ' + obj.nombre;
		}, function() {
		});
	};

	$scope.abrirProyecto = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalProyecto.html',
			controller : 'ModalProyectoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.proyectoid = obj.id;
			$scope.npproyecto = obj.codigo + ' - ' + obj.nombre;
		}, function() {
		});
	};

	$scope.abrirActividad = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalActividad.html',
			controller : 'ModalActividadController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.actividadid = obj.id;
			$scope.objeto.npactividad = obj.codigo + ' - ' + obj.nombre;
		}, function() {
		});
	};

    $scope.abrirItem=function(){
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalItems.html',
			controller : 'ModalItemController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.itemid = obj.id;
			$scope.objeto.npitem = obj.codigo + ' - ' + obj.nombre;			
		}, function() {
		});
	};

	$scope.abrirSubItem = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalSubitem.html',
			controller : 'ModalSubItemController',
			size : 'lg',
			resolve: {
				npitemid : function() {
					return $scope.objeto.npitemid;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.subitemid = obj.id;
			$scope.npsubitem = obj.codigo + ' - ' + obj.nombre;
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
            var firstError = null;
            if (form.$invalid) {
                var field = null, firstError = null;
                for (field in form) {
                    if (field[0] != '$') {
                        if (firstError === null && !form[field].$valid) {
                            firstError = form[field].$name;
                        }
                        if (form[field].$pristine) {
                            form[field].$dirty = true;
                        }
                    }
                }
                angular.element('.ng-invalid[name=' + firstError + ']').focus();
                return;
            } else {
            	reporteS01Factory.guardar($scope.objeto).then(function(resp){
        			 if (resp.estado){
      					 //SweetAlert.swal("Reporte S01!", "Registro guardado satisfactoriamente!", "success");
        				 alert("ok");
        			 }else{
	 		             SweetAlert.swal("Reporte S01!", resp.mensajes.msg, "error");
        			 }
        		})
            }
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
