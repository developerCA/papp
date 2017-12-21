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

    $scope.pagina = 1;
    $scope.data=[];

    $scope.consultar = function () {
        $scope.data = [];
        subitemsFactory.traerItemsCustom(
    		$scope.pagina,
    		$rootScope.ejefiscal
		).then(function (resp) {
        	$scope.dataset = resp.json.result;
            $scope.total = resp.json.total.valor;
            console.log(resp);
        });
    };

    $scope.pageChanged = function() {
        if ($scope.aplicafiltro){
        	$scope.filtrar();
        }else{
        	$scope.consultar();	
        }
    };  
    
    $scope.filtrarUnico=function(){
    	$scope.pagina=1;
    	$scope.filtrar();
    }  
      
    $scope.filtrar = function () {
        subitemsFactory.traerItemsFiltroCustom(
    		$scope.pagina,
    		$scope.codigo,
    		$scope.nombre,
    		$scope.estado,
    		$scope.tipo,
    		$rootScope.ejefiscal,
    		$scope.codigoIncop,
    		$scope.itemNombre
		).then(function (resp) {
			$scope.dataset = resp.json.result;
            $scope.total=resp.json.total.valor;
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
        $scope.aplicafiltro=false;
        $scope.pagina=1;
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
			templateUrl : 'assets/views/papp/modal/modalItems.html',
			controller : 'ModalItemController',
			size : 'lg',
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
			templateUrl : 'assets/views/papp/modal/modalUnidadMedida.html',
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