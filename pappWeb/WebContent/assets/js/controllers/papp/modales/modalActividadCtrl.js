'use strict';

app.controller('ModalActividadController', [ "$scope","$rootScope","$uibModalInstance","$uibModal","unidadId","SweetAlert","$filter", "ngTableParams","actividadFactory",
	function($scope,$rootScope,$uibModalInstance,$uibModal,unidadId,SweetAlert,$filter, ngTableParams, actividadFactory) {

	$scope.codigoFiltro=null;
	$scope.descripcionFiltro=null;

	$scope.edicion=false;
	$scope.guardar=false;
	$scope.objeto={};
	$scope.objetolista={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		actividadFactory.traerActividadNivelFiltro(unidadId,$rootScope.ejefiscal,null,null).then(function(resp){
			console.log(resp);
			if (resp.meta)
				$scope.data=resp;
		})
	};

	$scope.filtrar=function(){
		$scope.data=[];
		actividadFactory.traerActividadNivelFiltro(unidadId,$rootScope.ejefiscal,$scope.codigo,$scope.nombre).then(function(resp){
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
		$scope.descripcionFiltro=null;

		$scope.consultar();
	};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};

	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};

}]);
