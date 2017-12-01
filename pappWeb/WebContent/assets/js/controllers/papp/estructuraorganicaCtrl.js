'use strict';

app.controller('EstructuraOrganicaController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","estructuraorganicaFactory","unidadFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, estructuraorganicaFactory,unidadFactory) {

	$scope.codigo=null;
	$scope.fuerza=null;
	$scope.grado=null;
	$scope.padre=null;
	$scope.estado=null;

	$scope.edicion=false;
	$scope.nuevoar=false;
	$scope.guardar=false;
	$scope.objeto={estado:null};
	$scope.tabactivo=0;
	$scope.arbol={};
	$scope.objetoUnidad={};
	$scope.dUnidad=false;
	$scope.dUnidadEditar=false;
	$scope.dEmpleados=false;
	$scope.dEmpleadosEditar=false;

	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		//console.log('aqi');
		estructuraorganicaFactory.traerEstructuraOrganica(pagina).then(function(resp){
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
		estructuraorganicaFactory.traerEstructuraOrganicaFiltro(pagina,$scope.codigo,$scope.fuerza,$scope.grado,$scope.padre,$scope.estado).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.codigo=null;
		$scope.fuerza=null;
		$scope.grado=null;
		$scope.padre=null;
		$scope.estado=null;

		$scope.consultar();
		
	};

	$scope.nuevo=function(){
		$scope.objeto={id:null,estado:null};
		$scope.edicion=true;
		$scope.nuevoar=true;
		$scope.guardar=true;
	}
	
	$scope.editar=function(id){
		$scope.estructuraSeleccionada=id;
		estructuraorganicaFactory.traerEstructuraOrganicaEditar($scope.estructuraSeleccionada).then(function(resp){
			if (resp.estado) {
			    $scope.objeto=resp.json.estructuraorganica;
			}
			$scope.edicion=true;
			$scope.nuevoar=false;
			$scope.guardar=true;
			console.log($scope.objeto);
			$scope.tabactivo=0;
		})
	};

	$scope.nuevaUnidad=function(){
		$scope.objetoUnidad={id:null,unidadarbolerganicaid:$scope.estructuraSeleccionada,estado:"A"};
		$scope.dUnidad=false;
		$scope.dUnidadEditar=true;
		$scope.nuevoar=true;
	}
	
	$scope.agregarUnidadHija=function(node){
		console.log(node.unidadarbolerganicaid);
		console.log(node);
		$scope.objetoUnidad={
			id: null,
			unidadarbolerganicaid: node.unidadarbolerganicaid,
			unidadarbolpadreid: node.id,
			estado: "A"
		};
		$scope.dUnidad=false;
		$scope.dUnidadEditar=true;
		$scope.nuevoar=true;
	};

	$scope.mostrarUnidad=function(id){
		$scope.estructuraSeleccionada=id;
		$scope.tabactivo=1;
		$scope.edicion=true;
		$scope.dUnidad=true;
		$scope.nuevoar=false;

		unidadFactory.traerUnidadesArbol(pagina,$scope.estructuraSeleccionada,'A').then(function(resp){
			$scope.arbol = JSON.parse(JSON.stringify(resp).split('"descripcion":').join('"title":'));
		})
	};

	$scope.mantenerPlaza=function(node){
		console.log(node.id);
		unidadFactory.traerUnidadArbolDetail(
				node.id
			).then(function(resp){
				if (resp.estado) {
					$scope.objetoPlaza=resp.json.unidadarbol;
					$scope.objetoPlazaDetail=resp.json.details;
				}
				$scope.edicion=true;
				console.log(resp.json);
				$scope.dUnidad=false;
				$scope.dUnidadPlazaEditar=true;
		})
	};

//** Mantener Empleado
	$scope.agregarMantenerPlazaDetalle=function(){
		var obj={id:null,responsable:0,aprueba:0,revisa:0};
		$scope.objetoPlazaDetail.push(obj);
	}

	$scope.removerMantenerPlazaDetalle=function(index){
		$scope.objetoPlazaDetail.splice(index,1);
	}

	$scope.formUnidadPlaza = {
	    submit: function (formUnidadPlaza) {
			console.log("formUnidadPlaza");
	        var firstError = null;
	        if (formUnidadPlaza.$invalid) {
	            var field = null, firstError = null;
	            for (field in formUnidadPlaza) {
	                if (field[0] != '$') {
	                    if (firstError === null && !formUnidadPlaza[field].$valid) {
	                        firstError = formUnidadPlaza[field].$name;
	                    }
	                    if (formUnidadPlaza[field].$pristine) {
	                    	formUnidadPlaza[field].$dirty = true;
	                    }
	                }
	            }
	            angular.element('.ng-invalid[name=' + firstError + ']').focus();
	            return;
	        } else {
	        	unidadFactory.guardarArbol($scope.objetoUnidad).then(function(resp){
	        		if (resp.estado){
	        			formUnidadPlaza.$setPristine(true);
						$scope.dUnidad=true;
						$scope.dUnidadPlazaEditar=false;
	 		            $scope.objetUnidado={};
	 		            $scope.objetoPlazaDetail={};
	 		            SweetAlert.swal("Unidad - Mantener Plazas!", "Registro guardado satisfactoriamente!", "success");
	    			}else{
	 		            SweetAlert.swal("Unidad - Mantener Plazas!", resp.mensajes.msg, "error");
	    			}
	    		})
	        }
	    },
	    reset: function (formUnidadPlaza) {
	        $scope.myModel = angular.copy($scope.master);
	        formUnidadPlaza.$setPristine(true);
			$scope.dUnidad=true;
			$scope.dUnidadPlazaEditar=false;
	        $scope.objetoUnidad={};
            $scope.objetoPlazaDetail={};
	    }
	};


//** Plaza Empleados
	$scope.mostrarEmpleados=function(id){
		$scope.estructuraSeleccionada=id;
		$scope.tabactivo=2;
		$scope.edicion=true;
		$scope.dEmpleados=true;
		console.log("aqui");
		console.log(id)

		unidadFactory.traerUnidadesArbolPlazaEmpleado(pagina,$scope.estructuraSeleccionada,'A').then(function(resp){
			$scope.data = JSON.parse(JSON.stringify(resp).split('"descripcion":').join('"title":'));
			console.log("dataPlazaEmpleados");
			console.log($scope.data);
		})
	}

	$scope.treeOptions = {
	    accept: function(sourceNodeScope, destNodesScope, destIndex) {
	      return true;
	    },
	};

	$scope.modificarUnidad=function(node){
		console.log(node);
		unidadFactory.traerUnidadArbol(
				node.id
			).then(function(resp){
				if (resp.estado)
					$scope.objetoUnidad=resp.json.unidadarbol;
				$scope.edicion=true;
				console.log(resp.json);
				$scope.dUnidad=false;
				$scope.dUnidadEditar=true;
		})
	};

	$scope.formUnidad = {
        submit: function (formUnidad) {
			console.log("formUnidad");
            var firstError = null;
            if (formUnidad.$invalid) {
                var field = null, firstError = null;
                for (field in formUnidad) {
                    if (field[0] != '$') {
                        if (firstError === null && !formUnidad[field].$valid) {
                            firstError = formUnidad[field].$name;
                        }
                        if (formUnidad[field].$pristine) {
                        	formUnidad[field].$dirty = true;
                        }
                    }
                }
                angular.element('.ng-invalid[name=' + firstError + ']').focus();
                return;
            } else {
            	unidadFactory.guardarArbol($scope.objetoUnidad).then(function(resp){
            		if (resp.estado){
            			formUnidad.$setPristine(true);
    					$scope.dUnidad=true;
    					$scope.dUnidadEditar=false;
	 		            $scope.objetUnidado={};
	 		            SweetAlert.swal("Unidad!", "Registro guardado satisfactoriamente!", "success");
        			}else{
	 		            SweetAlert.swal("Unidad!", resp.mensajes.msg, "error");
        			}
        		})
            }
        },
        reset: function (formUnidad) {
            $scope.myModel = angular.copy($scope.master);
            formUnidad.$setPristine(true);
			$scope.dUnidad=true;
			$scope.dUnidadEditar=false;
            $scope.objetoUnidad={};
        }
	};
	
	$scope.cargarHijos=function(node){
		if (!node.iscargado)
			console.log(node);
		    node.iscargado=true;
		    
		    unidadFactory.traerUnidadesArbolhijos(pagina,$scope.estructuraSeleccionada,node.id,'A').then(function(resp){
				var nodes=JSON.parse(JSON.stringify(resp).split('"descripcion":').join('"title":'));
				node.nodes=nodes;
				
				//console.log($scope.arbol);
			})
			
	}
	
	$scope.abrirInstitucion = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalInstitucion.html',
			controller : 'ModalInstitucionController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.eorganicainstitucionid = obj.id;
			$scope.objeto.npcodigoinstitucion = obj.codigo;
			$scope.objeto.npnombreinstitucion = obj.nombre;
		}, function() {
		});
	};

	$scope.abrirGrado = function(index) {
		//console.log("aqui");
		var modalInstance = $uibModal.open({
			templateUrl : 'modalGrado.html',
			controller : 'ModalGradoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.estructuraorganicagradoid = obj.id;
			$scope.objeto.npnombregrado = obj.nombre;
		}, function() {
		});
	};

	$scope.abrirCodigoPresupuestario = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalUnidad.html',
			controller : 'ModalUnidadController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objetoUnidad.unidadarbolunidadid = obj.id;
			$scope.objetoUnidad.npcodigounidad = obj.codigopresup;
			$scope.objetoUnidad.npnombreunidad = obj.nombre;
		}, function() {
		});
	};

	$scope.abrirNivelOrganicoCodigo = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalNivelOrganico.html',
			controller : 'ModalNivelOrganicoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objetoUnidad.unidadarbolnorganid = obj.id;
			$scope.objetoUnidad.npcodigonivel = obj.codigo;
			$scope.objetoUnidad.npnombrenivel = obj.nombre;
		}, function() {
		});
	};

	$scope.abrirCargoCodigo = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalCargo.html',
			controller : 'ModalCargoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objetoPlazaDetail[index].unidadarbolplazacargoid = obj.id;
			$scope.objetoPlazaDetail[index].npcargocodigo = obj.codigo;
			$scope.objetoPlazaDetail[index].npcargonombre = obj.nombre;
		}, function() {
		});
	};

	$scope.abrirGradoFuerza = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalGradoFuerza.html',
			controller : 'ModalGradoFuerzaController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objetoPlazaDetail[index].unidadarbolplazagfid = obj.id;
			$scope.objetoPlazaDetail[index].npgradonombre = obj.npnombregrado;
			$scope.objetoPlazaDetail[index].npfuerzanombre = obj.npnombrefuerza;
		}, function() {
		});
	};

	$scope.abrirEspecialidad = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalEspecialidades.html',
			controller : 'ModalEspecialidadesController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objetoPlazaDetail[index].unidadarbolplazagfid = obj.id;
			$scope.objetoPlazaDetail[index].npespecialidadcodigo = obj.codigo;
			$scope.objetoPlazaDetail[index].npespecialdidadnombre = obj.nombre;
		}, function() {
		});
	};

	$scope.abrirClasificacion = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalClasificacion.html',
			controller : 'ModalClasificacionController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objetoPlazaDetail[index].unidadarbolplazafclasifid = obj.id;
			$scope.objetoPlazaDetail[index].npclasificacionnombre = obj.nombre;
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
		            	estructuraorganicaFactory.guardar($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Grado - Fuerza!", "Registro guardado satisfactoriamente!", "success");
		        			 }else{
			 		             SweetAlert.swal("Grado - Fuerza!", resp.mensajes.msg, "error");
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