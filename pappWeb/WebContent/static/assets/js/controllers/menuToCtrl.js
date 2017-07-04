'use strict';
/**
 * controller for angular-menu
 * 
 */
app.controller('MenuToCtrl', [ "$scope","$rootScope", "usuariosFactory", function($scope,$rootScope,usuariosFactory) {
    
	
	$scope.menu=[];
	
	$scope.traerUsuario = function(){
		usuariosFactory.traerUsuarioAuntenticado().then(function(r){
			$rootScope.user.name=r.nombre;
			$scope.menu=r.permisos;
			
		})
	};
	

} ]);