'use strict';

app.controller('ModalCertificacionesFondosLineasController', [ "$scope","$rootScope","certificacionID","unidadID","$uibModalInstance","SweetAlert","$filter", "ngTableParams","certificacionesFondosFactory",
	function($scope,$rootScope,certificacionID,unidadID,$uibModalInstance,SweetAlert,$filter, ngTableParams,certificacionesFondosFactory) {

	$scope.init=function(){
		certificacionesFondosFactory.nuevoLinea(
			certificacionID
		).then(function(resp){
			console.log(resp.json.certificacionlinea);
        	$scope.objeto = resp.json.certificacionlinea;
    		certificacionesFondosFactory.listarSubtareas(
				$rootScope.ejefiscal,
				unidadID
			).then(function(resp){
	        	$scope.listarSubtareas = [{
	        		id: "",
	        		descripcionexten: "Selecione una subtarea"
	        	}].concat(resp.json.result);
				//console.log($scope.listarSubtareas);
			})
		})
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
		).then(function(resp){
        	$scope.listarItems = [{
        		id: "",
        		descripcionexten: "Selecione un item"
        	}].concat(resp.json.result);
			console.log($scope.listarItems);
		})
	}

	$scope.cambioSubItems=function(){
		certificacionesFondosFactory.listarSubItems(
			$rootScope.ejefiscal,
			$scope.objeto.item
		).then(function(resp){
        	$scope.listarSubItems = [{
        		id: "",
        		descripcionexten: "Selecione un subitem"
        	}].concat(resp.json.result);
			console.log($scope.listarItems);
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
            	certificacionesFondosFactory.guardarLinea($scope.objeto).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.objeto={};
	 		             $scope.limpiar();
	 		    		 $uibModalInstance.close(obj);		
	 		             SweetAlert.swal("Certificaciones de Fondos!", "Registro registrado satisfactoriamente!", "success");
        			 }else{
	 		             SweetAlert.swal("Certificaciones de Fondos!", resp.mensajes.msg, "error");
        			 }
        		})
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
