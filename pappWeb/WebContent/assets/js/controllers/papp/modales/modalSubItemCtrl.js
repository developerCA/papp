'use strict';

app.controller('ModalSubItemController', ["$scope", "$rootScope", "$uibModalInstance","$filter", "ngTableParams","SubItemFactory",
	function($scope, $rootScope, $uibModalInstance,$filter, ngTableParams,subitemFactory) {
	
	$scope.codigoFiltro=null;
	$scope.nombreFiltro=null;
	$scope.padreFiltro=null;
	$scope.estadoFiltro=null;
	$scope.tipoFiltro=null;
	$scope.ivaFiltro=null;
		
	var pagina = 1;
	
	$scope.consultar=function(){
		
		$scope.data=[];
		subitemsFactory.traerItems(pagina, $rootScope.ejefiscal).then(function (resp) {
            if (resp.meta)
                $scope.data = resp;
        });
	
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
		
		console.log($scope.codigoFiltro + "-" +$scope.nombreFiltro + "-" +$scope.estadoFiltro + "-" + $scope.tipoFiltro);
		
		$scope.data=[];
		itemsFactory.traerItemsFiltro(pagina,$scope.codigoFiltro, $scope.nombreFiltro, $scope.estadoFiltro, $scope.tipoFiltro).then(function(resp){
			
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		
		$scope.codigoFiltro=null;
		$scope.nombreFiltro=null;
		$scope.padreFiltro=null;
		$scope.estadoFiltro=null;
		$scope.tipoFiltro=null;
		$scope.ivaFiltro=null;
		
		$scope.consultar();
		
	};
	
		
	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);
		
	};
	
	$scope.cancelar = function() {
			$uibModalInstance.dismiss('cancel');
	};



}]);