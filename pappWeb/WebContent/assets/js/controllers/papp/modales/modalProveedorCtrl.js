'use strict';

app.controller('ModalProveedorController', [ "$scope","$rootScope","$uibModalInstance","SweetAlert","$filter", "ngTableParams","proveedorFactory",
	function($scope,$rootScope,$uibModalInstance,SweetAlert,$filter, ngTableParams,proveedorFactory) {

	$scope.codigoFiltro=null;
	$scope.nombremostradoFiltro=null;
	$scope.razonsocialFiltro=null;
	$scope.edicion=false;
	$scope.data=[];

	var pagina = 1;

	$scope.filtrar=function(){
		proveedorFactory.modalTraerFiltro(
			pagina,
			$scope.codigoFiltro,
			null,
			$scope.nombremostradoFiltro,
			$scope.razonsocialFiltro,
			"A",
			1
		).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.codigoFiltro=null;
		$scope.nombremostradoFiltro=null;
		$scope.razonsocialFiltro=null;
		
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
