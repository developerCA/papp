'use strict';

app.controller('ModalGradoEscalaController', [ "$scope","$uibModalInstance","SweetAlert","$filter", "ngTableParams","GradoEscalaFactory",
	function($scope,$uibModalInstance,SweetAlert,$filter, ngTableParams,gradoEscalaFactory) {
    	
	$scope.codigo=null;
	$scope.nombregrado=null;
	$scope.nombrefuerza=null;
	$scope.grupoocupacional=null;
	$scope.estado=null;
	$scope.edicion=false;
	$scope.objeto={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		
		$scope.data=[];
		gradoEscalaFactory.traer(pagina).then(function(resp){
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
				
		gradoEscalaFactory.traerFiltro(pagina,$scope.codigo,$scope.nombregrado,$scope.nombrefuerza, $scope.grupoocupacional).then(function(resp){
			
			if (resp.meta)
				$scope.data=resp;
		})
	};
	
	$scope.limpiar=function(){

		$scope.codigo=null;
		$scope.nombregrado=null;
		$scope.nombrefuerza=null;
		$scope.grupoocupacional=null;
		$scope.estado=null;
		$scope.consultar();
		
	};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);

	};
	
	$scope.cancelar=function(){
		$uibModalInstance.dismiss();
	};
} ]);
