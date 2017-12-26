'use strict';

app.controller('ModalClaseGastoController', [ "$scope","$rootScope","$uibModalInstance","SweetAlert","$filter", "ngTableParams","claseGastoFactory",
	function($scope,$rootScope,$uibModalInstance,SweetAlert,$filter, ngTableParams,claseGastoFactory) {

	$scope.codigoFiltro=null;
	$scope.nombreFiltro=null;
	$scope.codigoClaseRegistroFiltro=null;
	$scope.nombreClaseRegistroFiltro=null;
	$scope.codigoClaseModificacionFiltro=null;
	$scope.nombreClaseModificacionFiltro=null;
	$scope.edicion=false;
	$scope.objeto={};
	$scope.detalles=[];
	$scope.pagina=1;
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		//console.log("Eje.Fis.:"+$rootScope.ejefiscal);
		claseGastoFactory.traerClases(
			$scope.pagina,
			$rootScope.ejefiscal
		).then(function(resp){
			console.log(resp);
			if (resp.meta)
				$scope.data=resp;
		})
	};

	$scope.filtrar=function(){
		$scope.data=[];
		claseGastoFactory.traerClasesFiltro(
			$scope.pagina,
			$rootScope.ejefiscal,
			$scope.codigoFiltro,
			$scope.nombreFiltro,
			$scope.codigoClaseRegistroFiltro,
			$scope.nombreClaseRegistroFiltro,
			$scope.codigoClaseModificacionFiltro,
			$scope.nombreClaseModificacionFiltro
		).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
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
		$scope.nombreFiltro=null;
		$scope.codigoClaseRegistroFiltro=null;
		$scope.nombreClaseRegistroFiltro=null;
		$scope.codigoClaseModificacionFiltro=null;
		$scope.nombreClaseModificacionFiltro=null;
		
		$scope.consultar();
		
	};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};
	
	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};
}]);
