'use strict';

app.controller('CertificacionesFondosController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","certificacionesFondosFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, certificacionesFondosFactory) {

	$scope.codigo=null;
	$scope.precompromiso=null;
	$scope.valorinicial=null;
	$scope.valorfinal=null;
	$scope.fechainicial=null;
	$scope.fechafinal=null;
	$scope.estado=null;

	$scope.edicion=false;
	$scope.nuevoar=false;
	$scope.guardar=false;
	$scope.objeto={estado:null};
	$scope.objetodetalles={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		//console.log('aqi');
		certificacionesFondosFactory.traerCertificacionesFondos(pagina).then(function(resp){
			console.log(resp);
			if (resp.meta)
				$scope.data=resp;
		})
	};
	
	$scope.$watch('data', function() {
		$scope.tableParams = new ngTableParams({
			page : 1, // show first page
			count : 5, // count per page
			filter: {} 	
		}, {
			total : $scope.data.length, // length of data
			getData : function($defer, params) {
				var orderedData = params.filter() ? $filter('filter')(
						$scope.data, params.filter()) : $scope.data;
				$scope.lista = orderedData.slice(
						(params.page() - 1) * params.count(), params
								.page()
								* params.count());
				params.total(orderedData.length);
				$defer.resolve($scope.lista);
			}
		});
	});

	$scope.filtrar=function(){
		$scope.data=[];
		certificacionesFondosFactory.traerCertificacionesFondosFiltro(pagina,$scope.codigo,$scope.fuerza,$scope.grado,$scope.padre,$scope.estado).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.codigo=null;
		$scope.fuerza=null;
		$scope.grado=null;
		$scope.padre=null;
		$scope.estado=null;

		$scope.consultar();
		
	};
	
	$scope.nuevo=function(){
		$scope.objeto={id:null,estado:null};
		$scope.edicion=true;
		$scope.nuevoar=true;
		$scope.guardar=true;
	}
	
	$scope.editar=function(id){
		certificacionesFondosFactory.traerCertificacionesFondosEditar(id).then(function(resp){
			console.log(resp.json);
			if (resp.estado) {
			    $scope.objeto=resp.json.certificacion;
			    $scope.objetodetalles=resp.json.certificacionlineas;
			}
			$scope.edicion=true;
			$scope.nuevoar=false;
			$scope.guardar=true;
		})
	};

	$scope.abrirUnidadCodigo = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalUnidades.html',
			controller : 'ModalUnidadController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.certificacionunidadid = obj.id;
			$scope.objeto.npunidadcodigo = obj.codigopresup;
			$scope.objeto.npunidadnombre = obj.nombre;
		}, function() {
			console.log("close modal");
		});
	};

	$scope.abrirClaseRegistroCodigo = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalClaseRegistro.html',
			controller : 'ModalClaseRegistroController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.certificacionclaseregid = obj.id;
			$scope.objeto.npcodigoregistro = obj.codigo;
			$scope.objeto.npnombreregistro = obj.nombre;
		}, function() {
			console.log("close modal");
		});
	};

	$scope.abrirTipoDocumentoCodigo = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalTipoDocumento.html',
			controller : 'ModalTipoDocumentoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.certificaciontipodocid = obj.id;
			$scope.objeto.npcodigotipodocumento = obj.codigo;
			$scope.objeto.npnombretipodocumento = obj.nombre;
		}, function() {
			console.log("close modal");
		});
	};

	$scope.liquidarManualMente = function(id) {
		console.log("ok");
		var modalInstance = $uibModal.open({
			templateUrl : 'modalLiquidacionManua.html',
			controller : 'ModalCertificacionesFondoLiquidacionManuaController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
/*
			$scope.objeto.certificaciontipodocid = obj.id;
			$scope.objeto.npcodigotipodocumento = obj.codigo;
			$scope.objeto.npnombretipodocumento = obj.nombre;
*/
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
		                
		            	certificacionesFondosFactory.guardar($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Certificaciones de Fondos!", "Registro registrado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Certificaciones de Fondos!", resp.mensajes.msg, "error");
		        				 
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