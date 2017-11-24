'use strict';

app.controller('ModalCargoEscalaController', [ "$scope","$rootScope","$uibModalInstance","SweetAlert","$filter", "ngTableParams","CargoEscalaFactory",
	function($scope,$rootScope,$uibModalInstance,SweetAlert,$filter, ngTableParams,cargoEscalaFactory) {
    	
	$scope.codigo=null;
	$scope.nombrecargo=null;
	$scope.grupoocupacional=null;
	$scope.estado=null;
	$scope.edicion=false;
	$scope.objeto={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		
		$scope.data=[];
		cargoEscalaFactory.traer(pagina).then(function(resp){
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
				
		cargoEscalaFactory.traerFiltro(pagina,$scope.codigo,$scope.nombrecargo, $scope.grupoocupacional).then(function(resp){
			
			if (resp.meta)
				$scope.data=resp;
		})
	};
	
	$scope.limpiar=function(){

		$scope.codigo=null;
		$scope.nombrecargo=null;
		$scope.grupoocupacional=null;
		$scope.estado=null;
		$scope.consultar();
		
	};
	
	$scope.nuevo=function(){
		
		$scope.objeto={id:null,estado:'A'};		
		$scope.edicion=true;
	};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};
	
	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};
}]);
