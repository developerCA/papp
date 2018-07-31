'use strict';

app.controller('SocioNegocioController', ["$scope", "$rootScope", "SweetAlert", "$filter", "ngTableParams", "sociosNegocioFactory",
	function ($scope, $rootScope, SweetAlert, $filter, ngTableParams, sociosNegocioFactory) {

    $scope.nombre = null;
    $scope.codigo = null;
    $scope.estado = null;
    $scope.edicion = false;
    $scope.objeto = {};
	$scope.data=[];

    $scope.pagina = 1;
    $scope.aplicafiltro=false;

	$scope.consultar=function(){
		sociosNegocioFactory.traer(
			$scope.pagina
		).then(function(resp){
        	$scope.data = resp.json.result;
            $scope.total = resp.json.total.valor;
		})
	};

    $scope.pageChanged = function() {
        if ($scope.aplicafiltro){
        	$scope.filtrarUnico();
        }else{
        	$scope.consultar();	
        }
    };  
    
    $scope.filtrar=function(){
    	$scope.pagina=1;
    	$scope.aplicafiltro=true;
    	$scope.filtrarUnico();
    }  

	$scope.filtrarUnico=function(){
        sociosNegocioFactory.traerFiltro($scope.pagina, $scope.codigo, $scope.nombre, $scope.estado).then(function (resp) {
        	$scope.data = resp.json.result;
            $scope.total = resp.json.total.valor;
        })
    };

    $scope.mayusculas = function () {
        $scope.nombre = $scope.nombre.toUpperCase();
    };

    $scope.limpiar = function () {
        $scope.nombre = null;
        $scope.codigo = null;
        $scope.estado = null;
    	$scope.pagina=1;
    	$scope.aplicafiltro=false;
        $scope.consultar();
    };

    $scope.nuevo = function () {

        $scope.objeto = { id: null,estado:'A' };
        $scope.edicion = true;
    };

    $scope.editar = function (id) {
        
        sociosNegocioFactory.traerObj(id).then(function (resp) {
        	console.log(resp);
            if (resp.estado)
                $scope.objeto = resp.json.socionegocio;
            // Si no se combierten en cadena no los procesa el <SELECT>
            $scope.objeto.esempleado = "" + $scope.objeto.esempleado;
            $scope.objeto.esproveedor = "" + $scope.objeto.esproveedor;
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

                sociosNegocioFactory.guardar($scope.objeto).then(function (resp) {
                    if (resp.estado) {
                        form.$setPristine(true);
                        $scope.edicion = false;
                        $scope.objeto = {};
                        $scope.limpiar();
                        SweetAlert.swal("Socio Negocio", "Registro guardado satisfactoriamente!", "success");

                    } else {
                        SweetAlert.swal("Socio Negocio", resp.mensajes.msg, "error");

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