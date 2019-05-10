'use strict';

app.controller('ModalReformasALasMetasLineasController', [ "$scope","$rootScope","ID","unidadID","unidadcodigo","unidadnombre","editar","$uibModalInstance","SweetAlert","$filter", "ngTableParams","reformasALasMetasFactory",
	function($scope,$rootScope,ID,unidadID,unidadcodigo,unidadnombre,editar,$uibModalInstance,SweetAlert,$filter, ngTableParams,reformasALasMetasFactory) {

	$scope.noeditar = false;

	$scope.init=function(){
		$scope.npunidadcodigo = unidadcodigo;
		$scope.npunidadnombre = unidadnombre;
		if (editar == null) {
			//nuevo
			reformasALasMetasFactory.nuevoLinea(
				ID
			).then(function(resp){
//				console.log(resp.json.certificacionlinea);
	        	$scope.objeto = resp.json.reformametalinea;
	        	$scope.noeditar = false;
	        	$scope.valorajustado = 0;
	        	$scope.saldo = 0;
	        	reformasALasMetasFactory.listarSubtareas(
					$rootScope.ejefiscal,
					unidadID
				).then(function(resp){
					$scope.si = resp.json.result;
		        	$scope.listarSubtareas = [{
		        		id: "",
		        		descripcionexten: "Selecione una subtarea"
		        	}].concat(resp.json.result);
					//console.log($scope.listarSubtareas);
				})
			})
		} else {
			//editar
			reformasALasMetasFactory.editarLinea(
					editar
				).then(function(resp){
					$scope.si = resp.json.result;
					//console.log(resp);
		        	$scope.objeto = resp.json.reformametalinea;
		        	$scope.objetoDetalles = resp.json.subiteminfo;
//		        	try {
//			        	$scope.valorajustado = $scope.objeto.npvalortotal + $scope.objeto.npvalorincremento - $scope.objeto.npvalordecremento;
//					} catch (e) {
//			        	$scope.valorajustado = 0;
//					}
//		        	try {
//			        	$scope.saldo = $scope.objeto.npsaldo - $scope.objeto.npvalorincremento + $scope.objeto.npvalordecremento;
//					} catch (e) {
//			        	$scope.saldo = 0;
//					}
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
		$scope.objeto.nivelactid = $scope.listarSubtareas[i].id;
    	$scope.listarItems = [{
    		id: "",
    		descripcionexten: "Cargando items"
    	}];
    	$scope.listarSubItems = [{
    		id: "",
    		descripcionexten: "Pendiente"
    	}];
    	$scope.valorajustado = 0;
    	$scope.saldo = 0;
		reformasALasMetasFactory.obtenerDetalles(
			$scope.objeto.subtarea
		).then(function(resp){
			//console.log("RR:",resp);
        	$scope.objetoDetalles = resp.json.subtareainfo;
			//console.log($scope.objetoDetalles);
        	$scope.ponerCodigos();
		});
		$scope.obtenerTotal();
	}

	$scope.obtenerTotal=function(){
		var i;
		for (i = 0; i < $scope.si.length; i++) {
			if ($scope.si[i].id == $scope.objeto.subtarea)
				break;
		}
		if (i == $scope.si.length)
			return;
		reformasALasMetasFactory.obtenerTodoSubtarea(
			$scope.si[i].tablarelacionid
		).then(function(resp){
			//console.log(resp);
        	$scope.metadescripcion = resp.json.descripciones.metadescripcion;
        	$scope.unidadmedida = resp.json.descripciones.unidadmedida;
        	$scope.valoractual = resp.json.valoractual.valor;
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
            	if (tObj.npvalorincremento == null) tObj.npvalorincremento = 0;
            	if (tObj.npvalordecremento == null) tObj.npvalordecremento = 0;
            	if (parseInt(tObj.npvalorincremento) == 0 && parseInt(tObj.npvalordecremento) == 0) {
            		SweetAlert.swal(
            				"Reformas a las Metas!",
            				"El incremento o el decremento tienen que tener valor uno de los dos",
            				"error"
    				);
            		return;
            	}
            	if (parseInt(tObj.npvalorincremento) != 0 && parseInt(tObj.npvalordecremento) != 0) {
            		SweetAlert.swal(
            				"Reformas a las Metas!",
            				"El incremento o el decremento solo uno de los dos puede tener valor",
            				"error"
    				);
            		return;
            	}
            	if (parseInt(tObj.npvalordecremento) > $scope.valoractual) {
            		SweetAlert.swal(
            				"Reformas a las Metas!",
            				"El decremento no puede ser mayor al valor actual",
            				"error"
    				);
            		return;
            	}
            	reformasALasMetasFactory.guardarLinea(tObj).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.objeto={};
	 		             tObj = {
 		            		reformalineas: resp.json.reformalineas
	 		             }
//	 		             try {
//	 		            	 tObj.valorincremento = resp.json.reforma.valorincremento;
//						 } catch (e) {
//							 tObj.valorincremento = 0;
//						 	console.log("ERROR QUE REPARAR");
//						 }
//	 		             try {
//	 		            	 tObj.valordecremento = resp.json.reforma.valordecremento;
//						 } catch (e) {
//							 tObj.valorincremento = 0;
//						 	console.log("ERROR QUE REPARAR");
//						 }
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

	$scope.buscarSubtareas=function(id){
		for (var i = 0; i < $scope.listarSubtareas.length; i++) {
			if ($scope.listarSubtareas[i].id == id) {
				return i;
			}
		}
		return false;
	}
}]);
