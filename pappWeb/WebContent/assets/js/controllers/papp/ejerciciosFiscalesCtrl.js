'use strict';
/**
 * controller for angular-menu
 * 
 */
app.controller('EjerciciosFiscalesController', [ "$scope","$rootScope","$aside","$filter", "ngTableParams","ejercicioFiscalFactory",  function($scope,$rootScope,$aside,$filter, ngTableParams,ejercicioFiscalFactory) {
    
	
	
	$scope.consultar=function(){
		
		$scope.data=[];
		$scope.txtbusqueda="";
		
		ejercicioFiscalFactory.traerEjercicios(1).then(function(resp){
			
			if (resp.meta)
				$scope.data=resp;
			
			
		})
	
	};
	
	$scope.$watch('data', function() {
		
		$scope.tableParams = new ngTableParams({
			page : 1, // show first page
			count : 8, // count per page
			filter: {} 	
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
	
	
	$scope.busqueda=function(){
	
		var myAside = $aside({title: 'My Title', content: 'My Content', show: true});

		  // Pre-fetch an external template populated with a custom scope
		  var myOtherAside = $aside({scope: $scope, template: 'aside/view/papp/filtros.html'});
		  // Show when some event occurs (use $promise property to ensure the template has been loaded)
		  myOtherAside.$promise.then(function() {
		    myOtherAside.show();
		  })
		
	}
	

} ]);