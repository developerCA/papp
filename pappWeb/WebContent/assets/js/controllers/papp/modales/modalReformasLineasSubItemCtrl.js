'use strict';

app.controller('ModalReformasLineasSubItemController', 
		[ "$scope","$rootScope","ID","unidadID","unidadcodigo","unidadnombre","editar","noeditar","itemId","subItemId","subItemTablarelacionid","$uibModalInstance","SweetAlert","$filter", "ngTableParams","reformasFactory",
	function($scope,$rootScope,ID,unidadID,unidadcodigo,unidadnombre,editar,noeditar,itemId,subItemId, subItemTablarelacionid,$uibModalInstance,SweetAlert,$filter, ngTableParams,reformasFactory) {

	$scope.noeditar = false;
	$scope.noeditar2 = false;

	$scope.init=function(){
		$scope.npunidadcodigo = unidadcodigo;
		$scope.npunidadnombre = unidadnombre;
		if (editar == null) {
			//nuevo
			reformasFactory.nuevoLinea(
				ID
			).then(function(resp){
	        	$scope.objeto = resp.json.reformalinea;
	        	$scope.noeditar = false;
	        	$scope.valorajustado = 0;
	        	$scope.saldo = 0;
			})
			$scope.cambioSubItems();
		} else {
			//editar
			reformasFactory.editarLinea(
				editar
			).then(function(resp){
	        	$scope.objeto = resp.json.reformalinea;
	        	$scope.objetoDetalles = resp.json.subiteminfo;
	        	try {
	            	$scope.valorajustado = $scope.objeto.npvalortotal;
				} catch (e) {
		        	$scope.valorajustado = 0;
				}
	        	try {
	        		$scope.saldo = $scope.objeto.npsaldo;
				} catch (e) {
		        	$scope.saldo = 0;
				}
	        	$scope.noeditar = true;
	        	$scope.noeditar2 = noeditar;
			})
		}
	}

	$scope.cambioSubItems=function(){
    	$scope.listarSubItems = [{
    		id: "",
    		descripcionexten: "Cargando subitems"
    	}];
    	$scope.valorajustado = 0;
    	$scope.saldo = 0;
		reformasFactory.listarSubItems(
			$rootScope.ejefiscal,
			itemId
		).then(function(resp){
			$scope.si = resp.json.result;
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
		reformasFactory.obtenerTotal(
			$scope.si[i].tablarelacionid,
			$scope.si[i].id
		).then(function(resp){
        	$scope.valorajustado = resp.json.valordisponiblesi.valorajustado;
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
            	var tObj = Object.assign({}, $scope.objeto);
            	if (tObj.nivelactid == 0) {
            	  tObj.nivelactid = tObj.subitem;
            	  //delete tObj.subitem;
            	}
            	if (tObj.valorincremento == null) tObj.valorincremento = 0;
            	if (tObj.valordecremento == null) tObj.valordecremento = 0;
            	if (parseInt($scope.saldo) < parseInt(tObj.valordecremento)) {
            		SweetAlert.swal(
            				"Reformas!",
            				"El valor decremento no puede ser mayor que el saldo",
            				"error"
    				);
            		return;
            	}
            	if (parseInt($scope.saldo) <= 0 && parseInt(tObj.valordecremento) > 0) {
            		SweetAlert.swal(
            				"Reformas!",
            				"Valor de saldo es cero no puede hacer un decremento",
            				"error"
    				);
            		return;
            	}
            	if (parseInt(tObj.valorincremento) == 0 && parseInt(tObj.valordecremento) == 0) {
            		SweetAlert.swal(
            				"Reformas!",
            				"Valor incremento o valor decremento tienen que tener valor uno de los dos",
            				"error"
    				);
            		return;
            	}
            	if (parseInt(tObj.valorincremento) != 0 && parseInt(tObj.valordecremento) != 0) {
            		SweetAlert.swal(
            				"Reformas!",
            				"Valor incremento o valor decremento solo uno de los dos puede tener valor",
            				"error"
    				);
            		return;
            	}
            	tObj.saldo = $scope.saldo;
            	reformasFactory.guardarLinea(tObj).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.objeto={};
	 		             tObj = {
 		            		reformalineas: resp.json.reformalineas
	 		             }
	 		             try {
	 		            	 tObj.valorincremento = resp.json.reforma.valorincremento;
						 } catch (e) {
							 tObj.valorincremento = 0;
						 	console.log("ERROR QUE REPARAR");
						 }
	 		             try {
	 		            	 tObj.valordecremento = resp.json.reforma.valordecremento;
						 } catch (e) {
							 tObj.valorincremento = 0;
						 	console.log("ERROR QUE REPARAR");
						 }
	 		    		 $uibModalInstance.close(tObj);		
        			 }else{
	 		             SweetAlert.swal("Reformas!", resp.mensajes.msg, "error");
        			 }
        		})
        		//console.log($scope.objeto);
            }
        },
        reset: function (form) {
    		$uibModalInstance.dismiss('cancel');
        }
    };
}]);
