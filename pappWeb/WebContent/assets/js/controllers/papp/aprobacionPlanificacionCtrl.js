'use strict';

app.controller('AprobacionPlanificacionController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","AprobacionPlanificacionFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams,AprobacionPlanificacionFactory) {

	$scope.nombreFiltro=null;
	$scope.codigoFiltro=null;
	$scope.estadoFiltro=null;
	$scope.edicion=false;
	$scope.objeto={};
	$scope.objetoPA={};
	$scope.detalles=[];
	$scope.divPlanificacionAnual=false;

	var pagina = 1;

	$scope.consultar=function(){
		$scope.data=[];
		AprobacionPlanificacionFactory.traerAprobacionPlanificacion(
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
		AprobacionPlanificacionFactory.traerAprobacionPlanificacionFiltro(
			pagina,
			$rootScope.ejefiscal,
			$scope.nombreFiltro,
			$scope.codigoFiltro,
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

	$scope.nuevo=function(){
		$scope.objeto={id:null,estado:'A'};
		$scope.detalles=[];
		$scope.edicion=true;
	}

	$scope.editar=function(id){
		AprobacionPlanificacionFactory.traerAprobacionPlanificacion(id).then(function(resp){
			if (resp.estado)
			   $scope.objeto=resp.json.aprobacionPlanificacion;
			   $scope.detalles=resp.json.details;
			   $scope.edicion=true;
			  console.log(resp.json);
		})
	};

	$scope.editarPlanificacionAnual=function(id){
		console.log(id);
		$scope.data=[];
		$scope.divPlanificacionAnual=true;
		AprobacionPlanificacionFactory.traerPlanificacionAnual(
			id,
			$rootScope.ejefiscal
		).then(function(resp){
			console.log(resp)
			if (resp.meta)
				$scope.dataPA=resp;
			$scope.divPlanificacionAnual=true;
		})
	}

	$scope.agregarDetalle=function(){
		var obj={id: {id: null}, codigo: null, estado: "A", nombre: null};
		$scope.detalles.push(obj);
	}

	$scope.removerDetalle=function(index){
		$scope.detalles.splice(index,1);
	}

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