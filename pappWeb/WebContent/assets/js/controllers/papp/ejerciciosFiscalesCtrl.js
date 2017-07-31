'use strict';
/**
 * controller for angular-menu
 * 
 */
app.controller('EjerciciosFiscalesController', [ "$scope","$rootScope","SweetAlert","$filter", "ngTableParams","ejercicioFiscalFactory",  function($scope,$rootScope,SweetAlert,$filter, ngTableParams,ejercicioFiscalFactory) {
    
	$scope.anioFiltro=null;
	$scope.estadoFiltro=null;
	$scope.edicion=false;
	$scope.objeto={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		
		$scope.data=[];
		ejercicioFiscalFactory.traerEjercicios(pagina).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	
	};
	
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
	
	$scope.nuevo=function(){
		$scope.objeto={id:null};
		
		$scope.edicion=true;
	}
	
	$scope.editar=function(id){
		ejercicioFiscalFactory.traerEjercicio(id).then(function(resp){
			
			if (resp.estado)
				$scope.objeto=resp.json.ejerciciofiscal;
				$scope.objeto.cerrado=$scope.objeto.cerrado.toString();
				$scope.edicion=true;

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
		                
		            	ejercicioFiscalFactory.guardarEjercicio($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Ejercicio Fiscal!", "Registro registrado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Ejercicio Fiscal!", resp.mensajes.msg, "error");
		        				 
		        			 }
		        			
		        		})
		        		
		            }

		        },
		        reset: function (form) {

		            $scope.myModel = angular.copy($scope.master);
		            form.$setPristine(true);
		            $scope.edicion=false;
		            $scope.objeto={};

		        }
    };
	 
	 

	

} ]);