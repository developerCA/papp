'use strict';
 
app.controller('ModalFuerzaController', [ "$scope","$uibModalInstance","SweetAlert","$filter", "ngTableParams","fuerzaFactory",  function($scope,$uibModalInstance,SweetAlert,$filter, ngTableParams,fuerzaFactory) {
    	
		$scope.nombreFiltro=null;
		$scope.codigoFiltro=null;
		$scope.estadoFiltro=null;
		$scope.siglaFiltro=null;
		$scope.edicion=false;
		$scope.objeto={};
		$scope.detalles=[];
		
		var pagina = 1;
		
		$scope.consultar=function(){
			$scope.data=[];
			fuerzaFactory.traerFuerzas(pagina).then(function(resp){
				console.log(resp);
				if (resp.meta)
					$scope.data=resp;				
			})
		};
		
		$scope.$watch('data', function() {
			$scope.tableParams = new ngTableParams({
				page : 1, // show first page
				count : 10, // count per page
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
			fuerzaFactory.traerFuerzasFiltro(pagina,$scope.nombreFiltro,$scope.codigoFiltro,$scope.sigla, $scope.estadoFiltro).then(function(resp){
				
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

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);

	};
	
	$scope.cancelar=function(){
		$uibModalInstance.dismiss();
	};
} ]);