'use strict';

app.controller('ModalClaseDocumentoController', [ "$scope","$rootScope","$uibModalInstance","SweetAlert","$filter", "ngTableParams","claseDocumentoFactory",
	function($scope,$rootScope,$uibModalInstance,SweetAlert,$filter, ngTableParams,claseDocumentoFactory) {

	$scope.nombreFiltro=null;
	$scope.codigoFiltro=null;
	$scope.estadoFiltro=null;
	$scope.edicion=false;
	$scope.objeto={};
	$scope.detalles=[];
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		//console.log("Eje.Fis.:"+$rootScope.ejefiscal);
		claseDocumentoFactory.traerClases(
			$rootScope.ejefiscal
		).then(function(resp){
			//console.log(resp["0"].data);
			//console.log(resp["0"].data.claseregistrocmcdocumento);
			if (resp.meta)
				$scope.data=resp["0"].data.tipodocumentoclasedocumento;
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
		claseDocumentoFactory.traerClasesFiltro(pagina,$rootScope.ejefiscal,$scope.nombreFiltro,$scope.codigoFiltro,$scope.estadoFiltro).then(function(resp){
			
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
	
	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};
}]);
