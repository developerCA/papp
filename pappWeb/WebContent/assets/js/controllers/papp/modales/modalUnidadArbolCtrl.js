'use strict';

app.controller('ModalUnidadArbolController', [ "$scope","$rootScope","$uibModalInstance","instituicionFuente","institucionentidad","SweetAlert","$filter", "ngTableParams","unidadFactory",
	function($scope,$rootScope,$uibModalInstance,instituicionFuente,institucionentidad,SweetAlert,$filter, ngTableParams,unidadFactory) {

	$scope.id=null;
	$scope.institucion=null;
	$scope.estado='A';
	$scope.data=[];

	$scope.consultar=function(){
		unidadFactory.traerUnidadArbolFiltro(
			1,
			$scope.id,
			instituicionFuente,
			institucionentidad,
			$scope.estado
		).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.id=null;
		$scope.institucion=null;
		$scope.estado=null;
		
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
