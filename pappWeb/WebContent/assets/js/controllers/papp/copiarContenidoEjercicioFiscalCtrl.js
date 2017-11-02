'use strict';

app.controller('CopiarContenidoEjercicioFiscalController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","copiarContenidoEjercicioFiscalFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, copiarContenidoEjercicioFiscalFactory) {

	$scope.edicion=true;
	$scope.guardar=true;
	$scope.objeto={origen:null,origenanio:'',destino:null,destinoanio:'',tipo:''};

	$scope.editar=function(){
	};

	$scope.abrirEjercicioFiscalOrigen = function() {
		var modalEFiscales = $uibModal.open({
			templateUrl : 'modalEjerciciosFiscales.html',
			controller : 'ModalEjerciciosFiscalesController',
			size : 'lg'
		});
		modalEFiscales.result.then(function(obj) {
			$scope.objeto.origen = obj.id.toString();
			$scope.objeto.origenanio = obj.anio;
		}, function() {
			console.log("close modal");
		});
	};

	$scope.abrirEjercicioFiscalDestino = function() {
		var modalEFiscales = $uibModal.open({
			templateUrl : 'modalEjerciciosFiscales.html',
			controller : 'ModalEjerciciosFiscalesController',
			size : 'lg'
		});
		modalEFiscales.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.destino = obj.id.toString();
			$scope.objeto.destinoanio = obj.anio;
		}, function() {
			console.log("close modal");
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
		                console.log($scope.objeto);
		            	copiarContenidoEjercicioFiscalFactory.guardar($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Permiso!", "Registro registrado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Permiso!", resp.mensajes.msg, "error");
		        				 
		        			 }
		        			
		        		})
		        		
		            }

		        },
		        reset: function (form) {

		            $scope.myModel = angular.copy($scope.master);
		            form.$setPristine(true);
		            $scope.edicion=false;
		            $scope.objeto={};

		        }
    };
} ]);