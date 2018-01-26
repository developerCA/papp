'use strict';

app.controller('ModalMetasController', [ "$scope","$uibModalInstance","ejefiscal","$uibModal","SweetAlert","$filter", "ngTableParams","MetasFactory",
	function($scope,$uibModalInstance,ejefiscal,$uibModal,SweetAlert,$filter, ngTableParams, MetasFactory) {

	$scope.codigoFiltro=null;
	$scope.descripcionFiltro=null;
	$scope.codigoPFiltro=null;
	$scope.descripcionPFiltro=null;
	$scope.estadoFiltro=null;

	$scope.edicion=false;
	$scope.guardar=false;
	$scope.objeto={};
	$scope.objetolista={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		//console.log("Consultar de ModalMetasController");
		$scope.data=[];
		MetasFactory.traerMetas(
			pagina,
			ejefiscal
		).then(function(resp){
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
		MetasFactory.traerMetasFiltro(
			pagina,
			ejefiscal,
			$scope.codigoFiltro,
			$scope.descripcionFiltro,
			$scope.codigoPFiltro,
			$scope.descripcionPFiltro,
			$scope.estadoFiltro
		).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}

	$scope.limpiar=function(){
		$scope.codigoFiltro=null;
		$scope.descripcionFiltro=null;
		$scope.codigoPFiltro=null;
		$scope.descripcionPFiltro=null;
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
