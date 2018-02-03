'use strict';

app.controller('ModalUnidadController', [ "$scope","$rootScope","$uibModalInstance","SweetAlert","$filter", "ngTableParams","unidadFactory",
	function($scope,$rootScope,$uibModalInstance,SweetAlert,$filter, ngTableParams,unidadFactory) {

	$scope.nombreFiltro=null;
	$scope.codigoFiltro=null;
	$scope.estadoFiltro=null;
	$scope.edicion=false;
	$scope.objeto={};
	$scope.detalles=[];
	$scope.data=[];

	var pagina = 1;

	$scope.consultar=function(){
		unidadFactory.traerUnidades(
			pagina
		).then(function(resp){
			//console.log(resp);
			if (resp.meta) $scope.data = resp;
		})
	};

	$scope.consultarF=function(){
		unidadFactory.traerFiltro(
			pagina,
			$rootScope.ejefiscal,
			$scope.nombreFiltro,
			$scope.codigoFiltro,
			$scope.estadoFiltro
		).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}

	$scope.filtrar=function(){
		unidadFactory.traerUnidadesFiltro(
			pagina,
			$scope.nombreFiltro,
			$scope.codigoFiltro,
			$scope.estadoFiltro
		).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.nombreFiltro=null;
		$scope.codigoFiltro=null;
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

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};
	
	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};
}]);
