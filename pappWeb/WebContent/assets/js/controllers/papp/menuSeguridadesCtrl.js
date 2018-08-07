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
			$scope.objeto={id:null};
			$scope.objeto.padreid = node.id;
			$scope.objeto.nppadre = node.nombre;
		}
		
		$scope.edicion=true;
	}
	
	$scope.editar=function(node){
		$scope.nodeEditando = node;
		menuSeguridadesFactory.traerMenu(node.id).then(function(resp){
			if (resp.estado)
			   $scope.objeto=resp.json.menu;
			   $scope.edicion=true;
			   console.log($scope.objeto);
		})
		
	};
	
	$scope.abrirMenuPadre = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalMenuPadre.html',
			controller : 'MenuPadreController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.padreid = obj.id;
			$scope.objeto.nppadre = obj.nombre;
		}, function() {
			console.log("close modal");
		});
	};
	
	$scope.abrirPermiso = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalPermiso.html',
			controller : 'ModalPerfilesPermisosController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.permisoid = obj.id;
			$scope.objeto.nppermisonombre = obj.nombre;
			$scope.objeto.nppermisodescripcion = obj.descripcion;
		}, function() {
			console.log("close modal");
		});
	};
		
	$scope.eliminar=function(node){
		if (!node.hijos) {
			SweetAlert.swal("Menu Seguridades!", "No se puede eliminar porque tiene hijos", "error");
			return;
		}
		SweetAlert.swal({
		   title: "Menu Seguridades?",
		   text: "Esta seguro que desea eliminar opcion '"+node.nombre+"', del menu?",
		   type: "warning",
		   showCancelButton: true,
		   confirmButtonColor: "#DD6B55",
		   confirmButtonText: "Si",
		   cancelButtonText: "No",
		   closeOnConfirm: false}, 
		function(isConfirm){
		    if (!isConfirm) return;
			menuSeguridadesFactory.eliminar(node.id).then(function(resp){
				//console.log(resp);
				if (resp.estado){
					SweetAlert.swal("Menu Seguridades!", "Opcion eliminada satisfactoriamente!", "success");
				}
				else{
					SweetAlert.swal("Menu Seguridades!", resp.mensajes.msg, "error");
				}
			})
		});
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
	 		            $scope.nodeEditando=resp.json.menu;
	 		            SweetAlert.swal("Menu Seguridades!", "Menu registrado satisfactoriamente!", "success");
        			}else{
	 		            SweetAlert.swal("Menu Seguridades!", resp.mensajes.msg, "error");
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