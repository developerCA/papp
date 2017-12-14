'use strict';

app.controller('ItemsController', ["$scope", "$rootScope", "$uibModal", "SweetAlert", "$filter", "ngTableParams", "ItemsFactory",
	function ($scope, $rootScope, $uibModal, SweetAlert, $filter, ngTableParams, itemsFactory) {

    $scope.nombre = null;
    $scope.codigo = null;
    $scope.padre = null;
    $scope.tipo = null;
    $scope.estado = null;
    $scope.edicion = false;
    $scope.codigopadre = null;
    $scope.url = "";
    $scope.objeto = null;
    $scope.idpadreFiltro
    $scope.data=[];
    $scope.pagina = 1;

    $scope.consultar = function () {
        $scope.dataset = [];
        
        itemsFactory.traerItemsCustom($scope.pagina, $rootScope.ejefiscal).then(function (resp) {
            $scope.dataset = resp.json.result;
            $scope.total=resp.json.total.valor;
            console.log($scope.total);
        });
    };

    $scope.pageChanged = function() {
        //console.log($scope.pagina);
        if ($scope.aplicafiltro){
        	$scope.filtrar();
        } else {
        	$scope.consultar();	
        }
    };       
 
    $scope.filtrarUnico=function(){
    	$scope.pagina=1;
    	$scope.filtrar();
    }  

    $scope.filtrar = function () {
        $scope.dataset = [];
        $scope.aplicafiltro=true;
        itemsFactory.traerItemsFiltroCustom(
        		$scope.pagina, $rootScope.ejefiscal, $scope.codigo,
        		$scope.nombre, $scope.estado,
        		$scope.tipo, $scope.codigopadre, null
		).then(function (resp) {
           
			$scope.dataset = resp.json.result;
            $scope.total=resp.json.total.valor;
            
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
        $scope.codigopadre = null;
        $scope.idpadreFiltro=null;
        $scope.aplicafiltro=false;
        $scope.pagina=1;
        $scope.consultar();
    };

    $scope.nuevo = function () {

        $scope.objeto = { id: null,estado:'A',itemejerciciofiscalid:$rootScope.ejefiscal };
        $scope.edicion = true;
    }

    $scope.editar = function (id) {

        itemsFactory.traerItem(id).then(function (resp) {
        	//console.log(resp);
            if (resp.estado)
                $scope.objeto = resp.json.item;
                console.log(resp.json);
                $scope.edicion = true;
        })

    };

    $scope.buscarItem=function(){
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalItems.html',
			controller : 'ModalItemController',
			size : 'lg'
		});

		modalInstance.result.then(function(obj) {
			$scope.objeto.itempadreid = obj.id;
			$scope.objeto.npcodigopadre = obj.codigo;			
			$scope.objeto.npnombrepadre = obj.nombre;			
		}, function() {
		});
	};

    $scope.buscarItemFiltro=function(){
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalItems.html',
			controller : 'ModalItemController',
			size : 'lg'
		});

		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.codigopadre = obj.codigo;
			$scope.idpadreFiltro=obj.id;
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