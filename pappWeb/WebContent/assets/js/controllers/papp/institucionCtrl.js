'use strict';

app.controller('InstitucionController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","institucionFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, institucionFactory) {

	$scope.nombre=null;

	$scope.edicion=false;
	$scope.guardar=false;
	$scope.objeto={};
	$scope.objetolista={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		institucionFactory.traerInstitucion(pagina).then(function(resp){
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
		institucionFactory.traerInstitucionFiltro(pagina,$scope.nombre).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.nombre=null;

		$scope.consultar();
	};
	
	$scope.nuevo=function(){
		$scope.objeto={id:null};
		$scope.objetolista=[];
		var obj={id:{permisoid:$scope.objeto},perfilpermisolectura:null};
		$scope.objetolista.push(obj);
		$scope.edicion=true;
		$scope.guardar=true;
	}
	
	$scope.editar=function(id){
		institucionFactory.traerInstitucionEditar(id).then(function(resp){
			console.log(resp.json);
			if (resp.estado) {
			   $scope.objeto=resp.json.institucion;
			   $scope.objetolista=resp.json.details;
			}
			$scope.edicion=true;
			$scope.guardar=true;

		})
		
	};

	$scope.agregarDetalle=function(){
		var obj={id:{perfilid:$scope.objeto,permisoid:null},nppermiso:null};
		$scope.objetolista.push(obj);
	}

	$scope.removerDetalle=function(index){
		$scope.objetolista.splice(index,1);
	}

	$scope.abrirNombrePais = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalPaises.html',
			controller : 'ModalDivisionGeograficaController',
			size : 'lg',
			resolve: {
				pais: function() {
					return $scope.objetolista[index].npnombrepais;
				},
				provincia: function() {
					return $scope.objetolista[index].npnombreprovincia;
				}
			}
		});
		console.log('lista antes');
		console.log($scope.objetolista[index]);
		modalInstance.result.then(function(obj) {
			$scope.objetolista[index].institucionentpaisid = obj.id;
			$scope.objetolista[index].npnombrepais = obj.nombre;
		}, function() {
		});
	};

	$scope.abrirNombreProvincia = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalProvincias.html',
			controller : 'ModalDivisionGeograficaController',
			size : 'lg',
			resolve: {
				pais: function() {
					return $scope.objetolista[index].npnombrepais;
				},
				provincia: function() {
					return $scope.objetolista[index].npnombreprovincia;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.objetolista[index].institucionentprovinciaid = obj.id;
			$scope.objetolista[index].npnombreprovincia = obj.nombre;
		}, function() {
		});
	};

	$scope.abrirNombreCanton = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalCantones.html',
			controller : 'ModalDivisionGeograficaController',
			size : 'lg',
			resolve: {
				pais: function() {
					return $scope.objetolista[index].npnombrepais;
				},
				provincia: function() {
					return $scope.objetolista[index].npnombreprovincia;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.objetolista[index].institucionentcantonid = obj.id;
			$scope.objetolista[index].npnombrecanton = obj.nombre;
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
		                
		            	institucionFactory.guardar($scope.objeto).then(function(resp){
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