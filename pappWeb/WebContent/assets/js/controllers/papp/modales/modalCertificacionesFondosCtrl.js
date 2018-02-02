'use strict';

app.controller('ModalCertificacionesFondosController', [ "$scope","$rootScope","$uibModalInstance","SweetAlert","$filter","unidadid","ngTableParams","certificacionesFondosFactory",
	function($scope,$rootScope,$uibModalInstance,SweetAlert,$filter,unidadid,ngTableParams,certificacionesFondosFactory) {

	$scope.codigoFiltro=null;
	$scope.descripcionFiltro=null;
	$scope.objeto={};
	$scope.detalles=[];
	$scope.data=[];

	var pagina = 1;

	$scope.filtrar=function(){
		certificacionesFondosFactory.modalTraerFiltro(
			pagina,
			$rootScope.ejefiscal,
			unidadid,
			$scope.codigoFiltro,
			$scope.descripcionFiltro
		).then(function(resp){
			//console.log(resp);
        	$scope.data = resp.result;
            $scope.total = resp.total.valor;
		})
	}
	
	$scope.limpiar=function(){
		$scope.codigoFiltro=null;
		$scope.descripcionFiltro=null;
		
		$scope.filtrar();
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

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);
		return;
		certificacionesFondosFactory.traerCertificacionesFondosEditar(
			obj.id
		).then(function(resp){
			//console.log(resp);
			obj.certificacion = resp.json.certificacion;
			obj.certificacionlineas = resp.json.certificacionlineas;
			$uibModalInstance.close(obj);		
		})
	};
	
	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};
}]);
