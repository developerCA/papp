'use strict';
 
app.controller('ModalInstitutoEntidadController', [ "$scope","$uibModalInstance","ejefiscal","SweetAlert","$filter", "ngTableParams","institutoEntidadFactory",
	function($scope,$uibModalInstance,ejefiscal,SweetAlert,$filter, ngTableParams,institutoEntidadFactory) {

		$scope.codigoInstitucionFiltro=null;
		$scope.nombreInstitucionFiltro=null;
		$scope.codigoEntidadFiltro=null;
		$scope.nombreEntidadFiltro=null;
		$scope.ejercicioFiscalFiltro=null;
		$scope.esteadoFiltro=null;
		$scope.edicion=false;
		$scope.objeto={};
		$scope.grupos = [];
		$scope.listaEjercicioFiscal = [];
		$scope.grupo = null;
		$scope.grupoJerarquico = null;
		
		var pagina = 1;
		
		$scope.init = function(){
			institutoEntidadFactory.traerEjerciciosFiscales().then(function(resp){
				if (resp.meta)
					$scope.listaEjercicioFiscal=resp;
			})
			$scope.consultar();
		};

		$scope.consultar=function(){
			$scope.data=[];
			institutoEntidadFactory.traerInstitutoEntidad(
				pagina,
				ejefiscal
			).then(function(resp){
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
			institutoEntidadFactory.traerInstitutoEntidadFiltro(
				pagina,
				$scope.codigoInstitucionFiltro,
				$scope.nombreInstitucionFiltro,
				$scope.codigoEntidadFiltro,
				$scope.nombreEntidadFiltro,
				($scope.ejercicioFiscalFiltro==null? ejefiscal: $scope.ejercicioFiscalFiltro),
				$scope.estadoFiltro
			).then(function(resp){
				console.log(resp);
				if (resp.meta)
					$scope.data=resp;
			})
		}
		
		$scope.limpiar=function(){
			$scope.codigoInstitucionFiltro=null;
			$scope.nombreInstitucionFiltro=null;
			$scope.codigoEntidadFiltro=null;
			$scope.nombreEntidadFiltro=null;
			$scope.ejercicioFiscalFiltro=null;
			$scope.esteadoFiltro=null;
			$scope.consultar();
			
		};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);

	};
	
	$scope.cancelar=function(){
		$uibModalInstance.dismiss();
	};
} ]);