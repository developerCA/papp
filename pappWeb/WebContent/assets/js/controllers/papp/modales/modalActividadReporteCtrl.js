'use strict';

app.controller('ModalActividadReporteController', [ "$scope","$uibModalInstance","$uibModal","ejefiscal","npunidad","npprogramaid","npproyectoid","SweetAlert","$filter", "ngTableParams","actividadReporteFactory",
	function($scope,$uibModalInstance,$uibModal,ejefiscal,npunidad,npprogramaid,npproyectoid,SweetAlert,$filter, ngTableParams, actividadReporteFactory) {

	$scope.nombre=null;
	
	var pagina = 1;
	
	$scope.filtrar=function(){
		$scope.data=[];
		actividadReporteFactory.traerActividadFiltro(
			pagina,
			ejefiscal,
			$scope.nombre,
			npunidad,
			npprogramaid,
			npproyectoid
		).then(function(resp){
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
		$scope.nombre=null;

		$scope.filtrar();
	};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};

	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};
}]);
