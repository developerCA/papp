/// <reference path="../../factory/unidadesMedidaFactory.js" />
'use strict';
/**
 * controller for angular-menu
 * 
 */
app.controller('UnidadesMedidaController', ["$scope", "$rootScope", "SweetAlert", "$filter", "ngTableParams", "UnidadesMedidaFactory", function ($scope, $rootScope, SweetAlert, $filter, ngTableParams, unidadesMedidaFactory) {

    $scope.nombre = null;
    $scope.estado = null;
    $scope.nombregrupo = null;
    $scope.edicion = false;
    $scope.url = "";
    $scope.objeto = { unidadmedidagrupomedidaid : null };
    
    var pagina = 1;

    $scope.init = function () {

        $scope.grupos = [];

        unidadesMedidaFactory.traerGrupos(pagina).then(function (resp) {
            if (resp.meta)
                $scope.grupos = resp;
            console.log($scope.grupos);
        });

        $scope.consultar();
    };

    $scope.consultar = function () {

        $scope.data = [];

        unidadesMedidaFactory.traerUnidades(pagina).then(function (resp) {
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
        unidadesMedidaFactory.traerUnidadesFiltro(pagina, $scope.nombre, $scope.estado, $scope.nombregrupo).then(function (resp) {
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
        $scope.nombregrupo = null;
        $scope.consultar();
    };

    $scope.nuevo = function () {

        $scope.objeto = { id: null };
        $scope.edicion = true;
    }

    $scope.editar = function (id) {
        
        unidadesMedidaFactory.traerUnidad(id).then(function (resp) {

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
                unidadesMedidaFactory.guardar($scope.objeto).then(function (resp) {
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