'use strict';

app.controller('ModalSubItemController', ["$scope", "$rootScope", "npitemid", "$uibModalInstance","$filter", "ngTableParams","SubItemsFactory",
	function($scope, $rootScope, npitemid, $uibModalInstance,$filter, ngTableParams,subitemsFactory) {
	
    $scope.nombre = null;
    $scope.codigo = null;
    $scope.padre = null;
    $scope.tipo = null;
    $scope.estado = null;
    $scope.edicion = false;
    $scope.url = "";
    $scope.objeto = null;

    $scope.pagina = 1;
    $scope.data=[];

    $scope.consultar = function () {
        console.log(npitemid);
        subitemsFactory.traerItemsFiltroCustom(
    		$scope.pagina,
    		null,
    		null,
    		"A",
    		null,
    		$rootScope.ejefiscal,
    		null,
    		null,
    		npitemid
		).then(function (resp) {
			$scope.dataset = resp.json.result;
            $scope.total=resp.json.total.valor;
        })
    };

    $scope.pageChanged = function() {
        if ($scope.aplicafiltro){
        	$scope.filtrar();
        }else{
        	$scope.consultar();	
        }
    };  
    
    $scope.filtrarUnico=function(){
    	$scope.pagina=1;
    	$scope.filtrar();
    }  

    $scope.filtrar = function () {
        subitemsFactory.traerItemsFiltroCustom(
    		$scope.pagina,
    		$scope.codigo,
    		$scope.nombre,
    		"A",
    		$scope.tipo,
    		$rootScope.ejefiscal,
    		($scope.codigoIncop == undefined? $scope.codigoIncop: $scope.codigoIncop.split('.').join('@')),
    		$scope.itemNombre,
    		npitemid
		).then(function (resp) {
			$scope.dataset = resp.json.result;
            $scope.total=resp.json.total.valor;
        })
    };

    $scope.mayusculas = function () {
        $scope.nombre = $scope.nombre.toUpperCase();
    };

    $scope.limpiar = function () {
        $scope.nombre = null;
        $scope.codigo = null;
        $scope.padre = null;
        $scope.tipo = null;
        $scope.estado = null;
        $scope.codigoIncop=null;
        $scope.itemNombre=null;
        $scope.aplicafiltro=false;
        $scope.pagina=1;
        $scope.consultar();
    };
		
	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);
		
	};
	
	$scope.cancelar = function() {
			$uibModalInstance.dismiss('cancel');
	};



}]);
