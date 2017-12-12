'use strict';

app.controller('MantenerIndicadoresController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","mantenerIndicadoresFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, mantenerIndicadoresFactory) {

	$scope.arbol={};
	$scope.edicion=false;
	$scope.guardar=false;
	$scope.objeto={};
	$scope.objetolista={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		mantenerIndicadoresFactory.traerMantenerIndicadores(
			pagina,
			$rootScope.ejefiscal
		).then(function(resp){
			//console.log(resp);
			if (resp.meta) {
				$scope.data = resp;
				$scope.arbol = JSON.parse(JSON.stringify($scope.data).split('"nombre":').join('"title":'));
				//console.log($scope.arbol);
			}
		})
	};

	$scope.cargarHijos=function(node){
		if (!node.iscargado)
			//console.log(node);
		    node.iscargado=true;

		    mantenerIndicadoresFactory.traerMantenerIndicadoresHijos(
	    		pagina,
	    		$rootScope.ejefiscal,
	    		node.id
    		).then(function(resp){
				var nodes=JSON.parse(JSON.stringify(resp).split('"nombre":').join('"title":'));
				node.nodes=nodes;
			})
	}

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
		mantenerIndicadoresFactory.traerMantenerIndicadoresFiltro(pagina,$scope.nombreFiltro).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}

	$scope.limpiar=function(){
		$scope.nombreFiltro=null;
		$scope.ordenFiltro=null;

		$scope.consultar();
	};
	
	$scope.nuevo=function(node){
		mantenerIndicadoresFactory.traerMantenerIndicadoresNuevoEstructura(
			node.id,
			$rootScope.ejefiscal,
			node.tipo
		).then(function(resp){
			$scope.objeto=resp.json.objetivo;

			console.log($scope.objeto);
			$scope.guardar=true;
			switch ($scope.objeto.tipo) {
				case "P": //Plan nacional
					$scope.edicion=true;
					break;
				case "E": //Estrategico
					$scope.edicionEstrategico=true;
					break;
				case "O": //Operativo
				case "F": //Fuerzas
				default:
					$scope.edicionOperativo=true;
					break;
			}
		})
	}
	
	$scope.editar=function(node){
		mantenerIndicadoresFactory.traerMantenerIndicadoresEditar(node.id).then(function(resp){
			console.log(resp.json.objetivo);
			if (resp.estado) {
			   $scope.objeto=resp.json.objetivo;
			}
			$scope.guardar=true;
			switch ($scope.objeto.tipo) {
				case "P": //Plan nacional
					$scope.edicion=true;
					break;
				case "E": //Estrategico
					$scope.edicionEstrategico=true;
					break;
				case "O": //Operativo
				case "F": //Fuerzas
				default:
					$scope.edicionOperativo=true;
					break;
			}
		})
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
			$scope.objeto.objetivoinstitucionid = obj.id;
			$scope.objeto.npCodigoInstitucion = obj.codigo;
			$scope.objeto.npNombreInstitucion = obj.nombre;
		}, function() {
		});
	};

	$scope.abrirMetas = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalMetas.html',
			controller : 'ModalMetasController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.objetivometaid = obj.id;
			$scope.objeto.npCodigoMeta = obj.codigo;
			$scope.objeto.npDescripcionMeta = obj.descripcion;
			$scope.objeto.npCodigoPolitica = obj.codigopadre;
			$scope.objeto.npDescripcionPolitica = obj.nombrepadre;
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
            	mantenerIndicadoresFactory.guardar($scope.objeto).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.edicionEstrategico=false;
	 		             $scope.edicionOperativo=false;
	 		             $scope.objeto={};
	 		             $scope.limpiar();
	 		             SweetAlert.swal("Formulaci&oacute;n Estrat&eacute;gica!", "Registro grabado satisfactoriamente!", "success");
        			 }else{
	 		             SweetAlert.swal("Formulaci&oacute;n Estrat&eacute;gica!", resp.mensajes.msg, "error");
        			 }
        		})
            }
        },
        reset: function (form) {
            $scope.myModel = angular.copy($scope.master);
            form.$setPristine(true);
            $scope.edicion=false;
            $scope.edicionEstrategico=false;
            $scope.edicionOperativo=false;
            $scope.objeto={};
        }
    };
} ]);