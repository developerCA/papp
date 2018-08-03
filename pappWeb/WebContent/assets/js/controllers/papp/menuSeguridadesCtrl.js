'use strict';

app.controller('MenuSeguridadController', [ "$scope","$rootScope","$uibModal","_","SweetAlert","$filter", "ngTableParams","menuSeguridadesFactory",
	function($scope,$rootScope,$uibModal,_,SweetAlert,$filter, ngTableParams,menuSeguridadesFactory) {
    
	
	$scope.nombreFiltro=null;
	$scope.ordenFiltro=null;
	
	$scope.edicion=false;
	$scope.objeto={};
	$scope.menuArbol={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		
		$scope.data=[];
		menuSeguridadesFactory.traerMenus(pagina).then(function(resp){
			//console.log(resp);
			if (resp.meta)
				$scope.data=resp;
			    $scope.menuArbol=_.filter($scope.data, function(menu){ return menu.padreid==0 && menu.nombre.trim()!="" });
				for (var hijo in $scope.menuArbol) {
					$scope.menuArbol[hijo].hijos = ($scope.cuantosHijos($scope.menuArbol[hijo])? false: true);
				}
			    //console.log($scope.menuArbol);
		})
	
	};

	$scope.cargarHijos=function(menuPadre){
		if (!menuPadre.iscargado) {
			menuPadre.iscargado=true;

			var menusHijos = _.filter($scope.data, function(menu){
				return menu.padreid==menuPadre.id && menu.nombre.trim()!="";
			});
			for (var hijo in menusHijos) {
				menusHijos[hijo].hijos = ($scope.cuantosHijos(menusHijos[hijo])? false: true);
			}
			//console.log(menusHijos);
			var nodes=JSON.parse(JSON.stringify(menusHijos).split('"descripcion":').join('"title":'));
			menuPadre.nodes=nodes;
		}
	}

	$scope.cuantosHijos=function(menuPadre){
		var menusHijos = _.filter($scope.data, function(menu){
			return menu.padreid==menuPadre.id && menu.nombre.trim()!="";
		});
		return menusHijos.length;
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
				$scope.lista = orderedData.slice(
						(params.page() - 1) * params.count(), params
								.page()
								* params.count());
				params.total(orderedData.length);
				$defer.resolve($scope.lista);
			}
		});
	});

	$scope.nuevo=function(node){
		if (node === undefined) {
			$scope.objeto={id:null};
		} else {
			
		}
		
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
	
	$scope.abrirPermiso = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalPermiso.html',
			controller : 'PerfilesPermisosController',
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