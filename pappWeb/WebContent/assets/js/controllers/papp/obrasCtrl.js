'use strict';

app.controller('ObrasController', ["$scope", "$rootScope", "SweetAlert", "$filter", "ngTableParams", "ObrasFactory",
	function ($scope, $rootScope, SweetAlert, $filter, ngTableParams, obrasFactory) {

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

        obrasFactory.traerObras(pagina,$rootScope.ejefiscal).then(function (resp) {
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
        obrasFactory.traerObrasFiltro(pagina, $rootScope.ejefiscal,$scope.codigo, $scope.nombre, $scope.estado).then(function (resp) {
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

    $scope.nuevo = function () {
        $scope.objeto = { id: null,obraejerciciofiscalid:$rootScope.ejefiscal,estado:'A' };
        $scope.edicion = true;
    }

    $scope.editar = function (id) {

        obrasFactory.traerObra(id).then(function (resp) {

            if (resp.estado)
                $scope.objeto = resp.json.obra;
            $scope.edicion = true;
            console.log($scope.objeto);

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
                obrasFactory.guardar($scope.objeto).then(function (resp) {
                    if (resp.estado) {
                        form.$setPristine(true);
                        $scope.edicion = false;
                        $scope.objeto = {};
                        $scope.limpiar();
                        SweetAlert.swal("Obras", "Registro guardado satisfactoriamente!", "success");

                    } else {
                        SweetAlert.swal("Obras", resp.mensajes.msg, "error");

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