'use strict';

app.controller('ModalItemController', ["$scope", "$rootScope", "$uibModalInstance","$filter", "ngTableParams","ItemsFactory",
	function($scope, $rootScope, $uibModalInstance,$filter, ngTableParams,itemsFactory) {
	
    $scope.nombre = null;
    $scope.codigo = null;
    $scope.padre = null;
    $scope.tipo = null;
    $scope.estado = null;
    $scope.edicion = false;
    $scope.codigopadre = null;
    $scope.url = "";
    $scope.objeto = null;
    $scope.idpadreFiltro
    $scope.data=[];
    $scope.pagina = 1;

    $scope.inicio = function () {
        if ($scope.$resolve.tipo !== undefined) {
        	$scope.tipo = $scope.$resolve.tipo
        }
        if ($scope.$resolve.estado !== undefined) {
        	$scope.estado = $scope.$resolve.estado
        }
        //console.log($scope.$resolve);
        $scope.consultar();
    }

    $scope.consultar = function () {
    	$scope.filtrar();
/*
        $scope.dataset = [];
        
        itemsFactory.traerItemsCustom($scope.pagina, $rootScope.ejefiscal).then(function (resp) {
            $scope.dataset = resp.json.result;
            $scope.total=resp.json.total.valor;
            console.log($scope.total);
        });
*/
    };

    $scope.pageChanged = function() {
        //console.log($scope.pagina);
        if ($scope.aplicafiltro){
        	$scope.filtrar();
        } else {
        	$scope.consultar();	
        }
    };       
 
    $scope.filtrarUnico=function(){
    	$scope.pagina=1;
    	$scope.filtrar();
    }  

    $scope.filtrar = function () {
        $scope.dataset = [];
        $scope.aplicafiltro=true;
        itemsFactory.traerItemsFiltroCustom(
        		$scope.pagina,
        		$rootScope.ejefiscal,
        		$scope.codigo,
        		$scope.nombre,
        		$scope.estado,
        		$scope.tipo,
        		$scope.codigopadre,
        		null
		).then(function (resp) {
           
			$scope.dataset = resp.json.result;
            $scope.total=resp.json.total.valor;
            
        })
    }

    $scope.mayusculas = function () {
        $scope.nombre = $scope.nombre.toUpperCase();
    }

    $scope.limpiar = function () {
        $scope.nombre = null;
        $scope.codigo = null;
        $scope.padre = null;
        $scope.tipo = null;
        $scope.estado = null;
        $scope.codigopadre = null;
        $scope.idpadreFiltro=null;
        $scope.aplicafiltro=false;
        $scope.pagina=1;
        $scope.inicio();
    };

    $scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};

	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};
}]);
