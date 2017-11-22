'use strict';

app.controller('GradoController', [ "$scope","$rootScope","SweetAlert","$filter", "ngTableParams","grupoJerarquicoFactory", "gradoFactory",  function($scope,$rootScope,SweetAlert,$filter, ngTableParams, grupoJerarquicoFactory, gradoFactory) {
    	
	$scope.nombreFiltro=null;
	$scope.siglaFiltro=null;
	$scope.grupoFiltro=null;
	$scope.esteadoFiltro=null;
	$scope.edicion=false;
	$scope.objeto={};
	$scope.grupos = [];
	$scope.grupo = null;
	$scope.grupoJerarquico = null;
	
	var pagina = 1;
	
	$scope.init = function(){
		
		$scope.traerGrupos();
		$scope.consultar();
		
	};
	
	$scope.traerGrupos = function(){
		
		grupoJerarquicoFactory.traerGrupos(pagina).then(function(resp){
			console.clear();
			console.log(resp);
			if (resp.meta)
				$scope.grupos=resp;				
		})
		
	};
	
	$scope.consultar=function(){
		
		$scope.data=[];
		gradoFactory.traerGrados(pagina).then(function(resp){
			if (resp.meta)
				console.log(resp);
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
		gradoFactory.traerGradosFiltro(pagina,$scope.nombreFiltro,$scope.siglaFiltro, $scope.grupoFiltro,$scope.estadoFiltro).then(function(resp){
			console.clear();
			console.log(resp);
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
		$scope.objeto={id:null,estado:'A'};
		
		$scope.edicion=true;
	}
	
	$scope.editar=function(id){
		
		gradoFactory.traerGrado(id).then(function(resp){
			
			if (resp.estado)
				
				$scope.objeto=resp.json.grado;
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
		                
		            	gradoFactory.guardar($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Grado!", "Registro guardado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Grado!", resp.mensajes.msg, "error");
		        				 
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