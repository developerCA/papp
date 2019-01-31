'use strict';

app.controller('EscalaRemuneracionController', [ "$scope","$rootScope","SweetAlert","$filter", "ngTableParams","escalaRemuneracionFactory",  function($scope,$rootScope,SweetAlert,$filter, ngTableParams,escalaRemuneracionFactory) {
    	
	$scope.codigo=null;
	$scope.grado=null;
	$scope.grupo=null;
	$scope.estado=null;
	$scope.edicion=false;
	$scope.objeto={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		
		$scope.data=[];
		escalaRemuneracionFactory.traer(pagina).then(function(resp){
			//console.log(resp);
			if (resp.meta)
				$scope.data=resp;				
		})
	
	};
	
	$scope.$watch('data', function() {
		
		$scope.tableParams = new ngTableParams({
			page : 1, // show first page
			count : 10, // count per page
			filter: {} 	
		}, {
			total : $scope.data.length, // length of data
			getData : function($defer, params) {
				var orderedData = params.filter() ? $filter('filter')(
						$scope.data, params.filter()) : $scope.data;
				$scope.lista = orderedData.slice(
						(params.page() - 1) * params.count(), params
								.page()
								* params.count());
				params.total(orderedData.length);
				$defer.resolve($scope.lista);
			}
		});
	});	
	
	$scope.filtrar=function(){
		
		$scope.data=[];
		escalaRemuneracionFactory.traerFiltro(pagina,$scope.grupo,$scope.codigo,$scope.grado, $scope.estado).then(function(resp){
			
			if (resp.meta)
				$scope.data=resp;
		})
	};
	
	$scope.limpiar=function(){
		$scope.codigo=null;
		$scope.grado=null;
		$scope.grupo=null;
		$scope.estado=null;
		$scope.consultar();
		
	};
	
	$scope.nuevo=function(){
		$scope.objeto={id:null,estado:'A'};
		
		$scope.edicion=true;
	};
	
	$scope.editar=function(id){
		escalaRemuneracionFactory.traerObj(id).then(function(resp){
			
			if (resp.estado){
				$scope.objeto=resp.json.escalarmu;
				$scope.edicion=true;
				//console.log($scope.objeto);
			}
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
		                
		            	escalaRemuneracionFactory.guardar($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Escala Remuneracion!", "Registro guardado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Escala Remuneracion!", resp.mensajes.msg, "error");
		        				 
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