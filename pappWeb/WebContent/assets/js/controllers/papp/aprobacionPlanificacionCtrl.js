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
		//console.log($scope.data[index]);
/* $scope.data[index].tipo
		AprobacionPlanificacionFactory.editarAprobarPlanificacion(
				$scope.data[index].id
 */
		AprobacionPlanificacionFactory.editarAprobarPlanificacion(
			$scope.data[index].id,
			$rootScope.ejefiscal,
			$scope.data[index].npacitividadunidad,
			"P"
		).then(function(resp){
			SweetAlert.swal("Aprobacion Planificacion!", resp.mensajes.msg, resp.mensajes.type);
		})
	};

	$scope.editarAprobarAjustada=function(index){
		AprobacionPlanificacionFactory.editarAprobarPlanificacion(
			$scope.data[index].id,
			$rootScope.ejefiscal,
			$scope.data[index].npacitividadunidad,
			"A"
		).then(function(resp){
			SweetAlert.swal("Aprobacion Planificacion!", resp.mensajes.msg, resp.mensajes.type);
		})
	};

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
