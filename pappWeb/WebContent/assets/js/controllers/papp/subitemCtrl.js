app.controller('SubItemController', ["$scope", "$rootScope", "$uibModal", "SweetAlert", "$filter", "ngTableParams", "SubItemsFactory",
	function ($scope, $rootScope, $uibModal, SweetAlert, $filter, ngTableParams, subitemsFactory) {

    $scope.nombre = null;
    $scope.codigo = null;
    $scope.padre = null;
    $scope.tipo = null;
    $scope.estado = null;
    $scope.edicion = false;
    $scope.url = "";
    $scope.objeto = null;

    var pagina = 1;
    $scope.data=[];

   

    $scope.consultar = function () {

        $scope.data = [];

        subitemsFactory.traerItems(pagina, $rootScope.ejefiscal).then(function (resp) {
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
        subitemsFactory.traerItemsFiltro(pagina, $scope.codigo, $scope.nombre, $scope.estado, $scope.tipo,$rootScope.ejefiscal,$scope.codigoIncop,$scope.itemNombre).then(function (resp) {
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
        $scope.padre = null;
        $scope.tipo = null;
        $scope.estado = null;
        $scope.codigoIncop=null;
        $scope.itemNombre=null;
        $scope.consultar();
    };

    $scope.nuevo = function () {

        $scope.objeto = { id: null ,estado:'A'};
        $scope.edicion = true;
    };

    $scope.editar = function (id) {

    	subitemsFactory.traerItem(id).then(function (resp) {
        	if (resp.estado)
                $scope.objeto = resp.json.subitem;
            	$scope.edicion = true;
            	console.log($scope.objeto);
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
			console.log(obj);
			$scope.objeto.npitemcodigo = obj.codigo;
			$scope.objeto.npitemnombre = obj.nombre;		
			$scope.objeto.subitemitemid= obj.id;
			
		}, function() {
			
		});

	};
	
	$scope.buscarUnidadMedida = function(){
		var modalInstance = $uibModal.open({
			templateUrl : 'modalUnidades.html',
			controller : 'ModalUnidadMedidaController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.subitemunidadmedidaid = obj.id;
			$scope.objeto.npunidadnombre = obj.nombre;		
			
			
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
            	console.log($scope.objeto);
            	subitemsFactory.guardar($scope.objeto).then(function (resp) {
                    if (resp.estado) {
                        form.$setPristine(true);
                        $scope.edicion = false;
                        $scope.objeto = {};
                        $scope.limpiar();
                        SweetAlert.swal("SubItem", "Registro guardado satisfactioriamente!", "success");

                    } else {
                        SweetAlert.swal("SubItem", resp.mensajes.msg, "error");
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