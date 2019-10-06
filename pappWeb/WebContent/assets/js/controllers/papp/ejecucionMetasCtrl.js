'use strict';

app.controller('EjecucionMetasController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","ejecucionMetasFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, ejecucionMetasFactory) {

	$scope.codigoFiltro=null;
	$scope.nombreFiltro=null;
	$scope.estadoFiltro=null;

	$scope.edicion=false;
	$scope.guardar=false;
	$scope.listaActividades=null;
	$scope.listaSubactividades=null;
	$scope.listaTareas=null;
	$scope.actividad={};
	$scope.subactividad={};
	$scope.tarea={};
	$scope.mesDesde=null;
	$scope.mesHasta=null;
	$scope.mes=null;
	$scope.edicionActividades=false;
	$scope.edicionSubtareas=false;

	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.limpiarBase();
		$scope.data = [];
		ejecucionMetasFactory.traerFiltro(pagina, $rootScope.ejefiscal, null, null, null).then(function(resp){
			if (resp.meta)
				$scope.data = resp;
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
		console.log($scope.estadoFiltro);
		$scope.data=[];
		ejecucionMetasFactory.traerFiltro(
			pagina,
			$rootScope.ejefiscal,
			$scope.codigoFiltro,
			$scope.nombreFiltro,
			$scope.estadoFiltro
		).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}

	$scope.limpiar=function(){
		$scope.codigoFiltro=null;
		$scope.nombreFiltro=null;
		$scope.estadoFiltro=null;
		$scope.consultar();
	};

	$scope.limpiarBase=function() {
		$scope.listaActividades = null;
		$scope.institucion = null;
		$scope.institucionId = null;
		$scope.entidad = null;
		$scope.entidadId = null;
		$scope.unidad = null;
		$scope.unidadId = null;
		$scope.listaDetalles = null;
		$scope.programa = null;
		$scope.proyecto = null;
	}

	$scope.actulizarPantalla=function() {
		// esta se utiliza solo para que refresque la pantalla
	}

	$scope.editarActividad=function(id) {
		$scope.limpiarBase();
		ejecucionMetasFactory.traerActividades(
				id,
				$rootScope.ejefiscal
		).then(function(resp){
			//console.log(resp.json);
			$scope.edicion = true;
			$scope.edicionActividades = true;
			$scope.listaActividades = resp.json.result;
			$scope.institucion = $scope.listaActividades[0].npInstitucioncodigo + ' ' + $scope.listaActividades[0].npInstitucion;
			$scope.institucionId = $scope.listaActividades[0].npInstitucionId;
			$scope.entidad = $scope.listaActividades[0].npentidadcodigo + ' ' + $scope.listaActividades[0].npentidad;
			$scope.entidadId = $scope.listaActividades[0].npentidadid;
			$scope.unidad = $scope.listaActividades[0].npunidadcodigo + ' ' + $scope.listaActividades[0].npunidadnombre;
			$scope.unidadId = $scope.listaActividades[0].npunidad;
		})
	};

	$scope.renovarActividad=function() {
		ejecucionMetasFactory.traerRenovarActividades(
				$scope.institucionId,
				$scope.entidadId,
				$scope.unidadId,
				$rootScope.ejefiscal,
				$scope.actividad.id,
				$scope.mes
		).then(function(resp){
			//console.log(resp.json);
			$scope.listaDetalles = resp.json.result[0].ejecuciondetalleacts;
			$scope.programa = resp.json.result[0].programa;
			$scope.proyecto = resp.json.result[0].proyecto;
		})
	}

	$scope.guardarLineActividad = function(index) {
        ejecucionMetasFactory.guardarLineActividad($scope.listaDetalles[index]).then(function(resp){
			 if (resp.estado){
//	             SweetAlert.swal(
//	            		 "EjecucionMetas!",
//	            		 "Registro guardado satisfactoriamente!",
//	            		 "success"
//        		 );
			 }else{
	             SweetAlert.swal(
	            		 "EjecucionMetas!",
	            		 resp.mensajes.msg,
	            		 "error"
        		 );
			 }
		})
    };

    $scope.volverActividad = function() {
		$scope.edicionActividades = false;
		$scope.edicion = false;
	}

	$scope.editarSubtareas = function(id) {
		$scope.limpiarBase();
		ejecucionMetasFactory.traerActividades(
				id,
				$rootScope.ejefiscal
		).then(function(resp){
			//console.log(resp.json);
			$scope.edicion = true;
			$scope.edicionSubtareas = true;
			$scope.listaActividades = resp.json.result;
			$scope.institucion = $scope.listaActividades[0].npInstitucioncodigo + ' ' + $scope.listaActividades[0].npInstitucion;
			$scope.institucionId = $scope.listaActividades[0].npInstitucionId;
			$scope.entidad = $scope.listaActividades[0].npentidadcodigo + ' ' + $scope.listaActividades[0].npentidad;
			$scope.entidadId = $scope.listaActividades[0].npentidadid;
			$scope.unidad = $scope.listaActividades[0].npunidadcodigo + ' ' + $scope.listaActividades[0].npunidadnombre;
			$scope.unidadId = $scope.listaActividades[0].npunidad;
		})
	};

	$scope.actulizarSubactividades = function(id) {
		ejecucionMetasFactory.traerSubactividades(
				$scope.actividad.id,
				$scope.actividad.npunidad,
				$rootScope.ejefiscal
		).then(function(resp){
			$scope.listaSubactividades=resp.json.result;
		})
	};

	$scope.actulizarTareas = function(id) {
		ejecucionMetasFactory.traerTareas(
				id,
				$rootScope.ejefiscal
		).then(function(resp){
			$scope.listaSubactividades=resp.json.result;
		})
	};

	$scope.renovarSubtareas=function() {
		ejecucionMetasFactory.traerRenovarSubtareas(
				$scope.actividad,
				$rootScope.ejefiscal,
				$scope.mesDesde,
				$scope.mesHasta,
				$scope.subactividad.id,
				$scope.tarea.id
		).then(function(resp){
			//console.log(resp.json);
			$scope.listaDetalles=resp.json.result;
		})
	}

	$scope.guardarLineSubtarea = function(index) {
        var objEnviar = Object.assign({}, $scope.objeto);
        objEnviar.details = $scope.objetolista;
    	ejecucionMetasFactory.guardarLineSubtarea(objEnviar).then(function(resp){
			 if (resp.estado){
//	             SweetAlert.swal(
//	            		 "EjecucionMetas!",
//	            		 "Registro guardado satisfactoriamente!",
//	            		 "success"
//        		 );
			 }else{
	             SweetAlert.swal(
	            		 "EjecucionMetas!",
	            		 resp.mensajes.msg,
	            		 "error"
        		 );
			 }
		})
    };

    $scope.volverSubtareas = function() {
		$scope.edicionSubtareas = false;
		$scope.edicion = false;
	}
} ]);