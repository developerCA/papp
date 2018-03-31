'use strict';

app.controller('RegistroMetasController', [ "$scope","$rootScope","$location","$uibModal","SweetAlert","$filter", "ngTableParams","RegistroMetasFactory",
	function($scope,$rootScope,$location,$uibModal,SweetAlert,$filter, ngTableParams,RegistroMetasFactory) {

	$scope.objeto={
		institucionid: null,
		npinstitucion: null,
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
		                
		            	RegistroMetasFactory.guardar($scope.objeto).then(function(resp){
	            			console.log(resp);
		            		if (resp.estado){
		        				 SweetAlert.swal("Registro Metas!", "Registro registrado satisfactoriamente!", "success");
		        				 $location.path("/index");
		        			 }else{
			 		             SweetAlert.swal("Registro Metas!", resp.mensajes.msg, "error");
		        				 
		        			 }
		        			
		        		})
		        		
		            }

		        }
    };
} ]);