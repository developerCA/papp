'use strict';

app.controller('ModalInstitucionController', [ "$scope","$uibModalInstance","$uibModal","SweetAlert","$filter", "ngTableParams","institucionFactory",
	function($scope,$uibModalInstance,$uibModal,SweetAlert,$filter, ngTableParams, institucionFactory) {

		$scope.nombre=null;

		$scope.edicion=false;
		$scope.guardar=false;
		$scope.objeto={};
		$scope.objetolista={};
		
		var pagina = 1;
		
		$scope.consultar=function(){
			$scope.data=[];
			institucionFactory.traerInstitucion(pagina).then(function(resp){
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
			institucionFactory.traerInstitucionFiltro(pagina,$scope.nombre).then(function(resp){
				if (resp.meta)
					$scope.data=resp;
			})
		}
		
		$scope.limpiar=function(){
			$scope.nombre=null;

			$scope.consultar();
		};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};

	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};

}]);
