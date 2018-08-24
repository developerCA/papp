'use strict';

app.controller('PermisosController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","permisosFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, permisosFactory) {

	$scope.nombreFiltro=null;
	$scope.ordenFiltro=null;
	
	$scope.edicion=false;
	$scope.guardar=false;
	$scope.nuevo=false;
	$scope.objeto={};
	
    $scope.pagina = 1;
    $scope.aplicafiltro=false;
	
	$scope.consultar=function(){
		permisosFactory.traerPermisos(
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
		$scope.data=[];
		permisosFactory.traerPermisosFiltro(
			$scope.pagina,
			$scope.nombreFiltro
		).then(function(resp){
        	$scope.data = resp.json.result;
            $scope.total = resp.json.total.valor;
		})
	}
	
	$scope.limpiar=function(){
		$scope.nombreFiltro=null;
		$scope.ordenFiltro=null;

		$scope.consultar();
	};
	
	$scope.nuevoR=function(){
		$scope.objeto={
			id:null,
			nombre:null
		};
		
		$scope.edicion=true;
		$scope.guardar=true;
		$scope.nuevo=true;
	}
	
	$scope.editar=function(id){
		permisosFactory.traerPermiso(id).then(function(resp){
			//console.log(resp.json);
			if (resp.estado)
			   $scope.objeto=resp.json.permiso;
			$scope.edicion=true;
			$scope.guardar=true;
			$scope.nuevo=false;;
		})
	};
	
	$scope.abrirPermisoPadre = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalPermisoPadre.html',
			controller : 'PermisoPadreController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.padreid = obj.id;
			$scope.objeto.nombrepadre=obj.nombre;
		}, function() {
			console.log("close modal");
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
            } else {
            	permisosFactory.guardar($scope.objeto).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.objeto={};
	 		             //$scope.limpiar();
	 		             SweetAlert.swal("Permiso!", "Registro registrado satisfactoriamente!", "success");
        			 }else{
	 		             SweetAlert.swal("Permiso!", resp.mensajes.msg, "error");
        			 }
        		})
            }
        },
        reset: function (form) {
            $scope.myModel = angular.copy($scope.master);
            form.$setPristine(true);
            $scope.edicion=false;
    		$scope.guardar=false;
    		$scope.nuevo=false;
        }
    };
} ]);
