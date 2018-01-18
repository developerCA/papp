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

	$scope.editarAprobarPlanificacion=function(index){
		index = ((pagina - 1) * 5) + index;
		if ($scope.data[index].npestadopresupuesto != "Planificado") {
			SweetAlert.swal("Aprobacion Planificacion!", "Solo se puede aprobar si esta Planificado", "warning");
			return;
		}
		SweetAlert.swal({
		   title: "Aprobacion Planificacion?",
		   text: "Seguro que desea Aprobar la Planificacion indicada",
		   type: "warning",
		   showCancelButton: true,
		   confirmButtonColor: "#DD6B55",
		   closeOnConfirm: true}, 
	    function(isConfirm){ 
		    if (!isConfirm) return;
	   		AprobacionPlanificacionFactory.editarAprobarPlanificacion(
			    $scope.data[index].id,
				$rootScope.ejefiscal,
				$scope.data[index].npacitividadunidad,
				"P"
			).then(function(resp){
				SweetAlert.swal("Aprobacion Planificacion!", resp.mensajes.msg, resp.mensajes.type);
			})
		});
	};

	$scope.editarAprobarAjustada=function(index){
		index = ((pagina - 1) * 5) + index;
		if ($scope.data[index].npestadopresupuesto != "Planificado") {
			SweetAlert.swal("Aprobacion Planificacion!", "Solo se puede aprobar si esta Planificado", "warning");
			return;
		}
		SweetAlert.swal({
		   title: "Aprobacion Planificacion?",
		   text: "Seguro que desea Aprobar el Ajustado indicada",
		   type: "warning",
		   showCancelButton: true,
		   confirmButtonColor: "#DD6B55",
		   closeOnConfirm: true}, 
	    function(isConfirm){ 
		    if (!isConfirm) return;
	   		AprobacionPlanificacionFactory.editarAprobarPlanificacion(
			    $scope.data[index].id,
				$rootScope.ejefiscal,
				$scope.data[index].npacitividadunidad,
				"A"
			).then(function(resp){
				SweetAlert.swal("Aprobacion Planificacion!", resp.mensajes.msg, resp.mensajes.type);
			})
		});
	};

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
	}
/*
	$scope.editarMatrizMetas=function(index){
		AprobacionPlanificacionFactory.traer(id).then(function(resp){
			if (resp.estado)
			   $scope.objeto=resp.json.aprobacionPlanificacion;
		   $scope.detalles=resp.json.details;
		   $scope.edicion=true;
		   console.log(resp.json);
		})
	};

	$scope.editaMatrizPresupuestos=function(index){
		AprobacionPlanificacionFactory.traer(id).then(function(resp){
			if (resp.estado)
			   $scope.objeto=resp.json.aprobacionPlanificacion;
		   $scope.detalles=resp.json.details;
		   $scope.edicion=true;
		   console.log(resp.json);
		})
	};
*/
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
            	$scope.objeto.details=$scope.detalles;
            	AprobacionPlanificacionFactory.guardar($scope.objeto).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.objeto={};
	 		             $scope.detalles=[];
	 		             $scope.limpiar();
	 		             SweetAlert.swal("Clase de Registro!", "Registro registrado satisfactoriamente!", "success");
        			 }else{
	 		             SweetAlert.swal("Clase de Registro!", resp.mensajes.msg, "error");
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
