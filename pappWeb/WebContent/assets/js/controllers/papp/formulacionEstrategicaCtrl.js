'use strict';

app.controller('FormulacionEstrategicaController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","formulacionEstrategicaFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, formulacionEstrategicaFactory) {

	$scope.dateOptions = {
	    changeYear: true,
	    changeMonth: true,
	    yearRange: '2000:-0',    
    };
	$scope.arbol={};
	$scope.edicion=false;
	$scope.guardar=false;
	$scope.nuevoar=false;
	$scope.objeto={};
	$scope.objetolista={};
	$scope.editarId=null;
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		formulacionEstrategicaFactory.traerFormulacionEstrategica(
			pagina,
			$rootScope.ejefiscal
		).then(function(resp){
			//console.log(resp);
			if (resp.meta) {
				$scope.data = resp;
				for (var i = 0; i < $scope.data.length; i++) {
					$scope.data[i].nodeTipo = "PR";
				}
				$scope.arbol = JSON.parse(JSON.stringify($scope.data).split('"nombre":').join('"title":'));
				
			}
		})
	};

	$scope.cargarHijos=function(node){
		//console.log(node);
		if (!node.iscargado)
		    node.iscargado=true;
		else
			return;

		if (node.nodeTipo == "PR") { // Carga Subprograma
		    formulacionEstrategicaFactory.traerSubPrograma(
	    		node.npNivelid,
	    		$rootScope.ejefiscal
			).then(function(resp){
				//console.log(resp);
				for (var i = 0; i < resp.length; i++) {
					resp[i].nodeTipo = "SP";
				}
				var nodes=JSON.parse(JSON.stringify(resp).split('"nombre":').join('"title":'));
				node.nodes=nodes;
			});
		}

		if (node.nodeTipo == "SP") { // Carga Proyecto
		    formulacionEstrategicaFactory.traerProyecto(
	    		node.npNivelid,
	    		$rootScope.ejefiscal
			).then(function(resp){
				//console.log(resp);
				for (var i = 0; i < resp.length; i++) {
					resp[i].nodeTipo = "PY";
				}
				var nodes=JSON.parse(JSON.stringify(resp).split('"nombre":').join('"title":'));
				node.nodes=nodes;
			});
		}

		if (node.nodeTipo == "PY") { // Carga Actividad
		    formulacionEstrategicaFactory.traerActividad(
	    		node.npNivelid,
	    		$rootScope.ejefiscal
			).then(function(resp){
				console.log(resp);
				for (var i = 0; i < resp.length; i++) {
					resp[i].nodeTipo = "AC";
				}
				var nodes=JSON.parse(JSON.stringify(resp).split('"descripcion":').join('"title":'));
				node.nodes=nodes;
			});
		}

		if (node.nodeTipo == "AC") { // Carga Actividad
		    formulacionEstrategicaFactory.traerSubActividad(
	    		node.npNivelid,
	    		$rootScope.ejefiscal
			).then(function(resp){
				console.log(resp);
				for (var i = 0; i < resp.length; i++) {
					resp[i].nodeTipo = "SA";
				}
				var nodes=JSON.parse(JSON.stringify(resp).split('"descripcion":').join('"title":'));
				node.nodes=nodes;
			});
		}
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
		$scope.editarId=null;
		console.log(node);
		$scope.nuevoar = true;
		$scope.guardar = true;
		if (node == null) { // Programa
			formulacionEstrategicaFactory.traerNuevo(
				"PR",
				0,
				$rootScope.ejefiscal
			).then(function(resp){
				console.log("Nuevo:");
				console.log(resp.json);
				if (resp.estado) {
					$scope.objetoPr=resp.json.programa;
					$scope.editarId=$scope.objeto.id;
					$scope.nodeTipo = "PR";
				}
				$scope.edicion=true;
			});
			return;
		}
		if (node.nodeTipo == "PR") { // SubPrograma
			formulacionEstrategicaFactory.traerNuevo(
				"SP",
				node.id,
				$rootScope.ejefiscal
			).then(function(resp){
				console.log("Nuevo:");
				console.log(resp.json);
				if (resp.estado) {
					$scope.objetoSp=resp.json.subprograma;
					$scope.editarId=$scope.objeto.id;
					$scope.nodeTipo = "SP";
				}
				$scope.edicionSubPrograma=true;
			});
		}
		if (node.nodeTipo == "SP") { // Proyecto
			formulacionEstrategicaFactory.traerNuevo(
				"PY",
				node.id,
				$rootScope.ejefiscal
			).then(function(resp){
				console.log("Nuevo:");
				console.log(resp.json);
				if (resp.estado) {
					$scope.objetoPy=resp.json.proyecto;
					$scope.editarId=$scope.objeto.id;
					$scope.nodeTipo = "PY";
				}
				$scope.edicionProyecto=true;
			});
		}
		if (node.nodeTipo == "PY") { // Actividad
			formulacionEstrategicaFactory.traerNuevo(
				"AC",
				node.id,
				$rootScope.ejefiscal
			).then(function(resp){
				console.log("Nuevo:");
				console.log(resp.json);
				if (resp.estado) {
					$scope.objetoAc=resp.json.actividad;
					$scope.editarId=$scope.objeto.id;
					$scope.nodeTipo = "AC";
				}
				$scope.edicionActividad=true;
			});
		}
		if (node.nodeTipo == "AC" || node.nodeTipo == "SA") { // SubActividad
			formulacionEstrategicaFactory.traerNuevo(
				"SA",
				node.id,
				$rootScope.ejefiscal
			).then(function(resp){
				console.log("Nuevo:");
				console.log(resp.json);
				if (resp.estado) {
					$scope.objetoSa=resp.json.subactividad;
					$scope.editarId=$scope.objeto.id;
					$scope.nodeTipo = "SA";
				}
				$scope.edicionSubActividad=true;
			});
		}
	}

	$scope.editar=function(node){
		console.log("Fuente:");
		console.log(node);
		if (node.nodeTipo == "PR") { // Programa
			formulacionEstrategicaFactory.traerProgramaEditar(node).then(function(resp){
				//console.log("Respuesta:");
				//console.log(resp.json);
				if (resp.estado) {
					$scope.objetoPr=resp.json.programa;
					$scope.editarId=$scope.objeto.id;
					$scope.nodeTipo="PR";
				}
				$scope.guardar=true;
				$scope.edicion=true;
			});
		}
		if (node.nodeTipo == "SP") { // SubPrograma
			formulacionEstrategicaFactory.traerSubProgramaEditar(node.id).then(function(resp){
				//console.log("Respuesta:");
				//console.log(resp.json);
				if (resp.estado) {
					$scope.objetoSp=resp.json.subprograma;
					$scope.editarId=$scope.objeto.id;
					$scope.nodeTipo="SP";
				}
				$scope.guardar=true;
				$scope.edicionSubPrograma=true;
			});
		}
		if (node.nodeTipo == "PY") { // Proyecto
			formulacionEstrategicaFactory.traerProyectoEditar(node.id).then(function(resp){
				console.log("Respuesta:");
				console.log(resp.json);
				if (resp.estado) {
					$scope.objetoPy=resp.json.proyecto;
					$scope.objetoPy.npFechainicio = toDate($scope.objeto.npFechainicio);
					$scope.objetoPy.npFechafin = toDate($scope.objeto.npFechafin);

					$scope.editarId=$scope.objeto.id;
					$scope.objetolistaPy=resp.json.proyectometa;
					if ($scope.objetolista == []) {
						$scope.agregarDetallePy();
					}
					$scope.nodeTipo="PY";
				}
				$scope.guardar=true;
				$scope.edicionProyecto=true;
			});
		}
		if (node.nodeTipo == "AC") { // Actividad
			formulacionEstrategicaFactory.traerActividadEditar(node.id).then(function(resp){
				console.log("Respuesta:");
				console.log(resp.json);
				if (resp.estado) {
					$scope.objetoAc=resp.json.actividad;
					$scope.editarId=$scope.objeto.id;
					$scope.objetolista=resp.json.nivelactividad;
					/* if ($scope.objetolista == []) {
						$scope.agregarDetalle();
					} */
					$scope.nodeTipo="AC";
				}
				$scope.guardar=true;
				$scope.edicionActividad=true;
			});
		}
		if (node.nodeTipo == "SA") { // SubActividad
			formulacionEstrategicaFactory.traerSubActividadEditar(node.id).then(function(resp){
				console.log("Respuesta:");
				console.log(resp.json);
				if (resp.estado) {
					$scope.objetoSa=resp.json.subactividad;
					$scope.editarId=$scope.objeto.id;
					$scope.nodeTipo="SA";
				}
				$scope.guardar=true;
				$scope.edicionSubActividad=true;
			});
		}
	};

	function toDate(fuente) {
		try {
			var parts = fuente.split('/');
			//please put attention to the month (parts[0]), Javascript counts months from 0:
			// January - 0, February - 1, etc
		} catch (err) {
			return new Date();
		}
		return new Date(parts[2],parts[0]-1,parts[1]); 
	}

	$scope.agregarDetallePy=function(){
		//documento pagina 15
		if ($scope.objetolistaPy == undefined) {
			$scope.objetolistaPy = [];
		}
		console.log($scope.objetolistaPy);
		var obj={
			id: {
				id: $scope.editarId,
				metaejerciciofiscalid: $rootScope.ejefiscal
			},
			proyectoid: $scope.editarId,
			ano: null,
			descripcion: null
		};
		$scope.objetolistaPy.push(obj);
		console.log($scope.objetolistaPy);
	}

	$scope.removerDetalle=function(index){
		$scope.objetolista.splice(index,1);
	}

	$scope.abrirObjetivoFuerzas = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalObjetivos.html',
			controller : 'ModalObjetivosController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objetoPr.programaobjetivofuersasid = obj.id4;
			$scope.objetoPr.npObjetivocodigo = obj.codigo4;
			$scope.objetoPr.npObjetivodescripcion = obj.descripcion;
		}, function() {
		});
	};

	$scope.abrirFuerza = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalFuerza.html',
			controller : 'ModalFuerzaController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objetoPy.proyectofuerzaid = obj.id;
			$scope.objetoPy.npFuerzacodigo = obj.codigo;
			$scope.objetoPy.npFuerzanombre = obj.nombre;
		}, function() {
		});
	};

	$scope.abrirLider = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalSocioNegocio.html',
			controller : 'ModalSocioNegocioController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objetoPy.proyectoliderid = obj.id;
			$scope.objetoPy.npSocionegociocodigo = obj.codigo;
			$scope.objetoPy.npSocionegocionombre = obj.nombremostrado;
		}, function() {
		});
	};

	$scope.abrirIndicador = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalIndicadores.html',
			controller : 'ModalIndicadoresController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objetoPy.proyectoindicadorid = obj.id;
			$scope.objetoPy.npIndicadorcodigo = obj.codigo;
			$scope.objetoPy.npIndicadornombre = obj.nombre;
		}, function() {
		});
	};

	$scope.abrirIndicadorActividadAc = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalIndicadoresActividad.html',
			controller : 'ModalIndicadoresActividadController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objetoAc.proyectoindicadorid = obj.id;
			$scope.objetoAc.npIndicadorcodigo = obj.npIndicadorcodigo;
			$scope.objetoAc.npIndicadornombre = obj.npIndicadornombre;
			$scope.objetoAc.npIndicadordescripcion = "NO SE OBTIENE";
			$scope.objetoAc.npIndicadormetcodigo = obj.codigo;
			$scope.objetoAc.npIndicadormetdescripcion = obj.descripcion;
			$scope.objetoAc.npIndicadormetadescriopcion = "NO SE OBTIENE";
			$scope.objetoAc.npUnidadmedidacodigo = obj.npCodigounidad;
			$scope.objetoAc.npUnidadmedidanombre = obj.npNombreunidad;
			$scope.objetoAc.npGrupomedidacodigo = obj.npCodigogrupo;
			$scope.objetoAc.npGrupomedidanombre = obj.npNombregrupo;
		}, function() {
		});
	};

	$scope.abrirIndicadorActividadSa = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalIndicadoresActividad.html',
			controller : 'ModalIndicadoresActividadController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objetoSa.proyectoindicadorid = obj.id;
			$scope.objetoSa.npIndicadorcodigo = obj.npIndicadorcodigo;
			$scope.objetoSa.npIndicadornombre = obj.npIndicadornombre;
			$scope.objetoSa.npIndicadordescripcion = "NO SE OBTIENE";
			$scope.objetoSa.npIndicadormetcodigo = obj.codigo;
			$scope.objetoSa.npIndicadormetdescripcion = obj.descripcion;
			$scope.objetoSa.npIndicadormetadescriopcion = "NO SE OBTIENE";
		}, function() {
		});
	};

	$scope.abrirUnidades = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalUnidad.html',
			controller : 'ModalUnidadController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			for (var i = 0; i < $scope.objetolista.length; i++) {
				if ($scope.objetolista[i].nivelactividadunidadid == obj.id) {
					SweetAlert.swal(
						"Formulacion Estrategica!",
						"La unidad ya esta en la lista!",
						"error"
					);
					return;
				}
			}
			var obj={
				id: null,
				tablarelacionid: $scope.editarId,
				nivelactividadejerfiscalid: $rootScope.ejefiscal,
				nivelactividadunidadid: obj.id,
				npCodigounidad: obj.codigopresup,
				npNombreunidad: obj.nombre
			};
			$scope.objetolista.push(obj);
		}, function() {
		});
	};

	$scope.form = {
        submit: function (form) {
        	console.log("GUARDAR===");
            var firstError = null;
        	console.log(form);
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
            	console.log("GUARDAndo-----------");
            	switch ($scope.nodeTipo) {
					case "PR":
		            	$scope.newobj = $scope.objetoPr;
						break;
					case "SP":
		            	$scope.newobj = $scope.objetoSp;
						break;
					case "PY":
		            	$scope.newobj = $scope.objetoPy;
	            		$scope.newobj.proyectometaTOs = $scope.objetolista;
						break;
					case "AC":
		            	$scope.newobj = $scope.objetoAc;
	            		$scope.newobj.nivelactividadTOs = $scope.objetolista;
						break;
					case "SA":
		            	$scope.newobj = $scope.objetoSa;
						break;
					default:
				}
        		console.log($scope.newobj);
            	formulacionEstrategicaFactory.guardar($scope.nodeTipo, $scope.newobj).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.edicionSubPrograma=false;
	 		             $scope.edicionProyecto=false;
	 		             $scope.edicionActividad=false;
	 		             $scope.edicionSubActividad=false;
	 		             $scope.objeto={};
	 		             SweetAlert.swal("Formulacion Estrategica!", "Registro grabado satisfactoriamente!", "success");
        			 }else{
	 		             SweetAlert.swal("Formulacion Estrategica!", resp.mensajes.msg, "error");
        			 }
        		})
            }
        },
        reset: function (form) {
            $scope.myModel = angular.copy($scope.master);
            form.$setPristine(true);
            $scope.edicion=false;
            $scope.edicionSubPrograma=false;
            $scope.edicionProyecto=false;
            $scope.edicionActividad=false;
            $scope.edicionSubActividad=false;
            $scope.objeto={};
        }
    };

	$scope.popupnpFechainicio = {
	    opened: false
	};
	$scope.opennpFechainicio = function() {
	    $scope.popupnpFechainicio.opened = true;
	}
	$scope.popupnpFechafin = {
	    opened: false
	};
	$scope.opennpFechafin = function() {
	    $scope.popupnpFechafin.opened = true;
	}
} ]);