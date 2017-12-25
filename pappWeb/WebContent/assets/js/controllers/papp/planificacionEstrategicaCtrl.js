'use strict';

app.controller('PlanificacionEstrategicaController', [ "$scope","$rootScope","$location","$uibModal","SweetAlert","$filter", "ngTableParams","PlanificacionEstrategicaFactory",
	function($scope,$rootScope,$location,$uibModal,SweetAlert,$filter, ngTableParams,PlanificacionEstrategicaFactory) {

	$scope.objeto={reporte: 'PDF'};

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
		                
		            	PlanificacionEstrategicaFactory.guardar($scope.objeto).then(function(resp){
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