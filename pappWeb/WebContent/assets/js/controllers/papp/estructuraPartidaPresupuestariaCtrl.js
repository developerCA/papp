'use strict';

app.controller('EstructuraPartidaPresupuestariaController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","estructuraPartidaPresupuestariaFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, estructuraPartidaPresupuestariaFactory) {

	$scope.edicion=true;
	$scope.guardar=false;
	$scope.objeto={estado:null};

	$scope.feLista=[
		{id: 1, tipo: 'Programa', longitud: 2},
		{id: 2, tipo: 'Subprograma', longitud: 2},
		{id: 3, tipo: 'Proyecto', longitud: 3},
		{id: 4, tipo: 'Actividad', longitud: 3},
		{id: 5, tipo: 'Subsctividad', longitud: 3}
	];
	$scope.pLista=[
		{id: 1, tipo: 'Actividad', prefijo: '', separador: 0, longitud: 3},
		{id: 2, tipo: 'Subactividad', prefijo: '', separador: 0, longitud: 3},
		{id: 3, tipo: 'Tarea', prefijo: '', separador: 0, longitud: 3},
		{id: 4, tipo: 'Subtarea', prefijo: '', separador: 0, longitud: 3},
		{id: 5, tipo: 'Item', prefijo: '', separador: 0, longitud: 6}
	];
	
	$scope.nuevo=function(){
		$scope.objeto={id:null,estado:null};
		$scope.edicion=true;
		$scope.nuevoar=true;
		$scope.guardar=true;
	}

	$scope.editar=function(){
		$scope.aejefiscal=$rootScope.ejefiscalobj.anio;

		//console.log($scope.aejefiscal);
		//console.log($rootScope);

		estructuraPartidaPresupuestariaFactory.traerPartidapresupuestaria($rootScope.ejefiscal).then(function(resp){
			//console.log(resp.json);
			if (resp.estado) {
			    $scope.feLista=resp.json.formulacion;
			    $scope.pLista=resp.json.planificacion;
			}
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
            	let tObj = {};
            	tObj.parmnivelesprogramanivelTOs=$scope.feLista;
            	tObj.nivelesplanificacionTOs=$scope.pLista;
            	//tObj.nivelesplanificacionTOs[2].nptipo = "proyecto";
            	estructuraPartidaPresupuestariaFactory.guardar(tObj).then(function(resp){
        			 if (resp.estado){
        				 SweetAlert.swal("Estructura Partida Presupuestaria!", "Registro guardado satisfactoriamente!", "success");
        			 }else{
	 		             SweetAlert.swal("Estructura Partida Presupuestaria!", resp.mensajes.msg, "error");
        			 }
        		})
            }
        },

        reset: function (form) {
            $scope.myModel = angular.copy($scope.master);
            form.$setPristine(true);
            $scope.edicion=false;
            $scope.objeto={};
        	$scope.divEO=false;
        	$scope.divMU=false;
        	$scope.divME=false;
            $scope.limpiar();
        }
    };
} ]);
