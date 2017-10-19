'use strict';

app.controller('ModalClasificacionController', ["$scope", "$uibModalInstance", "tipo","$filter", "ngTableParams","clasificacionFactory",
	function($scope, $uibModalInstance, tipo,$filter, ngTableParams,clasificacionFactory) {
	
	$scope.codigoFiltro=null;
	$scope.nombreFiltro=null;
	$scope.siglaFiltro=null;
		
	var pagina = 1;
	
	$scope.consultar=function(){
		
		$scope.data=[];
		clasificacionFactory.traerClasificaciones(pagina).then(function(resp){
			console.clear();
			console.log(resp);
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
		
		$scope.data=[];
		clasificacionFactory.traerClasificacionesFiltro(pagina,$scope.codigoFiltro, $scope.nombreFiltro,$scope.siglaFiltro).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		
		$scope.codigoFiltro=null;
		$scope.nombreFiltro=null;
		$scope.siglaFiltro=null;
		
		$scope.consultar();
		
	};
	
		
	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};
	
	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};

}]);
