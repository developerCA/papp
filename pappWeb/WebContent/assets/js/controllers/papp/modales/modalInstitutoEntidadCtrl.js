'use strict';
 
app.controller('ModalInstitutoEntidadController', [ "$scope","$uibModalInstance","SweetAlert","$filter", "ngTableParams","institutoEntidadFactory",  function($scope,$uibModalInstance,SweetAlert,$filter, ngTableParams,institutoEntidadFactory) {

		$scope.nombreFiltro=null;
		$scope.siglaFiltro=null;
		$scope.grupoFiltro=null;
		$scope.esteadoFiltro=null;
		$scope.edicion=false;
		$scope.objeto={};
		$scope.grupos = [];
		$scope.grupo = null;
		$scope.grupoJerarquico = null;
		
		var pagina = 1;
		
		$scope.init = function(){
			$scope.traerGrupos();
			$scope.consultar();
		};

		$scope.consultar=function(){
			$scope.data=[];
			institutoEntidadFactory.traerInstitutoEntidad(pagina).then(function(resp){
				if (resp.meta)
					console.log(resp);
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
			institutoEntidadFactory.traerInstitutoEntidadFiltro(pagina,$scope.nombreFiltro,$scope.siglaFiltro, $scope.grupoFiltro,$scope.estadoFiltro).then(function(resp){
				console.clear();
				console.log(resp);
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