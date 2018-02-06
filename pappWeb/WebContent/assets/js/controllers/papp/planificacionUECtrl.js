app.controller('PlanificacionUEController', [ "$scope","$rootScope","$aside","$uibModal","SweetAlert","$filter", "ngTableParams","PlanificacionUEFactory", "AprobacionPlanificacionFactory",
	function($scope,$rootScope,$aside,$uibModal,SweetAlert,$filter, ngTableParams,PlanificacionUEFactory, AprobacionPlanificacionFactory) {

	$scope.codigoFiltro=null;
	$scope.nombreFiltro=null;
	$scope.estadoFiltro=null;
	$scope.edicion=false;
	$scope.editar=false;
	$scope.divPlanificacionAnual=false;
	$scope.unidadid=null;
	$scope.node=null;
	$scope.data=[];
	$scope.novista = true;;
	$scope.objUnidad = 0;
	$scope.esnuevo=false;
	$scope.vLimpio=false;

	$scope.limpiarEdicion = function() {
		$scope.divEditarDistribucion=false;
		$scope.divPlanificacionAnual=true;
		$scope.mPlanificadaID=0;
		$scope.mAjustadaID=0;
		$scope.divMetaDistribucionPlanificada=false;
		$scope.divMetaDistribucionAjustada=false;
		$scope.divMetaDistribucionDevengo=false;
		$scope.divActividad=false;
		$scope.divSubActividad=false;
		$scope.divTarea=false;
		$scope.divSubTarea=false;
		$scope.divItem=false;
		$scope.divSubItem=false;
		$scope.editar=false;
		$scope.divPlanificacionAnualVista=false;
		$scope.npTotalPlanificado = 0;
		$scope.npTotalAjustado = 0;

		$scope.objeto=null;
		$scope.detalles=null;
		$scope.objetoPA=null;
		$scope.objetoPlanificada=null;
		$scope.detallesPlanificada=null;
		$scope.objetoAjustada=null;
		$scope.detallesAjustada=null;
		$scope.objetoDevengo=null;
		$scope.detallesDevengo=null;
		$scope.volver();
		if ($scope.nodeActivo != undefined && $scope.nodeActivo.siEditar != undefined) {
			$scope.edicion = false;
    		if ($scope.nodeActivo.siEditar == "P") {
    			$scope.aprobacionPlanificacion = true;
    		} else {
    			$scope.aprobacionAjustado = true;
    		}
		}
	}

	var pagina = 1;

	$scope.init=function() {
		$scope.limpiarEdicion();
		$scope.consultar();
	}

	$scope.consultar=function() {
		PlanificacionUEFactory.traer(
			pagina,
			$rootScope.ejefiscal
		).then(function(resp){
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
				pagina = params.page();
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
//		$scope.data=[];
		PlanificacionUEFactory.traerFiltro(
			pagina,
			$rootScope.ejefiscal,
			$scope.codigoFiltro,
			$scope.nombreFiltro,
			$scope.estadoFiltro
		).then(function(resp){
			if (resp.meta) $scope.data=resp;
		})
	}

	$scope.limpiar=function(){
		$scope.nombreFiltro=null;
		$scope.codigoFiltro=null;
		$scope.estadoFiltro=null;
		
		$scope.consultar();
	};

	$scope.editarDistribucionPlanificado=function(){
		if ($scope.objeto.id == 0) {
			return;
		}
		$scope.divEditarDistribucion=true;
		$scope.metaDistribucion('A');
		$scope.divMetaDistribucionPlanificada=true;
		$scope.divMetaDistribucionAjustada=false;
		$scope.divMetaDistribucionDevengo=false;
	}

	$scope.editarDistribucionAjustado=function(){
		$scope.divEditarDistribucion=true;
		$scope.metaDistribucion('A');
	}

	$scope.editarDistribucionDevengo=function(){
		$scope.divEditarDistribucion=true;
		$scope.metaDistribucion('D');
	}

	$scope.metaDistribucion = function(
		tipometa
	) {
		var id;
		if (tipometa == "P") {
			if ($scope.detallesPlanificada != null) {
				$scope.divMetaDistribucionPlanificada=true;
				$scope.divMetaDistribucionAjustada=false;
				$scope.divMetaDistribucionDevengo=false;
				return;
			}
			id = $scope.mPlanificadaID;
		}
		if (tipometa == "A") {
			if ($scope.detallesAjustada != null) {
				$scope.divMetaDistribucionPlanificada=false;
				$scope.divMetaDistribucionAjustada=true;
				$scope.divMetaDistribucionDevengo=false;
				return;
			}
			id = $scope.mAjustadaID;
		}
		if (tipometa == "D") {
			if ($scope.detallesDevengo != null) {
				$scope.divMetaDistribucionPlanificada=false;
				$scope.divMetaDistribucionAjustada=false;
				$scope.divMetaDistribucionDevengo=true;
				return;
			}
			id = $scope.mDevengoID;	
		}

		PlanificacionUEFactory.editarMDP(
			$scope.detalles[id].id.id,
			$scope.detalles[id].id.acumid,
			($scope.detalles[id].id.unidadid !== undefined? $scope.detalles[id].id.unidadid: $scope.objUnidad),
			($scope.divSubItem? "SI": ($scope.divSubTarea? "ST": "AC")),
			tipometa,
			$rootScope.ejefiscalobj.anio
		).then(function(resp) {
			//console.log(resp);
			if (!resp.estado) return;
			if (tipometa == "P") {
				$scope.objetoPlanificada=resp.json.cronograma;
				$scope.detallesPlanificada=resp.json.cronogramalinea;
				if ($scope.objetoPlanificada.unidadtiempo == "") {
					$scope.objetoPlanificada.unidadtiempo = "ME";
					$scope.distribucionValores("P");
				}
				$scope.divMetaDistribucionPlanificada=true;
				$scope.divMetaDistribucionAjustada=false;
				$scope.divMetaDistribucionDevengo=false;
				$scope.modificarMetaPlanificada(true);
			}
			if (tipometa == "A") {
				$scope.objetoAjustada=resp.json.cronograma;
				$scope.detallesAjustada=resp.json.cronogramalinea;
				if ($scope.objetoAjustada.unidadtiempo == "") {
					$scope.objetoAjustada.unidadtiempo = "ME";
					$scope.distribucionValores("A");
				}
				$scope.divMetaDistribucionPlanificada=false;
				$scope.divMetaDistribucionAjustada=true;
				$scope.divMetaDistribucionDevengo=false;
				$scope.modificarMetaAjustada(true);
			}
			if (tipometa == "D") {
				$scope.objetoDevengo=resp.json.cronograma;
				$scope.detallesDevengo=resp.json.cronogramalinea;
				if ($scope.objetoDevengo.unidadtiempo == "") {
					$scope.objetoDevengo.unidadtiempo = "ME";
					$scope.distribucionValores("D");
				}
				$scope.divMetaDistribucionPlanificada=false;
				$scope.divMetaDistribucionAjustada=false;
				$scope.divMetaDistribucionDevengo=true;
			}
			if ($scope.detallesPlanificada == null) {
				$scope.metaDistribucion('P');
			}
			$scope.vLimpio=false;
		});
	}

	//ng-change="modificarMetaPlanificada(treu);"
	$scope.modificarMetaPlanificada = function(sumar) {
/*		$scope.aDistribuirP = ($scope.divActividad
			? $scope.detalles[$scope.mPlanificadaID].metavalor
			: ($scope.divSubTarea
				? $scope.detalles[$scope.mPlanificadaID].cantidad
				: $scope.npTotalPlanificado
			)
		);
*/
		$scope.aDistribuirP = Number($scope.divActividad || $scope.divSubTarea
			? $scope.detalles[$scope.mPlanificadaID].cantidad
			: $scope.npTotalPlanificado
		);
		if (sumar != undefined) {
			$scope.sumarValoresP();
		}
	}

	$scope.sumarValoresP = function() {
		$scope.totalPlanificadaV = 0;
		$scope.totalPlanificadaP = 0;
		for (var i = 0; i < 12; i++) {
			$scope.totalPlanificadaV += $scope.detallesPlanificada[i].valor;
			$scope.totalPlanificadaP += $scope.detallesPlanificada[i].porcentaje;
		}
		$scope.diferenciaPlanificadaV = Number($scope.aDistribuirP.toFixed(2)) - Number($scope.totalPlanificadaV.toFixed(2));
		$scope.diferenciaPlanificadaP = 100 - Number($scope.totalPlanificadaP.toFixed(2));
	}

	$scope.modificarMetaAjustada = function(sumar) {
/*		$scope.aDistribuirA = ($scope.divActividad
			? $scope.detalles[$scope.mAjustadaID].metavalor
			: ($scope.divSubTarea
				? $scope.detalles[$scope.mAjustadaID].cantidad
				: $scope.npTotalAjustado
			)
		);
*/
		$scope.aDistribuirA = Number($scope.divActividad || $scope.divSubTarea
			? $scope.detalles[$scope.mAjustadaID].cantidad
			: $scope.npTotalAjustado
		);
		if (sumar != undefined) {
			$scope.sumarValoresA();
		}
	}

	$scope.sumarValoresA = function() {
		$scope.totalAjustadaV = 0;
		$scope.totalAjustadaP = 0;
		for (var i = 0; i < 12; i++) {
			$scope.totalAjustadaV += $scope.detallesAjustada[i].valor;
			$scope.totalAjustadaP += $scope.detallesAjustada[i].porcentaje;
		}
		$scope.diferenciaAjustadaV = Number($scope.aDistribuirA.toFixed(2)) - Number($scope.totalAjustadaV.toFixed(2));
		$scope.diferenciaAjustadaP = 100 - Number($scope.totalAjustadaP.toFixed(2));
	}

	$scope.distribucionValores = function(estado) {
		if (!$scope.editar) return;
		switch (estado) {
			case "P": //Planificado
				$scope.modificarMetaPlanificada();
				distribuirValor(
					$scope.objetoPlanificada,
					$scope.detallesPlanificada,
					$scope.aDistribuirP,
					$scope.detalles[$scope.mPlanificadaID].cantidad,
					$scope.divActividad,
					$scope.divSubItem,
					$scope.vLimpio
				);
				$scope.sumarValoresP();
				break;
			case "A": //Ajustada
				$scope.modificarMetaAjustada();
				distribuirValor(
					$scope.objetoAjustada,
					$scope.detallesAjustada,
					$scope.aDistribuirA,
					$scope.detalles[$scope.mAjustadaID].cantidad,
					$scope.divActividad,
					$scope.divSubItem,
					$scope.vLimpio
				);
				$scope.sumarValoresA();
				break;
			case "D": //Devengo
				$scope.totalDevengo = $scope.npTotalAjustado;
				distribuirValor(
					$scope.objetoDevengo,
					$scope.detallesDevengo,
					$scope.totalDevengo,
					0,
					$scope.divActividad,
					$scope.divSubItem,
					$scope.vLimpio
				);
				break;
			default:
				break;
		}
	}

	$scope.distribucionCalcularTotales = function(estado) {
		if (!$scope.editar) return;
		switch (estado) {
			case "P": //Planificado
				$scope.aDistribuirP = distribucionCalcularTotal(
					$scope.detallesPlanificada
				);
				break;
			case "A": //Ajustada
				$scope.aDistribuirA = distribucionCalcularTotal(
					$scope.detallesAjustada
				);
				break;
			case "D": //Devengo
				$scope.totalDevengo = distribucionCalcularTotal(
					$scope.detallesDevengo
				);
				break;
			default:
				break;
		}
	}

	$scope.nuevo=function(node){
		console.log(node);
		$scope.esnuevo=true;
		$scope.objeto=null;
		$scope.nodeActivo=node;
		if (node.nodeTipo == "SA") {// Tarea
			PlanificacionUEFactory.nuevo(
				"TA",
				node.id,
				$rootScope.ejefiscal + "/unidadid=" + node.npIdunidad
			).then(function(resp){
				//console.log(resp);
				if (!resp.estado) return;
				$scope.editar=true;
				$scope.objeto=Object.assign({}, resp.json.tareaunidad);
				$scope.divPlanificacionAnual=false;
				$scope.divTarea=true;
				//console.log("NUEVO OBJETO tarea:", $scope.objeto);
			});
		} else
		if (node.nodeTipo == "TA") {// SubTarea
			PlanificacionUEFactory.nuevo(
				"ST",
				node.id,
				$rootScope.ejefiscal + "/unidadid=" + node.npIdunidad
			).then(function(resp){
				//console.log(resp);
				if (!resp.estado) return;
				$scope.editar=true;
				$scope.objUnidad=resp.json.subtareaunidad.subtareaunidadunidadid;
				$scope.objeto=Object.assign({}, resp.json.subtareaunidad);
				$scope.detalles=resp.json.subtareaunidadacumulador;
				for (var i = 0; i < $scope.detalles.length; i++) {
					if ($scope.detalles[i].tipo == "P") {
						$scope.mPlanificadaID = i;
					}
					if ($scope.detalles[i].tipo == "A") {
						$scope.mAjustadaID = i;
					}
				}
				$scope.divPlanificacionAnual=false;
				$scope.divSubTarea=true;
				$scope.editarDistribucionPlanificado();
				//console.log("NUEVO OBJETO subtarea:", $scope.objeto);
			});
		} else
		if (node.nodeTipo == "ST") {// Item
			PlanificacionUEFactory.nuevo(
				"IT",
				node.id,
				$rootScope.ejefiscal + "/unidadid=" + node.npIdunidad
			).then(function(resp){
				console.log(resp);
				if (!resp.estado) return;
				$scope.editar=true;
				$scope.objeto=Object.assign({}, resp.json.itemunidad);
				$scope.divPlanificacionAnual=false;
				$scope.divItem=true;
				//console.log("NUEVO OBJETO subtarea:", $scope.objeto);
			});
		} else {// SubItem
			PlanificacionUEFactory.nuevo(
				"SI",
				node.id,
				$rootScope.ejefiscal +
				"/unidadid=" + node.npIdunidad +
				"&actividadid=" + node.npactividadid
			).then(function(resp){
				console.log(resp);
				if (!resp.estado) return;
				$scope.editar=true;
				$scope.objUnidad=resp.json.actividadunidad.id.unidadid;
				$scope.objeto=Object.assign({}, resp.json.subitemunidad, resp.json.totales);
				$scope.detalles=resp.json.subitemunidadacumulador;
				for (var i = 0; i < $scope.detalles.length; i++) {
					if ($scope.detalles[i].tipo == "P") {
						$scope.mPlanificadaID = i;
					}
					if ($scope.detalles[i].tipo == "A") {
						$scope.mAjustadaID = i;
					}
					if ($scope.detalles[i].tipo == "D") {
						$scope.mDevengoID = i;
					}
				}
				$scope.divPlanificacionAnual=false;
				$scope.divSubItem=true;
				$scope.editarDistribucionPlanificado();
			});
		}
	}

	$scope.editarPlanificacionAnual=function(node) {
		console.log(node);
		$scope.esnuevo=false;
		$scope.objeto=null;
		if (node.siEditar == undefined) {
			$scope.editar=($scope.planificacionUE.npestadopresupuesto == "Planificado"? true: false);
		} else {
			$scope.editar=true;
		}
		$scope.nodeActivo=node;
		if (node.nodeTipo == "AC") {
			PlanificacionUEFactory.editar(
				node.nodeTipo,
				node.tablarelacionid,
				"unidadid=" + node.npIdunidad
			).then(function(resp){
				//console.log(resp);
				if (!resp.estado) return;
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
				$scope.editarDistribucionPlanificado();
				//console.log("OBJETO:", $scope.objeto);
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
				$scope.divPlanificacionAnual=false;
				$scope.divSubActividad=true;
				//console.log("OBJETO:", $scope.objeto);
			});
		}
		if (node.nodeTipo == "TA") {
			PlanificacionUEFactory.editar(
				node.nodeTipo,
				node.tablarelacionid,
				"nivelactividad=" + node.nodePadre.id
			).then(function(resp){
				console.log(resp);
				if (!resp.estado) return;
				$scope.objeto=Object.assign({}, resp.json.tareaunidad);
				$scope.divPlanificacionAnual=false;
				$scope.divTarea=true;
				//console.log("OBJETO:", $scope.objeto);
			});
		}
		if (node.nodeTipo == "ST") {
			PlanificacionUEFactory.editar(
				node.nodeTipo,
				node.tablarelacionid,
				"nivelactividad=" + node.nodePadre.nodePadre.id
			).then(function(resp){
				console.log(resp);
				if (!resp.estado) return;
				$scope.objUnidad=resp.json.subtareaunidad.subtareaunidadunidadid;
				$scope.objeto=Object.assign({}, resp.json.subtareaunidad);
				$scope.detalles=resp.json.subtareaunidadacumulador;
				for (var i = 0; i < $scope.detalles.length; i++) {
					if ($scope.detalles[i].tipo == "P") {
						$scope.mPlanificadaID = i;
					}
					if ($scope.detalles[i].tipo == "A") {
						$scope.mAjustadaID = i;
					}
				}
				$scope.divPlanificacionAnual=false;
				$scope.divSubTarea=true;
				$scope.editarDistribucionPlanificado();
				//console.log("OBJETO:", $scope.objeto);
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
				$scope.objeto=Object.assign({}, resp.json.itemunidad);
				$scope.divPlanificacionAnual=false;
				$scope.divItem=true;
				//console.log("Editar OBJETO:", $scope.objeto);
			});
		}
		if (node.nodeTipo == "SI") {
			PlanificacionUEFactory.editar(
				node.nodeTipo,
				node.tablarelacionid,
				"unidadid=" + node.npIdunidad +
				"&ejerciciofiscal=" + $rootScope.ejefiscal +
				"&actividadid=" + node.npactividadid
			).then(function(resp){
				console.log(resp);
				if (!resp.estado) return;
				$scope.objUnidad=resp.json.actividadunidad.id.unidadid;
				$scope.objeto=Object.assign({}, resp.json.subitemunidad, resp.json.totales);
				$scope.detalles=resp.json.subitemunidadacumulador;
				for (var i = 0; i < $scope.detalles.length; i++) {
					if ($scope.detalles[i].tipo == "P") {
						$scope.mPlanificadaID = i;
					} else if ($scope.detalles[i].tipo == "A") {
						$scope.mAjustadaID = i;
					} else if ($scope.detalles[i].tipo == "D") {
						$scope.mDevengoID = i;
					} else {
						continue;
					}
	            	$scope.detalles[i].npvalor = $scope.detalles[i].valor * $scope.detalles[i].cantidad;
            		$scope.detalles[i].total = $scope.detalles[i].npvalor;
				}
				$scope.divPlanificacionAnual=false;
				$scope.divSubItem=true;
				$scope.calcularTotalPlanificado();
				$scope.calcularTotalAjustado();
				$scope.editarDistribucionPlanificado();
				//console.log("Editar OBJETO:", $scope.objeto);
			});
		}
		if (node.siEditar != undefined) {
			$scope.edicion = true;
			$scope.aprobacionPlanificacion = false;
			$scope.aprobacionAjustado = false;
		}
	};

	$scope.regresarPUE=function(obj) {
		$scope.edicion = false;
		$scope.arbol = [];
		$scope.objeto = {};
		$scope.details = [];
	}

	$scope.cargarPlanificacionAnual=function(obj) {
		//console.log(obj);
		var id = obj.id;
		$scope.edicion = true;
		$scope.planificacionUE = obj;
		$scope.unidadid = obj.npacitividadunidad;
		//$scope.nivelactividadunidadid = obj.nivelactividadunidadid;**
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
				$scope.dataPA[i].iscargado = false;
			}
			$scope.arbol = JSON.parse(JSON.stringify($scope.dataPA).split('"descripcionexten":').join('"title":'));
			$scope.dataPA = [];
			//console.log($scope.arbol)
			$scope.divPlanificacionAnual=true;
		})
	}

	$scope.abrirVentana=function(){
		var asideInstance = $aside.open({
		    templateUrl: 'assets/views/papp/modal/pue.html',
		    //controller: 'AsideCtrl',
		    placement: 'left',
		    size: 'sm'
		});
	};
    
    $scope.openAside = function(position) {
    	$rootScope.objetoVista=$scope.objetoVista;
    	$scope.asideState = {
	        open: true,
	        position: position
    	};

    	function postClose() {
        	$scope.asideState.open = false;
    	}

	    $aside.open({
	        templateUrl: 'assets/views/papp/modal/pue.html',
	        placement: position,
	        size: 'md',
	        backdrop: true,
	        controller: 'PUEController'
	    }).result.then(postClose, postClose);
    }

	$scope.vista=function(node){
		console.log(node);
		$scope.node = node;
		$scope.divMenuActividad = false;
		$scope.divMenuSubitems = false;
		//$scope.objetoVista = null;
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
					$scope.novista = false;
					$scope.cargarCodigosVista();
				}
			})
		}
		if (node.nodeTipo == "SA") {
			PlanificacionUEFactory.traerPAverSubActividad(
				node.tablarelacionid,
				node.npIdunidad,
				$rootScope.ejefiscal
			).then(function(resp){
				console.log(resp);
				if (resp.estado) {
					$scope.objetoVista=resp.json.subactividadplanificacion;
					$scope.divPlanificacionAnualVista=true;
					$scope.divMenuActividad = true;
					$scope.novista = false;
					$scope.cargarCodigosVista();
				}
			})
		}
		if (node.nodeTipo == "TA") {
			PlanificacionUEFactory.traerPAverTarea(
				node.tablarelacionid,
				node.npIdunidad,
				$rootScope.ejefiscal
			).then(function(resp){
				console.log(resp);
				if (resp.estado) {
					$scope.objetoVista=resp.json.tareaplanificacion;
					$scope.divPlanificacionAnualVista=true;
					$scope.divMenuActividad = true;
					$scope.novista = false;
					$scope.cargarCodigosVista();
				}
			})
		}
		if (node.nodeTipo == "ST") {
			PlanificacionUEFactory.traerPAverSubTarea(
				node.tablarelacionid,
				node.npIdunidad,
				$rootScope.ejefiscal
			).then(function(resp){
				console.log(resp);
				if (resp.estado) {
					$scope.objetoVista=resp.json.subtareaplanificacion;
					$scope.divPlanificacionAnualVista=true;
					$scope.divMenuActividad = true;
					$scope.novista = false;
					$scope.cargarCodigosVista();
				}
			})
		}
		if (node.nodeTipo == "IT") {
			PlanificacionUEFactory.traerPAverItem(
				node.tablarelacionid,
				node.npIdunidad,
				$rootScope.ejefiscal
			).then(function(resp){
				console.log(resp);
				if (resp.estado) {
					$scope.objetoVista=resp.json.itemplanificacion;
					$scope.divPlanificacionAnualVista=true;
					$scope.divMenuActividad = true;
					$scope.novista = false;
					$scope.cargarCodigosVista();
				}
			})
		}
		if (node.nodeTipo == "SI") {
			PlanificacionUEFactory.traerPAverSubItem(
				node.tablarelacionid,
				node.npIdunidad,
				$rootScope.ejefiscal
			).then(function(resp){
				console.log(resp);
				if (resp.estado) {
					$scope.objetoVista=resp.json.subitemplanificacion;
					$scope.divPlanificacionAnualVista=true;
					$scope.divMenuActividad = true;
					$scope.novista = false;
					$scope.cargarCodigosVista();
				}
			})
		}
	};

	$scope.cargarCodigosVista=function() {
		$scope.objetoVista.npPlannacional = $scope.objetoVista.npPlannacionalcodigo + ': ' + $scope.objetoVista.npPlannacional;
		$scope.objetoVista.npObjetivoestrategico = $scope.objetoVista.npObjetivoestrategicocodigo + ': ' + $scope.objetoVista.npObjetivoestrategico;
		$scope.objetoVista.npObjetivooperativo = $scope.objetoVista.npObjetivooperativocodigo + ': ' + $scope.objetoVista.npObjetivooperativo;
		$scope.objetoVista.npObjetivofuerza = $scope.objetoVista.npObjetivofuerzacodigo + ': ' + $scope.objetoVista.npObjetivofuerza;
		$scope.objetoVista.npInstitucion = $scope.objetoVista.npInstitucioncodigo + ': ' + $scope.objetoVista.npInstitucion;
		$scope.objetoVista.npentidad = $scope.objetoVista.npentidadcodigo + ': ' + $scope.objetoVista.npentidad;
		$scope.objetoVista.npPrograma = $scope.objetoVista.npProgramacodigo + ': ' + $scope.objetoVista.npPrograma;
		$scope.objetoVista.npSubprograma = $scope.objetoVista.npSubprogramacodigo + ': ' + $scope.objetoVista.npSubprograma;
		$scope.objetoVista.npProyecto = $scope.objetoVista.npProyectocodigo + ': ' + $scope.objetoVista.npProyecto;
		$scope.openAside('left');
	}

	$scope.regresarVista=function() {
		$scope.novista = true;
	}

	$scope.cargarHijos=function(node){
		//console.log(node);
		if (!node.iscargado)
		    node.iscargado=true;
		else
			return;

		var tipo = "";
		//console.log(node.nodeTipo);
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
		//console.log(tipo);
	    PlanificacionUEFactory.traerPAhijos(
    		tipo,
    		node.id,
    		node.npIdunidad,
    		$rootScope.ejefiscal,
    		(node.npactividadid==0? node.tablarelacionid: node.npactividadid)
		).then(function(resp){
			//console.log(resp);
			for (var i = 0; i < resp.length; i++) {
				resp[i].nodeTipo = tipo;
				resp[i].padreID = node.id;
			}
			var nodes;
			if (tipo == "AC" || tipo == "SA") {
				nodes=JSON.parse(JSON.stringify(resp).split('"descripcionexten":').join('"title":'));
				for (var i = 0; i < nodes.length; i++) {
					nodes[i].nodePadre = node;
				}
			} else {
				nodes=JSON.parse(JSON.stringify(resp).split('"npdescripcion":').join('"title":'));
				for (var i = 0; i < nodes.length; i++) {
					nodes[i].title = nodes[i].npcodigo + " - " + nodes[i].title;
					nodes[i].nodePadre = node;
				}
			}
			node.nodes=nodes;
		});
	}
	
	$scope.asideTM = {
			  "title": "Title",
			  "content": "<br><br><br><br><br><br>Hello Aside<br />This is a multiline message!"
			};
	

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

	$scope.abrirUnidadMedidaCodigo = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalUnidadMedida.html',
			controller : 'ModalUnidadMedidaController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.metaumid = obj.id;
			$scope.objeto.npcodigounidad = obj.codigo;
			$scope.objeto.npnombreunidad = obj.nombre;
			$scope.objeto.npcodigogrupo = obj.npCodigogrupo;
			$scope.objeto.npnombregrupo = obj.npNombregrupo;
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
			size : 'lg',
			resolve: {
				prestamo: function() {
					return true;
				},
				mostrarOrganismo: function() {
					return true;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.itemunidadorganismoid = obj.id.id;
			$scope.objeto.itemunidadorgprestamoid = obj.id.prestamoid;
			$scope.objeto.npcodigoorganismo = obj.codigo;
			$scope.objeto.npnombreorganismo = obj.nombre;		
			$scope.objeto.npcodigoorgpres = obj.npcodigoorganismo;
			$scope.objeto.npnombreorgpres = obj.npnombreorganismo;		
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

	$scope.abrirCodigoINCOP = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalSubitem.html',
			controller : 'ModalSubItemController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.subitemunidadsubitemid = obj.id;
			$scope.objeto.npcodigosubitem = obj.codigo;
			$scope.objeto.npnombresubitem = obj.nombre;	
			$scope.objeto.npprecio = obj.precio;	
		}, function() {
		});
	};

	$scope.abrirTipoProducto = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalTipoProducto.html',
			controller : 'ModalTipoProductoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.subitemunidadtipoproductoid = obj.id;
			$scope.objeto.nptipoprodnombre = obj.nombre;	
		}, function() {
		});
	};

	$scope.abrirProcedimiento = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalProcedimiento.html',
			controller : 'ModalProcedimientoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.subitemunidadprocedimientoid = obj.id;
			$scope.objeto.npprocedimientonombre = obj.nombre;	
		}, function() {
		});
	};

	$scope.abrirTipoRegimen = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalTipoRegimen.html',
			controller : 'ModalTipoRegimenController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.subitemunidadtiporegimenid = obj.id;
			$scope.objeto.nptiporegnombre = obj.nombre;	
		}, function() {
		});
	};

	$scope.form = {
        submit: function(form,name) {
            var firstError = null; //IVAN
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
		if ($scope.detalles[$scope.mPlanificadaID].cantidad != $scope.detalles[$scope.mPlanificadaID].npValor) {
            SweetAlert.swal(
        		"Planificacion UE! - Actividad",
        		"DEBE CREAR EL CRONOGRAMA DE META PLANIFICADO.",
        		"error"
    		);
            return;
		}
		if ($scope.detalles[$scope.mAjustadaID].cantidad != $scope.detalles[$scope.mAjustadaID].npValor) {
	        SweetAlert.swal(
	    		"Planificacion UE! - Actividad",
	    		"DEBE CREAR EL CRONOGRAMA DE META AJUSTADA.",
	    		"error"
			);
	        return;
		}
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
	            if ($scope.nodeActivo.siEditar == undefined) {
	            	$scope.cargarPlanificacionAnual($scope.planificacionUE);
	            }
			} else {
				SweetAlert.swal("Planificacion UE! - Actividad", resp.mensajes.msg, "error");
	    		$scope.divPlanificacionAnual = false;
			}
		});
	}

	$scope.resetformActividad = function(form) {
        //$scope.myModel = angular.copy($scope.master);
        form.$setPristine(true);
        $scope.limpiarEdicion();
	}

	$scope.calcularPorcientoP = function(index) {
		$scope.modificarMetaPlanificada();
		distribuirValor(
			$scope.objetoPlanificada,
			$scope.detallesPlanificada,
			$scope.aDistribuirP,
			$scope.detalles[$scope.mPlanificadaID].cantidad,
			$scope.divActividad,
			$scope.divSubItem,
			false
		);
		$scope.sumarValoresP();
	}

	$scope.submitformMetaDistribucionPlanificada = function(form) {
		if ($scope.divActividad || $scope.divSubTarea) {
			var porcentaje = 0;
			for (var i = 0; i < 12; i++) {
				porcentaje += $scope.detallesPlanificada[i].porcentaje;
			}
			porcentaje = Number(porcentaje.toFixed(0));
			if (porcentaje != 100) {
	            SweetAlert.swal(
            		"Planificacion UE! - Distribucion Planificada",
            		"La suma de los porcientos es diferente de 100%",
            		"error"
        		);
    			return;
			}
		}
		var t = 0;
		for (var i = 0; i < 12; i++) {
			t += $scope.detallesPlanificada[i].valor;
		}
		t = Number($scope.aDistribuirP.toFixed(2));
		if (t != ($scope.divActividad || $scope.divSubTarea
				? $scope.detalles[$scope.mPlanificadaID].cantidad
				: $scope.npTotalPlanificado
			)) {
            SweetAlert.swal(
        		"Planificacion UE! - Distribucion Planificada",
        		"La suma de los valores es diferente de la Meta Planificada",
        		"error"
    		);
			return;
		}
		if (!$scope.divSubItem) {
			for (var i = 0; i < 12; i++) {
				if ($scope.detallesPlanificada[i].valor > 0)
					if ($scope.detallesPlanificada[i].observacion == undefined || $scope.detallesPlanificada[i].observacion.trim() == "") {
				        SweetAlert.swal(
				    		"Planificacion UE! - Distribucion Planificada",
				    		"Observaciones incompletas. Todos los meses que tienen asignado valor tienen que tener una observacion.",
				    		"error"
						);
				        return;
				}
			}
		}
    	var tObj={};
    	tObj=Object.assign(tObj, $scope.objetoPlanificada);
    	tObj.cronogramalineaTOs=$scope.detallesPlanificada;
    	PlanificacionUEFactory.guardarMetaDistribucionPlanificada(tObj).then(function(resp){
			if (resp.estado) {
				//form.$setPristine(true);
				$scope.detallesPlanificada = null;
				$scope.metaDistribucion("P");
	            SweetAlert.swal("Planificacion UE! - Distribucion Planificada", "Guardado satisfactoriamente!", "success");
	            if ($scope.divActividad || $scope.divSubTarea) {
	            	$scope.detalles[$scope.mPlanificadaID].npValor = $scope.detalles[$scope.mPlanificadaID].cantidad;
	            } else { // SubItem
	            	$scope.detalles[$scope.mPlanificadaID].npvalor = $scope.detalles[$scope.mPlanificadaID].total;
	            }
	            $scope.divPlanificacionAnual = false;
			} else {
				SweetAlert.swal("Planificacion UE! - Distribucion Planificada", resp.mensajes.msg, "error");
	    		$scope.divPlanificacionAnual = false;
			}
		})
	}

	$scope.resetformMetaDistribucionPlanificada = function(form) {
        form.$setPristine(true);
        $scope.limpiarEdicion();
	}

	$scope.calcularPorcientoA = function(index) {
		$scope.modificarMetaAjustada();
		distribuirValor(
			$scope.objetoAjustada,
			$scope.detallesAjustada,
			$scope.aDistribuirA,
			$scope.detalles[$scope.mAjustadaID].cantidad,
			$scope.divActividad,
			$scope.divSubItem,
			false
		);
		$scope.sumarValoresA();
	}

	$scope.submitformMetaDistribucionAjustada = function(form) {
		if ($scope.divActividad || $scope.divSubTarea) {
			var porcentaje = 0;
			for (var i = 0; i < 12; i++) {
				porcentaje = porcentaje + $scope.detallesAjustada[i].porcentaje;
			}
			porcentaje = Number(porcentaje.toFixed(0));
			if (porcentaje != 100) {
	            SweetAlert.swal(
            		"Planificacion UE! - Distribucion Ajustada",
            		"La suma de los porcientos es diferente de 100%",
            		"error"
        		);
    			return;
			}
		}
		var t = 0;
		for (var i = 0; i < 12; i++) {
			t += $scope.detallesAjustada[i].valor;
		}
		t = Number($scope.aDistribuirA.toFixed(2));
		if (t != ($scope.divActividad || $scope.divSubTarea
				? $scope.detalles[$scope.mAjustadaID].cantidad
				: $scope.npTotalAjustado
			)) {
            SweetAlert.swal(
        		"Planificacion UE! - Distribucion Ajustada",
        		"La suma de los valores es diferente de la Meta Ajustada",
        		"error"
    		);
			return;
		}
		if (!$scope.divSubItem) {
			for (var i = 0; i < 12; i++) {
				if ($scope.detallesAjustada[i].valor > 0)
					if ($scope.detallesAjustada[i].observacion == undefined || $scope.detallesAjustada[i].observacion.trim() == "") {
				        SweetAlert.swal(
				    		"Planificacion UE! - Distribucion Ajustada",
				    		"Observaciones incompletas. Todos los meses que tienen asignado valor tienen que tener una observacion.",
				    		"error"
						);
				        return;
				}
			}
		}
    	var tObj=$scope.objetoAjustada;
    	tObj.cronogramalineaTOs=$scope.detallesAjustada;
    	PlanificacionUEFactory.guardarMetaDistribucionPlanificada(tObj).then(function(resp){
			if (resp.estado) {
				//form.$setPristine(true);
				$scope.detallesAjustada = null;
				$scope.metaDistribucion("A");
	            SweetAlert.swal("Planificacion UE! - Distribucion Ajustada", "Guardado satisfactoriamente!", "success");
	            if ($scope.divActividad || $scope.divSubTarea) {
	            	$scope.detalles[$scope.mAjustadaID].npValor = $scope.detalles[$scope.mAjustadaID].cantidad;
	            } else { // SubItem
	            	$scope.detalles[$scope.mAjustadaID].npvalor = $scope.detalles[$scope.mAjustadaID].total;
	            }
	            $scope.divPlanificacionAnual = false;
			} else {
				SweetAlert.swal("Planificacion UE! - Distribucion Ajustada", resp.mensajes.msg, "error");
	    		$scope.divPlanificacionAnual = false;
			}
		})
	}

	$scope.resetformMetaDistribucionAjustada = function(form) {
        form.$setPristine(true);
        $scope.limpiarEdicion();
	}

	$scope.submitformMetaDistribucionDevengo = function(form) {
		if ($scope.objetoDevengo.unidadtiempo == "PE") {
			$scope.totalDevengo = 0;
			for (var i = 0; i < 12; i++) {
				$scope.totalDevengo += $scope.detallesDevengo[i].valor;
			}
			$scope.totalDevengo = Number($scope.totalDevengo.toFixed(2));
		}
		if ($scope.totalDevengo !=  $scope.npTotalAjustado) {
            SweetAlert.swal(
        		"Planificacion UE! - Distribucion Devengo",
        		"La suma de los valores es diferente de la Meta Devengo",
        		"error"
    		);
			return;
		}
    	var tObj=$scope.objetoDevengo;
    	tObj.cronogramalineaTOs=$scope.detallesDevengo;
    	PlanificacionUEFactory.guardarMetaDistribucionPlanificada(tObj).then(function(resp){
			if (resp.estado) {
				//form.$setPristine(true);
				$scope.detallesDevengo = null;
				$scope.metaDistribucion("D");
	            SweetAlert.swal("Planificacion UE! - Distribucion Devengo", "Registro registrado satisfactoriamente!", "success");
	            $scope.divPlanificacionAnual = false;
			} else {
				SweetAlert.swal("Planificacion UE! - Distribucion Devengo", resp.mensajes.msg, "error");
	    		$scope.divPlanificacionAnual = false;
			}
		})
	}

	$scope.resetformMetaDistribucionDevengo = function(form) {
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
    			$scope.nodeActivo.nodePadre.iscargado = false;
    			$scope.cargarHijos($scope.nodeActivo.nodePadre);
			} else {
				SweetAlert.swal("Planificacion UE! - Subactividad", resp.mensajes.msg, "error");
	    		$scope.divPlanificacionAnual = false;
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
    		$scope.divPlanificacionAnual = false;
			if (resp.estado) {
				form.$setPristine(true);
				$scope.limpiarEdicion();
	            SweetAlert.swal("Planificacion UE! - Tarea", "Registro registrado satisfactoriamente!", "success");
    			$scope.nodeActivo.nodePadre.iscargado = false;
    			$scope.cargarHijos($scope.nodeActivo.nodePadre);
			} else {
				SweetAlert.swal("Planificacion UE! - Tarea", resp.mensajes.msg, "error");
	    		$scope.divPlanificacionAnual = false;
			}
		})
	}

	$scope.resetformTarea = function(form) {
        //$scope.myModel = angular.copy($scope.master);
        form.$setPristine(true);
        $scope.limpiarEdicion();
	}

	$scope.submitformSubTarea = function(form) {
    	var tObj={};
    	angular.copy($scope.objeto, tObj);
    	tObj.subtareaunidadacumulador=$scope.detalles;
		if ($scope.esnuevo) {
	    	PlanificacionUEFactory.guardarActividades("ST",tObj).then(function(resp){
	    		$scope.divPlanificacionAnual = false;
				if (resp.estado) {
					//$scope.objeto=Object.assign({}, resp.json.subtareaunidad);
					$scope.objeto = resp.json.subtareaunidad;
					for (var i = 0; i < $scope.detalles.length; i++) {
						$scope.detalles[i].id.id = resp.json.subtareaunidad.id;
					}
					$scope.esnuevo = false;
	    			$scope.nodeActivo.nodePadre.iscargado = false;
	    			$scope.cargarHijos($scope.nodeActivo.nodePadre);
	    			$scope.editarDistribucionPlanificado();
				} else {
					SweetAlert.swal("Planificacion UE! - Subtarea", resp.mensajes.msg, "error");
				}
			})
		}
		if ($scope.detalles[$scope.mPlanificadaID].cantidad != $scope.detalles[$scope.mPlanificadaID].npValor) {
            SweetAlert.swal(
        		"Planificacion UE! - Subtarea",
        		"DEBE CREAR EL CRONOGRAMA DE META PLANIFICADO.",
        		"error"
    		);
            return;
		}
		if ($scope.detalles[$scope.mAjustadaID].cantidad != $scope.detalles[$scope.mAjustadaID].npValor) {
	        SweetAlert.swal(
	    		"Planificacion UE! - Subtarea",
	    		"DEBE CREAR EL CRONOGRAMA DE META AJUSTADA.",
	    		"error"
			);
	        return;
		}
		if ($scope.esnuevo) {
			return;
		}
    	PlanificacionUEFactory.guardarActividades("ST",tObj).then(function(resp){
			if (resp.estado) {
				form.$setPristine(true);
				$scope.limpiarEdicion();
	            //$scope.limpiar();
	            SweetAlert.swal("Planificacion UE! - Subtarea", "Registro registrado satisfactoriamente!", "success");
			} else {
				SweetAlert.swal("Planificacion UE! - Subtarea", resp.mensajes.msg, "error");
	    		$scope.divPlanificacionAnual = false;
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
    			$scope.nodeActivo.nodePadre.iscargado = false;
    			$scope.cargarHijos($scope.nodeActivo.nodePadre);
			} else {
				SweetAlert.swal("Planificacion UE! - Item", resp.mensajes.msg, "error");
	    		$scope.divPlanificacionAnual = false;
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
		tObj.subitemunidadacumulador=$scope.detalles;
		if ($scope.esnuevo) {
	    	PlanificacionUEFactory.guardarActividades("SI",tObj).then(function(resp){
	    		$scope.divPlanificacionAnual = false;
				if (resp.estado) {
					$scope.objeto.id = resp.json.subitemunidad.id;
					for (var i = 0; i < $scope.detalles.length; i++) {
						$scope.detalles[i].id.id = resp.json.subitemunidad.id;
					}
					$scope.esnuevo = false;
	    			$scope.editarDistribucionPlanificado();
				} else {
					SweetAlert.swal("Planificacion UE! - Subitem", resp.mensajes.msg, "error");
				}
			})
		}
		//$scope.npTotalPlanificado;
		//$scope.npTotalAjustado;
		if ($scope.npTotalPlanificado != $scope.detalles[$scope.mPlanificadaID].npvalor) {
            SweetAlert.swal(
        		"Planificacion UE! - Subitem",
        		"No coincide del Presupuesto el Total Planifica y la distribuida, tiene que redistribuirla.",
        		"error"
    		);
            return;
		}
		if ($scope.npTotalAjustado != $scope.detalles[$scope.mAjustadaID].npvalor) {
	        SweetAlert.swal(
	    		"Planificacion UE! - Subitem",
	    		"No coincide del Presupuesto el Total Ajustada y la distribuida, tiene que redistribuirla.",
	    		"error"
			);
	        return;
		}
		if ($scope.esnuevo) {
			return;
		}
    	PlanificacionUEFactory.guardarActividades("SI",tObj).then(function(resp){
			if (resp.estado) {
				form.$setPristine(true);
				$scope.limpiarEdicion();
	            //$scope.limpiar();
	            SweetAlert.swal("Planificacion UE! - Subitem", "Registro registrado satisfactoriamente!", "success");
    			$scope.nodeActivo.nodePadre.iscargado = false;
    			$scope.cargarHijos($scope.nodeActivo.nodePadre);
			} else {
				SweetAlert.swal("Planificacion UE! - Subitem", resp.mensajes.msg, "error");
	    		$scope.divPlanificacionAnual = false;
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

	$scope.calcularTotalPlanificado = function() {
		$scope.npTotalPlanificado = $scope.detalles[$scope.mPlanificadaID].valor * $scope.detalles[$scope.mPlanificadaID].cantidad;
		$scope.detalles[$scope.mPlanificadaID].total = $scope.npTotalPlanificado;
	}

	$scope.calcularTotalAjustado = function() {
		$scope.npTotalAjustado = $scope.detalles[$scope.mAjustadaID].valor * $scope.detalles[$scope.mAjustadaID].cantidad;
		$scope.detalles[$scope.mAjustadaID].total = $scope.npTotalAjustado;
	}

	$scope.cargarMatrizPresupuestoTipo = function() {
		PlanificacionUEFactory.cargarMatrizPresupuesto(
			$scope.data[$scope.index].id,
			$rootScope.ejefiscal,
			$scope.tipo
		).then(function(resp){
			console.log(resp);
			if (!resp.estado) return;
			$scope.unidad = resp.json.unidad;
			$scope.nombreinstitucion = $scope.unidad.codigoinstitucion + " " + $scope.unidad.nombreinstitucion;
			$scope.nombreinstentidad = $scope.unidad.codigoinstentidad + " " + $scope.unidad.nombreinstentidad;
			$scope.nombreunidad = $scope.unidad.codigounidad + " " + $scope.unidad.nombreunidad;
			$scope.cabecera = resp.json.cabecera[0];
			$scope.programa = $scope.cabecera.programacodigo + " " + $scope.cabecera.programa;
			$scope.proyecto = $scope.cabecera.proyectocodigo + " " + $scope.cabecera.proyecto;
			$scope.actividad = $scope.cabecera.actividadcodigo + " " + $scope.cabecera.actividad;
			$scope.subactividad = $scope.cabecera.codigo + " " + $scope.cabecera.descripcion;
			$scope.detalle = resp.json.detalle;
			for (var i = 0; i < $scope.detalle.length; i++) {
				$scope.detalle[i].tareanombre = $scope.detalle[i].tareacodigo + " " + $scope.detalle[i].tareanombre;
				$scope.detalle[i].subtareanombre = $scope.detalle[i].subtareacodigo + " " + $scope.detalle[i].subtareanombre;
			}
			$scope.edicionMatrizPresupuesto = true;
		});
	}

	$scope.renovar = function() {
		if ($scope.edicionMatrizPresupuesto) {
			$scope.cargarMatrizPresupuestoTipo();
		}
		if ($scope.edicionMatrizMetas) {
			$scope.cargarMatrizMetasTipo();
		}
		return;
		var tObj = Object.assign($scope.unidad, $scope.cabecera);
		tObj.detalle = $scope.detalle;
		PlanificacionUEFactory.cargarMatrizPresupuesto(
			tObj
		).then(function(resp){
			console.log(resp);
			if (resp.estado) {
	            SweetAlert.swal("Planificacion UE! - Subitem", "Registro registrado satisfactoriamente!", "success");
			} else {
				SweetAlert.swal("Planificacion UE! - Subitem", resp.mensajes.msg, "error");
			}
		});
	}

	$scope.volver = function() {
		$scope.edicionMatrizPresupuesto = false;
		$scope.edicionMatrizMetas = false;
		$scope.aprobacionPlanificacion = false;
		$scope.aprobacionAjustado = false;
		$scope.edicionMatrizPresupuestoAP = false;
		$scope.edicionMatrizMetasAP = false;
	}

	$scope.editarMatrizPresupuesto = function(index) {
		$scope.tipo = "P";
		$scope.index = ((pagina - 1) * 5) + index;
		$scope.cargarMatrizPresupuestoTipo();
	}

	$scope.cargarMatrizPresupuestoTipo = function() {
		$scope.detallePA = null;
		PlanificacionUEFactory.cargarMatrizPresupuesto(
			$scope.data[$scope.index].id,
			$rootScope.ejefiscal,
			$scope.tipo
		).then(function(resp){
			console.log(resp);
			if (!resp.estado) return;
			$scope.unidad = resp.json.unidad;
			$scope.nombreinstitucion = $scope.unidad.codigoinstitucion + " " + $scope.unidad.nombreinstitucion;
			$scope.nombreinstentidad = $scope.unidad.codigoinstentidad + " " + $scope.unidad.nombreinstentidad;
			$scope.nombreunidad = $scope.unidad.codigounidad + " " + $scope.unidad.nombreunidad;
			$scope.cabecera = resp.json.cabecera[0];
			$scope.programa = $scope.cabecera.programacodigo + " " + $scope.cabecera.programa;
			$scope.proyecto = $scope.cabecera.proyectocodigo + " " + $scope.cabecera.proyecto;
			$scope.actividad = $scope.cabecera.actividadcodigo + " " + $scope.cabecera.actividad;
			$scope.subactividad = $scope.cabecera.codigo + " " + $scope.cabecera.descripcion;
			$scope.detallePA = resp.json.detalle;
			$scope.edicionMatrizPresupuesto = true;
		});
	}

	$scope.editarMatrizMetas = function(index) {
		$scope.tipo = "P";
		$scope.index = ((pagina - 1) * 5) + index;
		$scope.cargarMatrizMetasTipo();
	}

	$scope.cargarMatrizMetasTipo = function() {
		$scope.detallePA = null;
		PlanificacionUEFactory.cargarMatrizMetas(
			$scope.data[$scope.index].id,
			$rootScope.ejefiscal,
			$scope.tipo
		).then(function(resp){
			console.log(resp);
			if (!resp.estado) return;
			$scope.unidad = resp.json.unidad;
			$scope.nombreinstitucion = $scope.unidad.codigoinstitucion + " " + $scope.unidad.nombreinstitucion;
			$scope.nombreinstentidad = $scope.unidad.codigoinstentidad + " " + $scope.unidad.nombreinstentidad;
			$scope.nombreunidad = $scope.unidad.codigounidad + " " + $scope.unidad.nombreunidad;
			$scope.cabecera = resp.json.cabecera[0];
			$scope.programa = $scope.cabecera.programacodigo + " " + $scope.cabecera.programa;
			$scope.proyecto = $scope.cabecera.proyectocodigo + " " + $scope.cabecera.proyecto;
			$scope.actividad = $scope.cabecera.actividadcodigo + " " + $scope.cabecera.actividad;
			$scope.subactividad = $scope.cabecera.codigo + " " + $scope.cabecera.descripcion;
			$scope.detallePA = resp.json.detalle;
			$scope.edicionMatrizMetas = true;
		});
	}

	$scope.copiarPlanificadoAjustado = function() {
		//$scope.metaDistribucion("A");*
		$scope.objetoAjustada.unidadtiempo = $scope.objetoPlanificada.unidadtiempo;
		for (var i = 0; i < $scope.detallesPlanificada.length; i++) {
			$scope.detallesAjustada[i].valor = $scope.detallesPlanificada[i].valor;
			$scope.detallesAjustada[i].porcentaje = $scope.detallesPlanificada[i].porcentaje;
		}
		$scope.aDistribuirA = $scope.aDistribuirP;
		$scope.divMetaDistribucionPlanificada=false;
		$scope.divMetaDistribucionAjustada=true;
		$scope.divMetaDistribucionDevengo=false;
	}

//******

	$scope.modificarPresupuestoAjustado = function(obj, tipo) {
		//console.log("Fuentes:", obj);
		var node = {
			nodeTipo: obj.nivel,
			tablarelacionid: obj.tablarelacionid,
			npIdunidad: obj.npIdunidad,
			//padreID: obj.,
			npactividadid: obj.npactividadid,
			siEditar: tipo
		};
		if (obj.nivelpadreid != undefined) {
			node.padreID = obj.nivelpadreid;
		}
		$scope.editarPlanificacionAnual(node);
	}

	$scope.aprobarPlanificacion = function(obj) {
		$scope.detalles = null;
		if (obj.npestadopresupuesto != "Planificado") {
			SweetAlert.swal("Aprobacion Planificacion!", "Solo se puede aprobar si esta Planificado", "warning");
			return;
		}
		$scope.aprobacionPlanificacion = true;
		$scope.objetoPA = obj;
	}

	$scope.editarAprobarPlanificacion=function(){
		$scope.detallesPA = null;
		AprobacionPlanificacionFactory.editarAprobarPlanificacion(
			$scope.objetoPA.id,
			$rootScope.ejefiscal,
			$scope.objetoPA.npacitividadunidad,
			"P"
		).then(function(resp){
			$scope.detallesPA = resp.json.resultadoaprobacion;
			SweetAlert.swal("Aprobacion Planificacion!", resp.mensajes.msg, resp.mensajes.type);
		});
	};
	
	$scope.editadoObservacion = function(index) {
		//index = ((pagina - 1) * 5) + index;
		$scope.detalles[index].modificado = true;
	}
	
	$scope.aprobarAjustado = function(obj) {
		$scope.detallesPA = null;
		if (obj.npestadopresupuesto != "Aprobado") {
			SweetAlert.swal(
				"Aprobacion Planificacion!",
				"Solo se puede aprobar el Ajustado si el Planificado esta Aprobado",
				"warning"
			);
			return;
		}
		$scope.aprobacionAjustado = true;
		$scope.objetoPA = obj;
	}

	$scope.editarAprobarAjustada=function(){
		$scope.detallesPA = null;
   		AprobacionPlanificacionFactory.editarAprobarPlanificacion(
			$scope.objetoPA.id,
			$rootScope.ejefiscal,
			$scope.objetoPA.npacitividadunidad,
			"A"
		).then(function(resp){
			$scope.detallesPA = resp.json.resultadoaprobacion;
			SweetAlert.swal("Aprobacion Planificacion!", resp.mensajes.msg, resp.mensajes.type);
		})
	};

	$scope.renovarAP = function() {
		if ($scope.edicionMatrizPresupuestoAP) {
			$scope.cargarMatrizPresupuestoTipoAP();
		}
		if ($scope.edicionMatrizMetasAP) {
			$scope.cargarMatrizMetasTipoAP();
		}
	}

	$scope.editarMatrizPresupuestoAP = function(obj) {
		$scope.tipo = "P";
		$scope.objeto = obj;
		$scope.cargarMatrizPresupuestoTipoAP();
	}

	$scope.editarMatrizMetasAP = function(obj) {
		$scope.tipo = "M";
		$scope.objeto = obj;
		$scope.cargarMatrizMetasTipoAP();
	}

	$scope.cargarMatrizPresupuestoTipoAP = function() {
		AprobacionPlanificacionFactory.cargarMatrizPresupuesto(
			$scope.objeto.id,
			$rootScope.ejefiscal,
			$scope.tipo
		).then(function(resp){
			console.log(resp);
			if (!resp.estado) return;
			$scope.unidad = resp.json.unidad;
			$scope.nombreinstitucion = $scope.unidad.codigoinstitucion + " " + $scope.unidad.nombreinstitucion;
			$scope.nombreinstentidad = $scope.unidad.codigoinstentidad + " " + $scope.unidad.nombreinstentidad;
			$scope.nombreunidad = $scope.unidad.codigounidad + " " + $scope.unidad.nombreunidad;
			$scope.cabecera = resp.json.cabecera[0];
			$scope.programa = $scope.cabecera.programacodigo + " " + $scope.cabecera.programa;
			$scope.proyecto = $scope.cabecera.proyectocodigo + " " + $scope.cabecera.proyecto;
			$scope.actividad = $scope.cabecera.actividadcodigo + " " + $scope.cabecera.actividad;
			$scope.subactividad = $scope.cabecera.codigo + " " + $scope.cabecera.descripcion;
			$scope.detalles = resp.json.detalles;
			$scope.edicionMatrizPresupuestoAP = true;
		});
	}

	$scope.cargarMatrizMetasTipoAP = function() {
		AprobacionPlanificacionFactory.cargarMatrizMetas(
			$scope.objeto.id,
			$rootScope.ejefiscal,
			$scope.tipo
		).then(function(resp){
			console.log(resp);
			if (!resp.estado) return;
			$scope.unidad = resp.json.unidad;
			$scope.nombreinstitucion = $scope.unidad.codigoinstitucion + " " + $scope.unidad.nombreinstitucion;
			$scope.nombreinstentidad = $scope.unidad.codigoinstentidad + " " + $scope.unidad.nombreinstentidad;
			$scope.nombreunidad = $scope.unidad.codigounidad + " " + $scope.unidad.nombreunidad;
			$scope.cabecera = resp.json.cabecera[0];
			$scope.programa = $scope.cabecera.programacodigo + " " + $scope.cabecera.programa;
			$scope.proyecto = $scope.cabecera.proyectocodigo + " " + $scope.cabecera.proyecto;
			$scope.actividad = $scope.cabecera.actividadcodigo + " " + $scope.cabecera.actividad;
			$scope.subactividad = $scope.cabecera.codigo + " " + $scope.cabecera.descripcion;
			$scope.detalles = resp.json.detalles;
			$scope.edicionMatrizMetasAP = true;
		});
	}

	$scope.guardar = function(tipo) {
		var tObj =  [];
		for (var i = 0; i < $scope.detalles.length; i++) {
			if ($scope.detalles[i].modificado) {
				tObj.push($scope.detalles[i]);
				$scope.detalles[i].modificado=false;
			}
		}
		AprobacionPlanificacionFactory.guardar(
			tipo,
			tObj
		).then(function(resp){
			//console.log(resp.json);
			if (!resp.estado) {
				SweetAlert.swal(
					"Aprobacion Planificacion!",
					resp.mensajes.msg,
					"error"
				);
				return;
			}
			SweetAlert.swal(
				"Aprobacion Planificacion!",
				"Se grabo correctamente.",
				"success"
			);
		})
	};
} ]);

function distribuirValor(
	fuente,
	detalles,
	total,
	cantidad,
	divActividad,
	divSubItem,
	vLimpio
) {
	var presupuesto = total;
	var nPeriodo = 0;
	var intervalo = 0;

	switch (fuente.unidadtiempo) {
		case "ME": // Mensual
			nPeriodo = 12;
			intervalo = 1;
			break;
		case "BI": // Bimensual
			nPeriodo = 6;
			intervalo = 2;
			break;
		case "TM": // Trimestral
			nPeriodo = 4;
			intervalo = 3;
			break;
		case "CM": // Cuatrimestral
			nPeriodo = 3;
			intervalo = 4;
			break;
		case "SE": // Semestral
			nPeriodo = 2;
			intervalo = 6;
			break;
		case "AN": // Anual
			nPeriodo = 1;
			intervalo = 12;
			break;
		case "PE": // Personalizado
			nPeriodo = 0;
			intervalo = 1;
			break;
		default:
			break;
	}

	if (nPeriodo != 0 || vLimpio) {
		for (var i = 0; i < 12; i++) {
			detalles[i].valor = 0;
			detalles[i].porcentaje = 0;
		}
	}

	var valor = Number(
		presupuesto / nPeriodo
	);
	var valorResto = valor - valor.toFixed(2);
	var porcientoResto = 0;
	var suma = 0;
	var resto = 0;
	for (var i = (intervalo - 1); i < 12; i = i + intervalo) {
		if (nPeriodo != 0) {
			if (i == 11) {
				detalles[11].valor = Number((presupuesto - suma).toFixed(2));
			} else {
				detalles[i].valor = Number(valor.toFixed(2));
				resto = resto + valorResto;
				if (resto >= 0.01) {
					detalles[i].valor = Number(detalles[i].valor + Number(resto.toFixed(2)));
					resto = resto - resto.toFixed(2);
				}
				suma = suma + detalles[i].valor;
			}
		}
		if (!divSubItem) {
			if (detalles[i].valor == undefined) {
				detalles[i].valor = 0;
			}
			if (detalles[i].valor == 0 || presupuesto == 0) {
				detalles[i].porcentaje = 0;
			} else {
				detalles[i].porcentaje = Number(
					(detalles[i].valor * 100)
					/ presupuesto
				);
				if (detalles[i].porcentaje == NaN) {
					detalles[i].porcentaje = 0;
				} else {
					porcientoResto = porcientoResto + (detalles[i].porcentaje - Number(detalles[i].porcentaje.toFixed(2)));
					detalles[i].porcentaje = Number(detalles[i].porcentaje.toFixed(2));
					if (porcientoResto >= 0.01) {
						detalles[i].porcentaje = Number(detalles[i].porcentaje + Number(porcientoResto.toFixed(2)));
						porcientoResto = porcientoResto - porcientoResto.toFixed(2);
					} else if (porcientoResto <= -0.01) {
						detalles[i].porcentaje = Number(detalles[i].porcentaje + Number(porcientoResto.toFixed(2)));
						porcientoResto = porcientoResto - porcientoResto.toFixed(2);
					}
				}
			}
		}
	}

	if (nPeriodo == 0 || !divActividad) {
		return;
	}
	valor = Number(
		cantidad / nPeriodo
	);
	valorResto = valor - valor.toFixed(2);
	suma = 0;
	resto = 0;
	for (var i = (intervalo - 1); i < 12; i = i + intervalo) {
		if (i == 11) {
			detalles[11].valor = Number((cantidad - suma).toFixed(2));
		} else {
			try {
				detalles[i].valor = Number(valor.toFixed(2));
				resto = resto + valorResto;
				if (resto >= 0.01) {
					detalles[i].valor = Number(detalles[i].valor + Number(resto.toFixed(2)));
					resto = resto - Number(resto.toFixed(2));
				}
				suma = suma + detalles[i].valor;
			} catch (e) {
				detalles[i].valor = 0;
			} 
		}
	}
}

function distribucionCalcularTotal(
	detalles
) {
	var total = 0;
	for (var i = 0; i < 12; i++) {
		total = total + detalles[i].valor;
	}
	return total;
}
