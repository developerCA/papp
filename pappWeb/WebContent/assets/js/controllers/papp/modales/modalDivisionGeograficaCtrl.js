'use strict';

app.controller('ModalDivisionGeograficaController', [ "$scope","$rootScope","$uibModalInstance","pais","provincia","tipo","SweetAlert","$filter", "ngTableParams","divisionGeograficaFactory",
	function($scope,$rootScope,$uibModalInstance,pais,provincia,tipo,SweetAlert,$filter, ngTableParams,divisionGeograficaFactory) {
	
	$scope.nombreFiltro=null;
	$scope.codigoFiltro=null;
	$scope.estadoFiltro=null;
	$scope.edicion=false;
	$scope.objeto={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		
		$scope.data=[];
		divisionGeograficaFactory.traerDivisiones(pagina).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
			
		})
	
	};

	$scope.consultarPaises=function(){
		
		$scope.data=[];
		divisionGeograficaFactory.traerDivisionesFullFiltro(pagina,'A',null).then(function(resp){
			
			if (resp.meta)
				$scope.data=resp;
		})
	}

	$scope.consultarProvincias=function(){
		$scope.data=[];
		divisionGeograficaFactory.traerDivisionesFullFiltro(pagina,'P',pais).then(function(resp){
			
			if (resp.meta)
				$scope.data=resp;
		})
	}

	$scope.consultarCantones=function(){
		$scope.data=[];
		divisionGeograficaFactory.traerDivisionesFullFiltro(pagina,'C',provincia).then(function(resp){
			
			if (resp.meta)
				$scope.data=resp;
		})
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
	
	
	$scope.filtrar=function(){
		//console.log("tipo:'"+tipo+"'");
		$scope.data=[];
		divisionGeograficaFactory.traerDivisionesFiltro(pagina,$scope.nombreFiltro,$scope.codigoFiltro,$scope.estadoFiltro,tipo).then(function(resp){
			
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
