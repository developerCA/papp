'use strict';

app.controller('MatrizFormulacionActividadProyectoController', [ "$scope","$rootScope","$location","$uibModal","SweetAlert","$filter", "ngTableParams","MatrizFormulacionActividadProyectoFactory",
	function($scope,$rootScope,$location,$uibModal,SweetAlert,$filter, ngTableParams,MatrizFormulacionActividadProyectoFactory) {

	$scope.objeto = {
		actividadid: null,
		unidadid: null,
		ejerciciofiscalid: $rootScope.ejefiscal,
		tipo: "P",
		nivelactividadid: null,
		reporte: 'PDF'
	};
	$scope.unidadnombre = null;
	$scope.actividadnombre = null;
	$scope.unidadidToActividad = null;

	$scope.abrirActividad = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalActividad.html',
			controller : 'ModalActividadController',
			size : 'lg',
			resolve : {
				unidadId : function() {
					return $scope.unidadidToActividad;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.nivelactividadid = obj.id; //.npactividadid;
			$scope.objeto.actividadid = obj.tablarelacionid;
			$scope.actividadnombre = obj.descripcionexten;
		}, function() {
		});
	};

	$scope.abrirUnidad = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalUnidadCorto.html',
			controller : 'ModalUnidadCortoController',
			size : 'lg',
			resolve : {
				estadoaprobado : function() {
					return null;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj.id);
			$scope.objeto.unidadid = obj.id;//npIdunidad;
			$scope.unidadidToActividad = obj.id;
			$scope.unidadnombre = obj.codigopresup + ' - ' + obj.nombre;
		}, function() {
		});
/*
		var modalInstance = $uibModal.open({
			templateUrl : 'modalUnidad.html',
			controller : 'ModalPlanificacionUEController',
			size : 'lg',
			resolve : {
				objFuente : function() {
					return {};
				}
			}
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.unidadid = obj.id;
			$scope.objeto.npunidad = obj.codigopresup + ' - ' + obj.nombre;
		}, function() {
		});
*/
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
	        	$scope.objeto.ejerciciofiscalid = $rootScope.ejefiscal;
	        	var url = "/birt/frameset?__report=p06.rptdesign" +
	        			"&actividadid=" + $scope.objeto.actividadid +
	        			"&unidadid=" + $scope.objeto.unidadid +
	        			"&ejerciciofiscalid=" + $scope.objeto.ejerciciofiscalid +
	        			"&tipo=" + $scope.objeto.tipo +
	        			"&nivelactividadid=" + $scope.objeto.nivelactividadid;
	        	console.log(url);
	            console.log($scope.objeto);
	            window.open(url, '_blank');
	            //$location.path(url);
/*
	        	MatrizFormulacionActividadProyectoFactory.guardar($scope.objeto).then(function(resp){
	    			console.log(resp);
	        		if (resp.estado){
	    				 SweetAlert.swal("Matriz de Formulacion de Actividad sin Proyecto!", "Registro registrado satisfactoriamente!", "success");
	    				 //$location.path("/index");
	    			 }else{
	 		             SweetAlert.swal("Matriz de Formulacion de Actividad sin Proyecto!", resp.mensajes.msg, "error");
	    				 
	    			 }
	    			
	    		})
*/
	        }
	
	    }
    };
} ]);