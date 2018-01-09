'use strict';

app.controller('PlanificacionUEController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","PlanificacionUEFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams,PlanificacionUEFactory) {

	$scope.nombreFiltro=null;
	$scope.codigoFiltro=null;
	$scope.estadoFiltro=null;
	$scope.edicion=false;
	$scope.editar=false;
	$scope.divPlanificacionAnual=false;
	$scope.unidadid=null;
	$scope.node=null;
	$scope.data=[];

	$scope.limpiarEdicion = function() {
		$scope.divEditarDistribucion=false;
		$scope.divPlanificacionAnual=true;
		$scope.divEditarDistribucion=false;
		$scope.mPlanificadaID=0;
		$scope.mAjustadaID=0;
		$scope.divMetaDistribucionPlanificada=false;
		$scope.divMetaDistribucionAjustada=false;
		$scope.divActividad=false;
		$scope.divSubActividad=false;
		$scope.divTarea=false;
		$scope.divSubTarea=false;
		$scope.divItem=false;
		$scope.dibSubItem=false;
		$scope.editar=false;
		$scope.divPlanificacionAnualVista=false;
		//$scope.=false;

		$scope.objeto=null;
		$scope.detalles=null;
		$scope.objetoPA=null;
		$scope.objetoPlanificada=null;
		$scope.detallesPlanificada=null;
		$scope.objetoAjustada=null;
		$scope.detallesAjustada=null;
	}

    $scope.pagina = 1;
    $scope.aplicafiltro=false;

	$scope.init=function() {
		$scope.limpiarEdicion();
		$scope.consultar();
	}

	$scope.consultar=function() {
		PlanificacionUEFactory.traer(
			$scope.pagina,
			$rootScope.ejefiscal
		).then(function(resp){
			console.log(resp.json);
        	$scope.data = resp.json.result;
            $scope.total = resp.json.total.valor;
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
		$scope.data=[];
		PlanificacionUEFactory.traerFiltro(
				$scope.pagina,
			$rootScope.ejefiscal,
			$scope.nombreFiltro,
			$scope.codigoFiltro,
			$scope.estadoFiltro
		).then(function(resp){
        	$scope.data = resp.json.result;
            $scope.total = resp.json.total.valor;
		})
	}

	$scope.limpiar=function(){
		$scope.nombreFiltro=null;
		$scope.codigoFiltro=null;
		$scope.estadoFiltro=null;
    	$scope.pagina=1;
    	$scope.aplicafiltro=false;
		$scope.consultar();
	};

	$scope.editarDistribucionPlanificado=function(){
		$scope.divEditarDistribucion=true;
		$scope.metaDistribucion('P');
	}

	$scope.editarDistribucionAjustado=function(){
		$scope.divEditarDistribucion=true;
		$scope.metaDistribucion('A');
	}

	$scope.metaDistribucion = function(
		tipometa
	) {
		var id;
		if (tipometa == "P") {
			if ($scope.detallesPlanificada != null) {
				$scope.divMetaDistribucionPlanificada=true;
				$scope.divMetaDistribucionAjustada=false;
				return;
			}
			id = $scope.mPlanificadaID;
		}
		if (tipometa == "A") {
			if ($scope.detallesAjustada != null) {
				$scope.divMetaDistribucionPlanificada=false;
				$scope.divMetaDistribucionAjustada=true;
				return;
			}
			id = $scope.mAjustadaID;
		}
		PlanificacionUEFactory.editarMDP(
			$scope.detalles[id].id.id,
			$scope.detalles[id].id.acumid,
			$scope.detalles[id].id.unidadid,
			"AC",
			tipometa,
			$rootScope.ejefiscalobj.anio
		).then(function(resp) {
			console.log(resp);
			if (!resp.estado) return;
			if (tipometa == "P") {
				$scope.objetoPlanificada=resp.json.cronograma;
				$scope.detallesPlanificada=resp.json.cronogramalinea;
				$scope.divMetaDistribucionPlanificada=true;
				$scope.divMetaDistribucionAjustada=false;
			}
			if (tipometa == "A") {
				$scope.objetoAjustada=resp.json.cronograma;
				$scope.detallesAjustada=resp.json.cronogramalinea;
				$scope.divMetaDistribucionPlanificada=false;
				$scope.divMetaDistribucionAjustada=true;
			}
		});
	}

	$scope.nuevo=function(node){
		console.log(node);
		$scope.objeto=null;
		if (node.nodeTipo == "SA") {// Tarea
			PlanificacionUEFactory.nuevo(
				"TA",
				node.id,
				$rootScope.ejefiscal + "/unidadid=" + node.npIdunidad
			).then(function(resp){
				console.log(resp);
				if (!resp.estado) return;
				$scope.editar=true;
				$scope.objeto=Object.assign({}, resp.json.tareaunidad);
				$scope.divPlanificacionAnual=false;
				$scope.divTarea=true;
				console.log("NUEVO OBJETO tarea:", $scope.objeto);
			});
		}
		if (node.nodeTipo == "TA") {// Tarea
			PlanificacionUEFactory.nuevo(
				"ST",
				node.id,
				$rootScope.ejefiscal + "/unidadid=" + node.npIdunidad
			).then(function(resp){
				console.log(resp);
				if (!resp.estado) return;
				$scope.editar=true;
				$scope.objeto=Object.assign({}, resp.json.subtareaunidad);
				$scope.detalles=resp.json.subtareaunidadacumulador;
				$scope.divPlanificacionAnual=false;
				$scope.divSubTarea=true;
				console.log("NUEVO OBJETO subtarea:", $scope.objeto);
			});
		}
	}

	$scope.editarPlanificacionAnual=function(node){
		console.log(node);
		$scope.objeto=null;
		if (node.nodeTipo == "AC") {
			PlanificacionUEFactory.editar(
				node.nodeTipo,
				node.tablarelacionid,
				"unidadid=" + node.npIdunidad
			).then(function(resp){
				console.log(resp);
				if (!resp.estado) return;
				if ($scope.planificacionUE.npestadopresupuesto == "Planificado") {
					$scope.editar=true;
				} else {
					$scope.editar=false;
				}
				$scope.objeto=Object.assign({}, resp.json.actividad, resp.json.actividadunidad);
			    $scope.objeto.npFechainicio=toDate($scope.objeto.npFechainicio);
			    $scope.objeto.npFechafin=toDate($scope.objeto.npFechafin);
				$scope.detalles=resp.json.actividadunidadacumulador;
				for (var i = 0; i < $scope.detalles.length; i++) {
					if ($scope.detalles[i].tipo == "P") {
						$scope.mPlanificadaID = i;
					}
					if ($scope.detalles[i].tipo == "A") {
						$scope.mAjustadaID = i;
					}
				}
				$scope.divPlanificacionAnual=false;
				$scope.divActividad=true;
				console.log("OBJETO:", $scope.objeto);
			});
		}
		if (node.nodeTipo == "SA") {
			PlanificacionUEFactory.editar(
				node.nodeTipo,
				node.tablarelacionid,
				"unidadid=" + node.npIdunidad +
				"&nivelactividadid=" + node.padreID
			).then(function(resp){
				console.log(resp);
				if (!resp.estado) return;
				if ($scope.planificacionUE.npestadopresupuesto == "Planificado") {
					$scope.editar=true;
				} else {
					$scope.editar=false;
				}
				$scope.objeto=Object.assign({}, resp.json.subactividadunidad);
			    //$scope.detalles=resp.json.actividadunidadacumulador;
				/* for (var i = 0; i < $scope.detalles.length; i++) {
					if ($scope.detalles[i].tipo == "P") {
						$scope.mPlanificadaID = i;
					}
					if ($scope.detalles[i].tipo == "A") {
						$scope.mAjustadaID = i;
					}
				} */
				$scope.divPlanificacionAnual=false;
				$scope.divSubActividad=true;
				console.log("OBJETO:", $scope.objeto);
			});
		}
		if (node.nodeTipo == "TA") {
			PlanificacionUEFactory.editar(
				node.nodeTipo,
				node.tablarelacionid,
				"nivelactividad=" + node.id
			).then(function(resp){
				console.log(resp);
				if (!resp.estado) return;
				if ($scope.planificacionUE.npestadopresupuesto == "Planificado") {
					$scope.editar=true;
				} else {
					$scope.editar=false;
				}
				$scope.objeto=Object.assign({}, resp.json.tareaunidad);
			    //$scope.detalles=resp.json.actividadunidadacumulador;
				/* for (var i = 0; i < $scope.detalles.length; i++) {
					if ($scope.detalles[i].tipo == "P") {
						$scope.mPlanificadaID = i;
					}
					if ($scope.detalles[i].tipo == "A") {
						$scope.mAjustadaID = i;
					}
				} */
				$scope.divPlanificacionAnual=false;
				$scope.divTarea=true;
				console.log("OBJETO:", $scope.objeto);
			});
		}
		if (node.nodeTipo == "ST") {
			PlanificacionUEFactory.editar(
				node.nodeTipo,
				node.tablarelacionid,
				"nivelactividad=" + node.padreID
			).then(function(resp){
				console.log(resp);
				if (!resp.estado) return;
				if ($scope.planificacionUE.npestadopresupuesto == "Planificado") {
					$scope.editar=true;
				} else {
					$scope.editar=false;
				}
				$scope.objeto=Object.assign({}, resp.json.subtareaunidad);
				$scope.detalles=resp.json.subtareaunidadacumulador;
				$scope.divPlanificacionAnual=false;
				$scope.divSubTarea=true;
				console.log("OBJETO:", $scope.objeto);
			});
		}
		if (node.nodeTipo == "IT") {
			PlanificacionUEFactory.editar(
				node.nodeTipo,
				node.tablarelacionid,
				"nivelactividad=" + node.padreID
			).then(function(resp){
				console.log(resp);
				if (!resp.estado) return;
				if ($scope.planificacionUE.npestadopresupuesto == "Planificado") {
					$scope.editar=true;
				} else {
					$scope.editar=false;
				}
				$scope.objeto=Object.assign({}, resp.json.itemunidad);
				//$scope.detalles=resp.json.subtareaunidadacumulador;
				$scope.divPlanificacionAnual=false;
				$scope.divItem=true;
				console.log("Editar OBJETO:", $scope.objeto);
			});
		}
		if (node.nodeTipo == "SI") {
			//unidadid={unidadid}&ejerciciofiscal={ejerciciofiscal}&actividadid={tablarealacionid}
			PlanificacionUEFactory.editar(
				node.nodeTipo,
				node.tablarelacionid,
				"unidadid=" + node.npIdunidad +
				"&ejerciciofiscal=" + $rootScope.ejefiscal +
				"&actividadid=" + $scope.planificacionUE.tablarelacionid//IVAN
			).then(function(resp){
				console.log(resp);
				if (!resp.estado) return;
				if ($scope.planificacionUE.npestadopresupuesto == "Planificado") {
					$scope.editar=true;
				} else {
					$scope.editar=false;
				}
				$scope.objeto=Object.assign({}, resp.json.subitemunidad);
				//$scope.detalles=resp.json.subtareaunidadacumulador;
				$scope.divPlanificacionAnual=false;
				$scope.divSubItem=true;
				console.log("Editar OBJETO:", $scope.objeto);
			});
		}
	};

	$scope.regresarPUE=function(obj) {
		$scope.edicion = false;
		$scope.arbol = [];
		$scope.objeto = {};
		$scope.details = [];
	}

	$scope.cargarPlanificacionAnual=function(obj) {
		console.log(obj);
		var id = obj.id;
		$scope.edicion=true;
		$scope.planificacionUE=obj;
		$scope.unidadid = obj.npacitividadunidad;
		$scope.dataPA=[];
		$scope.divPlanificacionAnual=true;
		PlanificacionUEFactory.traerPAactividad(
			id,
			$rootScope.ejefiscal
		).then(function(resp){
			//console.log(resp)
			if (resp.meta)
				$scope.dataPA=resp;
			for (var i = 0; i < $scope.dataPA.length; i++) {
				$scope.dataPA[i].nodeTipo = "AC";
			}
			$scope.arbol = JSON.parse(JSON.stringify($scope.dataPA).split('"descripcionexten":').join('"title":'));
			$scope.dataPA = [];
			//console.log($scope.arbol)
			$scope.divPlanificacionAnual=true;
		})
	}

	$scope.vista=function(node){
		console.log(node);
		$scope.node = node;
		$scope.divMenuActividad = false;
		$scope.divMenuSubitems = false;
		$scope.objetoVista = null;
		if (node.nodeTipo == "AC") {
			PlanificacionUEFactory.traerPAverActividad(
				node.tablarelacionid,
				node.npIdunidad,
				$rootScope.ejefiscal
			).then(function(resp){
				console.log(node.nodeTipo);
				if (resp.estado) {
					$scope.objetoVista=resp.json.actividadplanificacion;
					$scope.divPlanificacionAnualVista=true;
					$scope.divMenuActividad = true;
				}
			})
		}
/*		if (node.nodeTipo == "SI") {
			PlanificacionUEFactory.traerPAverSubitem(
				node.tablarelacionid,
				node.npIdunidad,
				$rootScope.ejefiscal
			).then(function(resp){
				console.log(node.nodeTipo);
				if (resp.estado) {
					$scope.objetoVista=resp.json.actividadplanificacion;
					$scope.divPlanificacionAnualVista=true;
					$scope.divMenuSubitems = true;
				}
			})
		}*/
	};

	$scope.cargarHijos=function(node){
		console.log(node);
		if (!node.iscargado)
		    node.iscargado=true;
		else
			return;

		var tipo = "";
		console.log(node.nodeTipo);
		switch (node.nodeTipo) {
			case "AC":
				tipo = "SA";
				break;
			case "SA":
				tipo = "TA";
				break;
			case "TA":
				tipo = "ST";
				break;
			case "ST":
				tipo = "IT";
				break;
			case "IT":
				tipo = "SI";
				break;
			default:
				return;
				break;
		}
		console.log(tipo);
	    PlanificacionUEFactory.traerPAhijos(
    		tipo,
    		node.id,
    		node.npIdunidad,
    		$rootScope.ejefiscal
		).then(function(resp){
			console.log(resp);
			for (var i = 0; i < resp.length; i++) {
				resp[i].nodeTipo = tipo;
				resp[i].padreID = node.id;
			}
			var nodes;
			if (tipo == "AC" || tipo == "SA") {
				nodes=JSON.parse(JSON.stringify(resp).split('"descripcionexten":').join('"title":'));
			} else {
				nodes=JSON.parse(JSON.stringify(resp).split('"npdescripcion":').join('"title":'));
				for (var i = 0; i < nodes.length; i++) {
					nodes[i].title = nodes[i].npcodigo + " - " + nodes[i].title;
				}
			}
			node.nodes=nodes;
		});
	}

	$scope.abrirItemCodigo = function(index) {
		//console.log("aqui:", $scope.objeto);
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalItems.html',
			controller : 'ModalItemController',
			size : 'lg',
			resolve : {
				tipo : function() {
					return $scope.objeto.tipo;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.itemunidaditemid = obj.id;
			$scope.objeto.npcodigoitem = obj.codigo;
			$scope.objeto.npnombreitem = obj.nombre;		
		}, function() {
		});
	};

	$scope.abrirObraCodigo = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalObra.html',
			controller : 'ModalObraController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.itemunidadobraid = obj.id;
			$scope.objeto.npcodigoobra = obj.codigo;
			$scope.objeto.npnombreobra = obj.nombre;		
		}, function() {
		});
	};

	$scope.abrirFuenteFinanciamientoCodigo = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalFuenteFinanciamiento.html',
			controller : 'ModalFuenteFinanciamientoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.itemunidadfuentefinanid = obj.id;
			$scope.objeto.npcodigofuente = obj.codigo;
			$scope.objeto.npnombrefuente = obj.nombre;		
		}, function() {
		});
	};

	$scope.abrirOrganismoCodigo = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalOrganismo.html',
			controller : 'ModalOrganismoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.itemunidadorganismoid = obj.id;
			$scope.objeto.npcodigoorganismo = obj.codigo;
			$scope.objeto.npnombreorganismo = obj.nombre;		
			$scope.objeto.npcodigoorgpres = "NO SE OBTIENE";
			$scope.objeto.npnombreorgpres = "NO SE OBTIENE";		
		}, function() {
		});
	};

	$scope.abrirCantonCodigo = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalDiviviones.html',
			controller : 'ModalDivisionGeograficaController',
			size : 'lg',
			resolve: {
				pais: function() {
					return null;
				},
				provincia: function() {
					return null;
				},
				tipo : function() {
					return "C";
				}
			}
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.itemunidadcantonid = obj.id;
			$scope.objeto.npcodigocanton = obj.codigo;
			$scope.objeto.npnombrecanton = obj.nombre;
		}, function() {
		});
	}

	$scope.form = {
        submit: function(form,name) {
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
            	eval("$scope.submit" + name + "(form);");
            }
        },
        reset: function(form) {
            form.$setPristine(true);
            $scope.limpiarEdicion();
        }
    };

	$scope.submitformActividad = function(form) {
    	var tObj={};
    	tObj=Object.assign(tObj, $scope.objeto);
    	tObj.npFechainicio=toStringDate(tObj.npFechainicio);
    	tObj.npFechafin=toStringDate(tObj.npFechafin);
    	tObj.actividadunidadacumulador=$scope.detalles;
    	PlanificacionUEFactory.guardarActividades("AC",tObj).then(function(resp){
			if (resp.estado) {
				form.$setPristine(true);
				$scope.limpiarEdicion();
	            //$scope.limpiar();
	            SweetAlert.swal("Planificacion UE! - Actividad", "Registro registrado satisfactoriamente!", "success");
			} else {
				SweetAlert.swal("Planificacion UE! - Actividad", resp.mensajes.msg, "error");
			}
		})
	}

	$scope.resetformActividad = function(form) {
        //$scope.myModel = angular.copy($scope.master);
        form.$setPristine(true);
        $scope.limpiarEdicion();
	}

	$scope.submitformMetaDistribucionPlanificada = function(form) {
    	var tObj={};
    	tObj=Object.assign(tObj, $scope.objetoPlanificada);
    	tObj.cronogramalineaTOs=$scope.detallesPlanificada;
    	PlanificacionUEFactory.guardarMetaDistribucionPlanificada(tObj).then(function(resp){
			if (resp.estado) {
				form.$setPristine(true);
	            SweetAlert.swal("Planificacion UE! - Distribucion Planificada", "Registro registrado satisfactoriamente!", "success");
			} else {
				SweetAlert.swal("Planificacion UE! - Distribucion Planificada", resp.mensajes.msg, "error");
			}
		})
	}

	$scope.resetformMetaDistribucionPlanificada = function(form) {
        form.$setPristine(true);
        $scope.limpiarEdicion();
	}

	$scope.submitformMetaDistribucionAjustada = function(form) {
    	var tObj=$scope.objetoAjustada;
    	tObj.cronogramalineaTOs=$scope.detallesAjustada;
    	PlanificacionUEFactory.guardarMetaDistribucionAjustada(tObj).then(function(resp){
			if (resp.estado) {
				form.$setPristine(true);
	            SweetAlert.swal("Planificacion UE! - Distribucion Ajustada", "Registro registrado satisfactoriamente!", "success");
			} else {
				SweetAlert.swal("Planificacion UE! - Distribucion Ajustada", resp.mensajes.msg, "error");
			}
		})
	}

	$scope.resetformMetaDistribucionAjustada = function(form) {
        form.$setPristine(true);
        $scope.limpiarEdicion();
	}

	$scope.submitformSubActividad = function(form) {
    	var tObj=$scope.objeto;
    	//tObj.actividadunidadacumulador=$scope.detalles;
    	tObj.ponderacion = Number.parseInt(tObj.ponderacion);
    	PlanificacionUEFactory.guardarActividades("SA",tObj).then(function(resp){
			if (resp.estado) {
				form.$setPristine(true);
				$scope.limpiarEdicion();
	            //$scope.limpiar();
	            SweetAlert.swal("Planificacion UE! - Subactividad", "Registro registrado satisfactoriamente!", "success");
			} else {
				SweetAlert.swal("Planificacion UE! - Subactividad", resp.mensajes.msg, "error");
			}
		})
	}

	$scope.resetformSubActividad = function(form) {
        //$scope.myModel = angular.copy($scope.master);
        form.$setPristine(true);
        $scope.limpiarEdicion();
	}

	$scope.submitformTarea = function(form) {
    	var tObj=$scope.objeto;
    	//tObj.actividadunidadacumulador=$scope.detalles;
    	PlanificacionUEFactory.guardarActividades("TA",tObj).then(function(resp){
			if (resp.estado) {
				form.$setPristine(true);
				$scope.limpiarEdicion();
	            //$scope.limpiar();
	            SweetAlert.swal("Planificacion UE! - Tarea", "Registro registrado satisfactoriamente!", "success");
			} else {
				SweetAlert.swal("Planificacion UE! - Tarea", resp.mensajes.msg, "error");
			}
		})
	}

	$scope.resetformTarea = function(form) {
        //$scope.myModel = angular.copy($scope.master);
        form.$setPristine(true);
        $scope.limpiarEdicion();
	}

	$scope.submitformSubTarea = function(form) {
    	var tObj=$scope.objeto;
    	//tObj.actividadunidadacumulador=$scope.detalles;
    	PlanificacionUEFactory.guardarActividades("ST",tObj).then(function(resp){
			if (resp.estado) {
				form.$setPristine(true);
				$scope.limpiarEdicion();
	            //$scope.limpiar();
	            SweetAlert.swal("Planificacion UE! - Subtarea", "Registro registrado satisfactoriamente!", "success");
			} else {
				SweetAlert.swal("Planificacion UE! - Subtarea", resp.mensajes.msg, "error");
			}
		})
	}

	$scope.resetformSubTarea = function(form) {
        //$scope.myModel = angular.copy($scope.master);
        form.$setPristine(true);
        $scope.limpiarEdicion();
	}

	$scope.submitformItem = function(form) {
    	var tObj=$scope.objeto;
    	//tObj.actividadunidadacumulador=$scope.detalles;
    	PlanificacionUEFactory.guardarActividades("IT",tObj).then(function(resp){
			if (resp.estado) {
				form.$setPristine(true);
				$scope.limpiarEdicion();
	            //$scope.limpiar();
	            SweetAlert.swal("Planificacion UE! - Item", "Registro registrado satisfactoriamente!", "success");
			} else {
				SweetAlert.swal("Planificacion UE! - Item", resp.mensajes.msg, "error");
			}
		})
	}

	$scope.resetformItem = function(form) {
        //$scope.myModel = angular.copy($scope.master);
        form.$setPristine(true);
        $scope.limpiarEdicion();
	}

	$scope.submitformSubItem = function(form) {
    	var tObj=$scope.objeto;
    	//tObj.actividadunidadacumulador=$scope.detalles;
    	PlanificacionUEFactory.guardarActividades("SI",tObj).then(function(resp){
			if (resp.estado) {
				form.$setPristine(true);
				$scope.limpiarEdicion();
	            //$scope.limpiar();
	            SweetAlert.swal("Planificacion UE! - Subitem", "Registro registrado satisfactoriamente!", "success");
			} else {
				SweetAlert.swal("Planificacion UE! - Subitem", resp.mensajes.msg, "error");
			}
		})
	}

	$scope.resetformSubItem = function(form) {
        //$scope.myModel = angular.copy($scope.master);
        form.$setPristine(true);
        $scope.limpiarEdicion();
	}

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

	function toDate(fuente) {
		if (fuente === null || fuente === "") {
			return null;
		}
		try {
			var parts = fuente.split('/');
		} catch (err) {
			return new Date();
		}
		return new Date(parts[2]*1,parts[1]-1,parts[0]*1, 0, 0, 0, 0); 
	}

	function toStringDate(fuente) {
		if (fuente === null || fuente === "") {
			return "";
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

	$scope.devuelveColorNode = function(tipo) {
    	switch (tipo) {
			case "AC":
	        	return "#3333ff";
			case "SA":
	        	return "#5cd65c";
			case "TA":
	        	return "#ffd633";
			case "ST":
	        	return "#ff4d4d";
			case "IT":
	        	return "#00b3b3";
			default:
	        	return "auto";
		}
	}
} ]);
