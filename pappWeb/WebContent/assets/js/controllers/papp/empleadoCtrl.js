'use strict';

app.controller('EmpleadosController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","empleadosFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, empleadosFactory) {

	$scope.codigoFiltro=null;
	$scope.nombreFiltro=null;
	$scope.typoFiltro=null;
	$scope.estadoFiltro=null;

	$scope.edicion=false;
	$scope.guardar=false;
	$scope.nuevoar=false;
	$scope.objeto={};
	$scope.objetoTipoIdentidicacion={};
	$scope.objeto={};
	$scope.objetoEspecialidad={};
	$scope.objetoClasificacion={};
	$scope.fuerza=null;
	$scope.data=[];

    $scope.pagina = 1;
    $scope.aplicafiltro=false;

	$scope.consultar=function(){
		empleadosFactory.traerEmpleados(
			$scope.pagina
		).then(function(resp){
//			console.log(resp);
        	$scope.data = resp.json.result;
            $scope.total = resp.json.total.valor;
//			console.log($scope.data);
		})
	};

    $scope.pageChanged = function() {
        if ($scope.aplicafiltro){
        	$scope.filtrarUnico();
        }else{
        	$scope.consultar();	
        }
    };  
    
    $scope.filtrar=function(){
    	$scope.pagina=1;
    	$scope.aplicafiltro=true;
    	$scope.filtrarUnico();
    }  

	$scope.filtrarUnico=function(){
		empleadosFactory.traerEmpleadosFiltro(
			$scope.pagina,
			$scope.codigoFiltro,
			$scope.nombreFiltro,
			$scope.tipoFiltro,
			$scope.estadoFiltro
		).then(function(resp){
        	$scope.data = resp.json.result;
            $scope.total = resp.json.total.valor;
		})
	}
	
	$scope.limpiar=function(){
		$scope.codigoFiltro=null;
		$scope.nombreFiltro=null;
		$scope.tipoFiltro=null;
		$scope.estadoFiltro=null;
		$scope.aplicafiltro=false;

		$scope.consultar();
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
	
	$scope.nuevo=function(){
		$scope.objeto={
			id:null,
			esempleado: 1,
			esproveedor: 0,
			razonsocial: "RAZON SOCIAL",
			representantelegal: "REPRESENTANTE LEGAL"
		};
		//$scope.objetolista=[];
		//var obj={id:{permisoid:$scope.objeto},perfilpermisolectura:null};
		//$scope.objetolista.push(obj);
		$scope.fuerza=null;

		$scope.edicion=true;
		$scope.guardar=true;
		$scope.nuevoar=true;
	};

	$scope.editar=function(id){
		empleadosFactory.traerEmpleadosEditar(id).then(function(resp){
			//console.log(resp.json);
			if (resp.estado) {
				$scope.objeto=resp.json.empleado;
				$scope.fuerza=$scope.objeto.npfuerzaid;
			}
			$scope.edicion=true;
			$scope.guardar=true;
			$scope.nuevoar=false;
		})
	};

	$scope.nombreMostrado = function() {
		$scope.objeto.nombremostrado = "";
		if ($scope.objeto.primernombre != null && $scope.objeto.primernombre.trim() != "") {
			$scope.objeto.nombremostrado += $scope.objeto.primernombre;
		}
		if ($scope.objeto.segundonombre != null && $scope.objeto.segundonombre.trim() != "") {
			$scope.objeto.nombremostrado += " " + $scope.objeto.segundonombre;
		}
		if ($scope.objeto.primerapellido != null && $scope.objeto.primerapellido.trim() != "") {
			$scope.objeto.nombremostrado += " " + $scope.objeto.primerapellido;
		}
		if ($scope.objeto.segundoapellido != null && $scope.objeto.segundoapellido.trim() != "") {
			$scope.objeto.nombremostrado += " " + $scope.objeto.segundoapellido;
		}
		$scope.objeto.nombremostrado = $scope.objeto.nombremostrado.trim();
		if ($scope.nuevoar) {
			$scope.objeto.nombrecomercial = $scope.objeto.nombremostrado;
		}
	}

	$scope.agregarDetalle=function(){
		var obj={id:{perfilid:$scope.objeto,permisoid:null},nppermiso:null};
		$scope.objetolista.push(obj);
	}

	$scope.removerDetalle=function(index){
		$scope.objetolista.splice(index,1);
	}

	$scope.abrirIdentificacion = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalTipoIdentificacion.html',
			controller : 'ModalTipoIdentificacionController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.socionegociotipoidentid = obj.id.identificacionid;
			$scope.objeto.socionegociotipoidenttipoid = obj.id.identificaciontipoid;
		}, function() {
		});
	};

	$scope.abrirGradoEscala = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalGradoEscala.html',
			controller : 'ModalGradoEscalaController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.snempgradoescalaid = obj.id;
			$scope.objeto.npgradoescalacodigo = obj.codigo;
			$scope.objeto.npgradonombre = obj.npnombregrado;
			$scope.objeto.npfuerzanombre = obj.npnombrefuerza;
			$scope.objeto.npgrupoocupacional = obj.npgrupoocupacional;
			$scope.objeto.npcodigoescalarmu = obj.npcodigoescalarmu;
			$scope.objeto.npremuneracion = obj.npremuneracion;
			$scope.objeto.npcargocodigo = " ";
			$scope.fuerza = obj.npfuerzaid;
		}, function() {
		});
	};

	$scope.abrirEspecialidad = function(index) {
		//console.log($scope.fuerza);
		var modalInstance = $uibModal.open({
			templateUrl : 'modalEspecialidades.html',
			controller : 'ModalEspecialidadesController',
			size : 'lg',
			resolve : {
				fuerza : function() {
					return $scope.fuerza;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.socionegocioempespid = obj.id;
			$scope.objeto.npespecialidadcodigo = obj.codigo;
			$scope.objeto.npespecialidadnombre = obj.nombre;
		}, function() {
		});
	};

	$scope.abrirClasificacion = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalClasificacion.html',
			controller : 'ModalClasificacionController',
			size : 'lg',
			resolve : {
				fuerza : function() {
					return $scope.fuerza;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.socionegocioempclasifid = obj.id.fuerzaclasificacionid;
			$scope.objeto.npclasificacioncodigo = obj.codigo;
			$scope.objeto.npclasificacionnombre = obj.nombre;
		}, function() {
		});
	};

	$scope.abrirCargoEscala = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalCargoEscala.html',
			controller : 'ModalCargoEscalaController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.snempcargoescalaid = obj.id;
			$scope.objeto.npcargocodigo = obj.codigo;
			$scope.objeto.npncargoombre = obj.npnombrecargo;
			$scope.objeto.npcargogrupoocupacional = obj.npgrupoocupacional;
			$scope.objeto.npcargocodigoescalarmu = obj.npcodigoescalarmu;
			$scope.objeto.npremuneracion = obj.npremuneracion;
			$scope.objeto.npgradoescalacodigo = " ";
			$scope.objeto.npespecialidadcodigo = " ";
			$scope.objeto.npclasificacioncodigo = " ";
		}, function() {
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
			 		             SweetAlert.swal("Empleado!", "Registro guardado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Empleado", resp.mensajes.msg, "error");
		        				 
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