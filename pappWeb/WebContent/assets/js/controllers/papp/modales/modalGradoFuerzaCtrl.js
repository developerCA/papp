'use strict';

app.controller('ModalGradoFuerzaController', ["$scope", "$rootScope", "$uibModalInstance","$filter", "ngTableParams","gradofuerzaFactory",
	function($scope, $rootScope, $uibModalInstance,$filter, ngTableParams,gradofuerzaFactory) {
	
	$scope.codigo=null;
	$scope.fuerza=null;
	$scope.grado=null;
	$scope.padre=null;
	$scope.estado=null;

	$scope.edicion=false;
	$scope.nuevoar=false;
	$scope.guardar=false;
	$scope.objeto={};
		
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		gradofuerzaFactory.traerGradoFuerza(pagina).then(function(resp){
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
		gradofuerzaFactory.traerGradoFuerzaFiltro(pagina,$scope.codigo,$scope.fuerza,$scope.grado,$scope.padre,$scope.estado).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.codigo=null;
		$scope.fuerza=null;
		$scope.grado=null;
		$scope.padre=null;
		$scope.estado=null;

		$scope.consultar();
		
	};
			
	$scope.seleccionar=function(obj){
		console.log("=============");
		console.log(obj);
		console.log("=============");
		$uibModalInstance.close(obj);		
	};
	
	$scope.cancelar = function() {
			$uibModalInstance.dismiss('cancel');
	};

}]);
