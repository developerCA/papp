'use strict';

app.controller('MatrizProgramacionAnualPoliticaPublicaController', [ "$scope","$rootScope","$location","$uibModal","SweetAlert","$filter", "ngTableParams","MatrizProgramacionAnualPoliticaPublicaFactory",
	function($scope,$rootScope,$location,$uibModal,SweetAlert,$filter, ngTableParams,MatrizProgramacionAnualPoliticaPublicaFactory) {

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

	$scope.abrirInstitucion = function() {
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
			//console.log(obj);
			$scope.objeto.institucionid = obj.id;
			$scope.objeto.npinstitucion = obj.codigo + ' - ' + obj.nombre;
		}, function() {
		});
	};

	$scope.abrirUnidad = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalUnidad.html',
			controller : 'ModalUnidadController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.unidadid = obj.id;
			$scope.objeto.npunidad = obj.codigopresup + ' - ' + obj.nombre;
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
			//console.log(obj);
			$scope.objeto.programaid = obj.id;
			$scope.objeto.npprograma = obj.codigo + ' - ' + obj.nombre;
		}, function() {
		});
	};

	$scope.abrirProyecto = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalProyecto.html',
			controller : 'ModalProyectoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.proyectoid = obj.id;
			$scope.objeto.npproyecto = obj.codigo + ' - ' + obj.nombre;
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
		                
		            	MatrizProgramacionAnualPoliticaPublicaFactory.guardar($scope.objeto).then(function(resp){
	            			console.log(resp);
		            		if (resp.estado){
		        				 SweetAlert.swal("Matriz Programación Anual de la Política Pública!", "Registro registrado satisfactoriamente!", "success");
		        				 $location.path("/index");
		        			 }else{
			 		             SweetAlert.swal("Matriz Programación Anual de la Política Pública!", resp.mensajes.msg, "error");
		        				 
		        			 }
		        			
		        		})
		        		
		            }

		        }
    };
} ]);