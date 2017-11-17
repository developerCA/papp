'use strict';

app.controller('EmpleadosController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","empleadosFactory","$uibModalInstance",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, empleadosFactory,$uibModalInstance) {

	$scope.nombreFiltro=null;
	$scope.codigoFiltro=null;
	$scope.estadoFiltro=null;

	$scope.edicion=false;
	$scope.guardar=false;
	$scope.objeto={};
	$scope.objetolista={};

	var pagina = 1;

	$scope.consultar=function(){
		$scope.data=[];
		empleadosFactory.traerEmpleados(pagina).then(function(resp){
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

		empleadosFactory.traerEmpleadosFiltro(pagina,$scope.codigoFiltro,$scope.nombreFiltro,$scope.estadoFiltro).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.nombreFiltro=null;
		$scope.ordenFiltro=null;
		
		
		$scope.consultar();
		
	};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);
	};
	
	$scope.cancelar=function(){
		$uibModalInstance.dismiss();
	};
} ]);