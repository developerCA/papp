'use strict';

app.controller('CambiarContrasenaController', [ "$scope","$rootScope","$location","$uibModal","SweetAlert","$filter", "ngTableParams","CambiarContrasenaFactory",  function($scope,$rootScope,$location,$uibModal,SweetAlert,$filter, ngTableParams,CambiarContrasenaFactory) {
	
	$scope.objeto={};

	$scope.editar=function(){
		CambiarContrasenaFactory.traerUsuario().then(function(resp){
			//console.log("AQUIIIIII");
			//console.log(resp);
			if (resp.ususario)
			   $scope.objeto=resp.ususario;
			   $scope.objeto.clave=''; 
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
							//console.log(resp);
		            		if (resp.estado){
		        				 SweetAlert.swal("Cambiar Contrasena!", "Registro registrado satisfactoriamente!", "success");
		        				 $location.path("logout");
		        			 }else{
			 		             SweetAlert.swal("Cambiar Contrasena!", resp.mensajes.msg, "error");
		        				 
		        			 }
		        			
		        		})
		        		
		            }

		        }
    };
} ]);