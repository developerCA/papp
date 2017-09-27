'use strict';

app.controller('CambiarContrasenaController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","CambiarContrasenaFactory",  function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams,CambiarContrasenaFactory) {
	
	$scope.objeto={};

	$scope.editar=function(){
		console.log("aqui");
		CambiarContrasenaFactory.traerUsuario().then(function(resp){
			console.log(resp);
			if (resp.estado)
			   $scope.objeto=resp.json.menu;
			   $scope.edicion=true;
			   console.log($scope.objeto);
		})
		
	};
/*
	$scope.editar=function(id){
		CambiarContrasenaFactory.traerUsuario(id).then(function(resp){
			console.log(resp);
			if (resp.estado)
			   $scope.objeto=resp.json.menu;
			   $scope.edicion=true;
			   console.log($scope.objeto);
		})
		
	};
*/
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
		                
		            	CambiarContrasenaFactory.guardar($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Cambiar Contraseña!", "Registro registrado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Cambiar Contraseña!", resp.mensajes.msg, "error");
		        				 
		        			 }
		        			
		        		})
		        		
		            }

		        }
    };
} ]);