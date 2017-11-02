'use strict';

app.controller('ModalEjerciciosFiscalesController', [ "$scope","$uibModalInstance","$location","SweetAlert","$filter", "ngTableParams","ejercicioFiscalFactory",
	function($scope,$uibModalInstance,$location,SweetAlert,$filter, ngTableParams,ejercicioFiscalFactory) {
	    
		$scope.anioFiltro=null;
		$scope.estadoFiltro=null;
		$scope.edicion=false;
		$scope.objeto={};
		$scope.ejercicioSistema;
		var pagina = 1;
		
		$scope.consultar=function(){
			$scope.data=[];
			ejercicioFiscalFactory.traerEjercicios(pagina).then(function(resp){
				if (resp.meta)
					$scope.data=resp;
			})
		
		};
		
		$scope.iniciaAplicacion=function(){
			$scope.data=[];
			$scope.ejerciciosFiscales=[];
			ejercicioFiscalFactory.traerEjercicios(pagina).then(function(resp){
				if (resp.meta)
					$scope.ejerciciosFiscales=resp;
				    $rootScope.ejefiscal=$scope.ejerciciosFiscales[0].id;
				    $scope.ejercicioSistema= $rootScope.ejefiscal;
					    
			});
		
		};
		
		$scope.cambiarEjercicio=function(){
			
			 SweetAlert.swal({
		            title: "Ejericio Fiscal",
		            text: "Esta seguro de cambiar el ejercicio fiscal del sistema?",
		            type: "warning",
		            showCancelButton: true,
		            confirmButtonColor: "#DD6B55",
		            confirmButtonText: "Si!"
		        }, function () {
		        	$rootScope.ejefiscal=$scope.ejercicioSistema;
		        	
		            SweetAlert.swal({
		                title: "Ejercicio Fiscal se ha cambiado",
		                confirmButtonColor: "#007AFF"
		            });
		            
		            $location.path( "/index" );
		        });
			 
			
			
		}
		
		
		$scope.$watch('data', function() {
			
			$scope.tableParams = new ngTableParams({
				page : 1, // show first page
				count : 5, // count per page
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
		
		
		$scope.filtrar=function(){
			
			$scope.data=[];
			ejercicioFiscalFactory.traerEjerciciosFiltro(pagina,$scope.anioFiltro,$scope.estadoFiltro).then(function(resp){
				
				if (resp.meta)
					
					$scope.data=resp;
			})
			
		}
		
		$scope.limpiar=function(){
			$scope.anioFiltro=null;
			$scope.estadoFiltro=null;
			$scope.consultar();
			
		};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};

	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};

}]);
