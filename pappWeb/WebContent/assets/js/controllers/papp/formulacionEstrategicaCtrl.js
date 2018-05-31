'use strict';

app.controller('FormulacionEstrategicaController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","formulacionEstrategicaFactory","ejercicioFiscalFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, formulacionEstrategicaFactory,ejercicioFiscalFactory) {

	$scope.dateOptions = {
	    changeYear: true,
	    changeMonth: true,
	    yearRange: '2000:-0',    
    };
	$scope.arbol={};
	$scope.edicion=false;
	$scope.guardar=false;
	$scope.nuevoar=false;
	$scope.ejerciosFiscales=[];
	$scope.objeto={};
	$scope.objetolistaPy=[];
	$scope.objetolistaAc=[];
	$scope.editarId=null;
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		formulacionEstrategicaFactory.traer(
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
		cargarEF();
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
					//resp[i].nodePadre = node;
				}
				var nodes=JSON.parse(JSON.stringify(resp).split('"nombre":').join('"title":'));
				node.nodes=nodes;
				for (var i = 0; i < node.nodes.length; i++) {
					node.nodes[i].nodePadre = node;
				}
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
					//resp[i].nodePadre = node;
				}
				var nodes=JSON.parse(JSON.stringify(resp).split('"nombre":').join('"title":'));
				node.nodes=nodes;
				for (var i = 0; i < node.nodes.length; i++) {
					node.nodes[i].nodePadre = node;
				}
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
					//resp[i].nodePadre = node;
				}
				var nodes=JSON.parse(JSON.stringify(resp).split('"descripcion":').join('"title":'));
				node.nodes=nodes;
				for (var i = 0; i < node.nodes.length; i++) {
					node.nodes[i].nodePadre = node;
				}
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
					//resp[i].nodePadre = node;
				}
				var nodes=JSON.parse(JSON.stringify(resp).split('"descripcion":').join('"title":'));
				node.nodes=nodes;
				for (var i = 0; i < node.nodes.length; i++) {
					node.nodes[i].nodePadre = node;
				}
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
		$scope.nodeActivo=node;
		$scope.editarId=null;
		//console.log(node);
		$scope.nuevoar = true;
		$scope.guardar = true;
		if (node == null) { // Programa
			formulacionEstrategicaFactory.traerNuevo(
				"PR",
				0,
				$rootScope.ejefiscal
			).then(function(resp){
				//console.log(resp.json);
				if (!resp.estado) {
					SweetAlert.swal("Formulacion! - Nuevo Programa", resp.mensajes.msg, "error");
					return;
				}
				$scope.objetoPr=resp.json.programa;
				$scope.objetoPr.padre=0;
				$scope.editarId=$scope.objetoPr.id;
				$scope.nodeTipo = "PR";
				//console.log($scope.nuevoar);
				$scope.edicion=true;
			});
			//console.log($scope.nuevoar);
			return;
		}
		if (node.nodeTipo == "PR") { // SubPrograma
			formulacionEstrategicaFactory.traerNuevo(
				"SP",
				node.npNivelid,
				$rootScope.ejefiscal
			).then(function(resp){
				//console.log(resp.json);
				if (!resp.estado) {
					SweetAlert.swal("Formulacion! - Nuevo Subprograma", resp.mensajes.msg, "error");
					return;
				}
				$scope.objetoSp=resp.json.subprograma;
				//$scope.objetoSp.padre=node.npNivelid;
				$scope.editarId=$scope.objetoSp.id;
				$scope.nodeTipo = "SP";
				$scope.edicionSubPrograma=true;
			});
		}
		if (node.nodeTipo == "SP") { // Proyecto
			formulacionEstrategicaFactory.traerNuevo(
				"PY",
				node.npNivelid,
				$rootScope.ejefiscal
			).then(function(resp){
				//console.log(resp.json);
				if (!resp.estado) {
					SweetAlert.swal("Formulacion! - Nuevo Proyecto", resp.mensajes.msg, "error");
					return;
				}
				$scope.objetoPy=resp.json.proyecto;
				//$scope.objetoPy.padre=node.npNivelid;
				$scope.objetoPy.tipo="AN";
				$scope.objetolistaPy=[];
				$scope.editarId=$scope.objetoPy.id;
				$scope.nodeTipo = "PY";
				$scope.edicionProyecto=true;
			});
		}
		if (node.nodeTipo == "PY") { // Actividad
			formulacionEstrategicaFactory.traerNuevo(
				"AC",
				node.npNivelid,
				$rootScope.ejefiscal
			).then(function(resp){
				//console.log(resp.json);
				if (!resp.estado) {
					SweetAlert.swal("Formulacion! - Nueva Actividad", resp.mensajes.msg, "error");
					return;
				}
				$scope.objetoAc=resp.json.actividad;
				//$scope.objetoAc.padre=node.npNivelid;
				$scope.objetolistaAc=[];
				$scope.editarId=$scope.objetoAc.id;
				$scope.nodeTipo = "AC";
				$scope.edicionActividad=true;
			});
		}
		if (node.nodeTipo == "AC" || node.nodeTipo == "SA") { // SubActividad
			formulacionEstrategicaFactory.traerNuevo(
				"SA",
				node.id,
				$rootScope.ejefiscal
			).then(function(resp){
				//console.log(resp.json);
				if (!resp.estado) {
					SweetAlert.swal("Formulacion! - Nueva Subactividad", resp.mensajes.msg, "error");
					return;
				}
				$scope.objetoSa=resp.json.subactividad;
				//$scope.objetoSa.padre=node.npNivelid;
				$scope.editarId=$scope.objetoSa.id;
				$scope.nodeTipo = "SA";
				$scope.edicionSubActividad=true;
			});
		}
	}

	$scope.editar=function(node){
		$scope.nodeActivo=node;
		//console.log(node);
		if (node.nodeTipo == "PR") { // Programa
			formulacionEstrategicaFactory.traerProgramaEditar(node).then(function(resp){
				//console.log(resp.json);
				if (resp.estado) {
					$scope.objetoPr=resp.json.programa;
					$scope.editarId=$scope.objetoPr.id;
					$scope.nodeTipo="PR";
				}
				$scope.guardar=true;
				$scope.edicion=true;
			});
		}
		if (node.nodeTipo == "SP") { // SubPrograma
			formulacionEstrategicaFactory.traerSubProgramaEditar(node.id).then(function(resp){
				//console.log(resp.json);
				if (resp.estado) {
					$scope.objetoSp=resp.json.subprograma;
					$scope.editarId=$scope.objetoSp.id;
					$scope.nodeTipo="SP";
				}
				$scope.guardar=true;
				$scope.edicionSubPrograma=true;
			});
		}
		if (node.nodeTipo == "PY") { // Proyecto
			formulacionEstrategicaFactory.traerProyectoEditar(node.id).then(function(resp){
				//console.log(resp.json);
				if (resp.estado) {
					$scope.objetoPy=resp.json.proyecto;
					$scope.objetoPy.npFechainicio = toDate($scope.objetoPy.npFechainicio);
					$scope.objetoPy.npFechafin = toDate($scope.objetoPy.npFechafin);
					$scope.objetoPy.tipo="AN";
					$scope.editarId=$scope.objetoPy.id;
					$scope.objetolistaPy=resp.json.proyectometa;
					for (var i = 0; i < $scope.objetolistaPy.length; i++) {
						$scope.objetolistaPy[i].id.metaejerciciofiscalid = $scope.objetolistaPy[i].id.metaejerciciofiscalid.toString();
					}
					$scope.nodeTipo="PY";
				}
				$scope.guardar=true;
				$scope.edicionProyecto=true;
			});
		}
		if (node.nodeTipo == "AC") { // Actividad
			formulacionEstrategicaFactory.traerActividadEditar(node.id).then(function(resp){
				//console.log(resp.json);
				if (resp.estado) {
					$scope.objetoAc=resp.json.actividad;
					$scope.editarId=$scope.objetoAc.id;
					$scope.objetolistaAc=resp.json.nivelactividad;
					$scope.nodeTipo="AC";
				}
				$scope.guardar=true;
				$scope.edicionActividad=true;
			});
		}
		if (node.nodeTipo == "SA") { // SubActividad
			formulacionEstrategicaFactory.traerSubActividadEditar(node.id).then(function(resp){
				//console.log(resp.json);
				if (resp.estado) {
					$scope.objetoSa=resp.json.subactividad;
					$scope.editarId=$scope.objetoSa.id;
					$scope.nodeTipo="SA";
				}
				$scope.guardar=true;
				$scope.edicionSubActividad=true;
			});
		}
	};

	$scope.agregarDetallePy=function(){
		//documento pagina 15
		if ($scope.objetolistaPy == undefined) {
			$scope.objetolistaPy = [];
		}
		console.log($scope.editarId);
		var obj={
			id: {
				id: null, //$scope.editarId,
				metaejerciciofiscalid: null
			},
			proyectoid: $scope.editarId,
			descripcion: null
		};
		$scope.objetolistaPy.push(obj);
		//console.log($scope.objetolistaPy);
	}

	$scope.removerDetallePy=function(index){
		$scope.objetolistaPy.splice(index,1);
	}

	$scope.removerDetalleAc=function(index){
		formulacionEstrategicaFactory.eliminarActividadUnidad(
			$scope.objetolistaAc[index].id
		).then(function(resp){
			$scope.objetolistaAc.splice(index,1);
		});
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
			$scope.objetoAc.actividadindicadorid = obj.id.id;
			$scope.objetoAc.actividadindicadormetodoid = obj.id.metodoid;
			$scope.objetoAc.npIndicadorcodigo = obj.npIndicadorcodigo;
			$scope.objetoAc.npIndicadornombre = obj.npIndicadornombre;
			$scope.objetoAc.npIndicadordescripcion = obj.npIndicadordescripcion;
			$scope.objetoAc.npIndicadormetcodigo = obj.codigo;
			$scope.objetoAc.npIndicadormetdescripcion = obj.descripcion;
			$scope.objetoAc.npIndicadormetadescriopcion = obj.npIndicadormetadescriopcion;
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
			$scope.objetoSa.subactividadindicadorid = obj.id.id;
			$scope.objetoSa.subactividadindicadormetodoid = obj.id.metodoid;
			$scope.objetoSa.npIndicadorcodigo = obj.npIndicadorcodigo;
			$scope.objetoSa.npIndicadornombre = obj.npIndicadornombre;
			$scope.objetoSa.npIndicadordescripcion = obj.npIndicadordescripcion;
			$scope.objetoSa.npIndicadormetcodigo = obj.codigo;
			$scope.objetoSa.npIndicadormetdescripcion = obj.npIndicadormetadescriopcion;
			$scope.objetoSa.npIndicadormetadescriopcion = obj.npIndicadormetadescriopcion;
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
			for (var i = 0; i < $scope.objetolistaAc.length; i++) {
				if ($scope.objetolistaAc[i].nivelactividadunidadid == obj.id) {
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
			$scope.objetolistaAc.push(obj);
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
            	switch ($scope.nodeTipo) {
					case "PR":
		            	$scope.newobj = $scope.objetoPr;
						break;
					case "SP":
		            	$scope.newobj = $scope.objetoSp;
						break;
					case "PY":
		            	console.log($scope.objetoPy); 
		            	$scope.newobj = Object.assign({}, $scope.objetoPy);
		        		$scope.newobj.npFechainicio = toStringDate($scope.newobj.npFechainicio);
						$scope.newobj.npFechafin = toStringDate($scope.newobj.npFechafin);
						$scope.newobj.presupuestototal = Number($scope.newobj.presupuestototal.toString().replace(/[,]/g,""));
						$scope.newobj.presupuestoinicial = Number($scope.newobj.presupuestoinicial.toString().replace(/[,]/g,""));
						$scope.newobj.presupuestoplanificado = Number($scope.newobj.presupuestoplanificado.toString().replace(/[,]/g,""));
	            		$scope.newobj.proyectometaTOs = Array.from($scope.objetolistaPy);
						break;
					case "AC":
		            	$scope.newobj = Object.assign({}, $scope.objetoAc);
	            		$scope.newobj.nivelactividadTOs = Array.from($scope.objetolistaAc);
						break;
					case "SA":
		            	$scope.newobj = $scope.objetoSa;
						break;
					default:
				}
        		//console.log($scope.newobj);
            	formulacionEstrategicaFactory.guardar($scope.nodeTipo, $scope.newobj).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.edicionSubPrograma=false;
	 		             $scope.edicionProyecto=false;
	 		             $scope.edicionActividad=false;
	 		             $scope.edicionSubActividad=false;
	 		             if ($scope.nodeActivo == null) {
	 		            	 $scope.consultar();
	 		             } else {
	 		            	 if ($scope.nodeActivo.nodePadre) {
	 		            		 $scope.nodeActivo = $scope.nodeActivo.nodePadre;
	 		            	 }
	        				 if ($scope.nodeActivo.iscargado) {
	        					 $scope.nodeActivo.iscargado = false;
	        					 if ($scope.nodeActivo.nodes) {
	        						 delete $scope.nodeActivo.nodes;
	        						 console.log($scope.nodeActivo);
	        						 console.log($scope.nodeActivo.nodes);
	        					 }
	        				 }
	        				 if ($scope.nodeActivo.nodeTipo == "PR") {
	        					 $scope.consultar();
	        				 } else {
	        					 $scope.cargarHijos($scope.nodeActivo);
	        				 }
	 		             }
	 		             SweetAlert.swal("Formulacion Estrategica!", "Registro grabado satisfactoriamente!", "success");
        			 }else{
	 		             SweetAlert.swal("Formulacion Estrategica!", resp.mensajes.msg, "error");
        			 }
        		})
            }
        },
        reset: function (form) {
            //$scope.myModel = angular.copy($scope.master);
            //form.$setPristine(true);
            $scope.edicion=false;
            $scope.edicionSubPrograma=false;
            $scope.edicionProyecto=false;
            $scope.edicionActividad=false;
            $scope.edicionSubActividad=false;
			$scope.objetoPr={};
			$scope.objetoSp={};
			$scope.objetoPy={};
			$scope.objetoAc={};
			$scope.objetoPy={};
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

	$scope.devuelveColorNode = function(tipo) {
    	switch (tipo) {
			case "PR":
	        	return "#3333ff";
			case "SP":
	        	return "#5cd65c";
			case "PY":
	        	return "#ffd633";
			case "AC":
	        	return "#ff4d4d";
			case "SA":
	        	return "#00b3b3";
			default:
	        	return "auto";
		}
	}

	function cargarEF() {
		if ($scope.ejerciosFiscales.length != 0) return;
		ejercicioFiscalFactory.traerEjerciciosFiltro(pagina, null, "A")
		.then(function(resp) {
			//console.log(resp);
			$scope.ejerciosFiscales=resp;
		});
	}

	function toDate(fuente) {
		try {
			var parts = fuente.split('/');
		} catch (err) {
			return null;
		}
		//console.log(parts, parts[2]*1,parts[1]-1,parts[0]*1);
		return new Date(parts[2]*1,parts[1]-1,parts[0]*1, 0, 0, 0, 0); 
	}

	function toStringDate(fuente) {
		if (fuente == null) {
			return null;
		}
		try {
			var parts = fuente.toISOString();
			parts = parts.split('T');
			parts = parts[0].split('-');
		} catch (err) {
			return null;
		}
		return parts[2] + "/" + parts[1] + "/" + parts[0]; 
	}
} ]);