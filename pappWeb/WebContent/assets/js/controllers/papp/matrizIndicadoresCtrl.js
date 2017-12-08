'use strict';

app.controller('MatrizIndicadoresController', [ "$scope","$rootScope","$location","$uibModal","SweetAlert","$filter", "ngTableParams","MatrizIndicadoresFactory",
	function($scope,$rootScope,$location,$uibModal,SweetAlert,$filter, ngTableParams,MatrizIndicadoresFactory) {

	$scope.objeto={reporte: 'PDF'};

	$scope.abrirInstitucion = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalInstitucion.html',
			controller : 'ModalInstitucionController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.eorganicainstitucionid = obj.id;
			$scope.objeto.npinstitucion = obj.codigo + ' - ' + obj.nombre;
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
		                
		            	MatrizIndicadoresFactory.guardar($scope.objeto).then(function(resp){
	            			console.log(resp);
		            		if (resp.estado){
		        				 SweetAlert.swal("Planificaci&oacute;n Estrat&eacute;gica!", "Registro registrado satisfactoriamente!", "success");
		        				 $location.path("/index");
		        			 }else{
			 		             SweetAlert.swal("Planificaci&oacute;n Estrat&eacute;gica!", resp.mensajes.msg, "error");
		        				 
		        			 }
		        			
		        		})
		        		
		            }

		        }
    };
} ]);