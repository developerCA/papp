'use strict';
 
app.controller('ModalOrdenReversionLineasController', [ "$scope","$rootScope","ordenReversionID","unidadID","editar","ordenGastoID","ordenGastoValor","noeditar","$uibModalInstance","SweetAlert","$filter", "ngTableParams","ordenReversionLineasFactory",
	function($scope,$rootScope,ordenReversionID,unidadID,editar,ordenGastoID,ordenGastoValor,noeditar,$uibModalInstance,SweetAlert,$filter, ngTableParams,ordenReversionLineasFactory) {

	$scope.noeditar=false;
	$scope.noeditar2=false;
	$scope.init=function(){
		$scope.editarValor = (ordenGastoValor == 0? true: false);
		if (editar == null) {
			//nuevo
			ordenReversionLineasFactory.nuevoLinea(
				ordenReversionID
			).then(function(resp){
				//console.log(resp.json.ordenreversionlinea);
	        	$scope.objeto = resp.json.ordenreversionlinea;
	        	$scope.noeditar = false;
	        	$scope.cargarSubItems();
			})
		} else {
			//editar
			ordenReversionLineasFactory.editarLinea(
				editar
			).then(function(resp){
				//console.log(resp);
	        	$scope.objeto = resp.json.ordenreversionlinea;
	        	$scope.objetoDetalles = resp.json.subiteminfo;
	        	$scope.objeto.npvalor = $scope.objeto.nptotalordengasto - $scope.objeto.npdevengado;
	        	$scope.noeditar = true;
	        	$scope.noeditar2 = (!$scope.editarValor? false: noeditar);
			})
		}
	}

	$scope.cargarSubItems=function(){
		$scope.listarSubItems = [];
		//console.log($scope.objeto);
		ordenReversionLineasFactory.listarSubItems(
			ordenGastoID
		).then(function(resp){
			$scope.si = resp.json.result;
			$scope.listarSubItems = null;
        	$scope.listarSubItems = [{
        		id: "",
        		npSubitem: "Selecione un subitem"
        	}];//.concat(resp.json.result);
        	for (var i = 0; i < resp.json.result.length; i++) {
        		resp.json.result[i].npSubitem = resp.json.result[i].npSubitemcodigo + ": " + resp.json.result[i].npSubitem;
        		$scope.listarSubItems.push(resp.json.result[i]);
			}
		})
	}

	$scope.obtenerTotal=function(){
		var i;
		for (i = 0; i < $scope.si.length; i++) {
			if ($scope.si[i].nivelactid == $scope.objeto.subitem)
				break;
		}
		if (i == $scope.si.length)
			return;
		$scope.objeto.nptotalordengasto = $scope.si[i].valor;
		$scope.objeto.npdevengado = 0; //$scope.si[i].valor;
		ordenReversionLineasFactory.obtenerOtros(
			$scope.si[i].npSubitemunidadid,
			$scope.si[i].nivelactid //, ordenGastoID
		).then(function(resp){
			//console.log(resp);
        	$scope.objeto.npdevengado = resp.json.datoslineaordend.aprobadas;
			$scope.objeto.npdevengosnoapro = resp.json.datoslineaordend.noaprobadas;
        	//$scope.objeto.npdevengosnoapro = resp.json.datoslineaordend.noaprobadas;
        	//$scope.objeto.npsaldodisponible = resp.json.datoslineaordend.saldo;
        	$scope.objeto.npsaldo = resp.json.datoslineaordend.saldo;
        	//$scope.objeto.npvalor = ($scope.objeto.nptotalordengasto - $scope.objeto.npdevengado) - $scope.objeto.npdevengosnoapro;
        	$scope.objeto.npvalor = $scope.objeto.nptotalordengasto - $scope.objeto.npdevengado;
		})
		ordenReversionLineasFactory.obtenerTotal(
			$scope.si[i].nivelactid
		).then(function(resp){
			//console.log(resp);
			$scope.objetoDetalles = resp.json.subiteminfo;
			$scope.ponerCodigos();
		})
	}

	$scope.ponerCodigos = function() {
		$scope.objetoDetalles.programanombre = $scope.objetoDetalles.programacodigo + ": " + $scope.objetoDetalles.programanombre;
		$scope.objetoDetalles.subprogramanombre = $scope.objetoDetalles.subprogramacodigo + ": " + $scope.objetoDetalles.subprogramanombre;
		$scope.objetoDetalles.proyectonombre = $scope.objetoDetalles.proyectocodigo + ": " + $scope.objetoDetalles.proyectonombre;
		$scope.objetoDetalles.actividadnombre = $scope.objetoDetalles.actividadcodigo + ": " + $scope.objetoDetalles.actividadnombre;
		$scope.objetoDetalles.subactividadnombre = $scope.objetoDetalles.subactividadcodigo + ": " + $scope.objetoDetalles.subactividadnombre;
		$scope.objetoDetalles.tareanombre = $scope.objetoDetalles.tareacodigo + ": " + $scope.objetoDetalles.tareanombre;
		$scope.objetoDetalles.subtareanombre = $scope.objetoDetalles.subtareacodigo + ": " + $scope.objetoDetalles.subtareanombre;
		$scope.objetoDetalles.itemnombre = $scope.objetoDetalles.itemcodigo + ": " + $scope.objetoDetalles.itemnombre;
		if (editar != null) {
			$scope.objetoDetalles.subitemnombre = $scope.objetoDetalles.subitemcodigo + ": " + $scope.objetoDetalles.subitemnombre;
		}
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
            	if ($scope.objeto.npdevengosnoapro > 0) {
                    SweetAlert.swal(
                		"Orden Reversion! - Lineas",
                		"No puede realizar una orden de reversion mientras tenga ordenes de devengo resgistrados o solicitados",
                		"error"
            		);
            		return;
            	}
            	if ($scope.objeto.valor.toFixed(2) > $scope.objeto.npsaldo.toFixed(2)) {
                    SweetAlert.swal(
                		"Orden Devengo! - Lineas",
                		"El valor no puede ser mayor que el saldo por devengar",
                		"error"
            		);
            		return;
            	}
            	var tObj = Object.assign({}, $scope.objeto);
            	if (tObj.nivelactid == 0) {
	            	tObj.nivelactid = tObj.subitem;
	            	delete tObj.subitem;
            	}
            	ordenReversionLineasFactory.guardarLinea(tObj).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.objeto={};
	 		    		 $uibModalInstance.close(resp.json);		
        			 }else{
	 		             SweetAlert.swal(
 		            		 "Orden Reversion! - Lineas",
 		            		 resp.mensajes.msg,
 		            		 "error"
	            		 );
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
