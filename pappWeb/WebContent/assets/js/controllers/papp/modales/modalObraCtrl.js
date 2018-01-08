'use strict';

app.controller('ModalObraController', ["$scope", "$rootScope", "$uibModalInstance","$filter", "ngTableParams","ObrasFactory",
	function($scope, $rootScope, $uibModalInstance,$filter, ngTableParams,ObrasFactory) {

    $scope.codigo = null;
    $scope.nombre = null;
    $scope.estado = null;
    $scope.edicion = false;
    $scope.objeto = {};

    var pagina = 1;

    $scope.init = function () {
        $scope.consultar();
    };

    $scope.consultar = function () {

        $scope.data = [];

        ObrasFactory.traerObras(pagina,$rootScope.ejefiscal).then(function (resp) {
            if (resp.meta)
                $scope.data = resp;
        });

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
                $scope.obras = orderedData.slice(
						(params.page() - 1) * params.count(), params
								.page()
								* params.count());
                params.total(orderedData.length);
                $defer.resolve($scope.obras);
            }
        });
    });

    $scope.filtrar = function () {

        $scope.data = [];
        ObrasFactory.traerObrasFiltro(pagina, $rootScope.ejefiscal,$scope.codigo, $scope.nombre, $scope.estado).then(function (resp) {
            if (resp.meta)
                $scope.data = resp;
        })

    }

    $scope.mayusculas = function () {

        $scope.nombre = $scope.nombre.toUpperCase();

    }

    $scope.limpiar = function () {
        $scope.codigo = null;
        $scope.nombre = null;
        $scope.estado = null;
        $scope.consultar();
    };

    $scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};

	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};
}]);
