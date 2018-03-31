'use strict';

app.controller('ModalSubActividadController', [ "$scope","$uibModalInstance","$uibModal","SweetAlert","$filter", "ngTableParams","formulacionEstrategicaFactory",
	function($scope,$uibModalInstance,$uibModal,SweetAlert,$filter, ngTableParams, formulacionEstrategicaFactory) {

	$scope.codigo=null;
	$scope.nombre=null;

	$scope.edicion=false;
	$scope.guardar=false;
	$scope.objeto={};
	$scope.objetolista={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		formulacionEstrategicaFactory.traerSubActividad(
    		node.npNivelid,
    		$rootScope.ejefiscal
		).then(function(resp){
			console.log(resp);
			for (var i = 0; i < resp.length; i++) {
				resp[i].nodeTipo = "SA";
				//resp[i].nodePadre = node;
			}
			var nodes=JSON.parse(JSON.stringify(resp).split('"descripcion":').join('"title":'));
			node.nodes=nodes;
			for (var i = 0; i < node.nodes.length; i++) {
				node.nodes[i].nodePadre = node;
			}
		});

		
		subactividadFactory.traerPrograma(pagina).then(function(resp){
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
		subactividadFactory.traerProgramaFiltro(pagina,$scope.codigo,$scope.nombre).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.codigo=null;
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
