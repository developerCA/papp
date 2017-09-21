'use strict';

app.controller('FuerzaController', [ "$scope","$rootScope","SweetAlert","$filter", "ngTableParams","fuerzaFactory",  function($scope,$rootScope,SweetAlert,$filter, ngTableParams,fuerzaFactory) {
    	
	$scope.nombreFiltro=null;
	$scope.codigoFiltro=null;
	$scope.estadoFiltro=null;
	$scope.siglaFiltro=null;
	$scope.edicion=false;
	$scope.objeto={};
	$scope.detalles=[];
	
	var pagina = 1;
	
	$scope.consultar=function(){
		
		$scope.data=[];
		fuerzaFactory.traerFuerzas(pagina).then(function(resp){
			console.log(resp);
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
		fuerzaFactory.traerFuerzasFiltro(pagina,$scope.nombreFiltro,$scope.codigoFiltro,$scope.sigla, $scope.estadoFiltro).then(function(resp){
			
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.nombreFiltro=null;
		$scope.codigoFiltro=null;
		$scope.estadoFiltro=null;
		$scope.consultar();
		
	};
	
	$scope.nuevo=function(){
		$scope.objeto={id:null};
		$scope.detalles=[];
		$scope.edicion=true;
	}
	
	$scope.editar=function(id){
		fuerzaFactory.traerFuerza(id).then(function(resp){
			
			if (resp.estado)
				
				$scope.objeto=resp.json.cargo;
				$scope.detalles=resp.json.details;
				console.clear();
				console.log("==================");
				console.log($scope.detalles);
				$scope.edicion=true;
				console.log($scope.objeto);
		})
		
	};
	
	$scope.agregarDetalle=function(){
		var obj={codigo:null,estado:"A",nombre:null};
		$scope.detalles.push(obj);
		
	}
	
	$scope.removerDetalle=function(index){
		$scope.detalles.splice(index,1);		
	}
	
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
		            	
		            	$scope.objeto.details=$scope.detalles;
		            	
		            	fuerzaFactory.guardar($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.detalles=[];
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Fuerza!", "Registro registrado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Fuerza!", resp.mensajes.msg, "error");
		        				 
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