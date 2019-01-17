'use strict';

app.controller('ModalObjetivoOperacionalReporteController', [ "$scope","$uibModalInstance","$uibModal","ejefiscal","padre","SweetAlert","$filter", "ngTableParams","ObjetivoOperacionalReporteFactory",
	function($scope,$uibModalInstance,$uibModal,ejefiscal,padre,SweetAlert,$filter, ngTableParams, ObjetivoOperacionalReporteFactory) {

	$scope.edicion=false;
	$scope.guardar=false;
	$scope.objeto={};
	$scope.objetolista={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		ObjetivoOperacionalReporteFactory.traerFiltro(
			pagina,
			ejefiscal,
			padre
		).then(function(resp){
			//console.log(resp);
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
	
	$scope.limpiar=function(){
		$scope.consultar();
	};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};

	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};
}]);
