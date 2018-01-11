'use strict';

app.controller('InstitucionController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","institucionFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, institucionFactory) {

	$scope.codigoFiltro=null;
	$scope.nombreFiltro=null;
	$scope.estadoFiltro=null;

	$scope.edicion=false;
	$scope.guardar=false;
	$scope.objeto={};
	$scope.objetolista={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		institucionFactory.traerInstitucion(pagina, $rootScope.ejefiscal).then(function(resp){
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
		console.log($scope.estadoFiltro);
		$scope.data=[];
		institucionFactory.traerInstitucionFiltro(
			pagina,
			$rootScope.ejefiscal,
			$scope.codigoFiltro,
			$scope.nombreFiltro,
			$scope.estadoFiltro
		).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.codigoFiltro=null;
		$scope.nombreFiltro=null;
		$scope.estadoFiltro=null;

		$scope.consultar();
	};
	
	$scope.nuevo=function(){
		$scope.objeto={
			id: null,
			institucionejerciciofiscalid: $rootScope.ejefiscal,
			estado: "A"
		};
		$scope.objetolista=[];
		//$scope.agregarDetalle();
		$scope.edicion=true;
		$scope.guardar=true;
	}
	
	$scope.editar=function(id){
		institucionFactory.traerInstitucionEditar(id).then(function(resp){
			console.log(resp.json);
			$scope.edicion=true;
			$scope.guardar=true;
			//if (resp.estado) {
			   $scope.objeto=resp.json.institucion;
			   $scope.objetolista=resp.json.details;
			//}
		})
	};

	$scope.agregarDetalle=function(){
		var obj={
			id: {
				entid: null, // este es un ID del orde de la lista
				id: null
			},
			estado: "A"
		};
		$scope.objetolista.push(obj);
		console.log(obj);
		console.log($scope.objeto);
		console.log($scope.objetolista);
	}

	$scope.removerDetalle=function(index){
		$scope.objetolista.splice(index,1);
	}

	$scope.chequiarCodigo=function(index) {
		console.log(index);
		for (var i = 0; i < $scope.objetolista.length; i++) {
			if (index == i) continue;
			if ($scope.objetolista[index].codigo == $scope.objetolista[i].codigo) {
				SweetAlert.swal(
					"Institucion!",
					"El codigo ya existe, ingrece uno nuevo",
					"error"
				);
				$scope.objetolista[index].codigo = "";
				return;
			}
		}
	}

	$scope.abrirNombrePais = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalPaises.html',
			controller : 'ModalDivisionGeograficaController',
			size : 'lg',
			resolve: {
				pais: function() {
					return null;
				},
				provincia: function() {
					return null;
				},
				tipo : function() {
					return null;
				}
			}
		});
		console.log('pais:');
		console.log($scope.objetolista[index].npnombrepais);
		console.log('provincia:');
		console.log($scope.objetolista[index].npnombreprovincia);
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
					return $scope.objetolista[index].institucionentpaisid;
				},
				provincia: function() {
					return $scope.objetolista[index].institucionentprovinciaid;
				},
				tipo : function() {
					return null;
				}
			}
		});
		console.log($scope.objetolista[index]);
		console.log('pais:');
		console.log($scope.objetolista[index].npnombrepais);
		console.log('provincia:');
		console.log($scope.objetolista[index].npnombreprovincia);
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
					return $scope.objetolista[index].institucionentpaisid;
				},
				provincia: function() {
					return $scope.objetolista[index].institucionentprovinciaid;
				},
				tipo : function() {
					return null;
				}
			}
		});
		console.log('pais:');
		console.log($scope.objetolista[index].npnombrepais);
		console.log('provincia:');
		console.log($scope.objetolista[index].npnombreprovincia);
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
                var objEnviar = Object.assign({}, $scope.objeto);
                objEnviar.details = $scope.objetolista;
                console.log(objEnviar);
            	institucionFactory.guardar(objEnviar).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.objeto={};
	 		             $scope.limpiar();
	 		             SweetAlert.swal("Institucion!", "Registro guardado satisfactoriamente!", "success");
        			 }else{
	 		             SweetAlert.swal("Institucion!", resp.mensajes.msg, "error");
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