'use strict';

app.controller('ProveedorController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","proveedorrucFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, proveedorrucFactory) {

	$scope.codigoFiltro=null;
	$scope.nombremostradoFiltro=null;
	$scope.nombrecomercialFiltro=null;
	$scope.representantelegalFiltro=null;
	$scope.estadoFiltro=null;

	$scope.edicion=false;
	$scope.guardar=false;
	$scope.nuevoar=false;
	$scope.objeto={};
	$scope.data=[];

    $scope.pagina = 1;
    $scope.aplicafiltro=false;

	$scope.consultar=function(){
		proveedorrucFactory.traer(
			$scope.pagina
		).then(function(resp){
        	$scope.data = resp.json.result;
            $scope.total = resp.json.total.valor;
		})
	};

    $scope.pageChanged = function() {
        if ($scope.aplicafiltro){
        	$scope.filtrarUnico();
        }else{
        	$scope.consultar();	
        }
    };  

    $scope.filtrar=function(){
    	$scope.pagina=1;
    	$scope.aplicafiltro=true;
    	$scope.filtrarUnico();
    }  

	$scope.filtrarUnico=function(){
		proveedorrucFactory.traerFiltro(
			$scope.pagina,
			$scope.codigoFiltro,
			$scope.nombremostradoFiltro,
			$scope.nombrecomercialFiltro,
			$scope.representantelegalFiltro,
			$scope.estadoFiltro
		).then(function(resp){
        	$scope.data = resp.json.result;
            $scope.total = resp.json.total.valor;
		})
	}
	
	$scope.limpiar=function(){
		$scope.codigoFiltro=null;
		$scope.nombremostradoFiltro=null;
		$scope.nombrecomercialFiltro=null;
		$scope.representantelegalFiltro=null;
		$scope.estadoFiltro=null;

		$scope.consultar();
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

	$scope.editar=function(id){
		proveedorrucFactory.traerEditar(id).then(function(resp){
			if (resp.estado) {
				$scope.objeto=resp.json.empleado;
				$scope.fuerza=$scope.objeto.npfuerzaid;
			}
			$scope.edicion=true;
			$scope.guardar=true;
			$scope.nuevoar=false;
		})
	};

	$scope.abrirEspecialidad = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalEspecialidades.html',
			controller : 'ModalEspecialidadesController',
			size : 'lg',
			resolve : {
				fuerza : function() {
					return $scope.fuerza;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.socionegocioempespid = obj.id;
			$scope.objeto.npespecialidadcodigo = obj.codigo;
			$scope.objeto.npespecialidadnombre = obj.nombre;
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
            	proveedorrucFactory.guardar($scope.objeto).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.objeto={};
	 		             $scope.limpiar();
	 		             resp.mensajes.msg = "Registro guardado satisfactoriamente!";
        			 }
 		             SweetAlert.swal(
 		            		 "Proveedor Juridico!",
 		            		 resp.mensajes.msg,
 		            		 "error"
            		 );
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