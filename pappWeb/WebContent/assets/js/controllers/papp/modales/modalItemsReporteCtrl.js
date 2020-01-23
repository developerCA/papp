'use strict';

app.controller('ModalItemsReporteController', [ "$scope","$uibModalInstance","$uibModal","ejefiscal","npunidad","nivelactividadpadreid","actividadid","SweetAlert","$filter", "ngTableParams","itemsReporteFactory",
	function($scope,$uibModalInstance,$uibModal,ejefiscal,npunidad,nivelactividadpadreid,actividadid,SweetAlert,$filter, ngTableParams, itemsReporteFactory) {

	$scope.estado="A";
	
	var pagina = 1;
	
	$scope.filtrar=function(){
		$scope.data=[];
		itemsReporteFactory.traerFiltro(
			pagina,
			ejefiscal,
			nivelactividadpadreid,
			npunidad,
			actividadid
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
		$scope.tipo=null;
		$scope.nivelactividadpadreid=null;
		$scope.nivelactividadunidadid=null;
		$scope.estado=null;

		$scope.filtrar();
	};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};

	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};

}]);
