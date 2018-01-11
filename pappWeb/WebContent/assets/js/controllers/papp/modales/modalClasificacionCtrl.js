'use strict';

app.controller('ModalClasificacionController', [ "$scope","$uibModalInstance","fuerza","SweetAlert","$filter", "ngTableParams","clasificacionFactory",
	function($scope,$uibModalInstance,fuerza,SweetAlert,$filter, ngTableParams,clasificacionFactory) {
    	
	$scope.nombreFiltro=null;
	$scope.codigoFiltro=null;
	$scope.siglaFiltro=null;
	$scope.estadoFiltro=null;
	$scope.edicion=false;
	$scope.objeto={};
	$scope.data=[];
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		clasificacionFactory.setFuerza(fuerza);
		clasificacionFactory.traerClasificacionesFuerzaFiltro(pagina, null, null, null, 'A').then(function(resp){
			console.log(resp);
			if (resp.meta)
				$scope.data=resp;
		})
	};
	
	$scope.consultarSinFuerza=function(){
		$scope.data=[];
		clasificacionFactory.traerClasificacionesFiltro(pagina, null, null, null, 'A').then(function(resp){
			console.log(resp);
			if (resp.meta)
				$scope.data=resp;
		})
	};

	$scope.$watch('data', function() {
		$scope.tableParams = new ngTableParams({
			page : 1, // show first page
			count : 10, // count per page
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
		clasificacionFactory.traerClasificacionesFuerzaFiltro(pagina,$scope.codigoFiltro,$scope.nombreFiltro, $scope.siglaFiltro, "A").then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}

	$scope.filtrarSinFuerza=function(){
		$scope.data=[];
		clasificacionFactory.traerClasificacionesFiltro(pagina,$scope.codigoFiltro,$scope.nombreFiltro, $scope.siglaFiltro, "A").then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.nombreFiltro=null;
		$scope.codigoFiltro=null;
		$scope.siglaFiltro=null;
		$scope.estadoFiltro=null;
		$scope.consultar();
		
	};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);

	};
	
	$scope.cancelar=function(){
		$uibModalInstance.dismiss();
	};
} ]);
