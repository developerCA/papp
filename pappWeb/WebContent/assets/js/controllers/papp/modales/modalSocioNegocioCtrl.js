'use strict';

app.controller('ModalSocioNegocioController', ["$scope", "$rootScope", "$uibModalInstance", "$filter", "ngTableParams", "sociosNegocioFactory",
	function ($scope, $rootScope, $uibModalInstance, $filter, ngTableParams, sociosNegocioFactory) {

    $scope.nombre = null;
    $scope.codigo = null;
    $scope.estado = null;
    $scope.edicion = false;
    $scope.objeto = {};
    $scope.data = [];

    var pagina = 1;

    $scope.consultar = function () {

        $scope.data = [];

        sociosNegocioFactory.traer(pagina, $rootScope.ejefiscal).then(function (resp) {
            if (resp.meta)
                $scope.data = resp;
               //console.log($scope.data);
        })

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

    $scope.filtrar = function () {

        $scope.data = [];
        sociosNegocioFactory.traerFiltro(pagina, $scope.codigo, $scope.nombre, $scope.estado).then(function (resp) {

            if (resp.meta)

                $scope.data = resp;
        })

    };

    $scope.mayusculas = function () {

        $scope.nombre = $scope.nombre.toUpperCase();

    };

    $scope.limpiar = function () {
        $scope.nombre = null;
        $scope.codigo = null;
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
