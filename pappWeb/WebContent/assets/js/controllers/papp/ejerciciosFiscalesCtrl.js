'use strict';
/**
 * controller for angular-menu
 * 
 */
app.controller('EjerciciosFiscalesController', [ "$scope","$rootScope","$filter", "ngTableParams","ejercicioFiscalFactory",  function($scope,$rootScope,$filter, ngTableParams,ejercicioFiscalFactory) {
    
	
	
	$scope.consultar=function(){
		
		$scope.data=[];
		ejercicioFiscalFactory.traerEjercicios(1).then(function(resp){
			
			if (resp.meta)
				$scope.data=resp;
			console.log($scope.data);
			
		})
	
	};
	
	$scope.$watch('data', function() {
		
		$scope.tableParams = new ngTableParams({
			page : 1, // show first page
			count : 5, // count per page
			
		}, {
			total : $scope.data.length, // length of data
			getData : function($defer, params) {
				var orderedData = params.filter() ? $filter('filter')(
						$scope.data, params.filter()) : $scope.data;
				$scope.ejercicios = orderedData.slice(
						(params.page() - 1) * params.count(), params
								.page()
								* params.count());
				params.total(orderedData.length);
				$defer.resolve($scope.ejercicios);
			}
		});
	});
	
	

} ]);