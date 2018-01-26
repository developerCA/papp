'use strict';

app.controller('ModalObjetivosController', [ "$scope","$rootScope","ejefiscal","$uibModalInstance","SweetAlert","$filter", "ngTableParams","objetivosFactory",
	function($scope,$rootScope,ejefiscal,$uibModalInstance,SweetAlert,$filter, ngTableParams,objetivosFactory) {

	$scope.codigoFiltro=null;
	$scope.descripcionFiltro=null;
	$scope.edicion=false;
	$scope.objeto={};
	$scope.detalles=[];
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		objetivosFactory.traerObjetivos(
			pagina,
			ejefiscal
		).then(function(resp){
			console.log(resp);
			if (resp.meta)
				$scope.data=resp;
		})
	};

	$scope.filtrar=function(){
		$scope.data=[];
		objetivosFactory.traerObjetivosFiltro(
			pagina,
			$scope.codigoFiltro,
			$scope.descripcionFiltro
		).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.nombreFiltro=null;
		$scope.codigoFiltro=null;
		$scope.estadoFiltro=null;
		
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
