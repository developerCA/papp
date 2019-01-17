'use strict';

app.controller('ReporteS01Controller', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","reporteS01Factory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, reporteS01Factory) {

//	/planificacion/consultar/objetivo/objetivoejeerciciofisca={ejerciciofiscal}&estado=A&tipo=O
//	/planificacion/consultar/objetivo/objetivoejeerciciofisca={ejerciciofiscal}&id={idpadre}&estado=A&tipo=E

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

	$scope.objetivoOperacionalActivo = false;
	$scope.abrirObjetivoOperacional = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalObjetivoOperacionalReporte.html',
			controller : 'ModalObjetivoOperacionalReporteController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				},
				padre : function() {
					return $scope.objeto.objetivoestrategicoid;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.objetivooperacionalid = obj.id;
			$scope.npobjetivooperacional = obj.codigo + ' - ' + obj.npdescripcion;
			$scope.objetivoOperacionalActivo = true;
		}, function() {
		});
	};

	$scope.objetivoEstrategicoActivo = false;
	$scope.abrirObjetivoEstrategico = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalObjetivoEstrategicoReporte.html',
			controller : 'ModalObjetivoEstrategicoReporteController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				},
				padre : function() {
					return null;
					//return $scope.objeto.objetivooperacionalid;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.objetivoestrategicoid = obj.id;
			$scope.npobjetivoestrategico = obj.codigo + ' - ' + obj.nombre;
		}, function() {
		});
	};

	$scope.form = {
        submit: function (form) {
        	if (!$scope.objeto.actividadid) {
        		SweetAlert.swal("Reporte S01!", "Seleccione una Actividad", "error");
        		return;
        	}
        	if (!$scope.objeto.institucionid) {
        		SweetAlert.swal("Reporte S01!", "Seleccione una Institucion", "error");
        		return;
        	}
        	if (!$scope.objeto.organismoid) {
        		SweetAlert.swal("Reporte S01!", "Seleccione un Organismo", "error");
        		return;
        	}
        	var url = "/birt/frameset?__report=s01.rptdesign" +
				"&NivelActividad=" + $scope.objeto.actividadid +
				"&InstitucionActividad=" + $scope.objeto.institucionid +
				"&Organismo=" + $scope.objeto.organismoid +
				"&ejerciciofiscalid=" + $rootScope.ejefiscal;
			console.log(url);
		    console.log($scope.objeto);
		    window.open(url, '_blank');
        	return;
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
