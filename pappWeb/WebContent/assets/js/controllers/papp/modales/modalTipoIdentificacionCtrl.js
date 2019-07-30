'use strict';

app.controller('ModalTipoIdentificacionController', ["$scope", "$uibModalInstance", "SweetAlert", "$filter", "ngTableParams", "TipoIdentificacionFactory",
	function ($scope, $uibModalInstance, SweetAlert, $filter, ngTableParams, tipoIdentificacionFactory) {

    $scope.nombre = null;
    $scope.longminima = null;
    $scope.longmaxima= null;
    $scope.usaverifica = 0;
    $scope.edicion = false;
    $scope.detalleNuevo = false;
    $scope.objeto = {};
    $scope.detalles=[];
    var pagina = 1;

    $scope.init = function () {
        $scope.consultar();
    };

    $scope.consultar = function () {

        $scope.data = [];

        tipoIdentificacionFactory.traerTipos(pagina).then(function (resp) {
            if (resp.meta)
                $scope.data = resp;
        });

    };

    $scope.consultarTipo = function () {
        $scope.data = [];

        tipoIdentificacionFactory.traerTiposTipo(pagina, $scope.nombre).then(function (resp) {
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
                $scope.tipos = orderedData.slice(
						(params.page() - 1) * params.count(), params
								.page()
								* params.count());
                params.total(orderedData.length);
                $defer.resolve($scope.tipos);
            }
        });
    });

    $scope.$watch('detalles', function () {

    	
        $scope.detalleParams = new ngTableParams({
            page: 1, // show first page
            count: 5, // count per page
            filter: {}
        }, {
            total: $scope.detalles.length, // length of data
            getData: function ($defer, params) {
                var orderedData = params.filter() ? $filter('filter')(
						$scope.detalles, params.filter()) : $scope.detalles;
                $scope.dets = orderedData.slice(
						(params.page() - 1) * params.count(), params
								.page()
								* params.count());
                params.total(orderedData.length);
                $defer.resolve($scope.dets);
            }
        });
    });

    $scope.filtrar = function () {

        $scope.data = [];
        tipoIdentificacionFactory.traerTiposFiltro(pagina, $scope.nombre).then(function (resp) {
            if (resp.meta)
                $scope.data = resp;
        })

    }

    $scope.limpiar = function () {
        $scope.nombre = null;
        $scope.longminima = null;
        $scope.longmaxima= null;
        $scope.usaverifica= 0;
        $scope.consultar();
    };

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);

	};
	
	$scope.cancelar=function(){
		$uibModalInstance.dismiss();
	};
} ]);
