'use strict';

app.controller('ModalUnidadCortoController', [ "$scope","$rootScope","$uibModalInstance","estadoaprobado","SweetAlert","$filter", "ngTableParams","unidadFactory",
	function($scope,$rootScope,$uibModalInstance,estadoaprobado,SweetAlert,$filter, ngTableParams,unidadFactory) {

	$scope.nombreFiltro=null;
	$scope.codigoFiltro=null;
	$scope.estadoFiltro=null;
	$scope.edicion=false;
	$scope.objeto={};
	$scope.detalles=[];
	$scope.data = [];
	
	var pagina = 1;
	
	$scope.consultar=function(){
		unidadFactory.traerPlanificacionFiltroAprobado(
				pagina,
				$rootScope.ejefiscal,
				null,
				null,
				"A",
				estadoaprobado
		).then(function(resp){
			//console.log(resp);
			if (resp.meta)
				$scope.data=resp;
		})
	};

	$scope.filtrar=function(){
		$scope.data=[];
		unidadFactory.traerPlanificacionFiltroAprobado(
			pagina,
			$rootScope.ejefiscal,
			$scope.codigoFiltro,
			$scope.nombreFiltro,
			"A",
			estadoaprobado
		).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
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
	
	$scope.limpiar=function(){
		$scope.nombreFiltro=null;
		$scope.codigoFiltro=null;
		$scope.estadoFiltro=null;
		
		$scope.consultar();
		
	};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};
	
	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};
}]);
