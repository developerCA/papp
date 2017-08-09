'use strict';
/**
 * controller for angular-menu
 * 
 */
app.controller('TipoIdentificacionController', ["$scope", "$rootScope", "SweetAlert", "$filter", "ngTableParams", "TipoIdentificacionFactory", function ($scope, $rootScope, SweetAlert, $filter, ngTableParams, tipoIdentificacionFactory) {

    $scope.nombre = null;
    $scope.longminima = null;
    $scope.longmaxima= null;
    $scope.usaverifica = 0;
    $scope.edicion = false;
    $scope.objeto = {};

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

    $scope.filtrar = function () {

        $scope.data = [];
        tipoIdentificacionFactory.traerTiposFiltro(pagina, $scope.nombre).then(function (resp) {
            if (resp.meta)
                $scope.data = resp;
        })

    }

    $scope.mayusculas = function () {

        $scope.nombre = $scope.nombre.toUpperCase();

    }

    $scope.limpiar = function () {
        $scope.nombre = null;
        $scope.longminima = null;
        $scope.longmaxima= null;
        $scope.usaverifica= 0;
        $scope.consultar();
    };

    $scope.nuevo = function () {
        $scope.objeto = { id: null };
        $scope.edicion = true;
    }

    $scope.editar = function (id) {

        tipoIdentificacionFactory.traerTipo(id).then(function (resp) {
            if (resp.estado)
                $scope.objeto = resp.json.tipoidentificacion;
            $scope.edicion = true;

        })

    };

    $scope.form = {

        submit: function (form) {

            var firstError = null;
            if (form.$invalid) {

                var field = null, firstError = null;
                for (field in form) {
                    if (field[0] != '$') {
                        if (firstError === null && !form[field].$valid) {
                            firstError = form[field].$name;
                        }

                        if (form[field].$pristine) {
                            form[field].$dirty = true;
                        }
                    }
                }

                angular.element('.ng-invalid[name=' + firstError + ']').focus();
                return;

            } else {
                console.clear();
                console.log($scope.objeto);
                tipoIdentificacionFactory.guardar($scope.objeto).then(function (resp) {
                    if (resp.estado) {
                        form.$setPristine(true);
                        $scope.edicion = false;
                        $scope.objeto = {};
                        $scope.limpiar();
                        SweetAlert.swal("Tipo de Identificaci�n", "Registro satisfactorio!", "success");

                    } else {
                        SweetAlert.swal("Tipo de Identificaci�n", resp.mensajes.msg, "error");

                    }

                })

            }

        },
        reset: function (form) {

            $scope.myModel = angular.copy($scope.master);
            form.$setPristine(true);
            $scope.edicion = false;
            $scope.objeto = {};

        }
    };

}]);