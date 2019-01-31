'use strict';

app.controller('ContratoController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","contratoFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, contratoFactory) {

	$scope.cProveedorCodigoFiltro = null;
	$scope.cProveedorNombreMostradoFiltro = null;
	$scope.cFechainicialFiltro = null;
	$scope.cEstadoFiltro = null;
	var pagina = 1;

	$scope.cFiltrar = function(){
		contratoFactory.traer(
			pagina,
			$scope.cProveedorCodigoFiltro,
			$scope.cProveedorNombreMostradoFiltro,
			$scope.toStringDate($scope.cFechainicialFiltro),
			$scope.cEstadoFiltro
		).then(function(resp){
			//console.log(resp);
			if (resp.meta)
				$scope.data=resp;
		})
	}

	$scope.cLimpiar = function(){
		$scope.cProveedorCodigoFiltro = null;
		$scope.cProveedorNombreMostradoFiltro = null;
		$scope.cFechainicialFiltro = null;
		$scope.cEstadoFiltro = null;

		$scope.cFiltrar();
	};

	$scope.$watch('data', function() {
		if ($scope.data == undefined) {
			return;
		}
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

	$scope.toStringDate = function(fuente) {
		if (fuente == null) {
			return null;
		}
		try {
			var parts = fuente.toISOString();
			parts = parts.split('T');
			parts = parts[0].split('-');
		} catch (err) {
			return null;
		}
		return parts[2] + "/" + parts[1] + "/" + parts[0]; 
	}

	$scope.popupnpcFechainicio = {
	    opened: false
	};
	$scope.opennpcFechainicio = function() {
	    $scope.popupnpcFechainicio.opened = true;
	}
} ]);