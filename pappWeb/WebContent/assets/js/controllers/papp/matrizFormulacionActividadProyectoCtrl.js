'use strict';

app.controller('MatrizFormulacionActividadProyectoController', [ "$scope","$rootScope","$location","$uibModal","SweetAlert","$filter", "ngTableParams","MatrizFormulacionActividadProyectoFactory",
	function($scope,$rootScope,$location,$uibModal,SweetAlert,$filter, ngTableParams,MatrizFormulacionActividadProyectoFactory) {

	$scope.objeto={
		institucionid: null,
		npinstitucion: null,
		unidadid: null,
		npunidad: null,
		programaid: null,
		npprograma: null,
		mesdesde: "1",
		meshasta: "12",
		tipometa: "P",
		reporte: 'PDF'
	};

	$scope.abrirUnidad = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalUnidad.html',
			controller : 'ModalPlanificacionUEController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.unidadid = obj.id;
			$scope.objeto.npunidad = obj.codigopresup + ' - ' + obj.nombre;
		}, function() {
		});
	};

	$scope.abrirActividad = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalActividad.html',
			controller : 'ModalActividadController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.actividadid = obj.id;
			$scope.objeto.npactividad = obj.codigo + ' - ' + obj.nombre;
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
		                
		            	MatrizFormulacionActividadProyectoFactory.guardar($scope.objeto).then(function(resp){
	            			console.log(resp);
		            		if (resp.estado){
		        				 SweetAlert.swal("Matriz de Formulacion de Actividad sin Proyecto!", "Registro registrado satisfactoriamente!", "success");
		        				 $location.path("/index");
		        			 }else{
			 		             SweetAlert.swal("Matriz de Formulacion de Actividad sin Proyecto!", resp.mensajes.msg, "error");
		        				 
		        			 }
		        			
		        		})
		        		
		            }

		        }
    };
} ]);