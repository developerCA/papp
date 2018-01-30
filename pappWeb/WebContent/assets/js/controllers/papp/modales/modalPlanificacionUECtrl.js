'use strict';

app.controller('ModalPlanificacionUEController', ["$scope", "$rootScope", "objFuente", "$uibModalInstance","$filter", "ngTableParams","PlanificacionUEController",
	function($scope, $rootScope, objFuente, $uibModalInstance,$filter, ngTableParams,PlanificacionUEController) {
	
	$scope.codigo=null;
	$scope.grado=null;
	$scope.grupo=null;
	$scope.estado=null;
	$scope.edicion=false;
	$scope.objeto={};
	$scope.data=[];
		
	var pagina = 1;
	
	$scope.consultar=function(){
		escalaRemuneracionFactory.traer(pagina).then(function(resp){
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
		escalaRemuneracionFactory.traerFiltro(pagina,$scope.grupo,$scope.codigo,$scope.grado, $scope.estado).then(function(resp){
			
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		
		$scope.codigo=null;
		$scope.grado=null;
		$scope.grupo=null;
		$scope.estado=null;
		
		$scope.consultar();
		
	};
	
		
	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};
	
	$scope.cancelar = function() {
			$uibModalInstance.dismiss('cancel');
	};



}]);
