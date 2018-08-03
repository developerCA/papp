'use strict';
/**
 * controller for angular-menu
 * 
 */
app.controller('MenuController', [ "$scope","$rootScope", "seguridadFactory", function($scope,$rootScope,seguridadFactory) {
    
	
	$scope.menu=[];
	
	$scope.armarMenu=function(){
		
		$scope.traerMenu();
	};
	
	$scope.traerMenu = function(){
		seguridadFactory.traerMenu(1).then(function(resp){
			
			if (resp.meta)
			$scope.menu=resp;	
			//console.log($scope.menu);
			
			
		})
	};
	

} ]);