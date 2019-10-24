'use strict';

app.controller('ModalReformasLineasDistMetaController', [ "$scope","$rootScope","ID","editar","noeditar","$uibModalInstance","SweetAlert","$filter", "ngTableParams","reformasFactory",
	function($scope,$rootScope,ID,editar,noeditar,$uibModalInstance,SweetAlert,$filter, ngTableParams,reformasFactory) {

	$scope.rol=function(nombre) {
		return ifRollPermiso(nombre);
	}

	$scope.noeditar = false;

	$scope.init=function(){
		//editar
		$scope.noeditar = noeditar;
		reformasFactory.editarLineaDistMeta(
			editar
		).then(function(resp){
//			console.log(resp);
        	$scope.objeto = resp.json.reformametasubtarea;
        	$scope.objetoDetalles = resp.json.subtareainfo;
		});
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
            	reformasFactory.guardarLineaSubtarea(tObj).then(function(resp){
	       			 if (!resp.estado){
		 		             SweetAlert.swal(
	 		            		 "Reformas!",
	 		            		 resp.mensajes.msg,
	 		            		 "error"
	            		 );
		 		             return;
	    			 }
    				 form.$setPristine(true);
 		             $scope.edicion=false;
// 		             $scope.objeto={};
// 		             tObj = {
//	            		reformalineas: resp.json.reformalineas
// 		             }
 		    		 $uibModalInstance.close(resp.json);
        		})
            }
        },
        reset: function (form) {
    		$uibModalInstance.dismiss('cancel');
        }
    };
}]);
