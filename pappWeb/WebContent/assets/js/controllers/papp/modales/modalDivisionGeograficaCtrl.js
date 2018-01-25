'use strict';

app.controller('ModalDivisionGeograficaController', [ "$scope","$rootScope","$uibModalInstance","pais","provincia","tipo","SweetAlert","$filter", "ngTableParams","divisionGeograficaFactory",
	function($scope,$rootScope,$uibModalInstance,pais,provincia,tipo,SweetAlert,$filter, ngTableParams,divisionGeograficaFactory) {
	
	$scope.nombreFiltro=null;
	$scope.codigoFiltro=null;
	$scope.estadoFiltro=null;
	$scope.edicion=false;
	$scope.objeto={};
	$scope.tipo=null;
	$scope.data=[];
	$scope.cPais=false;
	$scope.cProvincia=false;
	$scope.cCanton=false;

	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		$scope.estadoFiltro="A";
		$scope.filtrar();
/*		divisionGeograficaFactory.traerDivisiones(pagina).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		}) */
	};

	$scope.consultarPaises=function(){
		$scope.cPais=true;
		$scope.data=[];
		$scope.tipo="A";
		divisionGeograficaFactory.traerDivisionesFullFiltro(pagina,'A',null,$scope.nombreFiltro).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}

	$scope.consultarProvincias=function(){
		$scope.cProvincia=true;
		$scope.data=[];
		$scope.tipo="P";
		divisionGeograficaFactory.traerDivisionesFullFiltro(pagina,'P',pais,$scope.nombreFiltro).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}

	$scope.consultarCantones=function(){
		$scope.cCanton=true;
		$scope.data=[];
		$scope.tipo="C";
		divisionGeograficaFactory.traerDivisionesFullFiltro(pagina,'C',provincia,$scope.nombreFiltro).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}

	$scope.filtrar=function(){
		if ($scope.cPais) {
			$scope.consultarPaises();
		} else if ($scope.cProvincia) {
			$scope.consultarProvincias();
		} else if ($scope.cCanton) {
			$scope.consultarCantones();
		} else {
			divisionGeograficaFactory.traerDivisionesFiltro(
				pagina,
				$scope.nombreFiltro,
				$scope.codigoFiltro,
				$scope.estadoFiltro,
				($scope.tipo != null? $scope.tipo: tipo) 
			).then(function(resp){
				if (resp.meta)
					$scope.data=resp;
			})
		}
	}

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
		$scope.nombreFiltro=null;
		if ($scope.cPais) {
			$scope.consultarPaises();
		} else if ($scope.cProvincia) {
			$scope.consultarProvincias();
		} else if ($scope.cCanton) {
			$scope.consultarCantones();
		} else {
			$scope.codigoFiltro=null;
			$scope.estadoFiltro=null;
			$scope.consultar();
		}
	};
	
	$scope.nuevo=function(){
		$scope.objeto={id:null,estado:'A'};
		
		$scope.edicion=true;
	}

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};
	
	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};
}]);
