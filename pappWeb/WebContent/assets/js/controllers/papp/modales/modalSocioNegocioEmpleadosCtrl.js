'use strict';

app.controller('ModalSocioNegocioEmpleadosController', ["$scope", "$rootScope", "$uibModalInstance", "$filter", "ngTableParams", "sociosNegocioFactory",
	function ($scope, $rootScope, $uibModalInstance, $filter, ngTableParams, sociosNegocioFactory) {

	$scope.soloEmpleados = true;
    $scope.nombre = null;
    $scope.codigo = null;
    $scope.estado = null;
    $scope.edicion = false;
    $scope.objeto = {};
    $scope.data = [];

    $scope.pagina = 1;
    $scope.aplicafiltro=false;

    $scope.consultar = function () {
        sociosNegocioFactory.traerFiltroEmpleados(
    		$scope.pagina,
    		$rootScope.ejefiscal,
    		null,
    		null,
    		null
		).then(function (resp) {
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
        sociosNegocioFactory.traerFiltroEmpleados(
    		$scope.pagina,
    		$rootScope.ejefiscal,
    		$scope.codigo,
    		$scope.nombre,
    		$scope.estado
		).then(function (resp) {
        	$scope.data = resp.json.result;
            $scope.total = resp.json.total.valor;
        })
    };

    $scope.mayusculas = function (buscar) {
        $scope.nombre = $scope.nombre.toUpperCase();
        if (buscar === true) {
        	$scope.filtrar();
        }
    };

    $scope.limpiar = function () {
        $scope.nombre = null;
        $scope.codigo = null;
        $scope.estado = null;
        $scope.aplicafiltro=false;
        $scope.consultar();
    };

    $scope.$watch('data', function () {
        $scope.tableParams = new ngTableParams({
            page: 1, // show first page
            count: 5, // count per page
            filter: {}
        }, {
            total: $scope.data.length, // length of data
            getData: function ($defer, params) {
                var orderedData = params.filter() ? $filter('filter')(
						$scope.data, params.filter()) : $scope.data;
                $scope.gruposMedida = orderedData.slice(
						(params.page() - 1) * params.count(), params
								.page()
								* params.count());
                params.total(orderedData.length);
                $defer.resolve($scope.gruposMedida);
            }
        });
    });

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};
	
	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};
}]);
