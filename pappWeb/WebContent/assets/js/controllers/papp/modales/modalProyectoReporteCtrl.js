'use strict';

app.controller('ModalProyectoReporteController', [ "$scope","$uibModalInstance","$uibModal","ejefiscal","SweetAlert","$filter", "ngTableParams","ProyectoReporteFactory",
	function($scope,$uibModalInstance,$uibModal,ejefiscal,SweetAlert,$filter, ngTableParams, ProyectoReporteFactory) {

	$scope.codigo=null;
	$scope.nombre=null;
	$scope.npcodigoprograma=null;
	$scope.npnombreprograma=null;
	$scope.npcodigosubprograma=null;
	$scope.npunidad=null;
	$scope.npprogramaid=null;
	$scope.padre=null;

	$scope.edicion=false;
	$scope.guardar=false;
	$scope.objeto={};
	$scope.objetolista={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		ProyectoReporteFactory.traerProyectoFiltro(
			pagina,
			ejefiscal,
			$scope.codigo,
			$scope.nombre,
			$scope.npcodigoprograma,
			$scope.npnombreprograma,
			$scope.npcodigosubprograma,
			$scope.npunidad,
			$scope.npprogramaid,
			$scope.padre
		).then(function(resp){
			//console.log(resp);
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
	
	$scope.limpiar=function(){
		$scope.programaCodigoFiltro=null;
		$scope.programaNombreFiltro=null;
		$scope.proyectoCodigoFiltro=null;
		$scope.proyectoNombreFiltro=null;
		$scope.subprogramaCodigoFiltro=null;

		$scope.consultar();
	};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};

	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};

}]);
