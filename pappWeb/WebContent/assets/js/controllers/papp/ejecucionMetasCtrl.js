'use strict';

app.controller('EjecucionMetasController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","ejecucionMetasFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, ejecucionMetasFactory) {

	$scope.codigoFiltro=null;
	$scope.nombreFiltro=null;
	$scope.estadoFiltro=null;

	$scope.edicion=false;
	$scope.guardar=false;
	$scope.objeto={};
	$scope.objetolista={};
	$scope.lista=null;
	$scope.actividad={};
	$scope.mes=null;

	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data = [];
		ejecucionMetasFactory.traerFiltro(pagina, $rootScope.ejefiscal, null, null, null).then(function(resp){
			if (resp.meta)
				$scope.data = resp;
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
		ejecucionMetasFactory.traerFiltro(
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

	$scope.actulizarPantalla=function() {}

	$scope.editarActividad=function(id) {
		ejecucionMetasFactory.traerActividades(
				id,
				$rootScope.ejefiscal
		).then(function(resp){
			//console.log(resp.json);
			$scope.edicion=true;
			$scope.guardar=true;
			$scope.lista=resp.json.result;
		})
	};

	$scope.renovar=function() {
		ejecucionMetasFactory.traerRenovar(
				$scope.actividad,
				$rootScope.ejefiscal,
				$scope.mes
		).then(function(resp){
			//console.log(resp.json);
			$scope.listaDetalles=resp.json.result;
		})
	}

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
            	ejecucionMetasFactory.guardar(objEnviar).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.objeto={};
	 		             $scope.limpiar();
	 		             SweetAlert.swal("EjecucionMetas!", "Registro guardado satisfactoriamente!", "success");
        			 }else{
	 		             SweetAlert.swal("EjecucionMetas!", resp.mensajes.msg, "error");
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