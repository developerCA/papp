'use strict';
/**
 * controller for angular-menu
 * 
 */
app.controller('TipoProductoController', ["$scope", "$rootScope", "SweetAlert", "$filter", "ngTableParams", "TipoProductoFactory", function ($scope, $rootScope, SweetAlert, $filter, ngTableParams, tipoProductoFactory) {

    $scope.nombre = null;
    $scope.estado = null;
    $scope.edicion = false;
    $scope.objeto = {};

    var pagina = 1;

    $scope.consultar = function () {

        $scope.data = [];

        tipoProductoFactory.traerTipos(pagina).then(function (resp) {
            if (resp.meta)
                $scope.data = resp;
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
        tipoProductoFactory.traerTiposFiltro(pagina, $scope.nombre, $scope.estado).then(function (resp) {

            if (resp.meta)

                $scope.data = resp;
        })

    }

    $scope.mayusculas = function () {

        $scope.nombre = $scope.nombre.toUpperCase();

    }

    $scope.limpiar = function () {
        $scope.nombre = null;
        $scope.estado = null;
        $scope.consultar();
    };

    $scope.nuevo = function () {

        $scope.objeto = { id: null };
        $scope.edicion = true;
    }

    $scope.editar = function (id) {
        tipoProductoFactory.traerTipo(id).then(function (resp) {
            $scope.objeto = resp.json.tipoproducto;
            $scope.edicion = true;

        })
    };

    $scope.eliminar = function (id) {

        SweetAlert.swal({
            title: "M�dulo de Tipo Productos",
            text: "Confirma eliminar el registro?",
            type: "warning",
            showCancelButton: true,
            confirmButtonText: "Eliminar",
            cancelButtonText: "Cancelar",
            closeOnConfirm: true
        },
            function () {
                tipoProductoFactory.eliminar(id).then(function (resp) {
                    if (resp.estado) {
                        SweetAlert.swal("M�dulo de Tipo Productos", "Registro eliminado!", "success");
                    } else {
                        SweetAlert.swal("M�dulo de Tipo Productos", resp.mensajes.msg, "error");
                    }
                })
            });
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
                if ($scope.objeto.activo == true) {
                    $scope.objeto.activo = 1;
                } else if ($scope.objeto.activo == false) {
                    $scope.objeto.activo = 0;
                }
                tipoProductoFactory.guardar($scope.objeto).then(function (resp) {
                    if (resp.estado) {
                        form.$setPristine(true);
                        $scope.edicion = false;
                        $scope.objeto = {};
                        $scope.limpiar();
                        SweetAlert.swal("M�dulo de Tipo Productos", "Registro satisfactorio!", "success");
                    } else {
                        SweetAlert.swal("M�dulo de Tipo Productos", resp.mensajes.msg, "error");
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