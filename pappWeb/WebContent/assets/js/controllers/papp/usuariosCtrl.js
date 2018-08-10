'use strict';

app.controller('UsuariosController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","usuariosFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, usuariosFactory) {

	$scope.nombreFiltro=null;
	$scope.usuarioFiltro=null;
	$scope.estadoFiltro=null;
	
	$scope.edicion=false;
	$scope.objeto={};
	$scope.objetoDetalles={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		usuariosFactory.traerUsuarios(pagina).then(function(resp){
			//console.log(resp);
			if (resp.meta)
				$scope.data=resp;
		})
	};

	$scope.$watch('data', function() {
		if (!$scope.data) return;
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
		usuariosFactory.traerUsuariosFiltro(pagina,$scope.usuarioFiltro,$scope.nombreFiltro,$scope.estadoFiltro).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.nombreFiltro=null;
		$scope.usuarioFiltro=null;
		$scope.estadoFiltro=null;

		$scope.consultar();
	};
	
	$scope.nuevo=function(){
		$scope.objeto={id:null};
		$scope.objetoDetalles=[];
		$scope.objetolista=[];
		var obj={id:{permisoid:null},perfilpermisolectura:null};
//		console.log(obj);
		$scope.objetolista.push(obj);
		console.log($scope.objetolista);

		$scope.edicion=true;
	}
	
	$scope.editar=function(id){
		usuariosFactory.traerUsuario(id).then(function(resp){
//console.clear();
		//console.log(resp.json);
		if (resp.estado)
			$scope.objeto=resp.json.usuario;
			$scope.objeto.confirmacion=$scope.objeto.clave;
		    $scope.objetoDetalles=resp.json.details;
			$scope.edicion=true;
			//console.log($scope.objeto);
		})
		
	};
	
	$scope.abrirNombrePerfil = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalNombrePerfil.html',
			controller : 'ModalNombrePerfilController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.perfilid = obj.id;
			$scope.objeto.npperfil=obj.nombre;
		}, function() {
			//console.log("close modal");
		});
	};
	
	$scope.abrirEmpleadoCodigo = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalEmpleadoCodigo.html',
			controller : 'ModalSocioNegocioEmpleadosController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.socionegocioid = obj.id;
			$scope.objeto.npsocionegociocodigo = obj.codigo;
			$scope.objeto.npsocionegocio = obj.nombremostrado;
		}, function() {
			//console.log("close modal");
		});
	};

	$scope.eliminar=function(id){
		usuariosFactory.eliminar(id).then(function(resp){
			//console.log(resp);
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
                if ($scope.objeto.clave != ($scope.objeto.confirmacion === undefined? "": $scope.objeto.confirmacion)) {
                	SweetAlert.swal("Usuario!", "Las claves no son iguales", "error");
                	return;
                }
                var tObj = Object.assign({}, $scope.objeto);
                tObj.usuariounidadTOs=$scope.objetoDetalles;
            	usuariosFactory.guardar(tObj).then(function(resp){
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

	 $scope.agregarDetalle=function(){
		var obj={id:{id:$scope.objeto.id, unidad:null},npunidad:null};
		$scope.objetoDetalles.push(obj);
	}

	$scope.removerDetalle=function(index){
		$scope.objetoDetalles.splice(index,1);
	}

	$scope.abrirUnudad = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalUnidadCorto.html',
			controller : 'ModalUnidadCortoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			$scope.objetoDetalles[index].id.unidad = obj.id;
			$scope.objetoDetalles[index].npunidad=obj.nombre;
		}, function() {
		});
	};
} ]);