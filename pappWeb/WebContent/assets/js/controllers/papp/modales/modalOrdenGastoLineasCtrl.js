'use strict';

app.controller('ModalOrdenGastoLineasController', [ "$scope","$rootScope","ID","certificacionID","editar","npcertificacionvalor","$uibModalInstance","SweetAlert","$filter", "ngTableParams","ordenGastoLineasFactory",
	function($scope,$rootScope,ID,certificacionID,editar,npcertificacionvalor,$uibModalInstance,SweetAlert,$filter, ngTableParams,ordenGastoLineasFactory) {

	$scope.noeditar=false;
	$scope.errorLimpio=false;

	$scope.init=function(){
    	$scope.npcertificacionvalor = npcertificacionvalor;
		if (editar == null) {
			//nuevo
			ordenGastoLineasFactory.nuevoLinea(
				ID
			).then(function(resp){
				//console.log(resp.json.ordengastolinea);
	        	$scope.objeto = resp.json.ordengastolinea;
	        	$scope.noeditar=false;
	        	$scope.cambioSubItems();
			})
		} else {
			//editar
			ordenGastoLineasFactory.editarLinea(
				editar
			).then(function(resp){
				//console.log(resp);
	        	$scope.objeto = resp.json.ordengastolinea;
	        	$scope.objetoDetalles = resp.json.subiteminfo;
				$scope.cargarListaCodigos();
	        	$scope.noeditar=true;
			})
		}
	}

	$scope.cambioSubItems=function(){
		//console.log($scope.objeto);
		$scope.listarSubItems = [];
		ordenGastoLineasFactory.listarSubItems(
			certificacionID // $scope.objeto.id.id
		).then(function(resp){
			if (resp.json.total.valor == 0) {
				$scope.errorLimpio=true;
				return;
			}
			$scope.listarSubItems = [{
        		id: "",
        		codigo: "",
        		npSubitem: "Selecione un subitem"
        	}];
			for (var i = 0; i < resp.json.result.length; i++) {
				resp.json.result[i].npSubitem = resp.json.result[i].npSubitemcodigo + ": " + resp.json.result[i].npSubitem; 
				$scope.listarSubItems.push(resp.json.result[i]);
			}
		})
	}

	$scope.obtenerTotal=function(){
		var i;
		for (i = 1; i < $scope.listarSubItems.length; i++) {
			if ($scope.listarSubItems[i].nivelactid == $scope.objeto.subitem)
				break;
		}
		if (i == $scope.listarSubItems.length)
			return;
		$scope.objeto.nivelactid = $scope.listarSubItems[i].nivelactid;
		$scope.objeto.npvalorcertificacion = $scope.listarSubItems[i].valor;
    	$scope.objeto.npsaldocertificacion = $scope.listarSubItems[i].npdisponible;
    	ordenGastoLineasFactory.obtenerTotal(
			$scope.listarSubItems[i].nivelactid
		).then(function(resp){
			//console.log(resp);
			$scope.objetoDetalles = resp.json.subiteminfo;
			$scope.cargarListaCodigos();
		})
	}

	$scope.cargarListaCodigos = function() {
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
			$scope.objeto.npsaldocertificacion += $scope.objeto.valor;
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
            	var tObj = Object.assign({}, $scope.objeto);
            	//tObj.nivelactid = tObj.subitem;
            	delete tObj.subitem;
            	ordenGastoLineasFactory.guardarLinea(tObj).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.objeto={};
	 		             tObj = {
 		            		 lineas: resp.json.ordengastolineas,
 		            		 valortotal: resp.json.ordengasto.valortotal
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

	$scope.cancelar=function() {
		$uibModalInstance.dismiss('cancel');
	}

	$scope.buscarSubtareas=function(id){
		for (var i = 0; i < $scope.listarSubtareas.length; i++) {
			if ($scope.listarSubtareas[i].id == id) {
				return i;
			}
		}
		return false;
	}
}]);
