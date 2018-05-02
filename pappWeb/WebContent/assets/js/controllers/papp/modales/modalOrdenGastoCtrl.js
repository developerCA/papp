'use strict';

app.controller('ModalOrdenGastoController', [ "$scope","$rootScope","$uibModalInstance","SweetAlert","$filter", "ngTableParams","ordenDevengoFactory",
	function($scope,$rootScope,$uibModalInstance,SweetAlert,$filter, ngTableParams,ordenDevengoFactory) {

	$scope.codigoFiltro=null;
	$scope.unidadFiltro=null;
	$scope.descripcionFiltro=null;

	$scope.edicion=false;
	$scope.data=[];
	
	var pagina = 1;

	$scope.filtrar=function(){
		ordenDevengoFactory.modalTraerFiltro(
			pagina,
			$rootScope.ejefiscal,
			$scope.codigoFiltro,
			"AP",
			$scope.unidadFiltro,
			$scope.descripcionFiltro
		).then(function(resp){
			$scope.data=resp.result;
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
		$scope.codigoFiltro=null;
		$scope.unidadFiltro=null;
		$scope.descripcionFiltro=null;

		$scope.filtrar();
	};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};
	
	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};
}]);
