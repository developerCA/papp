/// <reference path="../../factory/itemsFactory.js" />
'use strict';
/**
 * controller for angular-menu
 * 
 */
app.controller('ItemsController', ["$scope", "$rootScope", "$uibModal", "SweetAlert", "$filter", "ngTableParams", "ItemsFactory", function ($scope, $rootScope, $uibModal, SweetAlert, $filter, ngTableParams, itemsFactory) {

    $scope.nombre = null;
    $scope.codigo = null;
    $scope.padre = null;
    $scope.tipo = null;
    $scope.estado = null;
    $scope.edicion = false;
    $scope.url = "";
    $scope.objeto = null;
    $scope.data=[];
    var pagina = 1;

  

    $scope.consultar = function () {

        $scope.data = [];

        itemsFactory.traerItems(pagina, $rootScope.ejefiscal).then(function (resp) {
            if (resp.meta)
                $scope.data = resp;
            console.log($scope.data);
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
        itemsFactory.traerItemsFiltro(pagina, $rootScope.ejefiscal, $scope.codigo, $scope.nombre, $scope.estado, $scope.tipo).then(function (resp) {
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

        $scope.objeto = { id: null,estado:'A',itemejerciciofiscalid:$rootScope.ejefiscal };
        $scope.edicion = true;
    }

    $scope.editar = function (id) {

        itemsFactory.traerItem(id).then(function (resp) {
        	console.log(resp);
            if (resp.estado)
                $scope.objeto = resp.json.item;
                console.log($scope.objeto);
                $scope.edicion = true;

        })

    };
    
    $scope.buscarItem=function(){
		var modalInstance = $uibModal.open({
			templateUrl : 'modalItems.html',
			controller : 'ModalItemController',
			size : 'md',
			resolve : {
				tipo : function() {
					return $scope.objeto.tipo;
				}
			}
		});

		modalInstance.result.then(function(obj) {
			$scope.objeto.itempadreid = obj.codigo;
			$scope.objeto.npnombrepadre = obj.nombre;			
			
		}, function() {
			
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
                //OJO
                itemsFactory.guardar($scope.objeto).then(function (resp) {
                    if (resp.estado) {
                        form.$setPristine(true);
                        $scope.edicion = false;
                        $scope.objeto = {};
                        $scope.limpiar();
                        SweetAlert.swal("Item", "Registro guardado satisfactoriamente!", "success");

                    } else {
                        SweetAlert.swal("Item", resp.mensajes.msg, "error");
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