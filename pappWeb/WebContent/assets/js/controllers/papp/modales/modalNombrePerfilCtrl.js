'use strict';

app.controller('ModalNombrePerfilController', [ "$scope","$uibModalInstance","SweetAlert","$filter", "ngTableParams","PerfilesFactory",
	function($scope,$uibModalInstance,SweetAlert,$filter, ngTableParams,PerfilesFactory) {

	$scope.nombreFiltro=null;
	$scope.idFiltro=null;
	
	$scope.edicion=false;
	$scope.objeto={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		PerfilesFactory.traer(pagina).then(function(resp){
			
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
		PerfilesFactory.traerFiltro(pagina,$scope.nombreFiltro).then(function(resp){
		if (resp.meta)
			$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.nombreFiltro=null;
		$scope.idFiltro=null;
		$scope.consultar();
	};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);
	};
	
	$scope.cancelar=function(){
		$uibModalInstance.dismiss();
	};
} ]);