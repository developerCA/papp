'use strict';

app.controller('ModalCertificacionesFondosLineasController', [ "$scope","$rootScope","ID","unidadID","editar","$uibModalInstance","SweetAlert","$filter", "ngTableParams","certificacionesFondosFactory",
	function($scope,$rootScope,ID,unidadID,editar,$uibModalInstance,SweetAlert,$filter, ngTableParams,certificacionesFondosFactory) {

	$scope.noeditar = false;

	$scope.init=function(){
		//$scope.npcertificacionvalor = npcertificacionvalor;
		if (editar == null) {
			//nuevo
			certificacionesFondosFactory.nuevoLinea(
				ID
			).then(function(resp){
				console.log(resp.json.certificacionlinea);
	        	$scope.objeto = resp.json.certificacionlinea;
	        	$scope.noeditar = false;
	        	$scope.saldo = 0;
	        	certificacionesFondosFactory.listarSubtareas(
					$rootScope.ejefiscal,
					unidadID
				).then(function(resp){
		        	for (var i = 0; i < resp.json.result.length; i++) {
		        		resp.json.result[i].descripcionexten = resp.json.result[i].npcodigo + " - " + resp.json.result[i].npdescripcion;
					}
		        	$scope.listarSubtareas = [{
		        		id: "",
		        		descripcionexten: "Selecione una subtarea"
		        	}].concat(resp.json.result);
				})
			})
		} else {
			//editar
			certificacionesFondosFactory.editarLinea(
					editar
				).then(function(resp){
					//console.log(resp);
		        	$scope.objeto = resp.json.certificacionlinea;
		        	$scope.objetoDetalles = resp.json.subiteminfo;
		        	$scope.saldo = $scope.objeto.npvalor + $scope.objeto.npvalorinicial;
		        	$scope.ponerCodigos();
		        	$scope.noeditar=true;
				})
		}
	}

	$scope.ponerCodigos = function() {
		$scope.objetoDetalles.programanombre = $scope.objetoDetalles.programacodigo + ": " + $scope.objetoDetalles.programanombre;
		$scope.objetoDetalles.subprogramanombre = $scope.objetoDetalles.subprogramacodigo + ": " + $scope.objetoDetalles.subprogramanombre;
		$scope.objetoDetalles.proyectonombre = $scope.objetoDetalles.proyectocodigo + ": " + $scope.objetoDetalles.proyectonombre;
		$scope.objetoDetalles.actividadnombre = $scope.objetoDetalles.actividadcodigo + ": " + $scope.objetoDetalles.actividadnombre;
		$scope.objetoDetalles.subactividadnombre = $scope.objetoDetalles.subactividadcodigo + ": " + $scope.objetoDetalles.subactividadnombre;
		$scope.objetoDetalles.tareanombre = $scope.objetoDetalles.tareacodigo + ": " + $scope.objetoDetalles.tareanombre;
		if (editar != null) {
			$scope.objetoDetalles.subtareanombre = $scope.objetoDetalles.subtareacodigo + ": " + $scope.objetoDetalles.subtareanombre;
			$scope.objetoDetalles.itemnombre = $scope.objetoDetalles.itemcodigo + ": " + $scope.objetoDetalles.itemnombre;
			$scope.objetoDetalles.subitemnombre = $scope.objetoDetalles.subitemcodigo + ": " + $scope.objetoDetalles.subitemnombre;
		}
	}

	$scope.cambioItems=function(){
		var i = $scope.buscarSubtareas($scope.objeto.subtarea);
		//$scope.objeto.subitemid = $scope.listarSubtareas[i].id;
		$scope.objeto.npSubitemcodigo = $scope.listarSubtareas[i].npcodigo;
		$scope.objeto.npSubitem = $scope.listarSubtareas[i].npdescripcion;
		certificacionesFondosFactory.obtenerDetalles(
			$scope.objeto.subtarea
		).then(function(resp){
			//console.log("RR:",resp);
        	$scope.objetoDetalles = resp.json.subtareainfo;
			//console.log($scope.objetoDetalles);
		})
		certificacionesFondosFactory.listarItems(
			$rootScope.ejefiscal,
			$scope.objeto.subtarea
		).then(function(resp) {
        	for (var i = 0; i < resp.json.result.length; i++) {
        		resp.json.result[i].descripcionexten = resp.json.result[i].npcodigo + " - " + resp.json.result[i].npcodigocanton + " - " + resp.json.result[i].npcodigofuente + " - " + resp.json.result[i].npdescripcion;
			}
        	$scope.listarItems = [{
        		id: "",
        		descripcionexten: "Selecione un item"
        	}].concat(resp.json.result);
        	$scope.ponerCodigos();
		})
	}

	$scope.cambioSubItems=function(){
		certificacionesFondosFactory.listarSubItems(
			$rootScope.ejefiscal,
			$scope.objeto.item
		).then(function(resp){
			$scope.si = resp.json.result;
			$scope.listarSubItems = null;
        	for (var i = 0; i < resp.json.result.length; i++) {
        		resp.json.result[i].descripcionexten = resp.json.result[i].npcodigo + " - " + resp.json.result[i].npdescripcion;
        		//$scope.listarSubItems.push(resp.json.result[i]);
			}
        	$scope.listarSubItems = [{
        		id: "",
        		descripcionexten: "Selecione un subitem"
        	}].concat(resp.json.result);
		})
	}

	$scope.obtenerTotal=function(){
		var i;
		for (i = 0; i < $scope.si.length; i++) {
			if ($scope.si[i].id == $scope.objeto.subitem)
				break;
		}
		if (i == $scope.si.length)
			return;
		certificacionesFondosFactory.obtenerTotal(
			$scope.si[i].tablarelacionid,
			$scope.si[i].id
		).then(function(resp){
			//console.log(resp);
        	$scope.saldo = resp.json.valordisponiblesi.saldo;
		})
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
            	if (Number($scope.objeto.valor) > Number($scope.saldo)) {
        			SweetAlert.swal(
        					"Certificaciones de Fondos!",
        					"El valor no puede ser mayor que el saldo.",
        					"error"
        			);
        			return;
            	}
            	var tObj = Object.assign({}, $scope.objeto);
            	if (tObj.nivelactid == 0) {
            	  tObj.nivelactid = tObj.subitem;
            	  //delete tObj.subitem;
            	}
            	certificacionesFondosFactory.guardarLinea(tObj).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.objeto={};
	 		             tObj = {
 		            		 lineas: resp.json.certificacionlineas,
 		            		 valortotal: resp.json.certificacion.valortotal
	 		             }
	 		    		 $uibModalInstance.close(tObj);		
        			 }else{
	 		             SweetAlert.swal("Certificaciones de Fondos!", resp.mensajes.msg, "error");
        			 }
        		})
        		//console.log($scope.objeto);
            }
        },
        reset: function (form) {
    		$uibModalInstance.dismiss('cancel');
        }
    };

	$scope.buscarSubtareas=function(id){
		for (var i = 0; i < $scope.listarSubtareas.length; i++) {
			if ($scope.listarSubtareas[i].id == id) {
				return i;
			}
		}
		return false;
	}
}]);
