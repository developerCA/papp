'use strict';

app.controller('CambiarContrasenaUnicoController', [ "$scope","$rootScope","$location","$uibModal","SweetAlert","$filter", "ngTableParams","CambiarContrasenaUnicoFactory",  function($scope,$rootScope,$location,$uibModal,SweetAlert,$filter, ngTableParams,CambiarContrasenaUnicoFactory) {
	$scope.objeto={};

	$scope.editar=function(){
		CambiarContrasenaUnicoFactory.traerUsuario().then(function(resp){
			//console.log("AQUIIIIII");
			//console.log(resp);
			if (resp.ususario)
			   $scope.objeto=resp.ususario;
			   $scope.objeto.clave=''; 
			   $scope.edicion=true;
			   //console.log($scope.objeto);
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
            	CambiarContrasenaUnicoFactory.guardar($scope.objeto).then(function(resp){
					//console.log(resp);
            		if (resp.estado){
        				 SweetAlert.swal(
        						 "Cambiar Contrasena!",
        						 "Registro registrado satisfactoriamente!",
        						 "success"
						 );
        				 $location.path("/index");
        			 }else{
	 		             SweetAlert.swal(
	 		            		 "Cambiar Contrasena!",
	 		            		 resp.mensajes.msg,
	 		            		 "error"
	            		 );
        			 }
        		})
            }
        }
    };
} ]);
