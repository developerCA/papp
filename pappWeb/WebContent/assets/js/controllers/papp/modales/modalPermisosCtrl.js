'use strict';
 
app.controller('ModalPermisosController', [ "$scope","$uibModalInstance","SweetAlert","$filter", "ngTableParams","perfilesFactory",
	function($scope,$uibModalInstance,SweetAlert,$filter, ngTableParams,perfilesFactory) {

	$scope.nombreFiltro=null;
	$scope.idFiltro=null;
	
	$scope.edicion=false;
	$scope.objeto={};
	
    $scope.pagina = 1;

    $scope.consultar = function () {
		$scope.data=[];
		perfilesFactory.traer(
			$scope.pagina
		).then(function(resp){
        	$scope.data = resp.json.result;
            $scope.total = resp.json.total.valor;
		})
    };

    $scope.pageChanged = function() {
        if ($scope.aplicafiltro){
        	$scope.filtrarUnico();
        }else{
        	$scope.consultar();	
        }
    };  

    $scope.filtrar=function(){
    	$scope.pagina=1;
    	$scope.aplicafiltro=true;
    	$scope.filtrarUnico();
    }  

	$scope.filtrarUnico=function(){
		perfilesFactory.traerFiltro(
			$scope.pagina,
			$scope.idFiltro,
			$scope.nombreFiltro
		).then(function(resp){
        	$scope.data = resp.json.result;
            $scope.total = resp.json.total.valor;
        })
    };

	$scope.limpiar=function(){
		$scope.pagina = 1;
		$scope.nombreFiltro=null;
		$scope.idFiltro=null;
		$scope.consultar();
	};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);
	};
	
	$scope.cancelar=function(){
		$uibModalInstance.dismiss();
	};
} ]);