'use strict';

app.controller('UsuariosController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","usuariosFactory",  function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, usuariosFactory) {
    
	
	$scope.nombreFiltro=null;
	$scope.ordenFiltro=null;
	
	$scope.edicion=false;
	$scope.objeto={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		
		$scope.data=[];
		usuariosFactory.traerUsuarios(pagina).then(function(resp){
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
		usuariosFactory.traerUsuariosFiltro(pagina,$scope.nombreFiltro,$scope.ordenFiltro).then(function(resp){
			
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
		usuariosFactory.traerUsuario(id).then(function(resp){
			
			if (resp.estado)
			   $scope.objeto=resp.json.menu;
			   $scope.edicion=true;
			   console.log($scope.objeto);
		})
		
	};
	
	$scope.abrirUsuarioPadre = function() {

		var modalInstance = $uibModal.open({
			templateUrl : 'modalUsuarioPadre.html',
			controller : 'UsuarioPadreController',
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
		
		usuariosFactory.eliminar(id).then(function(resp){
			console.log(resp);
			if (resp.estado)
				$scope.limpiar();

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
		                
		            	usuariosFactory.guardar($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Usuario!", "Registro registrado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Usuario!", resp.mensajes.msg, "error");
		        				 
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