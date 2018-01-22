'use strict';

app.controller('ModalOrdenDevengoLineasController', [ "$scope","$rootScope","certificacionID","unidadID","editar","$uibModalInstance","SweetAlert","$filter", "ngTableParams","ordenDevengoLineasFactory",
	function($scope,$rootScope,certificacionID,unidadID,editar,$uibModalInstance,SweetAlert,$filter, ngTableParams,ordenDevengoLineasFactory) {

	$scope.noeditar=false;
	$scope.init=function(){
		if (editar == null) {
			//nuevo
			ordenDevengoLineasFactory.nuevoLinea(
				certificacionID
			).then(function(resp){
				console.log(resp.json.ordendevengolinea);
	        	$scope.objeto = resp.json.ordendevengolinea;
	        	$scope.noeditar=false;
	        	$scope.cambioSubItems();
			})
		} else {
			//editar
			ordenDevengoLineasFactory.editarLinea(
					editar
				).then(function(resp){
					//console.log(resp);
		        	$scope.objeto = resp.json.ordendevengolinea;
		        	$scope.objetoDetalles = resp.json.subiteminfo;
		        	$scope.noeditar=true;
				})
		}
	}

	$scope.cambioSubItems=function(){
		$scope.listarSubItems = [];
		//console.log($scope.objeto);
		ordenDevengoLineasFactory.listarSubItems(
			$scope.objeto.id.id
		).then(function(resp){
			$scope.si = resp.json.result;
        	$scope.listarSubItems = [{
        		id: "",
        		npSubitem: "Selecione un subitem"
        	}].concat(resp.json.result);
			//console.log($scope.listarItems);
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
		ordenDevengoLineasFactory.obtenerTotal(
			$scope.si[i].tablarelacionid
		).then(function(resp){
			//console.log(resp);
        	$scope.objeto.npvalor = resp.json.valordisponiblesi.saldo;
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
            	var tObj = Object.assign({}, $scope.objeto);
            	tObj.nivelactid = tObj.subitem;
            	delete tObj.subitem;
            	ordenDevengoLineasFactory.guardarLinea(tObj).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.objeto={};
	 		    		 $uibModalInstance.close(resp.json.ordendevengolineas);		
        			 }else{
	 		             SweetAlert.swal("Certificaciones de Fondos!", resp.mensajes.msg, "error");
        			 }
        		})
        		console.log($scope.objeto);
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
