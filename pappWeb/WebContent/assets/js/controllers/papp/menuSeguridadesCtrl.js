'use strict';

app.controller('MenuSeguridadController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","menuSeguridadesFactory",  function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams,menuSeguridadesFactory) {
    
	
	$scope.nombreFiltro=null;
	$scope.ordenFiltro=null;
	
	$scope.edicion=false;
	$scope.objeto={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		
		$scope.data=[];
		menuSeguridadesFactory.traerMenus(pagina).then(function(resp){
			console.log(resp);
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
		menuSeguridadesFactory.traerMenusFiltro(pagina,$scope.nombreFiltro,$scope.ordenFiltro).then(function(resp){
			
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.nombreFiltro=null;
		$scope.ordenFiltro=null;
		
		
		$scope.consultar();
		
	};
	
	$scope.nuevo=function(){
		$scope.objeto={id:null};
		
		$scope.edicion=true;
	}
	
	$scope.editar=function(id){
		menuSeguridadesFactory.traerMenu(id).then(function(resp){
			
			if (resp.estado)
			   $scope.objeto=resp.json.menu;
			   $scope.edicion=true;
			   console.log($scope.objeto);
		})
		
	};
	
	$scope.abrirMenuPadre = function() {

		var modalInstance = $uibModal.open({
			templateUrl : 'modalMenuPadre.html',
			controller : 'MenuPadreController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.padreid = obj.id;
			$scope.objeto.nombrepadre=obj.nombre;
		}, function() {
			console.log("close modal");
		});
	};
		
	$scope.eliminar=function(id){
		
		menuSeguridadesFactory.eliminar(id).then(function(resp){
			console.log(resp);
			if (resp.estado){
				$scope.limpiar();
				SweetAlert.swal("Menu!", "Menu eliminado satisfactoriamente!", "success");
			}
			else{
				SweetAlert.swal("Menu!", resp.mensajes.msg, "error");
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
		                
		            	menuSeguridadesFactory.guardar($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Menu!", "Menu registrado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Menu!", resp.mensajes.msg, "error");
		        				 
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