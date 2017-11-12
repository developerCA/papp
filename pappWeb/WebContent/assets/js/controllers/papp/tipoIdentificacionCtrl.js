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
    $scope.detalleNuevo = false;
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
        $scope.objeto = { id: null,usaverificaop:false };
        $scope.edicion = true;
    }

    $scope.editar = function (id) {

        tipoIdentificacionFactory.traerTipo(id).then(function (resp) {
        	
            if (resp.estado)
            $scope.objeto = resp.json.tipoidentificacion;
            $scope.objeto.usaverificaop= (($scope.objeto.usaverifica == 1) ? true : false);
            
            $scope.detalles = resp.json.details;
            $scope.edicion = true;
            

        })

    };

    $scope.nuevoDetalle = function () {
        $scope.detalleNuevo = true;
    };

    $scope.cancelarDetalle = function () {
        $scope.detalleNuevo = false;
        $scope.nuevoDetalleTipo = '';
    };

    $scope.agregarDetalle = function () {
        var obj = {
            tipo: $scope.nuevoDetalleTipo,
            npnombretipo: $scope.objeto.nombre,
            id: {
                identificacionid: $scope.objeto.id,
                identificaciontipoid : null
            },
            identificacionid: $scope.objeto.id,
            identificaciontipoid: null
        };
        $scope.detalles.push(obj);
        $scope.detalleNuevo = false;
    }

    $scope.removerDetalle=function(index){
		$scope.detalles.splice(index,1);		
	}

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
                $scope.objeto.details = $scope.detalles;
               
                $scope.objeto.usaverifica= (($scope.objeto.usaverificaop == true) ? 1 : 0);
               
                tipoIdentificacionFactory.guardar($scope.objeto).then(function (resp) {
                    if (resp.estado) {
                        form.$setPristine(true);
                        $scope.edicion = false;
                        $scope.objeto = {};
                        $scope.limpiar();
                        SweetAlert.swal("Tipo de Identificación", "Registro guardado satisfactoriamente!", "success");

                    } else {
                        SweetAlert.swal("Tipo de Identificación", resp.mensajes.msg, "error");

                    }

                })

            }

        },
        reset: function (form) {

            $scope.myModel = angular.copy($scope.master);
            form.$setPristine(true);
            $scope.edicion = false;
            $scope.objeto = {};

        },        
    };

}]);