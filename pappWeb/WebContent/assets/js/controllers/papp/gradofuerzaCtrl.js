'use strict';

app.controller('GradoFuerzaController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","gradofuerzaFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, gradofuerzaFactory) {

	$scope.codigo=null;
	$scope.fuerza=null;
	$scope.grado=null;
	$scope.padre=null;
	$scope.estado=null;

	$scope.edicion=false;
	$scope.nuevoar=false;
	$scope.guardar=false;
	$scope.objeto={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		gradofuerzaFactory.traerGradoFuerza(pagina).then(function(resp){
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
		gradofuerzaFactory.traerGradoFuerzaFiltro(pagina,$scope.codigo,$scope.fuerza,$scope.grado,$scope.padre,$scope.estado).then(function(resp){
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
		$scope.objeto={id:null,estado:"A", npnivel:1};

		$scope.edicion=true;
		$scope.nuevoar=true;
		$scope.guardar=true;
	}
	
	$scope.editar=function(id){
		gradofuerzaFactory.traerGradoFuerzaEditar(id).then(function(resp){
			if (resp.estado) {
			    $scope.objeto=resp.json.gradofuerza;
			}
			$scope.edicion=true;
			$scope.nuevoar=false;
			$scope.guardar=true;
		})
	};

	$scope.abrirGrado = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalGrado.html',
			controller : 'ModalGradoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.gradofuerzagradoid = obj.id;
			$scope.objeto.npcodigogrado = obj.codigo;
			$scope.objeto.npnombregrado = obj.nombre;
			$scope.objeto.npsiglagrado = obj.sigla;
			$scope.objeto.npgrupo = obj.npgruponombre;
		}, function() {

		});
	};

	$scope.abrirFuerza = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalFuerza.html',
			controller : 'ModalFuerzaController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.gradofuerzafuerzaid = obj.id;
			$scope.objeto.npcodigofuerza = obj.codigo;
			$scope.objeto.npnombrefuerza = obj.nombre;
			$scope.objeto.npsiglafuerza = obj.sigla;
		}, function() {
		});
	};

	$scope.abrirSuperior = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalGradoFuerzaSuperior.html',
			controller : 'ModalGradoFuerzaSuperiorController',
			size : 'lg',
			resolve: {
				idfuerza: $scope.objeto.gradofuerzafuerzaid
			}
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.gradofuerzapadreid = obj.id;
			$scope.objeto.npnombrepadre = obj.codigo;
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
		                
		            	gradofuerzaFactory.guardar($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Grado - Fuerza!", "Registro registrado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Grado - Fuerza!", resp.mensajes.msg, "error");
		        				 
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