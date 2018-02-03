'use strict';

app.controller('ModalContratoController', [ "$scope","$rootScope","$uibModalInstance","objetoFuente","SweetAlert","$filter","ngTableParams","contratoFactory",
	function($scope,$rootScope,$uibModalInstance,objetoFuente,SweetAlert,$filter,ngTableParams,contratoFactory) {

	$scope.editar = function() {
		
	}

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);
	};

	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};
}]);
