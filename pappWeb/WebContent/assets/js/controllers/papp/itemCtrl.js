/// <reference path="../../factory/itemsFactory.js" />
'use strict';
/**
 * controller for angular-menu
 * 
 */
app.controller('ItemsController', ["$scope", "$rootScope", "SweetAlert", "$filter", "ngTableParams", "ItemsFactory", function ($scope, $rootScope, SweetAlert, $filter, ngTableParams, itemsFactory) {

    $scope.nombre = null;
    $scope.codigo = null;
    $scope.padre = null;
    $scope.tipo = null;
    $scope.estado = null;
    $scope.edicion = false;
    $scope.url = "";
    $scope.objeto = null;

    var pagina = 1;

    $scope.init = function () {

        $scope.items = [];
        itemsFactory.traerItems(pagina, $rootScope.ejefiscal).then(function (resp) {
            if (resp.meta)
                $scope.items = resp;
            console.log($scope.items);
        });

        $scope.consultar();
    };

    $scope.consultar = function () {

        $scope.data = [];

        itemsFactory.traerItems(pagina, $rootScope.ejefiscal).then(function (resp) {
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
        itemsFactory.traerItemsFiltro(pagina, $scope.codigo, $scope.nombre, $scope.estado, $scope.tipo).then(function (resp) {
            if (resp.meta)
                $scope.data = resp;
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
        $scope.consultar();
    };

    $scope.nuevo = function () {

        $scope.objeto = { id: null };
        $scope.edicion = true;
    }

    $scope.editar = function (id) {

        itemsFactory.traerItem(id).then(function (resp) {

            if (resp.estado)
                $scope.objeto = resp.json.unidadmedida;
            console.log("*************");
            console.log($scope.objeto.unidadmedidagrupomedidaid);
            console.log("*************");
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
                //OJO
                $scope.objeto.unidadmedidagrupomedidaid = $scope.objeto.unidadmedidagrupomedidaid.id;
                itemsFactory.guardar($scope.objeto).then(function (resp) {
                    if (resp.estado) {
                        form.$setPristine(true);
                        $scope.edicion = false;
                        $scope.objeto = {};
                        $scope.limpiar();
                        SweetAlert.swal("Unidad de Medida", "Registro satisfactorio!", "success");

                    } else {
                        SweetAlert.swal("Unidad de Medida", resp.mensajes.msg, "error");

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