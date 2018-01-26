'use strict';

app.controller('AprobacionPlanificacionController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","AprobacionPlanificacionFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams,AprobacionPlanificacionFactory) {

	$scope.codigoFiltro=null;
	$scope.nombreFiltro=null;
	$scope.estadoFiltro=null;
	$scope.edicion=false;
	$scope.data=[];
	$scope.objeto={};
	$scope.detalles=[];
	$scope.edicionMatrizPresupuesto = false;
	$scope.edicionMatrizMetas = false;

	var pagina = 1;

	$scope.consultar=function(){
		AprobacionPlanificacionFactory.traer(
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
		AprobacionPlanificacionFactory.traerFiltro(
			pagina,
			$rootScope.ejefiscal,
			$scope.nombreFiltro,
			$scope.codigoFiltro
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

	$scope.nuevo=function(){
		$scope.objeto={id:null,estado:'A'};
		$scope.detalles=[];
		$scope.edicion=true;
	}
	
	$scope.aprobarPlanificacion = function(obj) {
		if (obj.npestadopresupuesto != "Planificado") {
			SweetAlert.swal("Aprobacion Planificacion!", "Solo se puede aprobar si esta Planificado", "warning");
			return;
		}
		$scope.aprobacionPlanificacion = true;
		$scope.objeto = obj;
	}

	$scope.editarAprobarPlanificacion=function(){
		AprobacionPlanificacionFactory.editarAprobarPlanificacion(
		    $scope.data[$scope.index].id,
			$rootScope.ejefiscal,
			$scope.data[$scope.index].npacitividadunidad,
			"P"
		).then(function(resp){
			$scope.detalle = resp.json.resultadoaprobacion;
			SweetAlert.swal("Aprobacion Planificacion!", resp.mensajes.msg, resp.mensajes.type);
		});
	};
	
	$scope.editadoObservacion = function(index) {
		//index = ((pagina - 1) * 5) + index;
		$scope.detalle[index].modificado = true;
	}
	
	$scope.aprobarAjustado = function(index) {
		$scope.index = ((pagina - 1) * 5) + index;
		if ($scope.data[$scope.index].npestadopresupuesto != "Aprobado") {
			SweetAlert.swal(
				"Aprobacion Planificacion!",
				"Solo se puede aprobar el Ajustado si el Planificado esta Aprobado",
				"warning"
			);
			return;
		}
		$scope.aprobacionAjustado = true;
		$scope.objeto = $scope.data[index];
	}

	$scope.editarAprobarAjustada=function(index){
   		AprobacionPlanificacionFactory.editarAprobarPlanificacion(
		    $scope.data[$scope.index].id,
			$rootScope.ejefiscal,
			$scope.data[$scope.index].npacitividadunidad,
			"A"
		).then(function(resp){
			$scope.detalle = resp.json.resultadoaprobacion;
			SweetAlert.swal("Aprobacion Planificacion!", resp.mensajes.msg, resp.mensajes.type);
		})
	};

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

	$scope.editarMatrizPresupuesto = function(index) {
		$scope.tipo = "P";
		$scope.index = ((pagina - 1) * 5) + index;
		$scope.cargarMatrizPresupuestoTipo();
	}

	$scope.editarMatrizMetas = function(index) {
		$scope.tipo = "P";
		$scope.index = ((pagina - 1) * 5) + index;
		$scope.cargarMatrizMetasTipo();
	}

	$scope.cargarMatrizPresupuestoTipo = function() {
		AprobacionPlanificacionFactory.cargarMatrizPresupuesto(
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
			$scope.edicionMatrizPresupuesto = true;
		});
	}

	$scope.cargarMatrizMetasTipo = function() {
		AprobacionPlanificacionFactory.cargarMatrizMetas(
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
			$scope.edicionMatrizMetas = true;
		});
	}

	$scope.volver = function() {
		$scope.edicionMatrizPresupuesto = false;
		$scope.edicionMatrizMetas = false;
		$scope.aprobacionPlanificacion = false;
		$scope.aprobacionAjustado = false;
	}

	$scope.guardar = function(tipo) {
		var tObj =  [];
		for (var i = 0; i < $scope.detalle.length; i++) {
			if ($scope.detalle[i].modificado) {
				tObj.push($scope.detalle[i]);
				$scope.detalle[i].modificado=false;
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

	$scope.abrirPUE = function(obj) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/planificacionUE.html',
			controller : 'ModalPlanificacionUEController',
			size : 'lg',
			resolve : {
				objFuente : function() {
					return obj;
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
} ]);
