'use strict';

app.controller('EmpleadosController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","empleadosFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, empleadosFactory) {

	$scope.nombreFiltro=null;
	$scope.codigoFiltro=null;
	$scope.estadoFiltro=null;

	$scope.edicion=false;
	$scope.guardar=false;
	$scope.nuevoar=false;
	$scope.objeto={};
	$scope.objetoTipoIdentidicacion={};
	$scope.objetoGradoEscala={};
	$scope.objetoEspecialidad={};
	$scope.objetoClasificacion={};

	var pagina = 1;

	$scope.consultar=function(){
		$scope.data=[];
		empleadosFactory.traerEmpleados(pagina).then(function(resp){
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

		empleadosFactory.traerEmpleadosFiltro(pagina,$scope.codigoFiltro,$scope.nombreFiltro,$scope.estadoFiltro).then(function(resp){
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
		$scope.objetolista=[];
		var obj={id:{permisoid:$scope.objeto},perfilpermisolectura:null};
//		console.log(obj);
		$scope.objetolista.push(obj);
//		console.log($scope.objetolista);

		$scope.edicion=true;
		$scope.guardar=true;
		$scope.nuevoar=true;
	}
	
	$scope.editar=function(id){
		empleadosFactory.traerEmpleadosEditar(id).then(function(resp){
//console.clear();
console.log(resp.json);
			if (resp.estado) {
				$scope.objeto=resp.json.empleado;

				//Tipo de identificacion = socionegociotipoidentid
				empleadosFactory.traerTipoIdentidicacionEditar($scope.objeto.socionegociotipoidentid).then(function(resp){
					console.log("Tipo Identificacion");
					console.log(resp.json);
					if (resp.estado) {
					   $scope.objetoTipoIdentidicacion=resp.json.tipoidentificacion;
					}
				})
/*
				//Tipo de identificacion tipo = socionegociotipoidenttipoid
				empleadosFactory.traerGradoEscalaEditar($scope.objeto.socionegociotipoidenttipoid).then(function(resp){
					console.log("");
					console.log(resp.json);
					if (resp.estado) {
					   $scope.objeto=resp.json.empleado;
					}
				})
*/
				//Grado escala = sn emp grado escala id
				empleadosFactory.traerGradoEscalaEditar($scope.objeto.snempgradoescalaid).then(function(resp){
					console.log("Grado Escala:");
					console.log(resp.json);
					if (resp.estado) {
					   $scope.objetoGradoEscala = resp.json.gradoescala;
					}
				})
/*
				//Cargo escala = 
				empleadosFactory.traerGradoEscalaEditar($scope.objeto.socionegociotipoidenttipoid).then(function(resp){
					console.log("");
					console.log(resp.json);
					if (resp.estado) {
					   $scope.objeto=resp.json.empleado;
					}
				})
*/
				//Especialidad = socionegocioempespid
				empleadosFactory.traerEspecialidadesEditar($scope.objeto.socionegocioempespid).then(function(resp){
					console.log("Especialidad");
					console.log(resp.json);
					if (resp.estado) {
					   $scope.objetoEspecialidad=resp.json.especialidades;
					}
				})

				//Clasificacion = socionegocioempclasifid
				empleadosFactory.traerClasificacionEditar($scope.objeto.socionegocioempclasifid).then(function(resp){
					console.log("Clasificacion");
					console.log(resp.json);
					if (resp.estado) {
					   $scope.objetoClasificacion=resp.json.clasificacion;
					}
				})
			}
			$scope.edicion=true;
			$scope.guardar=true;
			$scope.nuevoar=false;
		})
	};

	$scope.agregarDetalle=function(){
		var obj={id:{perfilid:$scope.objeto,permisoid:null},nppermiso:null};
		$scope.objetolista.push(obj);
	}

	$scope.removerDetalle=function(index){
		$scope.objetolista.splice(index,1);
	}

	$scope.abrirIdentificacion = function(index) {
		console.log("aqui");
		var modalInstance = $uibModal.open({
			templateUrl : 'modalTipoIdentificacion.html',
			controller : 'ModalTipoIdentificacionController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.socionegociotipoidentid = obj.id;
			$scope.objetoTipoIdentidicacion = obj;
		}, function() {
			console.log("close modal");
		});
	};

	$scope.abrirGradoEscala = function(index) {
		//console.log("aqui");
		var modalInstance = $uibModal.open({
			templateUrl : 'modalGradoEscala.html',
			controller : 'ModalGradoEscalaController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.snempgradoescalaid = obj.id;
			$scope.objetoGradoEscala = obj;
		}, function() {
			console.log("close modal");
		});
	};

	$scope.abrirEspecialidad = function(index) {
		console.log("aqui");
		var modalInstance = $uibModal.open({
			templateUrl : 'modalEspecialidades.html',
			controller : 'ModalEspecialidadesController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.socionegocioempespid = obj.id;
			$scope.objetoEspecialidad = obj;
		}, function() {
			console.log("close modal");
		});
	};

	$scope.abrirClasificacion = function(index) {
		console.log("aqui");
		var modalInstance = $uibModal.open({
			templateUrl : 'modalClasificacion.html',
			controller : 'ModalClasificacionController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.socionegocioempclasifid = obj.id;
			$scope.objetoClasificacion = obj;
		}, function() {
			console.log("close modal");
		});
	};

	$scope.abrirCargoEscala = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalCargoEscala.html',
			controller : 'ModalCargoEscalaController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objetoGradoEscala.npcodigogradoid = obj.id;
			$scope.objetoGradoEscala.npcodigogrado = obj.codigo;
			$scope.objetoGradoEscala.npnombrecargo = obj.npnombrecargo;
		}, function() {
			console.log("close modal");
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
		                
		            	empleadosFactory.guardar($scope.objeto).then(function(resp){
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
} ]);