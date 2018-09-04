'use strict';

app.controller('PerfilesController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","PerfilesFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams,PerfilesFactory) {


	$scope.nombreFiltro=null;
	$scope.ordenFiltro=null;

	$scope.edicion=false;
	$scope.guardar=false;
	$scope.objeto={};
	$scope.objetolista={};
	
	var pagina = 1;
	$scope.consultar=function(){
		$scope.data=[];
		PerfilesFactory.traer(pagina).then(function(resp){
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
		PerfilesFactory.traerFiltro(pagina,$scope.nombreFiltro).then(function(resp){
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
		$scope.objeto={id:{permisoid:null}};
		$scope.objetolista=[];
		var obj={id:{permisoid:$scope.objeto},perfilpermisolectura:null};
//		console.log(obj);
		$scope.objetolista.push(obj);
//		console.log($scope.objetolista);

		$scope.edicion=true;
		$scope.guardar=true;
	}
	
	$scope.editar=function(id){
		PerfilesFactory.traerPermiso(id).then(function(resp){
//console.clear();
//console.log(resp.json);
			if (resp.estado) {
			   $scope.objeto=resp.json.perfil;
			   $scope.objetolista=resp.json.details;
			   //console.log($scope.objetolista);
			}
			$scope.edicion=true;
			$scope.guardar=true;
		})
	}

	$scope.agregarDetalle=function(){
		var obj={id:{perfilid:$scope.objeto,permisoid:null},nppermiso:null};
		$scope.objetolista.push(obj);
	}

	$scope.removerDetalle=function(index){
		$scope.objetolista.splice(index,1);
	}
 
	$scope.abrirPerfilesPermisos = function(index) {
		//console.log("aqui");
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalPermiso.html',
			controller : 'ModalPermisosController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objetolista[index].id.permisoid = obj.id;
			$scope.objetolista[index].nppermiso=obj.descripcion;
			//$scope.objetolista[index].id.permisoid = obj.id.permisoid;
			//$scope.objetolista[index].nppermiso=obj.nppermiso;
		}, function() {
			//console.log("close modal");
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
            	PerfilesFactory.guardar($scope.objeto).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.objeto={};
	 		             $scope.limpiar();
	 		             SweetAlert.swal("Permiso!", "Registro registrado satisfactoriamente!", "success");
        			 }else{
	 		             SweetAlert.swal("Permiso!", resp.mensajes.msg, "error");
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

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);
	};
	
	$scope.cancelar=function(){
		$uibModalInstance.dismiss();
	};
} ]);