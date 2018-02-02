'use strict';

app.controller('ModalProveedorController', [ "$scope","$rootScope","$uibModalInstance","SweetAlert","$filter","ngTableParams","proveedorFactory",
	function($scope,$rootScope,$uibModalInstance,SweetAlert,$filter,ngTableParams,proveedorFactory) {

	$scope.codigoFiltro=null;
	$scope.nombremostradoFiltro=null;
	$scope.razonsocialFiltro=null;
	$scope.edicion=false;
	$scope.data=[];
    $scope.pagina = 1;
    $scope.aplicafiltro=false;

	$scope.filtrarUnico=function(){
		proveedorFactory.modalTraerFiltro(
			$scope.pagina,
			$scope.codigoFiltro,
			null,
			$scope.nombremostradoFiltro,
			$scope.razonsocialFiltro,
			"A",
			1
		).then(function(resp){
        	$scope.data = resp.json.result;
            $scope.total = resp.json.total.valor;
		})
	}

    $scope.pageChanged = function() {
    	$scope.filtrarUnico();
    };  
    
    $scope.filtrar=function(){
    	$scope.pagina=1;
    	$scope.aplicafiltro=true;
    	$scope.filtrarUnico();
    }  

	$scope.limpiar=function(){
    	$scope.pagina=1;
		$scope.codigoFiltro=null;
		$scope.nombremostradoFiltro=null;
		$scope.razonsocialFiltro=null;
		
	    $scope.aplicafiltro=false;
		$scope.filtrarUnico();
	};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};
	
	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};
}]);
